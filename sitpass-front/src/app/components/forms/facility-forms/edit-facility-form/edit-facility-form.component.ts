import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  ReactiveFormsModule,
  FormArray,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { DayOfWeek } from '../../../../models/enums/DayOfWeek';
import { ButtonComponent } from '../../../shared/button/button.component';
import { Facility } from '../../../../models/Facility';
import { LoginService } from '../../../../services/auth/login.service';
import { FacilityService } from '../../../../services/facility/facility.service';
import { ManagesService } from '../../../../services/manages/manages.service';
import { FileService } from '../../../../services/file/file.service';

@Component({
  selector: 'app-edit-facility-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonComponent],
  templateUrl: './edit-facility-form.component.html',
})
export class EditFacilityFormComponent {
  facilityId!: number;
  facility!: Facility;
  formData!: FormGroup;
  daysOfWeek = Object.values(DayOfWeek);
  imageUrls: string[] = [];
  errorMsg: string = '';
  isUserManager: Boolean = false;
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private facilityService: FacilityService,
    public loginService: LoginService,
    public managesService: ManagesService,
    private fileService: FileService,
    private location: Location
  ) {}

  ngOnInit() {
    this.initForm();
    this.route.params.subscribe((params) => {
      this.facilityId = +params['id'];
      this.isManager();
      this.getFacility();
    });
  }

  isManager(): void {
    this.managesService.isManager(this.facilityId).subscribe((state) => this.isUserManager = state )
  }

  getFacility() {
    this.facilityService.findById(this.facilityId).subscribe(
      (facility: Facility) => {
        if (!facility) {
          this.router.navigate(['/error']);
        } else {
          this.facility = facility;
          this.updateFormAfterFacilityInit();
        }
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  initForm() {
    this.formData = this.formBuilder.group(
      {
        id: [''],
        name: ['', [Validators.required]],
        city: ['', [Validators.required]],
        address: ['', Validators.required],
        description: ['', Validators.required],
        disciplines: this.formBuilder.array([]),
        workDays: this.formBuilder.array([]),
        images: this.formBuilder.array([]),
      },
      { validator: this.validateArrays }
    );
  }

  get disciplineControls() {
    return this.formData.get('disciplines') as FormArray;
  }

  get workDayControls() {
    return this.formData.get('workDays') as FormArray;
  }

  get imageControls() {
    return this.formData.get('images') as FormArray;
  }

  updateFormAfterFacilityInit() {
    this.formData = this.formBuilder.group(
      {
        id: [this.facility.id],
        name: [this.facility.name, [Validators.required]],
        city: [this.facility.city, [Validators.required]],
        address: [this.facility.address, Validators.required],
        description: [this.facility.description, Validators.required],
        disciplines: this.formBuilder.array([]),
        workDays: this.formBuilder.array([]),
        images: this.formBuilder.array([]),
      },
      { validator: this.validateArrays }
    );

    const disciplinesArray = this.formData.get('disciplines') as FormArray;
    this.facility.disciplines!.forEach((discipline) => {
      if(discipline.isDeleted != true) {
        disciplinesArray.push(
          this.formBuilder.group({
            id: [discipline.id],
            name: [discipline.name, Validators.required],
            facility: [discipline.facility],
            isDeleted: [discipline.isDeleted]
          })
        );
      }
    });

    const workDaysArray = this.formData.get('workDays') as FormArray;
    this.facility.workDays!.forEach((workDay) => {
      if(workDay.isDeleted != true) {
        workDaysArray.push(
          this.formBuilder.group({
            id: [workDay.id],
            validFrom: [workDay.validFrom],
            isDeleted: [workDay.isDeleted],
            facility: [workDay.facility],
            day: [workDay.day, Validators.required],
            from: [workDay.from, Validators.required],
            until: [workDay.until, Validators.required],
          })
        );
      }
    });

    const imagesArray = this.formData.get('images') as FormArray;
    if(this.facility.images) {
      this.facility.images.forEach((img) => {
        if(img.isDeleted != true) {
          imagesArray.push(
            this.formBuilder.group({
              id: [img.id],
              isDeleted: [img.isDeleted],
              path: [img.path],
              facility: [img.facility],
              user: [img.user]
            })
          )
          this.fileService.getFileContent(img.path).subscribe(
            (imageUrl: string) => {
              this.imageUrls.push(imageUrl);
            },
            (error: any) => {
              console.error("Error fetching image:", error);
            }
          )
        }
      });
    } 
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

    this.facility = this.formData.value as Facility;

    this.facilityService.update(this.facility).subscribe(
      () => {
        this.clearFormArray(this.formData.get('disciplines') as FormArray);
        this.clearFormArray(this.formData.get('workDays') as FormArray);
        this.clearFormArray(this.formData.get('images') as FormArray);
        this.formData.reset();
        this.location.back();
      },
      (err) => {
        this.errorMsg = err;
        setTimeout(() => {
          this.errorMsg = '';
        }, 5000);
      }
    );    
  }

  clearFormArray(formArray: FormArray) {
    while (formArray.length !== 0) {
      formArray.removeAt(0);
    }
  }

  addDiscipline() {
    this.disciplineControls.push(
      this.formBuilder.group({
        name: ['', Validators.required],
      })
    );
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

  addImage() {
    this.imageControls.push(this.formBuilder.group({
      path: ['']
    }))
  }

  removeImage(index: number) {
    this.imageControls.removeAt(index);
    this.imageUrls.splice(index, 1);
  }

  createWorkDayGroup() {
    return this.formBuilder.group({
      day: ['', Validators.required],
      from: ['', Validators.required],
      until: ['', Validators.required],
    });
  }

  checkForDuplicateDays(): boolean {
    const workDaysArray = this.formData.get('workDays') as FormArray;
    const days = workDaysArray.controls.map(
      (control: AbstractControl) => control.get('day')?.value
    );
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
}
