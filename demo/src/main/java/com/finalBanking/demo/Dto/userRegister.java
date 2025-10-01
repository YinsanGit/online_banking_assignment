package com.finalBanking.demo.Dto;


import com.finalBanking.demo.Validation.Anotetion.ValidPhoneNumber;
import com.finalBanking.demo.Validation.Anotetion.ValidUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class userRegister {
//    @NotBlank
//    @Size(min = 3, max = 20)
    @ValidUsername
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

//    @ValidPhoneNumber
    private String phoneNumber;
}
