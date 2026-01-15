import { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';

const PaymentCallback = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        const transactionStatus = searchParams.get('vnp_TransactionStatus');
        const responseCode = searchParams.get('vnp_ResponseCode');

        if (transactionStatus === '00' || responseCode === '00') {
            navigate('/booking-success', { replace: true });
        } else {
            navigate(`/booking/failed?code=${responseCode}`, { replace: true });
        }
    }, [searchParams, navigate]);

    return (
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: '50px' }}>
            <h2>Processing payment result...</h2>
        </div>
    );
};

export default PaymentCallback;