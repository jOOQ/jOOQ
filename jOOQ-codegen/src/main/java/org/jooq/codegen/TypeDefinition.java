package org.jooq.codegen;

import org.jooq.Name;

public class TypeDefinition {
    private final String type;
    private final int precision;
    private final int scale;
    private final Name userType;
    private final String javaType;
    private final String defaultType;

    public TypeDefinition(String type, int precision, int scale, Name userType, String javaType, String defaultType) {
        this.type = type;
        this.precision = precision;
        this.scale = scale;
        this.userType = userType;
        this.javaType = javaType;
        this.defaultType = defaultType;
    }

    public String getType() {
        return type;
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    public Name getUserType() {
        return userType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getDefaultType() {
        return defaultType;
    }
}