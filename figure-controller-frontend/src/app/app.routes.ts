import { Routes } from '@angular/router';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { MainLayoutComponent } from './core/main-layout/main-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { MyGWPPageComponent } from './pages/my-gwp-page/my-gwp-page.component';
import { MyGWPDetailedPageComponent } from './pages/my-gwp-detailed-page/my-gwp-detailed-page.component';
import { MyBusinessPageComponent } from './pages/my-business-page/my-business-page.component';
import { MyCancellationPageComponent } from './pages/my-cancellation-page/my-cancellation-page.component';
import { MyCollectionPageComponent } from './pages/my-collection-page/my-collection-page.component';
import { MyRenewalPageComponent } from './pages/my-renewal-page/my-renewal-page.component';
import { MyAchievementPageComponent } from './pages/my-achievement-page/my-achievement-page.component';
import { MyUnderwritersPageComponent } from './pages/my-underwriters-page/my-underwriters-page.component';
import { BranchesPageComponent } from './pages/branches-page/branches-page.component';
import { SalesOfficersOnBranchPageComponent } from './pages/sales-officers-on-branch-page/sales-officers-on-branch-page.component';
import { Testing1Component } from './pages/my-renewal-page/testing/testing1/testing1.component';
import { RagionsPageComponent } from './pages/ragions-page/ragions-page.component';
import { authGuard } from './core/guards/auth.guard';
import { MonthlyRegionGwpPageComponent } from './pages/monthly-region-gwp-page/monthly-region-gwp-page.component';
import { MonthlyBranchGwpPageComponent } from './pages/monthly-branch-gwp-page/monthly-branch-gwp-page.component';
import { RegionMonthlyDetailComponent } from './pages/region-monthly-detail/region-monthly-detail.component';
import { BranchMonthlyDetailComponent } from './pages/branch-monthly-detail/branch-monthly-detail.component';

export const routes: Routes = [
  // Public: Login (no layout)
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },

  // Authenticated area: All inside MainLayout
  {
    path: '',
    component: MainLayoutComponent,
    // canActivate: [authGuard],
    children: [
      // Default route after login → Dashboard
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

      // Dashboard (home page)
      { path: 'dashboard', component: DashboardComponent },

      // GWP with sub-route
      {
        path: 'my-gwp',
        children: [
          { path: '', component: MyGWPPageComponent },
          { path: 'detailed', component: MyGWPDetailedPageComponent }
        ]
      },

      // Other pages
      { path: 'my-business', component: MyBusinessPageComponent },
      { path: 'my-can', component: MyCancellationPageComponent },
      { path: 'my-cash-collection', component: MyCollectionPageComponent },
      { path: 'my-ren', component: MyRenewalPageComponent },
      { path: 'my-performance', component: MyAchievementPageComponent },
      { path: 'my-underwriters', component: MyUnderwritersPageComponent },
      { path: 'my-branches', component: BranchesPageComponent  },
      { path: 'regions', component: RagionsPageComponent },
      {
        path: 'region-branches/:regionCode/:regionName',
        component: BranchesPageComponent
      },
      { path: 'sales-officers-on-branch/:branchCode/:branchName',
        component: SalesOfficersOnBranchPageComponent 
      },
      { path: 'renewal-test-page', component: Testing1Component },

      { path: 'monthly-region-gwp', component: MonthlyRegionGwpPageComponent },
      { path: 'monthly-branch-gwp', component: MonthlyBranchGwpPageComponent },
      { path: 'region-monthly-detail/:regionCode/:year', component: RegionMonthlyDetailComponent },
      { path: 'branch-monthly-detail/:branchCode/:year', component: BranchMonthlyDetailComponent },

      // Fallback inside layout
      { path: '**', redirectTo: 'dashboard' }
    ]
  },

  // Global fallback
  { path: '**', redirectTo: 'login' }
];
