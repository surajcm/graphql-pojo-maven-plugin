package com.suraj.graphql.generation;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class JavaCodeGeneratorTest {
    @Test
    public void verifySchemaGeneration() {
        File schemaFile = new File("/Users/a-3133/workspace/personal/graphql-pojo-maven-plugin/src/test/resources/schema.graphqls");
        File outputDirectory = new File("/Users/a-3133/workspace/personal/graphql-pojo-maven-plugin/src/test/results/");
        String packageNameForTest = "com.example.helloworld";
        try {
            JavaCodeGenerator.getInstance().generatePojoFromSchema(schemaFile, outputDirectory, packageNameForTest);
        } catch (MojoExecutionException e) {
            Assertions.fail(e.getMessage());
        }
    }
}