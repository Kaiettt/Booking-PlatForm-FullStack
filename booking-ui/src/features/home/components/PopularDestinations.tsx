import { motion } from 'framer-motion';

const destinations = [
    {
        name: 'Ho Chi Minh City',
        image: 'https://images.unsplash.com/photo-1583417319070-4a69db38a482?auto=format&fit=crop&w=800&q=80',
        description: 'Vibrant energy & history'
    },
    {
        name: 'Da Nang',
        image: 'https://besthuecitytour.com/wp-content/uploads/2020/09/Things-To-Do-In-Da-Nang-Best-Hue-City-Tour-Travel-1-1536x1023.jpg',
        description: 'Coastal paradise & bridges'
    },
    {
        name: 'Hoi An',
        image: 'https://images.unsplash.com/photo-1557750255-c76072a7aad1?auto=format&fit=crop&w=800&q=80',
        description: 'Ancient town & lanterns'
    },
]

export default function PopularDestinations() {
    return (
        <section className="max-w-7xl mx-auto px-4 md:px-6 mt-20 mb-20">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.6 }}
                className="mb-10 text-center md:text-left"
            >
                <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-2">
                    Popular destinations in Vietnam
                </h2>
                <p className="text-gray-500 text-lg">
                    Most loved places by travelers from around the globe
                </p>
            </motion.div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                {destinations.map((item, index) => (
                    <motion.div
                        key={item.name}
                        initial={{ opacity: 0, y: 30 }}
                        whileInView={{ opacity: 1, y: 0 }}
                        viewport={{ once: true }}
                        transition={{ duration: 0.6, delay: index * 0.1 }}
                        className="group relative h-[400px] rounded-3xl overflow-hidden cursor-pointer shadow-lg hover:shadow-2xl transition-all duration-300"
                    >
                        {/* Image */}
                        <div className="absolute inset-0 w-full h-full overflow-hidden rounded-3xl">
                            <img
                                src={item.image}
                                alt={item.name}
                                className="h-full w-full object-cover transition-transform duration-700 group-hover:scale-110"
                            />
                        </div>

                        {/* Gradient overlay */}
                        <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/20 to-transparent opacity-80 group-hover:opacity-90 transition-opacity" />

                        {/* Text */}
                        <div className="absolute bottom-0 left-0 w-full p-8 transform translate-y-2 group-hover:translate-y-0 transition-transform duration-300">
                            <h3 className="text-white text-3xl font-bold mb-2 drop-shadow-md">
                                {item.name}
                            </h3>
                            <div className="overflow-hidden">
                                <p className="text-gray-200 text-base font-medium flex items-center gap-2 transform translate-y-8 group-hover:translate-y-0 transition-transform duration-300 opacity-0 group-hover:opacity-100">
                                    <span className="w-8 h-[2px] bg-white block"></span>
                                    {item.description}
                                </p>
                            </div>
                        </div>
                    </motion.div>
                ))}
            </div>
        </section>
    )
}
