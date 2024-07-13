import { RequestStatus } from "./enums/RequestStatus";

export interface AccountRequest {
    id?: number;
    email: string;
    password: string;
    rejectionReason?: string;
    createdAt?: Date;
    address?: string;
    requestStatus?: RequestStatus;
}