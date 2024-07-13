import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ErrorHandlerService } from '../error/error.service';
import { Observable, catchError } from 'rxjs';
import { Rate } from '../../models/Rate';

@Injectable({
  providedIn: 'root'
})
// this service is for rate because from rate(make an impression) im creating review
export class RateService {
  rateBaseUrl: string = 'http://localhost:8080/api/v1/facilities/reviews/rates';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  create(rate: Rate): Observable<Rate> {
    const payload: Rate = rate;
    return this.http.post<Rate>(this.rateBaseUrl, payload).pipe(catchError(this.errorHandler.handleError));
  }
}
