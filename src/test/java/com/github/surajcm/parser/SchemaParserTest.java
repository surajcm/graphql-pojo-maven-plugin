package com.github.surajcm.parser;

import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.errors.SchemaProblem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class SchemaParserTest {

    private final SchemaParser schemaParser = SchemaParser.getInstance();
    private File tempSchemaFile;

    @BeforeEach
    void setup() throws IOException {
        String tmpDirsLocation = System.getProperty("java.io.tmpdir");
        tempSchemaFile = File.createTempFile(tmpDirsLocation + "schema", ".graphqls");
    }

    @AfterEach
    void tearDown() {
        tempSchemaFile.deleteOnExit();
    }

    @Test
    void parseValidSchemaFile() throws IOException {
        String schema = "type Query { hello: String }\n"
                + "type User { id: ID! name: String }";
        Files.write(Paths.get(tempSchemaFile.getPath()), schema.getBytes());

        TypeDefinitionRegistry registry = schemaParser.parse(tempSchemaFile);

        Assertions.assertNotNull(registry);
        Assertions.assertTrue(registry.getType("Query").isPresent());
        Assertions.assertTrue(registry.getType("User").isPresent());
    }

    @Test
    void parseValidSchemaContent() {
        String schema = "type Query { hello: String }\n"
                + "type Product { id: ID! price: Float }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);

        Assertions.assertNotNull(registry);
        Assertions.assertTrue(registry.getType("Query").isPresent());
        Assertions.assertTrue(registry.getType("Product").isPresent());
    }

    @Test
    void parseSchemaWithEnums() {
        String schema = "enum Status { ACTIVE INACTIVE PENDING }\n"
                + "type Query { status: Status }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);

        Assertions.assertNotNull(registry);
        Assertions.assertTrue(registry.getType("Status").isPresent());
    }

    @Test
    void parseSchemaWithInputTypes() {
        String schema = "input CreateUserInput { name: String! email: String! }\n"
                + "type Query { hello: String }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);

        Assertions.assertNotNull(registry);
        Assertions.assertTrue(registry.getType("CreateUserInput").isPresent());
    }

    @Test
    void parseSchemaWithInterfaces() {
        String schema = "interface Node { id: ID! }\n"
                + "type User implements Node { id: ID! name: String }\n"
                + "type Query { user: User }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);

        Assertions.assertNotNull(registry);
        Assertions.assertTrue(registry.getType("Node").isPresent());
        Assertions.assertTrue(registry.getType("User").isPresent());
    }

    @Test
    void parseSchemaWithListTypes() {
        String schema = "type Query { users: [User] }\n"
                + "type User { id: ID! tags: [String!]! }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);

        Assertions.assertNotNull(registry);
        Assertions.assertTrue(registry.getType("User").isPresent());
    }

    @Test
    void parseInvalidSchemaShouldThrowException() {
        String invalidSchema = "this is not valid graphql {{{";

        Assertions.assertThrows(SchemaProblem.class, () ->
                schemaParser.parseContent(invalidSchema));
    }

    @Test
    void parseNonExistentFileShouldThrowException() {
        File nonExistent = new File("/nonexistent/schema.graphqls");

        Assertions.assertThrows(IOException.class, () ->
                schemaParser.parse(nonExistent));
    }

    @Test
    void parseTestResourceSchema() throws IOException {
        File resourceSchema = new File("src/test/resources/schema.graphqls");

        TypeDefinitionRegistry registry = schemaParser.parse(resourceSchema);

        Assertions.assertNotNull(registry);
        Assertions.assertTrue(registry.getType("Query").isPresent());
        Assertions.assertTrue(registry.getType("TempFilm").isPresent());
    }

    @Test
    void singletonInstanceShouldBeSame() {
        SchemaParser instance1 = SchemaParser.getInstance();
        SchemaParser instance2 = SchemaParser.getInstance();

        Assertions.assertSame(instance1, instance2);
    }
}
