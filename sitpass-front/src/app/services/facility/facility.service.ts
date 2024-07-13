import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';
import { ErrorHandlerService } from '../error/error.service';
import { Facility } from '../../models/Facility';
import { Pagination } from '../../models/Pagination';

@Injectable({
  providedIn: 'root'
})
export class FacilityService {
  facilityBaseUrl: string = 'http://localhost:8080/api/v1/facilities';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  findAll(): Observable<Facility[]> {
    return this.http.get<Facility[]>(this.facilityBaseUrl).pipe(catchError(this.errorHandler.handleError));
  }

  findAllByActiveTrueAndCity(allPagination: Pagination): Observable<Facility[]> {
    return this.http.get<Facility[]>(`${this.facilityBaseUrl}/page/${allPagination.currentPage}/size/${allPagination.pageSize}`)
    .pipe(catchError(this.errorHandler.handleError));
  }

  findAllByActiveTrueAndCityAndTotalRatingIsNotNullOrderByTotalRatingDesc(pagination: Pagination) {
    return this.http.get<Facility[]>(`${this.facilityBaseUrl}/page/${pagination.currentPage}/size/${pagination.pageSize}/most_popular`)
    .pipe(catchError(this.errorHandler.handleError));
  }

  findAllByFrequentlyVisited(pagination: Pagination) {
    return this.http.get<Facility[]>(`${this.facilityBaseUrl}/page/${pagination.currentPage}/size/${pagination.pageSize}/frequently_visited`)
    .pipe(catchError(this.errorHandler.handleError));
  }

  findAllByExploreNew(pagination: Pagination) {
    return this.http.get<Facility[]>(`${this.facilityBaseUrl}/page/${pagination.currentPage}/size/${pagination.pageSize}/explore_new`)
    .pipe(catchError(this.errorHandler.handleError));
  }

  findById(id: number): Observable<Facility> {
    return this.http.get<Facility>(`${this.facilityBaseUrl}/${id}`).pipe(catchError(this.errorHandler.handleError));
  }

  filterAll(payload: any, allPagination: Pagination): Observable<Facility[]> {
    return this.http.post<Facility[]>(`${this.facilityBaseUrl}/page/${allPagination.currentPage}/size/${allPagination.pageSize}/filter`, payload)
    .pipe(catchError(this.errorHandler.handleError));
  }

  create(facility: Facility): Observable<Facility> {
    const payload: Facility = facility;
    return this.http.post<Facility>(this.facilityBaseUrl, payload).pipe(catchError(this.errorHandler.handleError));
  }

  update(facility: Facility): Observable<Facility> {
    const payload: Facility = facility
    return this.http.put<Facility>(`${this.facilityBaseUrl}/${facility.id}`, payload).pipe(catchError(this.errorHandler.handleError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.facilityBaseUrl}/${id}`).pipe(catchError(this.errorHandler.handleError));
  }
}
