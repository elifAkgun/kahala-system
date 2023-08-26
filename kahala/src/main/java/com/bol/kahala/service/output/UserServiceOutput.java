package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserServiceOutput {
    private User user;
}
