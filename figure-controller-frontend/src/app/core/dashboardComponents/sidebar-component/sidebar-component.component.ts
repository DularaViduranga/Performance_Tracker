import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';
import { MetricCardComponentComponent } from '../metric-card-component/metric-card-component.component';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

interface Metric {
  id: string;
  label: string;
  icon: any;
  path: string;
}

@Component({
  selector: 'app-sidebar-component',
  imports: [CommonModule, LucideAngularModule, MetricCardComponentComponent],
  templateUrl: './sidebar-component.component.html',
  styleUrl: './sidebar-component.component.scss'
})
export class SidebarComponentComponent {
  @Input() activeTab!: string;
  @Output() tabChange = new EventEmitter<string>();
  userName: string = '';
  intermediaryCode: string = '';

  @Output() logoutRequested = new EventEmitter<void>();

  metrics: Metric[] = [
    { id: 'gwp', label: 'My GWP', icon: 'TrendingUp', path: 'my-gwp' },
    { id: 'collection', label: 'My Collection', icon: 'Wallet', path: 'my-cash-collection' },
    { id: 'renewal', label: 'My Renewal', icon: 'RefreshCcw', path: 'my-ren' },
    { id: 'cancellation', label: 'My Cancellation', icon: 'XCircle', path: 'my-can' },
    { id: 'performance', label: 'My Performance', icon: 'Award', path: 'my-performance' },
    { id: 'business', label: 'My Business', icon: 'Briefcase', path: 'my-business' },
  ];

  constructor(
    private authService: AuthService,
    private router: Router) {}

  navigateTo(path: string, id: string) {
    this.router.navigate([path]);
    this.tabChange.emit(id); // Still emit for parent component to track active state
  }

  goToDashboard() {
    this.router.navigate(['/dashboard']);
    this.tabChange.emit('dashboard'); // Optional: set active tab to dashboard
  }

  ngOnInit() {
    // You can use authService here if needed
    this.intermediaryCode = this.authService.getCurrentIntermediaryCode();
    this.userName = this.authService.getCurrentUserName();
    console.log('Sidebar initialized for user:', this.intermediaryCode, 'Name:', this.userName);
  }


  openLogoutConfirm() {
    this.logoutRequested.emit();
  }


}
