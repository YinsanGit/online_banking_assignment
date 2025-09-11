package com.finalBanking.demo.Service;

import com.finalBanking.demo.Dto.userRegister;
import com.finalBanking.demo.Entity.User;



public interface userService {
    User registerUser(userRegister userRegister);
    boolean existsByEmail(String email);


}
