import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../../../shared/button/button.component';
import { ManagesService } from '../../../../services/manages/manages.service';
import { Manages } from '../../../../models/Manages';

@Component({
  selector: 'app-create-manages-form',
  standalone: true,
  imports: [FormsModule, CommonModule, ButtonComponent],
  templateUrl: './create-manages-form.component.html',
})
export class CreateManagesFormComponent {
  @Output() managesAdded: EventEmitter<Manages> = new EventEmitter<Manages>();
  formData: any = {
    email: '',
    name: '',
    startDate: Date,
    endDate: Date
  };

  errorMessage: string = '';

  constructor(private managesService: ManagesService) { }

  onSubmit() {
    const { email, name, startDate, endDate } = this.formData;

    if (!email || !name || !startDate || !endDate) {
      this.errorMessage = 'Empty fields not allowed.';
      setTimeout(() => { this.errorMessage = "" }, 5000)
      return;
    }

    this.clearErrorMessage();

    this.managesService.create(email, name, startDate, endDate).subscribe(
      (addedManages) => {
        this.managesAdded.emit(addedManages)
        this.resetForm();
      },
      err => {
        this.errorMessage = err
        setTimeout(() => { this.errorMessage = '' }, 5000)
      }
    );
  }

  clearErrorMessage() { this.errorMessage = ''; }

  resetForm() {
    this.formData = {
      email: '',
      name: '',
      startDate: null,
      endDate: null
    };
  }
}
