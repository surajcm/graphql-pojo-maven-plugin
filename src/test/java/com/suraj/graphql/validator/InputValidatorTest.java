package com.suraj.graphql.validator;

import com.suraj.graphql.exception.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class InputValidatorTest {
    private final InputValidator inputValidator = InputValidator.getInstance();
    private File schemaFile;
    private File outputFile;

    @BeforeEach
    public void setup() {
        try {
            String tmpDirsLocation = System.getProperty("java.io.tmpdir");
            schemaFile = File.createTempFile(tmpDirsLocation + "temp", "graphqls");
            outputFile = File.createTempFile(tmpDirsLocation + "temp", "java");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        schemaFile.deleteOnExit();
        outputFile.deleteOnExit();
    }

    @Test
    void validateNullSchema() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () ->
                inputValidator.validateInputs(null, null, null));
        Assertions.assertTrue(thrown.getMessage().contains("Invalid schema directory"));
    }

    @Test
    void validateIncorrectSchema() {
        File abc = new File("/abc");
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () ->
                inputValidator.validateInputs(abc, null, null));
        Assertions.assertTrue(thrown.getMessage().contains("Invalid schema directory"));
    }

    @Test
    void validateEmptySchema() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () ->
                inputValidator.validateInputs(schemaFile, null, null));
        Assertions.assertTrue(thrown.getMessage().contains("Invalid schema directory"));
    }

    @Test
    void validateSchema() throws IOException {
        writeSchemaFile();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () ->
                inputValidator.validateInputs(schemaFile, null, null));
        Assertions.assertTrue(thrown.getMessage().contains("Invalid outputDirectory"));

    }

    @Test
    void validateIncorrectOutputDir() throws IOException {
        writeSchemaFile();
        File abc = new File("/abc");
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () ->
                inputValidator.validateInputs(schemaFile, abc, null));
        Assertions.assertTrue(thrown.getMessage().contains("Invalid outputDirectory"));
    }

    @Test
    void validateEmptyOutputDir() throws IOException {
        writeSchemaFile();
        writeOutputFile();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () ->
                inputValidator.validateInputs(schemaFile, outputFile, null));
        Assertions.assertTrue(thrown.getMessage().contains("Invalid packageName"));
    }

    @Test
    void validateEmptyPackage() throws IOException {
        writeSchemaFile();
        writeOutputFile();
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () ->
                inputValidator.validateInputs(schemaFile, outputFile, ""));
        Assertions.assertTrue(thrown.getMessage().contains("Invalid packageName"));
    }

    @Test
    void validateSuccess() throws IOException {
        writeSchemaFile();
        writeOutputFile();
        Assertions.assertDoesNotThrow(() -> inputValidator.validateInputs(
                schemaFile, outputFile, "hello"));
    }

    private void writeSchemaFile() throws IOException {
        String content = "Hello World !!";
        Files.write(Paths.get(schemaFile.getPath()), content.getBytes());
    }

    private void writeOutputFile() throws IOException {
        String content = "Hello World !!";
        Files.write(Paths.get(outputFile.getPath()), content.getBytes());
    }

}