import type { PropertyFilters } from '@/features/search/types/search-filters.type'

interface BuildSearchParamsInput {
    filters: PropertyFilters
    amenitiesList: { id: number; name: string }[]
    facilitiesList: { id: number; name: string }[]
    roomFacilitiesList: { id: number; name: string }[]
}

export function buildSearchParams({
    filters,
}: BuildSearchParamsInput): string {
    const params = new URLSearchParams()

    params.set('adults', String(filters.adults))
    params.set('children', String(filters.children))

    if (filters.minPrice !== undefined) params.set('minPrice', String(filters.minPrice))
    if (filters.maxPrice !== undefined) params.set('maxPrice', String(filters.maxPrice))
    if (filters.minRating !== undefined) params.set('minRating', String(filters.minRating))

    filters.propertyTypes.forEach(t => params.append('propertyType', t))

    filters.propertyAmenities.forEach(id => {
        params.append('propertyAmenities', String(id))
    })

    filters.propertyFacilities.forEach(id => {
        params.append('propertyFacilities', String(id))
    })

    filters.roomFacilities.forEach(id => {
        params.append('roomFacilities', String(id))
    })

    return params.toString()
}
