package com.github.surajcm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a GraphQL type definition (object type, input type, etc.).
 *
 * <p>This class encapsulates metadata about a GraphQL type, including its name,
 * kind (object, input object, interface), and the list of fields it contains.</p>
 *
 * @author Suraj Muraleedharan
 * @since 1.0.0
 * @see GraphQLTypeKind
 * @see GraphQLFieldInfo
 */
public class GraphQLTypeInfo {

    private final String name;
    private final GraphQLTypeKind kind;
    private final List<GraphQLFieldInfo> fields;

    /**
     * Constructs a new GraphQLTypeInfo with the specified attributes.
     *
     * @param name the name of the GraphQL type
     * @param kind the kind of the GraphQL type (object, input object, etc.)
     * @param fields the list of fields belonging to this type
     */
    public GraphQLTypeInfo(String name, GraphQLTypeKind kind, List<GraphQLFieldInfo> fields) {
        this.name = name;
        this.kind = kind;
        this.fields = fields != null ? new ArrayList<>(fields) : new ArrayList<>();
    }

    /**
     * Returns the name of the GraphQL type.
     *
     * @return the type name (e.g., "User", "CreateUserInput")
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the kind of the GraphQL type.
     *
     * @return the type kind (OBJECT, INPUT_OBJECT, INTERFACE, etc.)
     */
    public GraphQLTypeKind getKind() {
        return kind;
    }

    /**
     * Returns an unmodifiable list of fields belonging to this type.
     *
     * @return the list of fields, never null
     */
    public List<GraphQLFieldInfo> getFields() {
        return Collections.unmodifiableList(fields);
    }

    @Override
    public String toString() {
        return "GraphQLTypeInfo{"
                + "name='" + name + '\''
                + ", kind=" + kind
                + ", fields=" + fields
                + '}';
    }
}
