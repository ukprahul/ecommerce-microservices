package com.waelsworld.userservice.services;

import com.waelsworld.userservice.Dto.UserDto;

import com.waelsworld.userservice.Repositories.SessionRepository;
import com.waelsworld.userservice.Repositories.UserRepository;
import com.waelsworld.userservice.exceptions.InvalidPasswordException;
import com.waelsworld.userservice.exceptions.UserDoesNotExistsException;
import com.waelsworld.userservice.models.Role;
import com.waelsworld.userservice.models.Session;
import com.waelsworld.userservice.models.SessionStatus;
import com.waelsworld.userservice.models.User;
import com.waelsworld.userservice.exceptions.UserAlreadyExistsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public ResponseEntity<UserDto> login(String email, String password) throws UserDoesNotExistsException, InvalidPasswordException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistsException("User with email: " + email + " not found.");
        }

        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect");
        }

        List<String> roleNames = user.getRoles() == null ? List.of() :
                user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());

        String token = Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roleNames)
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                .signWith(getSigningKey())
                .compact();

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token=" + token + "; Path=/; HttpOnly");

        UserDto userDto = UserDto.from(user);

        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.INACTIVE);
        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    public UserDto signUp(String email, String password) throws UserAlreadyExistsException {
        User userOptional = userRepository.findByEmail(email).orElse(null);
        if (userOptional != null) {
            throw new UserAlreadyExistsException("User with email: " + email + " already exists.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return sessionOptional.map(Session::getSessionStatus).orElse(null);
    }
}
