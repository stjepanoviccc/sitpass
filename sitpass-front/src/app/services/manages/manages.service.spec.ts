import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ManagesService } from './manages.service';
import { ErrorHandlerService } from '../error/error.service';
import { Manages } from '../../models/Manages';

describe('ManagesService', () => {
  const manages: Manages = {id:1 , startDate: new Date(), endDate: new Date()};
  const managesList: Manages[] = [manages, manages];
  let service: ManagesService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ManagesService, ErrorHandlerService]
    });
    service = TestBed.inject(ManagesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all manages', () => {

    service.findAll().subscribe(m => {
      expect(m.length).toBe(2);
      expect(m).toEqual(managesList);
    });

    const req = httpMock.expectOne(`${service.managesBaseUrl}`);
    expect(req.request.method).toBe('GET');
    req.flush(managesList);
  });

  it('should retrieve all manages by user', () => {

    service.findAllByUser().subscribe(m => {
      expect(m.length).toBe(2);
      expect(m).toEqual(managesList);
    });

    const req = httpMock.expectOne(`${service.managesBaseUrl}/users`);
    expect(req.request.method).toBe('GET');
    req.flush(managesList);
  });

  it('should check if user is manager of a facility', () => {
    const facilityId = 1;
    const isManager = true;

    service.isManager(facilityId).subscribe(response => {
      expect(response).toBe(isManager);
    });

    const req = httpMock.expectOne(`${service.managesBaseUrl}/facilities/${facilityId}/${service.byFacility}`);
    expect(req.request.method).toBe('GET');
    req.flush(isManager);
  });

  it('should create a new manage', () => {

    service.create('New Manage', 'new@example.com', new Date(), new Date()).subscribe(m => {
      expect(m).toEqual(manages);
    });

    const req = httpMock.expectOne(`${service.managesBaseUrl}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      name: 'New Manage',
      email: 'new@example.com',
      startDate: jasmine.any(Date),
      endDate: jasmine.any(Date)
    });
    req.flush(manages);
  });

  it('should delete a manage', () => {
    const manageId = 1;

    service.delete(manageId).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${service.managesBaseUrl}/${manageId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should handle errors', () => {
    spyOn(TestBed.inject(ErrorHandlerService), 'handleError').and.callThrough();
    const errorHandler = TestBed.inject(ErrorHandlerService);
    const errorMessage = 'deliberate 404 error';

    service.findAll().subscribe(
      () => fail('should have failed with the 404 error'),
      (error) => {
        expect(error).toBeTruthy();
      }
    );

    const req = httpMock.expectOne(`${service.managesBaseUrl}`);
    req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    expect(errorHandler.handleError).toHaveBeenCalled();
  });
});
