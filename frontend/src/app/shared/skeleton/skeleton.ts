import { Component, input } from '@angular/core';

/**
 * Placeholder de carga con la silueta del contenido real.
 * variant: 'block' | 'line' | 'circle'
 */
@Component({
  selector: 'app-skeleton',
  standalone: true,
  template: `
    @switch (variant()) {
      @case ('line') {
        <span class="skeleton sk-line" [style.width]="width()"></span>
      }
      @case ('circle') {
        <span class="skeleton sk-circle" [style.width.px]="size()" [style.height.px]="size()"></span>
      }
      @default {
        <span class="skeleton sk-block" [style.width]="width()" [style.height]="height()"></span>
      }
    }
  `,
  styles: [
    `
      :host {
        display: contents;
      }
    `,
  ],
})
export class SkeletonComponent {
  variant = input<'block' | 'line' | 'circle'>('block');
  width = input<string>('100%');
  height = input<string>('1rem');
  size = input<number>(40);
}