package com.github.surajcm.generation;

import com.github.surajcm.model.GraphQLEnumInfo;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class EnumGeneratorTest {

    private EnumGenerator generator;
    private static final String TEST_PACKAGE = "com.example.generated";

    @BeforeEach
    void setup() {
        EnumGenerator.resetInstance();
        generator = EnumGenerator.getInstance();
    }

    @AfterEach
    void tearDown() {
        EnumGenerator.resetInstance();
    }

    @Test
    void generateSimpleEnum() {
        List<String> values = Arrays.asList("RED", "GREEN", "BLUE");
        GraphQLEnumInfo enumInfo = new GraphQLEnumInfo("Color", values);

        JavaFile javaFile = generator.generateEnum(enumInfo, TEST_PACKAGE);

        Assertions.assertNotNull(javaFile);
        Assertions.assertEquals(TEST_PACKAGE, javaFile.packageName);
        Assertions.assertEquals("Color", javaFile.typeSpec.name);
    }

    @Test
    void generateEnumWithAllValues() {
        List<String> values = Arrays.asList("ADMIN", "USER", "GUEST");
        GraphQLEnumInfo enumInfo = new GraphQLEnumInfo("Role", values);

        JavaFile javaFile = generator.generateEnum(enumInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public enum Role"));
        Assertions.assertTrue(code.contains("ADMIN"));
        Assertions.assertTrue(code.contains("USER"));
        Assertions.assertTrue(code.contains("GUEST"));
    }

    @Test
    void generateEnumWithSingleValue() {
        List<String> values = Collections.singletonList("ACTIVE");
        GraphQLEnumInfo enumInfo = new GraphQLEnumInfo("Status", values);

        JavaFile javaFile = generator.generateEnum(enumInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public enum Status"));
        Assertions.assertTrue(code.contains("ACTIVE"));
    }

    @Test
    void generateEnumWithMultipleValues() {
        List<String> values = Arrays.asList(
                "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
        );
        GraphQLEnumInfo enumInfo = new GraphQLEnumInfo("DayOfWeek", values);

        JavaFile javaFile = generator.generateEnum(enumInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public enum DayOfWeek"));
        for (String value : values) {
            Assertions.assertTrue(code.contains(value));
        }
    }

    @Test
    void generateEnumWritesToFile() throws IOException {
        Path tempDir = Files.createTempDirectory("enum-test");
        File outputDir = tempDir.toFile();

        try {
            List<String> values = Arrays.asList("PENDING", "APPROVED", "REJECTED");
            GraphQLEnumInfo enumInfo = new GraphQLEnumInfo("ApprovalStatus", values);

            generator.generateEnum(enumInfo, outputDir, TEST_PACKAGE);

            File generatedFile = new File(outputDir,
                    TEST_PACKAGE.replace('.', File.separatorChar) + File.separator + "ApprovalStatus.java");
            Assertions.assertTrue(generatedFile.exists());

            String content = new String(Files.readAllBytes(generatedFile.toPath()));
            Assertions.assertTrue(content.contains("public enum ApprovalStatus"));
            Assertions.assertTrue(content.contains("PENDING"));
            Assertions.assertTrue(content.contains("APPROVED"));
            Assertions.assertTrue(content.contains("REJECTED"));
        } finally {
            deleteDirectory(outputDir);
        }
    }

    @Test
    void singletonInstanceShouldBeSame() {
        EnumGenerator instance1 = EnumGenerator.getInstance();
        EnumGenerator instance2 = EnumGenerator.getInstance();
        Assertions.assertSame(instance1, instance2);
    }

    @Test
    void generateEnumWithUnderscoreValues() {
        List<String> values = Arrays.asList("ORDER_PLACED", "ORDER_SHIPPED", "ORDER_DELIVERED");
        GraphQLEnumInfo enumInfo = new GraphQLEnumInfo("OrderStatus", values);

        JavaFile javaFile = generator.generateEnum(enumInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public enum OrderStatus"));
        Assertions.assertTrue(code.contains("ORDER_PLACED"));
        Assertions.assertTrue(code.contains("ORDER_SHIPPED"));
        Assertions.assertTrue(code.contains("ORDER_DELIVERED"));
    }

    @Test
    void generateEnumWithNumberedValues() {
        List<String> values = Arrays.asList("LEVEL_1", "LEVEL_2", "LEVEL_3");
        GraphQLEnumInfo enumInfo = new GraphQLEnumInfo("SkillLevel", values);

        JavaFile javaFile = generator.generateEnum(enumInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public enum SkillLevel"));
        Assertions.assertTrue(code.contains("LEVEL_1"));
        Assertions.assertTrue(code.contains("LEVEL_2"));
        Assertions.assertTrue(code.contains("LEVEL_3"));
    }

    @Test
    void verifyPackageDeclaration() {
        List<String> values = Arrays.asList("TRUE", "FALSE");
        GraphQLEnumInfo enumInfo = new GraphQLEnumInfo("BooleanEnum", values);

        JavaFile javaFile = generator.generateEnum(enumInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("package " + TEST_PACKAGE));
    }

    private void deleteDirectory(File directory) {
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
