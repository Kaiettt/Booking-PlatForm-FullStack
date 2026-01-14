import { useState } from 'react';
import { User, Bed, Minus, Plus, Check } from 'lucide-react';
import { RoomAmenities } from './RoomAmenities';
import { RoomFacilities } from './RoomFacilities';
import { useNavigate } from 'react-router-dom';
import type { RoomType } from '@/features/room/room-type.type';
import type { Reservation } from '@/features/booking/types/type';
import { useHoldRooms } from '../hooks/useHoldRooms.hooks';

interface Props {
    roomTypes: RoomType[];
    availability: {
        checkIn: string;
        checkOut: string;
        adults: number;
        children: number;
    };
}

interface SelectedRooms {
    [roomId: number]: {
        [ratePlanId: number]: number;
    };
}

export default function PropertyRooms({ roomTypes, availability }: Props) {
    const navigate = useNavigate();
    const [selectedRooms, setSelectedRooms] = useState<SelectedRooms>({});
    const { holdRooms, isLoading } = useHoldRooms();
    if (!roomTypes || roomTypes.length === 0) return null;

    const getSelectedRoomCount = (roomId: number, ratePlanId: number): number => {
        return selectedRooms[roomId]?.[ratePlanId] ?? 0;
    };

    const handleRoomChange = (roomId: number, ratePlanId: number, value: string) => {
        const numValue = Number(value);
        setSelectedRooms(prev => {
            const newState = { ...prev };

            if (numValue > 0) {
                if (!newState[roomId]) newState[roomId] = {};
                newState[roomId][ratePlanId] = numValue;
            } else {
                if (newState[roomId]) {
                    delete newState[roomId][ratePlanId];
                    if (Object.keys(newState[roomId]).length === 0) {
                        delete newState[roomId];
                    }
                }
            }

            return newState;
        });
    };

    const handleReserve = async () => {
        const reservations: Reservation[] = [];

        // Data preparation
        for (const [roomIdStr, ratePlans] of Object.entries(selectedRooms)) {
            const roomId = Number(roomIdStr);
            const room = roomTypes.find(r => r.id === roomId);
            if (!room) continue;

            for (const [ratePlanIdStr, quantity] of Object.entries<number>(ratePlans)) {
                const ratePlanId = Number(ratePlanIdStr);
                if (quantity <= 0) continue;

                const ratePlan = room.ratePlans.find(rp => rp.id === ratePlanId);
                if (!ratePlan) continue;

                reservations.push({
                    roomId,
                    roomType: room.name,
                    ratePlanId,
                    ratePlanName: ratePlan.name,
                    quantity,
                    price: ratePlan.price
                });
            }
        }

        if (reservations.length === 0) {
            alert('Please select at least 1 room');
            return;
        }

        try {
            // 2. Await the holdRooms function from the hook
            await holdRooms(reservations, availability);

            // 3. Only if the call succeeds, save to storage and navigate
            localStorage.setItem('selectedRooms', JSON.stringify(reservations));
            localStorage.setItem('stay-info', JSON.stringify(availability));

            navigate("/confirm-booking");
        } catch (err) {
            // Error is already handled/set by the hook's internal state
            console.error("Reservation failed:", err);
        }
    };

    return (
        <section className="space-y-6" id="rooms">
            <div className="flex items-center justify-between">
                <h2 className="text-2xl font-bold text-gray-900">Available Rooms</h2>
            </div>

            <div className="overflow-hidden rounded-2xl border border-gray-200 shadow-sm bg-white">
                <div className="overflow-x-auto">
                    <table className="w-full min-w-[800px] table-auto text-left">
                        <thead className="bg-gray-50/80 backdrop-blur border-b border-gray-100">
                            <tr>
                                <th className="px-6 py-5 text-sm font-bold text-gray-900 w-[35%]">Room Type</th>
                                <th className="px-6 py-5 text-sm font-bold text-gray-900 w-[15%]">Sleeps</th>
                                <th className="px-6 py-5 text-sm font-bold text-gray-900 w-[15%]">Price</th>
                                <th className="px-6 py-5 text-sm font-bold text-gray-900 w-[20%]">Your Choices</th>
                                <th className="px-6 py-5 text-sm font-bold text-gray-900 w-[15%] text-right">Select</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-100">
                            {roomTypes.flatMap(room =>
                                room.ratePlans.map((ratePlan, index) => {
                                    const currentCount = getSelectedRoomCount(room.id, ratePlan.id);
                                    const isSelected = currentCount > 0;

                                    return (
                                        <tr
                                            key={`${room.id}-${ratePlan.id}`}
                                            className={`transition-colors duration-200 ${isSelected ? 'bg-blue-50/50' : 'hover:bg-gray-50'}`}
                                        >
                                            {index === 0 && (
                                                <td rowSpan={room.ratePlans.length} className="px-6 py-6 border-r border-gray-100 bg-white align-top">
                                                    <div className="space-y-4">
                                                        <div>
                                                            <h3 className="text-lg font-bold text-blue-600 mb-2">{room.name}</h3>
                                                            <div className="flex items-center text-sm font-medium text-gray-600 bg-gray-100 w-fit px-3 py-1.5 rounded-lg">
                                                                <Bed className="h-4 w-4 mr-2 text-gray-500" />
                                                                {room.bedType}
                                                            </div>
                                                        </div>
                                                        <div className="pt-2 border-t border-dashed border-gray-200">
                                                            <RoomAmenities amenities={room.roomAmenities} />
                                                        </div>
                                                        <RoomFacilities facilities={room.roomFacilities} />
                                                    </div>
                                                </td>
                                            )}
                                            <td className="px-6 py-6 align-top">
                                                <div className="flex flex-col gap-1">
                                                    <div className="flex items-center gap-2 text-gray-700 font-medium">
                                                        <User className="h-5 w-5 text-gray-400" />
                                                        <span>x {room.maxAdults}</span>
                                                    </div>
                                                    {room.maxChildren > 0 && (
                                                        <div className="text-xs text-gray-500 pl-7">
                                                            + {room.maxChildren} children
                                                        </div>
                                                    )}
                                                </div>
                                            </td>
                                            <td className="px-6 py-6 align-top">
                                                <div className="flex flex-col">
                                                    <span className="text-xl font-bold text-gray-900">${ratePlan.price}</span>
                                                    <span className="text-xs text-gray-500">per night</span>
                                                </div>
                                            </td>
                                            <td className="px-6 py-6 align-top">
                                                <div className="font-bold text-gray-900 mb-2">{ratePlan.name}</div>
                                                <div className="space-y-1.5">
                                                    {ratePlan.perks?.map(perk => (
                                                        <div key={perk.id} className="flex items-start text-xs font-medium text-emerald-700 bg-emerald-50 px-2 py-1 rounded">
                                                            <Check className="h-3.5 w-3.5 mr-1.5 mt-0.5 flex-shrink-0 text-emerald-600" />
                                                            {perk.name}
                                                        </div>
                                                    ))}
                                                </div>
                                            </td>
                                            <td className="px-6 py-6 align-top text-right">
                                                <div className="flex items-center justify-end gap-3">
                                                    {currentCount > 0 && (
                                                        <button
                                                            onClick={() => handleRoomChange(room.id, ratePlan.id, String(currentCount - 1))}
                                                            className="w-8 h-8 flex items-center justify-center rounded-full border border-gray-300 text-gray-600 hover:border-red-500 hover:text-red-500 hover:bg-red-50 transition-colors"
                                                        >
                                                            <Minus className="w-4 h-4" />
                                                        </button>
                                                    )}

                                                    <div className={`w-8 text-center font-bold ${currentCount > 0 ? 'text-blue-600 text-lg' : 'text-gray-300'}`}>
                                                        {currentCount}
                                                    </div>

                                                    <button
                                                        onClick={() => handleRoomChange(room.id, ratePlan.id, String(currentCount + 1))}
                                                        className={`w-8 h-8 flex items-center justify-center rounded-full border transition-all ${currentCount > 0
                                                            ? 'border-blue-600 text-blue-600 bg-blue-50'
                                                            : 'border-blue-600 text-blue-600 hover:bg-blue-600 hover:text-white'
                                                            }`}
                                                    >
                                                        <Plus className="w-4 h-4" />
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    );
                                })
                            )}
                        </tbody>
                    </table>
                </div>
            </div>

            <div className="sticky bottom-0 bg-white/80 backdrop-blur-lg border-t border-gray-200 p-4 -mx-4 md:static md:bg-transparent md:border-0 md:p-0 md:mx-0 flex flex-col md:flex-row justify-between items-center gap-4 mt-8">
                <div className="hidden md:block">
                    <p className="text-sm text-gray-500">
                        <span className="font-bold text-gray-900">No hidden fees.</span> You'll confirm detailed payment on the next step.
                    </p>
                </div>
                <button
                    onClick={handleReserve}
                    disabled={isLoading}
                    className={`w-full md:w-auto flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-8 py-3.5 rounded-xl text-lg font-bold transition-all shadow-lg shadow-blue-500/30 hover:shadow-blue-500/50 hover:-translate-y-0.5 active:translate-y-0 disabled:opacity-75 disabled:cursor-not-allowed`}
                >
                    {isLoading ? (
                        <>
                            <svg className="animate-spin -ml-1 mr-2 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                            </svg>
                            Processing...
                        </>
                    ) : (
                        "Reserve Now"
                    )}
                </button>
            </div>
        </section>
    );
}
