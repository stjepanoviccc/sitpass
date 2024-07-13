import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ButtonComponent } from '../../shared/button/button.component';
import { LoginService } from '../../../services/auth/login.service';
import { LoginRequest } from '../../../models/LoginRequest';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink, ButtonComponent],
  templateUrl: './login-form.component.html',
})
export class LoginFormComponent {
  formData: LoginRequest = {
    email: '',
    password: ''
  };

  errorMessage: string = '';

  constructor(private loginService: LoginService, private router: Router) { }

  login() {
    const { email, password } = this.formData;

    if (!email || !password) {
      this.errorMessage = 'Empty fields not allowed.';
      setTimeout(() => { this.errorMessage = "" }, 5000)
      return;
    }

    this.clearErrorMessage

    this.loginService.login(email, password).subscribe(
      () => this.router.navigate(['/']),
      err => {
        this.errorMessage = 'Invalid email or password'
        setTimeout(() => { this.errorMessage = "" }, 5000)
      }
    );
  }

  clearErrorMessage() {
    this.errorMessage = '';
  }
}
