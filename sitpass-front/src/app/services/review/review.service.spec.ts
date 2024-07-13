import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReviewService } from './review.service';
import { ErrorHandlerService } from '../error/error.service';
import { Review } from '../../models/Review';
import { Rate } from '../../models/Rate';

describe('ReviewService', () => {
  const rate: Rate = { id: 1, equipment: 8, staff: 9, space: 4, hygene: 5 };
  const review: Review = { id: 1, rate: rate };
  const reviews: Review[] = [review, review ];

  let reviewService: ReviewService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReviewService, ErrorHandlerService]
    });
    reviewService = TestBed.inject(ReviewService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve a review by ID', () => {
    reviewService.findById(1).subscribe(r => {
      expect(review).toEqual(r);
    });

    const req = httpMock.expectOne(`${reviewService.reviewBaseUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(review);
  });

  it('should retrieve all reviews by facility ID', () => {
    reviewService.findAllByFacility(1).subscribe(r => {
      expect(r.length).toBe(2);
      expect(r).toEqual(reviews);
    });

    const req = httpMock.expectOne(`${reviewService.reviewBaseUrl}/facilities/1`);
    expect(req.request.method).toBe('GET');
    req.flush(reviews);
  });

  it('should retrieve all reviews by facility ID and sort', () => {

    reviewService.findAllByFacilityAndSort(1, 'asc', 'desc').subscribe(r => {
      expect(r.length).toBe(2);
      expect(r).toEqual(reviews);
    });

    const req = httpMock.expectOne(`${reviewService.reviewBaseUrl}/facilities/1/sort?sortRating=asc&sortDate=desc`);
    expect(req.request.method).toBe('GET');
    req.flush(reviews);
  });

  it('should retrieve all reviews by user', () => {

    reviewService.findAllByUser().subscribe(r => {
      expect(r.length).toBe(2);
      expect(r).toEqual(reviews);
    });

    const req = httpMock.expectOne(`${reviewService.reviewBaseUrl}/users`);
    expect(req.request.method).toBe('GET');
    req.flush(reviews);
  });

  it('should create a new review', () => {
   
    reviewService.create(rate, 3, 1).subscribe(r => {
      expect(r).toEqual(review);
    });

    const req = httpMock.expectOne(`${reviewService.reviewBaseUrl}/facilities/1`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ rate: rate, exerciseCount: 3 });
    req.flush(review);
  });

  it('should hide a review by ID', () => {

    reviewService.hide(1).subscribe(r => {
      expect(r).toEqual(review);
    });

    const req = httpMock.expectOne(`${reviewService.reviewBaseUrl}/1`);
    expect(req.request.method).toBe('PATCH');
    req.flush(review);
  });

  it('should delete a review by ID', () => {
    reviewService.delete(1).subscribe(res => {
      expect(res).toBeNull();
    });

    const req = httpMock.expectOne(`${reviewService.reviewBaseUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should handle errors', () => {
    spyOn(TestBed.inject(ErrorHandlerService), 'handleError').and.callThrough();
    const errorHandler = TestBed.inject(ErrorHandlerService);
    const errorMessage = 'deliberate 404 error';

    reviewService.findById(999).subscribe(
      () => fail('should have failed with the 404 error'),
      (error) => {
        expect(error).toBeTruthy();
      }
    );

    const req = httpMock.expectOne(`${reviewService.reviewBaseUrl}/999`);
    req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    expect(errorHandler.handleError).toHaveBeenCalled();
  });
});
