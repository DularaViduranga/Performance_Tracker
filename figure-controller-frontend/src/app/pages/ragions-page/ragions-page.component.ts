import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { RegionService, RegionsWithGWPDTO } from '../../services/region.service';

@Component({
  selector: 'app-ragions-page',
  imports: [LucideAngularModule,CommonModule],
  templateUrl: './ragions-page.component.html',
  styleUrl: './ragions-page.component.scss'
})
export class RagionsPageComponent implements OnInit {
  dataSource : RegionsWithGWPDTO[] = [];
  loading = false;

  constructor(
    private router: Router,
    private regionService: RegionService
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.regionService.getRegionsWithGWP().subscribe({
      next: (data) => {
        this.dataSource = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  goToBranches(regionCode: string, regionName: string) {
    this.router.navigate(['/region-branches', regionCode, regionName]);
  }

}
