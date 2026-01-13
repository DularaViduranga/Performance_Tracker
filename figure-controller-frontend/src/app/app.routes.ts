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
      { path: 'my-branches', component: BranchesPageComponent ,title: 'Branch Overview' },
      { path: 'regions', component: RagionsPageComponent ,title: 'Regions Overview'},
      {
        path: 'region-branches/:regionCode/:regionName',
        component: BranchesPageComponent,
        title: 'Region Branches'// or just remove the prerender flag
      },
      { path: 'sales-officers-on-branch/:branchCode/:branchName',
        component: SalesOfficersOnBranchPageComponent ,
        title: 'Sales Officers Overview'  // or just remove the prerender flag
      },
      { path: 'renewal-test-page', component: Testing1Component },

      // Fallback inside layout
      { path: '**', redirectTo: 'dashboard' }
    ]
  },

  // Global fallback
  { path: '**', redirectTo: 'login' }
];
