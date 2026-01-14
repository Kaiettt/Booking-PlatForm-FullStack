package com.booking.KBookin.service.auth;

import com.booking.KBookin.dto.auth.GoogleUserInfo;

public interface OauthVerifier {
    GoogleUserInfo verify(String idToken);
}
