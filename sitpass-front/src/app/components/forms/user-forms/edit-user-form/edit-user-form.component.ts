import { Component, Input } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../../../shared/button/button.component';
import { UserService } from '../../../../services/user/user.service';
import { FileService } from '../../../../services/file/file.service';
import { User } from '../../../../models/User';

@Component({
  selector: 'app-edit-user-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, ButtonComponent],
  templateUrl: './edit-user-form.component.html',
})
export class EditUserFormComponent {
  @Input() emailFromToken: string | null = null;
  profileForm!: FormGroup;
  currentImageUrl: string = '';
  newImageUrl: string = '';
  successMessage: string | undefined;

  constructor(private formBuilder: FormBuilder, private userService: UserService, private fileService: FileService) {
    this.profileForm = new FormGroup({});
  }

  ngOnInit(): void {
    this.profileForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      name: [''],
      surname: [''],
      phoneNumber: ['', [Validators.pattern('^[0-9]{8,10}$')]],
      birthday: ['', [this.birthdayValidator]],
      address: ['', [Validators.required, Validators.nullValidator]],
      city: [''],
      zipCode: ['', [Validators.pattern('^[0-9]{4,6}$')]],
      id: ['', [Validators.required, Validators.nullValidator]],
      password: [''],
      isDeleted: [''],
      createdAt: ['']
    });

    this.userService.findByEmail(this.emailFromToken!).subscribe((data: User) => {
      this.profileForm.patchValue(data);
    });
    this.fileService.findByUser().subscribe((img) => {
      if(img != null) {
        this.fileService.getFileContent(img.path).subscribe((blob) => {
          this.currentImageUrl = blob;
        });
      }
    })
  }

  onSubmit(): void {
    this.profileForm.markAllAsTouched();
    this.userService.update(this.profileForm.value, this.newImageUrl).subscribe(() => {
      this.successMessage = "Profile updated successfully!";
      this.getImage();
      setTimeout(() => { this.successMessage = "" }, 5000)
    });
  }

  birthdayValidator(control: { value: string | number | Date; }) {
    const inputDate = new Date(control.value);
    const currentDate = new Date();
    if (inputDate >= currentDate) {
      return { 'futureDate': true };
    }
    return null;
  }

  onFileSelected(event: any):void {
    const file: File = event.target.files[0];
    this.newImageUrl = file.name;
  }

  getImage():void {
    this.fileService.findByUser().subscribe((img) => {
      if(img != null) {
        this.fileService.getFileContent(img.path).subscribe((blob) => {
          this.currentImageUrl = blob;
        });
      }
    })
  }

}
