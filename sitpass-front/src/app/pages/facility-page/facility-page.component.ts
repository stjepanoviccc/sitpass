import { Component } from '@angular/core';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { CommonModule } from '@angular/common';
import { register} from 'swiper/element/bundle';
import { Exercise } from '../../models/Exercise';
import { Review } from '../../models/Review';
import { Facility } from '../../models/Facility';
import { WrapComponent } from '../../components/shared/wrap/wrap.component';
import { TableComponent } from '../../components/shared/table/table.component';
import { ButtonComponent } from '../../components/shared/button/button.component';
import { BookAnExerciseFormComponent } from '../../components/forms/exercise-forms/book-an-exercise-form/book-an-exercise-form.component';
import { MakeAnImpressionFormComponent } from '../../components/forms/review-forms/make-an-impression-form/make-an-impression-form.component';
import { SortReviewsFormComponent } from '../../components/forms/review-forms/sort-reviews-form/sort-reviews-form.component';
import { ExerciseService } from '../../services/exercise/exercise.service';
import { ReviewService } from '../../services/review/review.service';
import { FileService } from '../../services/file/file.service';
import { FacilityService } from '../../services/facility/facility.service';
import { ManagesService } from '../../services/manages/manages.service';
import { LoginService } from '../../services/auth/login.service';

register();

@Component({
  selector: 'app-facility-page',
  standalone: true,
  imports: [
    WrapComponent,
    ButtonComponent,
    BookAnExerciseFormComponent,
    MakeAnImpressionFormComponent,
    SortReviewsFormComponent,
    TableComponent,
    CommonModule,
  ],
  templateUrl: './facility-page.component.html',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FacilityPageComponent {
  facilityId!: number;
  facility!: Facility;
  reviews: Review[] = [];
  filteredReviewsData!: any[];
  imageUrls: string[] = [];
  isUserManager: Boolean = false;
  count: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private facilityService: FacilityService,
    private exerciseService: ExerciseService,
    private reviewService: ReviewService,
    public fileService: FileService,
    public managesService: ManagesService,
    public loginService: LoginService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.facilityId = +params['id'];
      this.isManager();
      this.getFacility();
      this.getExerciseCount();
    });
  }

  isManager(): void {
    this.managesService.isManager(this.facilityId).subscribe((state) => this.isUserManager = state )
  }

  getFacility(): void {
    this.facilityService.findById(this.facilityId).subscribe(
      (facility: Facility) => {
        if (!facility) {
          this.router.navigate(['/error']);
        } else {
          this.facility = facility;
          this.getReviews();
          this.fetchImageUrls();
        }
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  getExerciseCount(): void {
    this.exerciseService.findAllByFacilityAndUser(this.facilityId).subscribe((exerciseList: Exercise[]) => {
        this.count = exerciseList!.length;
      });
  }
  
  fetchImageUrls(): void {
    if (this.facility.images) {
      for (let image of this.facility.images) {
        this.fileService.getFileContent(image.path).subscribe(
          (imageUrl: string) => {
            this.imageUrls.push(imageUrl);
          },
          (error: any) => {
            console.error('Error fetching image:', error);
          }
        );
      }
    }
  }

  getReviews(): void {
    this.reviewService.findAllByFacility(this.facilityId).subscribe((reviewList: Review[]) => {
      this.reviews = reviewList;
      this.filterReviewsData();
    });
  }

  handleSortedReviewsData(sortedReviews: Review[]): void {
    this.reviews = sortedReviews;
    this.filterReviewsData();
  }

  filterReviewsData(): void {
    this.filteredReviewsData = this.reviews.map(review => ({
      id: review.id,
      createdAt: review.createdAt,
      facilityName: review.facility?.name,
      equipment: review.rate!.equipment,
      hygene: review.rate!.hygene,
      staff: review.rate!.staff,
      space: review.rate!.space,
      hidden: review.hidden || "false"
    }));
  }

  viewReviewDetails(rowDataFiltered: any): void {
    this.router.navigate([(`/reviews/${rowDataFiltered.id}`)]);
  }

  hideReview(rowDataFiltered: any): void {
    this.reviewService.hide(rowDataFiltered.id).subscribe(() => {
      const reviewIndex = this.reviews.findIndex(review => review.id === rowDataFiltered.id);
      if (reviewIndex !== -1) {
        this.reviews[reviewIndex].hidden = true;
      }
  
      const filteredReviewIndex = this.filteredReviewsData.findIndex(review => review.id === rowDataFiltered.id);
      if (filteredReviewIndex !== -1) {
        this.filteredReviewsData[filteredReviewIndex].hidden = true;
      }
    });
  }

  deleteReview(rowDataFiltered: any): void {
    this.reviewService.delete(rowDataFiltered.id).subscribe(() => {
      this.reviews = this.reviews.filter(data => data.id != rowDataFiltered.id);
      this.filteredReviewsData = this.filteredReviewsData.filter(data => data.id != rowDataFiltered.id);
      this.handleSuccessEvent();
    })
  }

  handleSuccessEvent(): void {
    this.getFacility();
    this.getExerciseCount();
  }
  
  goBack(): void {
    this.location.back();
  }
}
