# Project Milestones

This document tracks the milestones required to complete the graphql-pojo-maven-plugin.

---

## Milestone 1: GraphQL Schema Parsing

**Status:** Completed

**Objective:** Integrate a GraphQL parser and extract type definitions from schema files.

### Tasks

- [x] **1.1** Add graphql-java dependency to pom.xml
  ```xml
  <dependency>
      <groupId>com.graphql-java</groupId>
      <artifactId>graphql-java</artifactId>
      <version>17.6</version>
  </dependency>
  ```

- [x] **1.2** Create `SchemaParser` class in `com.github.surajcm.parser` package
  - Read schema file content
  - Parse using graphql-java's `SchemaParser`
  - Return `TypeDefinitionRegistry` containing all type definitions

- [x] **1.3** Create `SchemaTypeExtractor` class to extract type information
  - Extract `ObjectTypeDefinition` (GraphQL types)
  - Extract `InputObjectTypeDefinition` (input types)
  - Extract `EnumTypeDefinition` (enums)
  - Extract `InterfaceTypeDefinition` (interfaces)
  - Extract field names and types from each definition

- [x] **1.4** Create data model classes to hold parsed schema information
  - `GraphQLTypeInfo` - represents a GraphQL type
  - `GraphQLFieldInfo` - represents a field with name and type
  - `GraphQLEnumInfo` - represents an enum type with values
  - `GraphQLTypeKind` - enum for type kinds (OBJECT, INPUT_OBJECT, ENUM, INTERFACE)

- [x] **1.5** Write unit tests for schema parsing (22 tests passing)
  - Test parsing valid schema files
  - Test error handling for malformed schemas
  - Test extraction of various GraphQL type structures

---

## Milestone 2: GraphQL to Java Type Mapping

**Status:** Completed

**Objective:** Map GraphQL types to appropriate Java types.

### Tasks

- [x] **2.1** Create `TypeMapper` class for GraphQL → Java type conversion
  - `String` → `java.lang.String`
  - `Int` → `java.lang.Integer`
  - `Float` → `java.lang.Double`
  - `Boolean` → `java.lang.Boolean`
  - `ID` → `java.lang.String`
  - `Long` → `java.lang.Long`
  - `Short` → `java.lang.Short`
  - `Byte` → `java.lang.Byte`
  - `BigDecimal` → `java.math.BigDecimal`
  - `BigInteger` → `java.math.BigInteger`
  - Custom scalar support via `addScalarMapping()`

- [x] **2.2** Handle list types (`[Type]` → `List<Type>`)

- [x] **2.3** Handle non-null types (`Type!` → tracked in GraphQLFieldInfo)

- [x] **2.4** Handle nested/complex types (references to other schema types with configurable package)

- [x] **2.5** Write unit tests for type mapping (23 tests passing)
  - Test all scalar mappings
  - Test list type mappings
  - Test custom type mappings
  - Test custom scalar additions

---

## Milestone 3: Java POJO Generation

**Status:** Completed

**Objective:** Generate proper Java POJO classes using JavaPoet.

### Tasks

- [x] **3.1** Refactor `SourceCodeGenerator` to accept parsed type information
  - Removed hardcoded `HelloWorld123` class
  - Accepts `GraphQLTypeInfo` as input
  - Uses `TypeMapper` for type conversion

- [x] **3.2** Generate class structure for each GraphQL type
  - Creates class with correct package and name
  - Adds private fields for each GraphQL field
  - Uses mapped Java types for field types (including List<T> for arrays)

- [x] **3.3** Generate getters and setters for all fields
  - Follows JavaBean naming conventions (getXxx/setXxx)
  - Handles all scalar and custom types correctly

- [x] **3.4** Generate constructors
  - No-arg constructor (for serialization/frameworks)
  - All-args constructor for all fields

- [x] **3.5** Generate `toString()`, `equals()`, and `hashCode()` methods
  - Uses `Objects.equals()` and `Objects.hash()` for proper implementation
  - Handles edge case of classes with no fields

- [x] **3.6** Write unit tests for POJO generation (16 tests passing)
  - Test generated class structure
  - Test all scalar types, list types, custom types
  - Test constructors, getters, setters
  - Test toString, equals, hashCode
  - Test file output

---

## Milestone 4: File Output Implementation

**Status:** Completed

**Objective:** Write generated Java files to the file system.

### Tasks

- [x] **4.1** Fix `SourceCodeGenerator.generatePojo()` to write to output directory
  - Changed `javaFile.writeTo(System.out)` to `javaFile.writeTo(outputDir)`
  - Pass output directory path to generator (optional - null skips file writing)

- [x] **4.2** Ensure proper package directory structure is created
  - JavaPoet's `writeTo(File)` creates package directories automatically
  - Leverages existing `FileUtils.cleanAndRecreateOutputDir()`

- [x] **4.3** Handle file overwrite scenarios
  - Clean existing generated files before regeneration via `JavaCodeGenerator`

- [ ] **4.4** Add logging for file generation operations (optional enhancement)
  - Log each file created
  - Report summary (files generated, total types processed)

- [x] **4.5** Write integration tests for file output
  - Verify files are created in correct location
  - Verify file contents match expected output
  - Clean up test artifacts (using temp directories)

---

## Milestone 5: End-to-End Integration

**Status:** Completed

**Objective:** Wire all components together in the Maven plugin lifecycle.

### Tasks

- [x] **5.1** Update `JavaCodeGenerator.generatePojoFromSchema()` to use new components
  - Calls `SchemaParser` to parse schema file
  - Calls `SchemaTypeExtractor` to extract object and input types
  - Calls `SourceCodeGenerator` for each type (which uses `TypeMapper` internally)
  - Writes generated files to output directory

- [x] **5.2** Error handling
  - Wraps `ValidationException` and `IOException` as `MojoExecutionException`
  - Provides clear error messages for parsing failures

- [x] **5.3** Create comprehensive integration test (5 tests in JavaCodeGeneratorTest)
  - Uses sample schema from `test/resources/schema.graphqls`
  - Generates POJOs and verifies correctness
  - Verifies generated file content

- [ ] **5.4** Update test schema with more complex types (optional enhancement)
  - Add enums
  - Add nested types
  - Add list fields
  - Add nullable and non-nullable fields

---

## Milestone 6: Enum Support

**Status:** Completed

**Objective:** Generate Java enums from GraphQL enum definitions.

### Tasks

- [x] **6.1** Extend `SchemaTypeExtractor` to handle `EnumTypeDefinition`

- [x] **6.2** Create `EnumGenerator` class using JavaPoet
  - Generate enum with all values
  - Singleton pattern implementation
  - Support for generating without file output (for testing)

- [x] **6.3** Wire enum generation into main flow
  - Extract enum types from schema in `JavaCodeGenerator`
  - Generate enum files alongside POJO files

- [x] **6.4** Write tests for enum generation (9 unit tests + 3 integration tests)
  - Test simple enum generation
  - Test enum with multiple values
  - Test file output
  - Integration tests verify end-to-end enum generation from schema

---

## Milestone 7: Input Type Support

**Status:** Completed

**Objective:** Generate Java classes from GraphQL input types.

### Tasks

- [x] **7.1** Extend `SchemaTypeExtractor` to handle `InputObjectTypeDefinition`

- [x] **7.2** Generate Java classes for input types (similar to object types)
  - `JavaCodeGenerator` processes input types alongside object types
  - Uses the same `SourceCodeGenerator` for POJO generation

- [x] **7.3** Write tests for input type generation
  - Covered by existing tests (input types are generated identically to object types)

---

## Milestone 8: Documentation & Release Preparation

**Status:** Not Started

**Objective:** Prepare the plugin for public release.

### Tasks

- [ ] **8.1** Update README.md with:
  - Complete usage instructions
  - Configuration options
  - Examples with various schema types
  - Troubleshooting guide

- [ ] **8.2** Add Javadoc to all public APIs

- [ ] **8.3** Configure Maven Central publishing
  - Set up GPG signing
  - Configure OSSRH deployment
  - Add necessary POM metadata (SCM, developers, etc.)

- [ ] **8.4** Create release workflow in GitHub Actions

- [ ] **8.5** Add CHANGELOG.md

---

## Progress Summary

| Milestone | Status | Progress |
|-----------|--------|----------|
| 1. GraphQL Schema Parsing | **Completed** | 100% |
| 2. GraphQL to Java Type Mapping | **Completed** | 100% |
| 3. Java POJO Generation | **Completed** | 100% |
| 4. File Output Implementation | **Completed** | 100% |
| 5. End-to-End Integration | **Completed** | 100% |
| 6. Enum Support | **Completed** | 100% |
| 7. Input Type Support | **Completed** | 100% |
| 8. Documentation & Release | Not Started | 0% |

**Overall Project Completion:** ~87.5%

**Notes:**
- Core functionality is complete: the plugin now parses GraphQL schemas and generates Java POJOs, enums, and input types
- Milestone 6 (Enum Support) is now complete with `EnumGenerator` class and comprehensive tests
- Milestone 7 (Input Type Support) is complete - input types are generated as POJOs just like object types
- Milestone 8 (Documentation & Release) is optional for basic functionality

---

## Quick Wins (Can Be Done Immediately)

1. ~~Fix file output in `SourceCodeGenerator`~~ **DONE** - Now writes to output directory
2. ~~Add graphql-java dependency to pom.xml~~ **DONE**
3. Fix TODO in `FileUtils.java:50` (low priority - current implementation works)
4. ~~Enable file deletion in `SourceCodeGeneratorTest.java:31`~~ **DONE** - Tests now use temp directories with proper cleanup

---

## Dependencies Between Milestones

```
Milestone 1 (Parsing) ──┐
                        ├──► Milestone 5 (Integration)
Milestone 2 (Mapping) ──┤
                        │
Milestone 3 (POJO Gen) ─┤
                        │
Milestone 4 (File Out) ─┘

Milestone 5 ──► Milestone 6 (Enums)
           └──► Milestone 7 (Input Types)
           └──► Milestone 8 (Release)
```

Milestones 1-4 can be worked on in parallel. Milestone 5 requires 1-4 to be complete. Milestones 6-8 depend on 5.
