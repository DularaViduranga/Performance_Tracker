import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { BarChartBoxComponentComponent } from '../../core/components/bar-chart-box-component/bar-chart-box-component.component';
import { CardComponent } from '../../core/components/card/card.component';
import { BranchpageNevigatorCardComponent } from '../../core/components/card/branchpage-nevigator-card/branchpage-nevigator-card.component';
import { LucideAngularModule, LucideHome } from 'lucide-angular';
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    BarChartBoxComponentComponent,
    CardComponent,
    BranchpageNevigatorCardComponent,
    LucideAngularModule

  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  animations: [
    trigger('slideInOut', [
      state('in', style({ transform: 'translateX(0%)' })),
      state('out', style({ transform: 'translateX(-100%)' })),
      transition('out => in', [animate('300ms ease-in')]),
      transition('in => out', [animate('300ms ease-out')])
    ])
  ]
})
export class HomeComponent {
  sidebarOpen = false;

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  closeSidebar() {
    this.sidebarOpen = false;
  }
}
