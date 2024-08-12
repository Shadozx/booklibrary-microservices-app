package com.shadoww.api.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PersonRequest {

    private String email;

    private String username;

    private String password;

    private String role;

    public boolean isEmpty() {
        return isEmailEmpty() && isUsernameEmpty() && isPasswordEmpty() && isRoleEmpty();
    }

    public boolean isEmailEmpty() {
        return email == null || email.equals("");
    }
    public boolean isUsernameEmpty() {
        return username == null || username.equals("");
    }

    public boolean isPasswordEmpty() {
        return password == null || password.equals("");
    }

    public boolean isRoleEmpty() {
        return role == null;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                "role=" + role +
                '}';
    }
}