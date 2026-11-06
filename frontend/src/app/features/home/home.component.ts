import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="hero fade-in">
      <div class="container">
        <h1>Welcome to EventEase</h1>
        <p>Manage college events with ease. Browse, register, and attend events happening around campus.</p>
        <div class="btn-group">
          <a routerLink="/events" class="btn btn-primary btn-lg">Browse Events</a>
          <a *ngIf="!authService.isAuthenticated()" routerLink="/register" class="btn btn-light btn-lg">Register</a>
        </div>
      </div>
    </section>

    <section class="features fade-in" style="animation-delay: 0.1s;">
      <div class="container">
        <div class="grid grid-cols-3">
          <div class="feature-card">
            <span class="feature-icon">📅</span>
            <h3>Browse Events</h3>
            <p>Discover upcoming events across categories.</p>
          </div>
          <div class="feature-card">
            <span class="feature-icon">🎫</span>
            <h3>Register</h3>
            <p>Sign up for events you want to attend.</p>
          </div>
          <div class="feature-card">
            <span class="feature-icon">👤</span>
            <h3>Your Dashboard</h3>
            <p>Track your registrations and history.</p>
          </div>
        </div>
      </div>
    </section>
  `,
  styles: []
})
export class HomeComponent {
  constructor(public authService: AuthService) {}
}
