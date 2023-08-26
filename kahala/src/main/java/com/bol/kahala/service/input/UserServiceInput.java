package com.bol.kahala.service.input;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class UserServiceInput {
    private String userId;
}
