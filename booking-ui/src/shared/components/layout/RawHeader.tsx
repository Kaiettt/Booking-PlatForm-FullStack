import { useState } from 'react';
import { Link } from 'react-router-dom';

export default function RawHeader() {
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

    return (
        <header className="bg-blue-600 text-white shadow-md">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16 items-center">
                    {/* Logo */}
                    <div className="flex-shrink-0 flex items-center">
                        <Link to="/" className="text-2xl font-bold text-white">
                            Booking<span className="text-yellow-400">App</span>
                        </Link>
                    </div>

                    {/* Desktop Navigation */}
                    <nav className="hidden md:flex space-x-6">
                        <Link to="/" className="hover:text-yellow-400 font-medium">Home</Link>
                        <Link to="/properties" className="hover:text-yellow-400 font-medium">Properties</Link>
                        <Link to="/about" className="hover:text-yellow-400 font-medium">About Us</Link>
                        <Link to="/contact" className="hover:text-yellow-400 font-medium">Contact</Link>
                    </nav>

                    {/* Mobile menu button */}
                    <div className="md:hidden">
                        <button
                            type="button"
                            className="text-white hover:text-yellow-400 focus:outline-none"
                            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                        >
                            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d={mobileMenuOpen ? "M6 18L18 6M6 6l12 12" : "M4 6h16M4 12h16M4 18h16"} />
                            </svg>
                        </button>
                    </div>
                </div>
            </div>

            {/* Mobile menu */}
            {mobileMenuOpen && (
                <div className="md:hidden bg-blue-600">
                    <div className="px-2 pt-2 pb-3 space-y-1">
                        <Link to="/" className="block px-3 py-2 rounded text-base font-medium hover:bg-blue-500">Home</Link>
                        <Link to="/properties" className="block px-3 py-2 rounded text-base font-medium hover:bg-blue-500">Properties</Link>
                        <Link to="/about" className="block px-3 py-2 rounded text-base font-medium hover:bg-blue-500">About Us</Link>
                        <Link to="/contact" className="block px-3 py-2 rounded text-base font-medium hover:bg-blue-500">Contact</Link>


                    </div>
                </div>
            )}
        </header>
    );
}
