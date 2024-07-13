import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Pagination } from '../../../models/Pagination';
import { Facility } from '../../../models/Facility';

@Component({
  selector: 'app-pagination-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pagination-button.component.html',
})

export class PaginationButtonComponent {
  @Input() pagination!: Pagination;
  @Input() facilitiesData!: Facility[];
  @Output() previousPage = new EventEmitter<void>();
  @Output() nextPage = new EventEmitter<void>();

  constructor() { }
}