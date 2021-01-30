package com.suraj.graphql.validator;


import java.io.File;

public class InputValidator {
    private static InputValidator INSTANCE;

    private InputValidator() {
    }

    public synchronized static InputValidator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InputValidator();
        }
        return INSTANCE;
    }

    public void validateInputs(File schema, File outputDir, String packageName)
            throws IllegalArgumentException {
        validateSchema(schema);
        validateOutputDir(outputDir);
        validatePackageName(packageName);
    }

    private void validateSchema(File schema) {
        String path = "";
        if (schema == null || !schema.exists()) {
            if (schema != null) {
                path = schema.getPath();
            }
            throw new IllegalArgumentException("Invalid schema directory : " + path);
        }
    }

    private void validateOutputDir(File outputDirectory) {
        String path = "";
        if (outputDirectory == null || !outputDirectory.exists()) {
            if (outputDirectory != null) {
                path = outputDirectory.getPath();
            }
            throw new IllegalArgumentException("Invalid outputDirectory : " + path);
        }
    }

    private void validatePackageName(String packageName) {
        if (packageName == null || packageName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid packageName : " + packageName);
        }
    }
}
