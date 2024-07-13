import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Facility } from '../../../../models/Facility';
import { Exercise } from '../../../../models/Exercise';
import { ButtonComponent } from '../../../shared/button/button.component';
import { FormsModule } from '@angular/forms';
import { ExerciseService } from '../../../../services/exercise/exercise.service';

@Component({
  selector: 'app-book-an-exercise-form',
  standalone: true,
  imports: [ButtonComponent, FormsModule],
  templateUrl: './book-an-exercise-form.component.html',
})
export class BookAnExerciseFormComponent {
  @Input() facility!: Facility;
  @Output() successEvent = new EventEmitter<void>();
  formData!: Exercise;
  errorMessage: string = "";
  successMessage: string = "";

  constructor(private exerciseService: ExerciseService) { }

  ngOnInit() {
    this.formData = {
      facility: this.facility,
      from: new Date(),
      until: new Date(),
    }
  }

  onSubmit(): void {
    const fromDate = new Date(this.formData.from!);
    const untilDate = new Date(this.formData.until!);
    if (!this.formData.from || !this.formData.until) {
      this.errorMessage = "Invalid date range";
      return;
    }

     if (fromDate.getTime() === untilDate.getTime()) {
      this.errorMessage = "Select valid dates";
      setTimeout(() => { this.clearErrorMessage(); }, 5000);
      return;
  }
    if (fromDate.toDateString() !== untilDate.toDateString()) {
      this.errorMessage = "From and Until must be on the same day";
      setTimeout(() => { this.clearErrorMessage(); }, 5000)
      return;
    }
    this.clearErrorMessage();

    this.exerciseService.create(this.formData, this.facility.id!).subscribe(
      success => { 
        this.successMessage = "Exercise reserved successfully!"
        setTimeout(() => { this.clearSuccessMessage(); }, 5000)
        this.formData.from = new Date(), this.formData.until = new Date()
        this.successEvent.emit() }, 
      error => {
        this.errorMessage = error;
        setTimeout(() => { this.clearErrorMessage(); }, 5000)
      }
    );
  }

  clearSuccessMessage(): void {
    this.successMessage = "";
  }

  clearErrorMessage(): void {
    this.errorMessage = "";
  }

}
