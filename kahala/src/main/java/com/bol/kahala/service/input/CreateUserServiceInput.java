package com.bol.kahala.service.input;

import com.bol.kahala.model.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateUserServiceInput {

    private User user;
}
