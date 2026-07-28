import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoanService, Loan } from '../../core/loan.service';
import { BookService, Book } from '../../core/book.service';
import { UserService, User } from '../../core/user.service';
import { NotificationService } from '../../core/notification.service';
import { IconComponent } from '../../shared/icon/icon';
import { SkeletonComponent } from '../../shared/skeleton/skeleton';

@Component({
  selector: 'app-loans',
  imports: [FormsModule, IconComponent, SkeletonComponent],
  templateUrl: './loans.html',
  styleUrl: './loans.css'
})
export class Loans implements OnInit {
  private loanService = inject(LoanService);
  private bookService = inject(BookService);
  private userService = inject(UserService);
  private notify = inject(NotificationService);

  loans = signal<Loan[]>([]);
  books = signal<Book[]>([]);
  users = signal<User[]>([]);
  loading = signal(true);
  showModal = signal(false);
  showReturnConfirm = signal(false);
  returningLoan = signal<Loan | null>(null);
  filterStatus = signal('ALL');

  form = { bookId: 0, userId: 0, notes: '' };

  ngOnInit() {
    this.loadAll();
  }

  loadAll() {
    this.loading.set(true);
    this.loanService.getAll().subscribe({
      next: data => { this.loans.set(data); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
    this.bookService.getAll().subscribe({ next: data => this.books.set(data) });
    this.userService.getAll().subscribe({ next: data => this.users.set(data) });
  }

  filteredLoans() {
    const status = this.filterStatus();
    if (status === 'ALL') return this.loans();
    return this.loans().filter(l => l.status === status);
  }

  availableBooks() {
    return this.books().filter(b => b.available);
  }

  activeUsers() {
    return this.users().filter(u => u.active);
  }

  openCreate() {
    this.form = { bookId: 0, userId: 0, notes: '' };
    this.showModal.set(true);
  }

  closeModal() { this.showModal.set(false); }

  createLoan() {
    if (!this.form.bookId || !this.form.userId) {
      this.notify.warning('Debes seleccionar un libro y un usuario');
      return;
    }
    this.loanService.create({
      bookId: this.form.bookId,
      userId: this.form.userId,
      notes: this.form.notes
    }).subscribe({
      next: () => { this.notify.success('Préstamo registrado exitosamente'); this.closeModal(); this.loadAll(); }
    });
  }

  confirmReturn(loan: Loan) {
    this.returningLoan.set(loan);
    this.showReturnConfirm.set(true);
  }

  returnBook() {
    const loan = this.returningLoan();
    if (loan) {
      this.loanService.returnBook(loan.id!).subscribe({
        next: () => { this.notify.success('Libro devuelto correctamente'); this.showReturnConfirm.set(false); this.loadAll(); }
      });
    }
  }

  cancelReturn() { this.showReturnConfirm.set(false); }

  isOverdue(loan: Loan): boolean {
    if (loan.status !== 'ACTIVE') return false;
    return new Date(loan.dueDate!) < new Date();
  }

  getStatusClass(loan: Loan): string {
    if (this.isOverdue(loan)) return 'badge-overdue';
    if (loan.status === 'RETURNED') return 'badge-returned';
    return 'badge-active';
  }

  getStatusText(loan: Loan): string {
    if (this.isOverdue(loan)) return '⚠ Vencido';
    if (loan.status === 'RETURNED') return '✓ Devuelto';
    return '● Activo';
  }
}
