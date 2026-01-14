import { Fragment } from 'react';
import HeroSection from '../components/HeroSection';
import SearchBar from '../components/SearchBar';
import ExploreVietnam from '../components/ExploreVietnam';
import PopularDestinations from '../components/PopularDestinations';
import Header from '@/shared/components/layout/Header';
import Footer from '@/shared/components/layout/Footer';

export default function HomePage() {
    return (
        <Fragment>
            <Header />
            <main className="min-h-screen bg-gray-50 pb-20">
                <HeroSection />
                <SearchBar />
                <ExploreVietnam />
                <PopularDestinations />
            </main>
            <Footer />
        </Fragment>
    );
}
