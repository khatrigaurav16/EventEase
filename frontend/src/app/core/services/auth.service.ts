import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, throwError } from 'rxjs';
import { catchError, timeout } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User, AuthResponse, LoginRequest, RegisterRequest } from '../models';
import { API_BASE_URL } from '../config';

const TOKEN_KEY = 'eventease_token';
const USER_KEY = 'eventease_user';
const REQUEST_TIMEOUT = 10000; // 10 seconds

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = `${API_BASE_URL}/auth`;
  private currentUserSubject = new BehaviorSubject<User | null>(this.getStoredUser());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response) => {
        this.setSession(response.token, response.username, response.role);
      })
    );
  }

  register(data: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, data).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getUserRole(): string | null {
    const user = this.getStoredUser();
    return user?.role || null;
  }

  isAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }

  private handleError(err: any) {
    let message = 'An unexpected error occurred';
    if (err.status === 0) {
      message = 'Unable to connect to the server. Is the backend running?';
    } else if (err.status === 404) {
      message = 'Resource not found';
    } else if (err.status === 500) {
      message = 'Server error. Please try again later.';
    } else if (err.error?.message) {
      message = err.error.message;
    }
    return throwError(() => new Error(message));
  }

  private setSession(token: string, username: string, role: string): void {
    const user: User = { id: 0, username, email: '', role };
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private getStoredUser(): User | null {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw);
    } catch {
      return null;
    }
  }
}
