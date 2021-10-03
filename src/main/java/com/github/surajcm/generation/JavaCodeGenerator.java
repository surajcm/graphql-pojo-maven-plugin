package com.github.surajcm.generation;

import com.github.surajcm.exception.ValidationException;
import com.github.surajcm.util.FileUtils;
import com.github.surajcm.validator.InputValidator;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;

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

    public void generatePojoFromSchema(final File schema,
                                       final File outputDir,
                                       final String packageName)
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
