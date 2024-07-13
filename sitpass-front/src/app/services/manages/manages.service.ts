import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';
import { ErrorHandlerService } from '../error/error.service';
import { Manages } from '../../models/Manages';

@Injectable({
  providedIn: 'root'
})
export class ManagesService {
  managesBaseUrl: string = 'http://localhost:8080/api/v1/manages';
  byFacility: string = 'facility';
  byReview: string = 'review';
  byComment: string = 'comment';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  findAll(): Observable<Manages[]> {
    return this.http.get<Manages[]>(this.managesBaseUrl).pipe(catchError(this.errorHandler.handleError));
  }

  findAllByUser(): Observable<Manages[]> {
    return this.http.get<Manages[]>(`${this.managesBaseUrl}/users`).pipe(catchError(this.errorHandler.handleError))
  }

  isManager(facilityId: number): Observable<Boolean> {
    return this.http.get<Boolean>(`${this.managesBaseUrl}/facilities/${facilityId}/${this.byFacility}`)
      .pipe(catchError(this.errorHandler.handleError));
  }

  create(name: string, email: string, startDate: Date, endDate: Date): Observable<Manages> {
    const payload: any = {name, email, startDate, endDate};
    return this.http.post<Manages>(this.managesBaseUrl, payload).pipe(catchError(this.errorHandler.handleError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.managesBaseUrl}/${id}`).pipe(catchError(this.errorHandler.handleError));
  }

}
