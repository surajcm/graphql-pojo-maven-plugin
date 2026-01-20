package com.github.surajcm.generation;

import com.github.surajcm.model.GraphQLEnumInfo;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

/**
 * Generates Java enum source code from GraphQL enum definitions.
 */
public class EnumGenerator {

    private static EnumGenerator instance;

    private EnumGenerator() {
    }

    public static synchronized EnumGenerator getInstance() {
        if (instance == null) {
            instance = new EnumGenerator();
        }
        return instance;
    }

    /**
     * Generates a Java enum file for the given GraphQL enum.
     *
     * @param enumInfo the GraphQL enum information
     * @param outputDir the output directory
     * @param packageName the package name for the generated enum
     * @return the generated JavaFile
     * @throws IOException if file writing fails
     */
    public JavaFile generateEnum(GraphQLEnumInfo enumInfo, File outputDir, String packageName)
            throws IOException {
        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder(enumInfo.getName())
                .addModifiers(Modifier.PUBLIC);

        // Add enum constants
        for (String value : enumInfo.getValues()) {
            enumBuilder.addEnumConstant(value);
        }

        TypeSpec enumSpec = enumBuilder.build();
        JavaFile javaFile = JavaFile.builder(packageName, enumSpec)
                .indent("    ")
                .build();

        if (outputDir != null) {
            javaFile.writeTo(outputDir);
        }

        return javaFile;
    }

    /**
     * Generates a JavaFile without writing to disk (useful for testing).
     *
     * @param enumInfo the GraphQL enum information
     * @param packageName the package name for the generated enum
     * @return the generated JavaFile
     */
    public JavaFile generateEnum(GraphQLEnumInfo enumInfo, String packageName) {
        try {
            return generateEnum(enumInfo, null, packageName);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException without file output", e);
        }
    }

    /**
     * Resets the singleton instance (useful for testing).
     */
    public static void resetInstance() {
        instance = null;
    }
}
