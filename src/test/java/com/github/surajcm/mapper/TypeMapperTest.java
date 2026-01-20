package com.github.surajcm.mapper;

import com.github.surajcm.model.GraphQLFieldInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class TypeMapperTest {

    private TypeMapper typeMapper;

    @BeforeEach
    void setup() {
        TypeMapper.resetInstance();
        typeMapper = TypeMapper.getInstance();
    }

    @AfterEach
    void tearDown() {
        TypeMapper.resetInstance();
    }

    @Test
    void mapStringType() {
        TypeName result = typeMapper.mapBaseType("String");
        Assertions.assertEquals(ClassName.get(String.class), result);
    }

    @Test
    void mapIntType() {
        TypeName result = typeMapper.mapBaseType("Int");
        Assertions.assertEquals(ClassName.get(Integer.class), result);
    }

    @Test
    void mapFloatType() {
        TypeName result = typeMapper.mapBaseType("Float");
        Assertions.assertEquals(ClassName.get(Double.class), result);
    }

    @Test
    void mapBooleanType() {
        TypeName result = typeMapper.mapBaseType("Boolean");
        Assertions.assertEquals(ClassName.get(Boolean.class), result);
    }

    @Test
    void mapIDType() {
        TypeName result = typeMapper.mapBaseType("ID");
        Assertions.assertEquals(ClassName.get(String.class), result);
    }

    @Test
    void mapLongType() {
        TypeName result = typeMapper.mapBaseType("Long");
        Assertions.assertEquals(ClassName.get(Long.class), result);
    }

    @Test
    void mapBigDecimalType() {
        TypeName result = typeMapper.mapBaseType("BigDecimal");
        Assertions.assertEquals(ClassName.get("java.math", "BigDecimal"), result);
    }

    @Test
    void mapBigIntegerType() {
        TypeName result = typeMapper.mapBaseType("BigInteger");
        Assertions.assertEquals(ClassName.get("java.math", "BigInteger"), result);
    }

    @Test
    void mapCustomTypeWithPackage() {
        typeMapper.setTargetPackage("com.example.model");
        TypeName result = typeMapper.mapBaseType("User");
        Assertions.assertEquals(ClassName.get("com.example.model", "User"), result);
    }

    @Test
    void mapCustomTypeWithoutPackage() {
        TypeName result = typeMapper.mapBaseType("User");
        Assertions.assertEquals(ClassName.get("", "User"), result);
    }

    @Test
    void mapListFieldType() {
        GraphQLFieldInfo field = new GraphQLFieldInfo("tags", "String", true, false);
        TypeName result = typeMapper.mapFieldType(field);

        Assertions.assertTrue(result instanceof ParameterizedTypeName);
        ParameterizedTypeName paramType = (ParameterizedTypeName) result;
        Assertions.assertEquals(ClassName.get(List.class), paramType.rawType);
        Assertions.assertEquals(1, paramType.typeArguments.size());
        Assertions.assertEquals(ClassName.get(String.class), paramType.typeArguments.get(0));
    }

    @Test
    void mapListOfCustomType() {
        typeMapper.setTargetPackage("com.example.model");
        GraphQLFieldInfo field = new GraphQLFieldInfo("users", "User", true, false);
        TypeName result = typeMapper.mapFieldType(field);

        Assertions.assertTrue(result instanceof ParameterizedTypeName);
        ParameterizedTypeName paramType = (ParameterizedTypeName) result;
        Assertions.assertEquals(ClassName.get(List.class), paramType.rawType);
        Assertions.assertEquals(ClassName.get("com.example.model", "User"), paramType.typeArguments.get(0));
    }

    @Test
    void mapNonListFieldType() {
        GraphQLFieldInfo field = new GraphQLFieldInfo("name", "String", false, true);
        TypeName result = typeMapper.mapFieldType(field);
        Assertions.assertEquals(ClassName.get(String.class), result);
    }

    @Test
    void isScalarTypeReturnsTrue() {
        Assertions.assertTrue(typeMapper.isScalarType("String"));
        Assertions.assertTrue(typeMapper.isScalarType("Int"));
        Assertions.assertTrue(typeMapper.isScalarType("Float"));
        Assertions.assertTrue(typeMapper.isScalarType("Boolean"));
        Assertions.assertTrue(typeMapper.isScalarType("ID"));
        Assertions.assertTrue(typeMapper.isScalarType("Long"));
    }

    @Test
    void isScalarTypeReturnsFalse() {
        Assertions.assertFalse(typeMapper.isScalarType("User"));
        Assertions.assertFalse(typeMapper.isScalarType("Product"));
        Assertions.assertFalse(typeMapper.isScalarType("CustomType"));
    }

    @Test
    void addCustomScalarMapping() {
        ClassName dateTime = ClassName.get("java.time", "LocalDateTime");
        typeMapper.addScalarMapping("DateTime", dateTime);

        Assertions.assertTrue(typeMapper.isScalarType("DateTime"));
        Assertions.assertEquals(dateTime, typeMapper.mapBaseType("DateTime"));
    }

    @Test
    void mapScalarTypeReturnsNullForNonScalar() {
        TypeName result = typeMapper.mapScalarType("User");
        Assertions.assertNull(result);
    }

    @Test
    void mapScalarTypeReturnsTypeNameForScalar() {
        TypeName result = typeMapper.mapScalarType("String");
        Assertions.assertEquals(ClassName.get(String.class), result);
    }

    @Test
    void getCustomTypeClassName() {
        typeMapper.setTargetPackage("com.example.model");
        ClassName result = typeMapper.getCustomTypeClassName("User");
        Assertions.assertEquals("com.example.model", result.packageName());
        Assertions.assertEquals("User", result.simpleName());
    }

    @Test
    void getTargetPackage() {
        Assertions.assertNull(typeMapper.getTargetPackage());
        typeMapper.setTargetPackage("com.example");
        Assertions.assertEquals("com.example", typeMapper.getTargetPackage());
    }

    @Test
    void singletonInstanceShouldBeSame() {
        TypeMapper instance1 = TypeMapper.getInstance();
        TypeMapper instance2 = TypeMapper.getInstance();
        Assertions.assertSame(instance1, instance2);
    }

    @Test
    void mapNonNullFieldType() {
        GraphQLFieldInfo field = new GraphQLFieldInfo("id", "ID", false, true);
        TypeName result = typeMapper.mapFieldType(field);
        Assertions.assertEquals(ClassName.get(String.class), result);
    }

    @Test
    void mapNonNullListFieldType() {
        GraphQLFieldInfo field = new GraphQLFieldInfo("items", "String", true, true);
        TypeName result = typeMapper.mapFieldType(field);

        Assertions.assertTrue(result instanceof ParameterizedTypeName);
        ParameterizedTypeName paramType = (ParameterizedTypeName) result;
        Assertions.assertEquals(ClassName.get(List.class), paramType.rawType);
    }
}
