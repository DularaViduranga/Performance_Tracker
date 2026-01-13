import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-nav-bar-component',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './nav-bar-component.component.html',
  styleUrl: './nav-bar-component.component.scss'
})
export class NavBarComponentComponent {
  @Input() sidebarOpen = false;
  @Output() closeSidebar = new EventEmitter<void>();

  onMouseMove(event: MouseEvent) {
    const button = event.currentTarget as HTMLElement;
    const rect = button.getBoundingClientRect();

    // Calculate cursor position relative to button center
    const x = event.clientX - rect.left - rect.width / 2;
    const y = event.clientY - rect.top - rect.height / 2;

    // Calculate shadow offset based on cursor position
    const shadowX = x / 5;
    const shadowY = y / 5;

    // Apply dynamic shadow that follows cursor
    button.style.boxShadow = `
      ${shadowX}px ${shadowY}px 10px rgba(0,0,0,0.2),
      ${shadowX * 0.5}px ${shadowY * 0.5}px 5px rgba(0,0,0,0.14),
      ${shadowX * 0.3}px ${shadowY * 0.3}px 3px rgba(0,0,0,0.12)
    `;
    button.style.transform = `translate(${x / 20}px, ${y / 20}px)`;
  }

  onMouseLeave(event: MouseEvent) {
    const button = event.currentTarget as HTMLElement;
    button.style.boxShadow = 'none';
    button.style.transform = 'translate(0, 0)';
  }

}
