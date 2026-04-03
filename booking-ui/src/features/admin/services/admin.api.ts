import { get, apiClient } from '@/services/apiClient'
import type { UserRole } from '@/features/user/types/user.enum'
import type { Address } from '@/features/property/types/location/address.type'
import type { Media } from '@/features/media/media.type'
import type { Amenity } from '@/features/property/types/property/property-amenity.type'
import type { Facility } from '@/features/property/types/property/property-facility.type'

export interface AdminUserResponse {
    id: number
    fullName: string
    email: string
    phoneNumber: string | null
    role: UserRole
    status: string
    isEmailVerified: boolean
    isPhoneVerified: boolean
    provider: string | null
}

export interface HostInfo {
    id: number
    fullName: string
    email: string
    phoneNumber: string | null
}

export interface AdminPropertyApproval {
    id: number
    name: string
    type: string
    description: string
    status: string
    avgRating: number | null
    totalRating: number | null
    address: Address
    host: HostInfo
    media: Media[]
    amenities: Amenity[]
    facilities: Facility[]
}

export const adminApi = {
    async getUsers(): Promise<AdminUserResponse[]> {
        return get<AdminUserResponse[]>('/users')
    },

    async updateUserRole(userId: number, role: UserRole): Promise<AdminUserResponse> {
        return apiClient.put(`/users/${userId}/role`, { role })
    },

    async getPendingProperties(): Promise<AdminPropertyApproval[]> {
        return get<AdminPropertyApproval[]>('/properties')
    },

    async approveProperty(propertyId: number): Promise<number> {
        return apiClient.put(`/properties/${propertyId}/approve`, null)
    },

    async rejectProperty(propertyId: number): Promise<number> {
        return apiClient.put(`/properties/${propertyId}/reject`, null)
    },
}

