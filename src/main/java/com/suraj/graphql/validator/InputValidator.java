package com.suraj.graphql.validator;


import java.io.File;

public class InputValidator {
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
            throws IllegalArgumentException {
        validateSchema(schema);
        validateOutputDir(outputDir);
        validatePackageName(packageName);
    }

    private void validateSchema(final File schema) {
        String path = "";
        if (schema == null || !schema.exists()) {
            if (schema != null) {
                path = schema.getPath();
            }
            throw new IllegalArgumentException("Invalid schema directory : " + path);
        }
    }

    private void validateOutputDir(final File outputDirectory) {
        String path = "";
        if (outputDirectory == null || !outputDirectory.exists()) {
            if (outputDirectory != null) {
                path = outputDirectory.getPath();
            }
            throw new IllegalArgumentException("Invalid outputDirectory : " + path);
        }
    }

    private void validatePackageName(final String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid packageName : " + packageName);
        }
    }
}
