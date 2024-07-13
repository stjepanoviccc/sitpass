import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { LoginService } from './login.service';
import { JwtUtilsService } from './jwt-utils.service';
import { LoginRequest } from '../../models/LoginRequest';
import { LoginResponse } from '../../models/LoginResponse';

describe('LoginService', () => {
  let loginService: LoginService;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;
  let jwtUtilsSpy: jasmine.SpyObj<JwtUtilsService>;

  beforeEach(() => {
    const router = jasmine.createSpyObj('Router', ['navigate']);
    const jwtUtils = jasmine.createSpyObj('JwtUtilsService', ['getEmail']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        LoginService,
        { provide: Router, useValue: router },
        { provide: JwtUtilsService, useValue: jwtUtils }
      ]
    });

    loginService = TestBed.inject(LoginService);
    httpMock = TestBed.inject(HttpTestingController);
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    jwtUtilsSpy = TestBed.inject(JwtUtilsService) as jasmine.SpyObj<JwtUtilsService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should login and store tokens in localStorage', () => {
    const dummyResponse: LoginResponse = {
      accessToken: 'dummyAccessToken',
      refreshToken: 'dummyRefreshToken'
    };
    const email = 'test@example.com';
    const password = 'password';
    jwtUtilsSpy.getEmail.and.returnValue(email);

    loginService.login(email, password).subscribe(response => {
      expect(response).toEqual(dummyResponse);
      expect(localStorage.getItem(loginService.accessTokenKey)).toBe(dummyResponse.accessToken);
      expect(localStorage.getItem(loginService.refreshTokenKey)).toBe(dummyResponse.refreshToken);
      expect(localStorage.getItem(loginService.email)).toBe(email);
      expect(jwtUtilsSpy.getEmail).toHaveBeenCalledWith(dummyResponse.accessToken);
    });

    const req = httpMock.expectOne(loginService.loginBaseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email, password } as LoginRequest);
    req.flush(dummyResponse);
  });

  it('should not store tokens if login response does not contain them', () => {
    const dummyResponse: LoginResponse = { accessToken: '', refreshToken: '' };
    const email = 'test@example.com';
    const password = 'password';

    loginService.login(email, password).subscribe(response => {
      expect(response).toEqual(dummyResponse);
      expect(localStorage.getItem(loginService.accessTokenKey)).toBeNull();
      expect(localStorage.getItem(loginService.refreshTokenKey)).toBeNull();
      expect(localStorage.getItem(loginService.email)).toBeNull();
      expect(jwtUtilsSpy.getEmail).not.toHaveBeenCalled();
    });

    const req = httpMock.expectOne(loginService.loginBaseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email, password } as LoginRequest);
    req.flush(dummyResponse);
  });

  it('should handle error response correctly', () => {
    const email = 'test@example.com';
    const password = 'password';
    const errorMessage = 'Invalid login credentials';

    loginService.login(email, password).subscribe(
      () => fail('should have failed with the 401 error'),
      error => {
        expect(error).toBeTruthy();
      }
    );

    const req = httpMock.expectOne(loginService.loginBaseUrl);
    expect(req.request.method).toBe('POST');
    req.flush({ message: errorMessage }, { status: 401, statusText: 'Unauthorized' });
  });
});
