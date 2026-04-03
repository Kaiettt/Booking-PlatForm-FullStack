import { get, post } from '@/services/apiClient'

export interface WishlistRequest {
    userId: number
    propertyId: number
}

export interface WishlistProperty {
    id: number
    wishListId: number
    propertyId: number
}

export interface WishlistResponse {
    id: number
    userId: number
    wishlistProperties: WishlistProperty[]
}

export const wishlistApi = {
    async addToWishlist(payload: WishlistRequest): Promise<WishlistResponse> {
        return post<WishlistResponse>('/wishlists', payload)
    },

    async fetchWishlistByUser(userId: number): Promise<WishlistResponse> {
        return get<WishlistResponse>(`/wishlists/${userId}`)
    },
}

