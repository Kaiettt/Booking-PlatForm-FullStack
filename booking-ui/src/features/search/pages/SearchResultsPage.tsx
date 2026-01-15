import { useSearchParams } from 'react-router-dom'
import { useMemo } from 'react'
import Header from '@/shared/components/layout/Header'
import Footer from '@/shared/components/layout/Footer'
import PropertyFilterSidebar from '@/shared/components/layout/PropertyFilterSidebar'
import PropertySearchContainer from '../components/PropertySearchContainer'
import { useSearchProperties } from '../hooks/useSearchProperties'
import type { PropertyFilters } from '../types/search-filters.type'
import {
    amenitiesList as defaultAmenitiesList,
    facilitiesList as defaultFacilitiesList,
    roomFacilitiesList as defaultRoomFacilitiesList,
} from '../data/filter-options'
// import Breadcrumb from '@/shared/components/layout/Breadcrumb'

export default function SearchResultsPage() {
    const [searchParams, setSearchParams] = useSearchParams()

    const sidebarFilters = useMemo<PropertyFilters>(() => ({
        adults: Number(searchParams.get('adults')) || 1,
        children: Number(searchParams.get('children')) || 0,
        minPrice: searchParams.get('minPrice') ? Number(searchParams.get('minPrice')) : undefined,
        maxPrice: searchParams.get('maxPrice') ? Number(searchParams.get('maxPrice')) : undefined,
        propertyTypes: searchParams.getAll('propertyType') || [],
        minRating: searchParams.get('minRating') ? Number(searchParams.get('minRating')) : undefined,
        propertyAmenities: searchParams.getAll('propertyAmenities').map(Number).filter(Boolean),
        propertyFacilities: searchParams.getAll('propertyFacilities').map(Number).filter(Boolean),
        roomFacilities: searchParams.getAll('roomFacilities').map(Number).filter(Boolean),
        checkingDate: searchParams.get('checkingDate') || undefined,
        checkoutDate: searchParams.get('checkoutDate') || undefined,
        page: searchParams.get('page') ? Number(searchParams.get('page')) : 1,
        size: searchParams.get('size') ? Number(searchParams.get('size')) : 10,
        city: searchParams.get('city') || '',
    }), [searchParams])

    // Memoize the params to prevent unnecessary re-renders
    const params = useMemo(() => ({
        // Location & date
        city: searchParams.get('city') ?? '',

        checkingDate:
            searchParams.get('checkingDate') ??
            new Date().toISOString().split('T')[0],

        checkoutDate:
            searchParams.get('checkoutDate') ??
            new Date(Date.now() + 86400000).toISOString().split('T')[0],

        // Guests
        adults: Number(searchParams.get('adults') ?? 1),
        children: Number(searchParams.get('children') ?? 0),

        // Price
        minPrice: searchParams.get('minPrice')
            ? Number(searchParams.get('minPrice'))
            : undefined,

        maxPrice: searchParams.get('maxPrice')
            ? Number(searchParams.get('maxPrice'))
            : undefined,

        // Property type (multi)
        propertyTypes: searchParams.getAll('propertyType'),

        // Rating
        minRating: searchParams.get('minRating')
            ? Number(searchParams.get('minRating'))
            : undefined,

        // Amenities / Facilities (multi)
        amenities: searchParams
            .getAll('propertyAmenities')
            .map(Number)
            .filter(Boolean)
            .map(id => defaultAmenitiesList.find(x => x.id === id)?.name)
            .filter((v): v is string => Boolean(v)),
        facilities: searchParams
            .getAll('propertyFacilities')
            .map(Number)
            .filter(Boolean)
            .map(id => defaultFacilitiesList.find(x => x.id === id)?.name)
            .filter((v): v is string => Boolean(v)),
        roomFacilities: searchParams
            .getAll('roomFacilities')
            .map(Number)
            .filter(Boolean)
            .map(id => defaultRoomFacilitiesList.find(x => x.id === id)?.name)
            .filter((v): v is string => Boolean(v)),

        // Pagination
        page: Number(searchParams.get('page') ?? 1),
        size: Number(searchParams.get('size') ?? 10),
    }), [searchParams])


    // Only fetch when params change
    const { result, loading, error } = useSearchProperties(params)

    return (
        <>
            <Header />

            <main className="pt-24 pb-16 bg-gray-50">
                <div className="max-w-7xl mx-auto px-4">
                    {/* Title */}
                    <h1 className="text-2xl font-bold mb-6">
                        Search results for "{params.city || 'all locations'}"
                    </h1>

                    <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
                        {/* Sidebar */}
                        <aside className="lg:col-span-1">
                            <PropertyFilterSidebar
                                filters={sidebarFilters}
                                onChange={(newFilters: PropertyFilters) => {
                                    const next = new URLSearchParams()

                                    const city = searchParams.get('city')
                                    const checkingDate = searchParams.get('checkingDate')
                                    const checkoutDate = searchParams.get('checkoutDate')

                                    if (city) next.set('city', city)
                                    if (checkingDate) next.set('checkingDate', checkingDate)
                                    if (checkoutDate) next.set('checkoutDate', checkoutDate)

                                    next.set('adults', String(newFilters.adults))
                                    next.set('children', String(newFilters.children))

                                    if (newFilters.minPrice !== undefined) next.set('minPrice', String(newFilters.minPrice))
                                    if (newFilters.maxPrice !== undefined) next.set('maxPrice', String(newFilters.maxPrice))
                                    if (newFilters.minRating !== undefined) next.set('minRating', String(newFilters.minRating))

                                    newFilters.propertyTypes.forEach(t => next.append('propertyType', t))
                                    newFilters.propertyAmenities.forEach(id => next.append('propertyAmenities', String(id)))
                                    newFilters.propertyFacilities.forEach(id => next.append('propertyFacilities', String(id)))
                                    newFilters.roomFacilities.forEach(id => next.append('roomFacilities', String(id)))

                                    if (newFilters.page !== undefined) next.set('page', String(newFilters.page))
                                    if (newFilters.size !== undefined) next.set('size', String(newFilters.size))

                                    setSearchParams(next)
                                }}
                            />
                        </aside>

                        {/* Results */}
                        <section className="ml-5 lg:col-span-3">
                            {loading && (
                                <p className="text-center py-16 text-gray-500">
                                    Searching properties...
                                </p>
                            )}

                            {error && (
                                <p className="text-center py-16 text-red-500">
                                    {error}
                                </p>
                            )}

                            {!loading && !error && result && (
                                <PropertySearchContainer
                                    result={result}
                                    onPageChange={(page) => {
                                        setSearchParams({
                                            ...Object.fromEntries(searchParams),
                                            page: page.toString(),
                                        })
                                    }}
                                />
                            )}
                        </section>
                    </div>
                </div>
            </main>

            <Footer />
        </>
    )
}
