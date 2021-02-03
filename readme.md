<h1 align="center">
    graphql-pojo-maven-plugin (Work In Progress)
</h1>
<hr />
A simple graphql schema to pojo generator for java. Doesn't depend on any other graphql / java frameworks.


### Plugin Configurations

```xml
<build>
    <plugins>
        ...
        <plugin>
            <groupId>com.suraj.graphql</groupId>
            <artifactId>graphql-pojo-maven-plugin</artifactId>
            <version>1.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <configuration>
                        <schema>src/main/resources/schema.graphqls</schema>
                        <outputDir>${project.build.directory}/src/main/java/</outputDir>
                        <packageName>com.company.project.schema</packageName>
                    </configuration>
                </execution>
            </executions>
        </plugin>
        ...
    </plugins>
</build>
```
<sub>* Both `schema` and `outputDir` are optional, and they default to the values provided in the sample above</sub>

The plugin will run automatically as part of the maven lifecycle when compiling your code , also you can run the plugin manually with `mvn generate-sources`.
