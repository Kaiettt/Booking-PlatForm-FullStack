import { Button } from "@/components/ui/button";
import { Calendar, Clock, CreditCard, MapPin, User, Check, Copy, Download, Share2 } from "lucide-react";
import { useNavigate } from "react-router-dom";
import Header from "@/shared/components/layout/Header";
import Footer from "@/shared/components/layout/Footer";
import { useFetchBookingResponse } from "../hooks/useFetchBookingResponse.hooks";
import { format } from "date-fns";
import { motion } from "framer-motion";
import { useState } from "react";

export default function BookingSuccessPage() {
    const { bookingData, isLoading, error } = useFetchBookingResponse();
    const navigate = useNavigate();
    const [copied, setCopied] = useState(false);

    const copyToClipboard = () => {
        if (bookingData?.bookingReference) {
            navigator.clipboard.writeText(bookingData.bookingReference);
            setCopied(true);
            setTimeout(() => setCopied(false), 2000);
        }
    };

    if (isLoading) {
        return (
            <div className="min-h-screen flex flex-col bg-gray-50">
                <Header />
                <main className="flex-1 flex items-center justify-center">
                    <div className="flex flex-col items-center">
                        <div className="w-12 h-12 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mb-4"></div>
                        <p className="text-gray-500 font-medium">Retrieving your booking...</p>
                    </div>
                </main>
                <Footer />
            </div>
        );
    }

    if (error || !bookingData) {
        return (
            <div className="min-h-screen flex flex-col bg-gray-50">
                <Header />
                <main className="flex-1 flex items-center justify-center p-4">
                    <div className="text-center max-w-md w-full bg-white p-8 rounded-2xl shadow-xl">
                        <div className="w-20 h-20 bg-red-50 rounded-full flex items-center justify-center mx-auto mb-6">
                            <span className="text-4xl">⚠️</span>
                        </div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-2">Booking Not Found</h2>
                        <p className="text-gray-500 mb-8">{error || "We couldn't retrieve your booking details."}</p>
                        <Button onClick={() => navigate("/")} className="w-full bg-gray-900 hover:bg-gray-800 text-white rounded-xl py-6">
                            Return Home
                        </Button>
                    </div>
                </main>
                <Footer />
            </div>
        );
    }

    const { bookingReference, guest, checkIn, checkOut, totalAmount, bookingItems } = bookingData;
    const checkInDate = new Date(checkIn);
    const checkOutDate = new Date(checkOut);
    const nights = Math.ceil((checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24));

    return (
        <div className="min-h-screen flex flex-col bg-gray-50">
            <Header />
            <main className="flex-1 py-12">
                <div className="container max-w-5xl mx-auto px-4">

                    {/* Hero Section */}
                    <div className="text-center mb-12">
                        <motion.div
                            initial={{ scale: 0, opacity: 0 }}
                            animate={{ scale: 1, opacity: 1 }}
                            transition={{ type: "spring", duration: 0.6, bounce: 0.5 }}
                            className="w-24 h-24 bg-green-500 rounded-full flex items-center justify-center mx-auto mb-6 shadow-lg shadow-green-500/30"
                        >
                            <Check className="w-12 h-12 text-white stroke-[3]" />
                        </motion.div>

                        <motion.div
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ delay: 0.2, duration: 0.4 }}
                        >
                            <h1 className="text-4xl font-bold text-gray-900 mb-2">Booking Confirmed!</h1>
                            <p className="text-xl text-gray-600 mb-8">
                                Pack your bags, you're going to <span className="text-green-600 font-semibold">your destination</span>!
                            </p>

                            {/* Booking Reference Badge */}
                            <div className="inline-flex items-center gap-3 bg-white px-4 py-2 rounded-xl shadow-sm border border-gray-200">
                                <span className="text-sm text-gray-500 uppercase tracking-wider font-semibold">Reference</span>
                                <span className="font-mono text-xl font-bold text-gray-900 tracking-wide">{bookingReference}</span>
                                <button
                                    onClick={copyToClipboard}
                                    className="p-1.5 hover:bg-gray-100 rounded-lg transition-colors text-gray-400 hover:text-gray-600"
                                    title="Copy reference"
                                >
                                    {copied ? <Check className="w-4 h-4 text-green-500" /> : <Copy className="w-4 h-4" />}
                                </button>
                            </div>
                            <p className="text-sm text-gray-400 mt-3">
                                Confirmation sent to <span className="font-medium text-gray-600">{guest.email}</span>
                            </p>
                        </motion.div>
                    </div>

                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 items-start">
                        {/* Left Column - Details */}
                        <motion.div
                            initial={{ x: -20, opacity: 0 }}
                            animate={{ x: 0, opacity: 1 }}
                            transition={{ delay: 0.4, duration: 0.5 }}
                            className="lg:col-span-2 space-y-6"
                        >
                            {/* Stay Details Card */}
                            <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                                <div className="p-6 border-b border-gray-100 flex items-center gap-3">
                                    <div className="w-10 h-10 rounded-full bg-blue-50 flex items-center justify-center text-blue-600">
                                        <MapPin className="w-5 h-5" />
                                    </div>
                                    <h3 className="text-lg font-bold text-gray-900">Your Stay</h3>
                                </div>
                                <div className="p-6">
                                    {bookingItems.map((item, index) => (
                                        <div key={index} className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-4 rounded-xl bg-gray-50 border border-gray-100 mb-4 last:mb-0 hover:border-blue-100 transition-colors">
                                            <div>
                                                <h4 className="font-bold text-gray-900 text-lg">{item.roomTypeName}</h4>
                                                <div className="flex flex-wrap gap-4 mt-2 text-sm text-gray-600">
                                                    <div className="flex items-center gap-1.5">
                                                        <User className="w-4 h-4 text-gray-400" />
                                                        <span>{item.quantity} {item.quantity > 1 ? "Rooms" : "Room"}</span>
                                                    </div>
                                                    <div className="flex items-center gap-1.5">
                                                        <CreditCard className="w-4 h-4 text-gray-400" />
                                                        <span>${item.amount.toFixed(2)} / night</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="text-right">
                                                <span className="block text-2xl font-bold text-blue-600">${item.amount.toFixed(2)}</span>
                                                <span className="text-xs text-gray-400">Total price</span>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>

                            {/* Date & Time Card */}
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 flex flex-col items-center text-center">
                                    <div className="w-12 h-12 rounded-full bg-amber-50 flex items-center justify-center text-amber-600 mb-3">
                                        <Calendar className="w-6 h-6" />
                                    </div>
                                    <p className="text-sm font-medium text-gray-400 uppercase tracking-wider mb-1">Check-in</p>
                                    <p className="text-lg font-bold text-gray-900">{format(checkInDate, "EEE, MMM d, yyyy")}</p>
                                    <p className="text-sm text-gray-500 mt-1">From 2:00 PM</p>
                                </div>

                                <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 flex flex-col items-center text-center">
                                    <div className="w-12 h-12 rounded-full bg-purple-50 flex items-center justify-center text-purple-600 mb-3">
                                        <Clock className="w-6 h-6" />
                                    </div>
                                    <p className="text-sm font-medium text-gray-400 uppercase tracking-wider mb-1">Check-out</p>
                                    <p className="text-lg font-bold text-gray-900">{format(checkOutDate, "EEE, MMM d, yyyy")}</p>
                                    <p className="text-sm text-gray-500 mt-1">Until 12:00 PM</p>
                                </div>
                            </div>
                        </motion.div>

                        {/* Right Column - Receipt & Actions */}
                        <motion.div
                            initial={{ x: 20, opacity: 0 }}
                            animate={{ x: 0, opacity: 1 }}
                            transition={{ delay: 0.6, duration: 0.5 }}
                            className="space-y-6"
                        >
                            {/* Receipt Card */}
                            <div className="bg-white rounded-2xl shadow-lg shadow-gray-200/50 overflow-hidden relative">
                                {/* Jagged Edge SVG Pattern at Bottom (Optional simulation or actual SVG) */}
                                <div className="bg-gray-900 p-6 text-white text-center">
                                    <h3 className="font-mono text-lg tracking-widest uppercase opacity-80">Receipt</h3>
                                    <div className="mt-2 text-3xl font-bold font-mono">${totalAmount.toFixed(2)}</div>
                                    <p className="text-xs text-gray-400 mt-1 font-mono">Total Paid</p>
                                </div>
                                <div className="p-6 space-y-4">
                                    <div className="space-y-3 text-sm">
                                        {bookingItems.map((item, index) => (
                                            <div key={index} className="flex justify-between text-gray-600">
                                                <span>{item.quantity}x {item.roomTypeName}</span>
                                                <span className="font-medium text-gray-900">${item.amount.toFixed(2)}</span>
                                            </div>
                                        ))}
                                        {bookingItems.some(i => i) && (
                                            <div className="flex justify-between text-gray-600">
                                                <span>Nights</span>
                                                <span className="font-medium text-gray-900">{nights}</span>
                                            </div>
                                        )}
                                    </div>
                                    <div className="border-t border-dashed border-gray-200 my-4"></div>
                                    <div className="flex justify-between items-center text-sm font-medium">
                                        <span className="text-gray-900">Payment Status</span>
                                        <span className="text-green-600 bg-green-50 px-2 py-1 rounded">Paid</span>
                                    </div>
                                </div>

                                {/* Decorative "Holes" or Jagged effect can be added here */}
                            </div>

                            {/* Action Buttons */}
                            <div className="grid grid-cols-2 gap-3">
                                <Button variant="outline" className="h-auto py-4 flex flex-col gap-2 rounded-xl bg-white border-gray-200 hover:bg-gray-50 hover:text-blue-600 transition-all">
                                    <Download className="w-5 h-5" />
                                    <span className="text-xs font-medium">Download PDF</span>
                                </Button>
                                <Button variant="outline" className="h-auto py-4 flex flex-col gap-2 rounded-xl bg-white border-gray-200 hover:bg-gray-50 hover:text-blue-600 transition-all">
                                    <Share2 className="w-5 h-5" />
                                    <span className="text-xs font-medium">Share</span>
                                </Button>
                            </div>

                            <div className="bg-blue-50 rounded-2xl p-6 border border-blue-100 text-center">
                                <h3 className="font-bold text-blue-900 mb-2">Need Assistance?</h3>
                                <p className="text-sm text-blue-700 mb-4">
                                    Our support team is available 24/7 to help you with your booking.
                                </p>
                                <Button asChild className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-xl h-11">
                                    <a href="/">Go to Home</a>
                                </Button>
                            </div>
                        </motion.div>
                    </div>
                </div>
            </main>
            <Footer />
        </div>
    );
}