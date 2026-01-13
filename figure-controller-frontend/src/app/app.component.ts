import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'figure_Controller_frontend';

//   constructor(private authService: AuthService) {
//   if (!this.authService.isAuthenticated()) {
//     this.authService.loginMock('RM').subscribe();
//   }
// }
}
