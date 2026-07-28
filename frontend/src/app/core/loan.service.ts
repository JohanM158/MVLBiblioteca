import { Injectable, inject } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

export interface Loan {
  id?: number; bookId: number; userId: number;
  bookTitle?: string; bookAuthor?: string; userName?: string; userEmail?: string;
  loanDate?: string; dueDate?: string; returnDate?: string;
  status?: string; notes?: string;
}

@Injectable({ providedIn: 'root' })
export class LoanService {
  private api = inject(ApiService);

  getAll(): Observable<Loan[]> { return this.api.get<Loan[]>('loans'); }
  getById(id: number): Observable<Loan> { return this.api.get<Loan>(`loans/${id}`); }
  getByUser(userId: number): Observable<Loan[]> { return this.api.get<Loan[]>(`loans/user/${userId}`); }
  create(loan: Loan): Observable<Loan> { return this.api.post<Loan>('loans', loan); }
  returnBook(id: number): Observable<Loan> { return this.api.put<Loan>(`loans/${id}/return`); }
}
