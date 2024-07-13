import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Rate } from '../../../../models/Rate';
import { ButtonComponent } from '../../../shared/button/button.component';
import { Facility } from '../../../../models/Facility';
import { ModalComponent } from '../../../shared/modal/modal.component';
import { RateService } from '../../../../services/review/rate.service';
import { ReviewService } from '../../../../services/review/review.service';
import { CommentService } from '../../../../services/review/comment.service';
import { Review } from '../../../../models/Review';

@Component({
  selector: 'app-make-an-impression-form',
  standalone: true,
  imports: [ButtonComponent, ModalComponent, FormsModule],
  templateUrl: './make-an-impression-form.component.html',
})
export class MakeAnImpressionFormComponent {
  @Input() facility!: Facility;
  @Input() count!: number;
  @Output() successEvent = new EventEmitter<void>();
  formData!: Rate;
  review: Review | undefined;
  comment: string = "";
  showCommentModal: boolean = false;
  errorMessage: string = "";
  successMessage: string = "";

  constructor(private rateService: RateService, private reviewService: ReviewService, private commentService: CommentService) { }

  ngOnInit() {
    this.formData = {
      equipment: 10,
      staff: 10,
      hygene: 10,
      space: 10
    }
  }

  onRateSubmit(): void {
    if (
      this.isValueOutsideRange(this.formData.equipment) ||
      this.isValueOutsideRange(this.formData.staff) ||
      this.isValueOutsideRange(this.formData.hygene) ||
      this.isValueOutsideRange(this.formData.space)
    ) {
      this.errorMessage = "Input values must be between 1 and 10."
      return;
    }
    this.clearErrorMessage();
    this.showCommentModal = true;
  }

  onSubmit(): void {
    // first create rate, then create review and after review is created if exist create comment and then update review with comment
    this.rateService.create(this.formData).subscribe(rateData => {
      this.reviewService.create(rateData, this.count, this.facility.id!).subscribe(reviewData => {
        if (this.comment != "") {
          this.commentService.create(reviewData, this.comment).subscribe(() => {
            this.comment = "";
          });
        }
        this.showCommentModal = false;
        this.clearFormData();
        this.successMessage = "Review sent successfully!"
        setTimeout(() => { this.clearSuccessMessage();  }, 5000)
        this.successEvent.emit()
      }, error => {
        this.errorMessage = "Error sending review";
        setTimeout(() => { this.clearErrorMessage();  }, 5000)
      })
    })
  }

  isValueOutsideRange(value: number): boolean {
    return value < 1 || value > 10;
  }

  clearErrorMessage(): void {
    this.errorMessage = "";
  }

  clearSuccessMessage(): void {
    this.successMessage = "";
  }

  clearFormData(): void {
    this.formData = {
      equipment: 10,
      staff: 10,
      hygene: 10,
      space: 10
    }
    this.comment = "";
  }
}
