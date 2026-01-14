// src/features/property/services/searchDetailProperty.ts

import { get } from '@/services/apiClient'
import type { PropertyDetail } from '../types/property/property.detai.type'
import { cleanParams } from '@/util/clean-param.util'

export const searchDetailProperty = {
    fetchPropertyDetail: (
        params: Record<string, string | string[]>
    ): Promise<PropertyDetail> => {
        const cleaned = cleanParams(params)

        return get('/search/room-type/filter', {
            params: cleaned,
        })
    },
}
