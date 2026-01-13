import { MyFiguresService, MyPerformanceResponseDTO } from '../../services/myFigures.service';
import { YearandmonthselectorComponentComponent } from '../../core/components/performance/yearandmonthselector-component/yearandmonthselector-component.component';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { GenericTableComponent, TableConfig } from '../../core/components/shared/generic-table/generic-table.component';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-my-achievement-page',
  standalone: true,
  imports: [
    YearandmonthselectorComponentComponent,
    CommonModule,
    MatIconModule,
    MatButtonModule,
    GenericTableComponent
  ],
  templateUrl: './my-achievement-page.component.html',
  styleUrls: ['./my-achievement-page.component.scss']
})
export class MyAchievementPageComponent implements OnInit {
  performanceData: MyPerformanceResponseDTO[] = [];
  isLoading = false;

  tableConfig: TableConfig = {
    columns: [
      { key: 'classCode', header: 'Class Code', sortable: true },
      { key: 'target', header: 'Target', sortable: true },
      { key: 'achieved', header: 'Achieved', sortable: true },
      {
        key: 'percentage',
        header: 'Percentage',
        sortable: true,
        format: (value: number) => value ? `${value.toFixed(1)}%` : '0.0%'
      },
    ],
    showPagination: false,
    showFooter: false,
    showFilter: false,
    emptyMessage: 'No achievement records found',
    emptyIcon: 'emoji_events'
  };


  constructor(
    private userService: AuthService,
    private myFiguresService: MyFiguresService
  ) {}

  selectedYear: number | null = new Date().getFullYear();
  selectedMonth: string | null = ('0' + (new Date().getMonth() + 1)).slice(-2);


  ngOnInit() {
    // Initialize with current date but don't load data automatically
    // The month selector will show the current month by default
  }

  onDateRangeChange(event: { fromDate: Date | null; toDate: Date | null }) {
    if (event.fromDate) {
      // Extract year and month from the fromDate
      this.selectedYear = event.fromDate.getFullYear();
      this.selectedMonth = ('0' + (event.fromDate.getMonth() + 1)).slice(-2);

      // Don't auto-load, wait for Search button click
    } else {
      // If no date selected, clear the selection
      this.selectedYear = null;
      this.selectedMonth = null;
    }
  }

  onSearch() {
    // Load performance data when Search button is clicked
    if (!this.selectedYear || !this.selectedMonth) {
      console.warn('Year or Month not selected. Skipping API call.');
      this.performanceData = [];
      return;
    }
    this.isLoading = true;
    console.log('Year:', this.selectedYear, 'Month:', this.selectedMonth);

    // Get intermediaryCode dynamically when needed
    this.myFiguresService
      .getMyPerformance(
        this.userService.getCurrentIntermediaryCode(),
        this.selectedYear.toString(),
        this.selectedMonth
      )
      .subscribe({
        next: (data) => {
          this.performanceData = data;
          console.log('Performance data:', data);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading performance data:', err);
          this.performanceData = [];
          this.isLoading = false;
        }
      });
  }

  onReset() {
    this.selectedYear = new Date().getFullYear();
    this.selectedMonth = ('0' + (new Date().getMonth() + 1)).slice(-2);
    this.performanceData = [];
  }
}
