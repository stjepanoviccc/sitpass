import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { WrapComponent } from '../../components/shared/wrap/wrap.component';
import { TableComponent } from '../../components/shared/table/table.component';
import { ModalComponent } from '../../components/shared/modal/modal.component';
import { ButtonComponent } from '../../components/shared/button/button.component';
import { EditUserFormComponent } from '../../components/forms/user-forms/edit-user-form/edit-user-form.component';
import { EditPasswordFormComponent } from '../../components/forms/user-forms/edit-password-form/edit-password-form.component';
import { ReviewService } from '../../services/review/review.service';
import { ExerciseService } from '../../services/exercise/exercise.service';
import { ManagesService } from '../../services/manages/manages.service';
import { Review } from '../../models/Review';
import { Exercise } from '../../models/Exercise';
import { Manages } from '../../models/Manages';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [
    WrapComponent,
    TableComponent,
    ModalComponent,
    ButtonComponent,
    EditUserFormComponent,
    EditPasswordFormComponent,
    CommonModule
  ],
  templateUrl: './profile-page.component.html',
  
})
export class ProfilePageComponent {
  emailFromToken: string | null = localStorage.getItem('email');
  showChangePassword: boolean = false;
  reviews: Review[] = [];
  exercises: Exercise[] = [];
  manages: Manages[] = [];
  filteredReviews: any[] = [];
  filteredExercises: any[] = [];
  filteredManages: any[] = [];
  reviewsErrorMsg: string = '';
  managesErrorMsg: string = '';
  exercisesErrorMsg: string = '';
  noDataMsg: string = 'No data available.';

  constructor(
    private reviewService: ReviewService,
    private exerciseService: ExerciseService,
    private manageService: ManagesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getReviews();
    this.getExercises();
    this.getManagerObjects();
  }

  getReviews(): void {
    this.reviewService.findAllByUser().subscribe(
      (reviewsByUser: Review[]) => {
        this.reviews = reviewsByUser || [];
        this.filterReviewsData();
      },
      (error) => {
        this.reviewsErrorMsg = error;
      }
    );
  }

  viewReviewDetails(rowData: any): void {
    this.router.navigate([(`/reviews/${rowData.id}`)]);
  }

  viewFacilityEdit(rowData: any): void {
    this.router.navigate([(`/dashboard/facilities/${rowData.facilityId}/edit`)]);
  }

  viewFacilityDetails(rowData: any): void {
    this.router.navigate([(`/facilities/${rowData.facilityId}`)]);
  }

  viewFacilityAnalytics(rowData: any): void {
    this.router.navigate([(`/dashboard/facilities/${rowData.facilityId}/analytics`)]);
  }

  getExercises(): void {
    this.exerciseService.findAllByUser().subscribe(
      (exercisesByUser: Exercise[]) => {
        this.exercises = exercisesByUser || [];
        this.filterExercisesData();
      },
      (error) => {
        this.exercisesErrorMsg = error;
      }
    )
  }

  getManagerObjects(): void {
    this.manageService.findAllByUser().subscribe(
      (managesByUser: Manages[]) => {
        this.manages = managesByUser || [];
        this.filterManagesData();
      },
      (error) => {
        this.managesErrorMsg = error;
      }
    )
  }

  filterReviewsData(): void {
    this.filteredReviews = this.reviews.map(review => ({
      id: review.id,
      createdAt: review.createdAt,
      facilityName: review.facility?.name,
      equipment: review.rate!.equipment,
      hygene: review.rate!.hygene,
      staff: review.rate!.staff,
      space: review.rate!.space,
    }));
  }

  filterExercisesData(): void {
    this.filteredExercises = this.exercises.map(exercise => ({
      id: exercise.id,
      from: exercise.from,
      until: exercise.until,
      facilityName: exercise.facility?.name
    }));
  }

  filterManagesData(): void {
    this.filteredManages = this.manages.map(manage => ({
      id: manage.id,
      startDate: manage.startDate,
      endDate: manage.endDate,
      facilityName: manage.facility?.name,
      facilityId: manage.facility?.id,
      city: manage.facility?.city
    }))
  }

}
