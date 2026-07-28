import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BookService, Book } from '../../core/book.service';
import { NotificationService } from '../../core/notification.service';
import { IconComponent } from '../../shared/icon/icon';
import { SkeletonComponent } from '../../shared/skeleton/skeleton';

@Component({
  selector: 'app-books',
  imports: [FormsModule, IconComponent, SkeletonComponent],
  templateUrl: './books.html',
  styleUrl: './books.css'
})
export class Books implements OnInit {
  private bookService = inject(BookService);
  private notify = inject(NotificationService);

  books = signal<Book[]>([]);
  loading = signal(true);
  showModal = signal(false);
  showDeleteConfirm = signal(false);
  editingBook = signal<Book | null>(null);
  deletingBook = signal<Book | null>(null);
  searchQuery = signal('');

  form: Book = { title: '', author: '', isbn: '', genre: '', year: undefined, description: '' };

  genres = ['Novela', 'Cuento', 'Ciencia Ficción', 'Fantasía', 'Realismo Mágico', 'Misterio', 'Poesía', 'Historia', 'Filosofía', 'Otro'];

  ngOnInit() { this.loadBooks(); }

  loadBooks() {
    this.loading.set(true);
    this.bookService.getAll().subscribe({
      next: data => { this.books.set(data); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  onSearch() {
    const q = this.searchQuery();
    if (q.length < 2) { this.loadBooks(); return; }
    this.bookService.search(q).subscribe({ next: data => this.books.set(data) });
  }

  openCreate() {
    this.form = { title: '', author: '', isbn: '', genre: '', year: undefined, description: '' };
    this.editingBook.set(null);
    this.showModal.set(true);
  }

  openEdit(book: Book) {
    this.form = { ...book };
    this.editingBook.set(book);
    this.showModal.set(true);
  }

  closeModal() { this.showModal.set(false); }

  save() {
    if (!this.form.title || !this.form.author) {
      this.notify.warning('Título y autor son obligatorios');
      return;
    }
    const editing = this.editingBook();
    if (editing) {
      this.bookService.update(editing.id!, this.form).subscribe({
        next: () => { this.notify.success('Libro actualizado correctamente'); this.closeModal(); this.loadBooks(); }
      });
    } else {
      this.bookService.create(this.form).subscribe({
        next: () => { this.notify.success('Libro creado correctamente'); this.closeModal(); this.loadBooks(); }
      });
    }
  }

  confirmDelete(book: Book) {
    this.deletingBook.set(book);
    this.showDeleteConfirm.set(true);
  }

  deleteBook() {
    const book = this.deletingBook();
    if (book) {
      this.bookService.delete(book.id!).subscribe({
        next: () => { this.notify.success('Libro eliminado'); this.showDeleteConfirm.set(false); this.loadBooks(); }
      });
    }
  }

  cancelDelete() { this.showDeleteConfirm.set(false); }
}
