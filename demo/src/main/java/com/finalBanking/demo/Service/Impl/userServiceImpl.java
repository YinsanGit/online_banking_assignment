package com.finalBanking.demo.Service.Impl;

import com.finalBanking.demo.Dto.userRegister;
import com.finalBanking.demo.Entity.User;
import com.finalBanking.demo.Repository.userRepository;
import com.finalBanking.demo.Service.userService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;

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
        user.setFirstName(userRegister.getUsername());
        user.setLastName(userRegister.getUsername());
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setCreateDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}