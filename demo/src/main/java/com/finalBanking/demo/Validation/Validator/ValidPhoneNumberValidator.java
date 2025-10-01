package com.finalBanking.demo.Validation.Validator;

import com.finalBanking.demo.Validation.Anotetion.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }

        // Example: allows +, digits, spaces, dashes; length 10–15
        return phoneNumber.matches("^(\\+)?[0-9\\-\\s]{10,15}$");
    }
}
