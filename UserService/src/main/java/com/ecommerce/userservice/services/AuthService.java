package com.ecommerce.userservice.services;

import com.ecommerce.userservice.dtos.AuthResponseDto;
import com.ecommerce.userservice.dtos.UserDto;
import com.ecommerce.userservice.models.Session;
import com.ecommerce.userservice.models.SessionStatus;
import com.ecommerce.userservice.models.User;
import com.ecommerce.userservice.repositories.SessionRepository;
import com.ecommerce.userservice.repositories.UserRepository;
import com.ecommerce.userservice.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       SessionRepository sessionRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserDto signUp(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists with email: " + email);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

    public AuthResponseDto login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user);

        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setExpiringAt(new Date(System.currentTimeMillis() + 259200000L));
        sessionRepository.save(session);

        return new AuthResponseDto(token, UserDto.from(user));
    }

    public void logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty()) {
            throw new RuntimeException("Session not found");
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty()) {
            return SessionStatus.ENDED;
        }
        Session session = sessionOptional.get();
        if (session.getSessionStatus() == SessionStatus.ENDED) {
            return SessionStatus.ENDED;
        }
        if (!jwtService.isTokenValid(token)) {
            session.setSessionStatus(SessionStatus.ENDED);
            sessionRepository.save(session);
            return SessionStatus.ENDED;
        }
        return SessionStatus.ACTIVE;
    }
}
