import { Injectable, inject } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

export interface Stats {
  totalBooks: number; totalUsers: number; activeLoans: number;
  overdueLoans: number; availableBooks: number;
  topBooks?: TopBook[]; loansByMonth?: MonthlyLoan[];
  genreDistribution?: GenreDistribution[]; activeUsers?: ActiveUser[];
}

export interface TopBook { bookId: number; title: string; author: string; loanCount: number; }
export interface MonthlyLoan { month: string; count: number; }
export interface GenreDistribution { genre: string; count: number; percentage: number; }
export interface ActiveUser { userId: number; name: string; email: string; loanCount: number; }

@Injectable({ providedIn: 'root' })
export class StatsService {
  private api = inject(ApiService);

  getAllStats(): Observable<Stats> { return this.api.get<Stats>('stats'); }
  getSummary(): Observable<Stats> { return this.api.get<Stats>('stats/summary'); }
  getTopBooks(): Observable<TopBook[]> { return this.api.get<TopBook[]>('stats/top-books'); }
  getLoansByMonth(): Observable<MonthlyLoan[]> { return this.api.get<MonthlyLoan[]>('stats/loans-by-month'); }
  getGenreDistribution(): Observable<GenreDistribution[]> { return this.api.get<GenreDistribution[]>('stats/genre-distribution'); }
  getActiveUsers(): Observable<ActiveUser[]> { return this.api.get<ActiveUser[]>('stats/active-users'); }
}
