import { Routes } from '@angular/router';
import { AuthGuard, AdminGuard } from './core/guards/auth.guard';
import { HomeComponent } from './features/home/home.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { EventListingComponent } from './features/events/event-listing/event-listing.component';
import { EventDetailsComponent } from './features/events/event-details/event-details.component';
import { AdminDashboardComponent } from './features/admin/admin-dashboard/admin-dashboard.component';
import { EventManagementComponent } from './features/admin/event-management/event-management.component';
import { AddEventComponent } from './features/admin/add-event/add-event.component';
import { EditEventComponent } from './features/admin/edit-event/edit-event.component';
import { UserDashboardComponent } from './features/user/user-dashboard/user-dashboard.component';
import { MyRegistrationsComponent } from './features/user/my-registrations/my-registrations.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'events', component: EventListingComponent },
  { path: 'events/:id', component: EventDetailsComponent },
  { path: 'my-registrations', component: MyRegistrationsComponent, canActivate: [AuthGuard] },
  { path: 'user-dashboard', component: UserDashboardComponent, canActivate: [AuthGuard] },
  { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [AdminGuard] },
  { path: 'admin/events', component: EventManagementComponent, canActivate: [AdminGuard] },
  { path: 'admin/events/add', component: AddEventComponent, canActivate: [AdminGuard] },
  { path: 'admin/events/edit/:id', component: EditEventComponent, canActivate: [AdminGuard] },
  { path: '**', redirectTo: '' }
];
