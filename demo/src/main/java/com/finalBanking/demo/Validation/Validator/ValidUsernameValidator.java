package com.finalBanking.demo.Validation.Validator;

import com.finalBanking.demo.Validation.Anotetion.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        // Example: username must be at least 4 characters and start with a letter
        return username.matches("^[A-Za-z][A-Za-z0-9_]{3,}$");
    }

}
