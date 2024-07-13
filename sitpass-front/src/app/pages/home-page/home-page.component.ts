import { Component, Inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ScrollService } from '../../services/router/scroll.service';
import { FacilityService } from '../../services/facility/facility.service';
import { Facility } from '../../models/Facility';
import { Pagination } from '../../models/Pagination';
import { WrapComponent } from '../../components/shared/wrap/wrap.component';
import { FilterFacilityFormComponent } from '../../components/forms/facility-forms/filter-facility-form/filter-facility-form.component';
import { FacilityCardComponent } from '../../components/shared/cards/facility-card/facility-card.component';
import { ButtonComponent } from '../../components/shared/button/button.component';
import { PaginationButtonComponent } from '../../components/shared/pagination-button/pagination-button.component';
import { MostPopularFacilitiesComponent } from '../../components/home-components/most-popular-facilities/most-popular-facilities.component';
import { FrequentlyVisitedFacilitiesComponent } from '../../components/home-components/frequently-visited-facilities/frequently-visited-facilities.component';
import { ExploreNewFacilitiesComponent } from '../../components/home-components/explore-new-facilities/explore-new-facilities.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [MostPopularFacilitiesComponent, FrequentlyVisitedFacilitiesComponent, ExploreNewFacilitiesComponent, 
    ButtonComponent, PaginationButtonComponent, WrapComponent, FilterFacilityFormComponent, FacilityCardComponent, RouterLink, CommonModule],
  templateUrl: './home-page.component.html',
})
export class HomePageComponent {
  facilitiesData: Facility[] = [];
  allPagination: Pagination = {currentPage: 0, pageSize: 5};

  filteredFormData: any;
  isFiltered: boolean = false;

  constructor(private facilityService: FacilityService, public scrollService: ScrollService, @Inject(Router) private router: Router) {}

  ngOnInit(): void {
    if(localStorage.getItem("accessToken") == null) {
      this.router.navigate([(`/login`)]);
    }
    this.loadFacilitiesData();
  }

  loadFacilitiesData(): void {
    this.facilityService.findAllByActiveTrueAndCity(this.allPagination).subscribe((data: Facility[]) => { this.facilitiesData = data; });
  }

   allPagination_loadPreviousPage(): void {
    if (this.allPagination.currentPage > 0) {
        this.allPagination.currentPage--;
        if(this.isFiltered) {
          this.facilityService.filterAll(this.filteredFormData, this.allPagination).subscribe( (newData: Facility[]) => {
            this.facilitiesData = newData;
          }, error => console.log(error))
        } else {
          this.loadFacilitiesData();
        }
    }
  }

  allPagination_loadNextPage(): void {
    this.allPagination.currentPage++;
    if(this.isFiltered) {
      this.facilityService.filterAll(this.filteredFormData, this.allPagination).subscribe( (newData: Facility[]) => {
        this.facilitiesData = newData;
      }, error => console.log(error))
    } else {
      this.loadFacilitiesData();
    }
  }

  handleFilteredFacilitiesData(filteredFacilities: Facility[]): void {
    this.allPagination = {currentPage: 0, pageSize: 5}
    this.facilitiesData = filteredFacilities;
  }

  handleFilteredFormData(filteredFormData: any): void {
    this.filteredFormData = filteredFormData;
  }
  
  handleIsFiltered(isFiltered: boolean): void {
    this.isFiltered = isFiltered;
  }
  
}
