export function cleanParams<T extends Record<string, any>>(params: T): Partial<T> {
    return Object.fromEntries(
        Object.entries(params).filter(
            ([_, v]) =>
                v !== undefined &&
                v !== null &&
                v !== '' &&
                !(Array.isArray(v) && v.length === 0),
        )
    ) as Partial<T>;
}