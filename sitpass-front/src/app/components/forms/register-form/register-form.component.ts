import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ButtonComponent } from '../../shared/button/button.component';
import { AccountRequest } from '../../../models/AccountRequest';
import { RegisterService } from '../../../services/auth/register.service';

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink, ButtonComponent],
  templateUrl: './register-form.component.html',
})
export class RegisterFormComponent {
  formData: AccountRequest = {
    email: '',
    password: '',
    address: ''
  };

  errorMessage: string = '';

  constructor(private registerService: RegisterService, private router: Router) { }

  sendAccountRequests() {
    const { email, password, address } = this.formData;

    if (!email || !password || !address) {
      this.errorMessage = 'Empty fields not allowed.';
      setTimeout(() => { this.errorMessage = "" }, 5000)
      return;
    }

    this.clearErrorMessage

    this.registerService.sendAccountRequests(email, password, address).subscribe(
      () => this.router.navigate(['/login']),
      () => {
        this.errorMessage = 'User with this email already exists'
        setTimeout(() => { this.errorMessage = "" }, 5000)
      }
    );
  }

  clearErrorMessage() { this.errorMessage = ''; }
}
