import { motion } from 'framer-motion';

export default function HeroSection() {
    return (
        <section
            className="relative h-[85vh] min-h-[600px] flex items-center justify-center bg-cover bg-center bg-no-repeat bg-fixed"
            style={{
                backgroundImage: 'url("https://images.unsplash.com/photo-1557683316-973673baf926?auto=format&fit=crop&w=1920&q=80")',
            }}
        >
            {/* Gradient Overlay */}
            <div className="absolute inset-0 bg-gradient-to-b from-black/60 via-black/40 to-transparent z-0"></div>

            {/* Content */}
            <div className="relative z-10 text-center px-4 max-w-5xl mx-auto text-white">
                <motion.h1
                    initial={{ opacity: 0, y: 30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.8, ease: "easeOut" }}
                    className="text-5xl md:text-7xl font-extrabold tracking-tight leading-tight mb-6 drop-shadow-lg"
                >
                    Find your perfect <span className="text-blue-400">stay</span>
                </motion.h1>

                <motion.p
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.8, delay: 0.2, ease: "easeOut" }}
                    className="text-xl md:text-2xl font-light text-gray-100 max-w-3xl mx-auto drop-shadow-md"
                >
                    Discover and book amazing accommodations around the world.
                    From cozy apartments to luxurious resorts.
                </motion.p>
            </div>
        </section>
    );
}
