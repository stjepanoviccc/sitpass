import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { LoginRequest } from '../../models/LoginRequest';
import { LoginResponse } from '../../models/LoginResponse';
import { JwtUtilsService } from './jwt-utils.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  loginBaseUrl: string = 'http://localhost:8080/api/v1/login';
  accessTokenKey: string = 'accessToken';
  refreshTokenKey: string = 'refreshToken';
  email: string = 'email';

  constructor(private http: HttpClient, private router: Router, private jwtService: JwtUtilsService) { }

  login(email: string, password: string): Observable<LoginResponse> {
    const payload: LoginRequest = { email, password };
    return this.http.post<LoginResponse>(this.loginBaseUrl, payload).pipe(
      tap((response: LoginResponse) => {
        if (response.accessToken && response.refreshToken) {
          localStorage.setItem(this.accessTokenKey, response.accessToken);
          localStorage.setItem(this.refreshTokenKey, response.refreshToken);
          localStorage.setItem(this.email, this.jwtService.getEmail(response.accessToken))
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.accessTokenKey);
    localStorage.removeItem(this.refreshTokenKey);
    this.router.navigate((['/login']))
  }

  refreshToken(): Observable<LoginResponse> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) {
      this.router.navigate((['/']))
    }
  
    return this.http.post<any>(this.loginBaseUrl + '/refresh-token', { refreshToken }).pipe(
      tap((response) => {
        if (response.accessToken) {
          localStorage.setItem(this.accessTokenKey, response.accessToken);
        }
      })
    );
  }

  isLoggedIn(): boolean {
    if(localStorage.getItem(this.accessTokenKey) != '') {
      return true;
    } 
    return false;
  }

  isAdmin(): boolean {
    if (this.isLoggedIn()) {
      let token = localStorage.getItem(this.accessTokenKey);
      if(this.jwtService.getRole(token || "") == "ROLE_ADMIN") {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  isUser(): boolean {
    if (this.isLoggedIn()) {
      let token = localStorage.getItem(this.accessTokenKey);
      if(this.jwtService.getRole(token || "") == "ROLE_USER") {
        return true;
      } else {
        return false;
      }
    } 
    return false;
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.accessTokenKey);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.refreshTokenKey);
  }

  getEmailFromToken(): string {
    return this.jwtService.getEmail(this.accessTokenKey);
  }
}
