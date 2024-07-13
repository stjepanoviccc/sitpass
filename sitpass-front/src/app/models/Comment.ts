import { Review } from "./Review";
import { User } from "./User";

export interface Comment {
    id?: number;
    user?: User;
    text: string;
    createdAt?: Date;
    review?: Review;
    parentComment?: Comment;
    replies?: Comment[];
    isDeleted?: boolean;
  }