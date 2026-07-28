import { Injectable, inject } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

export interface Book {
  id?: number; title: string; author: string; isbn?: string;
  genre?: string; year?: number; description?: string;
  available?: boolean; createdAt?: string; updatedAt?: string;
}

@Injectable({ providedIn: 'root' })
export class BookService {
  private api = inject(ApiService);

  getAll(): Observable<Book[]> { return this.api.get<Book[]>('books'); }
  getById(id: number): Observable<Book> { return this.api.get<Book>(`books/${id}`); }
  search(query: string): Observable<Book[]> { return this.api.get<Book[]>(`books/search?q=${query}`); }
  create(book: Book): Observable<Book> { return this.api.post<Book>('books', book); }
  update(id: number, book: Book): Observable<Book> { return this.api.put<Book>(`books/${id}`, book); }
  delete(id: number): Observable<void> { return this.api.delete<void>(`books/${id}`); }
}
