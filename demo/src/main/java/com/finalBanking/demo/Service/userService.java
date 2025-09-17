package com.finalBanking.demo.Service;

import com.finalBanking.demo.Dto.userRegister;
import com.finalBanking.demo.Entity.User;
import org.springframework.data.domain.Page;


public interface userService {
    User registerUser(userRegister userRegister);
    boolean existsByEmail(String email);
    Page<User> getAllUsers(int page, int size);

}
