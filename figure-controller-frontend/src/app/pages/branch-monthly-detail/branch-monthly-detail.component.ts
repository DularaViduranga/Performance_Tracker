import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { BranchesService, MonthWiseRegionGwpDTO } from '../../services/branches.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-branch-monthly-detail',
  imports: [CommonModule],
  templateUrl: './branch-monthly-detail.component.html',
  styleUrl: './branch-monthly-detail.component.scss'
})
export class BranchMonthlyDetailComponent {
  monthlyData: MonthWiseRegionGwpDTO[] = [];
  branchName = '';

  monthNames: string[] = [
    'January', 'February', 'March', 'April',
    'May', 'June', 'July', 'August',
    'September', 'October', 'November', 'December'
  ];

  constructor(
    private route: ActivatedRoute,
    private branchService: BranchesService
  ) {}

  ngOnInit(): void {
    const branchCode = this.route.snapshot.paramMap.get('branchCode')!;
    const year = Number(this.route.snapshot.paramMap.get('year'));

    this.branchName = history.state.branchName ?? '';

    this.branchService.getMonthWiseBranchGwp(branchCode, year).subscribe(data => {
      this.monthlyData = data;
      // Optionally fetch branch name if needed
    });
  }

  getMonthName(month: number): string {
    return this.monthNames[month - 1] ?? 'Unknown';
  }

  formatGwp(value: number): string {
    return new Intl.NumberFormat('en-LK', { style: 'currency', currency: 'LKR', notation: 'compact' }).format(value);
  }
}
