package com.github.surajcm.generation;

import com.github.surajcm.mapper.TypeMapper;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class JavaCodeGeneratorTest {

    private Path tempOutputDir;
    private static final String TEST_PACKAGE = "com.example.generated";

    @BeforeEach
    void setup() throws IOException {
        JavaCodeGenerator.resetInstance();
        SourceCodeGenerator.resetInstance();
        EnumGenerator.resetInstance();
        TypeMapper.resetInstance();
        tempOutputDir = Files.createTempDirectory("javagen-test");
    }

    @AfterEach
    void tearDown() {
        JavaCodeGenerator.resetInstance();
        SourceCodeGenerator.resetInstance();
        EnumGenerator.resetInstance();
        TypeMapper.resetInstance();
        deleteDirectory(tempOutputDir.toFile());
    }

    @Test
    void verifySchemaGeneration() throws MojoExecutionException {
        File schemaFile = new File("src/test/resources/schema.graphqls");
        File outputDirectory = tempOutputDir.toFile();

        JavaCodeGenerator.getInstance().generatePojoFromSchema(
                schemaFile,
                outputDirectory,
                TEST_PACKAGE);

        // Verify TempFilm.java was generated
        File generatedFile = new File(outputDirectory,
                TEST_PACKAGE.replace('.', File.separatorChar) + File.separator + "TempFilm.java");
        Assertions.assertTrue(generatedFile.exists(), "TempFilm.java should be generated");
    }

    @Test
    void verifyGeneratedFileContent() throws MojoExecutionException, IOException {
        File schemaFile = new File("src/test/resources/schema.graphqls");
        File outputDirectory = tempOutputDir.toFile();

        JavaCodeGenerator.getInstance().generatePojoFromSchema(
                schemaFile,
                outputDirectory,
                TEST_PACKAGE);

        File generatedFile = new File(outputDirectory,
                TEST_PACKAGE.replace('.', File.separatorChar) + File.separator + "TempFilm.java");

        String content = new String(Files.readAllBytes(generatedFile.toPath()));

        Assertions.assertTrue(content.contains("package " + TEST_PACKAGE));
        Assertions.assertTrue(content.contains("public class TempFilm"));
        Assertions.assertTrue(content.contains("private String title"));
        Assertions.assertTrue(content.contains("private Integer episode_id"));
        Assertions.assertTrue(content.contains("private Genre genre"));
        Assertions.assertTrue(content.contains("public String getTitle()"));
        Assertions.assertTrue(content.contains("public void setTitle(String title)"));
        Assertions.assertTrue(content.contains("public Integer getEpisode_id()"));
        Assertions.assertTrue(content.contains("public void setEpisode_id(Integer episode_id)"));
        Assertions.assertTrue(content.contains("public Genre getGenre()"));
        Assertions.assertTrue(content.contains("public void setGenre(Genre genre)"));
        Assertions.assertTrue(content.contains("toString()"));
        Assertions.assertTrue(content.contains("equals(Object o)"));
        Assertions.assertTrue(content.contains("hashCode()"));
    }

    @Test
    void singletonInstanceShouldBeSame() {
        JavaCodeGenerator instance1 = JavaCodeGenerator.getInstance();
        JavaCodeGenerator instance2 = JavaCodeGenerator.getInstance();
        Assertions.assertSame(instance1, instance2);
    }

    @Test
    void invalidSchemaFileShouldThrowException() {
        File invalidSchema = new File("/nonexistent/schema.graphqls");
        File outputDirectory = tempOutputDir.toFile();

        Assertions.assertThrows(MojoExecutionException.class, () ->
                JavaCodeGenerator.getInstance().generatePojoFromSchema(
                        invalidSchema,
                        outputDirectory,
                        TEST_PACKAGE));
    }

    @Test
    void nullPackageNameShouldThrowException() {
        File schemaFile = new File("src/test/resources/schema.graphqls");
        File outputDirectory = tempOutputDir.toFile();

        Assertions.assertThrows(MojoExecutionException.class, () ->
                JavaCodeGenerator.getInstance().generatePojoFromSchema(
                        schemaFile,
                        outputDirectory,
                        null));
    }

    @Test
    void verifyEnumGeneration() throws MojoExecutionException {
        File schemaFile = new File("src/test/resources/schema.graphqls");
        File outputDirectory = tempOutputDir.toFile();

        JavaCodeGenerator.getInstance().generatePojoFromSchema(
                schemaFile,
                outputDirectory,
                TEST_PACKAGE);

        // Verify Genre.java enum was generated
        File generatedEnumFile = new File(outputDirectory,
                TEST_PACKAGE.replace('.', File.separatorChar) + File.separator + "Genre.java");
        Assertions.assertTrue(generatedEnumFile.exists(), "Genre.java enum should be generated");
    }

    @Test
    void verifyGeneratedEnumContent() throws MojoExecutionException, IOException {
        File schemaFile = new File("src/test/resources/schema.graphqls");
        File outputDirectory = tempOutputDir.toFile();

        JavaCodeGenerator.getInstance().generatePojoFromSchema(
                schemaFile,
                outputDirectory,
                TEST_PACKAGE);

        File generatedEnumFile = new File(outputDirectory,
                TEST_PACKAGE.replace('.', File.separatorChar) + File.separator + "Genre.java");

        String content = new String(Files.readAllBytes(generatedEnumFile.toPath()));

        Assertions.assertTrue(content.contains("package " + TEST_PACKAGE));
        Assertions.assertTrue(content.contains("public enum Genre"));
        Assertions.assertTrue(content.contains("ACTION"));
        Assertions.assertTrue(content.contains("COMEDY"));
        Assertions.assertTrue(content.contains("DRAMA"));
        Assertions.assertTrue(content.contains("HORROR"));
        Assertions.assertTrue(content.contains("SCIFI"));
    }

    @Test
    void verifyEnumFieldInGeneratedPojo() throws MojoExecutionException, IOException {
        File schemaFile = new File("src/test/resources/schema.graphqls");
        File outputDirectory = tempOutputDir.toFile();

        JavaCodeGenerator.getInstance().generatePojoFromSchema(
                schemaFile,
                outputDirectory,
                TEST_PACKAGE);

        File generatedFile = new File(outputDirectory,
                TEST_PACKAGE.replace('.', File.separatorChar) + File.separator + "TempFilm.java");

        String content = new String(Files.readAllBytes(generatedFile.toPath()));

        // Verify that the Genre enum field is present in TempFilm
        Assertions.assertTrue(content.contains("private Genre genre"));
        Assertions.assertTrue(content.contains("public Genre getGenre()"));
        Assertions.assertTrue(content.contains("public void setGenre(Genre genre)"));
    }

    private void deleteDirectory(File directory) {
        if (directory == null || !directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
