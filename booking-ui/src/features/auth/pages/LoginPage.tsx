import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FiMail, FiLock } from "react-icons/fi";
import AuthCard from "../components/AuthCard";
import AuthInput from "../components/AuthInput";
import Footer from "@/shared/components/layout/Footer";
import RawHeader from "@/shared/components/layout/RawHeader";
import { authService } from "../services/auth.service";
import { GoogleLogin, type CredentialResponse } from "@react-oauth/google";

export default function LoginPage() {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    });
    const [message, setMessage] = useState("");

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    // Email/password login
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true);
        setMessage("");

        try {
            const request = {
                username: formData.email.trim(),
                password: formData.password,
            };
            await authService.login(request);
            navigate("/");
        } catch (error: any) {
            console.error("Login failed:", error);
            setMessage("Login failed. Please check your credentials.");
        } finally {
            setIsLoading(false);
        }
    };

    // Google login success
    const onGoogleSuccess = async (credentialResponse: CredentialResponse) => {
        setIsLoading(true);
        setMessage("");
        try {
            const idToken = credentialResponse?.credential;
            if (!idToken) throw new Error("Missing Google credential");
            // Send ID token to backend
            await authService.googleSignIn(idToken);
            navigate("/");
        } catch (error: any) {
            console.error("Google login failed:", error);
            setMessage("Google login failed. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    const onGoogleError = () => {
        setMessage("Google sign-in error. Please try again.");
    };

    return (
        <>
            <RawHeader />
            <AuthCard title="Welcome Back" subtitle="Sign in to your account">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <AuthInput
                        label="Email"
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        placeholder="you@example.com"
                        required
                        icon={<FiMail className="text-gray-400" />}
                    />
                    <AuthInput
                        label="Password"
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        placeholder="••••••••"
                        required
                        icon={<FiLock className="text-gray-400" />}
                    />

                    <div className="flex items-center justify-between">
                        <label className="flex items-center text-sm text-gray-600">
                            <input
                                type="checkbox"
                                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                            />
                            <span className="ml-2">Remember me</span>
                        </label>
                        <Link
                            to="/forgot-password"
                            className="text-sm font-medium text-blue-600 hover:underline"
                        >
                            Forgot password?
                        </Link>
                    </div>

                    <button
                        type="submit"
                        disabled={isLoading}
                        className={`w-full py-2 px-4 rounded-lg font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all duration-200 flex items-center justify-center ${isLoading ? "opacity-75 cursor-not-allowed" : ""
                            }`}
                    >
                        {isLoading ? "Signing in..." : "Sign In"}
                    </button>

                    <div className="relative my-4">
                        <div className="absolute inset-0 flex items-center">
                            <div className="w-full border-t border-gray-200"></div>
                        </div>
                        <div className="relative flex justify-center text-sm">
                            <span className="px-2 bg-white text-gray-500">Or continue with</span>
                        </div>
                    </div>

                    <div className="flex justify-center">
                        <GoogleLogin
                            onSuccess={onGoogleSuccess}
                            onError={onGoogleError}
                            useOneTap
                        />
                    </div>

                    {message && <div className="text-center text-red-500 mt-2">{message}</div>}

                    <p className="text-sm text-center text-gray-600 mt-4">
                        Don't have an account?{" "}
                        <Link to="/signup" className="font-medium text-blue-600 hover:underline">
                            Sign up
                        </Link>
                    </p>
                </form>
            </AuthCard>
            <Footer />
        </>
    );
}
