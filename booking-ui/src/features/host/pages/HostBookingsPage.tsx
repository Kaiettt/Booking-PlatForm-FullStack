import { useEffect, useState } from 'react'
import { useAuthStore } from '@/stores/auth.store'
import type { BookingResponse } from '@/features/booking/types/type'
import { hostApi } from '@/features/host/services/host.api'
import { Button } from '@/components/ui/button'

export default function HostBookingsPage() {
    const user = useAuthStore((s) => s.user)
    const isAuthenticated = useAuthStore((s) => s.isAuthenticated)

    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [bookings, setBookings] = useState<BookingResponse[]>([])

    const fetchBookings = async () => {
        if (!isAuthenticated || !user) return
        setLoading(true)
        setError(null)
        try {
            const data = await hostApi.getBookingsByHost(user.id)
            setBookings(data)
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to load bookings.')
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchBookings()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isAuthenticated, user?.id])

    const handleCheckIn = async (bookingId: number) => {
        await hostApi.checkIn(bookingId)
        await fetchBookings()
    }

    const handleCheckOut = async (bookingId: number) => {
        await hostApi.checkOut(bookingId)
        await fetchBookings()
    }

    if (!isAuthenticated || !user) {
        return (
            <div className="bg-white border border-gray-100 rounded-2xl p-6 text-sm text-gray-600">
                Please sign in as HOST to view bookings.
            </div>
        )
    }

    const canCheckIn = (status: string) => status === 'CONFIRMED' || status === 'PENDING'
    const canCheckOut = (status: string) => status === 'CHECK_IN'

    return (
        <div className="space-y-4">
            <div>
                <h1 className="text-2xl font-bold text-gray-900">Bookings</h1>
                <p className="text-sm text-gray-500 mt-1">{bookings.length} bookings</p>
            </div>

            {loading && <p className="text-gray-500">Loading...</p>}
            {error && <p className="text-red-600 text-sm">{error}</p>}

            {!loading && !error && (
                <div className="bg-white border border-gray-100 rounded-2xl overflow-hidden">
                    <div className="overflow-x-auto">
                        <table className="min-w-full text-sm">
                            <thead className="bg-gray-50 border-b border-gray-100">
                                <tr>
                                    <th className="text-left font-semibold p-4">Booking ID</th>
                                    <th className="text-left font-semibold p-4">Guest</th>
                                    <th className="text-left font-semibold p-4">Check-in</th>
                                    <th className="text-left font-semibold p-4">Check-out</th>
                                    <th className="text-left font-semibold p-4">Status</th>
                                    <th className="text-left font-semibold p-4">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {bookings.map((b) => {
                                    const status = b.status as unknown as string
                                    return (
                                        <tr key={b.id} className="border-b border-gray-50">
                                            <td className="p-4 font-medium text-gray-900">{b.id}</td>
                                            <td className="p-4">
                                                <div className="font-medium text-gray-900">{b.guest.name}</div>
                                                <div className="text-gray-500 text-xs">{b.guest.email}</div>
                                            </td>
                                            <td className="p-4">{b.checkIn}</td>
                                            <td className="p-4">{b.checkOut}</td>
                                            <td className="p-4">
                                                <span className="px-2 py-1 rounded-full text-xs font-semibold bg-gray-50 text-gray-700">
                                                    {status}
                                                </span>
                                            </td>
                                            <td className="p-4">
                                                <div className="flex flex-wrap gap-2">
                                                    <Button
                                                        variant="outline"
                                                        disabled={!canCheckIn(status)}
                                                        onClick={() => handleCheckIn(b.id)}
                                                        className="h-10"
                                                    >
                                                        Check-in
                                                    </Button>
                                                    <Button
                                                        variant="outline"
                                                        disabled={!canCheckOut(status)}
                                                        onClick={() => handleCheckOut(b.id)}
                                                        className="h-10"
                                                    >
                                                        Check-out
                                                    </Button>
                                                </div>
                                            </td>
                                        </tr>
                                    )
                                })}

                                {bookings.length === 0 && (
                                    <tr>
                                        <td colSpan={6} className="p-8 text-center text-gray-500">
                                            No bookings found.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </div>
    )
}

