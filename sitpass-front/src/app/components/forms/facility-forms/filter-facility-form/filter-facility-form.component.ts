import { Component, EventEmitter, Output } from '@angular/core';
import { FacilityService } from '../../../../services/facility/facility.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from '../../../shared/button/button.component';
import { DayOfWeek } from '../../../../models/enums/DayOfWeek';
import { Facility } from '../../../../models/Facility';
import { Pagination } from '../../../../models/Pagination';

@Component({
  selector: 'app-filter-facility-form',
  standalone: true,
  imports: [ButtonComponent, FormsModule, CommonModule],
  templateUrl: './filter-facility-form.component.html',
})
export class FilterFacilityFormComponent {
  formData!: any;
  errorMessage: string = "";
  daysOfWeek = Object.values(DayOfWeek);
  resetPagination: Pagination = {currentPage:0, pageSize:5};
  @Output() isFiltered: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() filteredFormData: EventEmitter<any> = new EventEmitter<any>();
  @Output() filteredFacilitiesData: EventEmitter<Facility[]> = new EventEmitter<Facility[]>();

  constructor(private facilityService: FacilityService) {}

  ngOnInit() {
    this.formData = {
      cities: "",
      disciplines: "",
      minTotalRating: null,
      maxTotalRating: null,
      workDay: "",
      from: "",
      until: ""
    }  
  }

  onSubmit(): void {
    if(this.formData.maxTotalRating < this.formData.minTotalRating) {
      this.errorMessage = " Max Total Rating must be bigger or equal to Min Total Rating.";
      setTimeout(() => {this.clearErrorMessage()}, 5000);
      return;
    }
    if(this.formData.maxTotalRating < 0 || this.formData.maxTotalRating > 10 || this.formData.minTotalRating < 0 || this.formData.minTotalRating > 10) {
      this.errorMessage = "Max/Min Total Rating can't be lower than 0 or bigger than 10";
      setTimeout(() => {this.clearErrorMessage()}, 5000);
    }
    this.facilityService.filterAll(this.formData, this.resetPagination).subscribe(filteredFacilities => { 
      this.filteredFacilitiesData.emit(filteredFacilities);
      this.filteredFormData.emit(this.formData);
      this.isFiltered.emit(true);
    }, error => this.errorMessage = error)
    this.clearErrorMessage();
  }

  resetFilter(): void {
    this.formData = {
      cities: "",
      disciplines: "",
      minTotalRating: null,
      maxTotalRating: null,
      workDay: "",
      from: "",
      until: ""
    };

    this.facilityService.findAllByActiveTrueAndCity(this.resetPagination).subscribe(all => {
      this.filteredFacilitiesData.emit(all);
      this.filteredFormData.emit(this.formData);
      this.isFiltered.emit(false);
    }, error => this.errorMessage = error)
    
    
    this.clearErrorMessage();
  }

  clearErrorMessage(): void {
    this.errorMessage = '';
  }
}
