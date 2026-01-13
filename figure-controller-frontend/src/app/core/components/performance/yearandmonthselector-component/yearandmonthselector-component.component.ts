import { Component, EventEmitter, Input, Output, OnInit, OnChanges, SimpleChanges, Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { NativeDateAdapter, DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { MatDatepicker } from '@angular/material/datepicker';

@Injectable()
export class CustomDateAdapter extends NativeDateAdapter {
  override format(date: Date, displayFormat: Object): string {
    if (displayFormat === 'input') {
      const month = ('0' + (date.getMonth() + 1)).slice(-2);
      const year = date.getFullYear();
      return `${month}/${year}`;
    }
    return super.format(date, displayFormat);
  }
}

export const CUSTOM_DATE_FORMATS = {
  parse: {
    dateInput: 'MM/YYYY',
  },
  display: {
    dateInput: 'input',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'app-yearandmonthselector-component',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  providers: [
    { provide: DateAdapter, useClass: CustomDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: CUSTOM_DATE_FORMATS }
  ],
  templateUrl: './yearandmonthselector-component.component.html',
  styleUrls: ['./yearandmonthselector-component.component.scss']
})
export class YearandmonthselectorComponentComponent implements OnInit, OnChanges {
  @Input() selectedYear: number | null = null;
  @Input() selectedMonth: string | null = null;
  @Output() dateRangeChange = new EventEmitter<{ fromDate: Date | null; toDate: Date | null }>();

  date = new FormControl<Date | null>(null);
  maxDate: Date = new Date();

  ngOnInit() {
    this.initializeDate();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['selectedYear'] || changes['selectedMonth']) {
      this.initializeDate();
    }
  }

  initializeDate() {
    if (this.selectedYear && this.selectedMonth) {
      const month = parseInt(this.selectedMonth) - 1;
      const selectedDate = new Date(this.selectedYear, month, 1);
      this.date.setValue(selectedDate);
    } else {
      this.date.setValue(null);
    }
  }

  setMonthAndYear(normalizedMonthAndYear: Date, datepicker: MatDatepicker<Date>) {
    const ctrlValue = this.date.value || new Date();
    ctrlValue.setMonth(normalizedMonthAndYear.getMonth());
    ctrlValue.setFullYear(normalizedMonthAndYear.getFullYear());
    
    this.date.setValue(ctrlValue);
    this.selectedYear = ctrlValue.getFullYear();
    this.selectedMonth = ('0' + (ctrlValue.getMonth() + 1)).slice(-2);
    
    datepicker.close();
    this.emitDateRange();
  }

  emitDateRange() {
    const selectedDate = this.date.value;
    if (selectedDate) {
      const year = selectedDate.getFullYear();
      const month = selectedDate.getMonth();
      
      const fromDate = new Date(year, month, 1);
      const toDate = new Date(year, month + 1, 0);
      
      this.dateRangeChange.emit({ fromDate, toDate });
    }
  }
}