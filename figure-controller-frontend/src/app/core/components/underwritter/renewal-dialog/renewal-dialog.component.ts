import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { GenericTableComponent, TableConfig } from '../../shared/generic-table/generic-table.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MyFiguresService } from '../../../../services/myFigures.service';
import { MatProgressBarModule } from '@angular/material/progress-bar';

export interface RenewalDialogData {
  sfcCode: string;
  fromDate: Date;  // Changed from string to Date
  toDate: Date;    // Changed from string to Date
}

@Component({
  selector: 'app-renewal-dialog',
  imports: [
    CommonModule,
    MatDialogModule,
    MatProgressBarModule,
    GenericTableComponent,
    MatIcon
  ],
  templateUrl: './renewal-dialog.component.html',
  styleUrl: './renewal-dialog.component.scss'
})
export class RenewalDialogComponent {
  renewalData: any[] = [];
  loading = true;

  renewalTableConfig: TableConfig = {
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
    emptyMessage: 'No Renewal data available',
    emptyIcon: 'cancel',
    rowClass: (row) => {
      if (this.isToBeRenewed(row.periodTo)) {
        return 'to-be-renewed-row';
      }
      return '';
    }
  };


constructor(
    @Inject(MAT_DIALOG_DATA) public data: RenewalDialogData,
    private myFiguresService: MyFiguresService
  ) {
    this.loadRenewalData();
  }
  loadRenewalData() {
    // Convert Date objects to ISO strings for backend
    const fromIso = this.data.fromDate.toDateString();
    const toIso = this.data.toDate.toDateString();

    this.myFiguresService.getMyRenewal(
      fromIso,
      toIso,
      this.data.sfcCode
    ).subscribe({
      next: (data) => {
        this.renewalData = data;
        this.loading = false;
        console.log('Renewal data loaded:', data);
      },
      error: (err) => {
        console.error('Renewal error:', err);
        this.renewalData = [];
        this.loading = false;
      }
    });
  }

  isToBeRenewed(periodTo: string): boolean {
    if (!periodTo) return false;

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const periodToDate = new Date(periodTo);
    periodToDate.setHours(0, 0, 0, 0);

    return periodToDate >= today;
  }


  // Format Date objects for display
  formatDisplay(date: Date): string {
    return date.toDateString(); // Use the new helper
  }
}
