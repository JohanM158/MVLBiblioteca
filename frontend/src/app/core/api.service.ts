import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from './notification.service';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';
  private http = inject(HttpClient);
  private notify = inject(NotificationService);

  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}/${endpoint}`).pipe(catchError(err => this.handleError(err)));
  }

  post<T>(endpoint: string, body: any): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}/${endpoint}`, body).pipe(catchError(err => this.handleError(err)));
  }

  put<T>(endpoint: string, body?: any): Observable<T> {
    return this.http.put<T>(`${this.baseUrl}/${endpoint}`, body || {}).pipe(catchError(err => this.handleError(err)));
  }

  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(`${this.baseUrl}/${endpoint}`).pipe(catchError(err => this.handleError(err)));
  }

  private handleError(error: HttpErrorResponse) {
    let message = 'Error de conexión con el servidor';
    if (error.error) {
      if (error.error.message) message = error.error.message;
      else if (error.error.details) message = Object.values(error.error.details).join(', ');
    }
    this.notify.error(message);
    return throwError(() => error);
  }
}
