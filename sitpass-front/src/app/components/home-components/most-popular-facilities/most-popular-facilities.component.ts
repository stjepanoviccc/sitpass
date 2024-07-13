import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FacilityCardComponent } from '../../shared/cards/facility-card/facility-card.component';
import { Facility } from '../../../models/Facility';
import { FacilityService } from '../../../services/facility/facility.service';
import { Pagination } from '../../../models/Pagination';
import { PaginationButtonComponent } from '../../shared/pagination-button/pagination-button.component';

@Component({
  selector: 'app-most-popular-facilities',
  standalone: true,
  imports: [FacilityCardComponent, PaginationButtonComponent, CommonModule],
  templateUrl: './most-popular-facilities.component.html',
})
export class MostPopularFacilitiesComponent {
  mostPopularFacilitiesData: Facility[] = [];
  pagination: Pagination = {currentPage: 0, pageSize: 1};

  constructor(private facilityService: FacilityService) {}

  ngOnInit(): void {
    this.loadFacilitiesData();
  }

  loadFacilitiesData(): void {
    this.facilityService
      .findAllByActiveTrueAndCityAndTotalRatingIsNotNullOrderByTotalRatingDesc({
        currentPage: this.pagination.currentPage,
        pageSize: this.pagination.pageSize,
      })
      .subscribe((data: Facility[]) => {
        this.mostPopularFacilitiesData = data;
      });
  }

  loadPreviousPage(): void {
    if (this.pagination.currentPage > 0) {
      this.pagination.currentPage--;
      this.loadFacilitiesData();
    }
  }

  loadNextPage(): void {
    this.pagination.currentPage++;
    this.loadFacilitiesData();
  }
}