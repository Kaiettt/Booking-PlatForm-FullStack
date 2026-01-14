package com.booking.KBookin.service.notification;


import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.booking.CancellationResponse;

public final class EmailTemplate {

    private EmailTemplate() {
        // Ngăn chặn khởi tạo class
    }
    public static String buildBookingSuccessEmail(BookingResponse booking) {
        StringBuilder itemsHtml = new StringBuilder();

        for (var item : booking.getBookingItems()) {
            itemsHtml.append(String.format("""
                <tr>
                    <td style="padding: 10px; border-bottom: 1px solid #eee;">
                        <strong>Room Type ID: %d</strong><br>
                        <span style="font-size: 14px; color: #666;">Rate Plan: %s</span>
                    </td>
                    <td style="padding: 10px; border-bottom: 1px solid #eee; text-align: right;">
                        %d x %,.0f VND
                    </td>
                </tr>
                """,
                    item.getRoomTypeId(),
                    item.getRatePlan().getName(),
                    item.getQuantity(),
                    item.getAmount()
            ));
        }

        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px;">
                <h2 style="color: #1D70B8; text-align: center;">Booking Confirmed!</h2>
                <p>Hi <strong>%s</strong>,</p>
                <p>Your booking has been successfully placed. Below are your details:</p>
                
                <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px;">
                    <p><strong>Reference:</strong> %s</p>
                    <p><strong>Check-in:</strong> %s</p>
                    <p><strong>Check-out:</strong> %s</p>
                </div>

                <table style="width: 100%%; border-collapse: collapse; margin-top: 20px;">
                    <thead>
                        <tr style="background-color: #eee;">
                            <th style="text-align: left; padding: 10px;">Item</th>
                            <th style="text-align: right; padding: 10px;">Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        %s
                    </tbody>
                </table>

                <h3 style="text-align: right; color: #1D70B8;">Total Amount: %,.0f VND</h3>
                
                <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
                <p style="font-size: 12px; color: #888; text-align: center;">
                    Thank you for choosing KBookin! If you have any questions, contact us at leekhoa0409@gmail.com.
                </p>
            </div>
            """.formatted(
                booking.getGuest().getName(),
                booking.getBookingReference(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                itemsHtml.toString(),
                booking.getTotalAmount()
        );
    }
    public static String buildVerificationEmail(String name, long token) {
        return """
            <div style="font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c">
                <table role="presentation" width="100%" style="border-collapse:collapse;min-width:100%;width:100%!important" cellpadding="0" cellspacing="0" border="0">
                    <tbody><tr>
                        <td width="100%" height="53" bgcolor="#0b0c0c">
                            <table role="presentation" width="100%" style="border-collapse:collapse;max-width:580px" cellpadding="0" cellspacing="0" border="0" align="center">
                                <tbody><tr>
                                    <td width="70" bgcolor="#0b0c0c" valign="middle" style="padding: 10px 20px;">
                                        <span style="font-family:Helvetica,Arial,sans-serif;font-size:28px;font-weight:700;color:#ffffff;text-decoration:none">Confirm your email</span>
                                    </td>
                                </tr></tbody>
                            </table>
                        </td>
                    </tr></tbody>
                </table>
                <table role="presentation" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse:collapse;max-width:580px;width:100%!important" width="100%">
                    <tbody><tr><td height="30"><br></td></tr>
                    <tr>
                        <td width="10" valign="middle"><br></td>
                        <td style="font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px">
                            <p style="margin:0 0 20px 0;color:#0b0c0c">Hi %s,</p>
                            <p style="margin:0 0 20px 0;color:#0b0c0c">Thank you for registering. Use the following verification code to activate your account:</p>
                            <p style="margin:0 0 20px 0;font-size:32px;font-weight:bold;color:#1D70B8;text-align:center;letter-spacing: 5px;">%d</p>
                            <p style="margin:0 0 20px 0;color:#0b0c0c">Token will expire in 15 minutes.</p>
                            <p>See you soon!</p>
                        </td>
                        <td width="10" valign="middle"><br></td>
                    </tr>
                    <tr><td height="30"><br></td></tr>
                </tbody></table>
            </div>
            """.formatted(name, token);
    }

    public static String buildCancelBookingEmail(
            CancellationResponse cancel,
            BookingResponse booking
    ) {
        StringBuilder itemsHtml = new StringBuilder();

        for (var item : booking.getBookingItems()) {
            itemsHtml.append(String.format("""
            <tr>
                <td style="padding: 10px; border-bottom: 1px solid #eee;">
                    <strong>Room Type ID: %d</strong><br>
                    <span style="font-size: 14px; color: #666;">Rate Plan: %s</span>
                </td>
                <td style="padding: 10px; border-bottom: 1px solid #eee; text-align: right;">
                    %d x %,.0f VND
                </td>
            </tr>
            """,
                    item.getRoomTypeId(),
                    item.getRatePlan().getName(),
                    item.getQuantity(),
                    item.getAmount()
            ));
        }

        return """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px;">
            
            <h2 style="color: #D32F2F; text-align: center;">
                Booking Cancelled
            </h2>

            <p>Hi <strong>%s</strong>,</p>

            <p>
                Your booking has been <strong>successfully cancelled</strong>.
                Below are the details of your cancelled booking and refund information.
            </p>

            <div style="background-color: #f9f9f9; padding: 15px; border-radius: 5px;">
                <p><strong>Booking Reference:</strong> %s</p>
                <p><strong>Check-in:</strong> %s</p>
                <p><strong>Check-out:</strong> %s</p>
                <p><strong>Cancelled By:</strong> %s</p>
                <p><strong>Cancellation Time:</strong> %s</p>
                <p><strong>Reason:</strong> %s</p>
            </div>

            <table style="width: 100%%; border-collapse: collapse; margin-top: 20px;">
                <thead>
                    <tr style="background-color: #eee;">
                        <th style="text-align: left; padding: 10px;">Item</th>
                        <th style="text-align: right; padding: 10px;">Price</th>
                    </tr>
                </thead>
                <tbody>
                    %s
                </tbody>
            </table>

            <div style="margin-top: 20px;">
                <p><strong>Total Amount:</strong> %,.0f VND</p>
                <p><strong>Cancellation Fee:</strong> %,.0f VND</p>
                <p><strong>Refund Amount:</strong> 
                    <span style="color: #2E7D32; font-weight: bold;">
                        %,.0f VND
                    </span>
                </p>
                <p><strong>Status:</strong> %s</p>
            </div>

            <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">

            <p style="font-size: 12px; color: #888; text-align: center;">
                We’re sorry to see your booking cancelled.  
                If you need assistance, contact us at leekhoa0409@gmail.com.
            </p>

        </div>
        """.formatted(
                booking.getGuest().getName(),
                booking.getBookingReference(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                cancel.getCanceledBy(),
                cancel.getCancellationTime(),
                cancel.getCancellationReason(),
                itemsHtml.toString(),
                booking.getTotalAmount(),
                cancel.getCancellationFee(),
                cancel.getRefundAmount(),
                cancel.getStatus()
        );
    }

}