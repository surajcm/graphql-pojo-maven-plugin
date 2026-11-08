package com.github.surajcm.model;

/**
 * Represents a field in a GraphQL type definition.
 *
 * <p>This class holds metadata about a single field within a GraphQL object type,
 * input type, or interface. It captures the field name, its type, and modifiers
 * such as list and non-null constraints.</p>
 *
 * @author Suraj Muraleedharan
 * @since 1.0.0
 */
public class GraphQLFieldInfo {

    private final String name;
    private final String typeName;
    private final boolean isList;
    private final boolean isNonNull;

    /**
     * Constructs a new GraphQLFieldInfo with the specified attributes.
     *
     * @param name the name of the field
     * @param typeName the GraphQL type name of the field
     * @param isList whether the field is a list type
     * @param isNonNull whether the field is non-nullable
     */
    public GraphQLFieldInfo(String name, String typeName, boolean isList, boolean isNonNull) {
        this.name = name;
        this.typeName = typeName;
        this.isList = isList;
        this.isNonNull = isNonNull;
    }

    /**
     * Returns the name of the field.
     *
     * @return the field name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the GraphQL type name of the field.
     *
     * <p>This is the base type name without list or non-null modifiers.</p>
     *
     * @return the type name (e.g., "String", "Int", "User")
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns whether this field is a list type.
     *
     * @return {@code true} if the field is a list (e.g., [String]), {@code false} otherwise
     */
    public boolean isList() {
        return isList;
    }

    /**
     * Returns whether this field is non-nullable.
     *
     * @return {@code true} if the field is non-null (e.g., String!), {@code false} otherwise
     */
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
