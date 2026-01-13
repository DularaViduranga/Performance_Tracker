import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject, Observable, of, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AllUserLoginResponseDTO {
  token: string;
  username: string;
  empType: string;
  intermediaryCode: string;
  role: string;
  branchCode?: string;
  branchName?: string;
  regionCode?: string;
  regionName?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private platformId = inject(PLATFORM_ID);
  private isBrowser = isPlatformBrowser(this.platformId);

  // 🚫 NEVER touch localStorage here
  private authSubject = new BehaviorSubject<AllUserLoginResponseDTO | null>(null);
  auth$ = this.authSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadUserFromStorage();
  }

  // ========================
  // AUTH INIT
  // ========================
  private loadUserFromStorage(): void {
    if (!this.isBrowser) return;

    const stored = localStorage.getItem('currentUser');
    if (!stored) return;

    try {
      const user = JSON.parse(stored) as AllUserLoginResponseDTO;
      this.authSubject.next(user);
    } catch {
      localStorage.removeItem('currentUser');
      localStorage.removeItem('authToken');
    }
  }

  // ========================
  // LOGIN
  // ========================
  login(username: string, password: string): Observable<AllUserLoginResponseDTO> {
    console.log('AuthService: Performing real login for', username);
    return this.http.post<AllUserLoginResponseDTO>(
      `${environment.apiUrl}api/v1/auth/login`,
      { username, password }
    ).pipe(
      tap(response => this.setUser(response))
    );
  }

  // MOCK LOGIN
  loginMock(role: 'SALES_OFFICER' | 'RM' | 'GM' | 'BM' = 'RM'): Observable<AllUserLoginResponseDTO> {
    const mockUsers: Record<string, AllUserLoginResponseDTO> = {
      RM: {
        token: 'mock-jwt-token',
        username: 'RM',
        empType: 'Non Sales',
        intermediaryCode: 'F8858',
        role: 'RM',
        regionCode: 'RG6',
        regionName: 'COLOMBO NORTH REGION'
      },
      GM: {
        token: 'mock-jwt-token',
        username: 'GM',
        empType: 'Non Sales',
        intermediaryCode: 'F8858',
        role: 'GM'
      },
      BM: {
        token: 'mock-jwt-token',
        username: 'BM',
        empType: 'Non Sales',
        intermediaryCode: 'F8858',
        role: 'BM',
        branchCode: 'KEKI',
        branchName: 'Kekirawa'
      },
      SALES_OFFICER: {
        token: 'mock-jwt-token',
        username: 'SALES_OFFICER',
        empType: 'Sales',
        intermediaryCode: 'F8858',
        role: 'SALES_OFFICER'
      }
    };

    const user = mockUsers[role];
    this.setUser(user);
    return of(user);
  }

  // ========================
  // LOGOUT
  // ========================
  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem('currentUser');
      localStorage.removeItem('authToken');
    }
    this.authSubject.next(null);
  }

  // ========================
  // GETTERS
  // ========================
  isAuthenticated(): boolean {
    return !!this.authSubject.value;
  }

  getCurrentUser(): AllUserLoginResponseDTO | null {
    return this.authSubject.value;
  }

  getRole(): string {
    return this.authSubject.value?.role ?? '';
  }

  getCurrentIntermediaryCode(): string {
    return this.authSubject.value?.intermediaryCode ?? '';
  }

  getBranchCode(): string {
    return this.authSubject.value?.branchCode ?? '';
  }

  getBranchName(): string {
    return this.authSubject.value?.branchName ?? '';
  }

  getRegionCode(): string {
    return this.authSubject.value?.regionCode ?? '';
  }

  getRegionName(): string {
    return this.authSubject.value?.regionName ?? '';
  }

  getToken(): string {
    return this.authSubject.value?.token ?? '';
  }

  getCurrentUserName(): string {
    return this.authSubject.value?.username ?? '';
  }

  // ========================
  // INTERNAL
  // ========================
  private setUser(user: AllUserLoginResponseDTO): void {
    if (this.isBrowser) {
      localStorage.setItem('currentUser', JSON.stringify(user));
      localStorage.setItem('authToken', user.token);
    }
    this.authSubject.next(user);
  }
}
