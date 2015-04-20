/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util.vertabelo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DataType;
import org.jooq.impl.DefaultDataType;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.vertabelo.jaxb.Column;
import org.jooq.util.vertabelo.jaxb.Property;
import org.jooq.util.vertabelo.jaxb.Table;
import org.jooq.util.vertabelo.jaxb.View;
import org.jooq.util.vertabelo.jaxb.ViewColumn;

/**
 * Definition of the Vertabelo XML Table
 *
 * @author Michał Kołodziejski
 */
public class VertabeloXMLTableDefinition extends AbstractTableDefinition {

    protected Table table;
    protected View view;

    public VertabeloXMLTableDefinition(SchemaDefinition schema, Table table) {
        super(schema, table.getName(), "");

        this.table = table;
    }

    public VertabeloXMLTableDefinition(SchemaDefinition schema, View view) {
        super(schema, view.getName(), "");

        this.view = view;
    }


    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        if(table != null) {
            // table
            return getTableElements();

        } else {
            // view
            return getViewElements();
        }
    }


    protected List<ColumnDefinition> getTableElements() {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        String schemaName = getSchemaName();
        SchemaDefinition schema = getDatabase().getSchema(schemaName);

        int position = 0;
        for(Column column : table.getColumns()) {
            ++position;

            // convert data type
            DataType<?> dataType = DefaultDataType.getDataType(getDialect(), column.getType());

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
                dataType.getTypeName(),
                dataType.hasLength() ? dataType.length() : null,
                dataType.hasPrecision() ? dataType.precision() : null,
                dataType.hasScale() ? dataType.scale() : null,
                column.isNullable(),
                !StringUtils.isEmpty(column.getDefaultValue()));

            ColumnDefinition columnDefinition = new DefaultColumnDefinition(
                this,
                column.getName(),
                position,
                type,
                false,
                column.getDescription());

            result.add(columnDefinition);
        }

        return result;
    }


    protected List<ColumnDefinition> getViewElements() {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        String schemaName = getSchemaName();
        SchemaDefinition schema = getDatabase().getSchema(schemaName);

        int position = 0;
        for(ViewColumn column : view.getViewColumns()) {
            ++position;

            // convert data type
            DataType<?> dataType = DefaultDataType.getDataType(getDialect(), column.getType());

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
                dataType.getTypeName(),
                dataType.hasLength() ? dataType.length() : null,
                dataType.hasPrecision() ? dataType.precision() : null,
                dataType.hasScale() ? dataType.scale() : null,
                true,
                false);

            ColumnDefinition columnDefinition = new DefaultColumnDefinition(
                this,
                column.getName(),
                position,
                type,
                false,
                column.getDescription());

            result.add(columnDefinition);
        }

        return result;
    }

    protected String getSchemaName() {
        Property additionalProperty;

        if(table != null) {
            additionalProperty = VertabeloXMLDatabase.findAdditionalProperty("Schema", table.getProperties());
        } else {
            additionalProperty = VertabeloXMLDatabase.findAdditionalProperty("Schema", view.getProperties());
        }

        return VertabeloXMLDatabase.getAdditionalPropertyValueOrEmpty(additionalProperty);
    }
}
