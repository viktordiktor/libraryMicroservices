package com.nikonenko.authservice.services;


import com.nikonenko.authservice.dto.AuthRequest;
import com.nikonenko.authservice.dto.AuthResponse;
import com.nikonenko.authservice.dto.RefreshRequest;
import com.nikonenko.authservice.models.Token;
import com.nikonenko.authservice.models.User;
import com.nikonenko.authservice.repositories.TokenRepository;
import com.nikonenko.authservice.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialExpiredException;
import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public AuthResponse register(AuthRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new EntityExistsException();
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        User user = modelMapper.map(request, User.class);
        user.setRole("ROLE_USER");
        userRepository.save(user);

        String accessToken = jwtUtil.generate(user.getId(), user.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(user.getId(), user.getRole(), "REFRESH");

        tokenRepository.save(Token.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .user(user)
                        .build());

        log.info("User {} registered", user.getId());

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshTokens(RefreshRequest refreshRequest) throws CredentialExpiredException {
        String refreshToken = refreshRequest.getRefreshToken();

        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new EntityNotFoundException("Refresh token not found"));

        if (jwtUtil.getExpirationDate(refreshToken).before(new Date())) {
            throw new CredentialExpiredException();
        }

        User user = token.getUser();

        String newAccessToken = jwtUtil.generate(user.getId(), user.getRole(), "ACCESS");
        String newRefreshToken = jwtUtil.generate(user.getId(), user.getRole(), "REFRESH");

        token.setAccessToken(newAccessToken);
        token.setRefreshToken(newRefreshToken);
        tokenRepository.save(token);

        log.info("Access token refreshed");

        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
