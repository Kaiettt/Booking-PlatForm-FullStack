import { get, post } from '@/services/apiClient'
import type { BookingResponse } from '@/features/booking/types/type'
import type { RoomType } from '@/features/room/room-type.type'
import { apiClient } from '@/services/apiClient'

export const hostApi = {
    async getBookingsByHost(hostId: number): Promise<BookingResponse[]> {
        return get<BookingResponse[]>(`/bookings/host/${hostId}`)
    },

    async checkIn(bookingId: number) {
        return post(`/bookings/check-in/${bookingId}`, null)
    },

    async checkOut(bookingId: number) {
        return post(`/bookings/check-out/${bookingId}`, null)
    },

    async getRoomTypesByProperty(propertyId: number): Promise<RoomType[]> {
        return get<RoomType[]>(`/properties/room-type/${propertyId}`, {})
    },

    async createRoomType(payload: any) {
        // Backend returns no body (Location header only); UI will refresh and re-select.
        return post(`/room-type`, payload)
    },

    async uploadRoomTypeMedia(roomTypeId: number, files: File[]) {
        const fd = new FormData()
        files.forEach((f) => fd.append('files', f))
        return apiClient.post(`/room-type/${roomTypeId}/media`, fd, {
            headers: { 'Content-Type': 'multipart/form-data' },
        })
    },
}

