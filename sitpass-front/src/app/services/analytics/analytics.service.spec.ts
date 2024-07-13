import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AnalyticsService } from './analytics.service';
import { ErrorHandlerService } from '../error/error.service';

describe('AnalyticsService', () => {
  let analyticsService: AnalyticsService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AnalyticsService, ErrorHandlerService]
    });

    analyticsService = TestBed.inject(AnalyticsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(analyticsService).toBeTruthy();
  });

  it('should retrieve users count by level', () => {
    const facilityId = 1;
    const level = 'high';
    const expectedUsersCount = 10;

    analyticsService.findUsersCountByLevel(facilityId, level).subscribe(usersCount => {
      expect(usersCount).toEqual(expectedUsersCount); 
    });
    const req = httpMock.expectOne(
      `${analyticsService.baseUrl}${facilityId}/analytics/users?level=${level}&dateFrom=&dateTo=`
    );
    expect(req.request.method).toEqual('GET');

    req.flush(expectedUsersCount);
  });

  it('should retrieve reviews count by level', () => {
    const facilityId = 1;
    const level = 'high';
    const expectedReviewsCount = 10;

    analyticsService.findReviewsCountByLevel(facilityId, level).subscribe(reviewsCount => {
      expect(reviewsCount).toEqual(expectedReviewsCount); 
    });

    const req = httpMock.expectOne(
      `${analyticsService.baseUrl}${facilityId}/analytics/reviews?level=${level}&dateFrom=&dateTo=`
    );
    expect(req.request.method).toEqual('GET');
    req.flush(expectedReviewsCount);
  });

});