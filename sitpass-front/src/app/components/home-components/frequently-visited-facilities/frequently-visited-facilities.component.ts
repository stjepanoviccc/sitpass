import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Facility } from '../../../models/Facility';
import { Pagination } from '../../../models/Pagination';
import { PaginationButtonComponent } from '../../shared/pagination-button/pagination-button.component';
import { FacilityCardComponent } from '../../shared/cards/facility-card/facility-card.component';
import { FacilityService } from '../../../services/facility/facility.service';

@Component({
  selector: 'app-frequently-visited-facilities',
  standalone: true,
  imports: [CommonModule, FacilityCardComponent, PaginationButtonComponent],
  templateUrl: './frequently-visited-facilities.component.html',
})
export class FrequentlyVisitedFacilitiesComponent {
  frequentlyVisitedFacilitiesData: Facility[] = [];
  pagination: Pagination = { currentPage: 0, pageSize: 1 };

  constructor(private facilityService: FacilityService) {}

  ngOnInit(): void {
    this.loadFacilitiesData();
  }

  loadFacilitiesData(): void {
    this.facilityService
      .findAllByFrequentlyVisited({
        currentPage: this.pagination.currentPage,
        pageSize: this.pagination.pageSize,
      })
      .subscribe((data: Facility[]) => {
        this.frequentlyVisitedFacilitiesData = data;
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
