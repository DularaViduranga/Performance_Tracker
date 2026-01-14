import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { RegionService } from '../../services/region.service';
import { BranchesService } from '../../services/branches.service';
import { HeroStatsComponentComponent } from '../../core/dashboardComponents/hero-stats-component/hero-stats-component.component';
import { QuickActionsComponentComponent } from '../../core/dashboardComponents/quick-actions-component/quick-actions-component.component';
import { AchievementTableComponentComponent } from '../../core/dashboardComponents/achievement-table-component/achievement-table-component.component';
import { PerformanceCardComponent } from '../../core/dashboardComponents/performance-card/performance-card.component';
import { IslandPerformanceCardComponent } from '../../core/dashboardComponents/island-performance-card/island-performance-card.component';
import { TopPerformersTableComponent } from '../../core/dashboardComponents/top-performers-table/top-performers-table.component';

interface IslandPerformance {
  target: number;
  achieved: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    HeroStatsComponentComponent,
    QuickActionsComponentComponent,
    AchievementTableComponentComponent,
    PerformanceCardComponent,
    IslandPerformanceCardComponent,
    TopPerformersTableComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  currentMonth = new Date().toLocaleString('default', { month: 'long' });
  currentYear = new Date().getFullYear();

  // BM/RM
  performanceData: { title: string; current: number; accumulated: number } | null = null;
  isBmOrRm = false;

  // GM/CEO
  islandAchieved: number = 0;
  islandTarget: number = 7500000000; // 7.5 Billion LKR - fixed
  isGmOrCeo = false;

  // New: Top performers
  topRegions: { name: string; currentMonthGwp: number; accumulatedGwp: number }[] = [];
  topBranches: { name: string; currentMonthGwp: number; accumulatedGwp: number }[] = [];

  constructor(
    private authService: AuthService,
    private regionService: RegionService,
    private branchesService: BranchesService
  ) {}

  ngOnInit(): void {
    const role = this.authService.getRole().toUpperCase();
    const branchCode = this.authService.getBranchCode();
    const regionCode = this.authService.getRegionCode();

    if (role === 'BM' && branchCode) {
      this.loadBranchPerformance(branchCode);
    } else if (role === 'RM' && regionCode) {
      this.loadRegionPerformance(regionCode);
    } else if (role === 'GM' || role === 'CEO') {
      this.loadIslandAchievedGwp();
      this.loadTopRegions();
      this.loadTopBranches();
    }

    this.isBmOrRm = ['BM', 'RM'].includes(role);
    this.isGmOrCeo = ['GM', 'CEO'].includes(role);
  }

  private loadBranchPerformance(branchCode: string): void {
    this.branchesService.getBranchAccumulatedPerformance(branchCode).subscribe({
      next: (data) => {
        this.performanceData = {
          title: `${data.branchName || data.branchCode} Branch`,
          current: data.currentMonthGwp,
          accumulated: data.accumulatedGwp
        };
      },
      error: () => {
        this.performanceData = { title: 'Branch Performance', current: 0, accumulated: 0 };
      }
    });
  }

  private loadRegionPerformance(regionCode: string): void {
    this.regionService.getRegionsWithGWP().subscribe({
      next: (regions) => {
        const myRegion = regions.find(r => r.regionCode === regionCode);
        if (myRegion) {
          this.performanceData = {
            title: myRegion.regionName,
            current: myRegion.currentMonthGwp,
            accumulated: myRegion.accumulatedGwp
          };
        }
      },
      error: () => {
        this.performanceData = { title: 'Region Performance', current: 0, accumulated: 0 };
      }
    });
  }

  private loadIslandAchievedGwp(): void {
    this.regionService.getIslandWideAchievedGwp().subscribe({
      next: (achieved) => {
        this.islandAchieved = achieved;
      },
      error: () => {
        this.islandAchieved = 0;
      }
    });
  }

  private loadTopRegions(): void {
    this.regionService.getTop3AccumulatedRegionsFromDaily().subscribe({
      next: (regions) => {
        this.topRegions = regions.map(r => ({
          name: r.regionName,
          currentMonthGwp: r.currentMonthGwp,
          accumulatedGwp: r.accumulatedGwp
        }));
      }
    });
  }

  private loadTopBranches(): void {
    this.branchesService.getTop10AccumulatedBranchesFromDaily().subscribe({
      next: (branches) => {
        this.topBranches = branches.map(b => ({
          name: b.branchName || b.branchCode,  // Use name, fallback to code
          currentMonthGwp: b.currentMonthGwp,
          accumulatedGwp: b.accumulatedGwp
        }));
      }
    });
  }

  tabNames: Record<string, string> = {
    performance: 'Performance Overview',
    gwp: 'Gross Written Premium',
    collection: 'Collection Metrics',
    renewal: 'Renewal Statistics',
    cancellation: 'Cancellation Analysis',
    business: 'New Business',
  };
}
