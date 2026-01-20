package com.github.surajcm.parser;

import com.github.surajcm.model.GraphQLEnumInfo;
import com.github.surajcm.model.GraphQLFieldInfo;
import com.github.surajcm.model.GraphQLTypeInfo;
import com.github.surajcm.model.GraphQLTypeKind;
import graphql.language.EnumTypeDefinition;
import graphql.language.EnumValueDefinition;
import graphql.language.FieldDefinition;
import graphql.language.InputObjectTypeDefinition;
import graphql.language.InputValueDefinition;
import graphql.language.InterfaceTypeDefinition;
import graphql.language.ListType;
import graphql.language.NonNullType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Extracts type information from a GraphQL TypeDefinitionRegistry.
 */
public final class SchemaTypeExtractor {

    private static SchemaTypeExtractor instance;

    private static final Set<String> BUILT_IN_TYPES = new HashSet<>(Arrays.asList(
            "Query", "Mutation", "Subscription"
    ));

    private SchemaTypeExtractor() {
    }

    public static SchemaTypeExtractor getInstance() {
        if (instance == null) {
            instance = new SchemaTypeExtractor();
        }
        return instance;
    }

    /**
     * Extracts all object types from the registry (excluding built-in types).
     *
     * @param registry the TypeDefinitionRegistry to extract from
     * @return list of GraphQLTypeInfo representing object types
     */
    public List<GraphQLTypeInfo> extractObjectTypes(TypeDefinitionRegistry registry) {
        List<GraphQLTypeInfo> types = new ArrayList<>();

        for (ObjectTypeDefinition typeDef : registry.getTypes(ObjectTypeDefinition.class)) {
            if (!BUILT_IN_TYPES.contains(typeDef.getName())) {
                List<GraphQLFieldInfo> fields = extractFields(typeDef.getFieldDefinitions());
                types.add(new GraphQLTypeInfo(typeDef.getName(), GraphQLTypeKind.OBJECT, fields));
            }
        }

        return types;
    }

    /**
     * Extracts all input object types from the registry.
     *
     * @param registry the TypeDefinitionRegistry to extract from
     * @return list of GraphQLTypeInfo representing input types
     */
    public List<GraphQLTypeInfo> extractInputTypes(TypeDefinitionRegistry registry) {
        List<GraphQLTypeInfo> types = new ArrayList<>();

        for (InputObjectTypeDefinition typeDef : registry.getTypes(InputObjectTypeDefinition.class)) {
            List<GraphQLFieldInfo> fields = extractInputFields(typeDef.getInputValueDefinitions());
            types.add(new GraphQLTypeInfo(typeDef.getName(), GraphQLTypeKind.INPUT_OBJECT, fields));
        }

        return types;
    }

    /**
     * Extracts all enum types from the registry.
     *
     * @param registry the TypeDefinitionRegistry to extract from
     * @return list of GraphQLEnumInfo representing enum types
     */
    public List<GraphQLEnumInfo> extractEnumTypes(TypeDefinitionRegistry registry) {
        List<GraphQLEnumInfo> enums = new ArrayList<>();

        for (EnumTypeDefinition enumDef : registry.getTypes(EnumTypeDefinition.class)) {
            List<String> values = enumDef.getEnumValueDefinitions().stream()
                    .map(EnumValueDefinition::getName)
                    .collect(Collectors.toList());
            enums.add(new GraphQLEnumInfo(enumDef.getName(), values));
        }

        return enums;
    }

    /**
     * Extracts all interface types from the registry.
     *
     * @param registry the TypeDefinitionRegistry to extract from
     * @return list of GraphQLTypeInfo representing interface types
     */
    public List<GraphQLTypeInfo> extractInterfaceTypes(TypeDefinitionRegistry registry) {
        List<GraphQLTypeInfo> types = new ArrayList<>();

        for (InterfaceTypeDefinition typeDef : registry.getTypes(InterfaceTypeDefinition.class)) {
            List<GraphQLFieldInfo> fields = extractFields(typeDef.getFieldDefinitions());
            types.add(new GraphQLTypeInfo(typeDef.getName(), GraphQLTypeKind.INTERFACE, fields));
        }

        return types;
    }

    /**
     * Extracts all types (object, input, interface) from the registry.
     *
     * @param registry the TypeDefinitionRegistry to extract from
     * @return list of all GraphQLTypeInfo
     */
    public List<GraphQLTypeInfo> extractAllTypes(TypeDefinitionRegistry registry) {
        List<GraphQLTypeInfo> allTypes = new ArrayList<>();
        allTypes.addAll(extractObjectTypes(registry));
        allTypes.addAll(extractInputTypes(registry));
        allTypes.addAll(extractInterfaceTypes(registry));
        return allTypes;
    }

    private List<GraphQLFieldInfo> extractFields(List<FieldDefinition> fieldDefinitions) {
        return fieldDefinitions.stream()
                .map(this::convertFieldDefinition)
                .collect(Collectors.toList());
    }

    private List<GraphQLFieldInfo> extractInputFields(List<InputValueDefinition> inputValueDefinitions) {
        return inputValueDefinitions.stream()
                .map(this::convertInputValueDefinition)
                .collect(Collectors.toList());
    }

    private GraphQLFieldInfo convertFieldDefinition(FieldDefinition fieldDef) {
        return extractFieldInfo(fieldDef.getName(), fieldDef.getType());
    }

    private GraphQLFieldInfo convertInputValueDefinition(InputValueDefinition inputDef) {
        return extractFieldInfo(inputDef.getName(), inputDef.getType());
    }

    private GraphQLFieldInfo extractFieldInfo(String name, Type<?> type) {
        boolean isNonNull = false;
        boolean isList = false;
        String typeName;

        Type<?> currentType = type;

        if (currentType instanceof NonNullType) {
            isNonNull = true;
            currentType = ((NonNullType) currentType).getType();
        }

        if (currentType instanceof ListType) {
            isList = true;
            currentType = ((ListType) currentType).getType();

            if (currentType instanceof NonNullType) {
                currentType = ((NonNullType) currentType).getType();
            }
        }

        if (currentType instanceof TypeName) {
            typeName = ((TypeName) currentType).getName();
        } else {
            typeName = "Unknown";
        }

        return new GraphQLFieldInfo(name, typeName, isList, isNonNull);
    }
}
