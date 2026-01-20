package com.github.surajcm.mapper;

import com.github.surajcm.model.GraphQLFieldInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps GraphQL types to Java types using JavaPoet TypeName.
 */
public final class TypeMapper {

    private static TypeMapper instance;

    private final Map<String, TypeName> scalarMappings;
    private String targetPackage;

    private TypeMapper() {
        scalarMappings = new HashMap<>();
        initializeScalarMappings();
    }

    public static TypeMapper getInstance() {
        if (instance == null) {
            instance = new TypeMapper();
        }
        return instance;
    }

    private void initializeScalarMappings() {
        scalarMappings.put("String", ClassName.get(String.class));
        scalarMappings.put("Int", ClassName.get(Integer.class));
        scalarMappings.put("Float", ClassName.get(Double.class));
        scalarMappings.put("Boolean", ClassName.get(Boolean.class));
        scalarMappings.put("ID", ClassName.get(String.class));
        scalarMappings.put("Long", ClassName.get(Long.class));
        scalarMappings.put("Short", ClassName.get(Short.class));
        scalarMappings.put("Byte", ClassName.get(Byte.class));
        scalarMappings.put("BigDecimal", ClassName.get("java.math", "BigDecimal"));
        scalarMappings.put("BigInteger", ClassName.get("java.math", "BigInteger"));
    }

    /**
     * Sets the target package for custom type resolution.
     *
     * @param packageName the package where generated types will reside
     */
    public void setTargetPackage(String packageName) {
        this.targetPackage = packageName;
    }

    /**
     * Gets the target package for custom type resolution.
     *
     * @return the target package name
     */
    public String getTargetPackage() {
        return targetPackage;
    }

    /**
     * Adds a custom scalar mapping.
     *
     * @param graphQLType the GraphQL scalar type name
     * @param javaType the Java TypeName to map to
     */
    public void addScalarMapping(String graphQLType, TypeName javaType) {
        scalarMappings.put(graphQLType, javaType);
    }

    /**
     * Checks if a type is a known scalar type.
     *
     * @param typeName the GraphQL type name
     * @return true if it's a scalar type
     */
    public boolean isScalarType(String typeName) {
        return scalarMappings.containsKey(typeName);
    }

    /**
     * Maps a GraphQL field to a Java TypeName.
     *
     * @param fieldInfo the GraphQL field information
     * @return the corresponding Java TypeName
     */
    public TypeName mapFieldType(GraphQLFieldInfo fieldInfo) {
        TypeName baseType = mapBaseType(fieldInfo.getTypeName());

        if (fieldInfo.isList()) {
            return ParameterizedTypeName.get(ClassName.get(List.class), baseType);
        }

        return baseType;
    }

    /**
     * Maps a GraphQL type name to a Java TypeName.
     *
     * @param graphQLTypeName the GraphQL type name
     * @return the corresponding Java TypeName
     */
    public TypeName mapBaseType(String graphQLTypeName) {
        TypeName scalarType = scalarMappings.get(graphQLTypeName);
        if (scalarType != null) {
            return scalarType;
        }

        if (targetPackage != null && !targetPackage.isEmpty()) {
            return ClassName.get(targetPackage, graphQLTypeName);
        }

        return ClassName.get("", graphQLTypeName);
    }

    /**
     * Maps a GraphQL scalar type name to a Java TypeName.
     * Returns null if the type is not a scalar.
     *
     * @param graphQLTypeName the GraphQL scalar type name
     * @return the corresponding Java TypeName, or null if not a scalar
     */
    public TypeName mapScalarType(String graphQLTypeName) {
        return scalarMappings.get(graphQLTypeName);
    }

    /**
     * Gets the Java class name for a custom GraphQL type.
     *
     * @param graphQLTypeName the GraphQL type name
     * @return the ClassName for the custom type
     */
    public ClassName getCustomTypeClassName(String graphQLTypeName) {
        if (targetPackage != null && !targetPackage.isEmpty()) {
            return ClassName.get(targetPackage, graphQLTypeName);
        }
        return ClassName.get("", graphQLTypeName);
    }

    /**
     * Resets the singleton instance (useful for testing).
     */
    public static void resetInstance() {
        instance = null;
    }
}
