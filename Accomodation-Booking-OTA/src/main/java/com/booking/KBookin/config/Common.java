package com.booking.KBookin.config;

public class Common {
    public static String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found";
    public static String USER_NOT_FOUND = "";
    public static int MAX_CHILDREN_AGE = 5;
        public static int PAYMENT_EXPIRE_TIME_MINUTE = 10;

         public static final String SECURE_HASH = "vnp_SecureHash";
    public static final String TXN_REF = "vnp_TxnRef";
    public static final String TRANSACTION_NO = "vnp_TransactionNo";
    public static final String BANK_CODE = "vnp_BankCode";
    public static final String AMOUNT = "vnp_Amount";
    public static final String PAY_DATE = "vnp_PayDate";
    public static final String RESPONSE_CODE = "vnp_ResponseCode";
    public static final String TRANSACTION_STATUS = "vnp_TransactionStatus";

    public static final String OAUTH_PROVIDER_PASSWORD = "GOOGLE";

    public static final int INVENTORY_HOLD_EXPIRE_TIME_SECOND = 480;
    public static final int REDIS_CACHE_TTL_MINUTES = 10;
    public static final int EXTEND_LOCK_TIME_MINUTES = 5;
}
