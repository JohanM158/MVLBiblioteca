import { Injectable, inject } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

export interface User {
  id?: number; firstName: string; lastName: string; email: string;
  phone?: string; membershipId?: string; registrationDate?: string; active?: boolean;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private api = inject(ApiService);

  getAll(): Observable<User[]> { return this.api.get<User[]>('users'); }
  getById(id: number): Observable<User> { return this.api.get<User>(`users/${id}`); }
  search(query: string): Observable<User[]> { return this.api.get<User[]>(`users/search?q=${query}`); }
  create(user: User): Observable<User> { return this.api.post<User>('users', user); }
  update(id: number, user: User): Observable<User> { return this.api.put<User>(`users/${id}`, user); }
  delete(id: number): Observable<void> { return this.api.delete<void>(`users/${id}`); }
}
