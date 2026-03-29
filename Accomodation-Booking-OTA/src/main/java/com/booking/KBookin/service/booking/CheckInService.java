package com.booking.KBookin.service.booking;

import com.booking.KBookin.dto.checkin.CheckinResponse;

public interface CheckInService {
    CheckinResponse handleCheckin(Long bookingId);
}
