import { Component, OnInit, inject, signal, effect } from '@angular/core';
import { RouterLink } from '@angular/router';
import { StatsService, Stats } from '../../core/stats.service';
import { IconComponent } from '../../shared/icon/icon';
import { SkeletonComponent } from '../../shared/skeleton/skeleton';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, IconComponent, SkeletonComponent],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  private statsService = inject(StatsService);
  stats = signal<Stats | null>(null);
  loading = signal(true);

  animatedBooks = signal(0);
  private raf = 0;

  constructor() {
    effect(() => {
      const total = this.stats()?.totalBooks ?? 0;
      const reduce =
        typeof matchMedia !== 'undefined' &&
        matchMedia('(prefers-reduced-motion: reduce)').matches;
      if (reduce) {
        this.animatedBooks.set(total);
        return;
      }
      this.countUpTo(total);
    });
  }

  get inCirculation(): number {
    const s = this.stats();
    return s ? Math.max(0, s.totalBooks - s.availableBooks) : 0;
  }

  get shelfOccupancy(): number {
    const s = this.stats();
    if (!s || s.totalBooks === 0) return 0;
    return Math.round((this.inCirculation / s.totalBooks) * 100);
  }

  get todayLabel(): string {
    return new Date().toLocaleDateString('es-ES', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
      year: 'numeric',
    });
  }

  ngOnInit() {
    this.statsService.getAllStats().subscribe({
      next: data => { this.stats.set(data); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  private countUpTo(target: number): void {
    cancelAnimationFrame(this.raf);
    const start = performance.now();
    const duration = 950;
    const step = (now: number) => {
      const t = Math.min(1, (now - start) / duration);
      const eased = 1 - Math.pow(1 - t, 3);
      this.animatedBooks.set(Math.round(target * eased));
      if (t < 1) this.raf = requestAnimationFrame(step);
    };
    this.raf = requestAnimationFrame(step);
  }
}
