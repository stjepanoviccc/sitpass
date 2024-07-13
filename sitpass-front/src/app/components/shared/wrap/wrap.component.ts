import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-wrap',
  standalone: true,
  imports: [],
  templateUrl: './wrap.component.html',
})
export class WrapComponent {
  @Input() extendClass?: string;
}
