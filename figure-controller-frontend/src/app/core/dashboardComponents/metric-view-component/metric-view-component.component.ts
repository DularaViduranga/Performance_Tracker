import { Component, Input } from '@angular/core';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-metric-view-component',
  imports: [LucideAngularModule],
  templateUrl: './metric-view-component.component.html',
  styleUrls: ['./metric-view-component.component.scss']
})
export class MetricViewComponentComponent {
  @Input() title!: string;
}
