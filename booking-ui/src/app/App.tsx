import { Outlet } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';

function App() {
    return (
        <>
            <main className="min-h-screen bg-gray-50">
                <Outlet />
            </main>
            <Toaster position="top-right" />
        </>
    );
}

export default App;
