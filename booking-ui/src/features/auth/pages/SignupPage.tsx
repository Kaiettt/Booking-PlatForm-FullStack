import { useState } from "react";
import { Link } from "react-router-dom";
import { FiMail, FiLock, FiUser, FiPhone } from "react-icons/fi";
import AuthCard from "../components/AuthCard";
import AuthInput from "../components/AuthInput";
import { authService } from "../services/auth.service";
import { toast } from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import Footer from "@/shared/components/layout/Footer";
import RawHeader from "@/shared/components/layout/RawHeader";

export default function SignupPage() {
    const [isLoading, setIsLoading] = useState(false);
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        password: '',
        confirmPassword: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (formData.password !== formData.confirmPassword) {
            toast.error("Passwords don't match!");
            return;
        }
        setIsLoading(true);
        console.log("Signup form submitting...", formData); // Debug log
        try {
            console.log("Calling authService.signup..."); // Debug log
            await authService.signup({
                firstName: formData.firstName,
                lastName: formData.lastName,
                email: formData.email,
                password: formData.password,
                phoneNumber: formData.phoneNumber
            });
            toast.success("Signup successful! Please login.");
            navigate("/login");
        } catch (error) {
            console.error(error);
            toast.error("Signup failed. Please try again.");
        } finally {
            setIsLoading(false);
        }
    };

    const handleGoogleSignIn = () => {
        // Handle Google sign-in
        console.log('Google sign-in clicked');
        // window.location.href = '/api/auth/google';
    };

    return (
        <>
            <RawHeader />
            <AuthCard
                title="Create an Account"
                subtitle="Join our community and start your journey"
            >
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 -mb-2">
                        <AuthInput
                            label="First Name"
                            type="text"
                            name="firstName"
                            value={formData.firstName}
                            onChange={handleChange}
                            placeholder="John"
                            required
                            icon={<FiUser className="text-gray-400" />}
                        />
                        <AuthInput
                            label="Last Name"
                            type="text"
                            name="lastName"
                            value={formData.lastName}
                            onChange={handleChange}
                            placeholder="Doe"
                            required
                        />
                    </div>

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
                        label="Phone Number"
                        type="tel"
                        name="phoneNumber"
                        value={formData.phoneNumber}
                        onChange={handleChange}
                        placeholder="0123456789"
                        icon={<FiPhone className="text-gray-400" />}
                    />

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 -mt-2">
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
                        <AuthInput
                            label="Confirm Password"
                            type="password"
                            name="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            placeholder="••••••••"
                            required
                        />
                    </div>

                    <div className="flex items-start -mt-4">
                        <div className="flex items-center h-5">
                            <input
                                id="terms"
                                name="terms"
                                type="checkbox"
                                required
                                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                            />
                        </div>
                        <label htmlFor="terms" className="ml-2 text-sm text-gray-600">
                            I agree to the{" "}
                            <a href="#" className="text-blue-600 hover:underline">Terms of Service</a> and{" "}
                            <a href="#" className="text-blue-600 hover:underline">Privacy Policy</a>
                        </label>
                    </div>

                    <button
                        type="submit"
                        onClick={() => console.log("Submit button clicked")}
                        disabled={isLoading}
                        className={`
                            w-full py-2 px-4 rounded-lg font-medium text-white
                            bg-blue-600 hover:bg-blue-700
                            focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500
                            transition-all duration-200
                            flex items-center justify-center
                            ${isLoading ? 'opacity-75 cursor-not-allowed' : ''}
                        `}
                    >
                        {isLoading ? (
                            <>
                                <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                                Creating Account...
                            </>
                        ) : 'Create Account'}
                    </button>

                    <div className="relative my-4">
                        <div className="absolute inset-0 flex items-center">
                            <div className="w-full border-t border-gray-200"></div>
                        </div>
                        <div className="relative flex justify-center text-sm">
                            <span className="px-2 bg-white text-gray-500">Or continue with</span>
                        </div>
                    </div>

                    <button
                        type="button"
                        onClick={handleGoogleSignIn}
                        className="w-full flex items-center justify-center py-2 px-4 border border-gray-300 rounded-lg shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
                    >
                        <svg className="w-5 h-5 mr-2" viewBox="0 0 24 24" fill="currentColor">
                            <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4" />
                            <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853" />
                            <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z" fill="#FBBC05" />
                            <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335" />
                        </svg>
                        Continue with Google
                    </button>

                    <p className="text-sm text-center text-gray-600 mt-4">
                        Already have an account?{" "}
                        <Link to="/login" className="font-medium text-blue-600 hover:underline">
                            Sign in
                        </Link>
                    </p>
                </form>
            </AuthCard>
            <Footer />
        </>
    );
}