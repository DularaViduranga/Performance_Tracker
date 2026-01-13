import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-top-bar-component',
  standalone: true,
  imports: [MatToolbarModule, MatIconModule],
  templateUrl: './top-bar-component.component.html',
  styleUrl: './top-bar-component.component.scss'
})
export class TopBarComponentComponent implements OnInit {

  @Output() menuClick = new EventEmitter<void>();

  // Expose these properties so the template can bind to them
  intermediaryCode: string = '';
  userName: string = '';

  constructor(
    private router: Router,
    private userService: AuthService
  ) {}

  navigateToHomePage() {
    this.router.navigate(['/home']);
  }

  toggleMenu() {
    this.menuClick.emit();
  }

  ngOnInit() {
    // populate values used in template
    this.intermediaryCode = this.userService.getCurrentIntermediaryCode() || '';
    this.userName = this.userService.getCurrentUserName() || '';
  }

}
