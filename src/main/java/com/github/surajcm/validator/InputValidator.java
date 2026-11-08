package com.github.surajcm.validator;


import com.github.surajcm.exception.ValidationException;

import java.io.File;

/**
 * Validates input parameters for the GraphQL POJO generation process.
 *
 * <p>This class provides validation methods to ensure that all required inputs
 * (schema file, output directory, and package name) are valid before the
 * generation process begins.</p>
 *
 * <p>This class follows the singleton pattern. Use {@link #getInstance()} to obtain
 * an instance.</p>
 *
 * @author Suraj Muraleedharan
 * @since 1.0.0
 */
public final class InputValidator {

    private static InputValidator instance;

    private InputValidator() {
    }

    /**
     * Returns the singleton instance of InputValidator.
     *
     * @return the InputValidator singleton instance
     */
    public static synchronized InputValidator getInstance() {
        if (instance == null) {
            instance = new InputValidator();
        }
        return instance;
    }

    /**
     * Validates all input parameters for POJO generation.
     *
     * <p>This method validates the schema file, output directory, and package name
     * by delegating to individual validation methods.</p>
     *
     * @param schema the GraphQL schema file to validate
     * @param outputDir the output directory to validate
     * @param packageName the Java package name to validate
     * @throws ValidationException if any of the inputs are invalid
     */
    public void validateInputs(final File schema, final File outputDir, final String packageName)
            throws ValidationException {
        validateSchema(schema);
        validateOutputDirectory(outputDir);
        validatePackageName(packageName);
    }

    /**
     * Validates the GraphQL schema file.
     *
     * <p>Checks that the schema file is not null, exists, and is not empty.</p>
     *
     * @param schema the GraphQL schema file to validate
     * @throws ValidationException if the schema is null, does not exist, or is empty
     */
    public void validateSchema(final File schema) throws ValidationException {
        if (schema == null) {
            throw new ValidationException("Invalid schema directory : null");
        }
        if (schema.length() == 0 || !schema.exists()) {
            String path = schema.getPath();
            throw new ValidationException("Invalid schema directory : " + path);
        }
    }

    /**
     * Validates the output directory.
     *
     * <p>Checks that the output directory is not null and exists.</p>
     *
     * @param outputDirectory the output directory to validate
     * @throws ValidationException if the output directory is null or does not exist
     */
    public void validateOutputDirectory(final File outputDirectory) throws ValidationException {
        if (outputDirectory == null) {
            throw new ValidationException("Invalid outputDirectory : null");
        }
        if (outputDirectory.length() == 0 || !outputDirectory.exists()) {
            String path = outputDirectory.getPath();
            throw new ValidationException("Invalid outputDirectory : " + path);
        }
    }

    /**
     * Validates the Java package name.
     *
     * <p>Checks that the package name is not null or empty.</p>
     *
     * @param packageName the Java package name to validate
     * @throws ValidationException if the package name is null or empty
     */
    public void validatePackageName(final String packageName) throws ValidationException {
        if (packageName == null || packageName.trim().isEmpty()) {
            throw new ValidationException("Invalid packageName : " + packageName);
        }
    }
}
