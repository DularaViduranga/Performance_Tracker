import { Component, Input } from '@angular/core';
import { HeroStatsComponentComponent } from '../../core/dashboardComponents/hero-stats-component/hero-stats-component.component';
import { QuickActionsComponentComponent } from '../../core/dashboardComponents/quick-actions-component/quick-actions-component.component';
import { AchievementTableComponentComponent } from '../../core/dashboardComponents/achievement-table-component/achievement-table-component.component';
import { MetricViewComponentComponent } from '../../core/dashboardComponents/metric-view-component/metric-view-component.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    HeroStatsComponentComponent,
    QuickActionsComponentComponent,
    AchievementTableComponentComponent

  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  currentMonth: string = new Date().toLocaleString('default', { month: 'long' });

  currentYear: number = new Date().getFullYear();

  tabNames: Record<string, string> = {
    performance: 'Performance Overview',
    gwp: 'Gross Written Premium',
    collection: 'Collection Metrics',
    renewal: 'Renewal Statistics',
    cancellation: 'Cancellation Analysis',
    business: 'New Business',
  };
}
