import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { AlertComponent } from '../../../shared/components/alert.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, AlertComponent, RouterLink],
  template: `
    <div class="auth-container fade-in">
      <div class="auth-card">
        <h2>Welcome Back</h2>
        <p class="text-center text-muted">Sign in to your account</p>
        <app-alert *ngIf="errorMessage" [message]="errorMessage" type="danger"></app-alert>

        <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
          <div class="form-group">
            <label class="form-label">Username</label>
            <input type="text" formControlName="username" class="form-control" placeholder="Enter your username" />
          </div>
          <div class="form-group">
            <label class="form-label">Password</label>
            <input type="password" formControlName="password" class="form-control" placeholder="Enter your password" />
          </div>
          <button type="submit" class="btn btn-primary btn-wide" [disabled]="form.invalid || loading">
            {{ loading ? 'Signing in...' : 'Sign In' }}
          </button>
        </form>

        <p class="text-center text-muted mt-3">
          Don't have an account? <a routerLink="/register" class="text-primary-link">Create account</a>
        </p>
      </div>
    </div>
  `,
  styles: [`
    .text-primary-link { color: var(--color-primary); font-weight: 500; }
    .text-primary-link:hover { text-decoration: underline; }
  `]
})
export class LoginComponent {
  form: FormGroup;
  loading = false;
  errorMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.form.value).subscribe({
      next: () => this.router.navigate(['/events']),
      error: () => {
        this.loading = false;
        this.errorMessage = 'Invalid username or password';
      }
    });
  }
}
