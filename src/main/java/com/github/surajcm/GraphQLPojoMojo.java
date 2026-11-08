package com.github.surajcm;

import com.github.surajcm.generation.JavaCodeGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Maven plugin Mojo that generates Java POJO classes from a GraphQL schema file.
 *
 * <p>This plugin parses GraphQL schema definitions and generates corresponding Java classes
 * including object types, input types, and enums. The generated classes include proper
 * getters, setters, and standard Java conventions.</p>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * <plugin>
 *     <groupId>com.github.surajcm</groupId>
 *     <artifactId>graphql-pojo-maven-plugin</artifactId>
 *     <version>1.0.0</version>
 *     <executions>
 *         <execution>
 *             <goals>
 *                 <goal>generate</goal>
 *             </goals>
 *             <configuration>
 *                 <schema>src/main/resources/schema.graphqls</schema>
 *                 <outputDir>src/main/java/</outputDir>
 *                 <packageName>com.example.generated</packageName>
 *             </configuration>
 *         </execution>
 *     </executions>
 * </plugin>
 * }</pre>
 *
 * <h2>GraphQL to Java Type Mapping</h2>
 * <ul>
 *     <li>{@code String} → {@code java.lang.String}</li>
 *     <li>{@code Int} → {@code java.lang.Integer}</li>
 *     <li>{@code Float} → {@code java.lang.Double}</li>
 *     <li>{@code Boolean} → {@code java.lang.Boolean}</li>
 *     <li>{@code ID} → {@code java.lang.String}</li>
 *     <li>Custom types → Generated Java classes</li>
 *     <li>List types → {@code java.util.List<T>}</li>
 *     <li>Enum types → Java enum classes</li>
 * </ul>
 *
 * @author Suraj Muraleedharan
 * @since 1.0.0
 * @see <a href="https://github.com/surajcm/graphql-pojo-maven-plugin">Project Homepage</a>
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class GraphQLPojoMojo extends AbstractMojo {

    private static final String DEFAULT_SCHEMA_LOCATION = "src/main/resources/schema.graphqls";
    private static final String DEFAULT_OUTPUT_DIR = "src/main/java/";

    /**
     * Path to the GraphQL schema file.
     *
     * <p>The schema file should be a valid GraphQL Schema Definition Language (SDL) file,
     * typically with a {@code .graphqls} or {@code .graphql} extension.</p>
     *
     * <p>Default value: {@code src/main/resources/schema.graphqls}</p>
     */
    @Parameter(defaultValue = DEFAULT_SCHEMA_LOCATION)
    private File schema;

    /**
     * Output directory where the generated Java classes will be written.
     *
     * <p>The plugin will create the necessary package directories under this path
     * based on the configured {@code packageName}.</p>
     *
     * <p>Default value: {@code src/main/java/}</p>
     */
    @Parameter(defaultValue = DEFAULT_OUTPUT_DIR)
    private File outputDir;

    /**
     * Java package name for the generated classes.
     *
     * <p>This parameter is required. All generated classes will be placed in this package.
     * The plugin will create the corresponding directory structure under {@code outputDir}.</p>
     *
     * <p>Example: {@code com.example.generated.schema}</p>
     */
    @Parameter(required = true)
    private String packageName;

    /**
     * Executes the POJO generation process.
     *
     * <p>This method reads the GraphQL schema file, parses its contents, and generates
     * corresponding Java POJO classes in the specified output directory.</p>
     *
     * @throws MojoExecutionException if schema parsing fails or file generation encounters an error
     */
    @Override
    public void execute() throws MojoExecutionException {
        JavaCodeGenerator.getInstance().generatePojoFromSchema(schema, outputDir, packageName);
    }
}