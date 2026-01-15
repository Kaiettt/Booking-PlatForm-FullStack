import { Search, Calendar, MapPin, Users } from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';

export default function SearchBar() {
    const navigate = useNavigate();
    const [destination, setDestination] = useState('');
    const [checkIn, setCheckIn] = useState('');
    const [checkOut, setCheckOut] = useState('');
    const [adults, setAdults] = useState(1);
    const [children, setChildren] = useState(0);
    const [showGuestSelector, setShowGuestSelector] = useState(false);

    const today = new Date().toISOString().split('T')[0];
    const totalGuests = adults + children;

    const handleGuestClick = () => {
        setShowGuestSelector(!showGuestSelector);
    };

    const handleSearch = () => {
        if (!destination) return

        navigate({
            pathname: '/search',
            search: new URLSearchParams({
                city: destination,
                checkingDate: checkIn || new Date().toISOString().split('T')[0],
                checkoutDate: checkOut || new Date(Date.now() + 86400000).toISOString().split('T')[0],
                adults: adults.toString(),
                children: children.toString(),
                page: '0',
                size: '10',
            }).toString(),
        })
    }

    return (
        <div className="-mt-24 z-20 relative max-w-6xl mx-auto px-4">
            <motion.div
                initial={{ y: 40, opacity: 0 }}
                animate={{ y: 0, opacity: 1 }}
                transition={{ duration: 0.6, delay: 0.4 }}
                className="bg-white/90 backdrop-blur-xl rounded-3xl shadow-2xl p-4 border border-white/40"
            >
                <div className="grid grid-cols-1 lg:grid-cols-12 gap-2 items-center">
                    {/* Destination */}
                    <div className="relative lg:col-span-4 group">
                        <div className="absolute inset-y-0 left-4 flex items-center pointer-events-none">
                            <MapPin className="h-6 w-6 text-blue-500 group-focus-within:text-blue-600 transition-colors" />
                        </div>
                        <input
                            type="text"
                            value={destination}
                            onChange={(e) => setDestination(e.target.value)}
                            placeholder="Where are you going?"
                            className="block w-full pl-14 pr-4 py-4 bg-gray-50/50 hover:bg-white border-transparent focus:border-blue-500 focus:bg-white focus:ring-4 focus:ring-blue-100 rounded-2xl transition-all duration-200 outline-none text-gray-900 placeholder-gray-400 font-medium"
                        />
                    </div>

                    {/* Check-in */}
                    <div className="relative lg:col-span-2 group">
                        <div className="absolute inset-y-0 left-4 flex items-center pointer-events-none">
                            <Calendar className="h-6 w-6 text-blue-500 group-focus-within:text-blue-600 transition-colors" />
                        </div>
                        <input
                            type="date"
                            value={checkIn}
                            min={today}
                            onChange={(e) => setCheckIn(e.target.value)}
                            className="block w-full pl-14 pr-4 py-4 bg-gray-50/50 hover:bg-white border-transparent focus:border-blue-500 focus:bg-white focus:ring-4 focus:ring-blue-100 rounded-2xl transition-all duration-200 outline-none text-gray-900 font-medium cursor-pointer"
                        />
                    </div>

                    {/* Check-out */}
                    <div className="relative lg:col-span-2 group">
                        <div className="absolute inset-y-0 left-4 flex items-center pointer-events-none">
                            <Calendar className="h-6 w-6 text-blue-500 group-focus-within:text-blue-600 transition-colors" />
                        </div>
                        <input
                            type="date"
                            value={checkOut}
                            min={checkIn || today}
                            onChange={(e) => setCheckOut(e.target.value)}
                            className="block w-full pl-14 pr-4 py-4 bg-gray-50/50 hover:bg-white border-transparent focus:border-blue-500 focus:bg-white focus:ring-4 focus:ring-blue-100 rounded-2xl transition-all duration-200 outline-none text-gray-900 font-medium cursor-pointer"
                        />
                    </div>

                    {/* Guests */}
                    <div className="relative lg:col-span-2">
                        <button
                            className="flex items-center justify-between w-full pl-14 pr-4 py-4 bg-gray-50/50 hover:bg-white border-2 border-transparent focus:border-blue-500 focus:bg-white focus:ring-4 focus:ring-blue-100 rounded-2xl transition-all duration-200 outline-none text-gray-900 font-medium relative group"
                            onClick={handleGuestClick}
                        >
                            <div className="absolute inset-y-0 left-4 flex items-center pointer-events-none">
                                <Users className="h-6 w-6 text-blue-500 group-focus-within:text-blue-600 transition-colors" />
                            </div>
                            <span>
                                {totalGuests} {totalGuests === 1 ? 'Guest' : 'Guests'}
                            </span>
                            <svg className={`h-5 w-5 text-gray-400 transition-transform duration-200 ${showGuestSelector ? 'rotate-180' : ''}`} fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                            </svg>
                        </button>

                        {/* Guest Selector Dropdown */}
                        <AnimatePresence>
                            {showGuestSelector && (
                                <motion.div
                                    initial={{ opacity: 0, y: 10, scale: 0.95 }}
                                    animate={{ opacity: 1, y: 0, scale: 1 }}
                                    exit={{ opacity: 0, y: 10, scale: 0.95 }}
                                    transition={{ duration: 0.2 }}
                                    className="absolute z-30 mt-3 w-full min-w-[300px] right-0 bg-white rounded-2xl shadow-xl border border-gray-100 p-6"
                                >
                                    <div className="flex justify-between items-center mb-6">
                                        <div>
                                            <div className="font-bold text-gray-900 text-lg">Adults</div>
                                            <div className="text-sm text-gray-500">Ages 13+</div>
                                        </div>
                                        <div className="flex items-center space-x-4">
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    if (adults > 1) setAdults(adults - 1);
                                                }}
                                                className="w-10 h-10 rounded-full border border-gray-200 flex items-center justify-center hover:bg-gray-50 hover:border-blue-500 transition-colors disabled:opacity-50"
                                                disabled={adults <= 1}
                                            >
                                                -
                                            </button>
                                            <span className="w-4 text-center font-bold text-lg">{adults}</span>
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    setAdults(adults + 1);
                                                }}
                                                className="w-10 h-10 rounded-full border border-gray-200 flex items-center justify-center hover:bg-gray-50 hover:border-blue-500 transition-colors"
                                            >
                                                +
                                            </button>
                                        </div>
                                    </div>

                                    <div className="flex justify-between items-center">
                                        <div>
                                            <div className="font-bold text-gray-900 text-lg">Children</div>
                                            <div className="text-sm text-gray-500">Ages 2-12</div>
                                        </div>
                                        <div className="flex items-center space-x-4">
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    if (children > 0) setChildren(children - 1);
                                                }}
                                                className="w-10 h-10 rounded-full border border-gray-200 flex items-center justify-center hover:bg-gray-50 hover:border-blue-500 transition-colors disabled:opacity-50"
                                                disabled={children <= 0}
                                            >
                                                -
                                            </button>
                                            <span className="w-4 text-center font-bold text-lg">{children}</span>
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    setChildren(children + 1);
                                                }}
                                                className="w-10 h-10 rounded-full border border-gray-200 flex items-center justify-center hover:bg-gray-50 hover:border-blue-500 transition-colors"
                                            >
                                                +
                                            </button>
                                        </div>
                                    </div>
                                </motion.div>
                            )}
                        </AnimatePresence>
                    </div>

                    {/* Search Button */}
                    <div className="lg:col-span-2">
                        <button
                            className="w-full h-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-4 px-8 rounded-2xl transition-all duration-300 transform hover:scale-[1.02] active:scale-[0.98] shadow-lg hover:shadow-blue-500/30 flex items-center justify-center space-x-2"
                            onClick={handleSearch}
                        >
                            <Search className="h-6 w-6" />
                            <span className="text-lg">Search</span>
                        </button>
                    </div>
                </div>
            </motion.div>
        </div>
    );
}