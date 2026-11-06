export interface User {
  id: number;
  username: string;
  email: string;
  role: string;
}

export interface Category {
  id: number;
  name: string;
}

export interface Event {
  id: number;
  title: string;
  description: string;
  categoryId: number;
  categoryName: string;
  eventDate: string;
  eventTime: string;
  venue: string;
  capacity: number;
  organizerName: string;
  status: string;
}

export interface Registration {
  id: number;
  userId: number;
  eventId: number;
  eventTitle: string;
  registrationDate: string;
  status: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  role: string;
}

export interface AdminStats {
  totalEvents: number;
  totalRegistrations: number;
  publishedEvents: number;
  cancelledEvents: number;
  completedEvents: number;
  activeRegistrations: number;
}

export interface UserDashboard {
  upcomingRegistrations: Registration[];
  registrationHistory: Registration[];
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface EventRequest {
  title: string;
  description: string;
  categoryId: number;
  eventDate: string;
  eventTime: string;
  venue: string;
  capacity: number;
  organizerName: string;
  status: string;
}

export interface RegistrationRequest {
  eventId: number;
}
