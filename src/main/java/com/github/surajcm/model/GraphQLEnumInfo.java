package com.github.surajcm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a GraphQL enum type definition.
 *
 * <p>This class encapsulates metadata about a GraphQL enum type, including its name
 * and the list of possible enum values.</p>
 *
 * @author Suraj Muraleedharan
 * @since 1.0.0
 */
public class GraphQLEnumInfo {

    private final String name;
    private final List<String> values;

    /**
     * Constructs a new GraphQLEnumInfo with the specified name and values.
     *
     * @param name the name of the GraphQL enum type
     * @param values the list of enum values
     */
    public GraphQLEnumInfo(String name, List<String> values) {
        this.name = name;
        this.values = values != null ? new ArrayList<>(values) : new ArrayList<>();
    }

    /**
     * Returns the name of the GraphQL enum type.
     *
     * @return the enum type name (e.g., "Status", "OrderType")
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable list of enum values.
     *
     * @return the list of enum values, never null
     */
    public List<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public String toString() {
        return "GraphQLEnumInfo{"
                + "name='" + name + '\''
                + ", values=" + values
                + '}';
    }
}
