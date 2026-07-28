import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService, User } from '../../core/user.service';
import { NotificationService } from '../../core/notification.service';
import { IconComponent } from '../../shared/icon/icon';
import { SkeletonComponent } from '../../shared/skeleton/skeleton';

@Component({
  selector: 'app-users',
  imports: [FormsModule, IconComponent, SkeletonComponent],
  templateUrl: './users.html',
  styleUrl: './users.css'
})
export class Users implements OnInit {
  private userService = inject(UserService);
  private notify = inject(NotificationService);

  users = signal<User[]>([]);
  loading = signal(true);
  showModal = signal(false);
  showDeleteConfirm = signal(false);
  editingUser = signal<User | null>(null);
  deletingUser = signal<User | null>(null);
  searchQuery = signal('');

  form: User = { firstName: '', lastName: '', email: '', phone: '' };

  ngOnInit() { this.loadUsers(); }

  loadUsers() {
    this.loading.set(true);
    this.userService.getAll().subscribe({
      next: data => { this.users.set(data); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  onSearch() {
    const q = this.searchQuery();
    if (q.length < 2) { this.loadUsers(); return; }
    this.userService.search(q).subscribe({ next: data => this.users.set(data) });
  }

  openCreate() {
    this.form = { firstName: '', lastName: '', email: '', phone: '' };
    this.editingUser.set(null);
    this.showModal.set(true);
  }

  openEdit(user: User) {
    this.form = { ...user };
    this.editingUser.set(user);
    this.showModal.set(true);
  }

  closeModal() { this.showModal.set(false); }

  save() {
    if (!this.form.firstName || !this.form.lastName || !this.form.email) {
      this.notify.warning('Nombre, apellido y email son obligatorios');
      return;
    }
    const editing = this.editingUser();
    if (editing) {
      this.userService.update(editing.id!, this.form).subscribe({
        next: () => { this.notify.success('Usuario actualizado correctamente'); this.closeModal(); this.loadUsers(); }
      });
    } else {
      this.userService.create(this.form).subscribe({
        next: () => { this.notify.success('Usuario creado correctamente'); this.closeModal(); this.loadUsers(); }
      });
    }
  }

  confirmDelete(user: User) {
    this.deletingUser.set(user);
    this.showDeleteConfirm.set(true);
  }

  deleteUser() {
    const user = this.deletingUser();
    if (user) {
      this.userService.delete(user.id!).subscribe({
        next: () => { this.notify.success('Usuario eliminado'); this.showDeleteConfirm.set(false); this.loadUsers(); }
      });
    }
  }

  cancelDelete() { this.showDeleteConfirm.set(false); }
}
