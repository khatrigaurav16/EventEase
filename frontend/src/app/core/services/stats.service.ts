import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, timeout } from 'rxjs/operators';
import { AdminStats } from '../models';
import { API_BASE_URL } from '../config';

const API_URL = API_BASE_URL;
const REQUEST_TIMEOUT = 10000;

@Injectable({ providedIn: 'root' })
export class StatsService {
  private apiUrl = API_URL;

  constructor(private http: HttpClient) {}

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

  getAdminStats(): Observable<AdminStats> {
    return this.http.get<AdminStats>(`${this.apiUrl}/admin/dashboard`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  getUserDashboard(): Observable<any> {
    return this.http.get(`${this.apiUrl}/user/dashboard`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }
}
