import { Component } from '@angular/core';
import { DateSelectorComponentComponent } from '../../core/components/gwp/date-selector-component/date-selector-component.component';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MyCashCollectionDTO, MyFiguresService } from '../../services/myFigures.service';
import { Router } from '@angular/router';
import { GenericTableComponent, TableConfig } from '../../core/components/shared/generic-table/generic-table.component';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-my-collection-page',
  imports: [
    DateSelectorComponentComponent,
    GenericTableComponent,
    CommonModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './my-collection-page.component.html',
  styleUrl: './my-collection-page.component.scss'
})
export class MyCollectionPageComponent {
  fromDate: Date | null = new Date();
  toDate: Date | null = new Date();
  dataSource: MyCashCollectionDTO[] = [];
  isLoading = false;

  tableConfig: TableConfig = {
  columns: [
    { key: 'branchName', header: 'Branch Name', sortable: true },
    { key: 'cusName', header: 'Customer Name', sortable: true },
    { key: 'tranType', header: 'Transaction Type', sortable: true },
    { key: 'settledAmount', header: 'Settled Amount', type: 'currency', align: 'right', sortable: true },
    { key: 'debPolicyNo', header: 'Policy No', sortable: true },
    { key: 'debClaCode', header: 'Class Code', sortable: true },
    { key: 'proDesc', header: 'Product Description', sortable: true }
  ],
  showFooter: true,
  footerColumns: ['settledAmount'],
  showPagination: false,
  showFilter: false,
  emptyMessage: 'No collection records found',
  emptyIcon: 'collections'
};

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
    console.log('Fetching cash collection from', start, 'to', end, 'for intermediaryCode:', intermediaryCode);
    this.isLoading = true;

    this.myFiguresService.getMyCashCollection(intermediaryCode, start, end)
      .subscribe({
        next: (data) => {
          this.dataSource = data;
          console.log('Cash collection:', data);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching cash collection:', err);
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
