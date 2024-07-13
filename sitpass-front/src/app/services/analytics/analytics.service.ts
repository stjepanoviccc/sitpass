import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';
import { ErrorHandlerService } from '../error/error.service';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  baseUrl: string = 'http://localhost:8080/api/v1/facilities/';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  findUsersCountByLevel(facilityId: number, level: string, dateFrom?: string, dateTo?: string): Observable<number> {
    let params = new HttpParams()
      .set('level', level)
      .set('dateFrom', dateFrom ? dateFrom : '')
      .set('dateTo', dateTo ? dateTo : '');
    return this.http.get<number>(`${this.baseUrl}${facilityId}/analytics/users`, { params }).pipe(catchError(this.errorHandler.handleError));;
  }

  findReviewsCountByLevel(facilityId: number, level: string, dateFrom?: string, dateTo?: string): Observable<number> {
    let params = new HttpParams()
      .set('level', level)
      .set('dateFrom', dateFrom ? dateFrom : '')
      .set('dateTo', dateTo ? dateTo : '');
    return this.http.get<number>(`${this.baseUrl}${facilityId}/analytics/reviews`, { params }).pipe(catchError(this.errorHandler.handleError));;
  }

  findPeakHours(facilityId: number, period: string, dateFrom?: string, dateTo?: string): Observable<any> {
    let params = new HttpParams()
      .set('period', period)
      .set('dateFrom', dateFrom ? dateFrom : '')
      .set('dateTo', dateTo ? dateTo : '');
    return this.http.get<any>(`${this.baseUrl}${facilityId}/analytics/peak-hours`, { params }).pipe(catchError(this.errorHandler.handleError));;
  }
}
