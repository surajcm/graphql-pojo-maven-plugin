package com.suraj.graphql.validator;

import com.suraj.graphql.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InputValidatorTest {
    private final InputValidator inputValidator = InputValidator.getInstance();

    @Test
    void validateSchema() {
        Assertions.assertThrows(ValidationException.class, () ->
                inputValidator.validateInputs(null,null,null));
    }


}