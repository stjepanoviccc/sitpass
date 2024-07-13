import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { ErrorHandlerService } from '../error/error.service';
import { User } from '../../models/User';

describe('UserService', () => {
  const user: User = { id: 1, email: 'test@example.com', password: 'password' };
  let userService: UserService;
  let httpMock: HttpTestingController;
  let errorHandler: jasmine.SpyObj<ErrorHandlerService>;

  beforeEach(() => {
    const errorHandlerSpy = jasmine.createSpyObj('ErrorHandlerService', ['handleError']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        UserService,
        { provide: ErrorHandlerService, useValue: errorHandlerSpy }
      ]
    });

    userService = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
    errorHandler = TestBed.inject(ErrorHandlerService) as jasmine.SpyObj<ErrorHandlerService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });

  it('should fetch user by email', () => {
    userService.findByEmail('test@example.com').subscribe(user => {
      expect(user).toEqual(user);
    });

    const req = httpMock.expectOne(`${userService.userBaseUrl}/profile/test@example.com`);
    expect(req.request.method).toBe('GET');
    req.flush(user);
  });

  it('should update user', () => {
    const imageUrl = 'http://example.com/image.jpg';

    userService.update(user, imageUrl).subscribe(user => {
      expect(user).toEqual(user);
    });

    const req = httpMock.expectOne(userService.userBaseUrl);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual({ userDTO: user, imageUrl: imageUrl });
    req.flush(user);
  });

  it('should change password', () => {
    const newPassword = 'newpassword';

    userService.changePassword(user.id!, newPassword).subscribe(user => {
      expect(user).toEqual(user);
    });

    const req = httpMock.expectOne(`${userService.userBaseUrl}/${user.id}/${newPassword}`);
    expect(req.request.method).toBe('PATCH');
    req.flush(user);
  });

  it('should validate current password', () => {
    const typedCurrentPassword = 'currentpassword';
    const isValid = true;

    userService.currentPasswordValidator(user.id!, typedCurrentPassword).subscribe(valid => {
      expect(valid).toBe(isValid);
    });

    const req = httpMock.expectOne(`${userService.userBaseUrl}/${user.id!}/${typedCurrentPassword}`);
    expect(req.request.method).toBe('GET');
    req.flush(isValid);
  });

  it('should handle errors', () => {
    const errorResponse = { status: 404, statusText: 'Not Found' };

    userService.findByEmail('nonexistent@example.com').subscribe(
      () => fail('expected an error, not user'),
      error => {
        expect(errorHandler.handleError).toHaveBeenCalled();
      }
    );

    const req = httpMock.expectOne(`${userService.userBaseUrl}/profile/nonexistent@example.com`);
    req.flush(null, errorResponse);
  });
});
