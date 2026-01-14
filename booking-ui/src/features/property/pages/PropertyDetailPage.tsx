import Header from "@/shared/components/layout/Header"
import Footer from "@/shared/components/layout/Footer"

import { useFetchProperty } from "../hooks/useFetchProperty.hook"

import PropertyHero from "../components/PropertyHero"
import PropertyGallery from "../components/PropertyGallery"
import PropertyOverview from "../components/PropertyOverview"
import PropertyAmenities from "../components/PropertyAmenities"
import PropertyFacilities from "../components/PropertyFacilities"
import PropertyAvailability from "../components/PropertyAvailability"
import PropertyRooms from "../components/PropertyRooms"
import PropertyReviews from "../components/PropertyReviews"
import Breadcrumb from "../../../shared/components/layout/Breadcrumb"
import { useState } from "react"
import { MapPin } from "lucide-react"

export default function PropertyDetailPage() {
    const { result, loading, error } = useFetchProperty()
    const [availability, setAvailability] = useState({
        checkIn: '',
        checkOut: '',
        adults: 2,
        children: 0
    });
    if (loading) {
        return (
            <>
                <Header />
                <main className="container mx-auto px-4 py-10">
                    <p className="text-gray-500">Loading property details...</p>
                </main>
                <Footer />
            </>
        )
    }

    if (error || !result) {
        return (
            <>
                <Header />
                <main className="container mx-auto px-4 py-10">
                    <p className="text-red-500">Failed to load property details.</p>
                </main>
                <Footer />
            </>
        )
    }

    return (
        <>
            <Header />

            <main className="container mx-auto px-4 py-6 space-y-8">
                {/* BREADCRUMB */}
                <Breadcrumb
                    items={[
                        { name: 'Home', path: '/' },
                        { name: result.type, path: `/properties?type=${result.type.toLowerCase()}` },
                        { name: result.address.country, path: `/properties?country=${encodeURIComponent(result.address.country)}` },
                        { name: result.address.city, path: `/properties?city=${encodeURIComponent(result.address.city)}` },
                        { name: result.name, path: '#', isLast: true },
                    ]}
                    className="mb-6"
                />

                {/* HERO */}
                <PropertyHero
                    name={result.name}
                    type={result.type}
                    avgRating={result.avgRating}
                    totalRating={result.totalRating}
                    address={result.address}
                />

                {/* GALLERY & MAP SECTION */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* LEFT: GALLERY (Takes up 2 columns) */}
                    <div className="lg:col-span-2">
                        <PropertyGallery media={result.media} />
                    </div>

                    {/* RIGHT: MAP & RATINGS (Takes up 1 column) */}
                    <div className="space-y-6">
                        {/* Rating Card */}
                        {result.avgRating && (
                            <div className="bg-white p-6 rounded-2xl border border-gray-100 shadow-sm flex items-center justify-between">
                                <div>
                                    <div className="flex items-center gap-2 mb-1">
                                        <div className="bg-blue-600 text-white w-12 h-12 flex items-center justify-center rounded-xl text-xl font-bold shadow-lg shadow-blue-200">
                                            {result.avgRating.toFixed(1)}
                                        </div>
                                        <div>
                                            <p className="font-bold text-gray-900 text-lg">
                                                {result.avgRating >= 4.5 ? 'Excellent' : 'Very Good'}
                                            </p>
                                            <p className="text-sm text-gray-500 underline decoration-dotted cursor-pointer hover:text-blue-600">
                                                {result.totalRating} reviews
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div className="text-right hidden sm:block">
                                    <button className="text-sm font-semibold text-blue-600 hover:bg-blue-50 px-3 py-2 rounded-lg transition-colors">
                                        Read all reviews
                                    </button>
                                </div>
                            </div>
                        )}

                        {/* Map Card */}
                        <div className="bg-white p-2 rounded-2xl border border-gray-100 shadow-sm overflow-hidden h-64 md:h-auto md:aspect-square relative group">
                            <iframe
                                width="100%"
                                height="100%"
                                frameBorder="0"
                                style={{ border: 0, borderRadius: '12px', minHeight: '200px' }}
                                src={`https://maps.google.com/maps?q=${encodeURIComponent(result.address.fullAddress)}&t=&z=15&ie=UTF8&iwloc=&output=embed`}
                                allowFullScreen
                                className="w-full h-full grayscale-[20%] group-hover:grayscale-0 transition-all duration-500"
                            ></iframe>

                            <a
                                href={`https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(result.address.fullAddress)}`}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="absolute bottom-4 left-1/2 -translate-x-1/2 bg-white/90 backdrop-blur-md px-4 py-2 rounded-full shadow-lg text-sm font-semibold text-gray-800 hover:scale-105 transition-transform flex items-center gap-2"
                            >
                                <MapPin className="w-4 h-4 text-red-500" />
                                Show on map
                            </a>
                        </div>
                    </div>
                </div>

                {/* MAIN CONTENT */}
                <div className="w-full">
                    <div className="w-full space-y-10">
                        <PropertyOverview
                            description={result.description}
                            type={result.type}
                            address={result.address}
                        />

                        <PropertyAmenities amenities={result.amenities} />

                        <PropertyFacilities facilities={result.facilities} />

                        <PropertyAvailability
                            availability={availability}
                            setAvailability={setAvailability}
                        />

                        <PropertyRooms roomTypes={result.roomTypes} availability={availability} />

                        <PropertyReviews
                            reviews={result.reviews}
                            avgRating={result.avgRating}
                            totalRating={result.totalRating}
                        />
                    </div>
                </div>
            </main>

            <Footer />
        </>
    )
}
