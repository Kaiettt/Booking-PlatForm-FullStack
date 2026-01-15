import type { BookingStatus } from "@/types/booking.enum";
import type { PaymentMethod, PaymentStatus, PrepaymentType } from "@/types/payment.enum";

export interface Reservation {
    roomId: number;
    roomType: string;
    ratePlanId: number;
    ratePlanName: string;
    quantity: number;
    price: number;
}




export interface LockRoomDetail {
    roomTypeId: number;
    quantity: number;
}

export interface LockRoomRequest {
    checkIn: string; // ISO Date string
    checkOut: string;
    userId: number;
    lockRoomRequestDetail: LockRoomDetail[];
}

export interface LockRoomResponseDetail {
    roomTypeId: number;
    roomTypeName: string | null;
    quantity: number;
}

export interface LockRoomResponse {
    id: number;
    checkIn: string;
    checkOut: string;
    expiresAt: string;
    status: "SOFT_LOCK" | "HARD_LOCK"
    userId: number;
    details: LockRoomResponseDetail[];
}

export interface Guest {
    name: string;
    email: string;
    phone: string;
    nationality: string;
}

export interface Perk {
    id: number;
    code: string;
    name: string;
}


export interface BookingRequestItem {
    roomTypeId: number;
    quantity: number;
    ratePlaneId: number;
}

export interface BookingRequest {
    userId: number;
    checkIn: string;
    checkOut: string;
    paymentMethod: PaymentMethod;
    specialRequet: string;
    guest: Guest;
    items: BookingRequestItem[];
}
export interface RatePlanResponse {
    id: number;
    name: string;
    price: number;
    prepaymentType: PrepaymentType;
    perks: Perk[];
}

export interface BookingResponseItem {
    id: number;
    roomTypeId: number;
    roomTypeName: string | null;
    quantity: number;
    amount: number;
    ratePlan: RatePlanResponse;
}

export interface BookingResponse {
    id: number;
    userId: number;
    status: BookingStatus;
    paymentMethod: PaymentMethod;
    paymentStatus: PaymentStatus;
    totalAmount: number;
    checkIn: string;
    checkOut: string;
    guest: Guest;
    specialRequest: string;
    bookingReference: string;
    bookingItems: BookingResponseItem[];
}