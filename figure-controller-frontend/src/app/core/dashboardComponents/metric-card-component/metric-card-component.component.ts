import { Component, Input } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-metric-card-component',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './metric-card-component.component.html',
  styleUrl: './metric-card-component.component.scss'
})
export class MetricCardComponentComponent {
  @Input() label!: string;
  @Input() icon!: string;
  @Input() isActive = false;
}
