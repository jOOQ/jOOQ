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
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static org.jooq.Clause.CONSTRAINT;
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.impl.QOM.ForeignKeyRule.CASCADE;
import static org.jooq.impl.QOM.ForeignKeyRule.NO_ACTION;
import static org.jooq.impl.QOM.ForeignKeyRule.RESTRICT;
import static org.jooq.impl.QOM.ForeignKeyRule.SET_DEFAULT;
import static org.jooq.impl.QOM.ForeignKeyRule.SET_NULL;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_CHECK;
import static org.jooq.impl.Keywords.K_CONSTRAINT;
import static org.jooq.impl.Keywords.K_DISABLE;
import static org.jooq.impl.Keywords.K_ENABLE;
import static org.jooq.impl.Keywords.K_ENFORCED;
import static org.jooq.impl.Keywords.K_FOREIGN_KEY;
import static org.jooq.impl.Keywords.K_NONCLUSTERED;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Keywords.K_NOT_ENFORCED;
import static org.jooq.impl.Keywords.K_ON_DELETE;
import static org.jooq.impl.Keywords.K_ON_UPDATE;
import static org.jooq.impl.Keywords.K_PRIMARY_KEY;
import static org.jooq.impl.Keywords.K_REFERENCES;
import static org.jooq.impl.Keywords.K_UNIQUE;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_NAME;
import static org.jooq.impl.Tools.EMPTY_STRING;
import static org.jooq.impl.Tools.fieldsByName;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_CONSTRAINT_REFERENCE;

import java.util.Collection;
import java.util.Set;

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
import org.jooq.Keyword;
import org.jooq.Name;
import org.jooq.Pro;
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;
import org.jooq.Table;
// ...
import org.jooq.impl.QOM.ForeignKey;
import org.jooq.impl.QOM.ForeignKeyRule;
import org.jooq.impl.QOM.PrimaryKey;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.QOM.UnmodifiableList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("rawtypes")
final class ForeignKeyConstraintImpl
extends
    AbstractConstraint
implements
    QOM.ForeignKey
  , ConstraintForeignKeyOnStep
  , ConstraintForeignKeyReferencesStepN


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



{
    static final Set<SQLDialect> NO_SUPPORT_FK                = SQLDialect.supportedBy(CLICKHOUSE, IGNITE, TRINO);







    private Field<?>[]            fields;
    private Table<?>              referencesTable;
    private Field<?>[]            referencesFields;
    private ForeignKeyRule        deleteRule;
    private ForeignKeyRule        updateRule;

    ForeignKeyConstraintImpl(Name name, Field<?>[] fields) {
        this(
            name,
            fields,
            null,
            null,
            null,
            null,
            true
        );
    }

    private ForeignKeyConstraintImpl(
        Name name,
        Field<?>[] fields,
        Table<?> referencesTable,
        Field<?>[] referencesFields,
        ForeignKeyRule deleteRule,
        ForeignKeyRule updateRule,
        boolean enforced
    ) {
        super(name, enforced);

        this.fields = fields;
        this.referencesTable = referencesTable;
        this.referencesFields = referencesFields;
        this.deleteRule = deleteRule;
        this.updateRule = updateRule;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept0(Context<?> ctx) {
        ctx.visit(K_FOREIGN_KEY)
           .sql(" (").visit(wrap(fields).qualify(false)).sql(") ")
           .visit(K_REFERENCES).sql(' ').visit(referencesTable);

        if (referencesFields.length > 0)
            ctx.sql(" (").visit(wrap(referencesFields).qualify(false)).sql(')');

        if (deleteRule != null)



                ctx.sql(' ').visit(K_ON_DELETE)
                   .sql(' ').visit(deleteRule.keyword);

        if (updateRule != null)



                ctx.sql(' ').visit(K_ON_UPDATE)
                   .sql(' ').visit(updateRule.keyword);
    }

    @Override
    final boolean supported(Context<?> ctx, Table<?> onTable) {
        return !NO_SUPPORT_FK.contains(ctx.dialect()) && supportedSelfReference(ctx, onTable);
    }

    final boolean supportedSelfReference(Context<?> ctx, Table<?> onTable) {





        return true;
    }

    // ------------------------------------------------------------------------
    // XXX: Constraint API
    // ------------------------------------------------------------------------

    @Override
    public final ForeignKeyConstraintImpl references(String table) {
        return references(table(name(table)), EMPTY_FIELD);
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String... f) {
        return references(table(name(table)), fieldsByName(f));
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, Collection<? extends String> f) {
        return references(table, f.toArray(EMPTY_STRING));
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table) {
        return references(table(table), EMPTY_FIELD);
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name... f) {
        return references(table(table), fieldsByName(f));
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Collection<? extends Name> f) {
        return references(table, f.toArray(EMPTY_NAME));
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table) {
        return references(table, EMPTY_FIELD);
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table<?> table, Field<?>... f) {
        referencesTable = table;
        referencesFields = f;
        return this;
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table<?> table, Collection<? extends Field<?>> f) {
        return references(table, f.toArray(EMPTY_FIELD));
    }

    @Override
    public final ForeignKeyConstraintImpl onDelete(ForeignKeyRule rule) {
        deleteRule = rule;
        return this;
    }

    @Override
    public final ForeignKeyConstraintImpl onDeleteNoAction() {
        return onDelete(NO_ACTION);
    }

    @Override
    public final ForeignKeyConstraintImpl onDeleteRestrict() {
        return onDelete(RESTRICT);
    }

    @Override
    public final ForeignKeyConstraintImpl onDeleteCascade() {
        return onDelete(CASCADE);
    }

    @Override
    public final ForeignKeyConstraintImpl onDeleteSetNull() {
        return onDelete(SET_NULL);
    }

    @Override
    public final ForeignKeyConstraintImpl onDeleteSetDefault() {
        return onDelete(SET_DEFAULT);
    }

    @Override
    public final ForeignKeyConstraintImpl onUpdate(ForeignKeyRule rule) {
        updateRule = rule;
        return this;
    }

    @Override
    public final ForeignKeyConstraintImpl onUpdateNoAction() {
        return onUpdate(NO_ACTION);
    }

    @Override
    public final ForeignKeyConstraintImpl onUpdateRestrict() {
        return onUpdate(RESTRICT);
    }

    @Override
    public final ForeignKeyConstraintImpl onUpdateCascade() {
        return onUpdate(CASCADE);
    }

    @Override
    public final ForeignKeyConstraintImpl onUpdateSetNull() {
        return onUpdate(SET_NULL);
    }

    @Override
    public final ForeignKeyConstraintImpl onUpdateSetDefault() {
        updateRule = SET_DEFAULT;
        return this;
    }



    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1) {
        return references(table, new Field[] { t1 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2) {
        return references(table, new Field[] { t1, t2 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3) {
        return references(table, new Field[] { t1, t2, t3 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4) {
        return references(table, new Field[] { t1, t2, t3, t4 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5) {
        return references(table, new Field[] { t1, t2, t3, t4, t5 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Table table, Field t1, Field t2, Field t3, Field t4, Field t5, Field t6, Field t7, Field t8, Field t9, Field t10, Field t11, Field t12, Field t13, Field t14, Field t15, Field t16, Field t17, Field t18, Field t19, Field t20, Field t21, Field t22) {
        return references(table, new Field[] { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1) {
        return references(table, new Name[] { field1 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2) {
        return references(table, new Name[] { field1, field2 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3) {
        return references(table, new Name[] { field1, field2, field3 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4) {
        return references(table, new Name[] { field1, field2, field3, field4 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5) {
        return references(table, new Name[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20, Name field21) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(Name table, Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20, Name field21, Name field22) {
        return references(table, new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1) {
        return references(table, new String[] { field1 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2) {
        return references(table, new String[] { field1, field2 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3) {
        return references(table, new String[] { field1, field2, field3 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4) {
        return references(table, new String[] { field1, field2, field3, field4 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5) {
        return references(table, new String[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final ForeignKeyConstraintImpl references(String table, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21, String field22) {
        return references(table, new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final ForeignKey $name(Name newName) {
        return new ForeignKeyConstraintImpl(newName,
            fields,
            referencesTable,
            referencesFields,
            deleteRule,
            updateRule,
            enforced
        );
    }

    @Override
    public final ForeignKey $enforced(boolean newEnforced) {
        return new ForeignKeyConstraintImpl($name(),
            fields,
            referencesTable,
            referencesFields,
            deleteRule,
            updateRule,
            newEnforced
        );
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $fields() {
        return QOM.unmodifiable(fields);
    }

    @Override
    public final ForeignKey $fields(UnmodifiableList<? extends Field<?>> newFields) {
        return new ForeignKeyConstraintImpl($name(),
            newFields.toArray(EMPTY_FIELD),
            referencesTable,
            referencesFields,
            deleteRule,
            updateRule,
            enforced
        );
    }

    @Override
    public final Table<?> $referencesTable() {
        return referencesTable;
    }

    @Override
    public final ForeignKey $referencesTable(Table<?> newReferencesTable) {
        return new ForeignKeyConstraintImpl($name(),
            fields,
            newReferencesTable,
            referencesFields,
            deleteRule,
            updateRule,
            enforced
        );
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $referencesFields() {
        return QOM.unmodifiable(referencesFields);
    }

    @Override
    public final ForeignKey $referencesFields(UnmodifiableList<? extends Field<?>> newReferencesFields) {
        return new ForeignKeyConstraintImpl($name(),
            fields,
            referencesTable,
            newReferencesFields.toArray(EMPTY_FIELD),
            deleteRule,
            updateRule,
            enforced
        );
    }

    @Override
    public final ForeignKeyRule $deleteRule() {
        return deleteRule;
    }

    @Override
    public final ForeignKey $deleteRule(ForeignKeyRule newDeleteRule) {
        return new ForeignKeyConstraintImpl($name(),
            fields,
            referencesTable,
            referencesFields,
            newDeleteRule,
            updateRule,
            enforced
        );
    }

    @Override
    public final ForeignKeyRule $updateRule() {
        return updateRule;
    }

    @Override
    public final ForeignKey $updateRule(ForeignKeyRule newUpdateRule) {
        return new ForeignKeyConstraintImpl($name(),
            fields,
            referencesTable,
            referencesFields,
            deleteRule,
            newUpdateRule,
            enforced
        );
    }


















}
