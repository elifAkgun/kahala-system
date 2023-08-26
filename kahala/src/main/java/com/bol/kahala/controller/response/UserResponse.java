package com.bol.kahala.controller.response;

import com.bol.kahala.model.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class UserResponse implements Serializable {
    private User user;
}
