package com.github.surajcm.validator;


import com.github.surajcm.exception.ValidationException;

import java.io.File;

public final class InputValidator {
    private static InputValidator instance;

    private InputValidator() {
    }

    public static synchronized InputValidator getInstance() {
        if (instance == null) {
            instance = new InputValidator();
        }
        return instance;
    }

    public void validateInputs(final File schema, final File outputDir, final String packageName)
            throws ValidationException {
        validateSchema(schema);
        validateOutputDir(outputDir);
        validatePackageName(packageName);
    }

    private void validateSchema(final File schema) throws ValidationException {
        if (schema == null) {
            throw new ValidationException("Invalid schema directory : null");
        }
        if (schema.length() == 0 || !schema.exists()) {
            String path = schema.getPath();
            throw new ValidationException("Invalid schema directory : " + path);
        }
    }

    private void validateOutputDir(final File outputDirectory) throws ValidationException {
        if (outputDirectory == null) {
            throw new ValidationException("Invalid outputDirectory : null");
        }
        if (outputDirectory.length() == 0 || !outputDirectory.exists()) {
            String path = outputDirectory.getPath();
            throw new ValidationException("Invalid outputDirectory : " + path);
        }
    }

    private void validatePackageName(final String packageName) throws ValidationException {
        if (packageName == null || packageName.trim().isEmpty()) {
            throw new ValidationException("Invalid packageName : " + packageName);
        }
    }
}
