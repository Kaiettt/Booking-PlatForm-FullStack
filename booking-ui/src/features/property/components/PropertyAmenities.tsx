import {
    Wifi,
    Dumbbell,
    Coffee,
    Waves,
    ParkingSquare,
    CheckCircle,
    Utensils,
    Wind,
    Tv,
    Bath,
    Snowflake,
    Briefcase
} from "lucide-react"
import type { JSX } from "react";

interface Props {
    amenities: { id: number; name: string }[]
}

const amenityIcons: Record<string, JSX.Element> = {
    // Standard Mappings
    "Free WiFi": <Wifi className="w-5 h-5 text-blue-600" />,
    "WiFi": <Wifi className="w-5 h-5 text-blue-600" />,
    "Pool": <Waves className="w-5 h-5 text-cyan-600" />,
    "Swimming Pool": <Waves className="w-5 h-5 text-cyan-600" />,
    "Gym": <Dumbbell className="w-5 h-5 text-orange-600" />,
    "Fitness Center": <Dumbbell className="w-5 h-5 text-orange-600" />,
    "Breakfast included": <Coffee className="w-5 h-5 text-amber-700" />,
    "Breakfast": <Coffee className="w-5 h-5 text-amber-700" />,
    "Parking": <ParkingSquare className="w-5 h-5 text-slate-600" />,
    "Free Parking": <ParkingSquare className="w-5 h-5 text-slate-600" />,
    "Restaurant": <Utensils className="w-5 h-5 text-red-600" />,
    "Air Conditioning": <Snowflake className="w-5 h-5 text-sky-500" />,
    "AC": <Snowflake className="w-5 h-5 text-sky-500" />,
    "Hair Dryer": <Wind className="w-5 h-5 text-purple-600" />,
    "TV": <Tv className="w-5 h-5 text-slate-800" />,
    "Bathtub": <Bath className="w-5 h-5 text-teal-600" />,
    "Work space": <Briefcase className="w-5 h-5 text-indigo-600" />
}

export default function PropertyAmenities({ amenities }: Props) {
    if (!amenities.length) return null

    return (
        <section className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
            <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center">
                <span className="w-8 h-8 rounded-lg bg-blue-100 flex items-center justify-center mr-3">
                    <CheckCircle className="w-5 h-5 text-blue-600" />
                </span>
                Popular Amenities
            </h3>

            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {amenities.map(a => (
                    <div
                        key={a.id}
                        className="flex items-center gap-3 p-3 rounded-xl hover:bg-gray-50 transition-colors duration-200 group"
                    >
                        <div className="w-10 h-10 rounded-full bg-gray-50 group-hover:bg-white border border-gray-100 group-hover:border-gray-200 shadow-sm flex items-center justify-center transition-all duration-200">
                            {amenityIcons[a.name] ?? (
                                <CheckCircle className="w-5 h-5 text-green-600" />
                            )}
                        </div>
                        <span className="text-gray-700 font-medium text-sm group-hover:text-gray-900 transition-colors">
                            {a.name}
                        </span>
                    </div>
                ))}
            </div>
        </section>
    )
}
