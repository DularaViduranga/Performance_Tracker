// import { CommonModule } from '@angular/common';
// import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
// import { Router } from '@angular/router';
// import { LucideAngularModule } from 'lucide-angular';
// import { Subscription } from 'rxjs';
// import { AuthService } from '../../../services/auth.service';

// @Component({
//   selector: 'app-quick-actions-component',
//   imports: [CommonModule, LucideAngularModule],
//   templateUrl: './quick-actions-component.component.html',
//   styleUrl: './quick-actions-component.component.scss'
// })
// export class QuickActionsComponentComponent implements OnInit, OnDestroy{
//   userRole: string = ''; // will be 'RM', 'GM', or empty
//   branchCode: string = '';
//   branchName: string = '';
//   private authSubscription!: Subscription;

//   @Output() tabChange = new EventEmitter<string>();
//   String = String;

//   constructor(
//     private router: Router ,
//     private authService: AuthService
//   ) {
//     console.log('User Role in Quick Actions:', this.userRole);
//   }
//   ngOnDestroy(): void {
//     if (this.authSubscription) {
//       this.authSubscription.unsubscribe();
//     }
//   }
//   ngOnInit(): void {
//     // Subscribe to auth changes to always have the latest role
//     this.authSubscription = this.authService.auth$.subscribe(user => {
//       this.userRole = user?.role || '';
//       this.branchCode = user?.branchCode || '';
//       this.branchName = user?.branchName || '';
//       console.log('Updated User Role in Quick Actions:', this.userRole);
//     });


//   }



//   navigateToSalesPersons() {
//     this.router.navigate(['/my-underwriters']);
//     this.tabChange.emit('My Sales Persons');
//   }

//   navigateToBranches() {
//     if (this.userRole === 'RM') {
//       // Direct to own branches
//       this.router.navigate(['/my-branches']);
//       this.tabChange.emit('Branches');
//     } else if (this.userRole === 'GM') {
//       // Show list of regions first
//       this.router.navigate(['/regions']);
//       this.tabChange.emit('Regions');
//     }

//   }


//   navigateToSalesPersonsOnBranch() {
//     this.router.navigate(['/sales-officers-on-branch', this.branchCode, this.branchName]);
//     this.tabChange.emit('My Sales Persons');
//   }
// }
import { CommonModule } from '@angular/common';
import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-quick-actions-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './quick-actions-component.component.html',
  styleUrl: './quick-actions-component.component.scss'
})
export class QuickActionsComponentComponent implements OnInit, OnDestroy {

  userRole = '';        // RM | GM | ''
  branchCode = '';
  branchName = '';

  @Output() tabChange = new EventEmitter<string>();

  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.auth$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        this.userRole = user?.role ?? '';
        this.branchCode = user?.branchCode ?? '';
        this.branchName = user?.branchName ?? '';
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  navigateToSalesPersons(): void {
    this.router.navigate(['/my-underwriters']);
    this.tabChange.emit('My Sales Persons');
  }

  navigateToBranches(): void {
    if (this.userRole === 'RM') {
      this.router.navigate(['/my-branches']);
      this.tabChange.emit('Branches');
      return;
    }

    if (this.userRole === 'GM') {
      this.router.navigate(['/regions']);
      this.tabChange.emit('Regions');
    }
  }

  navigateToSalesPersonsOnBranch(): void {
    if (!this.branchCode) return;

    this.router.navigate([
      '/sales-officers-on-branch',
      this.branchCode,
      this.branchName
    ]);

    this.tabChange.emit('My Sales Persons');
  }

  getAlphabet(index: number): string {
    return String.fromCharCode(65 + index); // A, B, C...
  }

}
