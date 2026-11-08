# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- Interface type support
- Custom scalar type mapping configuration
- Annotation support (e.g., `@NotNull`, `@Valid`)
- Builder pattern generation
- Immutable class generation option
- Custom naming strategy configuration

## [1.0.0] - 2024-01-20

### Added
- **GraphQL Schema Parsing**
  - Full support for GraphQL Schema Definition Language (SDL) files
  - Parsing of object types, input types, and enum types
  - Support for field definitions with type modifiers

- **Java POJO Generation**
  - Automatic generation of Java classes from GraphQL types
  - Generation of private fields with public getters and setters
  - Proper Java naming conventions (camelCase for fields, PascalCase for classes)

- **Type Mapping**
  - GraphQL `String` to `java.lang.String`
  - GraphQL `Int` to `java.lang.Integer`
  - GraphQL `Float` to `java.lang.Double`
  - GraphQL `Boolean` to `java.lang.Boolean`
  - GraphQL `ID` to `java.lang.String`
  - Custom object types to generated Java classes

- **List Type Support**
  - GraphQL list types (`[Type]`) mapped to `java.util.List<T>`
  - Proper handling of nested list types

- **Non-null Handling**
  - Support for GraphQL non-null modifier (`!`)
  - Preserved in generated field metadata

- **Enum Support**
  - Generation of Java enum classes from GraphQL enum types
  - Preservation of enum value names

- **Input Type Support**
  - Generation of Java classes for GraphQL input object types
  - Same structure as regular object types for consistency

- **Maven Plugin Configuration**
  - `schema` parameter: Path to GraphQL schema file (default: `src/main/resources/schema.graphqls`)
  - `outputDir` parameter: Output directory for generated files (default: `src/main/java/`)
  - `packageName` parameter: Java package name for generated classes (required)

- **Build Integration**
  - Automatic execution during `generate-sources` phase
  - Thread-safe implementation for parallel builds
  - Clean generation (removes previously generated files)

- **Documentation**
  - Comprehensive Javadoc for all public APIs
  - README with installation and usage instructions
  - Configuration reference documentation

### Technical Details
- Minimum Java version: 1.8
- Uses JavaPoet for code generation
- Uses graphql-java for schema parsing
- No runtime dependencies required in consuming projects

[Unreleased]: https://github.com/surajcm/graphql-pojo-maven-plugin/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/surajcm/graphql-pojo-maven-plugin/releases/tag/v1.0.0
