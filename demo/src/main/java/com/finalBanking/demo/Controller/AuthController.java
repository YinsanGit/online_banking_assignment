package com.finalBanking.demo.Controller;


import com.finalBanking.demo.Dto.loginRequest;
import com.finalBanking.demo.Dto.userRegister;
import com.finalBanking.demo.Entity.User;
import com.finalBanking.demo.Jwt.JwtTokenService;
import com.finalBanking.demo.Repository.permissionRepository;
import com.finalBanking.demo.Repository.roleRepository;
import com.finalBanking.demo.Repository.userRepository;
import com.finalBanking.demo.Service.userService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final JwtTokenService jwtTokenService;

    private final userService userService;
    private final userRepository userRepository;
    private final permissionRepository permissionRepository;
    private final roleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody userRegister req) {
        if (userService.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }
        // Ensure password is encoded in your service implementation; if not, encode here:
        // req = new userRegister(..., passwordEncoder.encode(req.getPassword()), ...);

        User created = userService.registerUser(req);

        // Optionally grant default role USER if exists
        roleRepository.findByName("USER").ifPresent(role -> {
            created.getRoles().add(role);
            userRepository.save(created);
        });

        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody loginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String token = jwtTokenService.generateToken(userDetails);
            Date expiresAt = jwtTokenService.extractExpiration(token);

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", token,
                    "expiresAt", expiresAt.getTime(),
                    "user", userDetails.getUsername()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "User account is disabled"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Authentication failed"));
        }
    }

}

