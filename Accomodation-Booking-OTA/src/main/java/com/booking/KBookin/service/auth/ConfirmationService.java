package com.booking.KBookin.service.auth;
import com.booking.KBookin.entity.auth.ConfirmationToken;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.repository.auth.ConfirmationTokenRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class ConfirmationService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    public long createNewEmailVerficationToken(User user) {
        Random random = new Random();
        long token = 100000 + random.nextInt(900000);
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(30)) // Set expiresAt to 30 minutes
                .build();
        this.confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    public ConfirmationToken getConfirmationByTokenAndEmail(long token,String email){
        Optional<ConfirmationToken> storeToken = this.confirmationTokenRepository.findByTokenAndUserEmail(token,email);
        if(!storeToken.isPresent()){
            throw new EntityNotFoundException("Token not found");
        }
        return storeToken.get();
    }

    public void confirmToken(long tokenNumber,String email) {
        this.confirmationTokenRepository.updateConfirmedAt(tokenNumber, LocalDateTime.now(),email);
    }
}
