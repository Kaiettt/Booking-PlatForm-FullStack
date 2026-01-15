import { useState } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import { ChevronLeft, ChevronRight } from 'lucide-react'

interface Props {
    media: { id: number; url: string }[]
}

export default function PropertyGallery({ media }: Props) {
    const [selectedIndex, setSelectedIndex] = useState(0)

    if (!media || media.length === 0) return null

    const handleNext = () => {
        setSelectedIndex((prev) => (prev + 1) % media.length)
    }

    const handlePrev = () => {
        setSelectedIndex((prev) => (prev - 1 + media.length) % media.length)
    }

    return (
        <section className="space-y-4">
            {/* Main Image Stage */}
            <div className="relative h-[400px] md:h-[500px] w-full bg-gray-100 rounded-3xl overflow-hidden group">
                <AnimatePresence mode="wait">
                    <motion.img
                        key={media[selectedIndex].id}
                        src={media[selectedIndex].url}
                        alt={`Property view ${selectedIndex + 1}`}
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        transition={{ duration: 0.3 }}
                        className="w-full h-full object-cover"
                    />
                </AnimatePresence>

                {/* Navigation Arrows (visible on hover) */}
                <div className="absolute inset-0 flex items-center justify-between p-4 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                    <button
                        onClick={handlePrev}
                        className="p-3 rounded-full bg-white/90 backdrop-blur-sm shadow-lg hover:scale-110 transition-transform text-gray-800"
                    >
                        <ChevronLeft className="w-6 h-6" />
                    </button>
                    <button
                        onClick={handleNext}
                        className="p-3 rounded-full bg-white/90 backdrop-blur-sm shadow-lg hover:scale-110 transition-transform text-gray-800"
                    >
                        <ChevronRight className="w-6 h-6" />
                    </button>
                </div>

                {/* Image Counter Badge */}
                <div className="absolute bottom-4 right-4 bg-black/50 backdrop-blur-md text-white px-4 py-2 rounded-full text-sm font-medium">
                    {selectedIndex + 1} / {media.length}
                </div>
            </div>

            {/* Thumbnails Strip */}
            <div className="relative">
                <div className="flex gap-4 overflow-x-auto pb-4 snap-x scrollbar-hide">
                    {media.map((item, index) => (
                        <button
                            key={item.id}
                            onClick={() => setSelectedIndex(index)}
                            className={`flex-none relative h-24 w-36 rounded-xl overflow-hidden snap-start transition-all duration-300 ${index === selectedIndex
                                ? 'ring-2 ring-blue-600 ring-offset-2 opacity-100 scale-105'
                                : 'opacity-70 hover:opacity-100'
                                }`}
                        >
                            <img
                                src={item.url}
                                alt={`Thumbnail ${index + 1}`}
                                className="w-full h-full object-cover"
                            />
                            {/* Active Overlay */}
                            {index === selectedIndex && (
                                <div className="absolute inset-0 bg-blue-600/10 mix-blend-multiply" />
                            )}
                        </button>
                    ))}
                </div>
            </div>
        </section>
    )
}
