import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';
import { BranchesService, GetAllBranchesFromDaily } from '../../services/branches.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-monthly-branch-gwp-page',
  imports: [CommonModule,LucideAngularModule],
  templateUrl: './monthly-branch-gwp-page.component.html',
  styleUrl: './monthly-branch-gwp-page.component.scss'
})
export class MonthlyBranchGwpPageComponent {
  branches: GetAllBranchesFromDaily[] = [];

  constructor(
    private branchesService: BranchesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.branchesService.getAllBranchesFromDaily().subscribe(data => {
      this.branches = data;
    });
  }

  viewBranchDetails(branchCode: string) {
    this.router.navigate(
      ['/branch-monthly-detail', branchCode, new Date().getFullYear()],
      { state: { branchName: this.branches.find(branch => branch.branchCode === branchCode)?.branchName } }
    );
  }
}
