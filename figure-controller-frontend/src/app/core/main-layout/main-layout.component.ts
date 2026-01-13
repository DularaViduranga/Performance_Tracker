import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';
import { SidebarComponentComponent } from '../../core/dashboardComponents/sidebar-component/sidebar-component.component';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    LucideAngularModule,
    SidebarComponentComponent
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent implements OnInit {
  isMobileMenuOpen = false;
  currentTime: Date = new Date();

  activeTab: string = 'performance';
  pageTitle: string = 'Performance Overview';

  showLogoutConfirm = false;
  isLoggingOut = false;

  private tabNames: Record<string, string> = {
    performance: 'Performance Overview',
    gwp: 'Gross Written Premium',
    collection: 'Collection Metrics',
    renewal: 'Renewal Statistics',
    cancellation: 'Cancellation Analysis',
    business: 'New Business',
  };

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  // Called from sidebar
  onLogoutRequested() {
    this.showLogoutConfirm = true;
  }

  cancelLogout() {
    this.showLogoutConfirm = false;
  }

  confirmLogout() {
    this.showLogoutConfirm = false;
    this.isLoggingOut = true;

    setTimeout(() => {
      this.authService.logout();
      this.router.navigate(['/login']);
      this.isLoggingOut = false;
    }, 3000);
  }

  ngOnInit(): void {
    this.updateFromRoute();

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => this.updateFromRoute());

    // Optional: Update currentTime every minute
    setInterval(() => {
      this.currentTime = new Date();
    }, 60000);
  }

  private updateFromRoute(): void {
    let url = this.router.url.split('?')[0];
    if (url.endsWith('/')) url = url.slice(0, -1);

    // Special dynamic pages
    if (url.startsWith('/sales-officers-on-branch')) {
      const segments = url.split('/');
      const branchNameParam = segments[segments.length - 1];
      const branchName = decodeURIComponent(branchNameParam).replace(/-/g, ' ');
      this.pageTitle = `${branchName} - Sales Officers Overview`;
      this.activeTab = '';
      return;
    }

    if (url === '/my-branches'|| url.startsWith('/region-branches')) {
      this.pageTitle = 'Branch Overview';
      this.activeTab = '';
      return;
    }

    if (url === '/regions') {
      this.pageTitle = 'Region Overview';
      this.activeTab = '';
      return;
    }

    if (url === '/my-underwriters') {
      this.pageTitle = 'Sales Officers Attached To You';
      this.activeTab = '';
      return;
    }

    if (url === '/dashboard' || url === '/') {
      this.pageTitle = 'Welcome to Performance Tracker';
      this.activeTab = '';
      return;
    }

    // Standard performance tabs
    const routeMap: Record<string, { tab: string; title: string }> = {
      '/my-performance': { tab: 'performance', title: 'Performance Overview' },
      '/my-gwp': { tab: 'gwp', title: 'Gross Written Premium' },
      '/my-gwp/detailed': { tab: 'gwp', title: 'Gross Written Premium' },
      '/my-cash-collection': { tab: 'collection', title: 'Collection Metrics' },
      '/my-ren': { tab: 'renewal', title: 'Renewal Statistics' },
      '/my-can': { tab: 'cancellation', title: 'Cancellation Analysis' },
      '/my-business': { tab: 'business', title: 'New Business' },
    };

    const matchedKey = Object.keys(routeMap)
      .sort((a, b) => b.length - a.length) // longest match first
      .find(key => url.startsWith(key));

    if (matchedKey) {
      this.pageTitle = routeMap[matchedKey].title;
      this.activeTab = routeMap[matchedKey].tab;
    } else {
      this.pageTitle = 'Performance Overview';
      this.activeTab = 'performance';
    }
  }

  onTabChange(newTab: string): void {
    this.activeTab = newTab;
    this.isMobileMenuOpen = false;
  }
}
