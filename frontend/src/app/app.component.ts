import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar" [class.scrolled]="scrolled">
      <div class="container flex items-center justify-between">
        <div class="navbar-brand">
          <a routerLink="/" routerLinkActive="active" class="text-xl font-bold">EventEase</a>
        </div>

        <div class="navbar-links flex items-center gap-3">
          <a routerLink="/" routerLinkActive="active">Home</a>
          <a routerLink="/events" routerLinkActive="active">Events</a>

          <ng-container *ngIf="authService.isAuthenticated()">
            <a routerLink="/my-registrations" routerLinkActive="active">My Registrations</a>
            <a *ngIf="authService.isAdmin()" routerLink="/admin/dashboard" routerLinkActive="active">Admin Dashboard</a>
            <a *ngIf="authService.isAdmin()" routerLink="/admin/events" routerLinkActive="active">Manage Events</a>
            <button class="btn btn-outline navbar-btn" (click)="logout()">Logout</button>
          </ng-container>

          <ng-container *ngIf="!authService.isAuthenticated()">
            <a routerLink="/login" class="btn btn-primary">Login</a>
            <a routerLink="/register" class="btn btn-outline">Register</a>
          </ng-container>
        </div>
      </div>
    </nav>

    <main class="container page">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: []
})
export class AppComponent {
  scrolled = false;

  constructor(public authService: AuthService) {}

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.scrolled = window.scrollY > 10;
  }

  logout(): void {
    this.authService.logout();
  }
}
