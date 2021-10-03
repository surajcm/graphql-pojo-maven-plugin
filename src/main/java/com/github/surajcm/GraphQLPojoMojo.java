package com.github.surajcm;

import com.github.surajcm.generation.JavaCodeGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class GraphQLPojoMojo extends AbstractMojo {
    private static final String DEFAULT_SCHEMA_LOCATION = "src/main/resources/schema.graphqls";
    private static final String DEFAULT_OUTPUT_DIR = "src/main/java/";

    @Parameter(defaultValue = DEFAULT_SCHEMA_LOCATION)
    private File schema;

    @Parameter(defaultValue = DEFAULT_OUTPUT_DIR)
    private File outputDir;

    @Parameter(required = true)
    private String packageName;

    @Override
    public void execute() throws MojoExecutionException {
        JavaCodeGenerator.getInstance().generatePojoFromSchema(schema, outputDir, packageName);
    }
}