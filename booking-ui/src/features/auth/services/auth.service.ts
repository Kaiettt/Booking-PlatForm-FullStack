import { get, post } from '@/services/apiClient'
import { tokenService } from '@/services/token.service'
import type { LoginRequest, LoginResponse } from '../types/login.type'
import type { SignupRequest, SignupResponse } from '../types/signup.type'
import { useAuthStore } from '@/stores/auth.store'

export const authService = {

    async googleSignIn(googleIdToken: string): Promise<void> {
        try {
            const response = await post<LoginResponse>('/auth/google', { token: googleIdToken });

            // Save the app access token & user info
            tokenService.setAccessToken(response.accessToken);
            useAuthStore.getState().setUser(response.user);
        } catch (error) {
            console.error('Google sign-in failed:', error);
            throw error;
        }
    },
    async login(loginRequest: LoginRequest): Promise<LoginResponse> {
        const response = await post<LoginResponse>('/auth/login', loginRequest)
        tokenService.setAccessToken(response.accessToken)
        useAuthStore.getState().setUser(response.user)
        return response
    },

    async refresh(): Promise<LoginResponse> {
        const response = await get<LoginResponse>('/auth/refresh')
        useAuthStore.getState().setUser(response.user)
        return response
    },

    async signup(signupRequest: SignupRequest): Promise<SignupResponse> {
        const response = await post<SignupResponse>('/auth/signup', signupRequest);
        return response;
    },

    async logout(): Promise<void> {
        tokenService.clearTokens()
        useAuthStore.getState().clearUser()
    },
}
