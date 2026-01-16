import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';
import { RegionService, RegionsWithGWPDTO } from '../../services/region.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-monthly-region-gwp-page',
  imports: [CommonModule,LucideAngularModule],
  templateUrl: './monthly-region-gwp-page.component.html',
  styleUrl: './monthly-region-gwp-page.component.scss'
})
export class MonthlyRegionGwpPageComponent {
  regions: RegionsWithGWPDTO[] = [];

  constructor(
    private regionService: RegionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.regionService.getRegionsWithGWP().subscribe(data => {
      this.regions = data;
    });
  }

  viewRegionDetails(regionCode: string) {
    this.router.navigate(
      ['/region-monthly-detail', regionCode, new Date().getFullYear()],
      {state:{ regionName: this.regions.find(region => region.regionCode === regionCode)?.regionName }}
    );
  }

}
