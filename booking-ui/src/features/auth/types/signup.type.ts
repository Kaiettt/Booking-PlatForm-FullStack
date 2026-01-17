export interface SignupRequest {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    phoneNumber?: string;
}

export interface SignupResponse {
    userId: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
}
