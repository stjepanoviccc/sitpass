import { Facility } from "./Facility";
import { Rate } from "./Rate";
import { User } from "./User";

export interface Review {
    id?: number;
    user?: User;
    facility?: Facility;
    rate: Rate;
    createdAt?: Date;
    exerciseCount?: number;
    hidden?: boolean;
    comment?: Comment;
    isDeleted?: boolean;
  }