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
 */
package org.jooq.impl;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Generated;

import org.jooq.Clause;
import org.jooq.CommonTableExpression;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;
import org.jooq.tools.StringUtils;

/**
 * The default implementation for a SQL identifier
 *
 * @author Lukas Eder
 */
final class NameImpl extends AbstractQueryPart implements Name {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8562325639223483938L;

    private final String[]    qualifiedName;

    NameImpl(String[] qualifiedName) {
        this.qualifiedName = nonEmpty(qualifiedName);
    }

    private static final String[] nonEmpty(String[] qualifiedName) {
        String[] result;
        int nulls = 0;

        for (int i = 0; i < qualifiedName.length; i++)
            if (StringUtils.isEmpty(qualifiedName[i]))
                nulls++;

        if (nulls > 0) {
            result = new String[qualifiedName.length - nulls];

            for (int i = qualifiedName.length - 1; i >= 0; i--)
                if (StringUtils.isEmpty(qualifiedName[i]))
                    nulls--;
                else
                    result[i - nulls] = qualifiedName[i];
        }
        else {
            result = qualifiedName.clone();
        }

        return result;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#3437] Fully qualify this field only if allowed in the current context
        if (ctx.qualify()) {
            String separator = "";

            for (String name : qualifiedName) {
                ctx.sql(separator).literal(name);
                separator = ".";
            }
        }
        else {
            ctx.literal(last());
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final String first() {
        return qualifiedName.length > 0 ? qualifiedName[0] : null;
    }

    @Override
    public final String last() {
        return qualifiedName.length > 0 ? qualifiedName[qualifiedName.length - 1] : null;
    }

    @Override
    public final boolean qualified() {
        return qualifiedName.length > 1;
    }

    @Override
    public final String[] getName() {
        return qualifiedName;
    }

    @Override
    public final WindowDefinition as(WindowSpecification window) {
        return new WindowDefinitionImpl(this, window);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <R extends Record> CommonTableExpression<R> as(Select<R> select) {
        return fields(new String[0]).as(select);
    }

    @Override
    public final DerivedColumnListImpl fields(String... fieldNames) {
        if (qualifiedName.length != 1)
            throw new IllegalStateException("Cannot create a DerivedColumnList from a qualified name : " + Arrays.asList(qualifiedName));

        return new DerivedColumnListImpl(first(), fieldNames);
    }



    @Override
    public final DerivedColumnListImpl fields(Function<? super Field<?>, ? extends String> fieldNameFunction) {
        return fields((f, i) -> fieldNameFunction.apply(f));
    }

    @Override
    public final DerivedColumnListImpl fields(BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction) {
        return new DerivedColumnListImpl(first(), fieldNameFunction);
    }



    // [jooq-tools] START [fields]

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1) {
        return fields(new String[] { fieldName1 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2) {
        return fields(new String[] { fieldName1, fieldName2 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18, fieldName19 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18, fieldName19, fieldName20 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20, String fieldName21) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18, fieldName19, fieldName20, fieldName21 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20, String fieldName21, String fieldName22) {
        return fields(new String[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18, fieldName19, fieldName20, fieldName21, fieldName22 });
    }

// [jooq-tools] END [fields]

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return Arrays.hashCode(getName());
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        // [#1626] NameImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof NameImpl)
            return Arrays.equals(getName(), (((NameImpl) that).getName()));

        return super.equals(that);
    }

    @Override
    public final boolean equalsIgnoreCase(Name that) {
        if (this == that)
            return true;

        String[] thisName = getName();
        String[] thatName = that.getName();

        if (thisName.length != thatName.length)
            return false;

        for (int i = 0; i < thisName.length; i++) {
            if (thisName[i] == null && thatName[i] == null)
                continue;

            if (thisName[i] == null || thatName[i] == null)
                return false;

            if (!thisName[i].equalsIgnoreCase(thatName[i]))
                return false;
        }

        return true;
    }
}
