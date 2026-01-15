package com.booking.KBookin.enumerate.rate;

public enum PrepaymentType {
    NONE,          // No prepayment required
    DEPOSIT,       // Requires a deposit
    FULL_PAYMENT,  // Requires full payment in advance
    PERCENTAGE     // Requires a percentage of the total amount
}
