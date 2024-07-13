import { Component } from '@angular/core';
import { WrapComponent } from '../../components/shared/wrap/wrap.component';
import { RegisterFormComponent } from '../../components/forms/register-form/register-form.component';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [WrapComponent, RegisterFormComponent],
  templateUrl: './register-page.component.html',
})
export class RegisterPageComponent {

}
