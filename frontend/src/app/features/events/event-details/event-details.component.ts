import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { EventService } from '../../../core/services/event.service';
import { RegistrationService } from '../../../core/services/registration.service';
import { AuthService } from '../../../core/services/auth.service';
import { LoadingComponent } from '../../../shared/components/loading.component';

@Component({
  selector: 'app-event-details',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingComponent],
  template: `
    <app-loading *ngIf="loading && !errorMessage"></app-loading>

    <div *ngIf="errorMessage" class="alert alert-danger fade-in">
      {{ errorMessage }}
    </div>

    <div *ngIf="event && !loading && !errorMessage" class="card fade-in">
      <div class="event-image">
        📅
        <span class="badge badge-{{ event.status.toLowerCase() }} event-badge">{{ event.status }}</span>
      </div>
      <div class="event-content">
        <h1>{{ event.title }}</h1>
        <p class="text-muted">{{ event.categoryName }}</p>

        <div class="event-details-grid">
          <div class="detail-item">
            <strong>Date</strong>
            <span>{{ event.eventDate }}</span>
          </div>
          <div class="detail-item">
            <strong>Time</strong>
            <span>{{ event.eventTime }}</span>
          </div>
          <div class="detail-item">
            <strong>Venue</strong>
            <span>{{ event.venue }}</span>
          </div>
          <div class="detail-item">
            <strong>Capacity</strong>
            <span>{{ event.capacity }}</span>
          </div>
          <div class="detail-item">
            <strong>Organizer</strong>
            <span>{{ event.organizerName }}</span>
          </div>
          <div class="detail-item">
            <strong>Status</strong>
            <span class="badge badge-{{ event.status.toLowerCase() }}">{{ event.status }}</span>
          </div>
        </div>

        <div class="mt-4">
          <strong>Description</strong>
          <p>{{ event.description }}</p>
        </div>

        <div class="mt-4 event-actions">
          <button
            *ngIf="authService.isAuthenticated() && event.status === 'PUBLISHED'"
            class="btn btn-primary btn-wide"
            (click)="register()"
            [disabled]="registerLoading"
          >
            {{ registerLoading ? 'Registering...' : 'Register for this event' }}
          </button>
          <button
            *ngIf="!authService.isAuthenticated()"
            class="btn btn-outline btn-wide"
            [routerLink]="['/login']"
          >
            Login to Register
          </button>
          <a routerLink="/events" class="btn btn-outline btn-wide">Back to Events</a>
        </div>

        <div *ngIf="registerError" class="alert alert-danger mt-3">{{ registerError }}</div>
        <div *ngIf="registerSuccess" class="alert alert-success mt-3">{{ registerSuccess }}</div>
      </div>
    </div>
  `,
  styles: []
})
export class EventDetailsComponent implements OnInit {
  event: any = null;
  loading = false;
  errorMessage = '';
  registerLoading = false;
  registerError = '';
  registerSuccess = '';

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    private registrationService: RegistrationService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadEvent(id);
  }

  loadEvent(id: number): void {
    this.loading = true;
    this.errorMessage = '';
    this.eventService.getEventById(id).subscribe({
      next: (data) => { this.event = data; this.loading = false; },
      error: (err) => { this.loading = false; this.errorMessage = err.message; }
    });
  }

  register(): void {
    this.registerLoading = true;
    this.registerError = '';
    this.registerSuccess = '';

    this.registrationService.registerForEvent({ eventId: this.event.id }).subscribe({
      next: () => {
        this.registerLoading = false;
        this.registerSuccess = 'Successfully registered for the event!';
        this.event.status = 'PUBLISHED';
      },
      error: (err) => {
        this.registerLoading = false;
        this.registerError = err.message || 'Registration failed';
      }
    });
  }
}
