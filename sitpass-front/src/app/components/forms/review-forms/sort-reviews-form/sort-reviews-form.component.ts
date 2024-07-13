import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Review } from '../../../../models/Review';
import { ButtonComponent } from '../../../shared/button/button.component';
import { FormsModule } from '@angular/forms';
import { ReviewService } from '../../../../services/review/review.service';

@Component({
  selector: 'app-sort-reviews-form',
  standalone: true,
  imports: [FormsModule, ButtonComponent],
  templateUrl: './sort-reviews-form.component.html',
})
export class SortReviewsFormComponent {
  @Input() facilityId!: number;
  @Output() sortedReviewsData: EventEmitter<Review[]> = new EventEmitter<Review[]>();
  formData: any = {
    sortRating: "default",
    sortDate: "default"
  }

  constructor(private reviewService: ReviewService) {}

  onSubmit() {
    this.reviewService.findAllByFacilityAndSort(this.facilityId, this.formData.sortRating, this.formData.sortDate)
    .subscribe((sortedReviews) => {
      console.log(sortedReviews)
      this.sortedReviewsData.emit(sortedReviews);
    })
  }
}
