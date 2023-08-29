package com.bol.kahala.service.output;

import com.bol.kahala.model.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A data class representing the output of the user creation process.
 * It contains the resulting user instance after creation.
 */
@Getter
@Builder
@ToString
public class CreateUserServiceOutput {

    /**
     * The user instance that was created.
     */
    private User user;
}
