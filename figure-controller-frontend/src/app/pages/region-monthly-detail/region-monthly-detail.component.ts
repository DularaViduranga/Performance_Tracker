import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MonthWiseRegionGwpDTO, RegionService } from '../../services/region.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-region-monthly-detail',
  imports: [CommonModule],
  templateUrl: './region-monthly-detail.component.html',
  styleUrl: './region-monthly-detail.component.scss'
})
export class RegionMonthlyDetailComponent {
  monthlyData: MonthWiseRegionGwpDTO[] = [];
  regionName = '';

  monthNames: string[] = [
    'January', 'February', 'March', 'April',
    'May', 'June', 'July', 'August',
    'September', 'October', 'November', 'December'
  ];

  constructor(
    private route: ActivatedRoute,
    private regionService: RegionService
  ) {}

  ngOnInit(): void {
    const regionCode = this.route.snapshot.paramMap.get('regionCode')!;
    const year = Number(this.route.snapshot.paramMap.get('year'));

    this.regionName = history.state.regionName ?? '';

    this.regionService.getMonthWiseRegionGwp(regionCode, year).subscribe(data => {
      this.monthlyData = data;
      // Optionally fetch region name if needed
    });
  }

  getMonthName(month: number): string {
    return this.monthNames[month - 1] ?? 'Unknown';
  }

  formatGwp(value: number): string {
    return new Intl.NumberFormat('en-LK', { style: 'currency', currency: 'LKR', notation: 'compact' }).format(value);
  }
}
