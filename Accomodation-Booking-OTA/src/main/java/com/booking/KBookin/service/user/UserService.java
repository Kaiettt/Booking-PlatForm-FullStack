package com.booking.KBookin.service.user;

import com.booking.KBookin.config.Common;
import com.booking.KBookin.entity.user.User;
import com.booking.KBookin.enumerate.user.UserRole;
import com.booking.KBookin.repository.user.UserRepository;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    public User getUserByUserName(String username) {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityExistsException(Common.USER_NOT_FOUND));
    }
    public User getUserByEmail(String email) {
        Optional<User> user =  this.userRepository.findByEmail(email);
        return user.isPresent() ?user.get():null;
    }
    public User getUserById(long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException(Common.USER_NOT_FOUND));
    }
    public User updateUser(User user) {
        return this.userRepository.save(user);
    }

    public User getUserByRefreshToken(String refresh_token) {
        return this.userRepository.findByRefreshToken(refresh_token)
                .orElseThrow(() -> new EntityExistsException(Common.REFRESH_TOKEN_NOT_FOUND));
    }

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }
}
