import { Facility } from "./Facility";
import { User } from "./User";

export interface Manages {
    id?: number;
    user?: User;
    facility?: Facility;
    startDate: Date;
    endDate: Date;
    isDeleted?: boolean;
}