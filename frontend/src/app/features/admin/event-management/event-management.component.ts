import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EventService } from '../../../core/services/event.service';
import { Event } from '../../../core/models';
import { LoadingComponent } from '../../../shared/components/loading.component';

@Component({
  selector: 'app-event-management',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingComponent],
  template: `
    <div class="page-header fade-in">
      <h1>Manage Events</h1>
      <a routerLink="/admin/events/add" class="btn btn-primary">+ Add Event</a>
    </div>

    <app-loading *ngIf="loading"></app-loading>

    <div *ngIf="!loading" class="card fade-in" style="animation-delay: 0.1s;">
      <div class="form-group mb-4">
        <input type="text" class="form-control" placeholder="Search events..." [(ngModel)]="searchTerm" (input)="searchEvents()" />
      </div>

      <table class="table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Category</th>
            <th>Date</th>
            <th>Venue</th>
            <th>Capacity</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let event of events">
            <td>{{ event.title }}</td>
            <td>{{ event.categoryName }}</td>
            <td>{{ event.eventDate }}</td>
            <td>{{ event.venue }}</td>
            <td>{{ event.capacity }}</td>
            <td><span class="badge badge-{{ event.status.toLowerCase() }}">{{ event.status }}</span></td>
            <td>
              <a [routerLink]="['/admin/events/edit', event.id]" class="btn btn-outline btn-sm">Edit</a>
              <button class="btn btn-danger btn-sm" (click)="deleteEvent(event.id)">Delete</button>
            </td>
          </tr>
          <tr *ngIf="events.length === 0">
            <td colspan="7" class="text-center">No events found.</td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
  styles: []
})
export class EventManagementComponent implements OnInit {
  events: Event[] = [];
  searchTerm = '';
  loading = false;

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.loading = true;
    this.eventService.getAllEvents().subscribe({
      next: (data) => { this.events = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  searchEvents(): void {
    this.loading = true;
    this.eventService.searchEvents(this.searchTerm || undefined).subscribe({
      next: (data) => { this.events = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  deleteEvent(id: number): void {
    if (!confirm('Are you sure you want to delete this event?')) return;
    this.eventService.deleteEvent(id).subscribe({
      next: () => this.loadEvents(),
      error: (err) => alert(err.error?.message || 'Cannot delete event with active registrations')
    });
  }
}
