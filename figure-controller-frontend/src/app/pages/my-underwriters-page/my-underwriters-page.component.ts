import { Component } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { GenericTableComponent, TableConfig, TableRowAction } from '../../core/components/shared/generic-table/generic-table.component';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { DateSelectorComponentComponent } from '../../core/components/gwp/date-selector-component/date-selector-component.component';
import { SFCCodeResponseDTO, UserService } from '../../services/user.service';
import { MatDialog } from '@angular/material/dialog';
import { GwpDialogComponent, GwpDialogData } from '../../core/components/underwritter/gwp-dialog/gwp-dialog.component';
import { DateUtils } from '../../core/utils/date.utils';
import { CollectionDialogComponent, CollectionDialogData } from '../../core/components/underwritter/collection-dialog/collection-dialog.component';
import { RenewalDialogComponent } from '../../core/components/underwritter/renewal-dialog/renewal-dialog.component';
import { CancellationDialogComponent } from '../../core/components/underwritter/cancellaton-dialog/cancellaton-dialog.component';
import { BusinessDialogComponent } from '../../core/components/underwritter/business-dialog/business-dialog.component';
import { PerformanceDialogComponent } from '../../core/components/underwritter/performance-dialog/performance-dialog.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-my-underwriters-page',
  imports: [
    DateSelectorComponentComponent,
    CommonModule,
    MatButtonModule,
    MatIconModule,
    GenericTableComponent,
    MatProgressSpinnerModule
  ],
  templateUrl: './my-underwriters-page.component.html',
  styleUrl: './my-underwriters-page.component.scss'
})
export class MyUnderwritersPageComponent {
  fromDate: Date | null = new Date();
  toDate: Date | null = new Date();
  dataSource: SFCCodeResponseDTO[] = [];
  isLoading = false;

  tableConfig: TableConfig = {
        columns: [
        { key: 'sfcCode', header: 'Sfc Code', sortable: true },
        { key: 'sfcName', header: 'Sfc Name', sortable: true },
        { key: 'brnName', header: 'Branch', sortable: true }
        ],
        showPagination: false,
        showFooter: false,
        showFilter: false,  // Show totals for these columns
        emptyMessage: 'No Underwriters data available',
        emptyIcon: 'cancel',
        rowActions: [
          {
            label: 'View GWP',
            icon: 'account_balance_wallet',
            dialog: 'gwp', // ← custom flag
            action: (row: any) => this.openGwpDialog(row)
          },
          {
            label: 'View Collection',
            icon: 'business_center',
            dialog: 'collection', // ← custom flag
            action: (row: any) => this.openCollectionDialog(row)
          },
          {
            label: 'View Renewal',
            icon: 'autorenew',
            dialog: 'renewal', // ← custom flag
            action: (row: any) => this.openRenewalDialog(row)
          },
          {
            label: 'View Cancellation',
            icon: 'cancel',
            dialog: 'cancellation', // ← custom flag
            action: (row: any) => this.openCancellationDialog(row)
          },
          {
            label: 'View Business',
            icon: 'business',
            dialog: 'business', // ← custom flag
            action: (row: any) => this.openBusinessDialog(row)
          },
          {
            label: 'View Performance',
            icon: 'trending_up',
            dialog: 'performance', // ← custom flag
            action: (row: any) => this.openPerformanceDialog(row)
          }
        ] as TableRowAction[]
  };


  constructor(
    private userService: UserService,
    private authService: AuthService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    // Initialize with current month
    const now = new Date();
    this.fromDate = new Date(now.getFullYear(), now.getMonth(), 1);
    this.toDate = new Date(now.getFullYear(), now.getMonth() + 1, 0);
  }

  // In your date picker component, when it emits:
  onDateRangeChange(dateRange: { fromDate: Date | null; toDate: Date | null }) {
    this.fromDate = dateRange.fromDate;
    this.toDate = dateRange.toDate;
    console.log('Date range changed:', dateRange);
  }


  onSearch() {
    if (!this.fromDate || !this.toDate) return;

    const sfcCode = this.authService.getCurrentIntermediaryCode(); // Replace with logged-in user later
    const fromDate = this.fromDate.toDateString();
    const toDate = this.toDate.toDateString();
    console.log('Fetching SFC data from', fromDate, 'to', toDate, 'for sfcCode:', sfcCode);
    this.isLoading = true;


    this.userService.getSFC( sfcCode, fromDate,toDate)
      .subscribe({
        next: (data) => {
          this.dataSource = data;
          console.log('SFC data:', data);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching SFC data:', err);
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

  openGwpDialog(row: SFCCodeResponseDTO) {
    if (!this.fromDate || !this.toDate) return;

    this.dialog.open(GwpDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: {
        sfcCode: row.sfcCode,
        fromDate: this.fromDate,
        toDate: this.toDate
      }
    });
  }

  openCollectionDialog(row: any) {
  if (!this.fromDate || !this.toDate) return;

  this.dialog.open(CollectionDialogComponent, {
    width: '900px',
    maxWidth: '95vw',
    data: {
      sfcCode: row.sfcCode,
      fromDate: this.fromDate,   // Pass Date object directly
      toDate: this.toDate        // Pass Date object directly
    } as CollectionDialogData
  });
  }

  openRenewalDialog(row: SFCCodeResponseDTO) {
    if (!this.fromDate || !this.toDate) return;

    this.dialog.open(RenewalDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: {
        sfcCode: row.sfcCode,
        fromDate: this.fromDate,
        toDate: this.toDate
      }
    });
  }

  openCancellationDialog(row: SFCCodeResponseDTO) {
    if (!this.fromDate || !this.toDate) return;

    this.dialog.open(CancellationDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: {
        sfcCode: row.sfcCode,
        fromDate: this.fromDate,
        toDate: this.toDate
      }
    });
  }

  openBusinessDialog(row: SFCCodeResponseDTO) {
    if (!this.fromDate || !this.toDate) return;

    this.dialog.open(BusinessDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: {
        sfcCode: row.sfcCode,
        fromDate: this.fromDate,
        toDate: this.toDate
      }
    });
  }

  openPerformanceDialog(row: SFCCodeResponseDTO) {
    if (!this.fromDate || !this.toDate) return;

    this.dialog.open(PerformanceDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: {
        sfcCode: row.sfcCode,
        fromDate: this.fromDate,
        toDate: this.toDate
      }
    });
  }
}
