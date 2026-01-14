import { useState, useEffect } from 'react';
import { CheckCircle, CreditCard, Banknote, Loader2, Users, ShieldCheck, Star, MapPin } from 'lucide-react';
import Header from '@/shared/components/layout/Header';
import Footer from '@/shared/components/layout/Footer';
import { useHandleBooking } from '../hooks/useHandleBooking.hooks';
// import type { PaymentMethod } from '@/types/payment.enum';
import { useNavigate } from 'react-router-dom';
import type { BookingResponse } from '../types/type';
import { bookingApi } from '../services/booking.api';

// Utility function to format dates
const formatDate = (dateString: string) => {
    const options: Intl.DateTimeFormatOptions = {
        weekday: 'short',
        month: 'short',
        day: 'numeric',
    };
    return new Date(dateString).toLocaleDateString('en-US', options);
};

// Utility function to calculate nights between two dates
const calculateNights = (checkIn: string, checkOut: string) => {
    const diffTime = Math.abs(new Date(checkOut).getTime() - new Date(checkIn).getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
};

interface Property {
    name: string;
    address: { fullAddress: string };
    avgRating: number;
    totalRating: number;
    media: { url: string }[];
}

interface SelectedRoom {
    roomId: string;
    roomType: string;
    ratePlanId: string;
    ratePlanName: string;
    quantity: number;
    price: number;
}

interface StayInfo {
    checkIn: string;
    checkOut: string;
    adults: number;
    children: number;
}

export default function ConfirmBookingPage() {
    const [property, setProperty] = useState<Property | null>(null);
    const [selectedRooms, setSelectedRooms] = useState<SelectedRoom[]>([]);
    const [stayInfo, setStayInfo] = useState<StayInfo | null>(null);
    const navigate = useNavigate();
    const { createBooking } = useHandleBooking();
    const [guest, setGuest] = useState({
        name: '',
        email: '',
        phone: '',
        nationality: ''
    });
    const [paymentMethod, setPaymentMethod] = useState('credit');
    const [specialRequest, setSpecialRequest] = useState('');

    useEffect(() => {
        const storedProperty = localStorage.getItem('propertyDetail');
        const storedRooms = localStorage.getItem('selectedRooms');
        const storedStay = localStorage.getItem('stay-info');

        if (storedProperty) setProperty(JSON.parse(storedProperty));
        if (storedRooms) setSelectedRooms(JSON.parse(storedRooms));
        if (storedStay) setStayInfo(JSON.parse(storedStay));
    }, []);

    const handleConfirmBooking = async () => {
        if (!guest.name || !guest.email || !guest.phone) {
            alert('Please fill in all required guest information.');
            return;
        }

        try {
            const response = await createBooking(
                guest,
                paymentMethod,
                specialRequest,
                stayInfo,
                selectedRooms
            );
            if (response.paymentMethod == 'VN_PAY') {
                await handleVnpayPayment(response);
            }
            else {
                navigate(`/booking-success`);
            }
        } catch (error) {
            console.error('Booking failed:', error);
            alert('There was an error processing your booking. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    const [isLoading, setIsLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        // Simulate loading
        const timer = setTimeout(() => {
            setIsLoading(false);
        }, 800);
        return () => clearTimeout(timer);
    }, []);

    const handleVnpayPayment = async (response: BookingResponse) => {
        try {
            setIsSubmitting(true);
            const params = {
                amount: response.totalAmount,
                vnp_TxnRef: response.bookingReference,
                vnp_OrderInfo: "BOOKINGHOTEL",
                ordertype: "BOOKINGG",
                language: "vn"
            };
            const data = await bookingApi.getVnpayUrl(params);
            if (data.paymentUrl) {
                window.location.href = data.paymentUrl;
            } else {
                throw new Error("Payment URL was not provided by the server.");
            }
        } catch (error) {
            console.error("VNPAY Redirect Error:", error);
            alert("Failed to initiate VNPAY payment. Please try again.");
            setIsSubmitting(false);
        }
    };

    const totalAmount = selectedRooms.reduce(
        (sum, room) => sum + room.price * room.quantity,
        0
    );

    const nights = stayInfo ? calculateNights(stayInfo.checkIn, stayInfo.checkOut) : 0;

    if (isLoading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
                <div className="flex flex-col items-center">
                    <Loader2 className="h-10 w-10 text-blue-600 animate-spin mb-4" />
                    <p className="text-gray-500 font-medium">Preparing your booking...</p>
                </div>
            </div>
        );
    }

    if (!property || !stayInfo) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
                <div className="text-center p-8 bg-white rounded-2xl shadow-xl max-w-md w-full mx-4">
                    <div className="w-16 h-16 bg-red-50 rounded-full flex items-center justify-center mx-auto mb-4">
                        <CheckCircle className="w-8 h-8 text-red-500" />
                    </div>
                    <h2 className="text-xl font-bold text-gray-900 mb-2">Session Expired</h2>
                    <p className="text-gray-500 mb-8">We couldn't find your booking details. Please start your search again.</p>
                    <a href="/" className="inline-block w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded-xl transition duration-200">
                        Back to Home
                    </a>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen flex flex-col bg-gray-50">
            <Header />

            <main className="flex-grow py-8 md:py-12">
                <div className="container mx-auto px-4 max-w-6xl">
                    <div className="mb-8">
                        <h1 className="text-3xl font-bold text-gray-900">Confirm Your Booking</h1>
                        <p className="text-gray-500 mt-2">You're just one step away from your stay</p>
                    </div>

                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                        {/* Left Column - Forms */}
                        <div className="lg:col-span-2 space-y-8">

                            {/* Property Horizontal Card */}
                            <section className="bg-white rounded-2xl shadow-sm border border-gray-100 p-4 flex flex-col sm:flex-row gap-4 items-start">
                                {property.media && property.media.length > 0 && (
                                    <img
                                        src={property.media[0].url}
                                        alt={property.name}
                                        className="w-full sm:w-32 h-32 object-cover rounded-xl"
                                    />
                                )}
                                <div className="flex-1 min-w-0 py-1">
                                    <div className="flex items-start justify-between gap-2">
                                        <div>
                                            <h2 className="text-xl font-bold text-gray-900 truncate">{property.name}</h2>
                                            <p className="text-gray-500 text-sm flex items-center mt-1">
                                                <MapPin className="w-3.5 h-3.5 mr-1" />
                                                {property.address.fullAddress}
                                            </p>
                                        </div>
                                        <div className="hidden sm:flex items-center bg-blue-50 text-blue-700 text-xs font-bold px-2 py-1 rounded-lg">
                                            <Star className="w-3 h-3 mr-1 fill-blue-700" />
                                            {property.avgRating}
                                        </div>
                                    </div>

                                    <div className="mt-4 flex gap-4 text-sm">
                                        <div className="bg-gray-50 px-3 py-2 rounded-lg flex-1">
                                            <p className="text-xs text-gray-500 mb-0.5">Check-in</p>
                                            <p className="font-semibold text-gray-900">{formatDate(stayInfo.checkIn)}</p>
                                        </div>
                                        <div className="bg-gray-50 px-3 py-2 rounded-lg flex-1">
                                            <p className="text-xs text-gray-500 mb-0.5">Check-out</p>
                                            <p className="font-semibold text-gray-900">{formatDate(stayInfo.checkOut)}</p>
                                        </div>
                                    </div>
                                </div>
                            </section>

                            {/* Guest Details */}
                            <section className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                                <div className="p-6 border-b border-gray-100 flex items-center gap-3">
                                    <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600">
                                        <Users className="w-5 h-5" />
                                    </div>
                                    <div>
                                        <h3 className="text-lg font-bold text-gray-900">Guest Details</h3>
                                        <p className="text-sm text-gray-500">Who is checking in?</p>
                                    </div>
                                </div>
                                <div className="p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
                                    <div className="md:col-span-2">
                                        <label className="block text-sm font-medium text-gray-700 mb-2">Full Name</label>
                                        <input
                                            type="text"
                                            value={guest.name}
                                            onChange={(e) => setGuest({ ...guest, name: e.target.value })}
                                            className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                                            placeholder="John Doe"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-2">Email Address</label>
                                        <input
                                            type="email"
                                            value={guest.email}
                                            onChange={(e) => setGuest({ ...guest, email: e.target.value })}
                                            className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                                            placeholder="john@example.com"
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-2">Phone Number</label>
                                        <input
                                            type="tel"
                                            value={guest.phone}
                                            onChange={(e) => setGuest({ ...guest, phone: e.target.value })}
                                            className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                                            placeholder="+1 234 567 890"
                                        />
                                    </div>
                                    <div className="md:col-span-2">
                                        <label className="block text-sm font-medium text-gray-700 mb-2">Nationality (Optional)</label>
                                        <input
                                            type="text"
                                            value={guest.nationality}
                                            onChange={(e) => setGuest({ ...guest, nationality: e.target.value })}
                                            className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                                            placeholder="Your nationality"
                                        />
                                    </div>
                                </div>
                            </section>

                            {/* Payment Method */}
                            <section className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                                <div className="p-6 border-b border-gray-100 flex items-center gap-3">
                                    <div className="w-10 h-10 rounded-full bg-green-100 flex items-center justify-center text-green-600">
                                        <CreditCard className="w-5 h-5" />
                                    </div>
                                    <div>
                                        <h3 className="text-lg font-bold text-gray-900">Payment Method</h3>
                                        <p className="text-sm text-gray-500">Secure payment options</p>
                                    </div>
                                </div>
                                <div className="p-6 grid grid-cols-1 sm:grid-cols-2 gap-4">
                                    <label className={`relative flex flex-col p-4 border-2 rounded-xl cursor-pointer transition-all duration-200 ${paymentMethod === 'VN_PAY' ? 'border-blue-600 bg-blue-50/50' : 'border-gray-100 hover:border-gray-300'}`}>
                                        <input
                                            type="radio"
                                            name="payment"
                                            value="VN_PAY"
                                            checked={paymentMethod === 'VN_PAY'}
                                            onChange={(e) => setPaymentMethod(e.target.value)}
                                            className="absolute top-4 right-4 h-5 w-5 text-blue-600 focus:ring-blue-500 border-gray-300"
                                        />
                                        <div className="w-12 h-8 mb-4">
                                            <img src="https://yt3.googleusercontent.com/JM1m2wng0JQUgSg9ZSEvz7G4Rwo7pYb4QBYip4PAhvGRyf1D_YTbL2DdDjOy0qOXssJPdz2r7Q=s900-c-k-c0x00ffffff-no-rj" alt="VNPAY" className="w-full h-full object-contain" />
                                        </div>
                                        <span className="font-bold text-gray-900">VNPAY Wallet</span>
                                        <span className="text-sm text-gray-500 mt-1">Instant payment via QR or Card</span>
                                    </label>

                                    <label className={`relative flex flex-col p-4 border-2 rounded-xl cursor-pointer transition-all duration-200 ${paymentMethod === 'CASH' ? 'border-blue-600 bg-blue-50/50' : 'border-gray-100 hover:border-gray-300'}`}>
                                        <input
                                            type="radio"
                                            name="payment"
                                            value="CASH"
                                            checked={paymentMethod === 'CASH'}
                                            onChange={(e) => setPaymentMethod(e.target.value)}
                                            className="absolute top-4 right-4 h-5 w-5 text-blue-600 focus:ring-blue-500 border-gray-300"
                                        />
                                        <div className="w-12 h-8 mb-4 flex items-center justify-start text-green-600">
                                            <Banknote className="w-8 h-8" />
                                        </div>
                                        <span className="font-bold text-gray-900">Cash Payment</span>
                                        <span className="text-sm text-gray-500 mt-1">Pay at the property upon check-in</span>
                                    </label>
                                </div>
                            </section>

                            {/* Special Requests */}
                            <section className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                                <div className="p-6 border-b border-gray-100">
                                    <h3 className="font-bold text-gray-900">Special Requests</h3>
                                </div>
                                <div className="p-6">
                                    <textarea
                                        rows={3}
                                        value={specialRequest}
                                        onChange={(e) => setSpecialRequest(e.target.value)}
                                        className="w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none resize-none"
                                        placeholder="Any specific preferences or requests?"
                                    />
                                </div>
                            </section>
                        </div>

                        {/* Right Column - Summary */}
                        <div className="lg:col-span-1">
                            <div className="sticky top-8 space-y-6">
                                {/* Price Summary Card */}
                                <div className="bg-white rounded-2xl shadow-lg shadow-gray-200/50 border border-gray-100 overflow-hidden">
                                    <div className="p-6 border-b border-dashed border-gray-200 bg-gray-50/50">
                                        <h3 className="text-lg font-bold text-gray-900">Price Breakdown</h3>
                                        <p className="text-sm text-gray-500">{nights} {nights === 1 ? 'night' : 'nights'} stay</p>
                                    </div>
                                    <div className="p-6 space-y-4">
                                        {selectedRooms.map((room) => (
                                            <div key={`${room.roomId}-${room.ratePlanId}`} className="flex justify-between text-sm">
                                                <div className="text-gray-600">
                                                    <span className="font-medium text-gray-900">{room.roomType}</span>
                                                    <div className="text-xs text-gray-400">x{room.quantity} rooms</div>
                                                </div>
                                                <div className="font-medium text-gray-900">
                                                    ${(room.price * room.quantity * nights).toFixed(2)}
                                                </div>
                                            </div>
                                        ))}

                                        <div className="pt-4 border-t border-dashed border-gray-200 space-y-2">
                                            <div className="flex justify-between text-sm text-gray-500">
                                                <span>Subtotal</span>
                                                <span>${(totalAmount * nights).toFixed(2)}</span>
                                            </div>
                                            <div className="flex justify-between text-sm text-gray-500">
                                                <span>Taxes & Fees (12%)</span>
                                                <span>${(totalAmount * 0.12 * nights).toFixed(2)}</span>
                                            </div>
                                        </div>

                                        <div className="pt-4 border-t border-gray-200">
                                            <div className="flex justify-between items-end">
                                                <span className="font-bold text-xl text-gray-900">Total</span>
                                                <span className="font-bold text-2xl text-blue-600">
                                                    ${(totalAmount * 1.12 * nights).toFixed(2)}
                                                </span>
                                            </div>
                                            <p className="text-xs text-gray-400 mt-1 text-right">Includes all taxes and fees</p>
                                        </div>

                                        <button
                                            onClick={handleConfirmBooking}
                                            disabled={isSubmitting}
                                            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-4 rounded-xl shadow-lg shadow-blue-500/30 transition-all hover:scale-[1.02] active:scale-[0.98] disabled:opacity-70 disabled:cursor-not-allowed flex items-center justify-center gap-2 mt-4"
                                        >
                                            {isSubmitting ? (
                                                <>
                                                    <Loader2 className="animate-spin h-5 w-5" />
                                                    Processing...
                                                </>
                                            ) : (
                                                <>
                                                    <ShieldCheck className="w-5 h-5" />
                                                    Confirm Booking
                                                </>
                                            )}
                                        </button>
                                        <p className="text-xs text-center text-gray-400">
                                            Secure booking powered by Booking Platform
                                        </p>
                                    </div>
                                </div>

                                {/* Trust Badges */}
                                <div className="bg-blue-50 rounded-xl p-5 border border-blue-100">
                                    <h4 className="font-bold text-blue-900 mb-3 text-sm">Free Cancellation</h4>
                                    <p className="text-xs text-blue-700 leading-relaxed">
                                        Cancel for free up to 24 hours before check-in. Plans change, we understand.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
            <Footer />
        </div>
    );
}
