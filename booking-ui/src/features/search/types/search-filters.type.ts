export interface PropertyFilters {
    adults: number
    children: number
    minPrice?: number
    maxPrice?: number
    propertyTypes: string[]
    minRating?: number
    propertyAmenities: number[]
    propertyFacilities: number[]
    roomFacilities: number[]
    checkingDate?: string
    checkoutDate?: string
    page?: number
    size?: number
    city?: string
}
