package com.app.FlyWise.controller;

import com.app.FlyWise.dto.LoginResponse;
import com.app.FlyWise.dto.UserDto;
import com.app.FlyWise.model.User;
import com.app.FlyWise.repository.RefreshTokenRepository;
import com.app.FlyWise.service.AuthService;
import com.app.FlyWise.service.JwtService;
import com.app.FlyWise.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    public AuthController(AuthService authService,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService,
                          RefreshTokenRepository refreshTokenRepository) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestParam String username,
                                            @RequestParam String email,
                                            @RequestParam String password) {
        User user = authService.register(username, email, password);
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String username,
                                               @RequestParam String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String accessToken = jwtService.generateToken(username);
            String refreshToken = refreshTokenService.createRefreshToken(username).getToken();

            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/useremail")
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {
        User user = authService.findByUsername(username);
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestParam String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .filter(refreshTokenService::isValid)
                .map(token -> {
                    String newAccessToken = jwtService.generateToken(token.getUser().getUsername());
                    return ResponseEntity.ok(
                            new LoginResponse(newAccessToken, token.getToken())
                    );
                })
                .orElseGet(() -> ResponseEntity.status(403).body(null));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String username) {
        User user = authService.findByUsername(username);
        refreshTokenService.deleteByUser(user);
        return ResponseEntity.ok("User logged out successfully.");
    }
}
