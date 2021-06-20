package com.mastersessay.blockchain.accounting.dto.request.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
public class SignupRequest implements Serializable {
    @NotBlank
    @Size(min = 3, max = 256)
    private String username;

    @NotBlank
    @Size(max = 256)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 256)
    private String password;

    @Override
    public String toString() {
        return "SignupRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
