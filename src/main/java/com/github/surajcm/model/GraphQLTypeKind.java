package com.github.surajcm.model;

/**
 * Represents the kind of GraphQL type definition.
 */
public enum GraphQLTypeKind {

    /**
     * Object type (e.g., type User { ... }).
     */
    OBJECT,

    /**
     * Input object type (e.g., input CreateUserInput { ... }).
     */
    INPUT_OBJECT,

    /**
     * Enum type (e.g., enum Status { ... }).
     */
    ENUM,

    /**
     * Interface type (e.g., interface Node { ... }).
     */
    INTERFACE
}
