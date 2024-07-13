import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError } from 'rxjs';
import { ErrorHandlerService } from '../error/error.service';
import { AccountRequest } from '../../models/AccountRequest';
import { RequestStatus } from '../../models/enums/RequestStatus';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  registerBaseUrl: string = 'http://localhost:8080/api/v1/registrations/accountRequests';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  findAll(): Observable<AccountRequest[]> {
    return this.http.get<AccountRequest[]>(this.registerBaseUrl)
      .pipe(catchError(this.errorHandler.handleError));
  }

  sendAccountRequests(email: string, password:string, address: string): Observable<AccountRequest> {
    const payload: AccountRequest = { email, password, address };
    return this.http.post<AccountRequest>(this.registerBaseUrl, payload)
      .pipe(catchError(this.errorHandler.handleError));
  }

  handleAccountRequests(accountRequest: AccountRequest, requestStatus: RequestStatus): Observable<AccountRequest> {
    accountRequest.requestStatus = requestStatus;
    const payload: AccountRequest = accountRequest;
    return this.http.put<AccountRequest>(`${this.registerBaseUrl}`, payload)
      .pipe(catchError(this.errorHandler.handleError));
  }

}
