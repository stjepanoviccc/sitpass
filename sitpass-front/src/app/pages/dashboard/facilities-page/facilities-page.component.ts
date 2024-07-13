import { Component, ViewChild } from '@angular/core';
import { WrapComponent } from '../../../components/shared/wrap/wrap.component';
import { TableComponent } from '../../../components/shared/table/table.component';
import { ButtonComponent } from '../../../components/shared/button/button.component';
import { CreateFacilityFormComponent } from '../../../components/forms/facility-forms/create-facility-form/create-facility-form.component';
import { FormsModule } from '@angular/forms';
import { Facility } from '../../../models/Facility';
import { Router } from '@angular/router';
import { FacilityService } from '../../../services/facility/facility.service';
import { LoginService } from '../../../services/auth/login.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-facilities-page',
  standalone: true,
  imports: [WrapComponent, TableComponent, ButtonComponent, CreateFacilityFormComponent, FormsModule, CommonModule],
  templateUrl: './facilities-page.component.html',
})
export class FacilitiesPageComponent {
  @ViewChild(CreateFacilityFormComponent) createFacilityForm!: CreateFacilityFormComponent;
  facilitiesData: Facility[] = [];
  filteredFacilitiesData: Facility[] = [];
  selectedRowData: any = null;

  constructor(private router: Router, private facilityService: FacilityService, public loginService: LoginService) { }

  ngOnInit(): void {
    this.loadFacilitiesData();
  }

  editFacility(rowDataFiltered: any) {
    this.router.navigate([`/dashboard/facilities/${rowDataFiltered.id}/edit`]);
  }

  deleteFacility(rowDataFiltered: any): void {
    this.facilityService.delete(rowDataFiltered.id).subscribe(() => {
      this.facilitiesData = this.facilitiesData.filter(data => data.id != rowDataFiltered.id);
      this.filteredFacilitiesData = this.filteredFacilitiesData.filter(data => data.id != rowDataFiltered.id);
    })
  }

  loadFacilitiesData(): void {
    this.facilityService.findAll().subscribe((data: Facility[]) => {
      this.facilitiesData = data;
      this.filterFacilitiesData();
    }
    );
  }

  addFacility(newFacility: Facility): void {
    this.facilitiesData.push(newFacility);
    this.filterFacilitiesData();
  }

  onAddFacility(): void {
    this.createFacilityForm.onSubmit();
  }

  filterFacilitiesData(): void {
    this.filteredFacilitiesData = this.facilitiesData.map(facility => ({
      id: facility.id,
      name: facility.name,
      totalRating: facility.totalRating,
      city: facility.city,
      description: 'More info on edit',
      createdAt: facility.createdAt,
      address: facility.address,
      active: facility.active
    }));
  }

}
