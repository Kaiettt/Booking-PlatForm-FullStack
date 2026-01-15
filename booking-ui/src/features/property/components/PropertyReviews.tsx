import { Star, ThumbsUp } from 'lucide-react'
import { motion } from 'framer-motion'
import type { Review } from '@/features/review/review.type'

interface Props {
    reviews: Review[]
    avgRating: number | null
    totalRating: number | null
}

const StarRating = ({ rating }: { rating: number }) => (
    <div className="flex gap-0.5">
        {[1, 2, 3, 4, 5].map((star) => (
            <Star
                key={star}
                className={`w-3.5 h-3.5 ${star <= rating ? 'fill-yellow-400 text-yellow-400' : 'fill-gray-100 text-gray-200'}`}
            />
        ))}
    </div>
)

const Avatar = ({ name }: { name: string }) => {
    const initials = name
        .split(' ')
        .map(n => n[0])
        .slice(0, 2)
        .join('')
        .toUpperCase()

    const colors = [
        'bg-blue-100 text-blue-600',
        'bg-green-100 text-green-600',
        'bg-purple-100 text-purple-600',
        'bg-rose-100 text-rose-600',
        'bg-orange-100 text-orange-600',
    ]
    const colorIndex = name.length % colors.length

    return (
        <div className={`w-10 h-10 rounded-full flex items-center justify-center font-bold text-sm ${colors[colorIndex]}`}>
            {initials}
        </div>
    )
}

export default function PropertyReviews({ reviews, avgRating, totalRating }: Props) {
    if (!reviews || reviews.length === 0) return null

    return (
        <section className="space-y-8 py-8 border-t border-gray-100" id="reviews">
            {/* Header */}
            <div className="flex items-center gap-6">
                <div className="bg-blue-600 text-white px-4 py-3 rounded-2xl text-center shadow-lg shadow-blue-600/20">
                    <span className="block text-3xl font-bold">{avgRating?.toFixed(1)}</span>
                    <span className="text-xs font-medium opacity-90">/ 5</span>
                </div>
                <div>
                    <h2 className="text-2xl font-bold text-gray-900">Guest Reviews</h2>
                    <p className="text-gray-500">
                        Based on {totalRating} verified experiences
                    </p>
                </div>
            </div>

            {/* Reviews Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {reviews.map((review, index) => (
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        transition={{ duration: 0.4, delay: index * 0.1 }}
                        key={review.id}
                        className="p-6 bg-gray-50 rounded-2xl hover:bg-white hover:shadow-lg transition-all duration-300 border border-transparent hover:border-gray-100"
                    >
                        {/* User Header */}
                        <div className="flex justify-between items-start mb-4">
                            <div className="flex items-center gap-3">
                                <Avatar name={review.userName} />
                                <div>
                                    <h3 className="font-bold text-gray-900 text-sm">{review.userName}</h3>
                                    <div className="text-xs text-gray-400">
                                        {review.createdAt && new Date(review.createdAt).toLocaleDateString(undefined, {
                                            year: 'numeric',
                                            month: 'short',
                                            day: 'numeric'
                                        })}
                                    </div>
                                </div>
                            </div>
                            <StarRating rating={review.rating} />
                        </div>

                        {/* Comment */}
                        <p className="text-gray-600 text-sm leading-relaxed">
                            "{review.comment}"
                        </p>

                        {/* Helpful (Mock) */}
                        <div className="mt-4 flex items-center gap-2 text-xs text-gray-400 cursor-pointer hover:text-blue-600 transition-colors w-fit">
                            <ThumbsUp className="w-3 h-3" />
                            <span>Helpful</span>
                        </div>
                    </motion.div>
                ))}
            </div>
        </section>
    )
}
