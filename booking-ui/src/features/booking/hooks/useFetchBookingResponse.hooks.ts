import { useState, useEffect } from "react";
import type { BookingResponse } from "../types/type";
import { bookingApi } from "../services/booking.api";

export function useFetchBookingResponse() {
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [bookingData, setBookingData] = useState<BookingResponse | null>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchBooking = async () => {
            try {
                const bookingId = localStorage.getItem("newBooking");
                if (!bookingId) {
                    throw new Error("No booking ID found in local storage");
                }

                const id = Number(bookingId);
                if (isNaN(id)) {
                    throw new Error("Invalid booking ID format");
                }

                const response = await bookingApi.getBookingById(id);
                setBookingData(response as BookingResponse);
            } catch (err) {
                console.error("Error fetching booking:", err);
                setError(err instanceof Error ? err.message : "Failed to fetch booking");
            } finally {
                setIsLoading(false);
            }
        };

        fetchBooking();
    }, []);

    return { bookingData, isLoading, error };
}