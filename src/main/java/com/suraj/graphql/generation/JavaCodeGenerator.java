package com.suraj.graphql.generation;

import com.suraj.graphql.util.FileUtils;
import com.suraj.graphql.validator.InputValidator;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class JavaCodeGenerator {
    private static JavaCodeGenerator INSTANCE;

    private JavaCodeGenerator() {
    }

    public synchronized static JavaCodeGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JavaCodeGenerator();
        }
        return INSTANCE;
    }

    public void generatePojoFromSchema(File schema, File outputDir, String packageName)
            throws MojoExecutionException {
        try {
            InputValidator.getInstance().validateInputs(schema, outputDir, packageName);
            FileUtils.getInstance().cleanAndRecreateOutputDir(outputDir, packageName);
        } catch (IllegalArgumentException | IOException illegalEx) {
            throw new MojoExecutionException("Unable to generate POJO classes : ", illegalEx);
        }
    }


}
