import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { AlertComponent } from '../../../shared/components/alert.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, AlertComponent, RouterLink],
  template: `
    <div class="auth-container fade-in">
      <div class="auth-card">
        <h2>Create Account</h2>
        <p class="text-center text-muted">Join EventEase today</p>
        <app-alert *ngIf="errorMessage" [message]="errorMessage" type="danger"></app-alert>
        <app-alert *ngIf="successMessage" [message]="successMessage" type="success"></app-alert>

        <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
          <div class="form-group">
            <label class="form-label">Username</label>
            <input type="text" formControlName="username" class="form-control" placeholder="Choose a username" />
          </div>
          <div class="form-group">
            <label class="form-label">Email</label>
            <input type="email" formControlName="email" class="form-control" placeholder="Enter your email" />
          </div>
          <div class="form-group">
            <label class="form-label">Password</label>
            <input type="password" formControlName="password" class="form-control" placeholder="Create a password" />
          </div>
          <button type="submit" class="btn btn-primary btn-wide" [disabled]="form.invalid || loading">
            {{ loading ? 'Creating account...' : 'Register' }}
          </button>
        </form>

        <p class="text-center text-muted mt-3">
          Already have an account? <a routerLink="/login" class="text-primary-link">Sign in</a>
        </p>
      </div>
    </div>
  `,
  styles: [`
    .text-primary-link { color: var(--color-primary); font-weight: 500; }
    .text-primary-link:hover { text-decoration: underline; }
  `]
})
export class RegisterComponent {
  form: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.register(this.form.value).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Account created successfully! Please login.';
        this.form.reset();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Registration failed. Username or email may already be taken.';
      }
    });
  }
}
