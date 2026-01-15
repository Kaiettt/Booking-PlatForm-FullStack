interface Props {
    description: string
    type: string
    address: { city: string; country: string }
}

export default function PropertyOverview({ description }: Props) {
    return (
        <section className="space-y-3">
            <h2 className="text-xl font-semibold">About this property</h2>
            <p className="text-gray-700 leading-relaxed">
                {description}
            </p>
        </section>
    )
}
