import { useEffect, useState } from 'react'
import { adminApi, type AdminPropertyApproval } from '../services/admin.api'

export default function ApprovalsPage() {
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [properties, setProperties] = useState<AdminPropertyApproval[]>([])
    const [savingId, setSavingId] = useState<number | null>(null)

    const fetchPending = async () => {
        setLoading(true)
        setError(null)
        try {
            const data = await adminApi.getPendingProperties()
            setProperties(data)
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to load pending properties.')
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchPending()
    }, [])

    const handleApprove = async (propertyId: number) => {
        setSavingId(propertyId)
        try {
            await adminApi.approveProperty(propertyId)
            await fetchPending()
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to approve property.')
        } finally {
            setSavingId(null)
        }
    }

    const handleReject = async (propertyId: number) => {
        setSavingId(propertyId)
        try {
            await adminApi.rejectProperty(propertyId)
            await fetchPending()
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to reject property.')
        } finally {
            setSavingId(null)
        }
    }

    return (
        <div className="space-y-4">
            <div>
                <h1 className="text-2xl font-bold text-gray-900">Property Approvals</h1>
                <p className="text-sm text-gray-500 mt-1">{properties.length} pending</p>
            </div>

            {loading && <p className="text-sm text-gray-500">Loading...</p>}
            {error && <p className="text-sm text-red-600">{error}</p>}

            {!loading && !error && properties.length === 0 && (
                <div className="bg-white border border-gray-100 rounded-2xl p-6 text-sm text-gray-600">
                    No pending properties.
                </div>
            )}

            {!loading && !error && properties.length > 0 && (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {properties.map((p) => (
                        <div key={p.id} className="bg-white border border-gray-100 rounded-2xl overflow-hidden">
                            <div className="h-40 bg-gray-100 relative">
                                {p.media?.[0]?.url ? (
                                    <img
                                        src={p.media[0].url}
                                        alt={p.name}
                                        className="h-full w-full object-cover"
                                    />
                                ) : (
                                    <div className="h-full w-full flex items-center justify-center text-sm text-gray-500">
                                        No image
                                    </div>
                                )}
                            </div>

                            <div className="p-5 space-y-3">
                                <div className="flex items-start justify-between gap-4">
                                    <div>
                                        <div className="font-bold text-gray-900">{p.name}</div>
                                        <div className="text-xs text-gray-500 mt-1">
                                            {p.type} • {p.address.city}, {p.address.country}
                                        </div>
                                        <div className="text-xs text-gray-500 mt-1">
                                            Host: <span className="font-semibold">{p.host.fullName}</span>
                                        </div>
                                    </div>
                                    <span className="px-3 py-1 bg-blue-50 text-blue-700 text-xs rounded-full font-semibold">
                                        {p.status}
                                    </span>
                                </div>

                                <p className="text-sm text-gray-600 line-clamp-3">{p.description}</p>

                                <div className="flex gap-3">
                                    <button
                                        type="button"
                                        disabled={savingId === p.id}
                                        onClick={() => handleApprove(p.id)}
                                        className="flex-1 px-3 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-semibold disabled:opacity-70"
                                    >
                                        {savingId === p.id ? 'Saving...' : 'Approve'}
                                    </button>
                                    <button
                                        type="button"
                                        disabled={savingId === p.id}
                                        onClick={() => handleReject(p.id)}
                                        className="flex-1 px-3 py-2 bg-red-600 hover:bg-red-700 text-white rounded-xl text-sm font-semibold disabled:opacity-70"
                                    >
                                        Reject
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}

