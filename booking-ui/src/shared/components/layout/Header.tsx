import { useAuthStore } from '@/stores/auth.store'
import { useState } from 'react'
import { Link } from 'react-router-dom'

export default function Header() {
    const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
    const [userMenuOpen, setUserMenuOpen] = useState(false)

    const user = useAuthStore((state) => state.user)
    const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
    const clearUser = useAuthStore((state) => state.clearUser)

    return (
        <header className="sticky top-0 z-50 bg-white shadow-sm border-b">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex h-16 items-center justify-between">
                    {/* Logo */}
                    <Link to="/" className="text-2xl font-bold text-blue-600">
                        Booking<span className="text-yellow-400">App</span>
                    </Link>

                    {/* Desktop Nav */}
                    <nav className="hidden md:flex space-x-8">
                        <Link to="/" className="text-gray-700 hover:text-blue-600 font-medium">
                            Home
                        </Link>
                        <Link to="/properties" className="text-gray-700 hover:text-blue-600 font-medium">
                            Properties
                        </Link>
                        <Link to="/about" className="text-gray-700 hover:text-blue-600 font-medium">
                            About
                        </Link>
                        <Link to="/contact" className="text-gray-700 hover:text-blue-600 font-medium">
                            Contact
                        </Link>
                    </nav>

                    {/* Right Section */}
                    <div className="hidden md:flex items-center space-x-4 relative">
                        {!isAuthenticated ? (
                            <>
                                <Link
                                    to="/login"
                                    className="text-gray-700 hover:text-blue-600 font-medium"
                                >
                                    Sign In
                                </Link>
                                <Link
                                    to="/signup"
                                    className="bg-blue-600 text-white px-4 py-2 rounded-lg font-medium hover:bg-blue-700 transition"
                                >
                                    Sign Up
                                </Link>
                            </>
                        ) : (
                            <>
                                {/* User menu */}
                                <button
                                    onClick={() => setUserMenuOpen(!userMenuOpen)}
                                    className="flex items-center space-x-2 hover:bg-gray-100 px-3 py-2 rounded-lg"
                                >
                                    <div className="w-8 h-8 rounded-full bg-blue-600 text-white flex items-center justify-center font-semibold">
                                        {user?.fullName.charAt(0)}
                                    </div>
                                    <span className="font-medium text-gray-700">
                                        {user?.fullName}
                                    </span>
                                </button>

                                {userMenuOpen && (
                                    <div className="absolute right-0 top-12 w-48 bg-white border rounded-lg shadow-md py-1">
                                        <Link
                                            to="/profile"
                                            className="block px-4 py-2 text-gray-700 hover:bg-gray-100"
                                        >
                                            Profile
                                        </Link>
                                        <button
                                            onClick={clearUser}
                                            className="w-full text-left px-4 py-2 text-red-600 hover:bg-gray-100"
                                        >
                                            Logout
                                        </button>
                                    </div>
                                )}
                            </>
                        )}
                    </div>

                    {/* Mobile menu button */}
                    <button
                        className="md:hidden text-gray-700"
                        onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                    >
                        ☰
                    </button>
                </div>
            </div>

            {/* Mobile Menu */}
            {mobileMenuOpen && (
                <div className="md:hidden bg-white border-t">
                    <div className="px-4 py-4 space-y-3">
                        <Link to="/" className="block text-gray-700 font-medium">
                            Home
                        </Link>
                        <Link to="/properties" className="block text-gray-700 font-medium">
                            Properties
                        </Link>
                        <Link to="/about" className="block text-gray-700 font-medium">
                            About
                        </Link>
                        <Link to="/contact" className="block text-gray-700 font-medium">
                            Contact
                        </Link>

                        <div className="pt-4 border-t">
                            {!isAuthenticated ? (
                                <>
                                    <Link to="/login" className="block text-gray-700 font-medium">
                                        Sign In
                                    </Link>
                                    <Link
                                        to="/signup"
                                        className="block mt-2 bg-blue-600 text-white px-4 py-2 rounded-lg text-center"
                                    >
                                        Sign Up
                                    </Link>
                                </>
                            ) : (
                                <>
                                    <div className="font-medium text-gray-800">
                                        {user?.fullName}
                                    </div>
                                    <Link
                                        to="/profile"
                                        className="block mt-2 text-gray-700"
                                    >
                                        Profile
                                    </Link>
                                    <button
                                        onClick={clearUser}
                                        className="block mt-2 text-red-600"
                                    >
                                        Logout
                                    </button>
                                </>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </header>
    )
}
