import { Component, Input, Renderer2 } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal.component.html',
})
export class ModalComponent {
  @Input() showModal: boolean = false;

  constructor(private renderer: Renderer2) { }

  ngOnChanges() {
    if (this.showModal) {
      this.renderer.setStyle(document.body, 'overflow', 'hidden');
    } else {
      this.renderer.removeStyle(document.body, 'overflow');
    }
  }

  ngOnDestroy() {
    this.renderer.removeStyle(document.body, 'overflow');
  }
}
