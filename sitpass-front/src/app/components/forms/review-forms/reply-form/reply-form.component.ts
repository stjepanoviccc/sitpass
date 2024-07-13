import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from '../../../shared/button/button.component';
import { Comment } from '@angular/compiler';
import { Review } from '../../../../models/Review';

@Component({
  selector: 'app-reply-form',
  standalone: true,
  imports: [ButtonComponent, FormsModule],
  templateUrl: './reply-form.component.html',
})
export class ReplyFormComponent implements OnChanges {
  @Input() reply!: Comment;
  @Input() review!: Review;
  @Output() replyAdded: EventEmitter<{ review: Review, lastComment: Comment, newReplyText: string }> = new EventEmitter<{ review: Review, lastComment: Comment, newReplyText: string }>(); 
  errorMsg:string = '';

  formData: any = {
    text: ''
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['reply'] && changes['reply'].currentValue) {}
  }

  onSubmit(): void {
    if (this.formData.text.trim() !== '') {
      this.replyAdded.emit({ review: this.review, lastComment: this.reply, newReplyText: this.formData.text });
      this.formData.text = '';
    }
  }
}
