package com.github.surajcm.parser;

import com.github.surajcm.model.GraphQLEnumInfo;
import com.github.surajcm.model.GraphQLFieldInfo;
import com.github.surajcm.model.GraphQLTypeInfo;
import com.github.surajcm.model.GraphQLTypeKind;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class SchemaTypeExtractorTest {

    private SchemaParser schemaParser;
    private SchemaTypeExtractor typeExtractor;

    @BeforeEach
    void setup() {
        schemaParser = SchemaParser.getInstance();
        typeExtractor = SchemaTypeExtractor.getInstance();
    }

    @Test
    void extractObjectTypes() {
        String schema = "type Query { hello: String }\n"
                + "type User { id: ID! name: String age: Int }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractObjectTypes(registry);

        Assertions.assertEquals(1, types.size());
        GraphQLTypeInfo userType = types.get(0);
        Assertions.assertEquals("User", userType.getName());
        Assertions.assertEquals(GraphQLTypeKind.OBJECT, userType.getKind());
        Assertions.assertEquals(3, userType.getFields().size());
    }

    @Test
    void extractObjectTypesExcludesBuiltInTypes() {
        String schema = "type Query { hello: String }\n"
                + "type Mutation { create: String }\n"
                + "type Subscription { onUpdate: String }\n"
                + "type User { id: ID! }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractObjectTypes(registry);

        Assertions.assertEquals(1, types.size());
        Assertions.assertEquals("User", types.get(0).getName());
    }

    @Test
    void extractFieldsWithNonNullType() {
        String schema = "type Query { hello: String }\n"
                + "type User { id: ID! name: String! }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractObjectTypes(registry);

        GraphQLTypeInfo userType = types.get(0);
        GraphQLFieldInfo idField = userType.getFields().stream()
                .filter(f -> f.getName().equals("id"))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(idField);
        Assertions.assertTrue(idField.isNonNull());
        Assertions.assertEquals("ID", idField.getTypeName());
    }

    @Test
    void extractFieldsWithListType() {
        String schema = "type Query { hello: String }\n"
                + "type User { id: ID! tags: [String] friends: [User!]! }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractObjectTypes(registry);

        GraphQLTypeInfo userType = types.get(0);

        GraphQLFieldInfo tagsField = userType.getFields().stream()
                .filter(f -> f.getName().equals("tags"))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(tagsField);
        Assertions.assertTrue(tagsField.isList());
        Assertions.assertFalse(tagsField.isNonNull());
        Assertions.assertEquals("String", tagsField.getTypeName());

        GraphQLFieldInfo friendsField = userType.getFields().stream()
                .filter(f -> f.getName().equals("friends"))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(friendsField);
        Assertions.assertTrue(friendsField.isList());
        Assertions.assertTrue(friendsField.isNonNull());
        Assertions.assertEquals("User", friendsField.getTypeName());
    }

    @Test
    void extractInputTypes() {
        String schema = "type Query { hello: String }\n"
                + "input CreateUserInput { name: String! email: String! age: Int }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractInputTypes(registry);

        Assertions.assertEquals(1, types.size());
        GraphQLTypeInfo inputType = types.get(0);
        Assertions.assertEquals("CreateUserInput", inputType.getName());
        Assertions.assertEquals(GraphQLTypeKind.INPUT_OBJECT, inputType.getKind());
        Assertions.assertEquals(3, inputType.getFields().size());
    }

    @Test
    void extractEnumTypes() {
        String schema = "type Query { hello: String }\n"
                + "enum Status { ACTIVE INACTIVE PENDING DELETED }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLEnumInfo> enums = typeExtractor.extractEnumTypes(registry);

        Assertions.assertEquals(1, enums.size());
        GraphQLEnumInfo statusEnum = enums.get(0);
        Assertions.assertEquals("Status", statusEnum.getName());
        Assertions.assertEquals(4, statusEnum.getValues().size());
        Assertions.assertTrue(statusEnum.getValues().contains("ACTIVE"));
        Assertions.assertTrue(statusEnum.getValues().contains("INACTIVE"));
        Assertions.assertTrue(statusEnum.getValues().contains("PENDING"));
        Assertions.assertTrue(statusEnum.getValues().contains("DELETED"));
    }

    @Test
    void extractInterfaceTypes() {
        String schema = "type Query { hello: String }\n"
                + "interface Node { id: ID! createdAt: String }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractInterfaceTypes(registry);

        Assertions.assertEquals(1, types.size());
        GraphQLTypeInfo nodeInterface = types.get(0);
        Assertions.assertEquals("Node", nodeInterface.getName());
        Assertions.assertEquals(GraphQLTypeKind.INTERFACE, nodeInterface.getKind());
        Assertions.assertEquals(2, nodeInterface.getFields().size());
    }

    @Test
    void extractAllTypes() {
        String schema = "type Query { hello: String }\n"
                + "type User { id: ID! }\n"
                + "input CreateUserInput { name: String! }\n"
                + "interface Node { id: ID! }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractAllTypes(registry);

        Assertions.assertEquals(3, types.size());
    }

    @Test
    void extractFromTestResourceSchema() {
        String schema = "type Query { film(id: Long): TempFilm }\n"
                + "type TempFilm { title: String episode_id: Int }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractObjectTypes(registry);

        Assertions.assertEquals(1, types.size());
        GraphQLTypeInfo filmType = types.get(0);
        Assertions.assertEquals("TempFilm", filmType.getName());
        Assertions.assertEquals(2, filmType.getFields().size());

        GraphQLFieldInfo titleField = filmType.getFields().stream()
                .filter(f -> f.getName().equals("title"))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(titleField);
        Assertions.assertEquals("String", titleField.getTypeName());

        GraphQLFieldInfo episodeField = filmType.getFields().stream()
                .filter(f -> f.getName().equals("episode_id"))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(episodeField);
        Assertions.assertEquals("Int", episodeField.getTypeName());
    }

    @Test
    void singletonInstanceShouldBeSame() {
        SchemaTypeExtractor instance1 = SchemaTypeExtractor.getInstance();
        SchemaTypeExtractor instance2 = SchemaTypeExtractor.getInstance();

        Assertions.assertSame(instance1, instance2);
    }

    @Test
    void extractEmptySchemaReturnsEmptyList() {
        String schema = "type Query { hello: String }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractObjectTypes(registry);

        Assertions.assertTrue(types.isEmpty());
    }

    @Test
    void extractMultipleObjectTypes() {
        String schema = "type Query { hello: String }\n"
                + "type User { id: ID! name: String }\n"
                + "type Post { id: ID! title: String content: String }\n"
                + "type Comment { id: ID! text: String }";

        TypeDefinitionRegistry registry = schemaParser.parseContent(schema);
        List<GraphQLTypeInfo> types = typeExtractor.extractObjectTypes(registry);

        Assertions.assertEquals(3, types.size());
    }
}
