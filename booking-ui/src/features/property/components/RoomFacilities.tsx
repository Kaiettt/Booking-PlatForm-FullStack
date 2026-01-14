import { type FC } from 'react';

interface RoomFacilitiesProps {
    facilities: string[];
}

export const RoomFacilities: FC<RoomFacilitiesProps> = ({ facilities }) => {
    if (!facilities?.length) return null;

    return (
        <div className="space-y-2">
            <h4 className="text-sm font-medium text-gray-700">Room Facilities</h4>
            <ul className="grid grid-cols-1 sm:grid-cols-2 gap-1.5 text-sm text-gray-600">
                {facilities.map((facility) => (
                    <li key={facility} className="flex items-start">
                        <span className="inline-block w-1.5 h-1.5 rounded-full bg-green-500 mt-2 mr-2 shrink-0"></span>
                        <span>{facility}</span>
                    </li>
                ))}
            </ul>
        </div>
    );
};
