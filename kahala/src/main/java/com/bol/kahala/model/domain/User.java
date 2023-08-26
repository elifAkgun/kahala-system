package com.bol.kahala.model.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String userId;
    @NotEmpty(message = "{NotEmpty.userRequest.userName}")
    private String userName;
    private String password;

}
