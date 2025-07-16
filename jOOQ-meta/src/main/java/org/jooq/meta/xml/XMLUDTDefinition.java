/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.meta.xml;

import static org.jooq.impl.DSL.name;
import static org.jooq.meta.xml.XMLDatabase.unbox;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.meta.AbstractUDTDefinition;
import org.jooq.meta.AttributeDefinition;
import org.jooq.meta.DefaultAttributeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.tools.StringUtils;
import org.jooq.util.xml.jaxb.Attribute;
import org.jooq.util.xml.jaxb.Column;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.UserDefinedType;

/**
 * @author Lukas Eder
 */
public class XMLUDTDefinition extends AbstractUDTDefinition {

    private final InformationSchema info;
    private final UserDefinedType   udt;

    public XMLUDTDefinition(SchemaDefinition schema, InformationSchema info, UserDefinedType udt, String comment, SchemaDefinition superTypeSchema, String superTypeName, boolean isInstantiable) {
        super(schema, null, udt.getUserDefinedTypeName(), false, comment, superTypeSchema, superTypeName, isInstantiable);

        this.info = info;
        this.udt = udt;
    }

    @Override
    protected List<AttributeDefinition> getElements0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<>();

        for (Attribute attribute : ((XMLDatabase) getDatabase()).getAttributesByUDTName(
            name(udt.getUserDefinedTypeCatalog(), udt.getUserDefinedTypeSchema(), udt.getUserDefinedTypeName())
        )) {
            SchemaDefinition schema = getDatabase().getSchema(attribute.getUdtSchema());

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                schema,
                attribute.getDataType(),
                unbox(attribute.getCharacterMaximumLength()),
                unbox(attribute.getNumericPrecision()),
                unbox(attribute.getNumericScale()),
                true,
                attribute.getAttributeDefault(),
                StringUtils.isEmpty(attribute.getAttributeUdtName())
                    ? null
                    : name(attribute.getAttributeUdtCatalog(), attribute.getAttributeUdtSchema(), attribute.getAttributeUdtName())
            );

            result.add(new DefaultAttributeDefinition(
                this,
                attribute.getAttributeName(),
                unbox(attribute.getOrdinalPosition()),
                type,
                attribute.getComment()
            ));
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() {
        List<RoutineDefinition> result = new ArrayList<>();
        return result;
    }
}
