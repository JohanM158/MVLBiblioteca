import { Component, computed, inject, input } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ICONS } from './icons';

/**
 * Icono SVG themeable (hereda currentColor).
 * El markup se marca como confiable porque proviene de constantes estáticas
 * (ICONS), no de entrada del usuario → bypass seguro.
 */
@Component({
  selector: 'app-icon',
  standalone: true,
  template: `<span
    class="icon"
    [innerHTML]="svg()"
    [attr.role]="label() ? 'img' : null"
    [attr.aria-label]="label() || null"
    [attr.aria-hidden]="label() ? null : true"
  ></span>`,
  styles: [
    `
      :host {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        line-height: 0;
        flex-shrink: 0;
      }
      .icon {
        display: inline-flex;
        line-height: 0;
      }
      .icon svg {
        display: block;
      }
    `,
  ],
})
export class IconComponent {
  private sanitizer = inject(DomSanitizer);

  name = input.required<string>();
  size = input<number>(20);
  strokeWidth = input<number>(1.6);
  label = input<string>('');

  svg = computed<SafeHtml>(() => {
    const inner = ICONS[this.name()] ?? '';
    const s = this.size();
    const markup = `<svg xmlns="http://www.w3.org/2000/svg" width="${s}" height="${s}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="${this.strokeWidth()}" stroke-linecap="round" stroke-linejoin="round" focusable="false" aria-hidden="true">${inner}</svg>`;
    return this.sanitizer.bypassSecurityTrustHtml(markup);
  });
}