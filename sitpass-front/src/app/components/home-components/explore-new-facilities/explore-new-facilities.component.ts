import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Facility } from '../../../models/Facility';
import { Pagination } from '../../../models/Pagination';
import { PaginationButtonComponent } from '../../shared/pagination-button/pagination-button.component';
import { FacilityCardComponent } from '../../shared/cards/facility-card/facility-card.component';
import { FacilityService } from '../../../services/facility/facility.service';

@Component({
  selector: 'app-explore-new-facilities',
  standalone: true,
  imports: [CommonModule, PaginationButtonComponent, FacilityCardComponent],
  templateUrl: './explore-new-facilities.component.html',
})
export class ExploreNewFacilitiesComponent {
  exploreNewFacilitiesData: Facility[] = [];
  pagination: Pagination = { currentPage: 0, pageSize: 5 };

  constructor(private facilityService: FacilityService) {}

  ngOnInit(): void {
    this.loadFacilitiesData();
  }

  loadFacilitiesData(): void {
    this.facilityService
      .findAllByExploreNew({
        currentPage: this.pagination.currentPage,
        pageSize: this.pagination.pageSize,
      })
      .subscribe((data: Facility[]) => {
        this.exploreNewFacilitiesData = data;
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
