import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Performer {
  name: string;
  currentMonthGwp: number;
  accumulatedGwp: number;
}

@Component({
  selector: 'app-top-performers-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './top-performers-table.component.html',
  styleUrl: './top-performers-table.component.scss'
})
export class TopPerformersTableComponent {
  @Input() title: string = '';
  @Input() data: Performer[] = [];
  @Input() rankStart: number = 1; // For showing rank 1–10 or 1–3

  formatGwp(value: number): string {
    return new Intl.NumberFormat('en-LK', {
      style: 'currency',
      currency: 'LKR',
      notation: 'compact',
      maximumFractionDigits: 1
    }).format(value);
  }
}
