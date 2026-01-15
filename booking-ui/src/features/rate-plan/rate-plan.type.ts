import type { PrepaymentType } from "../property/types/property/property.enum";

export interface Perk {
    id: number;
    code: string;
    name: string;
}

export interface RatePlan {
    id: number;
    name: string;
    price: number;
    prepaymentType: PrepaymentType;
    perks: Perk[];
}
