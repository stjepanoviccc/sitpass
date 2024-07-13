import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { WrapComponent } from '../../components/shared/wrap/wrap.component';
import { ReplyFormComponent } from '../../components/forms/review-forms/reply-form/reply-form.component';
import { ReviewService } from '../../services/review/review.service';
import { Review } from '../../models/Review';
import { CommentService } from '../../services/review/comment.service';
import { ManagesService } from '../../services/manages/manages.service';

@Component({
  selector: 'app-review-page',
  standalone: true,
  imports: [WrapComponent, ReplyFormComponent, CommonModule],
  templateUrl: './review-page.component.html',
})
export class ReviewPageComponent {
  reviewId!: number;
  review!: Review | any;
  parentComment!: any;
  isManager: Boolean = false;
  replies: any[] = [];
  lastComment: any;
  newReplyText: string = '';
  errorMsg: string = '';

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private reviewService: ReviewService,
    private commentService: CommentService,
    public managesService: ManagesService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.reviewId = +params['id'];
      this.getReview();
    });

  }

  getReview(): void {
    this.replies = [];
    this.reviewService
      .findById(this.reviewId)
      .subscribe((loadedReview: Review) => {
        this.review = loadedReview;
        this.parentComment = loadedReview.comment;
        this.getReplies();
        this.isUserManager();
      });
  }

  isUserManager(): void {
    this.managesService.isManager(this.review!.facility!.id!).subscribe((state) => {
      this.isManager = state;
      console.log(this.review.facility!.id!)
      console.log(state);
    })
  }

  getReplies(): void {
    if(this.parentComment != null) {
      this.commentService
      .findByParentComment(this.parentComment.id)
      .subscribe((reply: any) => {
        if(reply){
          this.replies.push(reply);
          this.cdr.detectChanges();
          this.getRecursiveReplies(reply.id);
        }
      });
    }
  }

  getRecursiveReplies(replyId: number): void {
    this.commentService.findByParentComment(replyId).subscribe((reply: any) => {
      if (reply) {
        this.replies.push(reply);
        this.cdr.detectChanges();
        if(reply.id != null) {
          this.getRecursiveReplies(reply.id);
        }
      }
    });
  }

  addReply(data: {
    review: Review;
    lastComment: any;
    newReplyText: string;
  }): void {
    this.commentService
      .addReply(data.review, data.lastComment, data.newReplyText)
      .subscribe(
        (newReply) => {
          this.replies.push(newReply);
          this.lastComment = newReply;
          this.errorMsg = '';
        },
        (err: any) => {
          this.errorMsg = err;
          setTimeout(() => {  this.errorMsg = ""; }, 10000);
        }
      );
  }

  goBack(): void {
    this.location.back();
  }
}
