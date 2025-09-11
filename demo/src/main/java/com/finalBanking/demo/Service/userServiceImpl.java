package com.finalBanking.demo.Service;

import com.finalBanking.demo.Dto.userRegister;
import com.finalBanking.demo.Entity.User;
import com.finalBanking.demo.Repository.userRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class userServiceImpl implements userService {
    private final userRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public userServiceImpl(userRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public User registerUser(userRegister userRegister) {
        if (existsByEmail(userRegister.getEmail())) {
            throw new RuntimeException("Email is already exit");
        }

        User user = new User();
        user.setUsername(userRegister.getUsername());
        user.setEmail(userRegister.getEmail());
        user.setPhoneNumber(userRegister.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}