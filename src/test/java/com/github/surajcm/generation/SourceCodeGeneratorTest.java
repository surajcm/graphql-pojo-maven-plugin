package com.github.surajcm.generation;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

class SourceCodeGeneratorTest {
    @Test
    void verifyGenerateJavaFile() {
        Path outputDirPath = Paths.get("src","test","results");
        String packageNameForTest = "com.example.helloworld";
        try {
            File outputDirectory = new File(outputDirPath.toFile().getAbsolutePath());
            SourceCodeGenerator.getInstance().generateJavaFile(outputDirectory, packageNameForTest);
            // validate and delete the files / directories
            deleteFilesInPackageDirectory(outputDirectory);
        } catch (MojoExecutionException e) {
            Assertions.fail(e.getMessage());
        }
    }

    private void deleteFilesInPackageDirectory(final File packageDirectory) {
        File[] filesInPackageDir = packageDirectory.listFiles();
        if (filesInPackageDir != null) {
            Arrays.stream(filesInPackageDir).forEach(System.out::println);
            //Arrays.stream(filesInPackageDir).forEach(File::delete);
        }
    }
}