package com.github.surajcm.generation;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

class JavaCodeGeneratorTest {
    @Test
    void verifySchemaGeneration() {
        Path schemaPath = Paths.get("src","test","resources","schema.graphqls");
        Path outputDirPath = Paths.get("src","test","results");
        String packageNameForTest = "com.example.helloworld";
        try {
            File schemaFile = new File(schemaPath.toFile().getAbsolutePath());
            File outputDirectory = new File(outputDirPath.toFile().getAbsolutePath());
            JavaCodeGenerator.getInstance().generatePojoFromSchema(
                    schemaFile,
                    outputDirectory,
                    packageNameForTest);
            // validate and delete the files / directories
            deleteFilesInPackageDirectory(outputDirectory);
        } catch (MojoExecutionException e) {
            Assertions.fail(e.getMessage());
        }
    }

    private void deleteFilesInPackageDirectory(final File packageDirectory) {
        File[] filesInPackageDir = packageDirectory.listFiles();
        if (filesInPackageDir != null) {
            Arrays.stream(filesInPackageDir).forEach(File::delete);
        }
    }
}