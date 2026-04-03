import { post } from '@/services/apiClient'
import type { Review } from '../review.type'

export interface ReviewCreateRequest {
    userId: number
    propertyId: number
    rating: number
    comment: string
}

export const reviewApi = {
    async createReview(payload: ReviewCreateRequest): Promise<Review> {
        return post<Review>('/reviews', payload)
    },
}

