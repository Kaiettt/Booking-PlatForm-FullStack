export interface SearchParams {
    // Location & date
    city: string
    checkingDate: string
    checkoutDate: string

    // Guests
    adults: number
    children: number

    // Price filter
    minPrice?: number
    maxPrice?: number

    // Property filter
    propertyTypes?: string[]   // HOTEL, APARTMENT, ...

    // Rating
    minRating?: number

    // Amenities / Facilities
    amenities?: string[]       // Breakfast included, Pool, ...
    facilities?: string[]
    roomFacilities?: string[]

    // Pagination
    page: number
    size: number
}
