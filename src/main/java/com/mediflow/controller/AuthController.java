package com.mediflow.controller;

import com.mediflow.dto.AuthDto;
import com.mediflow.entity.User;
import com.mediflow.repository.UserRepository;
import com.mediflow.security.CustomUserDetailsService;
import com.mediflow.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService uds;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager authManager,
                          CustomUserDetailsService uds,
                          JwtUtil jwtUtil,
                          UserRepository userRepo,
                          PasswordEncoder encoder) {
        this.authManager = authManager;
        this.uds = uds;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto.AuthResponse> login(
            @RequestBody AuthDto.LoginRequest req) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                req.getUsername(), req.getPassword())
        );
        var ud = uds.loadUserByUsername(req.getUsername());
        String token = jwtUtil.generateToken(ud);
        String role = ud.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");
        return ResponseEntity.ok(
            new AuthDto.AuthResponse(token, req.getUsername(), role));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody AuthDto.RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }
        userRepo.save(User.builder()
            .username(req.getUsername())
            .password(encoder.encode(req.getPassword()))
            .role(User.Role.valueOf(req.getRole().toUpperCase()))
            .build());
        return ResponseEntity.ok("User registered successfully");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody AuthDto.ChangePasswordRequest req) {
        // Verify old password first
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    req.getUsername(), req.getOldPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Current password is incorrect");
        }

        // Update with new password
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(encoder.encode(req.getNewPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(
            @PathVariable String username) {
        if (!userRepo.existsByUsername(username)) {
            return ResponseEntity.badRequest()
                    .body("User not found");
        }
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }
}