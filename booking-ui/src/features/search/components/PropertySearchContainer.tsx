import PropertyCard from '@/features/property/components/PropertyCard'
import type { PropertySearchResponse } from '@/features/home/types/property-search.type'

interface Props {
    result: PropertySearchResponse
    onPageChange: (currentPage: number) => void
}

export default function PropertySearchContainer({
    result,
    onPageChange,
}: Props) {
    const { data, currentPage, totalPages } = result

    return (
        <div className="flex flex-col gap-10">
            {/* Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {data.map((property) => (
                    <PropertyCard
                        key={property.id}
                        property={property}
                    />
                ))}
            </div>

            {/* Pagination */}
            {totalPages > 1 && (
                <div className="flex justify-center items-center gap-2">
                    <button
                        disabled={currentPage === 1}
                        onClick={() => onPageChange(currentPage - 1)}
                        className="px-4 py-2 border rounded-lg disabled:opacity-50"
                    >
                        Previous
                    </button>

                    {Array.from({ length: totalPages }).map((_, i) => {
                        const p = i + 1
                        return (
                            <button
                                key={p}
                                onClick={() => onPageChange(p)}
                                className={`px-4 py-2 rounded-lg border ${p === currentPage
                                    ? 'bg-blue-600 text-white'
                                    : 'hover:bg-gray-100'
                                    }`}
                            >
                                {p}
                            </button>
                        )
                    })}

                    <button
                        disabled={currentPage === totalPages}
                        onClick={() => onPageChange(currentPage + 1)}
                        className="px-4 py-2 border rounded-lg disabled:opacity-50"
                    >
                        Next
                    </button>
                </div>
            )}
        </div>
    )
}
