package com.suraj.graphql.generation;

import com.suraj.graphql.validator.InputValidator;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
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
            cleanAndRecreateOutputDir(outputDir, packageName);
        } catch (IllegalArgumentException | IOException illegalEx) {
            throw new MojoExecutionException("Unable to generate POJO classes : ", illegalEx);
        }
    }

    public void cleanAndRecreateOutputDir(File outputDir, String packageName) throws IOException {
        // during validation, we ensure that the output directory exists
        // if output directory is empty,  create the package
        String[] fileNames = outputDir.list();
        String packageWithSystemDirFormat = packageWithDirectoryFormat(packageName);
        if (fileNames == null || fileNames.length ==0) {
            File packageDirectory = new File(outputDir, packageWithSystemDirFormat);
            //create package and return
            if (packageDirectory.mkdirs() || packageDirectory.exists()) {
                return;
            } else {
                throw new IOException("Unable to create package : "+ packageName);
            }
        }
        // if output directory has contents, check whether package exists, else do nothing
        File[] filesInOutputDir = outputDir.listFiles();
        // if package exists, delete all files inside the package
    }

    private String packageWithDirectoryFormat(String packageName) {
        return Collections.list(new StringTokenizer(packageName, ".")).stream()
                .map(token -> (String) token)
                .filter(t -> t.trim().isEmpty())
                .collect(Collectors.joining(File.separator));
    }
}
