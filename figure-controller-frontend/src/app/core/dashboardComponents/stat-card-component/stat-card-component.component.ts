import { CommonModule } from '@angular/common';
import { Component,Input } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-stat-card-component',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './stat-card-component.component.html',
  styleUrl: './stat-card-component.component.scss'
})
export class StatCardComponentComponent {
  @Input() label: string = '';
  @Input() value: string | null | number = '';
  @Input() trend?: number;
  @Input() subtext: string = '';
}
