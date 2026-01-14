// src/app/core/dashboardComponents/performance-card/performance-card.component.ts
import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';

interface GwpData {
  title: string;
  current: number;
  accumulated: number;
}

@Component({
  selector: 'app-performance-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './performance-card.component.html',
  styleUrl: './performance-card.component.scss'
})
export class PerformanceCardComponent implements OnInit {
  @Input() data!: GwpData;  // { title: 'Kekirawa Branch', current: ..., accumulated: ... }

  constructor(private authService: AuthService) {}

  ngOnInit(): void {}

  // Compact currency formatting (7M, 1.2B, etc.)
  formatGwp(value: number): string {
    return new Intl.NumberFormat('en-LK', {
      style: 'currency',
      currency: 'LKR',
      notation: 'compact',
      maximumFractionDigits: 1
    }).format(value);
  }
}
