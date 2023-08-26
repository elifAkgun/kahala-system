package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserServiceOutput {
    private User user;
}
