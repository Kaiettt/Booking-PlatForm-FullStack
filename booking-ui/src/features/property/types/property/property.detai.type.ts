import type { Media } from "@/features/media/media.type";
import type { Address } from "../location/address.type";
import type { PropertyStatus, PropertyType } from "./property.enum";
import type { Amenity } from "./property-amenity.type";
import type { Facility } from "./property-facility.type";
import type { RoomType } from "@/features/room/room-type.type";
import type { Review } from "@/features/review/review.type";

export interface PropertyDetail {
    id: number;
    name: string;
    type: PropertyType;
    description: string;
    address: Address;
    status: PropertyStatus;
    avgRating: number;
    totalRating: number;
    minPrice: number;
    media: Media[];
    amenities: Amenity[];
    facilities: Facility[];
    roomTypes: RoomType[];
    reviews: Review[];
}
