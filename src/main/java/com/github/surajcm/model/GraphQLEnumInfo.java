package com.github.surajcm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a GraphQL enum type definition.
 */
public class GraphQLEnumInfo {

    private final String name;
    private final List<String> values;

    public GraphQLEnumInfo(String name, List<String> values) {
        this.name = name;
        this.values = values != null ? new ArrayList<>(values) : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

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
