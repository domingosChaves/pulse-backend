package com.domingos.pulse_backend.fabricante.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public void initialize(CNPJ constraintAnnotation) {
        // no-op
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // @NotBlank tratará null/empty
        String cnpj = value.replaceAll("\\D", "");
        if (cnpj.length() != 14) return false;
        // reject known invalid sequences
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        try {
            int[] digits = new int[14];
            for (int i = 0; i < 14; i++) digits[i] = Character.digit(cnpj.charAt(i), 10);

            // first check digit
            int sum = 0;
            int[] weight1 = {5,4,3,2,9,8,7,6,5,4,3,2};
            for (int i = 0; i < 12; i++) sum += digits[i] * weight1[i];
            int mod = sum % 11;
            int check1 = (mod < 2) ? 0 : 11 - mod;
            if (check1 != digits[12]) return false;

            // second check digit
            sum = 0;
            int[] weight2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};
            for (int i = 0; i < 13; i++) sum += digits[i] * weight2[i];
            mod = sum % 11;
            int check2 = (mod < 2) ? 0 : 11 - mod;
            return check2 == digits[13];
        } catch (Exception ex) {
            return false;
        }
    }
}

