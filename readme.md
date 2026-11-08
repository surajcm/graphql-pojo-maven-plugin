# graphql-pojo-maven-plugin

[![Build Status](https://github.com/surajcm/graphql-pojo-maven-plugin/actions/workflows/build.yml/badge.svg)](https://github.com/surajcm/graphql-pojo-maven-plugin/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/surajcm/graphql-pojo-maven-plugin/blob/master/LICENSE)
[![Java Version](https://img.shields.io/badge/Java-8%2B-blue.svg)](https://www.oracle.com/java/)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.surajcm/graphql-pojo-maven-plugin.svg)](https://search.maven.org/artifact/com.github.surajcm/graphql-pojo-maven-plugin)

<a href="https://foojay.io/today/works-with-openjdk"><img align="right" src="https://github.com/foojayio/badges/raw/main/works_with_openjdk/Works-with-OpenJDK.png" width="100"></a>

A lightweight Maven plugin that generates Java POJO classes from GraphQL schema files. No runtime dependencies on GraphQL frameworks required.

## Features

- **Schema Parsing**: Parses GraphQL Schema Definition Language (SDL) files
- **Object Type Support**: Generates Java classes for GraphQL object types
- **Input Type Support**: Generates Java classes for GraphQL input types
- **Enum Support**: Generates Java enums for GraphQL enum types
- **Type Mapping**: Automatic mapping of GraphQL scalar types to Java types
- **List Support**: Proper handling of GraphQL list types as `java.util.List<T>`
- **Non-null Handling**: Support for GraphQL non-null field modifiers
- **Clean Generation**: Cleans and recreates output directory on each run
- **Thread Safe**: Plugin is thread-safe for parallel builds

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6.0 or higher

### Installation

Add the plugin to your `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.surajcm</groupId>
            <artifactId>graphql-pojo-maven-plugin</artifactId>
            <version>1.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <configuration>
                        <schema>src/main/resources/schema.graphqls</schema>
                        <outputDir>src/main/java/</outputDir>
                        <packageName>com.example.generated</packageName>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Usage

The plugin runs automatically during the `generate-sources` phase. You can also run it manually:

```bash
mvn generate-sources
```

Or run the plugin goal directly:

```bash
mvn graphql-pojo:generate
```

## Configuration Options

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| `schema` | No | `src/main/resources/schema.graphqls` | Path to the GraphQL schema file |
| `outputDir` | No | `src/main/java/` | Directory where generated Java classes will be written |
| `packageName` | **Yes** | - | Java package name for generated classes |

## GraphQL to Java Type Mapping

| GraphQL Type | Java Type |
|--------------|-----------|
| `String` | `java.lang.String` |
| `Int` | `java.lang.Integer` |
| `Float` | `java.lang.Double` |
| `Boolean` | `java.lang.Boolean` |
| `ID` | `java.lang.String` |
| `[Type]` (List) | `java.util.List<Type>` |
| Custom Object Types | Generated Java class |
| Custom Input Types | Generated Java class |
| Enum Types | Generated Java enum |

## Example

### GraphQL Schema

```graphql
type User {
    id: ID!
    name: String!
    email: String
    age: Int
    roles: [Role!]!
}

enum Role {
    ADMIN
    USER
    GUEST
}

input CreateUserInput {
    name: String!
    email: String!
    age: Int
}
```

### Generated Output

The plugin generates the following Java files:

**User.java**
```java
package com.example.generated;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private Integer age;
    private List<Role> roles;

    // Getters and setters
}
```

**Role.java**
```java
package com.example.generated;

public enum Role {
    ADMIN,
    USER,
    GUEST
}
```

**CreateUserInput.java**
```java
package com.example.generated;

public class CreateUserInput {
    private String name;
    private String email;
    private Integer age;

    // Getters and setters
}
```

## Troubleshooting

### Schema file not found

Ensure the schema file path is correct relative to the project root:

```xml
<configuration>
    <schema>${project.basedir}/src/main/resources/schema.graphqls</schema>
</configuration>
```

### Output directory issues

If the output directory doesn't exist, create it first or use the target directory:

```xml
<configuration>
    <outputDir>${project.build.directory}/generated-sources/graphql/</outputDir>
</configuration>
```

Don't forget to add the generated sources to the build:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>build-helper-maven-plugin</artifactId>
    <version>3.3.0</version>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <goals>
                <goal>add-source</goal>
            </goals>
            <configuration>
                <sources>
                    <source>${project.build.directory}/generated-sources/graphql</source>
                </sources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Invalid package name

Package names must follow Java naming conventions:
- Use lowercase letters
- Separate words with dots
- No special characters or reserved keywords

## Building from Source

Clone the repository and build with Maven:

```bash
git clone https://github.com/surajcm/graphql-pojo-maven-plugin.git
cd graphql-pojo-maven-plugin
mvn clean install
```

Run tests:

```bash
mvn test
```

Generate Javadoc:

```bash
mvn javadoc:javadoc
```

## Author

**Suraj Muraleedharan**

* GitHub: [@surajcm](https://github.com/surajcm)

## Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/surajcm/graphql-pojo-maven-plugin/issues).

Please read our [Code of Conduct](CODE_OF_CONDUCT.md) before contributing.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
