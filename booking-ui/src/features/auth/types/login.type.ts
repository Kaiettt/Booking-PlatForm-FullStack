import type { UserRole, UserStatus } from '@/features/user/types/user.enum'

export interface LoginRequest {
    username: string
    password: string
}

export interface UserLogin {
    id: number
    fullName: string
    email: string
    phoneNumber: string | null
    role: UserRole
    status: UserStatus
    isEmailVerified: boolean
    isPhoneVerified: boolean
    provider: string | null
}

export interface LoginResponse {
    accessToken: string
    user: UserLogin
}
