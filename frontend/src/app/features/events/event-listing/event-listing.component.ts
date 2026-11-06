import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EventService } from '../../../core/services/event.service';
import { Event, Category } from '../../../core/models';
import { LoadingComponent } from '../../../shared/components/loading.component';

const DEFAULT_CATEGORIES = [
  { id: 1, name: 'Technology' },
  { id: 2, name: 'Sports' },
  { id: 3, name: 'Music' },
  { id: 4, name: 'Arts & Culture' },
  { id: 5, name: 'Education' },
  { id: 6, name: 'Social' },
];

@Component({
  selector: 'app-event-listing',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingComponent],
  template: `
    <div class="page-header fade-in">
      <h1>All Events</h1>
      <div class="controls flex items-center gap-3">
        <input type="text" class="form-control" placeholder="Search by title..." [(ngModel)]="searchTerm" (input)="searchEvents()" />
        <select class="form-control" [(ngModel)]="categoryFilter" (change)="filterByCategory()">
          <option value="">All Categories</option>
          <option *ngFor="let c of categories" [value]="c.id">{{ c.name }}</option>
        </select>
      </div>
    </div>

    <div *ngIf="errorMessage" class="alert alert-danger">
      {{ errorMessage }}
    </div>

    <app-loading *ngIf="loading && !errorMessage"></app-loading>

    <div *ngIf="!loading && !errorMessage" class="grid grid-cols-3">
      <div *ngFor="let event of events; trackBy: trackById" class="event-card fade-in">
        <div class="event-image">
          📅
          <span class="badge badge-{{ event.status.toLowerCase() }} event-badge">{{ event.status }}</span>
        </div>
        <div class="event-content">
          <h3>{{ event.title }}</h3>
          <p class="text-muted">{{ event.categoryName }}</p>
          <div class="event-meta">
            <span>📍 {{ event.venue }}</span>
            <span>📅 {{ event.eventDate }}</span>
            <span>🕐 {{ event.eventTime }}</span>
          </div>
          <div class="event-actions">
            <a [routerLink]="['/events', event.id]" class="btn btn-primary btn-sm">View Details</a>
          </div>
        </div>
      </div>

      <div *ngIf="events.length === 0" class="text-center col-span-full fade-in">
        <div class="empty-state">
          <span class="empty-icon">📭</span>
          <h3>No events found.</h3>
          <p>Try adjusting your search or category filter.</p>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class EventListingComponent implements OnInit {
  events: Event[] = [];
  categories: Category[] = [...DEFAULT_CATEGORIES];
  searchTerm = '';
  categoryFilter = '';
  loading = false;
  errorMessage = '';

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
    this.loadCategories();
  }

  trackById = (_: number, event: Event) => event.id;

  loadEvents(): void {
    this.loading = true;
    this.errorMessage = '';
    this.eventService.getPublishedEvents().subscribe({
      next: (data) => { this.events = data; this.loading = false; },
      error: (err) => { this.loading = false; this.errorMessage = err.message; }
    });
  }

  loadCategories(): void {
    this.eventService.getCategories().subscribe({
      next: (data) => { this.categories = data; },
      error: () => { /* keep default categories */ }
    });
  }

  searchEvents(): void {
    this.loading = true;
    this.errorMessage = '';
    this.eventService.searchEvents(this.searchTerm || undefined).subscribe({
      next: (data) => { this.events = data; this.loading = false; },
      error: (err) => { this.loading = false; this.errorMessage = err.message; }
    });
  }

  filterByCategory(): void {
    if (!this.categoryFilter) {
      this.loadEvents();
      return;
    }
    this.loading = true;
    this.errorMessage = '';
    this.eventService.filterByCategory(Number(this.categoryFilter)).subscribe({
      next: (data) => { this.events = data; this.loading = false; },
      error: (err) => { this.loading = false; this.errorMessage = err.message; }
    });
  }
}
