import { Component } from '@angular/core';

@Component({
  selector: 'app-loading',
  standalone: true,
  template: `
    <div class="loading-container">
      <div class="loading-spinner"></div>
      <p class="text-muted mt-3">Loading...</p>
    </div>
  `,
  styles: []
})
export class LoadingComponent {}
