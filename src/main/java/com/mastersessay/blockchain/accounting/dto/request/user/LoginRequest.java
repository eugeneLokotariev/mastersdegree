package com.mastersessay.blockchain.accounting.dto.request.user;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class LoginRequest implements Serializable {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                '}';
    }
}
