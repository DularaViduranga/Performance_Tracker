// // import { HttpInterceptorFn } from '@angular/common/http';
// // import { inject } from '@angular/core';
// // import { AuthService } from '../services/auth.service';

// // export const authInterceptor: HttpInterceptorFn = (req, next) => {
// //   const authService = inject(AuthService);
// //   const token = authService.getToken();

// //   if (token) {
// //     req = req.clone({
// //       setHeaders: {
// //         Authorization: `Bearer ${token}`
// //       }
// //     });
// //   }

// //   return next(req);
// // };
// // src/app/core/interceptors/auth.interceptor.ts
// import { HttpInterceptorFn } from '@angular/common/http';
// import { inject } from '@angular/core';
// import { Router } from '@angular/router';
// import { catchError } from 'rxjs/operators';
// import { throwError } from 'rxjs';
// import { AuthService } from '../services/auth.service';
// import { MatSnackBar } from '@angular/material/snack-bar';

// export const authInterceptor: HttpInterceptorFn = (req, next) => {
//   const authService = inject(AuthService);
//   const router = inject(Router);

//   const token = authService.getToken();

//   // 1. Add Bearer token if exists
//   if (token) {
//     req = req.clone({
//       setHeaders: {
//         Authorization: `Bearer ${token}`
//       }
//     });
//   }

//   // 2. Handle response errors (especially 401)
//   return next(req).pipe(
//     catchError(error => {
//       if (error.status === 401) {


//         const snackBar = inject(MatSnackBar);
//         snackBar.open('Session expired. Please log in again.', 'OK', {
//           duration: 5000,
//           panelClass: ['error-snackbar']
//         });
//         // Token expired or invalid → logout and redirect
//         authService.logout();

//         // Optional: remember where the user was
//         const returnUrl = router.url;

//         router.navigate(['/login'], {
//           queryParams: { returnUrl }   // so you can redirect back after re-login
//         });
//       }

//       // Re-throw the error so individual calls can still handle it if needed
//       return throwError(() => error);
//     })
//   );
// };
// src/app/core/interceptors/auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Inject everything at the top level (valid injection context)
  const authService = inject(AuthService);
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);

  const token = authService.getToken();

  // Add Bearer token if exists
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  // Handle errors globally
  return next(req).pipe(
    catchError(error => {
      if (error.status === 401) {
        // Show message
        snackBar.open('Session expired. Please log in again.', 'OK', {
          duration: 5000,
          panelClass: ['bg-red-600', 'text-white']
        });

        // Logout and redirect
        authService.logout();

        const returnUrl = router.url;
        router.navigate(['/login'], { queryParams: { returnUrl } });
      }

      // Re-throw so components can handle if needed
      return throwError(() => error);
    })
  );
};
