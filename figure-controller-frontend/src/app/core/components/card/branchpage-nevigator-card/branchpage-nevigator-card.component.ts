import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-branchpage-nevigator-card',
  imports: [RouterLink, MatIconModule],
  templateUrl: './branchpage-nevigator-card.component.html',
  styleUrl: './branchpage-nevigator-card.component.scss'
})
export class BranchpageNevigatorCardComponent {

}
