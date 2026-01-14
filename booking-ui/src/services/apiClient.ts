import axios, {
    AxiosError,
    type AxiosInstance,
    type AxiosResponse,
    type InternalAxiosRequestConfig,
} from 'axios'
import { tokenService } from './token.service'
import { authService } from '@/features/auth/services/auth.service'

const apiClient: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true, 
})


apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const token = tokenService.getAccessToken()
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => Promise.reject(error)
)

/* ================= RESPONSE ================= */

let isRefreshing = false
let failedQueue: {
    resolve: (token: string) => void
    reject: (error: AxiosError) => void
}[] = []

const processQueue = (error: AxiosError | null, token: string | null) => {
    failedQueue.forEach((p) => {
        error ? p.reject(error) : p.resolve(token!)
    })
    failedQueue = []
}

apiClient.interceptors.response.use(
    (response: AxiosResponse) => response.data,

    async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & {
            _retry?: boolean
        }

        if (error.response?.status === 401 && !originalRequest._retry) {
            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject })
                }).then((token) => {
                    originalRequest.headers.Authorization = `Bearer ${token}`
                    return apiClient(originalRequest)
                })
            }

            originalRequest._retry = true
            isRefreshing = true

            try {
                const { accessToken } = await authService.refresh()
                tokenService.setAccessToken(accessToken)

                processQueue(null, accessToken)

                originalRequest.headers.Authorization = `Bearer ${accessToken}`
                return apiClient(originalRequest)
            } catch (err) {
                processQueue(err as AxiosError, null)
                tokenService.clearTokens()
                return Promise.reject(err)
            } finally {
                isRefreshing = false
            }
        }

        return Promise.reject(error)
    }
)
const get = <T>(url: string, config?: any): Promise<T> =>
    apiClient.get(url, config) as Promise<T>

const post = <T>(url: string, data?: any, config?: any): Promise<T> =>
    apiClient.post(url, data, config) as Promise<T>

export { apiClient, get, post }

