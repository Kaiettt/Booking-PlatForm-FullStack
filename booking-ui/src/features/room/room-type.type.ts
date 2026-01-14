import type { Media } from "../media/media.type";
import type { RatePlan } from "../rate-plan/rate-plan.type";

export interface RoomType {
    id: number;
    name: string;
    maxAdults: number;
    maxChildren: number;
    maxGuest: number;
    sizeM2: number;
    bedType: string;
    viewType: string;
    smokingAllowed: boolean;
    totalRooms: number;
    roomAmenities: string[];
    roomFacilities: string[];
    media: Media[];
    ratePlans: RatePlan[];
}
