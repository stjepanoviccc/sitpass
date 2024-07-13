import { Component } from '@angular/core';
import { WrapComponent } from '../../../components/shared/wrap/wrap.component';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [WrapComponent],
  templateUrl: './not-found.component.html',
})
export class NotFoundComponent {
  constructor() { }
}
