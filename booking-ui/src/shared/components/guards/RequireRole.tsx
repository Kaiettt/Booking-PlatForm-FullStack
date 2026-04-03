import { Navigate } from 'react-router-dom'
import { useAuthStore } from '@/stores/auth.store'

export default function RequireRole({
    role,
    children,
}: {
    role: 'HOST' | 'ADMIN' | string
    children: React.ReactNode
}) {
    const isAuthenticated = useAuthStore((s) => s.isAuthenticated)
    const user = useAuthStore((s) => s.user)

    if (!isAuthenticated || !user) {
        return <Navigate to="/login" replace />
    }

    if (user.role !== role) {
        return <Navigate to="/" replace />
    }

    return <>{children}</>
}

