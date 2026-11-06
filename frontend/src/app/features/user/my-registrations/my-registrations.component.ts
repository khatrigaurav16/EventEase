import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistrationService } from '../../../core/services/registration.service';
import { LoadingComponent } from '../../../shared/components/loading.component';

@Component({
  selector: 'app-my-registrations',
  standalone: true,
  imports: [CommonModule, LoadingComponent],
  template: `
    <div class="page-header fade-in">
      <h1>My Registrations</h1>
    </div>

    <div *ngIf="errorMessage" class="alert alert-danger">
      {{ errorMessage }}
    </div>

    <app-loading *ngIf="loading && !errorMessage"></app-loading>

    <div *ngIf="!loading && !errorMessage" class="card fade-in">
      <table *ngIf="registrations.length > 0" class="table">
        <thead>
          <tr>
            <th>Event</th>
            <th>Date Registered</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let r of registrations">
            <td>{{ r.eventTitle }}</td>
            <td>{{ r.registrationDate }}</td>
            <td><span class="badge" [ngClass]="r.status === 'ACTIVE' ? 'badge-active' : 'badge-cancelled'">{{ r.status }}</span></td>
            <td>
              <button *ngIf="r.status === 'ACTIVE'" class="btn btn-danger btn-sm" (click)="cancel(r.id)">
                Cancel
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div *ngIf="registrations.length === 0" class="text-center py-4 text-muted">
        <span class="empty-icon" style="font-size: 2.5rem; display: block; margin-bottom: 0.5rem;">📭</span>
        <p>No registrations found.</p>
      </div>
    </div>

    <div *ngIf="successMessage" class="alert alert-success">{{ successMessage }}</div>
  `,
  styles: [`
    .table { width: 100%; border-collapse: collapse; }
    .table th, .table td { padding: 0.75rem; text-align: left; border-bottom: 1px solid var(--color-border); }
    .table th { background: var(--color-surface-hover); font-weight: 600; color: var(--color-text-secondary); }
    .py-4 { padding-top: 1rem; padding-bottom: 1rem; }
  `]
})
export class MyRegistrationsComponent implements OnInit {
  registrations: any[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private registrationService: RegistrationService) {}

  ngOnInit(): void {
    this.loadRegistrations();
  }

  loadRegistrations(): void {
    this.loading = true;
    this.errorMessage = '';
    this.registrationService.getMyRegistrations().subscribe({
      next: (data) => { this.registrations = data; this.loading = false; },
      error: (err) => { this.loading = false; this.errorMessage = err.message; }
    });
  }

  cancel(id: number): void {
    if (!confirm('Are you sure you want to cancel this registration?')) return;
    this.registrationService.cancelRegistration(id).subscribe({
      next: () => {
        this.successMessage = 'Registration cancelled successfully.';
        this.loadRegistrations();
      },
      error: (err) => {
        this.errorMessage = err.message || 'Failed to cancel registration';
      }
    });
  }
}
