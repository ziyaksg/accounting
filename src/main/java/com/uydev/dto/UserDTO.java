package com.uydev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;

    private String firstname;


    private String lastname;

    private String phone;

    private RoleDTO role;

    private CompanyDTO company;

    private boolean enabled = true;

    private boolean isOnlyAdmin; //(should be true if this user is only admin of any company.) I will write in business logic part

    public void setPassword(String password) {
        this.password = password;
        checkConfirmPassword();
    }

    private void checkConfirmPassword() {
        if (this.password == null || this.confirmPassword == null) {
            return;
        } else if (!this.password.equals(this.confirmPassword)) {
            this.confirmPassword = null;
        }
    }
}
