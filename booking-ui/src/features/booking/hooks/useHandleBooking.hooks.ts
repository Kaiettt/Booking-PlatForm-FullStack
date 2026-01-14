import { useState } from "react";
import { bookingApi } from "../services/booking.api";
import { useAuthStore } from "@/stores/auth.store";
import type { BookingRequest, BookingResponse } from "../types/type";

export function useHandleBooking() {
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const user = useAuthStore((state) => state.user);

    const createBooking = async (
        guestData: any,
        paymentMethod: string,
        specialRequest: string,
        stayInfo: any,
        selectedRooms: any[]
    ): Promise<BookingResponse> => {
        setIsSubmitting(true);
        setError(null);

        try {
            const bookingRequest: BookingRequest = {
                userId: user?.id || 0,
                checkIn: stayInfo.checkIn,
                checkOut: stayInfo.checkOut,
                paymentMethod: paymentMethod.toUpperCase() as any,
                specialRequet: specialRequest,
                guest: guestData,
                items: selectedRooms.map((room) => ({
                    roomTypeId: Number(room.roomId),
                    quantity: room.quantity,
                    ratePlaneId: Number(room.ratePlanId),
                })),
            };

            const response = await bookingApi.handleBooking(bookingRequest);
            localStorage.setItem('newBooking', response.id.toString());
            localStorage.removeItem('selectedRooms');
            return response;
        } catch (err: any) {
            const msg = err.response?.data?.message || "Failed to complete booking.";
            setError(msg);
            throw new Error(msg);
        } finally {
            setIsSubmitting(false);
        }
    };

    return { createBooking, isSubmitting, error };
}