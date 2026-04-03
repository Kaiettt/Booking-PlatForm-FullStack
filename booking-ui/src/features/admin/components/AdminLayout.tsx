import { Outlet, NavLink } from 'react-router-dom'
import Header from '@/shared/components/layout/Header'
import Footer from '@/shared/components/layout/Footer'

export default function AdminLayout() {
    return (
        <div className="min-h-screen flex flex-col bg-gray-50">
            <Header />

            <div className="flex flex-1 pt-20">
                <aside className="w-64 bg-white border-r border-gray-100">
                    <div className="p-4">
                        <h2 className="text-sm font-bold text-gray-800 uppercase tracking-wide">
                            Admin Panel
                        </h2>
                        <nav className="mt-4 space-y-2">
                            <NavLink
                                to="/admin/users"
                                className={({ isActive }) =>
                                    `block px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                                        isActive ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'
                                    }`
                                }
                            >
                                Manage Users
                            </NavLink>
                            <NavLink
                                to="/admin/approvals"
                                className={({ isActive }) =>
                                    `block px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                                        isActive ? 'bg-blue-50 text-blue-700' : 'text-gray-700 hover:bg-gray-50'
                                    }`
                                }
                            >
                                Property Approvals
                            </NavLink>
                        </nav>
                    </div>
                </aside>

                <main className="flex-1 p-6">
                    <Outlet />
                </main>
            </div>

            <Footer />
        </div>
    )
}

