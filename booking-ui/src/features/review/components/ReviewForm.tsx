import { useState } from 'react'
import { Star } from 'lucide-react'
import { useAuthStore } from '@/stores/auth.store'
import { Button } from '@/components/ui/button'
import { reviewApi } from '../services/review.api'

interface Props {
    propertyId: number
    onCreated?: () => void
}

export default function ReviewForm({ propertyId, onCreated }: Props) {
    const user = useAuthStore((s) => s.user)
    const isAuthenticated = useAuthStore((s) => s.isAuthenticated)

    const [rating, setRating] = useState<number>(5)
    const [comment, setComment] = useState<string>('')
    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setError(null)

        if (!isAuthenticated || !user) {
            setError('Please sign in to write a review.')
            return
        }

        if (!comment.trim()) {
            setError('Comment is required.')
            return
        }

        try {
            setSubmitting(true)
            await reviewApi.createReview({
                userId: user.id,
                propertyId,
                rating,
                comment: comment.trim(),
            })

            onCreated?.()
            setComment('')
            setRating(5)
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to create review.')
        } finally {
            setSubmitting(false)
        }
    }

    if (!isAuthenticated) {
        return (
            <div className="bg-blue-50 border border-blue-100 rounded-2xl p-5">
                <p className="text-sm text-blue-900 font-medium">Sign in to write a review.</p>
            </div>
        )
    }

    return (
        <form onSubmit={handleSubmit} className="space-y-4 bg-white rounded-2xl border border-gray-100 p-6">
            <div className="flex items-center justify-between gap-4">
                <h3 className="text-lg font-bold text-gray-900">Write a review</h3>
                <div className="flex items-center gap-1">
                    {[1, 2, 3, 4, 5].map((s) => (
                        <button
                            key={s}
                            type="button"
                            aria-label={`Rate ${s} star`}
                            onClick={() => setRating(s)}
                            className="p-0.5"
                        >
                            <Star
                                className={`w-5 h-5 ${
                                    s <= rating ? 'fill-yellow-400 text-yellow-400' : 'fill-gray-200 text-gray-200'
                                }`}
                            />
                        </button>
                    ))}
                </div>
            </div>

            <div className="space-y-2">
                <label className="text-sm font-medium text-gray-700">Comment</label>
                <textarea
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    maxLength={500}
                    rows={4}
                    className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none resize-none"
                    placeholder="Share your experience..."
                    required
                />
                <div className="text-xs text-gray-400">{comment.length}/500</div>
            </div>

            {error && <div className="text-sm text-red-600">{error}</div>}

            <Button
                type="submit"
                disabled={submitting}
                className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-xl h-11"
            >
                {submitting ? 'Submitting...' : 'Submit review'}
            </Button>
        </form>
    )
}

