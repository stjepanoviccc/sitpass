import { Component } from '@angular/core';
import { WrapComponent } from '../../../components/shared/wrap/wrap.component';

@Component({
  selector: 'app-no-permission',
  standalone: true,
  imports: [WrapComponent],
  templateUrl: './no-permission.component.html',
})
export class NoPermissionComponent {
  constructor() { }
}
