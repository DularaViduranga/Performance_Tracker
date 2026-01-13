import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { BranchesByRegionDTO, RegionService } from '../../services/region.service';
import { BranchCardComponent } from '../../core/components/card/branch-card/branch-card.component';
import { LucideAngularModule } from 'lucide-angular';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-branches-page',
  imports: [CommonModule, MatIconModule, BranchCardComponent, LucideAngularModule],
  templateUrl: './branches-page.component.html',
  styleUrls: ['./branches-page.component.scss']
})
export class BranchesPageComponent implements OnInit {
  branches: BranchesByRegionDTO[] = [];
  filteredBranches = signal<BranchesByRegionDTO[]>([]);
  searchTerm = signal('');
  regionName = signal<string>('My Region'); // title

  private userRole = localStorage.getItem('userRole') || '';
  private userRegionCode = localStorage.getItem('regionCode') || '';

  constructor(
    private regionService: RegionService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();

    const routeRegionCode = this.route.snapshot.paramMap.get('regionCode');
    const routeRegionName = this.route.snapshot.paramMap.get('regionName');

    if (routeRegionCode && routeRegionName) {
      this.regionName.set(`${routeRegionName} Branches`);
      this.loadBranches(routeRegionCode);
      return;
    }

    // Case 2: Regional Manager accessing directly via /my-branches
    if (currentUser && currentUser.role === 'RM' && currentUser.regionCode) {
      this.regionName.set('My Region Branches');
      this.loadBranches(currentUser.regionCode);
      return;
    }

    // Case 3: No valid access → redirect appropriately
    console.warn('No region access available - redirecting...');

    if (currentUser?.role === 'GM') {
      this.router.navigate(['/regions']);
    } else {
      this.router.navigate(['/dashboard']); // or login if not authenticated
    }

  }

  loadBranches(regionCode: string): void {
    this.regionService.getBranchesByRegion(regionCode).subscribe({
      next: (data) => {
        this.branches = data;
        this.filteredBranches.set(data);
        console.log('Branches loaded successfully:', this.branches);
      },
      error: (error) => {
        this.filteredBranches.set([]);
        console.error('Error fetching branches:', error);
      }
    });
  }

  updateSearch(event: any): void {
    const term = event.target.value.toLowerCase();
    this.searchTerm.set(term);
    this.filteredBranches.set(
      this.branches.filter(b =>
        b.brnName.toLowerCase().includes(term) ||
        b.brnCode.toLowerCase().includes(term)
      )
    );
  }

  clearSearch(): void {
    this.searchTerm.set('');
    this.filteredBranches.set(this.branches);
  }

  trackByBranchCode(index: number, branch: BranchesByRegionDTO): string {
    return branch.brnCode;
  }
}
