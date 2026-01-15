import { useRef } from 'react';
import { motion } from 'framer-motion';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import dalatImg from '@/assets/images/Dalat.webp';
import halongImg from '@/assets/images/halong.webp';
import hanoiImg from '@/assets/images/hanoi.jpg';
import nhatrangImg from '@/assets/images/nhatrang.jpeg';
import phuquocImg from '@/assets/images/phuquoc.webp';
import sapaImg from '@/assets/images/sapa.webp';
import vungtauImg from '@/assets/images/vungtau.jpg';

const destinations = [
    {
        name: 'Ho Chi Minh City',
        image: 'https://images.unsplash.com/photo-1583417319070-4a69db38a482?auto=format&fit=crop&w=800&q=80',
        properties: 1432,
    },
    {
        name: 'Hanoi',
        image: hanoiImg,
        properties: 982,
    },
    {
        name: 'Da Nang',
        image: 'https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?auto=format&fit=crop&w=800&q=80',
        properties: 875,
    },
    {
        name: 'Vung Tau',
        image: vungtauImg,
        properties: 543,
    },
    {
        name: 'Da Lat',
        image: dalatImg,
        properties: 621,
    },
    {
        name: 'Nha Trang',
        image: nhatrangImg,
        properties: 752,
    },
    {
        name: 'Hoi An',
        image: 'https://images.unsplash.com/photo-1557750255-c76072a7aad1?auto=format&fit=crop&w=800&q=80',
        properties: 428,
    },
    {
        name: 'Phu Quoc',
        image: phuquocImg,
        properties: 512,
    },
    {
        name: 'Ha Long',
        image: halongImg,
        properties: 345,
    },
    {
        name: 'Sapa',
        image: sapaImg,
        properties: 231,
    },
];

export default function ExploreVietnam() {
    const scrollContainerRef = useRef<HTMLDivElement>(null);

    const scroll = (direction: 'left' | 'right') => {
        if (scrollContainerRef.current) {
            const scrollAmount = 300; // Approx card width + gap
            const newScrollLeft = scrollContainerRef.current.scrollLeft + (direction === 'right' ? scrollAmount : -scrollAmount);
            scrollContainerRef.current.scrollTo({
                left: newScrollLeft,
                behavior: 'smooth',
            });
        }
    };

    return (
        <section className="max-w-7xl mx-auto px-4 md:px-6 mt-16 mb-16 relative group/section">
            <div className="flex justify-between items-end mb-8">
                <div>
                    <h2 className="text-3xl font-bold text-gray-900">Explore Vietnam</h2>
                    <p className="text-gray-500 mt-2">These popular destinations have a lot to offer</p>
                </div>

                {/* Navigation Arrows */}
                <div className="hidden md:flex gap-2">
                    <button
                        onClick={() => scroll('left')}
                        className="p-2 rounded-full border border-gray-200 hover:bg-gray-50 hover:border-gray-300 transition-colors shadow-sm"
                    >
                        <ChevronLeft className="w-5 h-5 text-gray-600" />
                    </button>
                    <button
                        onClick={() => scroll('right')}
                        className="p-2 rounded-full border border-gray-200 hover:bg-gray-50 hover:border-gray-300 transition-colors shadow-sm"
                    >
                        <ChevronRight className="w-5 h-5 text-gray-600" />
                    </button>
                </div>
            </div>

            {/* Carousel Container */}
            <div
                ref={scrollContainerRef}
                className="flex gap-6 overflow-x-auto pb-8 snap-x snap-mandatory scrollbar-hide -mx-4 px-4 md:mx-0 md:px-0"
                style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
            >
                {destinations.map((city, index) => (
                    <motion.div
                        key={city.name}
                        initial={{ opacity: 0, scale: 0.9 }}
                        whileInView={{ opacity: 1, scale: 1 }}
                        transition={{ duration: 0.5, delay: index * 0.05 }}
                        className="flex-none w-[200px] md:w-[240px] snap-start cursor-pointer group"
                    >
                        <div className="relative aspect-[4/3] rounded-xl overflow-hidden mb-3">
                            <img
                                src={city.image}
                                alt={city.name}
                                className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
                            />
                            <div className="absolute inset-0 bg-black/10 group-hover:bg-black/0 transition-colors" />
                        </div>
                        <h3 className="font-bold text-lg text-gray-900 group-hover:text-blue-600 transition-colors">
                            {city.name}
                        </h3>
                        <p className="text-sm text-gray-500">
                            {city.properties.toLocaleString()} properties
                        </p>
                    </motion.div>
                ))}
            </div>
        </section>
    );
}
