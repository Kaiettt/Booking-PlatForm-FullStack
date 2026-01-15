// src/features/auth/components/AuthInput.tsx
import { forwardRef, type InputHTMLAttributes,  } from "react";

interface AuthInputProps extends InputHTMLAttributes<HTMLInputElement> {
    label: string;
    error?: string;
    icon?: React.ReactNode;
}

const AuthInput = forwardRef<HTMLInputElement, AuthInputProps>(({
    label,
    error,
    icon,
    className = "",
    ...props
}, ref) => {
    return (
        <div className="mb-5">
            <label className="block text-sm font-medium text-gray-700 mb-1.5">
                {label}
            </label>
            <div className="relative">
                {icon && (
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        {icon}
                    </div>
                )}
                <input
                    ref={ref}
                    className={`
                        w-full px-4 py-2.5
                        border ${error ? 'border-red-500' : 'border-gray-300'} 
                        rounded-lg
                        focus:outline-none focus:ring-2 focus:ring-blue-500/50
                        transition-all duration-200
                        placeholder-gray-400
                        ${icon ? 'pl-10' : 'pl-4'}
                        ${className}
                    `}
                    {...props}
                />
                {error && (
                    <p className="mt-1 text-xs text-red-500">{error}</p>
                )}
            </div>
        </div>
    );
});

AuthInput.displayName = "AuthInput";

export default AuthInput;