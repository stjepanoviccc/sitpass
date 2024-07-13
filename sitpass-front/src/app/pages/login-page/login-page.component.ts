import { Component } from '@angular/core';
import { WrapComponent } from '../../components/shared/wrap/wrap.component';
import { LoginFormComponent } from '../../components/forms/login-form/login-form.component';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [WrapComponent, LoginFormComponent],
  templateUrl: './login-page.component.html',
})
export class LoginPageComponent {

}
