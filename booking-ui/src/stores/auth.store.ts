import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { LoginResponse } from '@/features/auth/types/login.type'

interface AuthState {
    user: LoginResponse['user'] | null
    isAuthenticated: boolean

    setUser: (user: LoginResponse['user']) => void
    clearUser: () => void
}

export const useAuthStore = create<AuthState>()(
    persist(
        (set) => ({
            user: null,
            isAuthenticated: false,

            setUser: (user) =>
                set({
                    user,
                    isAuthenticated: true,
                }),

            clearUser: () =>
                set({
                    user: null,
                    isAuthenticated: false,
                }),
        }),
        {
            name: 'auth-storage', // localStorage key
        }
    )
)
