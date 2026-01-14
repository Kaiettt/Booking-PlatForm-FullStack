import { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom'
import type { SearchParams } from '../types/search-param.type'
import { searchApi, searchPropertiesWithFilter } from '../services/searchApi'
import type { PropertySearchResponse } from '@/features/home/types/property-search.type'

export function useSearchProperties(params: SearchParams) {
    const location = useLocation()

    const [result, setResult] = useState<PropertySearchResponse | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    // 👇 check path
    const isFilterMode = location.pathname.includes('/filter')

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true)

                const response = isFilterMode
                    ? await searchPropertiesWithFilter.searchProperties(params)
                    : await searchApi.searchProperties(params)

                setResult(response)
            } catch {
                setError('Failed to load search results')
            } finally {
                setLoading(false)
            }
        }

        fetchData()
    }, [params, isFilterMode])

    return { result, loading, error, isFilterMode }
}
