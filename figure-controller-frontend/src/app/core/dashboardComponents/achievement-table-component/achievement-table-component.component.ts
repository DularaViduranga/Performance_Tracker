// import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { MyFiguresService, MyPerformanceResponseDTO } from '../../../services/myFigures.service';
// import { AuthService } from '../../../services/auth.service';
// import { Subject } from 'rxjs/internal/Subject';
// import { takeUntil } from 'rxjs/internal/operators/takeUntil';

// interface TableRow {
//   metric: string;
//   target: number;
//   achieved: number;
//   percentage: number;
//   status: 'excellent' | 'good' | 'warning' | 'critical';
// }

// @Component({
//   selector: 'app-achievement-table-component',
//   standalone: true,
//   imports: [CommonModule],
//   templateUrl: './achievement-table-component.component.html',
//   styleUrls: ['./achievement-table-component.component.scss']
// })
// export class AchievementTableComponentComponent implements OnInit {
//   currentMonth: string = '';
//   currentYear: number = new Date().getFullYear();
//   data: TableRow[] = [];
//   loading = true;
//   error = false;

//   private intermediaryCode: string = '';

//   // Map classCode → readable metric name
//   private classCodeMap: Record<string, string> = {
//     'MT': 'Motor Comprehensive',
//     'MC': 'Marine Cargo',
//     'MS': 'Miscellaneous',
//     'FI': 'Fire Insurance',
//     'EG': 'Engineering',
//     'MD': 'Medical',
//     // Add more as needed based on your actual class codes
//   };

//   constructor(
//     private myFiguresService: MyFiguresService,
//     private authService: AuthService
//   ) {}

//   private destroy$ = new Subject<void>();

//   ngOnDestroy() {
//     this.destroy$.next();
//     this.destroy$.complete();
//   }

//   ngOnInit(): void {
//     this.intermediaryCode = this.authService.getCurrentIntermediaryCode();

//     if (!this.intermediaryCode) {
//       console.error('No intermediary code found');
//       this.error = true;
//       this.loading = false;
//       return;
//     }

//     const today = new Date();
//     this.currentMonth = today.toLocaleString('default', { month: 'long' });
//     this.currentYear = today.getFullYear();

//     const year = this.currentYear.toString();
//     const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01-12

//     console.log(year, month);
//     this.myFiguresService.getMyPerformance(this.intermediaryCode, year, month)
//       .pipe(takeUntil(this.destroy$))
//       .subscribe({
//         next: (response: MyPerformanceResponseDTO[]) => {
//           this.data = response.map(item => ({
//             metric: this.classCodeMap[item.classCode] || `Class ${item.classCode}`,
//             target: item.target,
//             achieved: item.achieved,
//             percentage: item.percentage,
//             status: this.getStatusFromPercentage(item.percentage)
//           }));

//           // Sort by achieved descending (optional)
//           this.data.sort((a, b) => b.achieved - a.achieved);

//           console.log('Monthly achievements loaded successfully:', this.data);

//           this.loading = false;
//         },
//         error: (err) => {
//           console.error('Error loading monthly achievements:', err);
//           this.error = true;
//           this.loading = false;
//         }
//       });
//   }

//   private getStatusFromPercentage(percentage: number): 'excellent' | 'good' | 'warning' | 'critical' {
//     if (percentage >= 95) return 'excellent';
//     if (percentage >= 85) return 'good';
//     if (percentage >= 70) return 'warning';
//     return 'critical';
//   }

//   formatCurrency(val: number): string {
//     return new Intl.NumberFormat('en-LK', {
//       style: 'currency',
//       currency: 'LKR',
//       maximumSignificantDigits: 3,
//       notation: 'compact'
//     }).format(val);
//   }

//   getStatusClass(status: string): string {
//     const map = {
//       excellent: 'bg-emerald-100 text-emerald-700 border-emerald-200',
//       good: 'bg-blue-100 text-blue-700 border-blue-200',
//       warning: 'bg-amber-100 text-amber-700 border-amber-200',
//       critical: 'bg-rose-100 text-rose-700 border-rose-200',
//     };
//     return map[status as keyof typeof map] || 'bg-slate-100 text-slate-700';
//   }

//   getProgressClass(status: string): string {
//     const map = {
//       excellent: 'bg-emerald-500',
//       good: 'bg-blue-500',
//       warning: 'bg-amber-500',
//       critical: 'bg-rose-500',
//     };
//     return map[status as keyof typeof map] || 'bg-slate-300';
//   }
// }
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyFiguresService, MyPerformanceResponseDTO } from '../../../services/myFigures.service';
import { AuthService } from '../../../services/auth.service';
import { Subject, of } from 'rxjs';
import { takeUntil, catchError, finalize } from 'rxjs/operators';

interface TableRow {
  metric: string;
  target: number;
  achieved: number;
  percentage: number;
  status: 'excellent' | 'good' | 'warning' | 'critical';
}

@Component({
  selector: 'app-achievement-table-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './achievement-table-component.component.html',
  styleUrls: ['./achievement-table-component.component.scss']
})
export class AchievementTableComponentComponent implements OnInit, OnDestroy {

  currentMonth = '';
  currentYear = new Date().getFullYear();

  data: TableRow[] = [];
  loading = true;
  error = false;

  private intermediaryCode = '';
  private destroy$ = new Subject<void>();

  // classCode → readable name
  private classCodeMap: Record<string, string> = {
    MT: 'Motor Comprehensive',
    MC: 'Marine Cargo',
    MS: 'Miscellaneous',
    FI: 'Fire Insurance',
    EG: 'Engineering',
    MD: 'Medical',
  };

  constructor(
    private myFiguresService: MyFiguresService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.intermediaryCode = this.authService.getCurrentIntermediaryCode();

    // 🚫 No auth → don’t crash dashboard
    if (!this.intermediaryCode) {
      console.warn('No intermediary code found. Skipping API call.');
      this.loading = false;
      this.data = [];
      return;
    }

    const today = new Date();
    this.currentMonth = today.toLocaleString('default', { month: 'long' });
    this.currentYear = today.getFullYear();

    const year = this.currentYear.toString();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');

    this.myFiguresService
      .getMyPerformance(this.intermediaryCode, year, month)
      .pipe(
        takeUntil(this.destroy$),

        // ✅ HARD stop API errors from killing the route
        catchError(err => {
          console.error('Failed to load achievements:', err);
          this.error = true;
          return of([] as MyPerformanceResponseDTO[]);
        }),

        // ✅ Always stop loading spinner
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe(response => {
        this.data = response.map(item => ({
          metric: this.classCodeMap[item.classCode] || `Class ${item.classCode}`,
          target: item.target,
          achieved: item.achieved,
          percentage: item.percentage,
          status: this.getStatusFromPercentage(item.percentage)
        }));

        this.data.sort((a, b) => b.achieved - a.achieved);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private getStatusFromPercentage(
    percentage: number
  ): 'excellent' | 'good' | 'warning' | 'critical' {
    if (percentage >= 95) return 'excellent';
    if (percentage >= 85) return 'good';
    if (percentage >= 70) return 'warning';
    return 'critical';
  }

  formatCurrency(val: number): string {
    return new Intl.NumberFormat('en-LK', {
      style: 'currency',
      currency: 'LKR',
      maximumSignificantDigits: 3,
      notation: 'compact'
    }).format(val);
  }

  getStatusClass(status: string): string {
    const map = {
      excellent: 'bg-emerald-100 text-emerald-700 border-emerald-200',
      good: 'bg-blue-100 text-blue-700 border-blue-200',
      warning: 'bg-amber-100 text-amber-700 border-amber-200',
      critical: 'bg-rose-100 text-rose-700 border-rose-200',
    };
    return map[status as keyof typeof map] || 'bg-slate-100 text-slate-700';
  }

  getProgressClass(status: string): string {
    const map = {
      excellent: 'bg-emerald-500',
      good: 'bg-blue-500',
      warning: 'bg-amber-500',
      critical: 'bg-rose-500',
    };
    return map[status as keyof typeof map] || 'bg-slate-300';
  }
}
