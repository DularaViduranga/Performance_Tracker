import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-island-performance-card',
  imports: [CommonModule],
  templateUrl: './island-performance-card.component.html',
  styleUrl: './island-performance-card.component.scss'
})
export class IslandPerformanceCardComponent {
  @Input() title: string = '';
  @Input() amount: number = 0;
  @Input() variant: 'target' | 'achieved' = 'achieved';

  formatGwp(value: number): string {
    return new Intl.NumberFormat('en-LK', {
      style: 'currency',
      currency: 'LKR',
      notation: 'compact',
      maximumFractionDigits: 1
    }).format(value);
  }

  getGradient() {
    return this.variant === 'target'
      ? 'from-amber-50 to-orange-50 border-amber-300'
      : 'from-emerald-50 to-teal-50 border-emerald-300';
  }

  getTextColor() {
    return this.variant === 'target' ? 'text-amber-800' : 'text-emerald-800';
  }

  getLabel() {
    return this.variant === 'target' ? 'Annual Target GWP' : 'Achieved GWP';
  }
}
