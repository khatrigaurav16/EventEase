import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { EventService } from '../../../core/services/event.service';
import { EventRequest } from '../../../core/models';
import { LoadingComponent } from '../../../shared/components/loading.component';

@Component({
  selector: 'app-edit-event',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, LoadingComponent],
  template: `
    <div class="page-header fade-in">
      <h1>Edit Event</h1>
      <a routerLink="/admin/events" class="btn btn-outline">Back to Events</a>
    </div>
    <app-loading *ngIf="loading"></app-loading>

    <form *ngIf="event && !loading" [formGroup]="form" (ngSubmit)="onSubmit()" class="form-card space-y-4">
      <div class="form-grid">
        <div class="form-group">
          <label class="form-label">Title</label>
          <input type="text" formControlName="title" class="form-control" />
        </div>
        <div class="form-group">
          <label class="form-label">Category ID</label>
          <input type="number" formControlName="categoryId" class="form-control" />
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Description</label>
        <textarea formControlName="description" class="form-control" rows="4"></textarea>
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
          <input type="text" formControlName="venue" class="form-control" />
        </div>
        <div class="form-group">
          <label class="form-label">Capacity</label>
          <input type="number" formControlName="capacity" class="form-control" />
        </div>
      </div>
      <div class="form-grid">
        <div class="form-group">
          <label class="form-label">Organizer Name</label>
          <input type="text" formControlName="organizerName" class="form-control" />
        </div>
        <div class="form-group">
          <label class="form-label">Status</label>
          <select formControlName="status" class="form-control">
            <option value="PUBLISHED">Published</option>
            <option value="CANCELLED">Cancelled</option>
            <option value="COMPLETED">Completed</option>
          </select>
        </div>
      </div>
      <button type="submit" class="btn btn-primary btn-wide" [disabled]="form.invalid || loading">
        {{ loading ? 'Updating...' : 'Update Event' }}
      </button>
      <button type="button" class="btn btn-outline btn-wide" (click)="goBack()">Cancel</button>
    </form>
  `,
  styles: [`
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    @media (max-width: 640px) { .form-grid { grid-template-columns: 1fr; } }
  `]
})
export class EditEventComponent implements OnInit {
  form: FormGroup;
  event: any = null;
  loading = false;

  constructor(private fb: FormBuilder, private eventService: EventService, private route: ActivatedRoute, private router: Router) {
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
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loading = true;
    this.eventService.getEventById(id).subscribe({
      next: (data) => {
        this.event = data;
        this.form.patchValue(data);
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loading = true;

    const data: EventRequest = this.form.value;
    this.eventService.updateEvent(id, data).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/admin/events']);
      },
      error: () => {
        this.loading = false;
        alert('Failed to update event');
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/admin/events']);
  }
}
