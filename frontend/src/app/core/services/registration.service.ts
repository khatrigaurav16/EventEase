import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, timeout } from 'rxjs/operators';
import { Registration, RegistrationRequest } from '../models';
import { API_BASE_URL } from '../config';

const API_URL = `${API_BASE_URL}/registrations`;
const REQUEST_TIMEOUT = 10000;

@Injectable({ providedIn: 'root' })
export class RegistrationService {
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

  registerForEvent(data: RegistrationRequest): Observable<Registration> {
    return this.http.post<Registration>(`${this.apiUrl}`, data).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  getMyRegistrations(): Observable<Registration[]> {
    return this.http.get<Registration[]>(`${this.apiUrl}/my`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  cancelRegistration(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }
}
