import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIcon } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MyFiguresService } from '../../../../services/myFigures.service';
import { DateUtils } from '../../../utils/date.utils';
import { GenericTableComponent, TableConfig } from '../../shared/generic-table/generic-table.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';

export interface GwpDialogData {
  sfcCode: string;
  fromDate: Date;
  toDate: Date;
}

@Component({
  selector: 'app-gwp-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatProgressBarModule,
    MatButtonModule,
    GenericTableComponent,
    MatIcon
  ],
  templateUrl: './gwp-dialog.component.html',
  styleUrl: './gwp-dialog.component.scss'
})
export class GwpDialogComponent {
  gwpData: any[] = [];
  loading = true;

  gwpTableConfig: TableConfig = {
    columns: [
    { key: 'policyNumber', header: 'Policy Number', sortable: true, width: '200px' },
    { key: 'basic', header: 'Basic', type: 'currency', align: 'right', sortable: true },
    { key: 'srcc', header: 'SRCC', type: 'currency', align: 'right', sortable: true },
    { key: 'tc', header: 'TC', type: 'currency', align: 'right', sortable: true }
    ],
    showPagination: false,
    showFooter: true,
    footerColumns: ['basic', 'srcc', 'tc'],  // Show totals for these columns
    showFilter: false,
    emptyMessage: 'No GWP data available',
    emptyIcon: 'work'
  };

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: GwpDialogData,
    private myFiguresService: MyFiguresService
  ) {
    this.loadGwpData();
  }

  loadGwpData() {
    const fromIso = this.data.fromDate.toDateString();
    const toIso = this.data.toDate.toDateString();

    this.myFiguresService.getMyGWP(this.data.sfcCode, fromIso, toIso).subscribe({
      next: (data) => {
        this.gwpData = data;
        this.loading = false;
        console.log('Gwp data loaded:', data);
      },
      error: (err) => {
        console.error('GWP error:', err);
        this.gwpData = [];
        this.loading = false;
      }
    });
  }

  formatDisplay(date: Date): string {
    return date.toDateString(); // Use the new helper
  }
}
