import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatsService } from '../../../core/services/stats.service';
import { AdminStats } from '../../../core/models';
import { RouterLink } from '@angular/router';
import { LoadingComponent } from '../../../shared/components/loading.component';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingComponent],
  template: `
    <div class="page-header fade-in">
      <h1>Admin Dashboard</h1>
      <a routerLink="/admin/events/add" class="btn btn-primary">+ Add Event</a>
    </div>

    <div *ngIf="errorMessage" class="alert alert-danger">
      {{ errorMessage }}
    </div>

    <app-loading *ngIf="loading && !errorMessage"></app-loading>

    <div *ngIf="!loading && !errorMessage" class="stats-grid fade-in" style="animation-delay: 0.1s;">
      <div class="stat-card primary">
        <div class="stat-icon">📊</div>
        <div>
          <div class="stat-value">{{ stats?.totalEvents }}</div>
          <div class="stat-label">Total Events</div>
        </div>
      </div>
      <div class="stat-card accent">
        <div class="stat-icon">🎫</div>
        <div>
          <div class="stat-value">{{ stats?.totalRegistrations }}</div>
          <div class="stat-label">Total Registrations</div>
        </div>
      </div>
      <div class="stat-card info">
        <div class="stat-icon">✅</div>
        <div>
          <div class="stat-value">{{ stats?.publishedEvents }}</div>
          <div class="stat-label">Published Events</div>
        </div>
      </div>
      <div class="stat-card danger">
        <div class="stat-icon">📝</div>
        <div>
          <div class="stat-value">{{ stats?.activeRegistrations }}</div>
          <div class="stat-label">Active Registrations</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">❌</div>
        <div>
          <div class="stat-value">{{ stats?.cancelledEvents }}</div>
          <div class="stat-label">Cancelled Events</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🏁</div>
        <div>
          <div class="stat-value">{{ stats?.completedEvents }}</div>
          <div class="stat-label">Completed Events</div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-2 fade-in" style="animation-delay: 0.2s;">
      <div class="card">
        <h3 style="margin-top: 0;">Quick Actions</h3>
        <div class="form-group" style="margin-bottom: 1rem;">
          <a routerLink="/admin/events/add" class="btn btn-primary btn-wide">Create New Event</a>
        </div>
        <div class="form-group" style="margin-bottom: 0;">
          <a routerLink="/admin/events" class="btn btn-outline btn-wide">Manage All Events</a>
        </div>
      </div>
      <div class="card">
        <h3 style="margin-top: 0;">Recent Activity</h3>
        <div class="empty-state">
          <span class="empty-icon">📋</span>
          <p>Activity feed coming soon.</p>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class AdminDashboardComponent implements OnInit {
  stats: AdminStats | null = null;
  loading = false;
  errorMessage = '';

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    this.loading = true;
    this.statsService.getAdminStats().subscribe({
      next: (data: AdminStats) => { this.stats = data; this.loading = false; },
      error: (err) => { this.loading = false; this.errorMessage = err.message || 'Failed to load dashboard'; }
    });
  }
}
