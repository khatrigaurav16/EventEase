import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, timeout } from 'rxjs/operators';
import { Event, EventRequest, Category } from '../models';
import { API_BASE_URL } from '../config';

const API_URL = API_BASE_URL;
const REQUEST_TIMEOUT = 10000; // 10 seconds

@Injectable({ providedIn: 'root' })
export class EventService {
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

  getPublishedEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/events`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  getEventById(id: number): Observable<Event> {
    return this.http.get<Event>(`${this.apiUrl}/events/${id}`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  searchEvents(title?: string): Observable<Event[]> {
    const params: Record<string, string> = {};
    if (title) {
      params.title = title;
    }
    return this.http.get<Event[]>(`${this.apiUrl}/events/search`, { params }).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  filterByCategory(categoryId: number): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/events/category/${categoryId}`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/categories`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  getAllEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/admin/events`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  createEvent(data: EventRequest): Observable<Event> {
    return this.http.post<Event>(`${this.apiUrl}/admin/events`, data).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  updateEvent(id: number, data: EventRequest): Observable<Event> {
    return this.http.put<Event>(`${this.apiUrl}/admin/events/${id}`, data).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/admin/events/${id}`).pipe(
      timeout(REQUEST_TIMEOUT),
      catchError(this.handleError)
    );
  }
}
