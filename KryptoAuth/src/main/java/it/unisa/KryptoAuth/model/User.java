package it.unisa.KryptoAuth.model;

import it.unisa.KryptoAuth.service.validator.PasswordsEquals;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@PasswordsEquals(message = "Passwords non concidono.")
public class User {
    @NotNull(message = "Email o Username vuoti.")
    @NotBlank(message = "Email o Username vuoti.")
    @Pattern(regexp = "^.{3,}$", message = "Email or Username must have at last 3 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,}[a-zA-Z0-9]$|^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
            flags = Pattern.Flag.UNICODE_CASE,
            message = "Email or Username field wrong.")
    private String email;

    @NotNull(message = "Password vuota.")
    @NotBlank(message = "Password vuota.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[=^ ì{}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-])[A-Za-z\\d=^ ì{}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-]{8,}$",
            flags = Pattern.Flag.UNICODE_CASE, message = "Password errata.")
    private String password;

    @NotNull(message = "Ripeti Password vuota.")
    @NotBlank(message = "Ripeti Password vuota.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[=^ ì{}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-])[A-Za-z\\d=^ ì{}()£/+çò°àù§èé#@$!%€*?&:,;'._<>|-]{8,}$",
            flags = Pattern.Flag.UNICODE_CASE, message = "Ripeti Password errata.")
    private String repeatPassword;

    @NotNull
    private String role;

    public User(){}

    public User(String email, String password, String repeatPassword, String role) {
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.role = role;
    }

    public User(String email, String password, String repeatPassword) {
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
