import { Component, Inject } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { GenericTableComponent, TableConfig } from '../../shared/generic-table/generic-table.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MyFiguresService } from '../../../../services/myFigures.service';
import { MatProgressBarModule } from '@angular/material/progress-bar';

export interface CancellationDialogData {
  sfcCode: string;
  fromDate: Date;  // Changed from string to Date
  toDate: Date;    // Changed from string to Date
}

@Component({
  selector: 'app-cancellaton-dialog',
  imports: [
    CommonModule,
    MatDialogModule,
    MatProgressBarModule,
    GenericTableComponent,
    MatIcon
  ],
  templateUrl: './cancellaton-dialog.component.html',
  styleUrl: './cancellaton-dialog.component.scss'
})
export class CancellationDialogComponent {
  cancellationData: any[] = [];
  loading = true;

  cancellationTableConfig: TableConfig = {
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
    emptyMessage: 'No Cancellation data available',
    emptyIcon: 'cancel'
  };

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: CancellationDialogData,
    private myFiguresService: MyFiguresService
  ) {
    this.loadCancellationData();
  }

  loadCancellationData() {
    // Convert Date objects to ISO strings for backend
    const fromIso = this.data.fromDate.toDateString();
    const toIso = this.data.toDate.toDateString();

    this.myFiguresService.getMyCancellation(
      fromIso,
      toIso,
      this.data.sfcCode
    ).subscribe({
      next: (data) => {
        this.cancellationData = data;
        this.loading = false;
        console.log('Cancellation data loaded:', data);
      },
      error: (err) => {
        console.error('Cancellation error:', err);
        this.cancellationData = [];
        this.loading = false;
      }
    });
  }

  // Format Date objects for display
  formatDisplay(date: Date): string {
    return date.toDateString(); // Use the new helper
  }
}
