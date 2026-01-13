// import { Component, OnInit } from '@angular/core';
// import { Router } from '@angular/router';
// import { AuthService } from '../../services/auth.service';
// import { CommonModule } from '@angular/common';

// interface WatermarkLogo {
//   top: string;
//   left: string;
//   size: number;
//   rotate: number;
//   opacity: number;
//   duration: string;
// }


// @Component({
//   selector: 'app-login-page',
//   imports: [CommonModule],
//   templateUrl: './login-page.component.html',
//   styleUrl: './login-page.component.scss'
// })
// export class LoginPageComponent implements OnInit{
//   username: string = '';
//   password: string = '';
//   logos: WatermarkLogo[] = [];

//   constructor(
//     private router: Router,
//     private authService: AuthService
//   ) {}
//   ngOnInit(): void {
//     this.generateLogos();
//   }


//   navigateToHomePage(){
//     // this.router.navigate(['/home']);
//     this.authService.loginMock().subscribe(() => {
//       this.router.navigate(['/home']);
//   });
//   }
//   // Navigate to the home page
//   // navigateToHomePage() {
//   //   this.authService.login(this.username, this.password)
//   // .subscribe(res => {
//   //   localStorage.setItem('token', res.token);
//   //   this.router.navigate(['/dashboard']);
//   // });
//   // }

//   loginAsRM() {
//     this.authService.loginMock('RM').subscribe(() => {
//       this.router.navigate(['/home']);
//     });
//   }

//   loginAsGM() {
//     this.authService.loginMock('GM').subscribe(() => {
//       this.router.navigate(['/home']);
//     });
//   }

//   loginAsBM() {
//     this.authService.loginMock('BM').subscribe(() => {
//       this.router.navigate(['/home']);
//     });
//   }

//   loginAsSalesOfficer() {
//     this.authService.loginMock('SALES_OFFICER').subscribe(() => {
//       this.router.navigate(['/home']);
//     });
//   }



//   private generateLogos(): void {
//     const count = 35;

//     for (let i = 0; i < count; i++) {

//       // avoid center area (login card)
//       const top = Math.random() * 100;
//       const left = Math.random() * 100;

//       if (top > 30 && top < 70 && left > 30 && left < 70) {
//         i--;
//         continue;
//       }

//       this.logos.push({
//         top: `${top}%`,
//         left: `${left}%`,
//         size: 60 + Math.random() * 100,
//         rotate: Math.random() * 360,
//         opacity: 0.04 + Math.random() * 0.08,
//         duration: `${20 + Math.random() * 20}s`
//       });
//     }
//   }

// }


import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // ← Add this for ngModel

interface WatermarkLogo {
  top: string;
  left: string;
  size: number;
  rotate: number;
  opacity: number;
  duration: string;
}

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, FormsModule], // ← FormsModule for two-way binding
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent implements OnInit {
  username: string = '';
  password: string = '';
  loading: boolean = false;
  error: string = '';

  logos: WatermarkLogo[] = [];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.generateLogos();

    // If user is already logged in, redirect to home/dashboard
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/home']);
    }
  }

  // Real login (uncomment when backend is ready)
  onLogin(): void {
    if (!this.username || !this.password) {
      this.error = 'Please enter username and password';
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.navigateAfterLogin();

      },
      error: (err) => {
        this.loading = false;
        this.error = 'Invalid username or password';
        console.error('Login failed:', err);
      }
    });
  }

  // Mock logins (for development/demo)
  loginAs(role: 'RM' | 'GM' | 'BM' | 'SALES_OFFICER'): void {
    this.loading = true;
    this.authService.loginMock(role).subscribe({
      next: () => {
        this.loading = false;
        this.navigateAfterLogin();
      },
      error: (err) => {
        this.loading = false;
        this.error = 'Mock login failed';
        console.error(err);
      }
    });
  }

  // Common navigation after successful login
  private navigateAfterLogin(): void {
    // Optional: redirect back to the page they were trying to access
    const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    this.router.navigateByUrl(returnUrl);
  }

  // Watermark background logos
  private generateLogos(): void {
    const count = 35;

    for (let i = 0; i < count; i++) {
      let top: number, left: number;

      // Avoid center area where login card is (30%-70%)
      do {
        top = Math.random() * 100;
        left = Math.random() * 100;
      } while (top > 30 && top < 70 && left > 30 && left < 70);

      this.logos.push({
        top: `${top}%`,
        left: `${left}%`,
        size: 60 + Math.random() * 100,
        rotate: Math.random() * 360,
        opacity: 0.04 + Math.random() * 0.08,
        duration: `${20 + Math.random() * 20}s`
      });
    }
  }
}
