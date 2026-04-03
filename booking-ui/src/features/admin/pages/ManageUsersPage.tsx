import { useEffect, useState } from 'react'
import { adminApi, type AdminUserResponse } from '../services/admin.api'
import { UserRole } from '@/features/user/types/user.enum'

export default function ManageUsersPage() {
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [users, setUsers] = useState<AdminUserResponse[]>([])
    const [draftRoles, setDraftRoles] = useState<Record<number, UserRole>>({})
    const [savingUserId, setSavingUserId] = useState<number | null>(null)

    const fetchUsers = async () => {
        setLoading(true)
        setError(null)
        try {
            const data = await adminApi.getUsers()
            setUsers(data)
            const nextDraft: Record<number, UserRole> = {}
            data.forEach((u) => (nextDraft[u.id] = u.role))
            setDraftRoles(nextDraft)
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to load users.')
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchUsers()
    }, [])

    const handleSave = async (userId: number) => {
        const role = draftRoles[userId]
        if (!role) return
        setSavingUserId(userId)
        try {
            const updated = await adminApi.updateUserRole(userId, role)
            setUsers((prev) => prev.map((u) => (u.id === userId ? updated : u)))
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to update user role.')
        } finally {
            setSavingUserId(null)
        }
    }

    return (
        <div className="space-y-4">
            <div>
                <h1 className="text-2xl font-bold text-gray-900">Manage Users</h1>
                <p className="text-sm text-gray-500 mt-1">{users.length} users</p>
            </div>

            {loading && <p className="text-sm text-gray-500">Loading...</p>}
            {error && <p className="text-sm text-red-600">{error}</p>}

            {!loading && !error && (
                <div className="bg-white border border-gray-100 rounded-2xl overflow-hidden">
                    <div className="overflow-x-auto">
                        <table className="min-w-full text-sm">
                            <thead className="bg-gray-50 border-b border-gray-100">
                                <tr>
                                    <th className="text-left font-semibold p-4">ID</th>
                                    <th className="text-left font-semibold p-4">User</th>
                                    <th className="text-left font-semibold p-4">Email</th>
                                    <th className="text-left font-semibold p-4">Phone</th>
                                    <th className="text-left font-semibold p-4">Role</th>
                                    <th className="text-left font-semibold p-4">Status</th>
                                    <th className="text-left font-semibold p-4">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map((u) => (
                                    <tr key={u.id} className="border-b border-gray-50">
                                        <td className="p-4 font-medium text-gray-900">{u.id}</td>
                                        <td className="p-4">
                                            <div className="font-semibold text-gray-900">{u.fullName}</div>
                                        </td>
                                        <td className="p-4 text-gray-700">{u.email}</td>
                                        <td className="p-4 text-gray-700">{u.phoneNumber ?? '-'}</td>
                                        <td className="p-4">
                                            <select
                                                value={draftRoles[u.id] ?? u.role}
                                                onChange={(e) =>
                                                    setDraftRoles((p) => ({
                                                        ...p,
                                                        [u.id]: e.target.value as UserRole,
                                                    }))
                                                }
                                                className="px-3 py-2 bg-gray-50 border border-gray-200 rounded-xl"
                                            >
                                                <option value={UserRole.ADMIN}>ADMIN</option>
                                                <option value={UserRole.GUEST}>GUEST</option>
                                                <option value={UserRole.HOST}>HOST</option>
                                            </select>
                                        </td>
                                        <td className="p-4 text-gray-700">{u.status}</td>
                                        <td className="p-4">
                                            <button
                                                type="button"
                                                disabled={savingUserId === u.id}
                                                onClick={() => handleSave(u.id)}
                                                className="px-3 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-semibold disabled:opacity-70"
                                            >
                                                {savingUserId === u.id ? 'Saving...' : 'Save'}
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                                {users.length === 0 && (
                                    <tr>
                                        <td colSpan={7} className="p-8 text-center text-gray-500">
                                            No users found.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </div>
    )
}

