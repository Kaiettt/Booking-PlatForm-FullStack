import { useEffect, useMemo, useState } from 'react'
import { hostApi } from '@/features/host/services/host.api'
import type { RoomType } from '@/features/room/room-type.type'

export default function ManageRoomTypesPage() {
    const [propertyId, setPropertyId] = useState<number | null>(() => {
        const raw = localStorage.getItem('host_property_id')
        return raw ? Number(raw) : null
    })

    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [roomTypes, setRoomTypes] = useState<RoomType[]>([])

    const [selectedRoomTypeId, setSelectedRoomTypeId] = useState<number | null>(null)
    const [mediaFiles, setMediaFiles] = useState<File[]>([])

    // Create room type form
    const [form, setForm] = useState({
        name: '',
        maxAdults: 2,
        maxChildren: 0,
        maxGuest: 2,
        sizeM2: 20,
        bedType: '',
        viewType: '',
        smokingAllowed: false,
        totalRooms: 1,
    })

    const canLoad = propertyId !== null && !Number.isNaN(propertyId)

    const fetchRoomTypes = async () => {
        if (!canLoad || propertyId === null) return
        setLoading(true)
        setError(null)
        try {
            const data = await hostApi.getRoomTypesByProperty(propertyId)
            setRoomTypes(data)
            if (data.length > 0 && selectedRoomTypeId === null) {
                setSelectedRoomTypeId(data[0].id)
            }
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to load room types.')
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchRoomTypes()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [propertyId])

    const handleCreate = async () => {
        if (!propertyId) return
        setLoading(true)
        setError(null)

        try {
            await hostApi.createRoomType({
                propertyId,
                name: form.name.trim(),
                maxAdults: form.maxAdults,
                maxChildren: form.maxChildren,
                maxGuest: form.maxGuest,
                sizeM2: form.sizeM2,
                bedType: form.bedType || null,
                viewType: form.viewType || null,
                smokingAllowed: form.smokingAllowed,
                totalRooms: form.totalRooms,
            })

            const data = await hostApi.getRoomTypesByProperty(propertyId)
            setRoomTypes(data)
            const created = data.find((r) => r.name === form.name.trim())
            if (created) setSelectedRoomTypeId(created.id)
            setMediaFiles([])
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to create room type.')
        } finally {
            setLoading(false)
        }
    }

    const handleUploadMedia = async () => {
        if (!selectedRoomTypeId || mediaFiles.length === 0) return
        setLoading(true)
        setError(null)

        try {
            await hostApi.uploadRoomTypeMedia(selectedRoomTypeId, mediaFiles)
            await fetchRoomTypes()
            setMediaFiles([])
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to upload media.')
        } finally {
            setLoading(false)
        }
    }

    const selectedRoomType = useMemo(() => {
        return roomTypes.find((r) => r.id === selectedRoomTypeId) ?? null
    }, [roomTypes, selectedRoomTypeId])

    return (
        <div className="space-y-6">
            <div>
                <h1 className="text-2xl font-bold text-gray-900">Manage Room Types</h1>
                <p className="text-sm text-gray-500 mt-1">Property room setup</p>
            </div>

            {error && <div className="text-sm text-red-600">{error}</div>}

            <div className="bg-white rounded-2xl border border-gray-100 p-6 space-y-4">
                <div className="flex items-center justify-between gap-4">
                    <div className="flex items-center gap-3">
                        <span className="text-sm font-medium text-gray-700">Property ID</span>
                        <input
                            type="number"
                            value={propertyId ?? ''}
                            onChange={(e) => {
                                const val = e.target.value
                                setPropertyId(val ? Number(val) : null)
                            }}
                            className="w-44 px-3 py-2 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                    <button
                        type="button"
                        onClick={fetchRoomTypes}
                        disabled={!canLoad || loading}
                        className="px-4 py-2 bg-gray-900 hover:bg-gray-800 text-white rounded-xl text-sm font-semibold disabled:opacity-70"
                    >
                        Reload
                    </button>
                </div>

                {loading && <p className="text-sm text-gray-500">Loading...</p>}

                {!loading && roomTypes.length === 0 && (
                    <p className="text-sm text-gray-600">No room types yet.</p>
                )}

                {roomTypes.length > 0 && (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {roomTypes.map((rt) => (
                            <div
                                key={rt.id}
                                className={`border rounded-2xl p-4 ${
                                    rt.id === selectedRoomTypeId
                                        ? 'border-blue-200 bg-blue-50/50'
                                        : 'border-gray-100 bg-white'
                                }`}
                            >
                                <div className="flex items-start justify-between gap-3">
                                    <div>
                                        <div className="font-bold text-gray-900">{rt.name}</div>
                                        <div className="text-xs text-gray-500 mt-1">
                                            Max guest: {rt.maxGuest} • Size: {rt.sizeM2} m2
                                        </div>
                                        <div className="text-xs text-gray-500 mt-1">
                                            Total rooms: {rt.totalRooms}
                                        </div>
                                    </div>
                                    <button
                                        type="button"
                                        onClick={() => setSelectedRoomTypeId(rt.id)}
                                        className={`px-3 py-1.5 rounded-xl text-xs font-semibold ${
                                            rt.id === selectedRoomTypeId
                                                ? 'bg-blue-600 text-white'
                                                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                                        }`}
                                    >
                                        Select
                                    </button>
                                </div>

                                {rt.media?.length ? (
                                    <div className="mt-3 flex gap-2 overflow-x-auto">
                                        {rt.media.slice(0, 4).map((m) => (
                                            <img
                                                key={m.id}
                                                src={m.url}
                                                alt={rt.name}
                                                className="h-16 w-16 object-cover rounded-lg border border-gray-100"
                                            />
                                        ))}
                                    </div>
                                ) : (
                                    <div className="mt-3 text-xs text-gray-500">No media uploaded.</div>
                                )}
                            </div>
                        ))}
                    </div>
                )}
            </div>

            <div className="bg-white rounded-2xl border border-gray-100 p-6 space-y-4">
                <h2 className="text-lg font-bold text-gray-900">Create room type</h2>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Name</label>
                        <input
                            value={form.name}
                            onChange={(e) => setForm((p) => ({ ...p, name: e.target.value }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Total rooms</label>
                        <input
                            type="number"
                            value={form.totalRooms}
                            onChange={(e) => setForm((p) => ({ ...p, totalRooms: Number(e.target.value) }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Max adults</label>
                        <input
                            type="number"
                            value={form.maxAdults}
                            onChange={(e) => setForm((p) => ({ ...p, maxAdults: Number(e.target.value) }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Max children</label>
                        <input
                            type="number"
                            value={form.maxChildren}
                            onChange={(e) => setForm((p) => ({ ...p, maxChildren: Number(e.target.value) }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Max guest</label>
                        <input
                            type="number"
                            value={form.maxGuest}
                            onChange={(e) => setForm((p) => ({ ...p, maxGuest: Number(e.target.value) }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Size (m2)</label>
                        <input
                            type="number"
                            value={form.sizeM2}
                            onChange={(e) => setForm((p) => ({ ...p, sizeM2: Number(e.target.value) }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Bed type (optional)</label>
                        <input
                            value={form.bedType}
                            onChange={(e) => setForm((p) => ({ ...p, bedType: e.target.value }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">View type (optional)</label>
                        <input
                            value={form.viewType}
                            onChange={(e) => setForm((p) => ({ ...p, viewType: e.target.value }))}
                            className="mt-1 w-full px-4 py-3 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 focus:bg-white transition-all outline-none"
                        />
                    </div>
                </div>

                <div className="flex items-center gap-3">
                    <input
                        id="smokingAllowed"
                        type="checkbox"
                        checked={form.smokingAllowed}
                        onChange={(e) => setForm((p) => ({ ...p, smokingAllowed: e.target.checked }))}
                    />
                    <label htmlFor="smokingAllowed" className="text-sm font-medium text-gray-700">
                        Smoking allowed
                    </label>
                </div>

                <button
                    type="button"
                    onClick={handleCreate}
                    disabled={loading || !propertyId || !form.name.trim()}
                    className="w-full bg-blue-600 hover:bg-blue-700 text-white rounded-xl h-11 font-semibold disabled:opacity-70"
                >
                    {loading ? 'Please wait...' : 'Create room type'}
                </button>
            </div>

            <div className="bg-white rounded-2xl border border-gray-100 p-6 space-y-4">
                <h2 className="text-lg font-bold text-gray-900">Upload room type media</h2>

                {selectedRoomType ? (
                    <div className="text-sm text-gray-600">
                        Selected: <span className="font-semibold text-gray-900">{selectedRoomType.name}</span> (ID:{' '}
                        {selectedRoomType.id})
                    </div>
                ) : (
                    <div className="text-sm text-gray-600">Select a room type first.</div>
                )}

                <input
                    type="file"
                    accept="image/*"
                    multiple
                    onChange={(e) => setMediaFiles(Array.from(e.target.files ?? []))}
                />
                <div className="text-xs text-gray-500">{mediaFiles.length} files selected</div>

                <button
                    type="button"
                    onClick={handleUploadMedia}
                    disabled={loading || !selectedRoomTypeId || mediaFiles.length === 0}
                    className="w-full bg-gray-900 hover:bg-gray-800 text-white rounded-xl h-11 font-semibold disabled:opacity-70"
                >
                    {loading ? 'Uploading...' : 'Upload media'}
                </button>
            </div>
        </div>
    )
}

