import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { EventService } from '../../../core/services/event.service';
import { EventRequest, Category } from '../../../core/models';
import { LoadingComponent } from '../../../shared/components/loading.component';

const DEFAULT_CATEGORIES: Category[] = [
  { id: 1, name: 'Technology' },
  { id: 2, name: 'Sports' },
  { id: 3, name: 'Music' },
  { id: 4, name: 'Arts & Culture' },
  { id: 5, name: 'Education' },
  { id: 6, name: 'Social' },
];

@Component({
  selector: 'app-add-event',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, LoadingComponent],
  template: `
    <div class="page-header">
      <h1>Add Event</h1>
      <a routerLink="/admin/events" class="btn btn-outline">Back to Events</a>
    </div>
    <app-loading *ngIf="loading"></app-loading>

    <form *ngIf="!loading" [formGroup]="form" (ngSubmit)="onSubmit()" class="form-card space-y-4">
      <div class="form-grid">
        <div class="form-group">
          <label class="form-label">Title</label>
          <input type="text" formControlName="title" class="form-control" placeholder="Event title" />
        </div>
        <div class="form-group">
          <label class="form-label">Category</label>
          <select formControlName="categoryId" class="form-control">
            <option value="">Select a category</option>
            <option *ngFor="let c of categories" [value]="c.id">{{ c.name }}</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Description</label>
        <textarea formControlName="description" class="form-control" rows="4" placeholder="Describe your event..."></textarea>
      </div>
      <div class="form-grid">
        <div class="form-group">
          <label class="form-label">Event Date</label>
          <input type="date" formControlName="eventDate" class="form-control" />
        </div>
        <div class="form-group">
          <label class="form-label">Event Time</label>
          <input type="time" formControlName="eventTime" class="form-control" />
        </div>
      </div>
      <div class="form-grid">
        <div class="form-group">
          <label class="form-label">Venue</label>
          <input type="text" formControlName="venue" class="form-control" placeholder="Venue location" />
        </div>
        <div class="form-group">
          <label class="form-label">Capacity</label>
          <input type="number" formControlName="capacity" class="form-control" placeholder="Max attendees" />
        </div>
      </div>
      <div class="form-grid">
        <div class="form-group">
          <label class="form-label">Organizer Name</label>
          <input type="text" formControlName="organizerName" class="form-control" placeholder="Organizer name" />
        </div>
        <div class="form-group">
          <label class="form-label">Status</label>
          <select formControlName="status" class="form-control">
            <option value="PUBLISHED">Published</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
        </div>
      </div>
      <button type="submit" class="btn btn-primary btn-wide" [disabled]="form.invalid || loading">
        {{ loading ? 'Creating Event...' : 'Create Event' }}
      </button>
    </form>
  `,
  styles: [`
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    @media (max-width: 640px) { .form-grid { grid-template-columns: 1fr; } }
  `]
})
export class AddEventComponent implements OnInit {
  form: FormGroup;
  categories: Category[] = [...DEFAULT_CATEGORIES];
  loading = false;

  constructor(private fb: FormBuilder, private eventService: EventService, private router: Router) {
    this.form = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      categoryId: ['', Validators.required],
      eventDate: ['', Validators.required],
      eventTime: ['', Validators.required],
      venue: ['', Validators.required],
      capacity: ['', [Validators.required, Validators.min(1)]],
      organizerName: ['', Validators.required],
      status: ['PUBLISHED', Validators.required]
    });
  }

  ngOnInit(): void {
    this.eventService.getPublishedEvents().subscribe({
      next: (events) => {
        const backendCats = events.map(e => ({ id: e.categoryId, name: e.categoryName }));
        const seen = new Set(this.categories.map(c => c.name));
        for (const c of backendCats) {
          if (!seen.has(c.name)) {
            this.categories.push(c);
            seen.add(c.name);
          }
        }
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;

    const data: EventRequest = this.form.value;
    this.eventService.createEvent(data).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/admin/events']);
      },
      error: () => {
        this.loading = false;
        alert('Failed to create event');
      }
    });
  }
}
