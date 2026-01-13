import { Component, Inject } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { GenericTableComponent, TableConfig } from '../../shared/generic-table/generic-table.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MyFiguresService } from '../../../../services/myFigures.service';
import { MatProgressBarModule } from '@angular/material/progress-bar';

export interface PerformanceDialogData {
  sfcCode: string;
  fromDate: Date;  // Changed from string to Date
  toDate: Date;    // Changed from string to Date
}

@Component({
  selector: 'app-performance-dialog',
  imports: [
    CommonModule,
    MatDialogModule,
    MatProgressBarModule,
    GenericTableComponent,
    MatIcon
  ],
  templateUrl: './performance-dialog.component.html',
  styleUrl: './performance-dialog.component.scss'
})
export class PerformanceDialogComponent {
  performanceData: any[] = [];
  loading = true;

  performanceTableConfig: TableConfig = {
    columns: [
      { key: 'classCode', header: 'Class Code', sortable: true },
      { key: 'target', header: 'Target', sortable: true },
      { key: 'achieved', header: 'Achieved', sortable: true },
      {
        key: 'percentage',
        header: 'Percentage',
        sortable: true,
        format: (value: number) => value ? `${value.toFixed(1)}%` : '0.0%'
      }
    ],
    showPagination: false,
    showFooter: false,
    showFilter: false,
    emptyMessage: 'No performance data available',
    emptyIcon: 'cancel'
  };

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: PerformanceDialogData,
    private myFiguresService: MyFiguresService
  ) {
    this.loadPerformanceData();
  }

  loadPerformanceData() {
    // Convert Date objects to ISO strings for backend
    const fromIso = this.data.fromDate.toDateString();
    const toIso = this.data.toDate.toDateString();

    this.myFiguresService.getPerformanceByPeriod(
      this.data.sfcCode,
      fromIso,
      toIso
    ).subscribe({
      next: (data) => {
        this.performanceData = data;
        this.loading = false;
        console.log('Performance data loaded:', data);
      },
      error: (err) => {
        console.error('Performance error:', err);
        this.performanceData = [];
        this.loading = false;
      }
    });
  }

  // Format Date objects for display
  formatDisplay(date: Date): string {
    return date.toDateString(); // Use the new helper
  }
}
