import { Component, Inject } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { GenericTableComponent, TableConfig } from '../../shared/generic-table/generic-table.component';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MyFiguresService } from '../../../../services/myFigures.service';

export interface BusinessDialogData {
  sfcCode: string;
  fromDate: Date;  // Changed from string to Date
  toDate: Date;    // Changed from string to Date
}

@Component({
  selector: 'app-business-dialog',
  imports: [
    CommonModule,
    MatDialogModule,
    MatProgressBarModule,
    GenericTableComponent,
    MatIcon
  ],
  templateUrl: './business-dialog.component.html',
  styleUrls: ['./business-dialog.component.scss']
})
export class BusinessDialogComponent {
  businessData: any[] = [];
  loading = true;

  businessTableConfig: TableConfig = {
    columns: [
      { key: 'policyNo', header: 'Policy No', sortable: true },
      { key: 'classDesc', header: 'Class Description', sortable: true },
      { key: 'prodDesc', header: 'Product Description', sortable: true },
      { key: 'customerName', header: 'Customer Name', sortable: true },
      { key: 'phoneNumber', header: 'Phone Number', sortable: true },
      { key: 'createdDate', header: 'Created Date', type: 'date', sortable: true },
      { key: 'periodFrom', header: 'Start', type: 'date', sortable: true },
      { key: 'periodTo', header: 'End', type: 'date', sortable: true },
      { key: 'branchName', header: 'Branch', sortable: true }
    ],
    showPagination: false,
    showFooter: false,
    showFilter: false,
    emptyMessage: 'No Business data available',
    emptyIcon: 'cancel',
    rowClass: (row) => row.status === 'POLICY CANCELLED' ? 'cancelled-row' : ''
  };

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: BusinessDialogData,
    private myFiguresService: MyFiguresService
  ) {
    this.loadBusinessData();
  }

  loadBusinessData() {
    // Convert Date objects to ISO strings for backend
    const fromIso = this.data.fromDate.toDateString();
    const toIso = this.data.toDate.toDateString();

    this.myFiguresService.getMyNewBuisness(
      fromIso,
      toIso,
      this.data.sfcCode
    ).subscribe({
      next: (data) => {
        this.businessData = data;
        this.loading = false;
        console.log('Business data loaded:', data);
      },
      error: (err) => {
        console.error('Business error:', err);
        this.businessData = [];
        this.loading = false;
      }
    });
  }

  // Format Date objects for display
  formatDisplay(date: Date): string {
    return date.toDateString(); // Use the new helper
  }
}
