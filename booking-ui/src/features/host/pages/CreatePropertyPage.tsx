import { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { apiClient, post } from '@/services/apiClient'
import { useAuthStore } from '@/stores/auth.store'
import type { PropertyType } from '@/features/property/types/property/property.enum'

type Step = 1 | 2 | 3

type DocumentItem = {
    type: string
    file: File | null
}

export default function CreatePropertyPage() {
    const user = useAuthStore((s) => s.user)
    const isAuthenticated = useAuthStore((s) => s.isAuthenticated)
    const navigate = useNavigate()

    const [step, setStep] = useState<Step>(1)
    const [propertyId, setPropertyId] = useState<number | null>(null)
    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const propertyTypes = useMemo(
        () => ['HOTEL', 'APARTMENT', 'VILLA', 'HOSTEL'] as PropertyType[],
        []
    )

    // Step 1
    const [form1, setForm1] = useState({
        name: '',
        description: '',
        type: 'HOTEL' as PropertyType,
        address: {
            streetAddress: '',
            ward: '',
            district: '',
            city: '',
            state: '',
            postalCode: '',
            country: 'Vietnam',
            lat: '0',
            lng: '0',
        },
    })

    // Step 2
    const [documents, setDocuments] = useState<DocumentItem[]>([
        { type: 'BUSINESS_LICENSE', file: null },
    ])

    // Step 3
    const [images, setImages] = useState<File[]>([])

    const handleCreateProperty = async () => {
        if (!user) return
        setSubmitting(true)
        setError(null)

        try {
            const payload = {
                name: form1.name.trim(),
                description: form1.description.trim(),
                type: form1.type,
                address: {
                    streetAddress: form1.address.streetAddress.trim(),
                    ward: form1.address.ward.trim() || null,
                    district: form1.address.district.trim() || null,
                    city: form1.address.city.trim(),
                    state: form1.address.state.trim() || null,
                    postalCode: form1.address.postalCode.trim() || null,
                    country: form1.address.country.trim(),
                    geo: {
                        lat: Number(form1.address.lat),
                        lng: Number(form1.address.lng),
                    },
                },
                userId: user.id,
            }

            const createdId = await post<number>('/properties', payload)
            setPropertyId(createdId)
            setStep(2)
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to create property.')
        } finally {
            setSubmitting(false)
        }
    }

    const handleUploadDocuments = async () => {
        if (!propertyId) return
        setSubmitting(true)
        setError(null)

        try {
            const fd = new FormData()
            const docsWithFiles = documents.filter((d) => d.file)
            docsWithFiles.forEach((doc, idx) => {
                fd.append(`propertyDetailDocuments[${idx}].type`, doc.type)
                fd.append(`propertyDetailDocuments[${idx}].file`, doc.file as File)
            })

            const updatedId = await apiClient.put<number>(`/properties/${propertyId}/documents`, fd, {
                headers: { 'Content-Type': 'multipart/form-data' },
            })
            setPropertyId(updatedId.data)
            setStep(3)
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to upload documents.')
        } finally {
            setSubmitting(false)
        }
    }

    const handleUploadItems = async () => {
        if (!propertyId) return
        setSubmitting(true)
        setError(null)

        try {
            const fd = new FormData()
            fd.append('propertyId', String(propertyId))
            images.forEach((img) => fd.append('images', img))

            const updatedId = await apiClient.put<number>(`/properties/${propertyId}/items`, fd, {
                headers: { 'Content-Type': 'multipart/form-data' },
            })

            localStorage.setItem('host_property_id', String(updatedId.data))
            navigate('/host/room-types')
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to upload property items.')
        } finally {
            setSubmitting(false)
        }
    }

    if (!isAuthenticated || !user) {
        return (
            <div className="bg-white border border-gray-100 rounded-2xl p-6 text-sm text-gray-600">
                Please sign in as HOST to create a property.
            </div>
        )
    }

    return (
        <div className="space-y-6">
            <div>
                <h1 className="text-2xl font-bold text-gray-900">Create Property</h1>
                <p className="text-sm text-gray-500 mt-1">Step {step} of 3</p>
            </div>

            {error && <div className="text-sm text-red-600">{error}</div>}

            {step === 1 && (
                <div className="bg-white rounded-2xl border border-gray-100 p-6 space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Property name</label>
                        <input
                            value={form1.name}
                            onChange={(e) => setForm1((p) => ({ ...p, name: e.target.value }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Description</label>
                        <textarea
                            value={form1.description}
                            onChange={(e) => setForm1((p) => ({ ...p, description: e.target.value }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none resize-none"
                            rows={4}
                        />
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Type</label>
                            <select
                                value={form1.type}
                                onChange={(e) => setForm1((p) => ({ ...p, type: e.target.value as PropertyType }))}
                                className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                            >
                                {propertyTypes.map((t) => (
                                    <option key={t} value={t}>
                                        {t}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700">Country</label>
                            <input
                                value={form1.address.country}
                                onChange={(e) =>
                                    setForm1((p) => ({ ...p, address: { ...p.address, country: e.target.value } }))
                                }
                                className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Street address</label>
                        <input
                            value={form1.address.streetAddress}
                            onChange={(e) =>
                                setForm1((p) => ({
                                    ...p,
                                    address: { ...p.address, streetAddress: e.target.value },
                                }))
                            }
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">City</label>
                            <input
                                value={form1.address.city}
                                onChange={(e) =>
                                    setForm1((p) => ({
                                        ...p,
                                        address: { ...p.address, city: e.target.value },
                                    }))
                                }
                                className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700">Latitude</label>
                            <input
                                type="number"
                                value={form1.address.lat}
                                onChange={(e) =>
                                    setForm1((p) => ({
                                        ...p,
                                        address: { ...p.address, lat: e.target.value },
                                    }))
                                }
                                className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                            />
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Longitude</label>
                            <input
                                type="number"
                                value={form1.address.lng}
                                onChange={(e) =>
                                    setForm1((p) => ({
                                        ...p,
                                        address: { ...p.address, lng: e.target.value },
                                    }))
                                }
                                className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Optional ward</label>
                            <input
                                value={form1.address.ward}
                                onChange={(e) =>
                                    setForm1((p) => ({
                                        ...p,
                                        address: { ...p.address, ward: e.target.value },
                                    }))
                                }
                                className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                            />
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Optional district</label>
                            <input
                                value={form1.address.district}
                                onChange={(e) =>
                                    setForm1((p) => ({
                                        ...p,
                                        address: { ...p.address, district: e.target.value },
                                    }))
                                }
                                className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                            />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Optional state</label>
                            <input
                                value={form1.address.state}
                                onChange={(e) =>
                                    setForm1((p) => ({
                                        ...p,
                                        address: { ...p.address, state: e.target.value },
                                    }))
                                }
                                className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Optional postal code</label>
                        <input
                            value={form1.address.postalCode}
                            onChange={(e) =>
                                setForm1((p) => ({
                                    ...p,
                                    address: { ...p.address, postalCode: e.target.value },
                                }))
                            }
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>

                    <button
                        onClick={handleCreateProperty}
                        disabled={submitting}
                        className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-xl h-11 font-semibold disabled:opacity-70"
                    >
                        {submitting ? 'Creating...' : 'Continue to documents'}
                    </button>
                </div>
            )}

            {step === 2 && (
                <div className="bg-white rounded-2xl border border-gray-100 p-6 space-y-4">
                    <div className="text-sm text-gray-600">
                        Property ID: <span className="font-semibold">{propertyId}</span>
                    </div>

                    {documents.map((doc, idx) => (
                        <div key={idx} className="grid grid-cols-1 md:grid-cols-3 gap-4 items-center">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Document type</label>
                                <select
                                    value={doc.type}
                                    onChange={(e) => {
                                        const next = [...documents]
                                        next[idx] = { ...next[idx], type: e.target.value }
                                        setDocuments(next)
                                    }}
                                    className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                                >
                                    <option value="BUSINESS_LICENSE">BUSINESS_LICENSE</option>
                                    <option value="PROPERTY_DEED">PROPERTY_DEED</option>
                                    <option value="IDENTITY_ID">IDENTITY_ID</option>
                                    <option value="ROOM_TYPE_IMAGES">ROOM_TYPE_IMAGES</option>
                                </select>
                            </div>
                            <div className="md:col-span-2">
                                <label className="block text-sm font-medium text-gray-700">File</label>
                                <input
                                    type="file"
                                    accept="image/*,application/pdf"
                                    onChange={(e) => {
                                        const file = e.target.files?.[0] ?? null
                                        const next = [...documents]
                                        next[idx] = { ...next[idx], file }
                                        setDocuments(next)
                                    }}
                                    className="mt-1 w-full"
                                />
                            </div>
                        </div>
                    ))}

                    <div className="flex gap-3">
                        <button
                            type="button"
                            onClick={() => setDocuments((p) => [...p, { type: 'BUSINESS_LICENSE', file: null }])}
                            className="px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded-xl text-sm font-semibold"
                        >
                            Add more
                        </button>
                        <button
                            type="button"
                            onClick={handleUploadDocuments}
                            disabled={submitting}
                            className="flex-1 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-sm font-semibold disabled:opacity-70"
                        >
                            {submitting ? 'Uploading...' : 'Continue to items'}
                        </button>
                    </div>
                </div>
            )}

            {step === 3 && (
                <div className="bg-white rounded-2xl border border-gray-100 p-6 space-y-4">
                    <div className="text-sm text-gray-600">
                        Property ID: <span className="font-semibold">{propertyId}</span>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Property images</label>
                        <input
                            type="file"
                            accept="image/*"
                            multiple
                            onChange={(e) => setImages(Array.from(e.target.files ?? []))}
                            className="mt-1 w-full"
                        />
                        <div className="text-xs text-gray-500 mt-1">{images.length} files selected</div>
                    </div>

                    <button
                        type="button"
                        onClick={handleUploadItems}
                        disabled={submitting}
                        className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-xl h-11 font-semibold disabled:opacity-70"
                    >
                        {submitting ? 'Saving...' : 'Finish & go to room types'}
                    </button>
                </div>
            )}
        </div>
    )
}

