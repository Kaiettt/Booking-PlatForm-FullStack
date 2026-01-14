export interface PaginatedResponse<T> {
    currentPage: number
    totalPages: number
    totalElements: number
    data: T[]
}
