import { Injectable } from '@angular/core';
import { Exercise } from '../../models/Exercise';
import { HttpClient } from '@angular/common/http';
import { ErrorHandlerService } from '../error/error.service';
import { Observable, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {
  exerciseBaseUrl: string = 'http://localhost:8080/api/v1/exercises';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }
  
  findAllByUser(): Observable<Exercise[]>{
    return this.http.get<Exercise[]>(`${this.exerciseBaseUrl}/users`).pipe(catchError(this.errorHandler.handleError));
  }

  findAllByFacilityAndUser(facilityId: number): Observable<Exercise[]> {
    return this.http.get<Exercise[]>(`${this.exerciseBaseUrl}/facilities/${facilityId}`).pipe(catchError(this.errorHandler.handleError));
  }

  create(exercise: Exercise, facilityId: number) {
    const payload: Exercise = exercise;
    return this.http.post<Exercise>(`${this.exerciseBaseUrl}/facilities/${facilityId}`, payload).pipe(catchError(this.errorHandler.handleError));
  }
}
