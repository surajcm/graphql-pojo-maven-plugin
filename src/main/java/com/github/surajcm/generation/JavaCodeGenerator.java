package com.github.surajcm.generation;

import com.github.surajcm.exception.ValidationException;
import com.github.surajcm.model.GraphQLEnumInfo;
import com.github.surajcm.model.GraphQLTypeInfo;
import com.github.surajcm.parser.SchemaParser;
import com.github.surajcm.parser.SchemaTypeExtractor;
import com.github.surajcm.util.FileUtils;
import com.github.surajcm.validator.InputValidator;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Orchestrates the POJO generation process from GraphQL schema files.
 */
public final class JavaCodeGenerator {

    private static JavaCodeGenerator instance;

    private JavaCodeGenerator() {
    }

    public static synchronized JavaCodeGenerator getInstance() {
        if (instance == null) {
            instance = new JavaCodeGenerator();
        }
        return instance;
    }

    /**
     * Generates POJOs from a GraphQL schema file.
     *
     * @param schema the GraphQL schema file
     * @param outputDir the output directory for generated files
     * @param packageName the package name for generated classes
     * @throws MojoExecutionException if generation fails
     */
    public void generatePojoFromSchema(final File schema,
                                       final File outputDir,
                                       final String packageName)
            throws MojoExecutionException {
        try {
            // Validate inputs
            InputValidator.getInstance().validateInputs(schema, outputDir, packageName);

            // Clean and recreate output directory
            FileUtils.getInstance().cleanAndRecreateOutputDir(outputDir, packageName);

            // Parse the schema
            TypeDefinitionRegistry registry = SchemaParser.getInstance().parse(schema);

            // Extract types
            SchemaTypeExtractor extractor = SchemaTypeExtractor.getInstance();
            List<GraphQLTypeInfo> objectTypes = extractor.extractObjectTypes(registry);
            List<GraphQLTypeInfo> inputTypes = extractor.extractInputTypes(registry);
            List<GraphQLEnumInfo> enumTypes = extractor.extractEnumTypes(registry);

            // Generate POJOs for object types
            SourceCodeGenerator generator = SourceCodeGenerator.getInstance();
            for (GraphQLTypeInfo typeInfo : objectTypes) {
                generator.generatePojo(typeInfo, outputDir, packageName);
            }

            // Generate POJOs for input types
            for (GraphQLTypeInfo typeInfo : inputTypes) {
                generator.generatePojo(typeInfo, outputDir, packageName);
            }

            // Generate enums
            EnumGenerator enumGenerator = EnumGenerator.getInstance();
            for (GraphQLEnumInfo enumInfo : enumTypes) {
                enumGenerator.generateEnum(enumInfo, outputDir, packageName);
            }

        } catch (ValidationException | IOException ex) {
            throw new MojoExecutionException("Unable to generate POJO classes: " + ex.getMessage(), ex);
        }
    }

    /**
     * Resets the singleton instance (useful for testing).
     */
    public static void resetInstance() {
        instance = null;
    }
}
