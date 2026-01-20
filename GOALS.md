# Project Goals

## Overview

**graphql-pojo-maven-plugin** is a Maven plugin that automatically generates Java POJO (Plain Old Java Object) classes from GraphQL schema definitions. This eliminates manual boilerplate code writing and ensures consistency between GraphQL schemas and Java type representations.

## Current Goal

Build a fully functional Maven plugin that:
1. Reads GraphQL schema files (`.graphqls`)
2. Parses GraphQL type definitions (types, queries, mutations, scalars, enums)
3. Generates corresponding Java POJO classes with appropriate fields and types
4. Writes generated files to a configurable output directory with proper package structure

## Current State

The project has established:
- Maven plugin infrastructure (Mojo configuration, lifecycle binding)
- Input validation framework
- File system utilities for directory management
- CI/CD pipeline with GitHub Actions
- Code quality tooling (Checkstyle, PMD, JaCoCo)

**What's Missing:**
- GraphQL schema parsing (no parser library integrated)
- Actual POJO generation logic (currently outputs a placeholder `HelloWorld123` class)
- File output functionality (writes to stdout instead of files)

## Future Goals

### Phase 1: Core Functionality
- [ ] Integrate GraphQL parser library (graphql-java)
- [ ] Parse basic GraphQL types from schema files
- [ ] Generate Java POJOs with fields matching GraphQL type fields
- [ ] Map GraphQL scalar types to Java types (String, Int, Float, Boolean, ID)
- [ ] Write generated files to the configured output directory

### Phase 2: Enhanced Type Support
- [ ] Support GraphQL enums → Java enums
- [ ] Support GraphQL input types → Java classes
- [ ] Support GraphQL interfaces → Java interfaces
- [ ] Support GraphQL unions → Java marker interfaces or sealed classes
- [ ] Handle nullable vs non-nullable fields (with annotations)
- [ ] Support custom scalar type mappings

### Phase 3: Code Generation Quality
- [ ] Generate proper getters and setters (or use Lombok annotations)
- [ ] Generate builders for complex types
- [ ] Generate `toString()`, `equals()`, and `hashCode()` methods
- [ ] Support Java Bean validation annotations
- [ ] Add Javadoc comments from GraphQL descriptions

### Phase 4: Advanced Features
- [ ] Support for GraphQL directives
- [ ] Configuration for output style (records vs classes, Lombok vs plain)
- [ ] Support for custom type mappings via configuration
- [ ] Incremental generation (only regenerate changed types)
- [ ] Support for multiple schema files
- [ ] Integration with graphql-java-codegen patterns

### Phase 5: Ecosystem Integration
- [ ] Publish to Maven Central
- [ ] Gradle plugin variant
- [ ] IDE plugin support (IntelliJ, Eclipse)
- [ ] Integration with popular GraphQL frameworks (DGS, Spring GraphQL)

## Non-Goals

- This plugin does NOT generate GraphQL client code (queries/mutations)
- This plugin does NOT generate resolvers or server-side GraphQL handlers
- This plugin does NOT validate GraphQL schemas for correctness (relies on parser)

## Success Criteria

The plugin is considered complete when:
1. A user can point to a `.graphqls` file
2. Run `mvn generate-sources`
3. Receive properly formatted Java POJO classes in the specified package
4. Generated classes compile without errors
5. Generated classes accurately represent the GraphQL schema types
