import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-alert',
  standalone: true,
  template: `
    <div *ngIf="message" class="alert" [ngClass]="type === 'success' ? 'alert-success' : 'alert-danger'">
      {{ message }}
    </div>
  `,
  styles: [`
    :host { display: block; }
  `]
})
export class AlertComponent {
  @Input() message = '';
  @Input() type: 'success' | 'danger' = 'danger';
}
