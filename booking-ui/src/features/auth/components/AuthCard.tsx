// src/features/auth/components/AuthCard.tsx
import type { ReactNode } from "react";

interface AuthCardProps {
    title: string;
    subtitle: string;
    children: ReactNode;
}

export default function AuthCard({ title, subtitle, children }: AuthCardProps) {
    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-600 to-blue-700 p-4">
            <div className="w-full max-w-sm bg-white/95 backdrop-blur-sm rounded-xl shadow-2xl overflow-hidden border border-white/10">
                <div className="p-6">
                    <div className="text-center mb-6">
                        <div className="flex items-center justify-center mb-3">
                            <div className="bg-blue-100 p-2 rounded-lg">
                                <svg
                                    className="w-6 h-6 text-blue-600"
                                    fill="none"
                                    stroke="currentColor"
                                    viewBox="0 0 24 24"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth={2}
                                        d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
                                    />
                                </svg>
                            </div>
                        </div>
                        <h1 className="text-xl font-bold text-gray-800">{title}</h1>
                        <p className="text-gray-500 text-sm mt-1">{subtitle}</p>
                    </div>
                    {children}
                </div>
                <div className="bg-gray-50 px-4 py-3 text-center">
                    <p className="text-xs text-gray-500">
                        By continuing, you agree to our{" "}
                        <a href="#" className="text-blue-600 hover:underline">Terms</a> and{" "}
                        <a href="#" className="text-blue-600 hover:underline">Privacy</a>
                    </p>
                </div>
            </div>
        </div>
    );
}