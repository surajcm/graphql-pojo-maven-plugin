package com.github.surajcm.model;

/**
 * Represents a field in a GraphQL type definition.
 */
public class GraphQLFieldInfo {

    private final String name;
    private final String typeName;
    private final boolean isList;
    private final boolean isNonNull;

    public GraphQLFieldInfo(String name, String typeName, boolean isList, boolean isNonNull) {
        this.name = name;
        this.typeName = typeName;
        this.isList = isList;
        this.isNonNull = isNonNull;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isList() {
        return isList;
    }

    public boolean isNonNull() {
        return isNonNull;
    }

    @Override
    public String toString() {
        return "GraphQLFieldInfo{"
                + "name='" + name + '\''
                + ", typeName='" + typeName + '\''
                + ", isList=" + isList
                + ", isNonNull=" + isNonNull
                + '}';
    }
}
