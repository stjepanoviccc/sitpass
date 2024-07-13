import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FacilityService } from './facility.service';
import { ErrorHandlerService } from '../error/error.service';
import { Facility } from '../../models/Facility';
import { Pagination } from '../../models/Pagination';

describe('FacilityService', () => {
  const facility: Facility = {id: 1, description: "desc", address: "address", city: "city"};
  const facilities: Facility[] = [facility, facility];
  const pagination: Pagination = { currentPage: 1, pageSize: 10 };
  let facilityService: FacilityService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FacilityService, ErrorHandlerService]
    });
    facilityService = TestBed.inject(FacilityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all facilities', () => {
    facilityService.findAll().subscribe(facilities => {
      expect(facilities.length).toBe(2);
      expect(facilities).toEqual(facilities);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}`);
    expect(req.request.method).toBe('GET');
    req.flush(facilities);
  });

  it('should retrieve facilities by active true and city with pagination', () => {

    facilityService.findAllByActiveTrueAndCity(pagination).subscribe(facilities => {
      expect(facilities.length).toBe(2);
      expect(facilities).toEqual(facilities);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}/page/${pagination.currentPage}/size/${pagination.pageSize}`);
    expect(req.request.method).toBe('GET');
    req.flush(facilities);
  });

  it('should retrieve most popular facilities', () => {    

    facilityService.findAllByActiveTrueAndCityAndTotalRatingIsNotNullOrderByTotalRatingDesc(pagination).subscribe(facilities => {
      expect(facilities.length).toBe(2);
      expect(facilities).toEqual(facilities);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}/page/${pagination.currentPage}/size/${pagination.pageSize}/most_popular`);
    expect(req.request.method).toBe('GET');
    req.flush(facilities);
  });

  it('should retrieve frequently visited facilities', () => {

    facilityService.findAllByFrequentlyVisited(pagination).subscribe(facilities => {
      expect(facilities.length).toBe(2);
      expect(facilities).toEqual(facilities);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}/page/${pagination.currentPage}/size/${pagination.pageSize}/frequently_visited`);
    expect(req.request.method).toBe('GET');
    req.flush(facilities);
  });

  it('should retrieve explore new facilities', () => {

    facilityService.findAllByExploreNew(pagination).subscribe(facilities => {
      expect(facilities.length).toBe(2);
      expect(facilities).toEqual(facilities);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}/page/${pagination.currentPage}/size/${pagination.pageSize}/explore_new`);
    expect(req.request.method).toBe('GET');
    req.flush(facilities);
  });

  it('should retrieve a facility by id', () => {

    facilityService.findById(1).subscribe(facility => {
      expect(facility).toEqual(facility);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(facility);
  });

  it('should filter facilities', () => {
    const payload = { city: 'New York' };

    facilityService.filterAll(payload, pagination).subscribe(facilities => {
      expect(facilities.length).toBe(2);
      expect(facilities).toEqual(facilities);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}/page/${pagination.currentPage}/size/${pagination.pageSize}/filter`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(facilities);
  });

  it('should create a new facility', () => {

    facilityService.create(facility).subscribe(facility => {
      expect(facility).toEqual(facility);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(facility);
    req.flush(facility);
  });

  it('should update a facility', () => {

    facilityService.update(facility).subscribe(facility => {
      expect(facility).toEqual(facility);
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(facility);
    req.flush(facility);
  });

  it('should delete a facility', () => {
    facilityService.delete(1).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should handle errors', () => {
    spyOn(TestBed.inject(ErrorHandlerService), 'handleError').and.callThrough();
    const errorHandler = TestBed.inject(ErrorHandlerService);
    const errorMessage = 'deliberate 404 error';

    facilityService.findAll().subscribe(
      () => fail('should have failed with the 404 error'),
      (error) => {
        expect(error).toBeTruthy();
      }
    );

    const req = httpMock.expectOne(`${facilityService.facilityBaseUrl}`);
    req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    expect(errorHandler.handleError).toHaveBeenCalled();
  });
});
