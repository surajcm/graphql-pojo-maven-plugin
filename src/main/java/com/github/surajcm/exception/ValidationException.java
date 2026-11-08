package com.github.surajcm.exception;

/**
 * Exception thrown when validation of plugin inputs fails.
 *
 * <p>This exception is thrown by {@link com.github.surajcm.validator.InputValidator}
 * when one or more input parameters (schema file, output directory, or package name)
 * fail validation checks.</p>
 *
 * <p>Common validation failures include:</p>
 * <ul>
 *     <li>Schema file is null or does not exist</li>
 *     <li>Output directory is null or does not exist</li>
 *     <li>Package name is null or empty</li>
 * </ul>
 *
 * @author Suraj Muraleedharan
 * @since 1.0.0
 * @see com.github.surajcm.validator.InputValidator
 */
public class ValidationException extends Exception {

    private static final long serialVersionUID = 42L;

    /**
     * Constructs a new ValidationException with the specified error message.
     *
     * @param errorMessage the detail message describing the validation failure
     */
    public ValidationException(final String errorMessage) {
        super(errorMessage);
    }
}