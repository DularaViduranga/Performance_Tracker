import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { GenericTableComponent, TableConfig } from '../../shared/generic-table/generic-table.component';
import { MatIcon } from '@angular/material/icon';
import { MyFiguresService } from '../../../../services/myFigures.service';
import { DateUtils } from '../../../utils/date.utils';
import { MatProgressBarModule } from '@angular/material/progress-bar';


export interface CollectionDialogData {
  sfcCode: string;
  fromDate: Date;  // Changed from string to Date
  toDate: Date;    // Changed from string to Date
}

@Component({
  selector: 'app-collection-dialog',
  imports: [
    CommonModule,
    MatDialogModule,
    MatProgressBarModule,
    GenericTableComponent,
    MatIcon
  ],
  templateUrl: './collection-dialog.component.html',
  styleUrl: './collection-dialog.component.scss'
})
export class CollectionDialogComponent {
  collectionData: any[] = [];
  loading = true;

  collectionTableConfig: TableConfig = {
    columns: [
      { key: 'branchName', header: 'Branch Name', sortable: true },
      { key: 'cusName', header: 'Customer Name', sortable: true },
      { key: 'tranType', header: 'Transaction Type', sortable: true },
      { key: 'settledAmount', header: 'Settled Amount', type: 'currency', align: 'right', sortable: true },
      { key: 'debPolicyNo', header: 'Policy No', sortable: true },
      { key: 'debClaCode', header: 'Class Code', sortable: true },
      { key: 'proDesc', header: 'Product Description', sortable: true }
    ],
    showPagination: false,
    showFooter: false,
    showFilter: false,
    emptyMessage: 'No Collection data available',
    emptyIcon: 'cancel'
  };

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: CollectionDialogData,
    private myFiguresService: MyFiguresService
  ) {
    this.loadCollectionData();
  }

  loadCollectionData() {
    // Convert Date objects to ISO strings for backend
    const fromIso = this.data.fromDate.toDateString();
    const toIso = this.data.toDate.toDateString();

    this.myFiguresService.getMyCashCollection(
      this.data.sfcCode,
      fromIso,
      toIso
    ).subscribe({
      next: (data) => {
        this.collectionData = data;
        this.loading = false;
        console.log('Collection data loaded:', data);
      },
      error: (err) => {
        console.error('Collection error:', err);
        this.collectionData = [];
        this.loading = false;
      }
    });
  }

  // Format Date objects for display
  formatDisplay(date: Date): string {
    return date.toDateString(); // Use the new helper
  }
}
