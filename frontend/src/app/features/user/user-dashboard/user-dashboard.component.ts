import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatsService } from '../../../core/services/stats.service';
import { LoadingComponent } from '../../../shared/components/loading.component';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule, LoadingComponent],
  template: `
    <div class="page-header fade-in">
      <h1>Your Dashboard</h1>
    </div>

    <app-loading *ngIf="loading"></app-loading>

    <div *ngIf="!loading" class="grid grid-cols-2 fade-in" style="animation-delay: 0.1s;">
      <div class="card">
        <h3>Upcoming Registrations</h3>
        <div *ngIf="upcoming.length === 0" class="empty-state">
          <span class="empty-icon">📭</span>
          <p>No upcoming registrations</p>
        </div>
        <div *ngFor="let r of upcoming" class="card mb-3" style="padding: 1rem;">
          <h4 style="margin: 0 0 0.25rem 0;">{{ r.eventTitle }}</h4>
          <p class="text-muted" style="margin: 0 0 0.5rem 0;">{{ r.registrationDate }}</p>
          <span class="badge badge-active">{{ r.status }}</span>
        </div>
      </div>
      <div class="card">
        <h3>Registration History</h3>
        <div *ngIf="history.length === 0" class="empty-state">
          <span class="empty-icon">📋</span>
          <p>No history</p>
        </div>
        <div *ngFor="let r of history" class="card mb-3" style="padding: 1rem;">
          <h4 style="margin: 0 0 0.25rem 0;">{{ r.eventTitle }}</h4>
          <p class="text-muted" style="margin: 0 0 0.5rem 0;">{{ r.registrationDate }}</p>
          <span class="badge" [ngClass]="r.status === 'CANCELLED' ? 'badge-cancelled' : 'badge-completed'">{{ r.status }}</span>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .py-4 { padding-top: 1rem; padding-bottom: 1rem; }
  `]
})
export class UserDashboardComponent implements OnInit {
  upcoming: any[] = [];
  history: any[] = [];
  loading = false;

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    this.loading = true;
    this.statsService.getUserDashboard().subscribe({
      next: (data: any) => {
        this.upcoming = data.upcomingRegistrations || [];
        this.history = data.registrationHistory || [];
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }
}
