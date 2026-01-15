import { useEffect } from 'react';

interface Props {
    availability: {
        checkIn: string;
        checkOut: string;
        adults: number;
        children: number;
    };
    setAvailability: React.Dispatch<React.SetStateAction<{
        checkIn: string;
        checkOut: string;
        adults: number;
        children: number;
    }>>;
}

export default function PropertyAvailability({ availability, setAvailability }: Props) {
    // Set default check-in to today and check-out to tomorrow
    useEffect(() => {
        const today = new Date();
        const tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);

        const formatDate = (date: Date) => {
            return date.toISOString().split('T')[0];
        };

        if (!availability.checkIn) {
            setAvailability(prev => ({
                ...prev,
                checkIn: formatDate(today),
                checkOut: formatDate(tomorrow)
            }));
        }
    }, []);

    const handleCheckInChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const checkInDate = new Date(e.target.value);
        const checkOutDate = new Date(availability.checkOut);

        // If check-in is after current check-out, update check-out to be next day
        if (checkInDate >= checkOutDate) {
            const nextDay = new Date(checkInDate);
            nextDay.setDate(nextDay.getDate() + 1);

            setAvailability(prev => ({
                ...prev,
                checkIn: e.target.value,
                checkOut: nextDay.toISOString().split('T')[0]
            }));
        } else {
            setAvailability(prev => ({
                ...prev,
                checkIn: e.target.value
            }));
        }
    };

    const handleCheckOutChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const checkInDate = new Date(availability.checkIn);
        const checkOutDate = new Date(e.target.value);

        // Ensure check-out is after check-in
        if (checkOutDate > checkInDate) {
            setAvailability(prev => ({
                ...prev,
                checkOut: e.target.value
            }));
        }
    };
    return (
        <section className="p-6 bg-white rounded-2xl shadow-md space-y-5">
            <h3 className="text-lg font-semibold">Check availability</h3>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                {/* Check-in */}
                <div className="space-y-1">
                    <label className="text-xs font-medium text-gray-600">Check-in</label>
                    <input
                        type="date"
                        value={availability.checkIn}
                        onChange={handleCheckInChange}
                        min={new Date().toISOString().split('T')[0]}
                        className="w-full px-4 py-3 border border-gray-200 rounded-lg text-sm
                                   focus:ring-2 focus:ring-blue-500 focus:outline-none
                                   hover:border-gray-300 transition"
                        required
                    />
                </div>

                {/* Check-out */}
                <div className="space-y-1">
                    <label className="text-xs font-medium text-gray-600">Check-out</label>
                    <input
                        type="date"
                        value={availability.checkOut}
                        onChange={handleCheckOutChange}
                        min={availability.checkIn || new Date().toISOString().split('T')[0]}
                        className="w-full px-4 py-3 border border-gray-200 rounded-lg text-sm
                                   focus:ring-2 focus:ring-blue-500 focus:outline-none
                                   hover:border-gray-300 transition"
                        required
                    />
                </div>

                {/* Adults */}
                <div className="space-y-1">
                    <label className="text-xs font-medium text-gray-600">Adults</label>
                    <input
                        type="number"
                        min={1}
                        value={availability.adults}
                        onChange={e => setAvailability(prev => ({ ...prev, adults: Number(e.target.value) }))}
                        className="w-full px-4 py-3 border border-gray-200 rounded-lg text-sm
                                   focus:ring-2 focus:ring-blue-500 focus:outline-none
                                   hover:border-gray-300 transition"
                    />
                </div>

                {/* Children */}
                <div className="space-y-1">
                    <label className="text-xs font-medium text-gray-600">Children</label>
                    <input
                        type="number"
                        min={0}
                        value={availability.children}
                        onChange={e => setAvailability(prev => ({ ...prev, children: Number(e.target.value) }))}
                        className="w-full px-4 py-3 border border-gray-200 rounded-lg text-sm
                                   focus:ring-2 focus:ring-blue-500 focus:outline-none
                                   hover:border-gray-300 transition"
                    />
                </div>
            </div>

            <button
                className="w-full sm:w-auto px-8 py-3 bg-blue-600 text-white rounded-lg font-semibold text-sm
                           hover:bg-blue-700 transition"
            >
                Search
            </button>
        </section>
    );
}
