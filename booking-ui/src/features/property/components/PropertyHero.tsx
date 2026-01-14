import { Star, MapPin } from "lucide-react"
import type { PropertyDetail } from "../types/property/property.detai.type"

interface Props {
    name: string
    type: string
    avgRating: number | null
    totalRating: number | null
    address: PropertyDetail["address"]
}

export default function PropertyHero({
    name,
    type,
    avgRating,
    totalRating,
    address
}: Props) {
    return (
        <section className="space-y-2">
            <div className="flex items-center gap-3">
                <h1 className="text-3xl font-bold">{name}</h1>
                <span className="px-3 py-1 bg-blue-100 text-blue-700 text-sm rounded-full">
                    {type}
                </span>
            </div>

            <div className="flex items-center gap-4 text-sm text-gray-600">
                <div className="flex items-center gap-1">
                    <MapPin className="w-4 h-4" />
                    <span>{address.fullAddress}</span>
                </div>

                {avgRating && (
                    <div className="flex items-center gap-1 font-semibold text-blue-600">
                        <Star className="w-4 h-4 fill-blue-600" />
                        {avgRating.toFixed(1)} ({totalRating} reviews)
                    </div>
                )}
            </div>
        </section>
    )
}
