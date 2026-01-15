import type { BookingRequest, BookingResponse, LockRoomRequest, LockRoomResponse } from "../types/type";
import { get, post } from "@/services/apiClient";

export const bookingApi = {
    async holdRooms(lockRoomRequest: LockRoomRequest): Promise<LockRoomResponse> {
        return post('/lock/rooms', lockRoomRequest);
    },
    async handleBooking(bookingRequest: BookingRequest): Promise<BookingResponse> {
        return post('/bookings', bookingRequest);
    },
    async getVnpayUrl(params: {
        amount: number;
        vnp_TxnRef: string;
        vnp_OrderInfo: string;
        ordertype: string;
        language: string;
    }): Promise<{ paymentUrl: string; message: string }> {
        const queryParams = new URLSearchParams();
        Object.entries(params).forEach(([key, value]) => {
            queryParams.append(key, String(value));
        });
        return post(`/payments?${queryParams.toString()}`, null);
    },
    async getBookingById(id: number):Promise<BookingResponse>{
        return get(`/bookings/${id}`);
    }

};