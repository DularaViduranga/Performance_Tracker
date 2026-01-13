import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatCardComponentComponent } from '../stat-card-component/stat-card-component.component';
import { MyFiguresService, MyPerformanceResponseDTO } from '../../../services/myFigures.service';
import { AuthService } from '../../../services/auth.service';
import { forkJoin, Subject, of } from 'rxjs';
import { takeUntil, catchError } from 'rxjs/operators';

interface Stat {
  current: number;
  previous: number;
  change: number;
}

@Component({
  selector: 'app-hero-stats-component',
  standalone: true,
  imports: [CommonModule, StatCardComponentComponent],
  templateUrl: './hero-stats-component.component.html',
  styleUrls: ['./hero-stats-component.component.scss']
})
export class HeroStatsComponentComponent implements OnInit, OnDestroy {

  gwp: Stat = { current: 0, previous: 0, change: 0 };
  cashCollection: Stat = { current: 0, previous: 0, change: 0 };
  renewals: Stat = { current: 0, previous: 0, change: 0 };
  newBusiness: Stat = { current: 0, previous: 0, change: 0 };

  loading = true;
  error = false;

  private intermediaryCode = '';
  private destroy$ = new Subject<void>();

  constructor(
    private myFigureService: MyFiguresService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.intermediaryCode = this.authService.getCurrentIntermediaryCode();

    if (!this.intermediaryCode) {
      this.error = true;
      this.loading = false;
      return;
    }

    this.loadAllStats();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadAllStats(): void {
    const today = new Date();
    const currentYear = today.getFullYear();
    const currentMonth = today.getMonth();

    // ⚠️ DATE FORMAT KEPT AS-IS (backend dependency)
    const formatDate = (date: Date): string => {
      const options: Intl.DateTimeFormatOptions = {
        weekday: 'short',
        month: 'short',
        day: '2-digit',
        year: 'numeric'
      };
      return date.toLocaleDateString('en-US', options).replace(/,/g, '');
    };

    const currentStartDate = new Date(currentYear, currentMonth, 1);
    const currentEndDate = new Date(currentYear, currentMonth + 1, 0);
    const currentStart = formatDate(currentStartDate);
    const currentEnd = formatDate(currentEndDate);

    const prevMonth = currentMonth === 0 ? 11 : currentMonth - 1;
    const prevYear = currentMonth === 0 ? currentYear - 1 : currentYear;
    const prevStartDate = new Date(prevYear, prevMonth, 1);
    const prevEndDate = new Date(prevYear, prevMonth + 1, 0);
    const prevStart = formatDate(prevStartDate);
    const prevEnd = formatDate(prevEndDate);

    forkJoin({
      currGwp: this.myFigureService.getPerformanceByPeriod(this.intermediaryCode, currentStart, currentEnd)
        .pipe(catchError(() => of([]))),
      prevGwp: this.myFigureService.getPerformanceByPeriod(this.intermediaryCode, prevStart, prevEnd)
        .pipe(catchError(() => of([]))),

      currCash: this.myFigureService.getMyCashCollection(this.intermediaryCode, currentStart, currentEnd)
        .pipe(catchError(() => of([]))),
      prevCash: this.myFigureService.getMyCashCollection(this.intermediaryCode, prevStart, prevEnd)
        .pipe(catchError(() => of([]))),

      currRenewal: this.myFigureService.getMyRenewal(currentStart, currentEnd, this.intermediaryCode)
        .pipe(catchError(() => of([]))),
      prevRenewal: this.myFigureService.getMyRenewal(prevStart, prevEnd, this.intermediaryCode)
        .pipe(catchError(() => of([]))),

      currNewBiz: this.myFigureService.getMyNewBuisness(currentStart, currentEnd, this.intermediaryCode)
        .pipe(catchError(() => of([]))),
      prevNewBiz: this.myFigureService.getMyNewBuisness(prevStart, prevEnd, this.intermediaryCode)
        .pipe(catchError(() => of([]))),
    })
    .pipe(takeUntil(this.destroy$))
    .subscribe({
      next: res => {
        this.gwp.current = this.sumAchieved(res.currGwp);
        this.gwp.previous = this.sumAchieved(res.prevGwp);
        this.gwp.change = this.calculateChange(this.gwp.current, this.gwp.previous);

        this.cashCollection.current = this.sumSettled(res.currCash);
        this.cashCollection.previous = this.sumSettled(res.prevCash);
        this.cashCollection.change = this.calculateChange(this.cashCollection.current, this.cashCollection.previous);

        this.renewals.current = res.currRenewal.length;
        this.renewals.previous = res.prevRenewal.length;
        this.renewals.change = this.calculateChange(this.renewals.current, this.renewals.previous);

        this.newBusiness.current = res.currNewBiz.length;
        this.newBusiness.previous = res.prevNewBiz.length;
        this.newBusiness.change = this.calculateChange(this.newBusiness.current, this.newBusiness.previous);

        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  private sumAchieved(data: MyPerformanceResponseDTO[]): number {
    return data.reduce((sum, i) => sum + i.achieved, 0);
  }

  private sumSettled(data: any[]): number {
    return data.reduce((sum, i) => sum + i.settledAmount, 0);
  }

  private calculateChange(current: number, previous: number): number {
    if (previous === 0) return current > 0 ? 100 : 0;
    return ((current - previous) / previous) * 100;
  }
}
