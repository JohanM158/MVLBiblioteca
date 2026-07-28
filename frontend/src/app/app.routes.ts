import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard').then(m => m.Dashboard) },
  { path: 'books', loadComponent: () => import('./features/books/books').then(m => m.Books) },
  { path: 'users', loadComponent: () => import('./features/users/users').then(m => m.Users) },
  { path: 'loans', loadComponent: () => import('./features/loans/loans').then(m => m.Loans) },
  { path: 'statistics', loadComponent: () => import('./features/statistics/statistics').then(m => m.Statistics) },
  { path: '**', redirectTo: 'dashboard' }
];
