package com.bol.kahala.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A utility class to retrieve validation error messages from the message source.
 */
@Component
public class ValidationMessagesUtil {

    private MessageSourceAccessor messageSourceAccessor;

    /**
     * Constructs a new ValidationMessagesUtil instance with the provided MessageSource.
     *
     * @param messageSource The MessageSource used to retrieve validation error messages.
     */
    public ValidationMessagesUtil(MessageSource messageSource) {
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }

    /**
     * Retrieves a validation error message using the provided message code and parameters.
     *
     * @param code      The message code that represents the validation error.
     * @param parameter Optional parameters to be included in the message.
     * @return The formatted validation error message.
     */
    public String getExceptionMessage(String code, String... parameter) {
        return messageSourceAccessor.getMessage(code, List.of(parameter).toArray());
    }
}

