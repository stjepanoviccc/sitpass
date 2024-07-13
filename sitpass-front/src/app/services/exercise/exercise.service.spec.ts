import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ExerciseService } from './exercise.service';
import { ErrorHandlerService } from '../error/error.service';
import { Exercise } from '../../models/Exercise';
import { Facility } from '../../models/Facility';

describe('ExerciseService', () => {
    const facility: Facility = {id: 1, description: "desc", address: "address", city: "city"};
    const exercise: Exercise = {id: 1, facility};
    const exercises: Exercise[] = [exercise, exercise];
    let exerciseService: ExerciseService;
    let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ExerciseService, ErrorHandlerService]
    });
    exerciseService = TestBed.inject(ExerciseService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve all exercises by user', () => {
    exerciseService.findAllByUser().subscribe(e => {
      expect(e.length).toBe(2);
      expect(e).toEqual(exercises);
    });

    const req = httpMock.expectOne(`${exerciseService.exerciseBaseUrl}/users`);
    expect(req.request.method).toBe('GET');
    req.flush(exercises);
  });

  it('should retrieve all exercises by facility and user', () => {
    exerciseService.findAllByFacilityAndUser(1).subscribe(e => {
      expect(e.length).toBe(2);
      expect(e).toEqual(exercises);
    });

    const req = httpMock.expectOne(`${exerciseService.exerciseBaseUrl}/facilities/1`);
    expect(req.request.method).toBe('GET');
    req.flush(exercises);
  });

  it('should create a new exercise', () => {
    exerciseService.create(exercise, 1).subscribe(e => {
      expect(e).toEqual(exercise);
    });

    const req = httpMock.expectOne(`${exerciseService.exerciseBaseUrl}/facilities/1`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(exercise);
    req.flush(exercise);
  });

  it('should handle errors', () => {
    spyOn(TestBed.inject(ErrorHandlerService), 'handleError').and.callThrough();
    const errorHandler = TestBed.inject(ErrorHandlerService);
    const errorMessage = 'deliberate 404 error';

    exerciseService.findAllByUser().subscribe(
      () => fail('should have failed with the 404 error'),
      (error) => {
        expect(error).toBeTruthy();
      }
    );

    const req = httpMock.expectOne(`${exerciseService.exerciseBaseUrl}/users`);
    req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    expect(errorHandler.handleError).toHaveBeenCalled();
  });
});
