import { Heart } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useAuthStore } from '@/stores/auth.store'
import { wishlistApi } from '../services/wishlist.api'

export default function WishlistButton({ propertyId }: { propertyId: number }) {
    const user = useAuthStore((s) => s.user)
    const isAuthenticated = useAuthStore((s) => s.isAuthenticated)

    const [saved, setSaved] = useState(false)
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        const init = async () => {
            if (!isAuthenticated || !user) return
            try {
                const data = await wishlistApi.fetchWishlistByUser(user.id)
                setSaved(data.wishlistProperties.some((p) => p.propertyId === propertyId))
            } catch {
                // If wishlist fetch fails, keep button as non-saved state.
                setSaved(false)
            }
        }

        init()
    }, [isAuthenticated, user, propertyId])

    const handleAdd = async () => {
        if (!isAuthenticated || !user) return
        if (saved || loading) return

        try {
            setLoading(true)
            await wishlistApi.addToWishlist({ userId: user.id, propertyId })
            setSaved(true)
        } catch (err) {
            // If already exists, backend may throw - treat as saved.
            setSaved(true)
        } finally {
            setLoading(false)
        }
    }

    return (
        <button
            type="button"
            onClick={handleAdd}
            disabled={!isAuthenticated || loading}
            className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full border border-gray-100 bg-white hover:bg-gray-50 transition disabled:opacity-50 disabled:cursor-not-allowed"
        >
            <Heart className={`w-4 h-4 ${saved ? 'fill-red-500 text-red-500' : 'text-gray-400'}`} />
            <span className="text-xs font-semibold text-gray-700">{saved ? 'Saved' : 'Wishlist'}</span>
        </button>
    )
}

