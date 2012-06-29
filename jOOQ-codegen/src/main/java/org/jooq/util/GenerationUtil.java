/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Set;

import org.jooq.SQLDialect;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.util.h2.H2DataType;

/**
 * @author Lukas Eder
 */
class GenerationUtil {

    private static Set<String> JAVA_KEYWORDS = unmodifiableSet(new HashSet<String>(asList(
         "abstract",
         "assert",
         "boolean",
         "break",
         "byte",
         "case",
         "catch",
         "char",
         "class",
         "const",
         "continue",
         "default",
         "double",
         "do",
         "else",
         "enum",
         "extends",
         "false",
         "final",
         "finally",
         "float",
         "for",
         "goto",
         "if",
         "implements",
         "import",
         "instanceof",
         "interface",
         "int",
         "long",
         "native",
         "new",
         "package",
         "private",
         "protected",
         "public",
         "return",
         "short",
         "static",
         "strictfp",
         "super",
         "switch",
         "synchronized",
         "this",
         "throw",
         "throws",
         "transient",
         "true",
         "try",
         "void",
         "volatile",
         "while")));

    /**
     * Take a literal (e.g. database column) and make it a Java identifier to be
     * used without case-change as an enum identifier
     * <p>
     * [#959] These literals are escaped if they collide with reserved words.
     * This implementation is meant as a fix for [#959]. These types of
     * collisions have to be generally reviewed again, when allowing for more
     * control over generated source code, as of [#408][#911]
     */
    public static String convertToJavaIdentifier(String literal) {
        if (JAVA_KEYWORDS.contains(literal)) {
            return literal + "_";
        }

        StringBuilder sb = new StringBuilder();

        if ("".equals(literal)) {
            return "_";
        }

        if (!Character.isJavaIdentifierStart(literal.charAt(0))) {
            sb.append("_");
        }

        for (int i = 0; i < literal.length(); i++) {
            char c = literal.charAt(i);

            if (!Character.isJavaIdentifierPart(c)) {
                sb.append("_");
            }
            else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Take a qualified Java type and make it a simple type
     *
     * @see Class#getSimpleName()
     */
    static String getSimpleJavaType(String qualifiedJavaType) {
        if (qualifiedJavaType == null) {
            return null;
        }

        return qualifiedJavaType.replaceAll(".*\\.", "");
    }

    /**
     * Gets the base type for an array type, depending on the RDBMS dialect
     */
    static String getArrayBaseType(SQLDialect dialect, String t, String u) {
        switch (dialect) {
            case POSTGRES: {

                // The convention is to prepend a "_" to a type to get an array type
                if (u != null && u.startsWith("_")) {
                    return u.substring(1);
                }

                // But there are also arrays with a "vector" suffix
                else {
                    return u;
                }
            }

            case H2: {
                return H2DataType.OTHER.getTypeName();
            }

            case HSQLDB: {

                // In HSQLDB 2.2.5, there has been an incompatible INFORMATION_SCHEMA change around the
                // ELEMENT_TYPES view. Arrays are now described much more explicitly
                if ("ARRAY".equalsIgnoreCase(t)) {
                    return "OTHER";
                }

                // This is for backwards compatibility
                else {
                    return t.replace(" ARRAY", "");
                }
            }
        }

        throw new SQLDialectNotSupportedException("getArrayBaseType() is not supported for dialect " + dialect);
    }
}