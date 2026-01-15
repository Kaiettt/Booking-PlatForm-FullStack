
import type { SearchParams } from '../types/search-param.type'
import type { PropertySearchResponse } from '@/features/home/types/property-search.type'
import { get } from '@/services/apiClient'
import { cleanParams } from '@/util/clean-param.util'



export const searchApi = {
    searchProperties: (params: SearchParams): Promise<PropertySearchResponse> => {
        const cleaned = cleanParams(params)

        return get('/search', { params: cleaned })
    },
}

export const searchPropertiesWithFilter = {
    searchProperties: (params: SearchParams): Promise<PropertySearchResponse> => {
        const cleaned = cleanParams(params)

        return get('/search/filter', { params: cleaned })
    },
}
