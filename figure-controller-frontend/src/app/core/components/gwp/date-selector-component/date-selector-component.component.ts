import { Component, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-date-selector-component',
  standalone: true,
  imports: [CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    FormsModule],
  templateUrl: './date-selector-component.component.html',
  styleUrl: './date-selector-component.component.scss'
})
export class DateSelectorComponentComponent implements OnChanges {
  @Input() fromDate: Date | null = new Date();
  @Input() toDate: Date | null = new Date();

  @Output() dateRangeChange = new EventEmitter<{ fromDate: Date | null; toDate: Date | null }>();

  // Min and max date constraints
  minToDate: Date | null = null;
  maxFromDate: Date | null = null;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['fromDate'] || changes['toDate']) {
      this.updateDateConstraints();
      this.onDateChange();
    }
  }

  updateDateConstraints() {
    // Set minimum date for "To Date" based on "From Date"
    if (this.fromDate) {
      this.minToDate = this.fromDate;
    } else {
      this.minToDate = null;
    }

    // Set maximum date for "From Date" based on "To Date"
    if (this.toDate) {
      this.maxFromDate = this.toDate;
    } else {
      this.maxFromDate = null;
    }
  }

  onFromDateChange() {
    this.updateDateConstraints();
    
    // If toDate is before fromDate, clear toDate
    if (this.fromDate && this.toDate && this.fromDate > this.toDate) {
      this.toDate = null;
    }
    
    this.onDateChange();
  }

  onToDateChange() {
    this.updateDateConstraints();
    
    // If fromDate is after toDate, clear fromDate
    if (this.fromDate && this.toDate && this.fromDate > this.toDate) {
      this.fromDate = null;
    }
    
    this.onDateChange();
  }

  onDateChange() {
    this.dateRangeChange.emit({
      fromDate: this.fromDate,
      toDate: this.toDate
    });
  }

}