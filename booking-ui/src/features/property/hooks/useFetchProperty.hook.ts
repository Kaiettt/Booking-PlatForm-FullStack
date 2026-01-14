import { useEffect, useState } from 'react'
import type { PropertyDetail } from '../types/property/property.detai.type'
import { buildQueryFromStorage } from '../util/build-param.util'
import { searchDetailProperty } from '../services/propertyApi'

export function useFetchProperty() {

    const [result, setResult] = useState<PropertyDetail | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)


    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true)
                const query = buildQueryFromStorage();
                const response = await searchDetailProperty.fetchPropertyDetail(query);

                setResult(response)
                localStorage.setItem('propertyDetail', JSON.stringify(response));
            } catch {
                setError('Failed to load search results')
            } finally {
                setLoading(false)
            }
        }

        fetchData()
    }, [])

    return { result, loading, error }
}
