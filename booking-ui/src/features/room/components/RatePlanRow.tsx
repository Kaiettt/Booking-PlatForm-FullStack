interface Props {
    ratePlan: any
}

export default function RatePlanRow({ ratePlan }: Props) {
    return (
        <div className="flex justify-between items-center p-4 border rounded-lg">
            <div>
                <p className="font-medium">{ratePlan.name}</p>
                <p className="text-xs text-gray-500">
                    {ratePlan.perks.map((p: any) => p.name).join(", ")}
                </p>
            </div>

            <div className="text-right">
                <p className="text-lg font-bold">${ratePlan.price}</p>
                <button className="mt-1 px-4 py-2 bg-blue-600 text-white text-sm rounded-lg">
                    Reserve
                </button>
            </div>
        </div>
    )
}
