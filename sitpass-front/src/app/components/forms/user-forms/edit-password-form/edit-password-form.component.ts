import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from '../../../shared/button/button.component';
import { UserService } from '../../../../services/user/user.service';
import { User } from '../../../../models/User';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-edit-password-form',
  standalone: true,
  imports: [ButtonComponent, CommonModule, FormsModule],
  templateUrl: './edit-password-form.component.html',
})
export class EditPasswordFormComponent {
  @Input() emailFromToken: string | null = null;
  @Input() extendClass?: string;
  @Input() toggleChangePassword!: () => void;

  loadedFormData = {
    id: -1,
    password: ''
  }

  formData = {
    typedCurrentPassword: "",
    newPassword1: "",
    newPassword2: "",
    errorMessage: "",
    successMessage: ""
  }

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.findByEmail(this.emailFromToken!).subscribe((data: User) => {
      if (data.id !== null && data.id !== undefined) {
        this.loadedFormData.password = data.password;
        this.loadedFormData.id = data.id;
      }
    });
  }

  onSubmit(): void {
    this.passwordValidator(this.loadedFormData.id, this.formData.typedCurrentPassword!, this.formData.newPassword1!, this.formData.newPassword2!)
      .subscribe(validator => {
        if (validator.isValid) {
          this.loadedFormData.password = this.formData.newPassword1!;
          this.userService.changePassword(this.loadedFormData.id, this.loadedFormData.password)
            .subscribe(data => {
              this.loadedFormData.password = data.password;
              this.formData = {
                typedCurrentPassword: "",
                newPassword1: "",
                newPassword2: "",
                errorMessage: "",
                successMessage: "Success!"
              };
              setTimeout(() => {  this.formData.successMessage = "";  }, 5000)
            }, error => { console.log(error); });
        } else {
          this.formData.errorMessage = validator.errorMessage;
        }
      });
  }

  passwordValidator(id: number, typedCurrentPassword: string, newPassword1: string, newPassword2: string): Observable<{ isValid: boolean, errorMessage: string }> {
    if (newPassword1.length < 5 || newPassword2.length < 5) {
      return of({ isValid: false, errorMessage: "Password must be at least 5 characters long!" });
    }
    return new Observable<{ isValid: boolean, errorMessage: string }>(observer => {
      this.userService.currentPasswordValidator(id, typedCurrentPassword).subscribe(data => {
        let validationResult;
        if (data) {
          if (newPassword1 == newPassword2) {
            validationResult = { isValid: true, errorMessage: "" }
          } else {
            validationResult = { isValid: false, errorMessage: "New password isn't same" }
          }
        } else {
          validationResult = { isValid: false, errorMessage: "Current password isn't same." };
        }
        observer.next(validationResult);
        observer.complete();
      });
    });
  }
}