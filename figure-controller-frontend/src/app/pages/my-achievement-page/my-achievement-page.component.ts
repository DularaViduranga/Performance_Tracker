// import { MyFiguresService, MyPerformanceResponseDTO } from '../../services/myFigures.service';
// import { YearandmonthselectorComponentComponent } from '../../core/components/performance/yearandmonthselector-component/yearandmonthselector-component.component';
// import { CommonModule } from '@angular/common';
// import { Component, OnInit } from '@angular/core';
// import { MatIconModule } from '@angular/material/icon';
// import { MatButtonModule } from '@angular/material/button';
// import { GenericTableComponent, TableConfig } from '../../core/components/shared/generic-table/generic-table.component';
// import { UserService } from '../../services/user.service';
// import { AuthService } from '../../services/auth.service';

// @Component({
//   selector: 'app-my-achievement-page',
//   standalone: true,
//   imports: [
//     YearandmonthselectorComponentComponent,
//     CommonModule,
//     MatIconModule,
//     MatButtonModule,
//     GenericTableComponent
//   ],
//   templateUrl: './my-achievement-page.component.html',
//   styleUrls: ['./my-achievement-page.component.scss']
// })
// export class MyAchievementPageComponent implements OnInit {
//   performanceData: MyPerformanceResponseDTO[] = [];
//   isLoading = false;

//   tableConfig: TableConfig = {
//     columns: [
//       { key: 'classCode', header: 'Class Code', sortable: true },
//       { key: 'target', header: 'Target', sortable: true },
//       { key: 'achieved', header: 'Achieved', sortable: true },
//       {
//         key: 'percentage',
//         header: 'Percentage',
//         sortable: true,
//         format: (value: number) => value ? `${value.toFixed(1)}%` : '0.0%'
//       },
//     ],
//     showPagination: false,
//     showFooter: false,
//     showFilter: false,
//     emptyMessage: 'No achievement records found',
//     emptyIcon: 'emoji_events'
//   };


//   constructor(
//     private userService: AuthService,
//     private myFiguresService: MyFiguresService
//   ) {}

//   selectedYear: number | null = new Date().getFullYear();
//   selectedMonth: string | null = ('0' + (new Date().getMonth() + 1)).slice(-2);


//   ngOnInit() {
//     // Initialize with current date but don't load data automatically
//     // The month selector will show the current month by default
//   }

//   onDateRangeChange(event: { fromDate: Date | null; toDate: Date | null }) {
//     if (event.fromDate) {
//       // Extract year and month from the fromDate
//       this.selectedYear = event.fromDate.getFullYear();
//       this.selectedMonth = ('0' + (event.fromDate.getMonth() + 1)).slice(-2);

//       // Don't auto-load, wait for Search button click
//     } else {
//       // If no date selected, clear the selection
//       this.selectedYear = null;
//       this.selectedMonth = null;
//     }
//   }

//   onSearch() {
//     // Load performance data when Search button is clicked
//     if (!this.selectedYear || !this.selectedMonth) {
//       console.warn('Year or Month not selected. Skipping API call.');
//       this.performanceData = [];
//       return;
//     }
//     this.isLoading = true;
//     console.log('Year:', this.selectedYear, 'Month:', this.selectedMonth);

//     // Get intermediaryCode dynamically when needed
//     this.myFiguresService
//       .getMyPerformance(
//         this.userService.getCurrentIntermediaryCode(),
//         this.selectedYear.toString(),
//         this.selectedMonth
//       )
//       .subscribe({
//         next: (data) => {
//           this.performanceData = data;
//           console.log('Performance data:', data);
//           this.isLoading = false;
//         },
//         error: (err) => {
//           console.error('Error loading performance data:', err);
//           this.performanceData = [];
//           this.isLoading = false;
//         }
//       });
//   }

//   onReset() {
//     this.selectedYear = new Date().getFullYear();
//     this.selectedMonth = ('0' + (new Date().getMonth() + 1)).slice(-2);
//     this.performanceData = [];
//   }
// }
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { GenericTableComponent, TableConfig } from '../../core/components/shared/generic-table/generic-table.component';
import { MyFiguresService, MyPerformanceResponseDTO } from '../../services/myFigures.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-my-achievement-page',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    GenericTableComponent
  ],
  templateUrl: './my-achievement-page.component.html',
  styleUrls: ['./my-achievement-page.component.scss']
})
export class MyAchievementPageComponent implements OnInit {

  performanceData: MyPerformanceResponseDTO[] = [];
  isLoading = false;

  selectedYear: number | null = null;
  selectedMonth: string | null = null;

  availableYears: number[] = [
    2030, 2029, 2028, 2027, 2026, 2025,
    2024, 2023, 2022, 2021, 2020,
  ];
  availableMonths = [
    { value: '01', label: 'January' },
    { value: '02', label: 'February' },
    { value: '03', label: 'March' },
    { value: '04', label: 'April' },
    { value: '05', label: 'May' },
    { value: '06', label: 'June' },
    { value: '07', label: 'July' },
    { value: '08', label: 'August' },
    { value: '09', label: 'September' },
    { value: '10', label: 'October' },
    { value: '11', label: 'November' },
    { value: '12', label: 'December' }
  ];

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
      }
    ],
    showPagination: false,
    showFooter: false,
    showFilter: false,
    emptyMessage: 'No achievement records found',
    emptyIcon: 'emoji_events'
  };

  constructor(
    private authService: AuthService,
    private myFiguresService: MyFiguresService
  ) {}

  ngOnInit(): void {
    const now = new Date();

    this.selectedYear = now.getFullYear();
    this.selectedMonth = (now.getMonth() + 1).toString().padStart(2, '0');

    // If current year is outside 2020–2030, reset to 2020
    if (!this.availableYears.includes(this.selectedYear)) {
      this.selectedYear = 2020;
    }
  }

  onSearch(): void {
    if (!this.selectedYear || !this.selectedMonth) {
      return;
    }

    this.isLoading = true;

    this.myFiguresService
      .getMyPerformance(
        this.authService.getCurrentIntermediaryCode(),
        this.selectedYear.toString(),
        this.selectedMonth
      )
      .subscribe({
        next: (data) => {
          this.performanceData = data;
          this.isLoading = false;
        },
        error: () => {
          this.performanceData = [];
          this.isLoading = false;
        }
      });
  }

  onReset(): void {
    const now = new Date();
    this.selectedYear = now.getFullYear();
    this.selectedMonth = (now.getMonth() + 1).toString().padStart(2, '0');
    this.performanceData = [];
  }
}
