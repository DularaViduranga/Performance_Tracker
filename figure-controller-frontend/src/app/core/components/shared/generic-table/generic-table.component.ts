import { Component, Input, ViewChild, OnInit, AfterViewInit, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';

export interface TableColumn {
  key: string;
  header: string;
  type?: 'text' | 'number' | 'date' | 'currency';
  align?: 'left' | 'center' | 'right';
  sortable?: boolean;
  width?: string;
  format?: (value: any) => string;
  cssClass?: (row: any) => string;
}

export interface TableRowAction {
  label: string;
  icon?: string;
  dialog?: string;
  route?: string;                    // old way (navigate)
  queryParams?: (row: any) => any;                  // optional identifier
  action?: (row: any) => void;
  visible?: (row: any) => boolean;
}

export interface TableConfig {
  columns: TableColumn[];
  showFooter?: boolean;
  footerColumns?: string[];
  showPagination?: boolean;
  pageSize?: number;
  pageSizeOptions?: number[];
  showFilter?: boolean;
  emptyMessage?: string;
  emptyIcon?: string;
  rowClass?: (row: any) => string;
  exportFileName?: string;
  rowActions?: TableRowAction[];
}

@Component({
  selector: 'app-generic-table',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatMenuModule,
    MatButtonModule
  ],
  templateUrl: './generic-table.component.html',
  styleUrl: './generic-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GenericTableComponent implements OnInit, AfterViewInit {
  @Input() data: any[] = [];
  @Input() config!: TableConfig;
  @Output() rowClick = new EventEmitter<any>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = [];

  constructor(private router: Router) {}

  ngOnInit() {
    this.displayedColumns = this.config.columns.map(col => col.key);
    // if (this.config.rowActions?.length) {
    //   this.displayedColumns.push('actions');
    // }
    this.dataSource.data = this.data;
  }

  ngOnChanges() {
    this.dataSource.data = this.data;
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.config.showPagination ? this.paginator : null;
    this.dataSource.sort = this.sort;
  }

  formatValue(value: any, column: TableColumn): string {
    if (value === null || value === undefined) return '-';
    if (column.format) return column.format(value);

    switch (column.type) {
      case 'number':
      case 'currency':
        return Number(value || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
      case 'date':
        const d = new Date(value);
        return isNaN(d.getTime()) ? '-' : d.toLocaleDateString();
      default:
        return String(value);
    }
  }

  getColumnTotal(columnKey: string): number {
    const col = this.config.columns.find(c => c.key === columnKey);
    if (!col || !['number', 'currency'].includes(col.type || '')) return 0;
    return this.data.reduce((sum, row) => sum + (Number(row[columnKey]) || 0), 0);
  }

  getGrandTotal(): number {
    if (!this.config.footerColumns?.length) return 0;

    return this.config.footerColumns.reduce((sum, key) => {
      return sum + this.getColumnTotal(key);
    }, 0);
  }

  formatCurrency(value: number): string {
    return Number(value || 0).toLocaleString('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  }


  getCellClass(row: any, column: TableColumn): string {
    return column.cssClass ? column.cssClass(row) : '';
  }

  getRowClass(row: any): string {
    return this.config.rowClass ? this.config.rowClass(row) : '';
  }

  onRowClick(row: any) {
    this.rowClick.emit(row);
  }

  navigateTo(action: TableRowAction, row: any) {
    if (action.action) {
    // If action() exists → call it (for dialogs!)
    action.action(row);
  } else if (action.route) {
    // Old behavior: navigate to route
    const qp = action.queryParams ? action.queryParams(row) : { id: row.id };
    this.router.navigate([action.route], { queryParams: qp });
  }
  }
}
