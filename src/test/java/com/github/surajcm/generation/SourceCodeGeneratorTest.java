package com.github.surajcm.generation;

import com.github.surajcm.mapper.TypeMapper;
import com.github.surajcm.model.GraphQLFieldInfo;
import com.github.surajcm.model.GraphQLTypeInfo;
import com.github.surajcm.model.GraphQLTypeKind;
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

class SourceCodeGeneratorTest {

    private SourceCodeGenerator generator;
    private static final String TEST_PACKAGE = "com.example.generated";

    @BeforeEach
    void setup() {
        SourceCodeGenerator.resetInstance();
        TypeMapper.resetInstance();
        generator = SourceCodeGenerator.getInstance();
    }

    @AfterEach
    void tearDown() {
        SourceCodeGenerator.resetInstance();
        TypeMapper.resetInstance();
    }

    @Test
    void generateSimplePojo() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("name", "String", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("User", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);

        Assertions.assertNotNull(javaFile);
        Assertions.assertEquals(TEST_PACKAGE, javaFile.packageName);
        Assertions.assertEquals("User", javaFile.typeSpec.name);
    }

    @Test
    void generatePojoWithAllScalarTypes() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("stringField", "String", false, false),
                new GraphQLFieldInfo("intField", "Int", false, false),
                new GraphQLFieldInfo("floatField", "Float", false, false),
                new GraphQLFieldInfo("booleanField", "Boolean", false, false),
                new GraphQLFieldInfo("idField", "ID", false, false),
                new GraphQLFieldInfo("longField", "Long", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("AllScalars", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("private String stringField"));
        Assertions.assertTrue(code.contains("private Integer intField"));
        Assertions.assertTrue(code.contains("private Double floatField"));
        Assertions.assertTrue(code.contains("private Boolean booleanField"));
        Assertions.assertTrue(code.contains("private String idField"));
        Assertions.assertTrue(code.contains("private Long longField"));
    }

    @Test
    void generatePojoWithListField() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("tags", "String", true, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("Post", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("private List<String> tags"));
        Assertions.assertTrue(code.contains("import java.util.List"));
    }

    @Test
    void generatePojoWithCustomTypeField() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("author", "User", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("Post", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("private User author"));
    }

    @Test
    void generatePojoWithListOfCustomType() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("comments", "Comment", true, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("Post", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("private List<Comment> comments"));
    }

    @Test
    void generatePojoHasNoArgConstructor() {
        List<GraphQLFieldInfo> fields = Collections.singletonList(
                new GraphQLFieldInfo("name", "String", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("User", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public User() {"));
    }

    @Test
    void generatePojoHasAllArgsConstructor() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("name", "String", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("User", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public User(String id, String name)"));
        Assertions.assertTrue(code.contains("this.id = id"));
        Assertions.assertTrue(code.contains("this.name = name"));
    }

    @Test
    void generatePojoHasGetters() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("name", "String", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("User", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public String getId()"));
        Assertions.assertTrue(code.contains("return id;"));
        Assertions.assertTrue(code.contains("public String getName()"));
        Assertions.assertTrue(code.contains("return name;"));
    }

    @Test
    void generatePojoHasSetters() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("name", "String", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("User", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public void setId(String id)"));
        Assertions.assertTrue(code.contains("this.id = id;"));
        Assertions.assertTrue(code.contains("public void setName(String name)"));
        Assertions.assertTrue(code.contains("this.name = name;"));
    }

    @Test
    void generatePojoHasToString() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("name", "String", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("User", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("@Override"));
        Assertions.assertTrue(code.contains("public String toString()"));
        Assertions.assertTrue(code.contains("User{"));
    }

    @Test
    void generatePojoHasEquals() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("name", "String", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("User", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public boolean equals(Object o)"));
        Assertions.assertTrue(code.contains("if (this == o) return true"));
        Assertions.assertTrue(code.contains("if (o == null || getClass() != o.getClass()) return false"));
        Assertions.assertTrue(code.contains("Objects.equals(id, that.id)"));
    }

    @Test
    void generatePojoHasHashCode() {
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("id", "ID", false, true),
                new GraphQLFieldInfo("name", "String", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("User", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public int hashCode()"));
        Assertions.assertTrue(code.contains("Objects.hash(id, name)"));
    }

    @Test
    void generatePojoWithNoFields() {
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("Empty", GraphQLTypeKind.OBJECT, Collections.emptyList());

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public class Empty"));
        Assertions.assertTrue(code.contains("public Empty() {"));
        Assertions.assertTrue(code.contains("return \"Empty{}\""));
        Assertions.assertTrue(code.contains("return true")); // equals returns true for empty
        Assertions.assertTrue(code.contains("return 0")); // hashCode returns 0 for empty
    }

    @Test
    void generatePojoWritesToFile() throws IOException {
        Path tempDir = Files.createTempDirectory("pojo-test");
        File outputDir = tempDir.toFile();

        try {
            List<GraphQLFieldInfo> fields = Arrays.asList(
                    new GraphQLFieldInfo("id", "ID", false, true),
                    new GraphQLFieldInfo("name", "String", false, false)
            );
            GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("TestUser", GraphQLTypeKind.OBJECT, fields);

            generator.generatePojo(typeInfo, outputDir, TEST_PACKAGE);

            File generatedFile = new File(outputDir,
                    TEST_PACKAGE.replace('.', File.separatorChar) + File.separator + "TestUser.java");
            Assertions.assertTrue(generatedFile.exists());

            String content = new String(Files.readAllBytes(generatedFile.toPath()));
            Assertions.assertTrue(content.contains("public class TestUser"));
        } finally {
            deleteDirectory(outputDir);
        }
    }

    @Test
    void singletonInstanceShouldBeSame() {
        SourceCodeGenerator instance1 = SourceCodeGenerator.getInstance();
        SourceCodeGenerator instance2 = SourceCodeGenerator.getInstance();
        Assertions.assertSame(instance1, instance2);
    }

    @Test
    void generatePojoFromTempFilmSchema() {
        // Matches the test schema in src/test/resources/schema.graphqls
        List<GraphQLFieldInfo> fields = Arrays.asList(
                new GraphQLFieldInfo("title", "String", false, false),
                new GraphQLFieldInfo("episode_id", "Int", false, false)
        );
        GraphQLTypeInfo typeInfo = new GraphQLTypeInfo("TempFilm", GraphQLTypeKind.OBJECT, fields);

        JavaFile javaFile = generator.generatePojo(typeInfo, TEST_PACKAGE);
        String code = javaFile.toString();

        Assertions.assertTrue(code.contains("public class TempFilm"));
        Assertions.assertTrue(code.contains("private String title"));
        Assertions.assertTrue(code.contains("private Integer episode_id"));
        Assertions.assertTrue(code.contains("public String getTitle()"));
        Assertions.assertTrue(code.contains("public Integer getEpisode_id()"));
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
