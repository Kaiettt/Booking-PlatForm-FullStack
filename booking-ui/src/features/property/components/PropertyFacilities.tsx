import { Building2, Check } from "lucide-react";

interface Props {
    facilities: { id: number; name: string }[]
}

export default function PropertyFacilities({ facilities }: Props) {
    if (!facilities.length) return null

    return (
        <section className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 mt-8">
            <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center">
                <span className="w-8 h-8 rounded-lg bg-indigo-100 flex items-center justify-center mr-3">
                    <Building2 className="w-5 h-5 text-indigo-600" />
                </span>
                Property Facilities
            </h3>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-y-3 gap-x-8">
                {facilities.map(f => (
                    <div
                        key={f.id}
                        className="flex items-center gap-3 text-gray-600 group hover:text-gray-900 transition-colors"
                    >
                        <div className="w-6 h-6 rounded-full bg-green-50 flex-shrink-0 flex items-center justify-center border border-green-100 group-hover:bg-green-100 transition-colors">
                            <Check className="w-3.5 h-3.5 text-green-600" />
                        </div>
                        <span className="text-sm font-medium leading-tight">
                            {f.name}
                        </span>
                    </div>
                ))}
            </div>
        </section>
    )
}
