import RatePlanRow from "./RatePlanRow"

interface Props {
    room: any
}

export default function RoomTypeCard({ room }: Props) {
    return (
        <div className="border rounded-2xl p-6 space-y-4">
            <h4 className="text-lg font-semibold">{room.name}</h4>

            <p className="text-sm text-gray-600">
                {room.sizeM2} m² · {room.bedType} · {room.viewType}
            </p>

            <div className="space-y-2">
                {room.ratePlans.map((rp: any) => (
                    <RatePlanRow key={rp.id} ratePlan={rp} />
                ))}
            </div>
        </div>
    )
}
