import { Component } from '@angular/core';
import { DateSelectorComponentComponent } from '../../core/components/gwp/date-selector-component/date-selector-component.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MyCancellationResponseDTO,MyFiguresService } from '../../services/myFigures.service';
import { Router } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { GenericTableComponent, TableConfig } from '../../core/components/shared/generic-table/generic-table.component';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-my-cancellation-page',
  imports: [
    DateSelectorComponentComponent,
    CommonModule,
    MatButtonModule,
    MatIconModule,
    GenericTableComponent,
    MatProgressSpinnerModule
  ],
  templateUrl: './my-cancellation-page.component.html',
  styleUrl: './my-cancellation-page.component.scss'
})
export class MyCancellationPageComponent {
  fromDate: Date | null = new Date();
  toDate: Date | null = new Date();
  dataSource: MyCancellationResponseDTO[] = [];
  isLoading = false;

  tableConfig: TableConfig = {
      columns: [
      { key: 'policyNo', header: 'Policy No', sortable: true },
      { key: 'classDesc', header: 'Class Description', sortable: true },
      { key: 'prodDesc', header: 'Product Description', sortable: true },
      { key: 'customerName', header: 'Customer Name', sortable: true },
      { key: 'phoneNumber', header: 'Phone Number', sortable: true },
      { key: 'createdDate', header: 'Created Date', type: 'date', sortable: true },
      { key: 'periodFrom', header: 'Start', type: 'date', sortable: true },
      { key: 'periodTo', header: 'End', type: 'date', sortable: true },
      { key: 'branchName', header: 'Branch', sortable: true },
      ],
      showPagination: false,
      showFooter: false,
      showFilter: false,  // Show totals for these columns
      emptyMessage: 'No cancellation data available',
      emptyIcon: 'cancel'};

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
    console.log('Fetching cancellation data from', start, 'to', end, 'for intermediaryCode:', intermediaryCode);
    this.isLoading = true;

    this.myFiguresService.getMyCancellation( start, end,intermediaryCode)
      .subscribe({
        next: (data) => {
          this.dataSource = data;
          console.log('Cancellation data:', data);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching cancellation data:', err);
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

}
