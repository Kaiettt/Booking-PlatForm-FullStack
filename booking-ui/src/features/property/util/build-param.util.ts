export function buildAndStorePropertySearchParams(
    searchParams: URLSearchParams,
    propertyId: number
) {
    const allowedKeys = [
        'checkingDate',
        'checkoutDate',
        'adults',
        'children',
        'minPrice',
        'maxPrice',
        'roomFacilities',
    ]

    const params: Record<string, string | string[]> = {
        propertyId: String(propertyId),
    }

    allowedKeys.forEach((key) => {
        const values = searchParams.getAll(key)

        if (values.length === 1 && values[0]) {
            params[key] = values[0]
        }

        if (values.length > 1) {
            const filtered = values.filter(v => v && v.trim() !== '')
            if (filtered.length > 0) {
                params[key] = filtered
            }
        }
    })

    localStorage.setItem(
        'property_search_context',
        JSON.stringify(params)
    )

    return params
}



// src/features/property/util/build-param.util.ts

export function buildQueryFromStorage(): Record<string, string | string[]> {
    const raw = localStorage.getItem('property_search_context')
    if (!raw) return {}

    const data = JSON.parse(raw) as Record<string, string | string[]>

    const params: Record<string, string | string[]> = {}

    Object.entries(data).forEach(([key, value]) => {
        if (Array.isArray(value) && value.length > 0) {
            params[key] = value
        } else if (value !== null && value !== '') {
            params[key] = value
        }
    })

    return params
}
