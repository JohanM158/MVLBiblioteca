import { Component, effect, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { NotificationService } from './core/notification.service';
import { IconComponent } from './shared/icon/icon';

type Theme = 'light' | 'dark';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, IconComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected notify = inject(NotificationService);
  protected sidebarOpen = signal(false);

  // Valor inicial: lo guardado, o la preferencia del SO, o claro.
  protected theme = signal<Theme>(this.readInitialTheme());

  constructor() {
    // Cada cambio de tema se refleja en <html> (escritura DOM segura, sin ciclo).
    effect(() => {
      document.documentElement.setAttribute('data-theme', this.theme());
    });
  }

  toggleSidebar() {
    this.sidebarOpen.update((v) => !v);
  }
  closeSidebar() {
    this.sidebarOpen.set(false);
  }
  toggleTheme() {
    this.theme.update((t) => {
      const next: Theme = t === 'dark' ? 'light' : 'dark';
      try {
        localStorage.setItem('bvm-theme', next);
      } catch {
        /* almacenamiento no disponible: no bloqueamos */
      }
      return next;
    });
  }

  private readInitialTheme(): Theme {
    try {
      const saved = localStorage.getItem('bvm-theme');
      if (saved === 'light' || saved === 'dark') return saved;
    } catch {
      /* ignore */
    }
    return typeof matchMedia !== 'undefined' && matchMedia('(prefers-color-scheme: dark)').matches
      ? 'dark'
      : 'light';
  }
}