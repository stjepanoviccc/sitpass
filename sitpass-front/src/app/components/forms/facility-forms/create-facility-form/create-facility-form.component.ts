import { Component, EventEmitter, Output } from '@angular/core';
import { Facility } from '../../../../models/Facility';
import { FacilityService } from '../../../../services/facility/facility.service';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormArray, FormsModule } from '@angular/forms';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ButtonComponent } from '../../../shared/button/button.component';
import { DayOfWeek } from '../../../../models/enums/DayOfWeek';

@Component({
  selector: 'app-create-facility-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule, ButtonComponent],
  templateUrl: './create-facility-form.component.html',
})
export class CreateFacilityFormComponent {
  @Output() facilityAdded: EventEmitter<Facility> = new EventEmitter<Facility>();
  formData!: FormGroup;
  daysOfWeek = Object.values(DayOfWeek);
  errorMsg: string = '';

  constructor(private formBuilder: FormBuilder, private facilityService: FacilityService) { };

  ngOnInit() {
    this.formData = this.formBuilder.group({
      name: ['', [Validators.required]],
      city: ['', [Validators.required]],
      address: ['', Validators.required],
      description: ['', Validators.required],
      disciplines: this.formBuilder.array([]),
      workDays: this.formBuilder.array([]),
      images: this.formBuilder.array([])
    }, { validator: this.validateArrays });
  }

  onSubmit(): void {
    const duplicate = this.checkForDuplicateDays();
    const workDaysArray = this.formData.get('workDays') as FormArray;

    if (duplicate) {
      workDaysArray.setErrors({ duplicateDay: true });
      return;
    } else {
      workDaysArray.setErrors(null);
    }

    const facility = this.formData.value as Facility;

    const imagePaths: { path: string }[] = [];
    if (facility.images && facility.images.length > 0) {
      facility.images.forEach((file: File) => {
        imagePaths.push({ path: file.name });
      });
    }

    facility.images = imagePaths;

    this.facilityService.create(facility).subscribe((addedFacility) => {
      this.facilityAdded.emit(addedFacility);
      this.clearFormArray(this.formData.get('images') as FormArray);
      this.clearFormArray(this.formData.get('disciplines') as FormArray);
      this.clearFormArray(this.formData.get('workDays') as FormArray);
      this.formData.reset();

    },
    (err) => {
      this.errorMsg = err;
      setTimeout(() => {  this.errorMsg = ""; }, 5000);
    }
    );
  }

  clearFormArray(formArray: FormArray) {
    while (formArray.length !== 0) {
      formArray.removeAt(0);
    }
    document.querySelector('#imagesContainer')!.innerHTML = `
      <label>Images:</label>
        <input type="file" name="images" multiple (change)="onFileSelected($event)" class="my-input-secondary" />`;
  }

  get disciplineControls() {
    return this.formData.get('disciplines') as FormArray;
  }

  get workDayControls() {
    return this.formData.get('workDays') as FormArray;
  }

  addDiscipline() {
    this.disciplineControls.push(this.formBuilder.group({
      name: ['', Validators.required]
    }));
  }

  removeDiscipline(index: number) {
    this.disciplineControls.removeAt(index);
  }

  addWorkDay() {
    this.workDayControls.push(this.createWorkDayGroup());
  }

  removeWorkDay(index: number) {
    this.workDayControls.removeAt(index);
  }

  createWorkDayGroup() {
    return this.formBuilder.group({
      day: ['', Validators.required],
      from: ['', Validators.required],
      until: ['', Validators.required]
    });
  }

  checkForDuplicateDays(): boolean {
    const workDaysArray = this.formData.get('workDays') as FormArray;
    const days = workDaysArray.controls.map((control: AbstractControl) => control.get('day')?.value);
    const uniqueDays = new Set<string>();

    for (const day of days) {
      if (uniqueDays.has(day)) {
        return true;
      } else {
        uniqueDays.add(day);
      }
    }

    return false;
  }

  validateArrays(form: FormGroup) {
    const disciplines = form.get('disciplines') as FormArray;
    const workDays = form.get('workDays') as FormArray;

    if (disciplines.length === 0) {
      disciplines.setErrors({ required: true });
    } else {
      disciplines.setErrors(null);
    }

    if (workDays.length === 0) {
      workDays.setErrors({ required: true });
    } else {
      workDays.setErrors(null);
    }
  }

  onFileSelected(event: any): void {
    const files: FileList = event.target.files;
    if (files && files.length > 0) {
      const selectedImagesArray = this.formData.get('images') as FormArray;
      for (let i = 0; i < files.length; i++) {
        const file: File = files[i];
        selectedImagesArray.push(this.formBuilder.control(file));
      }
    }
  }
}
