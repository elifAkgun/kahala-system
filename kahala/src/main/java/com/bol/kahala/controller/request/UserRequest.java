package com.bol.kahala.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class UserRequest implements Serializable {
    @NotEmpty(message = "{NotEmpty.userRequest.user.userName}")
    private String userName;
    @NotEmpty(message = "{NotEmpty.userRequest.user.password}")
    private String password;

}
