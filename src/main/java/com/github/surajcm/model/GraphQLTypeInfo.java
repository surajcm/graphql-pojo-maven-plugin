package com.github.surajcm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a GraphQL type definition (object type, input type, etc.).
 */
public class GraphQLTypeInfo {

    private final String name;
    private final GraphQLTypeKind kind;
    private final List<GraphQLFieldInfo> fields;

    public GraphQLTypeInfo(String name, GraphQLTypeKind kind, List<GraphQLFieldInfo> fields) {
        this.name = name;
        this.kind = kind;
        this.fields = fields != null ? new ArrayList<>(fields) : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public GraphQLTypeKind getKind() {
        return kind;
    }

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
