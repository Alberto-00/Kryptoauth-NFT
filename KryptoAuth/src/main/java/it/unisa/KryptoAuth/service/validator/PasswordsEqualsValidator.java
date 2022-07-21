package it.unisa.KryptoAuth.service.validator;

import it.unisa.KryptoAuth.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsEqualsValidator implements ConstraintValidator<PasswordsEquals, Object> {

    private String message;

    @Override
    public void initialize(PasswordsEquals arg0) {
        this.message = arg0.message();
    }

    @Override
    public boolean isValid(Object candidate, ConstraintValidatorContext arg1) {
        User user = (User) candidate;
        boolean isValid;

        if (user.getRepeatPassword() == null || user.getPassword() == null)
            isValid = false;
        else
            isValid = user.getPassword().compareTo(user.getRepeatPassword()) == 0;

        if (!isValid) {
            arg1.disableDefaultConstraintViolation();
            arg1.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("password")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
