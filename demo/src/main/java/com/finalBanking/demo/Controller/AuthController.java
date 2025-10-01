package com.finalBanking.demo.Controller;


import com.finalBanking.demo.Dto.loginRequest;
import com.finalBanking.demo.Dto.userRegister;
import com.finalBanking.demo.Entity.User;
import com.finalBanking.demo.Exception.ApiResponseUtil;
import com.finalBanking.demo.Exception.CustomErrorException;
import com.finalBanking.demo.Jwt.JwtTokenService;
import com.finalBanking.demo.Repository.roleRepository;
import com.finalBanking.demo.Repository.userRepository;
import com.finalBanking.demo.Security.AuthenticationService;
import com.finalBanking.demo.Service.userService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import org.springframework.web.bind.annotation.*;

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
    private final AuthenticationService authenticationService;
    private final roleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody userRegister req) {
        if (userService.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }
        User created = userService.registerUser(req);

        // Optionally grant default role USER if exists
        roleRepository.findByName("USER").ifPresent(role -> {
            created.getRoles().add(role);
            userRepository.save(created);
        });

        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody loginRequest request) {
        try {
            String token = authenticationService.authenticate(request.email(), request.password());
            Date expiresAt = jwtTokenService.extractExpiration(token);

            return ResponseEntity.ok(ApiResponseUtil.successResponse(
                    Map.of(
                            "message", "Login successful",
                            "token", token,
                            "expiresAt", expiresAt.getTime(),
                            "user", request.email()
                    )
            ));

        } catch (BadCredentialsException e) {
            // Invalid email OR password
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseUtil.errorResponse(
                            "Invalid email or password",
                            Map.of("email", request.email())
                    ));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseUtil.errorResponse(
                            "User account is disabled",
                            Map.of("email", request.email())
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseUtil.errorResponse(
                            "Authentication failed",
                            Map.of("error", e.getMessage())
                    ));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    private ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,   // Default page number is 0 if not provided
            @RequestParam(defaultValue = "10") int size   // Default size is 10 if not provided
    ) {
        return userService.getAllUsers(page, size);

    }
}

