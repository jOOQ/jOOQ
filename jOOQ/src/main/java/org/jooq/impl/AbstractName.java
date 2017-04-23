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

/**
 * The default implementation for a qualified SQL identifier.
 *
 * @author Lukas Eder
 */
abstract class AbstractName extends AbstractQueryPart implements Name {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8562325639223483938L;

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
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
        if (getName().length != 1)
            throw new IllegalStateException("Cannot create a DerivedColumnList from a qualified name : " + Arrays.asList(getName()));

        return new DerivedColumnListImpl(last(), fieldNames);
    }

    @Override
    public final DerivedColumnListImpl fields(Name... fieldNames) {
        if (getName().length != 1)
            throw new IllegalStateException("Cannot create a DerivedColumnList from a qualified name : " + Arrays.asList(getName()));

        return new DerivedColumnListImpl(last(), Tools.fieldNamesToStrings(fieldNames));
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

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1) {
        return fields(new Name[] { fieldName1 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2) {
        return fields(new Name[] { fieldName1, fieldName2 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18, fieldName19 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18, fieldName19, fieldName20 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20, Name fieldName21) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18, fieldName19, fieldName20, fieldName21 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final DerivedColumnListImpl fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20, Name fieldName21, Name fieldName22) {
        return fields(new Name[] { fieldName1, fieldName2, fieldName3, fieldName4, fieldName5, fieldName6, fieldName7, fieldName8, fieldName9, fieldName10, fieldName11, fieldName12, fieldName13, fieldName14, fieldName15, fieldName16, fieldName17, fieldName18, fieldName19, fieldName20, fieldName21, fieldName22 });
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
        if (that instanceof AbstractName)
            return Arrays.equals(getName(), (((AbstractName) that).getName()));

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
