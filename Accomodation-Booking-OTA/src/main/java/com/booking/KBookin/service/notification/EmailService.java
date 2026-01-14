package com.booking.KBookin.service.notification;

import com.booking.KBookin.dto.booking.BookingResponse;
import com.booking.KBookin.dto.booking.CancellationResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.mail.display-name}")
    private String displayName;

    @Async
    public void sendToken(String to, String userName, long token) {
        String emailContent = EmailTemplate.buildVerificationEmail(userName, token);
        sendHtmlEmail(to, "Confirm your email.", emailContent);
    }

    @Async
    public void sendBookingSuccess(BookingResponse bookingResponse) {
        String to = bookingResponse.getGuest().getEmail();
        String emailContent = EmailTemplate.buildBookingSuccessEmail(bookingResponse);
        String subject = "Booking Confirmation - " + bookingResponse.getBookingReference();

        sendHtmlEmail(to, subject, emailContent);
    }


    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(htmlContent, true);
            helper.setTo(to);
            helper.setSubject(subject);

            helper.setFrom(fromEmail, displayName);

            mailSender.send(mimeMessage);
            LOGGER.info("Email sent to: {} with subject: {}", to, subject);

        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.error("Failed to send email to {}", to, e);
        }
    }
    @Async
    public void sendCancelBookingSuccess(CancellationResponse cancelResponse, BookingResponse bookingResponse) {
        String to = bookingResponse.getGuest().getEmail();
        String subject = "Booking Cancelled - " + bookingResponse.getBookingReference();

        String emailContent = EmailTemplate.buildCancelBookingEmail(cancelResponse, bookingResponse);

        sendHtmlEmail(to, subject, emailContent);
    }


}