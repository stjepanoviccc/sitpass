import { Facility } from "./Facility";
import { User } from "./User";

export interface Exercise {
    id?: number;
    user?: User;
    facility: Facility;
    from?: Date;
    until?: Date;
    isDeleted?: boolean;
}