import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { ErrorHandlerService } from '../error/error.service';
import { User } from '../../models/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  userBaseUrl: string = 'http://localhost:8080/api/v1/users';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  findByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${this.userBaseUrl}/profile/${email}`).pipe(catchError(this.errorHandler.handleError));
  }

  update(user: User, imageUrl: string): Observable<User> {
    return this.http.put<User>(`${this.userBaseUrl}`, {userDTO: user, imageUrl: imageUrl}).pipe(catchError(this.errorHandler.handleError));
  }

  changePassword(id: number, password: string): Observable<User> {
    return this.http.patch<User>(`${this.userBaseUrl}/${id}/${password}`, {}).pipe(catchError(this.errorHandler.handleError));
  }

  currentPasswordValidator(id: number, typedCurrentPassword: string) {
    return this.http.get<Boolean>(`${this.userBaseUrl}/${id}/${typedCurrentPassword}`, {}).pipe(catchError(this.errorHandler.handleError));
  }

}
