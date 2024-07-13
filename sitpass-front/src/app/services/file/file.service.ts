import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ErrorHandlerService } from '../error/error.service';
import { Image } from '../../models/Image';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  private baseUrl: string = 'http://localhost:8080/api/v1/sitpass-bucket';
  private imgBaseUrl: string = 'http://localhost:8080/api/v1/images';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getFileContent(path: string): Observable<string> {
    const url = `${this.baseUrl}/${path}`;
    return this.http.get(url, { responseType: 'arraybuffer' }).pipe(
      map((data: ArrayBuffer) => {
        const blob = new Blob([data], { type: 'image/jpeg' }); // Adjust type as per your image format
        return URL.createObjectURL(blob);
      }),
      catchError(error => {
        this.errorHandler.handleError(error);
        return throwError('Failed to fetch image');
      })
    );
  }

  //image
  findByUser(): Observable<Image> {
    return this.http.get<Image>(this.imgBaseUrl).pipe(catchError(this.errorHandler.handleError));
  }
}