import type { PropertyFilters } from '@/features/search/types/search-filters.type'
import { Star, SlidersHorizontal, RotateCcw, ChevronDown, Check, Minus, Plus } from 'lucide-react'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion, AnimatePresence } from 'framer-motion'
import {
    propertyTypes as defaultPropertyTypes,
    amenitiesList as defaultAmenitiesList,
    facilitiesList as defaultFacilitiesList,
    roomFacilitiesList as defaultRoomFacilitiesList,
} from '@/features/search/data/filter-options'

interface ListItem {
    id: number
    name: string
}

interface Props {
    filters: PropertyFilters
    onChange: (filters: PropertyFilters) => void
    propertyTypes?: string[]
    amenitiesList?: ListItem[]
    facilitiesList?: ListItem[]
    roomFacilitiesList?: ListItem[]
}

function Section({
    title,
    children,
    defaultOpen = true,
}: {
    title: string
    children: React.ReactNode
    defaultOpen?: boolean
}) {
    const [open, setOpen] = useState(defaultOpen)

    return (
        <div className="border-b border-gray-100 last:border-0 py-4">
            <button
                type="button"
                onClick={() => setOpen(!open)}
                className="flex w-full items-center justify-between group"
            >
                <h3 className="font-semibold text-gray-900 group-hover:text-blue-600 transition-colors">{title}</h3>
                <ChevronDown
                    className={`h-5 w-5 text-gray-400 transition-transform duration-300 ${open ? 'rotate-180' : ''}`}
                />
            </button>
            <AnimatePresence>
                {open && (
                    <motion.div
                        initial={{ height: 0, opacity: 0 }}
                        animate={{ height: 'auto', opacity: 1 }}
                        exit={{ height: 0, opacity: 0 }}
                        transition={{ duration: 0.3 }}
                        className="overflow-hidden"
                    >
                        <div className="pt-4">{children}</div>
                    </motion.div>
                )}
            </AnimatePresence>
        </div>
    )
}

function CheckboxItem({
    label,
    checked,
    onChange
}: {
    label: string,
    checked: boolean,
    onChange: () => void
}) {
    return (
        <label className="flex items-start gap-3 cursor-pointer group select-none">
            <div className="relative mt-0.5">
                <input
                    type="checkbox"
                    checked={checked}
                    onChange={onChange}
                    className="peer sr-only"
                />
                <div className={`w-5 h-5 rounded border-2 flex items-center justify-center transition-all duration-200 
                    ${checked
                        ? 'bg-blue-600 border-blue-600'
                        : 'border-gray-300 bg-white group-hover:border-blue-400'
                    }`}
                >
                    {checked && <Check className="w-3.5 h-3.5 text-white" strokeWidth={3} />}
                </div>
            </div>
            <span className={`text-sm transition-colors ${checked ? 'text-gray-900 font-medium' : 'text-gray-600 group-hover:text-gray-900'}`}>
                {label}
            </span>
        </label>
    )
}

export default function PropertyFilterSidebar({
    filters,
    onChange,
    propertyTypes = defaultPropertyTypes,
    amenitiesList = defaultAmenitiesList,
    facilitiesList = defaultFacilitiesList,
    roomFacilitiesList = defaultRoomFacilitiesList,
}: Props) {
    const [draft, setDraft] = useState<PropertyFilters>(filters)
    const navigate = useNavigate()

    useEffect(() => {
        setDraft(filters)
    }, [filters])

    const update = <K extends keyof PropertyFilters>(
        key: K,
        value: PropertyFilters[K],
    ) => {
        setDraft(prev => ({ ...prev, [key]: value }))
    }

    const toggleArrayValue = <K extends keyof PropertyFilters>(
        key: K,
        value: PropertyFilters[K] extends (infer U)[] ? U : never
    ) => {
        const currentValue = (draft[key] as unknown as any[]) || []
        const newValue = currentValue.includes(value as never)
            ? currentValue.filter(v => v !== value)
            : [...currentValue, value]
        update(key, newValue as PropertyFilters[K])
    }

    const resetFilters = () => {
        const cleared: PropertyFilters = {
            adults: 1,
            children: 0,
            minPrice: undefined,
            maxPrice: undefined,
            propertyTypes: [],
            minRating: undefined,
            propertyAmenities: [],
            propertyFacilities: [],
            roomFacilities: [],
            checkingDate: draft.checkingDate,
            checkoutDate: draft.checkoutDate,
            page: 1,
            size: draft.size,
            city: draft.city,
        }
        setDraft(cleared)
        onChange(cleared)

        const params = new URLSearchParams()
        if (cleared.checkingDate) params.set('checkingDate', cleared.checkingDate)
        if (cleared.checkoutDate) params.set('checkoutDate', cleared.checkoutDate)
        if (cleared.city) params.set('city', cleared.city)

        navigate(`/search/filter?${params.toString()}`)
    }

    const applyFilters = () => {
        onChange(draft)
        const params = new URLSearchParams()

        if (draft.city) params.set('city', draft.city)
        if (draft.checkingDate) params.set('checkingDate', draft.checkingDate)
        if (draft.checkoutDate) params.set('checkoutDate', draft.checkoutDate)

        params.set('adults', String(draft.adults))
        params.set('children', String(draft.children))

        if (draft.minPrice !== undefined) params.set('minPrice', String(draft.minPrice))
        if (draft.maxPrice !== undefined) params.set('maxPrice', String(draft.maxPrice))
        if (draft.minRating !== undefined) params.set('minRating', String(draft.minRating))

        draft.propertyTypes.forEach(t => params.append('propertyType', t))
        draft.propertyAmenities.forEach(id => params.append('propertyAmenities', String(id)))
        draft.propertyFacilities.forEach(id => params.append('propertyFacilities', String(id)))
        draft.roomFacilities.forEach(id => params.append('roomFacilities', String(id)))

        if (draft.page !== undefined) params.set('page', String(draft.page))
        if (draft.size !== undefined) params.set('size', String(draft.size))

        navigate(`/search/filter?${params.toString()}`)
    }

    return (
        <aside className="w-full lg:w-80 space-y-6 sticky top-24">
            {/* Header - Floating Card Style */}
            <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-5 flex items-center justify-between">
                <div className="flex items-center gap-3">
                    <div className="p-2 bg-blue-50 rounded-lg">
                        <SlidersHorizontal className="h-5 w-5 text-blue-600" />
                    </div>
                    <div>
                        <h2 className="font-bold text-gray-900">Filters</h2>
                        <p className="text-xs text-gray-500 font-medium">Refine your search</p>
                    </div>
                </div>
                <button
                    onClick={resetFilters}
                    className="p-2 text-gray-400 hover:text-gray-700 hover:bg-gray-100 rounded-lg transition-all"
                    title="Reset all filters"
                >
                    <RotateCcw className="h-4 w-4" />
                </button>
            </div>

            {/* Main Filters Container */}
            <div className="bg-white rounded-2xl shadow-sm border border-gray-200 px-6 py-2">
                {/* Price Range */}
                <Section title="Price per night">
                    <div className="space-y-4">
                        <div className="flex items-center gap-3">
                            <div className="relative flex-1">
                                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 text-sm">$</span>
                                <input
                                    type="number"
                                    placeholder="Min"
                                    min="0"
                                    value={draft.minPrice ?? ''}
                                    onChange={e => update('minPrice', Number(e.target.value) || undefined)}
                                    className="w-full pl-6 pr-3 py-2.5 rounded-xl border border-gray-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none text-sm transition-all"
                                />
                            </div>
                            <span className="text-gray-300">-</span>
                            <div className="relative flex-1">
                                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 text-sm">$</span>
                                <input
                                    type="number"
                                    placeholder="Max"
                                    min="0"
                                    value={draft.maxPrice ?? ''}
                                    onChange={e => update('maxPrice', Number(e.target.value) || undefined)}
                                    className="w-full pl-6 pr-3 py-2.5 rounded-xl border border-gray-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none text-sm transition-all"
                                />
                            </div>
                        </div>
                    </div>
                </Section>

                {/* Rating */}
                <Section title="Star Rating">
                    <div className="flex flex-wrap gap-2">
                        {[5, 4, 3].map(r => (
                            <button
                                key={r}
                                onClick={() => update('minRating', r === draft.minRating ? undefined : r)}
                                className={`flex items-center gap-1.5 px-4 py-2.5 rounded-xl border transition-all duration-200 ${draft.minRating === r
                                        ? 'bg-blue-50 border-blue-500 text-blue-700 shadow-sm ring-1 ring-blue-500/20'
                                        : 'border-gray-200 text-gray-600 hover:border-gray-300 hover:bg-gray-50'
                                    }`}
                            >
                                <span className="font-semibold text-sm">{r}</span>
                                <Star className={`h-4 w-4 ${draft.minRating === r ? 'fill-blue-500 text-blue-500' : 'fill-yellow-400 text-yellow-400'}`} />
                            </button>
                        ))}
                    </div>
                </Section>

                {/* Guests */}
                <Section title="Guests">
                    <div className="space-y-4">
                        <div className="flex items-center justify-between">
                            <span className="text-sm font-medium text-gray-700">Adults</span>
                            <div className="flex items-center gap-3">
                                <button
                                    onClick={() => update('adults', Math.max(1, draft.adults - 1))}
                                    className="w-8 h-8 flex items-center justify-center rounded-full border border-gray-200 hover:border-blue-500 hover:text-blue-600 transition-colors"
                                >
                                    <Minus className="w-4 h-4" />
                                </button>
                                <span className="w-4 text-center text-sm font-semibold">{draft.adults}</span>
                                <button
                                    onClick={() => update('adults', draft.adults + 1)}
                                    className="w-8 h-8 flex items-center justify-center rounded-full border border-gray-200 hover:border-blue-500 hover:text-blue-600 transition-colors"
                                >
                                    <Plus className="w-4 h-4" />
                                </button>
                            </div>
                        </div>
                        <div className="flex items-center justify-between">
                            <span className="text-sm font-medium text-gray-700">Children</span>
                            <div className="flex items-center gap-3">
                                <button
                                    onClick={() => update('children', Math.max(0, draft.children - 1))}
                                    className="w-8 h-8 flex items-center justify-center rounded-full border border-gray-200 hover:border-blue-500 hover:text-blue-600 transition-colors"
                                    disabled={draft.children <= 0}
                                >
                                    <Minus className="w-4 h-4" />
                                </button>
                                <span className="w-4 text-center text-sm font-semibold">{draft.children}</span>
                                <button
                                    onClick={() => update('children', draft.children + 1)}
                                    className="w-8 h-8 flex items-center justify-center rounded-full border border-gray-200 hover:border-blue-500 hover:text-blue-600 transition-colors"
                                >
                                    <Plus className="w-4 h-4" />
                                </button>
                            </div>
                        </div>
                    </div>
                </Section>

                {/* Property Type */}
                <Section title="Property Type">
                    <div className="flex flex-wrap gap-2">
                        {propertyTypes.map(type => (
                            <button
                                key={type}
                                onClick={() => toggleArrayValue('propertyTypes', type)}
                                className={`px-4 py-2 rounded-xl text-sm font-medium border transition-all ${draft.propertyTypes.includes(type)
                                        ? 'bg-blue-600 text-white border-blue-600 shadow-md shadow-blue-200'
                                        : 'bg-white text-gray-600 border-gray-200 hover:border-blue-300 hover:text-blue-600'
                                    }`}
                            >
                                {type}
                            </button>
                        ))}
                    </div>
                </Section>

                {/* Amenities */}
                <Section title="Amenities">
                    <div className="space-y-3">
                        {amenitiesList.map(item => (
                            <CheckboxItem
                                key={item.id}
                                label={item.name}
                                checked={draft.propertyAmenities?.includes(item.id) || false}
                                onChange={() => toggleArrayValue('propertyAmenities', item.id)}
                            />
                        ))}
                    </div>
                </Section>

                <Section title="Property Facilities">
                    <div className="space-y-3">
                        {facilitiesList.map(item => (
                            <CheckboxItem
                                key={item.id}
                                label={item.name}
                                checked={draft.propertyFacilities?.includes(item.id) || false}
                                onChange={() => toggleArrayValue('propertyFacilities', item.id)}
                            />
                        ))}
                    </div>
                </Section>

                <Section title="Room Facilities">
                    <div className="space-y-3">
                        {roomFacilitiesList.map(item => (
                            <CheckboxItem
                                key={item.id}
                                label={item.name}
                                checked={draft.roomFacilities?.includes(item.id) || false}
                                onChange={() => toggleArrayValue('roomFacilities', item.id)}
                            />
                        ))}
                    </div>
                </Section>
            </div>

            {/* Apply Button */}
            <div className="bg-white p-4 rounded-2xl shadow-sm border border-gray-200 sticky bottom-4">
                <button
                    onClick={applyFilters}
                    className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3.5 rounded-xl transition-all shadow-lg shadow-blue-500/30 hover:shadow-blue-500/40 active:scale-[0.98]"
                >
                    Apply Filters
                </button>
            </div>
        </aside>
    )
}
