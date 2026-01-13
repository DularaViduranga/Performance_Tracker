import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-region-card',
  imports: [],
  templateUrl: './region-card.component.html',
  styleUrl: './region-card.component.scss'
})
export class RegionCardComponent {
  @Input() regionCode: string = '';
  @Input() regionName: string = '';

  constructor(private router: Router) { }

  onCardClick() : void {
    // Navigate to the branch page for the given region code
    this.router.navigate(['/sales-officers-on-region', this.regionCode, this.regionName]);
  }
}
