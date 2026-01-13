import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { MyFiguresService, MyPerformanceResponseDTO } from '../../../services/myFigures.service';
import { UserService } from '../../../services/user.service';
import { ViewChild } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

Chart.register(...registerables);

@Component({
  selector: 'app-bar-chart-box-component',
  standalone: true,
  imports: [
    CommonModule,
    BaseChartDirective
  ],
  templateUrl: './bar-chart-box-component.component.html',
  styleUrl: './bar-chart-box-component.component.scss'
})
export class BarChartBoxComponentComponent implements OnInit {
  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;
  performanceData: MyPerformanceResponseDTO[] = [];
  isBrowser: boolean;

  public barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: ['MT', 'MS', 'MC', 'FI', 'EG', 'MD'],
    datasets: [
      {
        label: 'Target',
        data: [],
        backgroundColor: '#06b6d4',
        borderColor: '#06b6d4',
      },
      {
        label: 'Achieved',
        data: [],
        backgroundColor: '#6b7280',
        borderColor: '#6b7280',
      }
    ]
  };

  public barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        labels: {
          color: '#374151'
        }
      }
    },
    scales: {
      x: {
        ticks: {
          color: '#6b7280',
          font: {
            weight: 500
          }
        },
        grid: {
          color: '#e5e7eb'
        }
      },
      y: {
        ticks: {
          color: '#6b7280'
        },
        grid: {
          color: '#e5e7eb'
        }
      }
    }
  };

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private myFiguresService: MyFiguresService,
    private userService: AuthService

  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngOnInit(): void {
    if (this.isBrowser) {
      this.loadPerformanceData();
    }
  }

  loadPerformanceData() {
    const now = new Date();

  // First day of current month
  const startDate = new Date(now.getFullYear(), now.getMonth(), 1);

  // Last day of current month
  const endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0);

    this.myFiguresService.getPerformanceByPeriod(
      this.userService.getCurrentIntermediaryCode(),
      startDate.toDateString(),
      endDate.toDateString()
    ).subscribe({
      next: (data) => {
        this.performanceData = data;
        this.barChartData.datasets[0].data = this.performanceData.map(item => item.target);
        this.barChartData.datasets[1].data = this.performanceData.map(item => item.achieved);
        this.chart?.update();
        console.log('Performance data loaded:', data);
      },
      error: (err) => {
        console.error('Performance error:', err);
        this.performanceData = [];
      }
    });
  }


}
