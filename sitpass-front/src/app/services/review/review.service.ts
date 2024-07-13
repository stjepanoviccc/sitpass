import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { Review } from '../../models/Review';
import { HttpClient } from '@angular/common/http';
import { ErrorHandlerService } from '../error/error.service';
import { Rate } from '../../models/Rate';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  reviewBaseUrl: string = 'http://localhost:8080/api/v1/reviews';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  findById(reviewId: number) {
    return this.http.get<Review>(`${this.reviewBaseUrl}/${reviewId}`).pipe(catchError(this.errorHandler.handleError));
  }

  findAllByFacility(facilityId: number) {
    return this.http.get<Review[]>(`${this.reviewBaseUrl}/facilities/${facilityId}`).pipe(catchError(this.errorHandler.handleError));
  }

  findAllByFacilityAndSort(facilityId: number, sortRating: string, sortDate: string) {
    return this.http.get<Review[]>(`${this.reviewBaseUrl}/facilities/${facilityId}/sort`, {
        params: {
            sortRating: sortRating,
            sortDate: sortDate
        }
    }).pipe(catchError(this.errorHandler.handleError));
  }

  findAllByUser(): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.reviewBaseUrl}/users`).pipe(catchError(this.errorHandler.handleError));
  }

  create(rate: Rate, count: number, facilityId: number): Observable<Review> {
    const payload: Review = { rate: rate, exerciseCount: count };
    return this.http.post<Review>(`${this.reviewBaseUrl}/facilities/${facilityId}`, payload).pipe(catchError(this.errorHandler.handleError));
  }

  hide(reviewId: number): Observable<Review> {
    return this.http.patch<Review>(`${this.reviewBaseUrl}/${reviewId}`, {}).pipe(catchError(this.errorHandler.handleError));
  }

  delete(reviewId: number): Observable<void>  {
    return this.http.delete<void>(`${this.reviewBaseUrl}/${reviewId}`).pipe(catchError(this.errorHandler.handleError));
  }
}
