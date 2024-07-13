import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ErrorHandlerService } from '../error/error.service';
import { Comment } from '../../models/Comment';
import { Observable, catchError } from 'rxjs';
import { Review } from '../../models/Review';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  commentBaseUrl: string = 'http://localhost:8080/api/v1/comments';
  
  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  findAllByParentComment(parentCommentId: number) {
    return this.http.get<Comment[]>(`${this.commentBaseUrl}/${parentCommentId}`).pipe(catchError(this.errorHandler.handleError));
  }

  findByParentComment(parentCommentId: number) {
    return this.http.get<Comment>(`${this.commentBaseUrl}/${parentCommentId}`).pipe(catchError(this.errorHandler.handleError));
  }

  create(review: Review, text: string): Observable<Comment> {
    const payload: Comment = {text};
    return this.http.post<Comment>(`${this.commentBaseUrl}/reviews/${review.id}`, payload).pipe(catchError(this.errorHandler.handleError));
  }

  addReply(review: Review, parentComment: Comment, text:string): Observable<Comment> {
    const payload: Comment = {text};
    return this.http.post<Comment>(`${this.commentBaseUrl}/${parentComment.id}/reviews/${review.id}`, payload).pipe(catchError(this.errorHandler.handleError));
  }
}
