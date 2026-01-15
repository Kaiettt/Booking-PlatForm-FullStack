import { Link } from 'react-router-dom';
import { ChevronRight } from 'lucide-react';

type BreadcrumbItem = {
    name: string;
    path: string;
    isLast?: boolean;
};

interface BreadcrumbProps {
    items: BreadcrumbItem[];
    className?: string;
}

export default function Breadcrumb({ items, className = '' }: BreadcrumbProps) {
    return (
        <nav className={`flex items-center text-sm ${className}`} aria-label="Breadcrumb">
            <ol className="flex items-center flex-wrap gap-2">
                {items.map((item, index) => (
                    <li key={item.path} className="flex items-center">
                        {/* Separator */}
                        {index > 0 && <ChevronRight className="h-4 w-4 text-gray-400" />}

                        {/* Last item */}
                        {item.isLast ? (
                            <span className="bg-gray-100 text-gray-600 px-2 py-1 rounded-full font-medium">
                                {item.name}
                            </span>
                        ) : (
                            <Link
                                to={item.path}
                                className="text-blue-600 hover:text-blue-800 hover:underline font-medium transition-colors px-2 py-1 rounded-full hover:bg-gray-100"
                            >
                                {item.name}
                            </Link>
                        )}
                    </li>
                ))}
            </ol>
        </nav>
    );
}
