import { Component } from '@angular/core';
import { DateSelectorComponentComponent } from '../../core/components/gwp/date-selector-component/date-selector-component.component';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MyFiguresService, MyNewBuisnessCountsByTransactionTypesDTO, MyNewBuisnessDTO } from '../../services/myFigures.service';
import { Router } from '@angular/router';
import { GenericTableComponent, TableConfig } from '../../core/components/shared/generic-table/generic-table.component';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-my-business-page',
  imports: [
    DateSelectorComponentComponent,
    CommonModule,
    MatButtonModule,
    MatIconModule,
    GenericTableComponent
],
  templateUrl: './my-business-page.component.html',
  styleUrl: './my-business-page.component.scss'
})
export class MyBusinessPageComponent {
  fromDate: Date | null = new Date();
  toDate: Date | null = new Date();
  dataSource: MyNewBuisnessDTO[] = [];
  dataSource2: MyNewBuisnessCountsByTransactionTypesDTO[] = [];
  isLoading = false;

  tableConfig: TableConfig = {
  columns: [
    { key: 'policyNo', header: 'Policy No', sortable: true },
    { key: 'classDesc', header: 'Class Description', sortable: true },
    { key: 'prodDesc', header: 'Product Description', sortable: true },
    { key: 'customerName', header: 'Customer Name', sortable: true },
    { key: 'phoneNumber', header: 'Phone Number', sortable: true },
    { key: 'createdDate', header: 'Created Date', type: 'date', sortable: true },
    { key: 'periodFrom', header: 'Period From', type: 'date', sortable: true },
    { key: 'periodTo', header: 'Period To', type: 'date', sortable: true },
    { key: 'branchName', header: 'Branch Name', sortable: true },
    { key: 'status', header: 'Status', sortable: true },
    { key: 'transactionDesc', header: 'Transaction', sortable: true }
  ],
  showPagination: false,
  showFooter: false,
  showFilter: false,
  emptyMessage: 'No business records found',
  emptyIcon: 'business_center',
  rowClass: (row) => row.status === 'POLICY CANCELLED' ? 'cancelled-row' : ''};

  tableConfig2: TableConfig = {
  columns: [
    { key: 'type', header: 'Type', sortable: true },
    { key: 'count', header: 'Count', sortable: true },
  ],
  showPagination: false,
  showFooter: false,
  showFilter: false,
  emptyMessage: 'No business records found',
  emptyIcon: 'business_center'};


  constructor(
    private myFiguresService : MyFiguresService,
    private userService: AuthService,
    private router: Router
  ) {}

  onDateRangeChange(dateRange: { fromDate: Date | null; toDate: Date | null }) {
    this.fromDate = dateRange.fromDate;
    this.toDate = dateRange.toDate;
    console.log('Date range changed:', dateRange);
  }


  onSearch() {
    if (!this.fromDate || !this.toDate) return;

    const intermediaryCode = this.userService.getCurrentIntermediaryCode(); // Replace with logged-in user later
    const start = this.fromDate.toDateString();
    const end = this.toDate.toDateString();
    console.log('Fetching new business data from', start, 'to', end, 'for intermediaryCode:', intermediaryCode);
    this.isLoading = true;

    this.myFiguresService.getMyNewBuisness( start, end,intermediaryCode)
      .subscribe({
        next: (data) => {
          this.dataSource = data;
          console.log('New business data:', data);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching new business data:', err);
          this.dataSource = [];
          this.isLoading = false;
        }
      });


    this.myFiguresService.getMyNewBuisnessCountsByTransactionTypes( start, end,intermediaryCode)
      .subscribe({
        next: (data) => {
          this.dataSource2 = data;
          console.log('New business counts by transaction types data:', data);
        },
        error: (err) => {
          console.error('Error fetching new business counts by transaction types data:', err);
          this.dataSource2 = [];
        }
      });

  }

  onReset() {
    this.fromDate = new Date();
    this.toDate = new Date();
    this.dataSource = [];
    this.dataSource2 = [];
  }
}
