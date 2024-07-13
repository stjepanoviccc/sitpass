import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RegisterService } from './register.service';
import { ErrorHandlerService } from '../error/error.service';
import { AccountRequest } from '../../models/AccountRequest';
import { RequestStatus } from '../../models/enums/RequestStatus';

describe('RegisterService', () => {
  const accRequest: AccountRequest = {email: "email@gmail.com", password: "password123", address: "Address" };
  const accRequests: AccountRequest[] = [accRequest, accRequest];
  let registerService: RegisterService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        RegisterService,
        { provide: ErrorHandlerService, useValue: { handleError: jasmine.createSpy('handleError').and.callFake((error) => { throw error; }) } }
      ]
    });

    registerService = TestBed.inject(RegisterService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all account requests', () => {
    registerService.findAll().subscribe(ar => {
      expect(ar).toEqual(accRequests);
    });

    const req = httpMock.expectOne(registerService.registerBaseUrl);
    expect(req.request.method).toBe('GET');
    req.flush(accRequests);
  });

  it('should send account request', () => {

    registerService.sendAccountRequests(accRequest.email, accRequest.password, accRequest.address!).subscribe(accountRequest => {
      expect(accountRequest).toEqual(accRequest);
    });

    const req = httpMock.expectOne(registerService.registerBaseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(accRequest);
    req.flush(accRequest);
  });

  it('should handle account requests', () => {
    const dummyAccountRequest: AccountRequest = { email: 'test@example.com', password: 'password', address: 'address', requestStatus: RequestStatus.PENDING };
    const updatedAccountRequest: AccountRequest = { ...dummyAccountRequest, requestStatus: RequestStatus.ACCEPTED };

    registerService.handleAccountRequests(dummyAccountRequest, RequestStatus.ACCEPTED).subscribe(accountRequest => {
      expect(accountRequest).toEqual(updatedAccountRequest);
    });

    const req = httpMock.expectOne(registerService.registerBaseUrl);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedAccountRequest);
    req.flush(updatedAccountRequest);
  });

  it('should handle errors for findAll', () => {
    registerService.findAll().subscribe(
      () => fail('should have failed with the network error'),
      (error) => {
        expect(error).toBeTruthy();
      }
    );

    const req = httpMock.expectOne(registerService.registerBaseUrl);
    req.error(new ErrorEvent('Network error'));
  });

  it('should handle errors for sendAccountRequests', () => {

    registerService.sendAccountRequests(accRequest.email, accRequest.password, accRequest.address!).subscribe(
      () => fail('should have failed with the network error'),
      (error) => {
        expect(error).toBeTruthy();
      }
    );

    const req = httpMock.expectOne(registerService.registerBaseUrl);
    req.error(new ErrorEvent('Network error'));
  });

});