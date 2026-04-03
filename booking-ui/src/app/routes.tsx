import { createBrowserRouter } from 'react-router-dom';
import type { RouteObject } from 'react-router-dom';
import App from './App';
import HomePage from '@/features/home/pages/HomePage';
import SearchResultsPage from '@/features/search/pages/SearchResultsPage';
import PropertyDetailPage from '@/features/property/pages/PropertyDetailPage';
import LoginPage from '@/features/auth/pages/LoginPage';
import SignupPage from '@/features/auth/pages/SignupPage';
import ConfirmBookingPage from '@/features/booking/pages/ConfirmBooking';
import BookingSuccessPage from '@/features/booking/pages/BookingSuccess';
import PaymentCallback from '@/features/booking/pages/PaymentCallback';
import WishlistPage from '@/features/wishlist/pages/WishlistPage';
import HostLayout from '@/features/host/components/HostLayout';
import HostBookingsPage from '@/features/host/pages/HostBookingsPage';
import CreatePropertyPage from '@/features/host/pages/CreatePropertyPage';
import ManageRoomTypesPage from '@/features/host/pages/ManageRoomTypesPage';
import RequireRole from '@/shared/components/guards/RequireRole';
import AdminLayout from '@/features/admin/components/AdminLayout';
import ManageUsersPage from '@/features/admin/pages/ManageUsersPage';
import ApprovalsPage from '@/features/admin/pages/ApprovalsPage';
import { Navigate } from 'react-router-dom';

const routes: RouteObject[] = [
    {
        path: '/',
        element: <App />,
        children: [
            {
                index: true,
                element: <HomePage />
            },
            {
                path: 'search',
                element: <SearchResultsPage />
            },
            {
                path: 'search/filter',
                element: <SearchResultsPage />
            },
            {
                path: 'property/:name',
                element: <PropertyDetailPage />
            }, {
                path: 'login',
                element: <LoginPage />
            },
            {
                path: 'signup',
                element: <SignupPage />
            },
            {
                path: 'confirm-booking',
                element: <ConfirmBookingPage />
            },
            {
                path: 'booking-success',
                element: <BookingSuccessPage />
            },
            {
                path: 'payments/call-back',
                element: <PaymentCallback />
            },
            {
                path: 'wishlist',
                element: <WishlistPage />
            },
            {
                path: 'host',
                element: (
                    <RequireRole role="HOST">
                        <HostLayout />
                    </RequireRole>
                ),
                children: [
                    {
                        index: true,
                        element: <Navigate to="bookings" replace />,
                    },
                    {
                        path: 'bookings',
                        element: <HostBookingsPage />,
                    },
                    {
                        path: 'properties/create',
                        element: <CreatePropertyPage />,
                    },
                    {
                        path: 'room-types',
                        element: <ManageRoomTypesPage />,
                    },
                ],
            },
            {
                path: 'admin',
                element: (
                    <RequireRole role="ADMIN">
                        <AdminLayout />
                    </RequireRole>
                ),
                children: [
                    {
                        index: true,
                        element: <Navigate to="users" replace />,
                    },
                    {
                        path: 'users',
                        element: <ManageUsersPage />,
                    },
                    {
                        path: 'approvals',
                        element: <ApprovalsPage />,
                    },
                ],
            }
        ]
    }
];

export const router = createBrowserRouter(routes);
