/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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


import static org.jooq.impl.FieldTypeHelper.normalise;

import org.jooq.SQLDialect;
import org.jooq.util.oracle.OracleDataType;


/**
 * @author Lukas Eder
 */
public class DefaultDataTypeDefinition implements DataTypeDefinition {

    private final Database database;
    private final String   typeName;
    private final String   udtName;
    private final int      precision;
    private final int      scale;

    public DefaultDataTypeDefinition(Database database, String typeName, Number precision, Number scale) {
        this(database, typeName, precision, scale, typeName);
    }

    public DefaultDataTypeDefinition(Database database, String typeName, Number precision, Number scale, String udtName) {
        this.database = database;
        this.typeName = typeName;
        this.udtName = udtName;
        this.precision = precision == null ? 0 : precision.intValue();
        this.scale = scale == null ? 0 : scale.intValue();
    }

    @Override
    public final Database getDatabase() {
        return database;
    }

    private final SQLDialect getDialect() {
        return getDatabase().getDialect();
    }

    @Override
    public final boolean isUDT() {
        return getDatabase().getUDT(udtName) != null;
    }

    @Override
    public final String getType() {
        return typeName;
    }

    @Override
    public final int getPrecision() {
        return precision;
    }

    @Override
    public final int getScale() {
        return scale;
    }

    @Override
    public final String getUserType() {
        return udtName;
    }

    @Override
    public final boolean isGenericNumberType() {
        switch (getDialect()) {
            case ORACLE: {
                return (OracleDataType.NUMBER.getTypeName().equalsIgnoreCase(typeName)
                    && precision == 0
                    && scale == 0);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
        result = prime * result + ((udtName == null) ? 0 : udtName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DefaultDataTypeDefinition) {
            DefaultDataTypeDefinition other = (DefaultDataTypeDefinition) obj;

            if (normalise(typeName).equals(normalise(other.typeName)) &&
                normalise(udtName).equals(normalise(other.udtName))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DataType [ t=");
        sb.append(typeName);
        sb.append("; p=");
        sb.append(precision);
        sb.append("; s=");
        sb.append(scale);
        sb.append("; u=");
        sb.append(udtName);
        sb.append(" ]");

        return sb.toString();
    }
}
