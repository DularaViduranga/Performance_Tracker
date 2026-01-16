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
  @Input() targetAmount: number = 0;

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

  getProgressWidth(): string {
    if (this.variant === 'target') {
      return '100%';
    }

    if (!this.targetAmount || this.targetAmount <= 0) {
      return '0%';
    }

    const percentage = (this.amount / this.targetAmount) * 100;
    return Math.min(percentage, 100) + '%';
  }

  getBarColor() {
    return this.variant === 'target'
      ? 'bg-amber-500'
      : 'bg-emerald-500';
  }

}
