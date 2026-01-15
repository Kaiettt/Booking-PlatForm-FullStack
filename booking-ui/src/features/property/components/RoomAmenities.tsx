import { type FC } from 'react';

interface RoomAmenitiesProps {
    amenities: string[];
}

export const RoomAmenities: FC<RoomAmenitiesProps> = ({ amenities }) => {
    if (!amenities?.length) return null;

    return (
        <div className="space-y-2">
            <h4 className="text-sm font-medium text-gray-700">Room Amenities</h4>
            <ul className="grid grid-cols-1 sm:grid-cols-2 gap-1.5 text-sm text-gray-600">
                {amenities.map((amenity) => (
                    <li key={amenity} className="flex items-start">
                        <span className="inline-block w-1.5 h-1.5 rounded-full bg-blue-500 mt-2 mr-2 flex-shrink-0"></span>
                        <span>{amenity}</span>
                    </li>
                ))}
            </ul>
        </div>
    );
};
