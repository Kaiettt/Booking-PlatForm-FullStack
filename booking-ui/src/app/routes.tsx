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
            }
        ]
    }
];

export const router = createBrowserRouter(routes);
