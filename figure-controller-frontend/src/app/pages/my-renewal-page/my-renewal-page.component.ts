import { Component } from '@angular/core';
import { DateSelectorComponentComponent } from '../../core/components/gwp/date-selector-component/date-selector-component.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MyFiguresService, MyRenewalResponseDTO } from '../../services/myFigures.service';
import { Router } from '@angular/router';
import { GenericTableComponent, TableConfig,TableColumn } from '../../core/components/shared/generic-table/generic-table.component';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-my-renewal-page',
  imports: [
    DateSelectorComponentComponent,
    GenericTableComponent,
    CommonModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './my-renewal-page.component.html',
  styleUrl: './my-renewal-page.component.scss'
})
export class MyRenewalPageComponent {
  fromDate: Date | null = new Date();
  toDate: Date | null = new Date();
  dataSource: MyRenewalResponseDTO[] = [];
  isLoading = false;

  // Table Configuration
  tableConfig: TableConfig = {
    columns: [
      { key: 'policyNo', header: 'Policy No', sortable: true },
      { key: 'phone', header: 'Phone', sortable: true },
      { key: 'classDesc', header: 'Class Description', sortable: true },
      { key: 'prodDesc', header: 'Product Description', sortable: true },
      { key: 'customerName', header: 'Customer Name', sortable: true },
      { key: 'createdDate', header: 'Created Date', type: 'date', sortable: true },
      { key: 'periodFrom', header: 'Period From', type: 'date', sortable: true },
      { key: 'periodTo', header: 'Period To', type: 'date', sortable: true },
      { key: 'branchName', header: 'Branch Name', sortable: true }
    ],
    showPagination: false,
    showFooter: false,
    showFilter: false,
    emptyMessage: 'No renewal records found',
    emptyIcon: 'autorenew',
    rowClass: (row) => {
      if (this.isToBeRenewed(row.periodTo)) {
        return 'to-be-renewed-row';
      }
      return '';
    }
  };

  constructor(
    private myFiguresService : MyFiguresService,
    private userService: AuthService,
    private router: Router
  ) {

  }

  onDateRangeChange(dateRange: { fromDate: Date | null; toDate: Date | null }) {
    this.fromDate = dateRange.fromDate;
    this.toDate = dateRange.toDate;
    console.log('Date range changed:', dateRange);
  }


  isToBeRenewed(periodTo: string): boolean {
    if (!periodTo) return false;

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const periodToDate = new Date(periodTo);
    periodToDate.setHours(0, 0, 0, 0);

    return periodToDate >= today;
  }


  onSearch() {
    if (!this.fromDate || !this.toDate) return;

    const intermediaryCode = this.userService.getCurrentIntermediaryCode(); // Replace with logged-in user later
    const start = this.fromDate.toDateString();
    const end = this.toDate.toDateString();
    console.log('Fetching renewal data from', start, 'to', end, 'for intermediaryCode:', intermediaryCode);
    this.isLoading = true;

    // Use mock data instead of API call for testing
    // setTimeout(() => {
    //   this.dataSource = this.mockData;
    //   console.log('Renewal data (mock):', this.dataSource);
    //   this.isLoading = false;
    // }, 3000);

    this.myFiguresService.getMyRenewal( start, end,intermediaryCode)
      .subscribe({
        next: (data) => {
          this.dataSource = data;
          console.log('Renewal data:', data);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching renewal data:', err);
          this.dataSource = [];
          this.isLoading = false;
        }
      });
  }

  onReset() {
    this.fromDate = new Date();
    this.toDate = new Date();
    this.dataSource = [];
  }
  navigateToTest() {
    this.router.navigate(['/renewal-test-page']);
  }
}
