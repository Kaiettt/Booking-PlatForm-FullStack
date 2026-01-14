import type { Address } from "../location/address.type"
import type { Amenity } from "./property-amenity.type"
import type { Facility } from "./property-facility.type"
import type { PropertyStatus, PropertyType } from "./property.enum"

export interface Property {
    id: number
    name: string
    type: PropertyType
    description: string
    address: Address
    status: PropertyStatus
    mediaUrl: string
    minPrice: number
    avgRating: number | null
    totalRating: number | null
    amenities: Amenity[]
    facilities: Facility[]
}


