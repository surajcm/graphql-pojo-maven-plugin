package com.suraj.graphql.generation;

import com.suraj.graphql.exception.ValidationException;
import com.suraj.graphql.util.FileUtils;
import com.suraj.graphql.validator.InputValidator;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;

public final class JavaCodeGenerator {
    private static JavaCodeGenerator INSTANCE;

    private JavaCodeGenerator() {
    }

    public static synchronized JavaCodeGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JavaCodeGenerator();
        }
        return INSTANCE;
    }

    public void generatePojoFromSchema(final File schema, final File outputDir, final String packageName)
            throws MojoExecutionException {
        try {
            InputValidator.getInstance().validateInputs(schema, outputDir, packageName);
            FileUtils.getInstance().cleanAndRecreateOutputDir(outputDir, packageName);
            SourceCodeGenerator.getInstance().generateJavaFile(outputDir,packageName);
        } catch (ValidationException | IOException illegalEx) {
            throw new MojoExecutionException("Unable to generate POJO classes : ", illegalEx);
        }
    }


}
