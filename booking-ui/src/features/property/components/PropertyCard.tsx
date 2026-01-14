import type { Property } from '@/features/property/types/property/property.type'
import { Heart, Star, MapPin } from 'lucide-react'
import { useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { toSlug } from '@/util/slug-conversion.util'
import { buildAndStorePropertySearchParams } from '../util/build-param.util'

interface Props {
    property: Property
}

export default function PropertyCard({ property }: Props) {
    const [isFavorite, setIsFavorite] = useState(false)
    const navigate = useNavigate()
    const [searchParams] = useSearchParams()

    const handleViewPropertyDetail = () => {
        const slug = toSlug(property.name)
        buildAndStorePropertySearchParams(searchParams, property.id)
        navigate(`/property/${slug}`)
    }

    return (
        <div
            onClick={handleViewPropertyDetail}
            className="group bg-white rounded-2xl overflow-hidden border border-gray-100 shadow-sm hover:shadow-xl hover:shadow-blue-900/5 transition-all duration-300 cursor-pointer flex flex-col h-full"
        >
            {/* Image Container */}
            <div className="relative h-64 overflow-hidden">
                <img
                    src={property.mediaUrl}
                    alt={property.name}
                    className="h-full w-full object-cover transition-transform duration-700 group-hover:scale-110"
                />

                {/* Overlays */}
                <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-60" />

                {/* Top Badge Row */}
                <div className="absolute top-4 left-4 flex gap-2">
                    <span className="bg-white/95 backdrop-blur-sm text-xs font-bold px-3 py-1.5 rounded-full shadow-sm text-gray-800 uppercase tracking-wide">
                        {property.type}
                    </span>
                    {property.avgRating && property.avgRating >= 4.5 && (
                        <span className="bg-blue-600/90 backdrop-blur-sm text-xs font-bold px-3 py-1.5 rounded-full shadow-sm text-white flex items-center gap-1">
                            <Star className="w-3 h-3 fill-current" />
                            Top Rated
                        </span>
                    )}
                </div>

                {/* Favorite Button */}
                <button
                    onClick={(e) => {
                        e.stopPropagation();
                        setIsFavorite(!isFavorite);
                    }}
                    className="absolute top-4 right-4 p-2.5 bg-white/20 hover:bg-white backdrop-blur-md rounded-full transition-all duration-300 group/heart"
                >
                    <Heart
                        className={`w-5 h-5 transition-colors duration-300 ${isFavorite
                            ? 'fill-red-500 text-red-500'
                            : 'text-white group-hover/heart:text-red-500'
                            }`}
                    />
                </button>

                {/* Rating Badge (Bottom Left overlay) */}
                {property.avgRating && (
                    <div className="absolute bottom-4 left-4 flex items-center gap-2">
                        <div className="bg-blue-600 text-white w-10 h-10 flex items-center justify-center rounded-xl font-bold text-sm shadow-lg">
                            {property.avgRating.toFixed(1)}
                        </div>
                        <div className="text-white text-sm font-medium drop-shadow-md">
                            {property.avgRating >= 4.5 ? 'Excellent' : 'Very Good'}
                        </div>
                    </div>
                )}
            </div>

            {/* Content */}
            <div className="p-5 flex flex-col flex-1">
                {/* Header */}
                <div className="mb-3">
                    <h3 className="font-bold text-xl text-gray-900 line-clamp-1 group-hover:text-blue-600 transition-colors mb-1">
                        {property.name}
                    </h3>

                    <div className="flex items-center text-gray-500 text-sm">
                        <MapPin className="w-4 h-4 mr-1 text-gray-400" />
                        <span className="line-clamp-1">
                            {property.address.city}, {property.address.country}
                        </span>
                    </div>
                </div>

                {/* Amenities */}
                <div className="flex flex-wrap gap-2 mb-6">
                    {property.amenities.slice(0, 3).map((a) => (
                        <span
                            key={a.id}
                            className="text-xs font-medium bg-gray-50 text-gray-600 px-2.5 py-1 rounded-md border border-gray-100"
                        >
                            {a.name}
                        </span>
                    ))}
                    {property.amenities.length > 3 && (
                        <span className="text-xs text-gray-400 self-center font-medium px-1">
                            +{property.amenities.length - 3} more
                        </span>
                    )}
                </div>

                {/* Footer (Price & Action) */}
                <div className="mt-auto flex items-end justify-between border-t border-gray-50 pt-4">
                    <div>
                        <p className="text-xs font-medium text-gray-400 uppercase tracking-wider mb-0.5">Start from</p>
                        <div className="flex items-baseline gap-1">
                            <span className="text-2xl font-bold text-gray-900 group-hover:text-blue-600 transition-colors">
                                ${property.minPrice}
                            </span>
                            <span className="text-sm font-medium text-gray-500">/ night</span>
                        </div>
                    </div>

                    <button className="px-4 py-2 bg-blue-50 text-blue-600 text-sm font-bold rounded-xl group-hover:bg-blue-600 group-hover:text-white transition-all duration-300">
                        View Details
                    </button>
                </div>
            </div>
        </div>
    )
}
