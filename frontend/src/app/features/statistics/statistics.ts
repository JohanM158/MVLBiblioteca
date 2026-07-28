import { Component, OnInit, inject, signal } from '@angular/core';
import { StatsService, Stats, TopBook, MonthlyLoan, GenreDistribution, ActiveUser } from '../../core/stats.service';
import { IconComponent } from '../../shared/icon/icon';
import { SkeletonComponent } from '../../shared/skeleton/skeleton';

@Component({
  selector: 'app-statistics',
  imports: [IconComponent, SkeletonComponent],
  templateUrl: './statistics.html',
  styleUrl: './statistics.css'
})
export class Statistics implements OnInit {
  private statsService = inject(StatsService);

  stats = signal<Stats | null>(null);
  loading = signal(true);

  barColors = ['bar-purple', 'bar-cyan', 'bar-rose', 'bar-emerald', 'bar-amber', 'bar-blue'];

  ngOnInit() {
    this.statsService.getAllStats().subscribe({
      next: data => { this.stats.set(data); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  getMaxLoanCount(): number {
    const topBooks = this.stats()?.topBooks;
    if (!topBooks || topBooks.length === 0) return 1;
    return Math.max(...topBooks.map(b => b.loanCount));
  }

  getMaxMonthlyCount(): number {
    const monthly = this.stats()?.loansByMonth;
    if (!monthly || monthly.length === 0) return 1;
    return Math.max(...monthly.map(m => m.count));
  }

  getBarHeight(count: number, max: number): string {
    if (max === 0) return '4px';
    return Math.max(4, (count / max) * 100) + '%';
  }

  getBarColor(index: number): string {
    return this.barColors[index % this.barColors.length];
  }

  getRankClass(index: number): string {
    if (index === 0) return 'rank-1';
    if (index === 1) return 'rank-2';
    if (index === 2) return 'rank-3';
    return 'rank-default';
  }
}
