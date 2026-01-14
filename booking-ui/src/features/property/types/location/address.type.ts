import type { GeoLocation } from "./geo-location.type"

export interface Address {
    streetAddress: string
    ward: string
    district: string
    city: string
    state: string
    postalCode: string
    country: string
    geo: GeoLocation
    fullAddress: string
}