package com.github.surajcm.generation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.plugin.MojoExecutionException;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class SourceCodeGenerator {
    private static SourceCodeGenerator INSTANCE;

    private SourceCodeGenerator() {
    }

    public static synchronized SourceCodeGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SourceCodeGenerator();
        }
        return INSTANCE;
    }

    public void generateJavaFile(final File outputDir, final String packageName)
            throws MojoExecutionException {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld123")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, helloWorld)
                .build();

        try {
            javaFile.writeTo(System.out);
        } catch (IOException exception) {
            throw new MojoExecutionException("Unable to generate POJO classes : ", exception);
        }
    }
}
