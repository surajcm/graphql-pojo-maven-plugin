package com.suraj.graphql.validator;


import com.suraj.graphql.exception.ValidationException;

import java.io.File;

public final class InputValidator {
    private static InputValidator INSTANCE;

    private InputValidator() {
    }

    public static synchronized InputValidator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InputValidator();
        }
        return INSTANCE;
    }

    public void validateInputs(final File schema, final File outputDir, final String packageName)
            throws ValidationException {
        validateSchema(schema);
        validateOutputDir(outputDir);
        validatePackageName(packageName);
    }

    private void validateSchema(final File schema) throws ValidationException {
        String path = "";
        if (schema == null || !schema.exists()) {
            if (schema != null) {
                path = schema.getPath();
            }
            throw new ValidationException("Invalid schema directory : " + path);
        }
    }

    private void validateOutputDir(final File outputDirectory) throws ValidationException {
        String path = "";
        if (outputDirectory == null || !outputDirectory.exists()) {
            if (outputDirectory != null) {
                path = outputDirectory.getPath();
            }
            throw new ValidationException("Invalid outputDirectory : " + path);
        }
    }

    private void validatePackageName(final String packageName) throws ValidationException {
        if (packageName == null || packageName.trim().isEmpty()) {
            throw new ValidationException("Invalid packageName : " + packageName);
        }
    }
}
