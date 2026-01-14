export interface Review {
    id: number;
    userName: string;
    propertyId: number;
    rating: number;
    comment: string;
    createdAt?: string;
}
