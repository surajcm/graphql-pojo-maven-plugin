package com.github.surajcm.generation;

import com.github.surajcm.mapper.TypeMapper;
import com.github.surajcm.model.GraphQLFieldInfo;
import com.github.surajcm.model.GraphQLTypeInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generates Java POJO source code from GraphQL type definitions.
 */
public class SourceCodeGenerator {

    private static SourceCodeGenerator instance;
    private final TypeMapper typeMapper;

    private SourceCodeGenerator() {
        this.typeMapper = TypeMapper.getInstance();
    }

    public static synchronized SourceCodeGenerator getInstance() {
        if (instance == null) {
            instance = new SourceCodeGenerator();
        }
        return instance;
    }

    /**
     * Generates a Java POJO file for the given GraphQL type.
     *
     * @param typeInfo the GraphQL type information
     * @param outputDir the output directory
     * @param packageName the package name for the generated class
     * @return the generated JavaFile
     * @throws IOException if file writing fails
     */
    public JavaFile generatePojo(GraphQLTypeInfo typeInfo, File outputDir, String packageName)
            throws IOException {
        typeMapper.setTargetPackage(packageName);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(typeInfo.getName())
                .addModifiers(Modifier.PUBLIC);

        List<FieldSpec> fields = new ArrayList<>();

        for (GraphQLFieldInfo fieldInfo : typeInfo.getFields()) {
            TypeName fieldType = typeMapper.mapFieldType(fieldInfo);
            FieldSpec field = FieldSpec.builder(fieldType, fieldInfo.getName(), Modifier.PRIVATE)
                    .build();
            fields.add(field);
            classBuilder.addField(field);
        }

        // Add no-arg constructor
        classBuilder.addMethod(generateNoArgConstructor());

        // Add all-args constructor if there are fields
        if (!fields.isEmpty()) {
            classBuilder.addMethod(generateAllArgsConstructor(typeInfo.getFields(), packageName));
        }

        // Add getters and setters
        for (GraphQLFieldInfo fieldInfo : typeInfo.getFields()) {
            TypeName fieldType = typeMapper.mapFieldType(fieldInfo);
            classBuilder.addMethod(generateGetter(fieldInfo.getName(), fieldType));
            classBuilder.addMethod(generateSetter(fieldInfo.getName(), fieldType));
        }

        // Add toString, equals, hashCode
        classBuilder.addMethod(generateToString(typeInfo.getName(), typeInfo.getFields()));
        classBuilder.addMethod(generateEquals(typeInfo.getName(), typeInfo.getFields()));
        classBuilder.addMethod(generateHashCode(typeInfo.getFields()));

        TypeSpec typeSpec = classBuilder.build();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .indent("    ")
                .build();

        if (outputDir != null) {
            javaFile.writeTo(outputDir);
        }

        return javaFile;
    }

    /**
     * Generates a JavaFile without writing to disk (useful for testing).
     *
     * @param typeInfo the GraphQL type information
     * @param packageName the package name for the generated class
     * @return the generated JavaFile
     */
    public JavaFile generatePojo(GraphQLTypeInfo typeInfo, String packageName) {
        try {
            return generatePojo(typeInfo, null, packageName);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException without file output", e);
        }
    }

    private MethodSpec generateNoArgConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    private MethodSpec generateAllArgsConstructor(List<GraphQLFieldInfo> fields, String packageName) {
        typeMapper.setTargetPackage(packageName);
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        for (GraphQLFieldInfo fieldInfo : fields) {
            TypeName fieldType = typeMapper.mapFieldType(fieldInfo);
            constructor.addParameter(fieldType, fieldInfo.getName());
            constructor.addStatement("this.$N = $N", fieldInfo.getName(), fieldInfo.getName());
        }

        return constructor.build();
    }

    private MethodSpec generateGetter(String fieldName, TypeName fieldType) {
        String methodName = "get" + capitalize(fieldName);
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(fieldType)
                .addStatement("return $N", fieldName)
                .build();
    }

    private MethodSpec generateSetter(String fieldName, TypeName fieldType) {
        String methodName = "set" + capitalize(fieldName);
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(fieldType, fieldName)
                .addStatement("this.$N = $N", fieldName, fieldName)
                .build();
    }

    private MethodSpec generateToString(String className, List<GraphQLFieldInfo> fields) {
        MethodSpec.Builder method = MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class);

        if (fields.isEmpty()) {
            method.addStatement("return $S", className + "{}");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("return \"").append(className).append("{\" +\n");

            for (int i = 0; i < fields.size(); i++) {
                String fieldName = fields.get(i).getName();
                if (i == 0) {
                    sb.append("        \"").append(fieldName).append("=\" + ").append(fieldName);
                } else {
                    sb.append(" +\n        \", ").append(fieldName).append("=\" + ").append(fieldName);
                }
            }
            sb.append(" +\n        \"}\"");

            method.addStatement(sb.toString());
        }

        return method.build();
    }

    private MethodSpec generateEquals(String className, List<GraphQLFieldInfo> fields) {
        MethodSpec.Builder method = MethodSpec.methodBuilder("equals")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(Object.class, "o");

        method.addStatement("if (this == o) return true");
        method.addStatement("if (o == null || getClass() != o.getClass()) return false");

        if (fields.isEmpty()) {
            method.addStatement("return true");
        } else {
            method.addStatement("$N that = ($N) o", className, className);

            StringBuilder comparison = new StringBuilder("return ");
            for (int i = 0; i < fields.size(); i++) {
                String fieldName = fields.get(i).getName();
                if (i > 0) {
                    comparison.append(" &&\n        ");
                }
                comparison.append("$T.equals(").append(fieldName).append(", that.").append(fieldName).append(")");
            }

            Object[] args = new Object[fields.size()];
            for (int i = 0; i < fields.size(); i++) {
                args[i] = Objects.class;
            }
            method.addStatement(comparison.toString(), args);
        }

        return method.build();
    }

    private MethodSpec generateHashCode(List<GraphQLFieldInfo> fields) {
        MethodSpec.Builder method = MethodSpec.methodBuilder("hashCode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class);

        if (fields.isEmpty()) {
            method.addStatement("return 0");
        } else {
            StringBuilder fieldNames = new StringBuilder();
            for (int i = 0; i < fields.size(); i++) {
                if (i > 0) {
                    fieldNames.append(", ");
                }
                fieldNames.append(fields.get(i).getName());
            }
            method.addStatement("return $T.hash($L)", Objects.class, fieldNames.toString());
        }

        return method.build();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Resets the singleton instance (useful for testing).
     */
    public static void resetInstance() {
        instance = null;
    }
}
