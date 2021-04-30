/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
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
package org.jooq.impl;

// ...
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.inlined;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_BY;
import static org.jooq.impl.Keywords.K_COLUMNS;
import static org.jooq.impl.Keywords.K_FOR;
import static org.jooq.impl.Keywords.K_ORDINALITY;
import static org.jooq.impl.Keywords.K_PASSING;
import static org.jooq.impl.Keywords.K_PATH;
import static org.jooq.impl.Keywords.K_REF;
import static org.jooq.impl.Keywords.K_VALUE;
import static org.jooq.impl.Keywords.K_XMLTABLE;
import static org.jooq.impl.Names.N_XMLTABLE;
import static org.jooq.impl.SQLDataType.XML;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitSubquery;
import static org.jooq.impl.XMLPassingMechanism.BY_REF;
import static org.jooq.impl.XMLPassingMechanism.BY_VALUE;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
// ...
import org.jooq.Record;
import org.jooq.TableOptions;
import org.jooq.XML;
import org.jooq.XMLTableColumnPathStep;
import org.jooq.XMLTablePassingStep;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class XMLTable
extends AbstractTable<Record>
implements
    XMLTablePassingStep,
    XMLTableColumnPathStep {
    private final Field<String>                 xpath;
    private final Field<XML>                    passing;
    private final XMLPassingMechanism           passingMechanism;
    private final QueryPartList<XMLTableColumn> columns;
    private final boolean                       hasOrdinality;
    private transient FieldsImpl<Record>        fields;

    XMLTable(Field<String> xpath) {
        this(xpath, null, null, null, false);
    }

    private XMLTable(
        Field<String> xpath,
        Field<XML> passing,
        XMLPassingMechanism passingMechanism,
        QueryPartList<XMLTableColumn> columns,
        boolean hasOrdinality
    ) {
        super(TableOptions.expression(), N_XMLTABLE);

        this.xpath = xpath;
        this.passing = passing;
        this.passingMechanism = passingMechanism;
        this.columns = columns == null ? new QueryPartList<>() : columns;
        this.hasOrdinality = hasOrdinality;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final XMLTable passing(XML xml) {
        return passing(Tools.field(xml));
    }

    @Override
    public final XMLTable passing(Field<XML> xml) {
        return new XMLTable(xpath, xml, null, columns, hasOrdinality);
    }

    @Override
    public final XMLTable passingByRef(XML xml) {
        return passingByRef(Tools.field(xml));
    }

    @Override
    public final XMLTable passingByRef(Field<XML> xml) {
        return new XMLTable(xpath, xml, BY_REF, columns, hasOrdinality);
    }

    @Override
    public final XMLTable passingByValue(XML xml) {
        return passingByRef(Tools.field(xml));
    }

    @Override
    public final XMLTable passingByValue(Field<XML> xml) {
        return new XMLTable(xpath, xml, BY_VALUE, columns, hasOrdinality);
    }

    @Override
    public final XMLTable column(String name) {
        return column(DSL.name(name));
    }

    @Override
    public final XMLTable column(Name name) {
        return column(DSL.field(name));
    }

    @Override
    public final XMLTable column(Field<?> name) {
        return column(name, name.getDataType());
    }

    @Override
    public final XMLTable column(String name, DataType<?> type) {
        return column(DSL.name(name), type);
    }

    @Override
    public final XMLTable column(Name name, DataType<?> type) {
        return column(DSL.field(name), type);
    }

    @Override
    public final XMLTable column(Field<?> name, DataType<?> type) {
        QueryPartList<XMLTableColumn> c = new QueryPartList<>(columns);
        c.add(new XMLTableColumn(name, type, false, null));
        return new XMLTable(xpath, passing, passingMechanism, c, hasOrdinality);
    }

    @Override
    public final XMLTable forOrdinality() {
        return path0(true, null);
    }

    @Override
    public final XMLTable path(String path) {
        return path0(false, path);
    }

    private final XMLTable path0(boolean forOrdinality, String path) {
        QueryPartList<XMLTableColumn> c = new QueryPartList<>(columns);
        int i = c.size() - 1;
        XMLTableColumn last = c.get(i);
        c.set(i, new XMLTableColumn(last.field, last.type, forOrdinality, path));
        return new XMLTable(xpath, passing, passingMechanism, c, hasOrdinality || forOrdinality);
    }

    // -------------------------------------------------------------------------
    // XXX: Table API
    // -------------------------------------------------------------------------

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImplN.class;
    }

    @Override
    final FieldsImpl<Record> fields0() {
        if (fields == null)
            fields = new FieldsImpl<>(map(columns,
                c -> c.field.getDataType() == c.type ? c.field : DSL.field(c.field.getQualifiedName(), c.type)
            ));

        return fields;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                acceptStandard(ctx);
                break;
        }
    }





























    private final void acceptStandard(Context<?> ctx) {
        ctx.visit(K_XMLTABLE).sqlIndentStart('(');
        acceptXPath(ctx, xpath);
        if (passing != null)
            acceptPassing(ctx, passing, passingMechanism);

        ctx.formatSeparator()
           .visit(K_COLUMNS).separatorRequired(true).visit(columns);

        ctx.sqlIndentEnd(')');
    }

    static final void acceptXPath(Context<?> ctx, Field<String> xpath) {






        ctx.visit(xpath);
    }

    static final void acceptPassing(Context<?> ctx, Field<XML> passing, XMLPassingMechanism passingMechanism) {
        ctx.formatSeparator()
           .visit(K_PASSING);

        if (passingMechanism == BY_REF)
            ctx.sql(' ').visit(K_BY).sql(' ').visit(K_REF);
        else if (passingMechanism == BY_VALUE)
            ctx.sql(' ').visit(K_BY).sql(' ').visit(K_VALUE);







        ctx.sql(' ').visit(passing);
    }

    private static class XMLTableColumn extends AbstractQueryPart {

        final Field<?>            field;
        final DataType<?>         type;
        final boolean             forOrdinality;
        final String              path;

        XMLTableColumn(Field<?> field, DataType<?> type, boolean forOrdinality, String path) {
            this.field = field;
            this.type = type;
            this.forOrdinality = forOrdinality;
            this.path = path;
        }

        @Override
        public final void accept(Context<?> ctx) {
            ctx.qualify(false, c -> c.visit(field)).sql(' ');

            if (forOrdinality)
                ctx.visit(K_FOR).sql(' ').visit(K_ORDINALITY);
            else
                Tools.toSQLDDLTypeDeclaration(ctx, type);

            if (path != null)
                ctx.sql(' ').visit(K_PATH).sql(' ').visit(inline(path));




        }
    }
}
