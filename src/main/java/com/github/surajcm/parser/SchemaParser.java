package com.github.surajcm.parser;

import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Parses GraphQL schema files and returns a TypeDefinitionRegistry.
 */
public final class SchemaParser {

    private static SchemaParser instance;

    private SchemaParser() {
    }

    public static SchemaParser getInstance() {
        if (instance == null) {
            instance = new SchemaParser();
        }
        return instance;
    }

    /**
     * Parses a GraphQL schema file and returns the TypeDefinitionRegistry.
     *
     * @param schemaFile the GraphQL schema file to parse
     * @return TypeDefinitionRegistry containing all type definitions
     * @throws IOException if the file cannot be read
     * @throws graphql.schema.idl.errors.SchemaProblem if the schema is invalid
     */
    public TypeDefinitionRegistry parse(File schemaFile) throws IOException {
        String schemaContent = new String(Files.readAllBytes(schemaFile.toPath()));
        return parseContent(schemaContent);
    }

    /**
     * Parses GraphQL schema content string and returns the TypeDefinitionRegistry.
     *
     * @param schemaContent the GraphQL schema content as a string
     * @return TypeDefinitionRegistry containing all type definitions
     * @throws graphql.schema.idl.errors.SchemaProblem if the schema is invalid
     */
    public TypeDefinitionRegistry parseContent(String schemaContent) {
        graphql.schema.idl.SchemaParser schemaParser = new graphql.schema.idl.SchemaParser();
        return schemaParser.parse(schemaContent);
    }
}
