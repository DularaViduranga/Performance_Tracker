import {
  Component,
  Input,
  Output,
  EventEmitter
} from '@angular/core';
import {
  trigger,
  state,
  style,
  transition,
  animate
} from '@angular/animations';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mobile-sidebar-component',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './mobile-sidebar-component.component.html',
  styleUrl: './mobile-sidebar-component.component.scss',
  animations: [
    trigger('sidebarAnim', [
      state(
        'open',
        style({ transform: 'translateX(0)' })
      ),
      state(
        'closed',
        style({ transform: 'translateX(-100%)' })
      ),
      transition('closed => open', animate('300ms ease-out')),
      transition('open => closed', animate('300ms ease-in'))
    ]),
    trigger('fadeBackdrop', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('200ms ease-out', style({ opacity: 0.5 }))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ opacity: 0 }))
      ])
    ])
  ]
})
export class MobileSidebarComponentComponent {
  @Input() sidebarOpen = false;
  @Output() closeSidebar = new EventEmitter<void>();
  @Output() pageSelected = new EventEmitter<string>();

  constructor(private router: Router) {}

navigateToGwp() {
  this.pageSelected.emit('My GWP');
  this.router.navigate(['/my-gwp']);
  this.closeSidebar.emit();
}

navigateToColl() {
  this.pageSelected.emit('My Cash Collection');
  this.router.navigate(['/my-cash-collection']);
  this.closeSidebar.emit();
}

navigateToRen() {
  this.pageSelected.emit('My Renewal');
  this.router.navigate(['/my-ren']);
  this.closeSidebar.emit();
}

navigateToCan() {
  this.pageSelected.emit('My Cancellation');
  this.router.navigate(['/my-can']);
  this.closeSidebar.emit();
}

navigateToPerformance() {
  this.pageSelected.emit('My Performance');
  this.router.navigate(['/my-performance']);
  this.closeSidebar.emit();
}

navigateToBusiness() {
  this.pageSelected.emit('My Business');
  this.router.navigate(['/my-business']);
  this.closeSidebar.emit();
}
}
