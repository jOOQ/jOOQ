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

import static org.jooq.Clause.CONSTRAINT;
import static org.jooq.impl.ConstraintImpl.Action.CASCADE;
import static org.jooq.impl.ConstraintImpl.Action.NO_ACTION;
import static org.jooq.impl.ConstraintImpl.Action.RESTRICT;
import static org.jooq.impl.ConstraintImpl.Action.SET_DEFAULT;
import static org.jooq.impl.ConstraintImpl.Action.SET_NULL;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Tools.fieldsByName;
import static org.jooq.impl.Tools.DataKey.DATA_CONSTRAINT_REFERENCE;

import javax.annotation.Generated;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.ConstraintForeignKeyOnStep;
import org.jooq.ConstraintForeignKeyReferencesStep1;
import org.jooq.ConstraintForeignKeyReferencesStep10;
import org.jooq.ConstraintForeignKeyReferencesStep11;
import org.jooq.ConstraintForeignKeyReferencesStep12;
import org.jooq.ConstraintForeignKeyReferencesStep13;
import org.jooq.ConstraintForeignKeyReferencesStep14;
import org.jooq.ConstraintForeignKeyReferencesStep15;
import org.jooq.ConstraintForeignKeyReferencesStep16;
import org.jooq.ConstraintForeignKeyReferencesStep17;
import org.jooq.ConstraintForeignKeyReferencesStep18;
import org.jooq.ConstraintForeignKeyReferencesStep19;
import org.jooq.ConstraintForeignKeyReferencesStep2;
import org.jooq.ConstraintForeignKeyReferencesStep20;
import org.jooq.ConstraintForeignKeyReferencesStep21;
import org.jooq.ConstraintForeignKeyReferencesStep22;
import org.jooq.ConstraintForeignKeyReferencesStep3;
import org.jooq.ConstraintForeignKeyReferencesStep4;
import org.jooq.ConstraintForeignKeyReferencesStep5;
import org.jooq.ConstraintForeignKeyReferencesStep6;
import org.jooq.ConstraintForeignKeyReferencesStep7;
import org.jooq.ConstraintForeignKeyReferencesStep8;
import org.jooq.ConstraintForeignKeyReferencesStep9;
import org.jooq.ConstraintForeignKeyReferencesStepN;
import org.jooq.ConstraintTypeStep;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("rawtypes")
final class ConstraintImpl extends AbstractQueryPart
implements
    ConstraintTypeStep
  , ConstraintForeignKeyOnStep
  , ConstraintForeignKeyReferencesStepN

// [jooq-tools] START [implements]
  , ConstraintForeignKeyReferencesStep1
  , ConstraintForeignKeyReferencesStep2
  , ConstraintForeignKeyReferencesStep3
  , ConstraintForeignKeyReferencesStep4
  , ConstraintForeignKeyReferencesStep5
  , ConstraintForeignKeyReferencesStep6
  , ConstraintForeignKeyReferencesStep7
  , ConstraintForeignKeyReferencesStep8
  , ConstraintForeignKeyReferencesStep9
  , ConstraintForeignKeyReferencesStep10
  , ConstraintForeignKeyReferencesStep11
  , ConstraintForeignKeyReferencesStep12
  , ConstraintForeignKeyReferencesStep13
  , ConstraintForeignKeyReferencesStep14
  , ConstraintForeignKeyReferencesStep15
  , ConstraintForeignKeyReferencesStep16
  , ConstraintForeignKeyReferencesStep17
  , ConstraintForeignKeyReferencesStep18
  , ConstraintForeignKeyReferencesStep19
  , ConstraintForeignKeyReferencesStep20
  , ConstraintForeignKeyReferencesStep21
  , ConstraintForeignKeyReferencesStep22

// [jooq-tools] END [implements]

{

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 1018023703769802616L;
    private static final Clause[] CLAUSES          = { CONSTRAINT };

    private final Name            name;
    private Field<?>[]            unique;
    private Field<?>[]            primaryKey;
    private Field<?>[]            foreignKey;
    private Table<?>              referencesTable;
    private Field<?>[]            references;
    private Action                onDelete;
    private Action                onUpdate;
    private Condition             check;

    ConstraintImpl() {
        this(null);
    }

    ConstraintImpl(Name name) {
        this.name = name;
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ctx.data(DATA_CONSTRAINT_REFERENCE) != null) {
            if (name == null)
                throw new DataAccessException("Cannot ALTER or DROP CONSTRAINT without name");

            ctx.visit(name);
        }
        else {
            boolean qualify = ctx.qualify();

            if (name != null)
                ctx.keyword("constraint")
                   .sql(' ')
                   .visit(name)
                   .formatIndentStart()
                   .formatSeparator();

            if (unique != null) {
                ctx.keyword("unique")
                   .sql(" (")
                   .qualify(false)
                   .visit(new QueryPartList<Field<?>>(unique))
                   .qualify(qualify)
                   .sql(')');
            }
            else if (primaryKey != null) {
                ctx.keyword("primary key")
                   .sql(" (")
                   .qualify(false)
                   .visit(new QueryPartList<Field<?>>(primaryKey))
                   .qualify(qualify)
                   .sql(')');
            }
            else if (foreignKey != null) {
                ctx.keyword("foreign key")
                   .sql(" (")
                   .qualify(false)
                   .visit(new QueryPartList<Field<?>>(foreignKey))
                   .qualify(qualify)
                   .sql(')')
                   .formatSeparator()
                   .keyword("references")
                   .sql(' ')
                   .visit(referencesTable)
                   .sql(" (")
                   .qualify(false)
                   .visit(new QueryPartList<Field<?>>(references))
                   .qualify(qualify)
                   .sql(')');

                if (onDelete != null)
                    ctx.sql(' ').keyword("on delete")
                       .sql(' ').keyword(onDelete.sql);

                if (onUpdate != null)
                    ctx.sql(' ').keyword("on update")
                       .sql(' ').keyword(onUpdate.sql);
            }
            else if (check != null) {
                ctx.keyword("check")
                   .sql(" (")
                   .qualify(false)
                   .visit(check)
                   .qualify(qualify)
                   .sql(')');
            }

            ctx.formatIndentEnd();
        }
    }

    @Override
    public final ConstraintImpl unique(String... fields) {
        return unique(fieldsByName(fields));
    }

    @Override
    public final ConstraintImpl unique(Field<?>... fields) {
        unique = fields;
        return this;
    }

    @Override
    public final ConstraintImpl check(Condition condition) {
        check = condition;
        return this;
    }

    @Override
    public final ConstraintImpl primaryKey(String... fields) {
        return primaryKey(fieldsByName(fields));
    }

    @Override
    public final ConstraintImpl primaryKey(Field<?>... fields) {
        primaryKey = fields;
        return this;
    }

    @Override
    public final ConstraintImpl foreignKey(String... fields) {
        return foreignKey(fieldsByName(fields));
    }

    @Override
    public final ConstraintImpl foreignKey(Field<?>... fields) {
        foreignKey = fields;
        return this;
    }

    @Override
    public final ConstraintImpl references(String table, String... fields) {
        return references(table(name(table)), fieldsByName(fields));
    }

    @Override
    public final ConstraintImpl references(Table<?> table, Field<?>... fields) {
        referencesTable = table;
        references = fields;
        return this;
    }

    @Override
    public final ConstraintImpl onDeleteNoAction() {
        onDelete = NO_ACTION;
        return this;
    }

    @Override
    public final ConstraintImpl onDeleteRestrict() {
        onDelete = RESTRICT;
        return this;
    }

    @Override
    public final ConstraintImpl onDeleteCascade() {
        onDelete = CASCADE;
        return this;
    }

    @Override
    public final ConstraintImpl onDeleteSetNull() {
        onDelete = SET_NULL;
        return this;
    }

    @Override
    public final ConstraintImpl onDeleteSetDefault() {
        onDelete = SET_DEFAULT;
        return this;
    }

    @Override
    public final ConstraintImpl onUpdateNoAction() {
        onUpdate = NO_ACTION;
        return this;
    }

    @Override
    public final ConstraintImpl onUpdateRestrict() {
        onUpdate = RESTRICT;
        return this;
    }

    @Override
    public final ConstraintImpl onUpdateCascade() {
        onUpdate = CASCADE;
        return this;
    }

    @Override
    public final ConstraintImpl onUpdateSetNull() {
        onUpdate = SET_NULL;
        return this;
    }

    @Override
    public final ConstraintImpl onUpdateSetDefault() {
        onUpdate = SET_DEFAULT;
        return this;
    }

    // [jooq-tools] START [foreignKey]

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1> ConstraintImpl foreignKey(Field<T1> field1) {
    	return foreignKey(new Field[] { field1 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2) {
    	return foreignKey(new Field[] { field1, field2 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
    	return foreignKey(new Field[] { field1, field2, field3 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
    	return foreignKey(new Field[] { field1, field2, field3, field4 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
    	return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1) {
    	return foreignKey(new String[] { field1 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2) {
    	return foreignKey(new String[] { field1, field2 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3) {
    	return foreignKey(new String[] { field1, field2, field3 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4) {
    	return foreignKey(new String[] { field1, field2, field3, field4 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21, String field22) {
    	return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1) {
    	return references(table, new Field[] { t1 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2) {
    	return references(table, new Field[] { t1, t2 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3) {
    	return references(table, new Field[] { t1, t2, t3 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4) {
    	return references(table, new Field[] { t1, t2, t3, t4 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
    	return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1) {
    	return references(table, new String[] { field1 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2) {
    	return references(table, new String[] { field1, field2 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3) {
    	return references(table, new String[] { field1, field2, field3 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4) {
    	return references(table, new String[] { field1, field2, field3, field4 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5) {
    	return references(table, new String[] { field1, field2, field3, field4, field5 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Generated("This method was generated using jOOQ-tools")
    @Override
    public final ConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21, String field22) {
    	return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

// [jooq-tools] END [foreignKey]

    enum Action {
        NO_ACTION("no action"),
        RESTRICT("restrict"),
        CASCADE("cascade"),
        SET_NULL("set null"),
        SET_DEFAULT("set default");

        String sql;

        private Action(String sql) {
            this.sql = sql;
        }
    }
}
