import { useEffect, useMemo, useState } from 'react'
import Header from '@/shared/components/layout/Header'
import Footer from '@/shared/components/layout/Footer'
import { useAuthStore } from '@/stores/auth.store'
import { wishlistApi, type WishlistProperty } from '../services/wishlist.api'
import { searchDetailProperty } from '@/features/property/services/propertyApi'
import type { Property } from '@/features/property/types/property/property.type'
import { toSlug } from '@/util/slug-conversion.util'
import { buildAndStorePropertySearchParams } from '@/features/property/util/build-param.util'
import { useNavigate } from 'react-router-dom'

function formatISODate(d: Date) {
    return d.toISOString().split('T')[0]
}

function mapDetailToProperty(detail: any): Property {
    return {
        id: detail.id,
        name: detail.name,
        type: detail.type,
        description: detail.description,
        address: detail.address,
        status: detail.status,
        mediaUrl: detail.media?.[0]?.url ?? '',
        minPrice: detail.minPrice,
        avgRating: detail.avgRating ?? null,
        totalRating: detail.totalRating ?? null,
        amenities: detail.amenities ?? [],
        facilities: detail.facilities ?? [],
    }
}

export default function WishlistPage() {
    const user = useAuthStore((s) => s.user)
    const isAuthenticated = useAuthStore((s) => s.isAuthenticated)
    const navigate = useNavigate()

    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [properties, setProperties] = useState<Property[]>([])

    const defaultQueryParams = useMemo(() => {
        const now = new Date()
        const checkIn = formatISODate(now)
        const checkOut = formatISODate(new Date(now.getTime() + 86400000))
        const next = new URLSearchParams()
        next.set('checkingDate', checkIn)
        next.set('checkoutDate', checkOut)
        next.set('adults', '2')
        next.set('children', '0')
        return next
    }, [])

    useEffect(() => {
        const fetchWishlist = async () => {
            if (!isAuthenticated || !user) return
            setLoading(true)
            setError(null)
            try {
                const data = await wishlistApi.fetchWishlistByUser(user.id)
                const wishlistProperties: WishlistProperty[] = data.wishlistProperties || []

                const details = await Promise.all(
                    wishlistProperties.map((p) =>
                        searchDetailProperty.fetchPropertyDetail({
                            propertyId: String(p.propertyId),
                            checkingDate: defaultQueryParams.get('checkingDate') || '',
                            checkoutDate: defaultQueryParams.get('checkoutDate') || '',
                            adults: defaultQueryParams.get('adults') || '',
                            children: defaultQueryParams.get('children') || '',
                        })
                    )
                )

                setProperties(details.map(mapDetailToProperty))
            } catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to load wishlist.')
            } finally {
                setLoading(false)
            }
        }

        fetchWishlist()
    }, [defaultQueryParams, isAuthenticated, user])

    if (!isAuthenticated) {
        return (
            <>
                <Header />
                <main className="min-h-screen flex items-center justify-center bg-gray-50 px-4 py-12">
                    <div className="text-center max-w-md w-full bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
                        <h1 className="text-2xl font-bold text-gray-900 mb-2">Your Wishlist</h1>
                        <p className="text-gray-600 text-sm mb-6">Please sign in to see saved properties.</p>
                        <button
                            onClick={() => navigate('/login')}
                            className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-xl h-11 font-semibold"
                        >
                            Go to Login
                        </button>
                    </div>
                </main>
                <Footer />
            </>
        )
    }

    return (
        <>
            <Header />
            <main className="bg-gray-50 pt-24 pb-16">
                <div className="max-w-7xl mx-auto px-4">
                    <div className="flex items-center justify-between gap-4 mb-8">
                        <h1 className="text-2xl font-bold text-gray-900">Wishlist</h1>
                        <p className="text-sm text-gray-500">{properties.length} saved</p>
                    </div>

                    {loading && (
                        <p className="text-center py-16 text-gray-500">Loading wishlist...</p>
                    )}

                    {error && (
                        <div className="text-center py-16 text-red-600 text-sm">{error}</div>
                    )}

                    {!loading && !error && properties.length === 0 && (
                        <div className="text-center py-16 bg-white rounded-2xl shadow-sm border border-gray-100">
                            <p className="text-gray-600 text-sm">No saved properties yet.</p>
                        </div>
                    )}

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                        {properties.map((property) => (
                            <div key={property.id} className="bg-white rounded-2xl overflow-hidden border border-gray-100 shadow-sm">
                                <div className="relative h-56 bg-gray-100">
                                    <img
                                        src={property.mediaUrl}
                                        alt={property.name}
                                        className="h-full w-full object-cover"
                                    />
                                </div>

                                <div className="p-5 space-y-4">
                                    <div>
                                        <div className="flex items-center justify-between gap-3">
                                            <h2 className="font-bold text-lg text-gray-900 line-clamp-1">
                                                {property.name}
                                            </h2>
                                            <span className="px-3 py-1 bg-blue-100 text-blue-700 text-xs rounded-full font-semibold">
                                                {property.type}
                                            </span>
                                        </div>
                                        <p className="text-sm text-gray-600 mt-1">
                                            {property.address.city}, {property.address.country}
                                        </p>
                                    </div>

                                    <div className="flex items-center justify-between gap-3">
                                        <div className="text-xs text-gray-500">
                                            From <span className="font-semibold text-gray-900">${property.minPrice}</span>/night
                                        </div>
                                        <div className="text-xs text-gray-500">
                                            {property.avgRating ? (
                                                <span className="font-semibold text-blue-600">
                                                    {property.avgRating.toFixed(1)} ★
                                                </span>
                                            ) : (
                                                'New'
                                            )}
                                        </div>
                                    </div>

                                    <button
                                        onClick={() => {
                                            // Ensure PropertyDetailPage has enough context to fetch.
                                            buildAndStorePropertySearchParams(defaultQueryParams, property.id)
                                            navigate(`/property/${toSlug(property.name)}`)
                                        }}
                                        className="w-full px-4 py-2 bg-blue-50 text-blue-700 text-sm font-bold rounded-xl hover:bg-blue-600 hover:text-white transition-colors"
                                    >
                                        View Details
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </main>
            <Footer />
        </>
    )
}

