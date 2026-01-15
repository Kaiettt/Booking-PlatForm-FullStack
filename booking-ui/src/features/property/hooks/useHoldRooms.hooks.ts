import { bookingApi } from "@/features/booking/services/booking.api";
import type { LockRoomRequest, LockRoomResponse, Reservation } from "@/features/booking/types/type";
import { useAuthStore } from "@/stores/auth.store";
import { useState } from "react";

export function useHoldRooms() {
     const [isLoading, setIsLoading] = useState(false); // Added loading state
     const [error, setError] = useState<string | null>(null);
     const user = useAuthStore((state) => state.user);

     const holdRooms = async (
          reservations: Reservation[],
          stayInfo: { checkIn: string; checkOut: string }
     ): Promise<LockRoomResponse> => {

          if (!user?.id) {
               const authError = "User session not found. Please log in.";
               setError(authError);
               throw new Error(authError);
          }

          setIsLoading(true); // Start loading
          setError(null);

          try {
               // Ensure dates include time component (using 14:00 for check-in and 12:00 for check-out as per common hotel practices)
               const checkInWithTime = `${stayInfo.checkIn.split('T')[0]}T14:00:00`;
               const checkOutWithTime = `${stayInfo.checkOut.split('T')[0]}T12:00:00`;

               const lockRequest: LockRoomRequest = {
                    checkIn: checkInWithTime,
                    checkOut: checkOutWithTime,
                    userId: user.id,
                    lockRoomRequestDetail: reservations.map((res) => ({
                         roomTypeId: res.roomId,
                         quantity: res.quantity,
                    })),
               };

               const response = await bookingApi.holdRooms(lockRequest);
               return response;

          } catch (err: any) {
               const errorMessage = err.response?.data?.message || "Failed to hold rooms. They may have been booked by someone else.";
               setError(errorMessage);
               throw new Error(errorMessage);
          } finally {
               setIsLoading(false); // Stop loading regardless of success/fail
          }
     };

     return {
          holdRooms,
          isLoading, // Return loading state
          error,
     };
}