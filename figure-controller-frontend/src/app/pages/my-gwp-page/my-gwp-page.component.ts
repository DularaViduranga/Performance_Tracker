import { Component } from '@angular/core';
import { DateSelectorComponentComponent } from '../../core/components/gwp/date-selector-component/date-selector-component.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MyFiguresService, MyGWPResponseDTO } from '../../services/myFigures.service';
import { Router } from '@angular/router';
import { GenericTableComponent, TableConfig } from '../../core/components/shared/generic-table/generic-table.component';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-my-gwp-page',
  imports: [
    DateSelectorComponentComponent,
    GenericTableComponent,
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './my-gwp-page.component.html',
  styleUrl: './my-gwp-page.component.scss'
})
export class MyGWPPageComponent {
  fromDate: Date | null = new Date();
  toDate: Date | null = new Date();
  initialFromDate: Date = new Date();  // Store initial values
  initialToDate: Date = new Date();
  dataSource: MyGWPResponseDTO[] = [];
  isLoading = false;

  tableConfig: TableConfig = {
  columns: [
    { key: 'policyNumber', header: 'Policy Number', sortable: true, width: '200px' },
    { key: 'basic', header: 'Basic', type: 'currency', align: 'right', sortable: true },
    { key: 'srcc', header: 'SRCC', type: 'currency', align: 'right', sortable: true },
    { key: 'tc', header: 'TC', type: 'currency', align: 'right', sortable: true }
  ],
  showPagination: false,
  showFilter: false,
  showFooter: true,
  footerColumns: ['basic', 'srcc', 'tc'],  // Show totals for these columns
  emptyMessage: 'No GWP data available',
  emptyIcon: 'work'};

  constructor(
    private myFiguresService : MyFiguresService,
    private router: Router,
    private userService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  onDateRangeChange(dateRange: { fromDate: Date | null; toDate: Date | null }) {
    this.fromDate = dateRange.fromDate;
    this.toDate = dateRange.toDate;
    console.log('Date range changed:', dateRange);
  }


  private formatDate(date: Date): string {
    const day = ('0' + date.getDate()).slice(-2);
    const monthShortNames = ["JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"];
    const month = monthShortNames[date.getMonth()];
    const year = date.getFullYear().toString().slice(-2);
    return `${day}-${month}-${year}`;
  }

  onSearch() {
    if (!this.fromDate || !this.toDate) return;

    const intermediaryCode = this.userService.getCurrentIntermediaryCode(); // Replace with logged-in user later
    const start = this.fromDate.toDateString();
    const end = this.toDate.toDateString();
    console.log('Fetching GWP from', start, 'to', end, 'for intermediaryCode:', intermediaryCode);
    this.isLoading = true;

    this.myFiguresService.getMyGWP(intermediaryCode, start, end)
      .subscribe({
        next: (data) => {
          this.dataSource = data;
          console.log('GWP Data:', data);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching GWP:', err);
          this.dataSource = [];
          this.isLoading = false;
        }
      });
  }


  navigateToDetailed() {

  // Validate that both dates are selected
  if (!this.fromDate || !this.toDate) {
    this.snackBar.open('Please select both From Date and To Date before viewing detailed report.', 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
    return;
  }

  // Check if dates haven't been changed from initial values
    if (this.fromDate.getTime() === this.initialFromDate.getTime() &&
        this.toDate.getTime() === this.initialToDate.getTime()) {
      this.snackBar.open('Please select date range before viewing detailed report.', 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
    return;
  }


  const intermediaryCode = 'F8858';
  const start = this.fromDate.toDateString();
  const end = this.toDate.toDateString();

  this.router.navigate(
    ['/my-gwp/detailed'],
    {
      queryParams: {
        start,
        end,
        intermediaryCode
      }
    }
  );
  }

  onReset() {
    this.fromDate = new Date();
    this.toDate = new Date();
    this.initialFromDate = new Date();  // Reset initial values too
    this.initialToDate = new Date();
    this.dataSource = [];
  }

}
