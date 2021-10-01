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

import static org.jooq.impl.DSL.keyword;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Predicate;

// ...
import org.jooq.Condition;
import org.jooq.Constraint;
import org.jooq.DMLQuery;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.Function10;
import org.jooq.Function11;
import org.jooq.Function12;
import org.jooq.Function13;
import org.jooq.Function14;
import org.jooq.Function15;
import org.jooq.Function16;
import org.jooq.Function17;
import org.jooq.Function18;
import org.jooq.Function19;
import org.jooq.Function2;
import org.jooq.Function20;
import org.jooq.Function21;
import org.jooq.Function22;
import org.jooq.Function3;
import org.jooq.Function4;
import org.jooq.Function5;
import org.jooq.Function6;
import org.jooq.Function7;
import org.jooq.Function8;
import org.jooq.Function9;
import org.jooq.Internal;
import org.jooq.JSONEntry;
import org.jooq.Keyword;
// ...
import org.jooq.OrderField;
import org.jooq.Parameter;
import org.jooq.Privilege;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.RowId;
import org.jooq.SortOrder;
import org.jooq.XML;
import org.jooq.tools.reflect.Reflect;
import org.jooq.types.DayToSecond;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A draft of the new query object model API.
 * <p>
 * <strong>This API is EXPERIMENTAL. Use at your own risk.</strong>
 * <p>
 * <h3>Purpose</h3>
 * <p>
 * This class provides a single namespace for jOOQ's query object model API.
 * Every {@link QueryPart} from the DSL API has a matching {@link MQueryPart}
 * representation in this API, and an internal implementation in the
 * <code>org.jooq.impl</code> package, that covers both the DSL and model API
 * functionality.
 * <p>
 * The goal of this model API is to allow for expression tree transformations
 * that are independent of the DSL API that would otherwise be too noisy for
 * this task.
 * <p>
 * <h3>Design</h3>
 * <p>
 * In order to avoid conflicts between the model API and the DSL API, all model
 * API in this class follows these naming conventions:
 * <ul>
 * <li>All public model API types are named <code>MXyz</code>, e.g.
 * {@link MQueryPart}</li>
 * <li>All private model API utility types are named <code>UXyz</code>, e.g.
 * {@link UEmpty}</li>
 * <li>All accessor methods are named <code>$property()</code>, e.g.
 * {@link MNot#$arg1()}</li>
 * </ul>
 * <p>
 * Furthermore, the current draft design lets each {@link QueryPart} publicly
 * extend its matching {@link MQueryPart}. <strong>This may not be the case in
 * the future, as long as this API is experimental, a backwards incompatible
 * change may revert this</strong>. Alternative possible designs include:
 * <ul>
 * <li>There's no public relationship between the two types (though accessor
 * methods might be provided)</li>
 * <li>The relationship might be inversed to let {@link MQueryPart} extend
 * {@link QueryPart}.</li>
 * <li>The publicly available {@link QueryPart} types don't have an
 * {@link MQueryPart} equivalence, but they <em>are</em> the
 * {@link MQueryPart}.</li>
 * </ul>
 * <p>
 * <h3>Limitations</h3>
 * <p>
 * The API offers public access to jOOQ's internal representation, and as such,
 * is prone to incompatible changes between minor releases, in addition to the
 * incompatible changes that may arise due to this API being experimental. In
 * this experimental stage, not all {@link QueryPart} implementations have a
 * corresponding public {@link MQueryPart} type, but may just implement the API
 * via a {@link UEmpty} or {@link UNotYetImplemented} subtype, and may not
 * provide access to contents via accessor methods.
 * <p>
 * <h3>Mutability</h3>
 * <p>
 * While some elements of this API are historically mutable (either mutable
 * objects are returned from {@link MQueryPart} subtypes, or argument objects
 * when constructing an {@link MQueryPart} remains mutable, rather than copied),
 * users must not rely on this mutable behaviour. Once this API stabilises, all
 * mutability will be gone, accidental remaining mutability will be considered a
 * bug.
 * <p>
 * <h3>Notes</h3>
 * <p>
 * The Java 17 distribution of jOOQ will make use of sealed types to improve the
 * usability of the model API in pattern matching expressions etc. The
 * implementations currently can't be made publicly available
 * {@link java.lang.Record} types, because of the existing internal type
 * hierarchy.
 *
 * @author Lukas Eder
 */
// Work around https://bugs.eclipse.org/bugs/show_bug.cgi?id=576378
@SuppressWarnings("rawtypes")
@Internal
public final class QOM {

    // -------------------------------------------------------------------------
    // XXX: Model
    // -------------------------------------------------------------------------

    public interface MQueryPart {
        <R> R traverse(
            R init,
            Predicate<? super R> abort,
            Predicate<? super MQueryPart> recurse,
            BiFunction<? super R, ? super MQueryPart, ? extends R> accumulate
        );

        default <R> R traverse(
            R init,
            BiFunction<? super R, ? super MQueryPart, ? extends R> accumulate
        ) {
            return traverse(init, b -> false, p -> true, accumulate);
        }

        @NotNull
        default MQueryPart replace(Function1<? super MQueryPart, ? extends MQueryPart> replacement) {
            return replace(p -> true, replacement);
        }

        @NotNull
        MQueryPart replace(
            Predicate<? super MQueryPart> recurse,
            Function1<? super MQueryPart, ? extends MQueryPart> replacement
        );

        /**
         * @deprecated - [#12425] - 3.16.0 - The name "contains" conflicts with
         *             {@link Field#contains(Object)} and
         *             {@link List#contains(Object)} and will be replaced.
         *             There's no definitive naming strategy for these
         *             {@link MQueryPart} method names yet. Must be fixed prior
         *             to releasing the API!
         */
        @Deprecated
        default boolean $contains(MQueryPart part) {
            return traverse(equals(part), b -> b, p -> true, (b, p) -> b || p.equals(part));
        }

        @Nullable
        default MQueryPart findAny(Predicate<? super MQueryPart> predicate) {
            return traverse((MQueryPart) null, p -> p != null, p -> true, (r, p) -> predicate.test(p) ? p : r);
        }

        @NotNull
        default List<MQueryPart> find(Predicate<? super MQueryPart> predicate) {
            return traverse(new ArrayList<>(), (l, p) -> {
                if (predicate.test(p))
                    l.add(p);

                return l;
            });
        }
    }

    public interface MName extends UEmpty {}
    public interface MNamed extends MQueryPart {
        @NotNull MName $name();
    }
    public interface MTyped<T> extends MQueryPart {
        @NotNull MDataType<T> $dataType();
    }
    public interface MComment extends UEmpty {
        @NotNull String $comment();
    }
    public interface MRole extends MNamed {}
    public interface MUser extends MNamed {}
    public interface MCollection<Q extends MQueryPart> extends MQueryPart, Collection<Q> {}
    public interface MList<Q extends MQueryPart> extends MCollection<Q>, List<Q> {}
    public interface MDataType<T> extends MQueryPart {}
    public interface MCharacterSet extends UEmpty {}
    public interface MCollation extends UEmpty {}
    public interface MDomain<T> extends MQueryPart {}

    public interface MDerivedColumnList extends MQueryPart {
        @NotNull MName $tableName();
        @NotNull MList<? extends MName> $columnNames();
    }

    public /* sealed */ interface MWith
        extends
            MQueryPart
        /* permits
            WithImpl */ 
    {
        @NotNull MList<? extends MCommonTableExpression<?>> $commonTableExpressions();
        boolean $recursive();
    }

    // -------------------------------------------------------------------------
    // XXX: Queries
    // -------------------------------------------------------------------------

    public interface MQueries extends MQueryPart {
        @NotNull MList<? extends MQuery> $queries();
    }
    public interface MQuery extends MQueryPart {}
    public interface MDDLQuery extends MQuery {}
    public interface MRowCountQuery extends MQuery {}
    public interface MResultQuery<R extends Record> extends MQuery {}
    public interface MSelect<R extends Record> extends MResultQuery<R> {
        @Nullable MWith $with();
        @NotNull MList<? extends MSelectFieldOrAsterisk> $select();
        @NotNull MSelect<?> $select(MList<? extends MSelectFieldOrAsterisk> select);
        boolean $distinct();
        @NotNull MList<? extends MTable<?>> $from();
        @NotNull MSelect<?> $from(MList<? extends MTable<?>> from);
        @Nullable MCondition $where();
        @NotNull MList<? extends MGroupField> $groupBy();
        @Nullable MCondition $having();
        @NotNull MList<? extends MWindowDefinition> $window();
        @Nullable MCondition $qualify();
        @NotNull MList<? extends MSortField<?>> $orderBy();
    }
    public interface MDelete<R extends Record> extends MRowCountQuery {
        @NotNull MTable<?> $table();
        @Nullable MCondition $condition();
    }

    public /* sealed */ interface MCreateType
        extends
            MDDLQuery
        /* permits
            CreateTypeImpl */ 
    {
        @NotNull MName $name();
        @NotNull MList<? extends MField<String>> $values();
    }

    public /* sealed */ interface MDropType
        extends
            MDDLQuery
        /* permits
            DropTypeImpl */ 
    {
        @NotNull MList<? extends MName> $names();
        boolean $ifExists();
        @Nullable Cascade $cascade();
    }

    public /* sealed */ interface MCreateView<R extends Record>
        extends
            MDDLQuery
        /* permits
            CreateViewImpl */ 
    {
        boolean $ifNotExists();
        boolean $orReplace();
        @NotNull MTable<?> $view();
        @NotNull MList<? extends MField<?>> $fields();
        @NotNull MResultQuery<R> $query();
    }

    // -------------------------------------------------------------------------
    // XXX: Schema
    // -------------------------------------------------------------------------

    public interface MCatalog extends MNamed {}
    public interface MSchema extends MNamed {}
    public interface MQualified extends MNamed {
        @Nullable MSchema $schema();
    }
    public interface MPackage extends MQualified {}
    public interface MUDT extends MQualified {}











    public interface MSequence<T extends Number> extends MQualified {}
    public interface MConstraint extends MNamed {}
    public interface MPrimaryKey extends MConstraint {
        @NotNull MList<? extends MField<?>> $fields();
    }
    public interface MUniqueKey extends MConstraint {
        @NotNull MList<? extends MField<?>> $fields();
    }
    public interface MForeignKey extends MConstraint {
        @NotNull MList<? extends MField<?>> $fields();
        @NotNull MConstraint $references();
    }
    public interface MCheck extends MConstraint {
        @NotNull MCondition $condition();
    }
    public interface MIndex extends MNamed {
        @NotNull MTableRef<?> $table();
    }

    // -------------------------------------------------------------------------
    // XXX: Statements
    // -------------------------------------------------------------------------

    public interface MStatement extends MQueryPart {}
    public interface MBlock extends MQueryPart {
        @NotNull MList<? extends MStatement> $statements();
    }
    public interface MNull extends MStatement {}












































    // -------------------------------------------------------------------------
    // XXX: Tables
    // -------------------------------------------------------------------------

    public interface MTable<R extends Record> extends MQueryPart {}
    public interface MTableAlias<R extends Record> extends MTable<R> {
        @NotNull MTable<R> $table();
        @NotNull MName $alias();
        // TODO [#12425] Reuse MDerivedColumnList
    }
    public interface MTableRef<R extends Record> extends UEmptyTable<R>, MQualified {}
    public interface MDual extends MTable<Record>, UEmpty {}
    public interface MLateral<R extends Record> extends MTable<R>, UOperator1<MTable<R>, MTable<R>> {}
    public interface MDerivedTable<R extends Record> extends MTable<R>, UOperator1<MSelect<R>, MTable<R>> {}
    public interface MValues<R extends Record> extends MTable<R>, UOperator1<MList<? extends MRow>, MTable<R>> {}
    public interface MCommonTableExpression<R extends Record> extends MTable<R> {
        @NotNull MDerivedColumnList $derivedColumnList();
        @NotNull MResultQuery<R> $query();
        @NotNull Materialized $materialized();
    }
    public interface MDataChangeDeltaTable<R extends Record> extends MTable<R> {
        @NotNull ResultOption $resultOption();
        @NotNull DMLQuery<R> $query();
    }
    public interface MRowsFrom extends MTable<Record> {
        @NotNull MList<? extends MTable<?>> $tables();
    }
    public interface MGenerateSeries<T> extends MTable<Record1<T>>, UOperator3<MField<T>, MField<T>, MField<T>, MTable<Record1<T>>> {
        @NotNull default MField<T> $from() { return $arg1(); }
        @NotNull default MField<T> $to() { return $arg2(); }
        @Nullable default MField<T> $step() { return $arg3(); }
    }

    // -------------------------------------------------------------------------
    // XXX: Conditions
    // -------------------------------------------------------------------------

    public interface MCondition extends MQueryPart {}

    public /* sealed */ interface MCombinedCondition
        extends
            MCondition,
            UOperator2<MCondition, MCondition, MCondition>
        /* permits
            MAnd,
            MOr */ 
    {}

    public /* sealed */ interface MCompareCondition<T>
        extends
            MCondition,
            UOperator2<MField<T>, MField<T>, MCondition>
        /* permits
            MEq,
            MNe,
            MLt,
            MLe,
            MGt,
            MGe,
            MIsDistinctFrom,
            MIsNotDistinctFrom,
            MContains,
            MContainsIgnoreCase,
            MStartsWith,
            MStartsWithIgnoreCase,
            MEndsWith,
            MEndsWithIgnoreCase */ 
    {}

    public /* sealed */ interface MTrue
        extends
            MCondition,
            UEmpty
        /* permits
            TrueCondition */ 
    {}

    public /* sealed */ interface MFalse
        extends
            MCondition,
            UEmpty
        /* permits
            FalseCondition */ 
    {}

    public interface MBetween<T> extends UOperator3<MField<T>, MField<T>, MField<T>, MCondition> {
        boolean $symmetric();
    }

    public /* sealed */ interface MInList<T>
        extends
            MCondition,
            UOperator2<MField<T>, MList<? extends MField<T>>, MCondition>
        /* permits
            InList */ 
    {
        @NotNull default MField<T> $field() { return $arg1(); }
        @NotNull default MList<? extends MField<T>> $list() { return $arg2(); }
    }

    public /* sealed */ interface MNotInList<T>
        extends
            MCondition,
            UOperator2<MField<T>, MList<? extends MField<T>>, MCondition>
        /* permits
            NotInList */ 
    {
        @NotNull default MField<T> $field() { return $arg1(); }
        @NotNull default MList<? extends MField<T>> $list() { return $arg2(); }
    }

    public /* sealed */ interface MRegexpLike
        extends
            MCondition
        /* permits
            RegexpLike */ 
    {
        @NotNull MField<?> $search();
        @NotNull MField<String> $pattern();
    }

    public /* sealed */ interface MExtract
        extends
            MField<Integer>
        /* permits
            Extract */ 
    {
        @NotNull MField<?> $field();
        @NotNull DatePart $datePart();
    }

    public /* sealed */ interface MRowIsNull
        extends
            MCondition,
            UOperator1<MRow, MCondition>
        /* permits
            RowIsNull */ 
    {
        @NotNull default MRow $field() { return $arg1(); }
    }

    public /* sealed */ interface MRowIsNotNull
        extends
            MCondition,
            UOperator1<MRow, MCondition>
        /* permits
            RowIsNotNull */ 
    {
        @NotNull default MRow $field() { return $arg1(); }
    }

    public /* sealed */ interface MRowOverlaps
        extends
            MCondition,
            UOperator2<MRow, MRow, MCondition>
        /* permits
            RowOverlaps */ 
    {}

    public /* sealed */ interface MSelectIsNull
        extends
            MCondition,
            UOperator1<MSelect<?>, MCondition>
        /* permits
            SelectIsNull */ 
    {
        @NotNull default MSelect<?> $field() { return $arg1(); }
    }

    public /* sealed */ interface MSelectIsNotNull
        extends
            MCondition,
            UOperator1<MSelect<?>, MCondition>
        /* permits
            SelectIsNotNull */ 
    {
        @NotNull default MSelect<?> $field() { return $arg1(); }
    }


    // -------------------------------------------------------------------------
    // XXX: Rows
    // -------------------------------------------------------------------------

    public interface MFieldOrRow extends MQueryPart {}

    public interface MRow extends MFieldOrRow {
        @NotNull MList<? extends MField<?>> $fields();
    }

    public /* sealed */ interface MRowField<R extends Record>
        extends
            MField<R>
        /* permits
            RowField */ 
    {
        @NotNull MRow $row();
    }

    // -------------------------------------------------------------------------
    // XXX: SelectFields, GroupFields and SortFields
    // -------------------------------------------------------------------------

    public interface MSelectFieldOrAsterisk extends MQueryPart {}
    public interface MAsterisk extends MSelectFieldOrAsterisk {
        @NotNull MList<? extends MField<?>> $except();
    }
    public interface MQualifiedAsterisk extends MSelectFieldOrAsterisk {
        @NotNull MTable<?> $table();
        @NotNull MList<? extends MField<?>> $except();
    }

    public interface MGroupField extends MQueryPart {}

    public /* sealed */ interface MRollup
        extends
            MGroupField,
            UOperator1<MList<? extends MFieldOrRow>, MGroupField>
        /* permits
            Rollup */ 
    {}

    public /* sealed */ interface MCube
        extends
            MGroupField,
            UOperator1<MList<? extends MFieldOrRow>, MGroupField>
        /* permits
            Cube */ 
    {}

    public /* sealed */ interface MGroupingSets
        extends
            MGroupField,
            UOperator1<MList<? extends MList<? extends MFieldOrRow>>, MGroupField>
        /* permits
            GroupingSets */ 
    {}

    public interface MSortField<T> extends MQueryPart {
        @NotNull MField<T> $field();
        @NotNull SortOrder $sortOrder();
        @NotNull NullOrdering $nullOrdering();
    }

    // -------------------------------------------------------------------------
    // XXX: Aggregate functions and window functions
    // -------------------------------------------------------------------------

    public interface MAggregateFunction<T> extends MField<T> {
        @Nullable MCondition $filterWhere();
    }

    public /* sealed */ interface MRatioToReport
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RatioToReport */ 
    {
        @NotNull MField<? extends Number> $field();
    }

    public /* sealed */ interface MMode<T>
        extends
            MAggregateFunction<T>,
            UOperator1<MField<T>, MAggregateFunction<T>>
        /* permits
            Mode */ 
    {
        @NotNull default MField<T> $field() { return $arg1(); }
    }

    public /* sealed */ interface MMultisetAgg<R extends Record>
        extends
            MAggregateFunction<Result<R>>
        /* permits
            MultisetAgg */ 
    {
        @NotNull MRow $row();
    }

    public /* sealed */ interface MArrayAgg<T>
        extends
            MAggregateFunction<T[]>,
            UOperator1<MField<T>, MAggregateFunction<T[]>>
        /* permits
            ArrayAgg */ 
    {
        @NotNull default MField<T> $field() { return $arg1(); }
        boolean $distinct();
    }

    public /* sealed */ interface MXMLAgg
        extends
            MAggregateFunction<XML>,
            UOperator1<MField<XML>, MAggregateFunction<XML>>
        /* permits
            XMLAgg */ 
    {
        @NotNull default MField<XML> $field() { return $arg1(); }
    }

    public interface MJSONEntry<T> extends MQueryPart {
        @NotNull MField<String> $key();
        @NotNull MField<?> $value();
    }

    public /* sealed */ interface MJSONArrayAgg<J>
        extends
            MAggregateFunction<J>,
            UOperator1<MField<?>, MAggregateFunction<J>>
        /* permits
            JSONArrayAgg */ 
    {
        @NotNull default MField<?> $field() { return $arg1(); }
        @NotNull JSONOnNull $onNull();
        @NotNull MDataType<?> $returning();
    }

    public /* sealed */ interface MJSONObjectAgg<J>
        extends
            MAggregateFunction<J>,
            UOperator1<MJSONEntry<?>, MAggregateFunction<J>>
        /* permits JSONObjectAgg */ 
    {
        @NotNull default MJSONEntry<?> $entry() { return $arg1(); }
        @NotNull JSONOnNull $onNull();
        @NotNull MDataType<?> $returning();
    }

    public /* sealed */ interface MCountTable
        extends
            MAggregateFunction<Integer>
        /* permits
            CountTable */ 
    {
        @NotNull MTable<?> $table();
        boolean $distinct();
    }











    public interface MWindowSpecification extends MQueryPart {
        @Nullable MWindowDefinition $windowDefinition();
        @NotNull MList<? extends MField<?>> $partitionBy();
        @NotNull MList<? extends MSortField<?>> $orderBy();
        @Nullable FrameUnits $frameUnits();
        @Nullable Integer $frameStart();
        @Nullable Integer $frameEnd();
        @Nullable FrameExclude $exclude();

    }

    public interface MWindowDefinition extends MQueryPart {
        @NotNull MName $name();
        @Nullable MWindowSpecification $windowSpecification();
    }

    public interface MWindowFunction<T> extends MField<T> {
        @Nullable MWindowSpecification $windowSpecification();
        @Nullable MWindowDefinition $windowDefinition();
    }

    public /* sealed */ interface MRowNumber
        extends
            MWindowFunction<Integer>
        /* permits
            RowNumber */ 
    {}

    public /* sealed */ interface MRank
        extends
            MWindowFunction<Integer>
        /* permits
            Rank */ 
    {}

    public /* sealed */ interface MDenseRank
        extends
            MWindowFunction<Integer>
        /* permits
            DenseRank */ 
    {}

    public /* sealed */ interface MPercentRank
        extends
            MWindowFunction<BigDecimal>
        /* permits
            PercentRank */ 
    {}

    public /* sealed */ interface MCumeDist
        extends
            MWindowFunction<BigDecimal>
        /* permits
            CumeDist */ 
    {}

    public /* sealed */ interface MNtile
        extends
            MWindowFunction<Integer>
        /* permits Ntile */ 
    {
        @NotNull MField<Integer> $tiles();
    }

    public /* sealed */ interface MLead<T>
        extends
            MWindowFunction<T>
        /* permits
            Lead */ 
    {
        @NotNull MField<T> $field();
        @Nullable MField<Integer> $offset();
        @Nullable MField<T> $defaultValue();
        @Nullable NullTreatment $nullTreatment();
    }

    public /* sealed */ interface MLag<T>
        extends
            MWindowFunction<T>
        /* permits
            Lag */ 
    {
        @NotNull MField<T> $field();
        @Nullable MField<Integer> $offset();
        @Nullable MField<T> $defaultValue();
        @Nullable NullTreatment $nullTreatment();
    }

    public /* sealed */ interface MFirstValue<T>
        extends
            MWindowFunction<T>
        /* permits
            FirstValue */ 
    {
        @NotNull MField<T> $field();
        @Nullable NullTreatment $nullTreatment();
    }

    public /* sealed */ interface MLastValue<T>
        extends
            MWindowFunction<T>
        /* permits
            LastValue */ 
    {
        @NotNull MField<T> $field();
        @Nullable NullTreatment $nullTreatment();
    }

    public /* sealed */ interface MNthValue<T>
        extends
            MWindowFunction<T>
        /* permits
            NthValue */ 
    {
        @NotNull MField<T> $field();
        @Nullable FromFirstOrLast $fromFirstOrLast();
        @Nullable NullTreatment $nullTreatment();
    }

    // -------------------------------------------------------------------------
    // XXX: Fields
    // -------------------------------------------------------------------------

    public interface MField<T> extends MFieldOrRow, MTyped<T>, MGroupField {}

    public /* sealed */ interface MFieldAlias<T>
        extends
            MField<T>
        /* permits
            FieldAlias */ 
    {
        @NotNull MField<T> $field();
        @NotNull MName $alias();
    }

    public /* sealed */ interface MFunction<T>
        extends
            MNamed,
            MField<T>
        /* permits
            org.jooq.impl.Function,
            org.jooq.impl.Function1 */ 
    {
        @NotNull MList<? extends MField<?>> $args();
    }

    public /* sealed */ interface MCast<T>
        extends
            MField<T>
        /* permits
            Cast */ 
    {
        @NotNull MField<?> $field();
    }

    public /* sealed */ interface MCoerce<T>
        extends
            MField<T>
        /* permits
            Coerce */ 
    {
        @NotNull MField<?> $field();
    }

    public interface MParam<T> extends MField<T>, UEmpty {
        T $value();
        @NotNull MParam<T> $value(T value);
    }

    public interface MInline<T> extends MParam<T> {}
    public interface MVal<T> extends MParam<T> {}

    public interface MFieldRef<T> extends UEmptyField<T>, MNamed {
        @NotNull MTableRef<?> $table();
    }

    public /* sealed */ interface MDefault<T>
        extends
            MField<T>,
            UEmpty
        /* permits
            Default */ 
    {}

    public /* sealed */ interface MCollated
        extends
            MField<String>
        /* permits
            Collated */ 
    {
        @NotNull MField<?> $field();
        @NotNull MCollation $collation();
    }

    public /* sealed */ interface MArray<T>
        extends
            MField<T[]>
        /* permits
            org.jooq.impl.Array */ 
    {
        @NotNull MList<? extends MField<?>> $elements();
    }

    public /* sealed */ interface MArrayQuery<T>
        extends
            MField<T[]>
        /* permits
            ArrayQuery */ 
    {
        @NotNull MSelect<? extends Record1<T>> $select();
    }

    public /* sealed */ interface MMultiset<R extends Record>
        extends
            MField<Result<R>>
        /* permits
            Multiset */ 
    {
        @NotNull MSelect<R> $select();
    }

    public /* sealed */ interface MScalarSubquery<T>
        extends
            MField<T>,
            UOperator1<MSelect<? extends Record1<T>>, MField<T>>
        /* permits
            ScalarSubquery */ 
    {}

    public /* sealed */ interface MNeg<T>
        extends
            MField<T>,
            UOperator1<MField<T>, MField<T>>
        /* permits
            Neg */ 
    {}

    public /* sealed */ interface MGreatest<T>
        extends
            MField<T>,
            UOperator1<MList<? extends MField<T>>, MField<T>>
        /* permits
            Greatest */ 
    {}

    public /* sealed */ interface MLeast<T>
        extends
            MField<T>,
            UOperator1<MList<? extends MField<T>>, MField<T>>
        /* permits
            Least */ 
    {}

    public /* sealed */ interface MChoose<T>
        extends
            MField<T>,
            UOperator2<MField<Integer>, MList<? extends MField<T>>, MField<T>>
        /* permits
            Choose */ 
    {}

    public /* sealed */ interface MFieldFunction<T>
        extends
            MField<Integer>,
            UOperator2<MField<T>, MList<? extends MField<T>>, MField<Integer>>
        /* permits
            FieldFunction */ 
    {}

    public /* sealed */ interface MNvl2<T>
        extends
            MField<T>,
            UOperator3<MField<?>, MField<T>, MField<T>, MField<T>>
        /* permits
            Nvl2 */ 
    {
        @NotNull default MField<?> $value() { return $arg1(); }
        @NotNull default MField<T> $ifNotNull() { return $arg2(); }
        @NotNull default MField<T> $ifIfNull() { return $arg3(); }
    }

    public /* sealed */ interface MIif<T>
        extends
            MField<T>,
            UOperator3<MCondition, MField<T>, MField<T>, MField<T>>
        /* permits
            Iif */ 
    {
        @NotNull default MCondition $condition() { return $arg1(); }
        @NotNull default MField<T> $ifTrue() { return $arg2(); }
        @NotNull default MField<T> $ifFalse() { return $arg3(); }
    }

    public /* sealed */ interface MCoalesce<T>
        extends
            MField<T>,
            UOperator1<MList<? extends MField<T>>, MField<T>>
        /* permits
            Coalesce */ 
    {}

    public /* sealed */ interface MConcat
        extends
            MField<String>,
            UOperator1<MList<? extends MField<?>>, MField<String>>
        /* permits
            Concat */ 
    {}

    public /* sealed */ interface MTimestampDiff<T>
        extends
            MField<DayToSecond>,
            UOperator2<MField<T>, MField<T>, MField<DayToSecond>>
        /* permits
            TimestampDiff */ 
    {
        @NotNull default MField<T> $minuend() { return $arg1(); }
        @NotNull default MField<T> $subtrahend() { return $arg2(); }
    }

    public /* sealed */ interface MConvert<T>
        extends
            MField<T>
        /* permits
            ConvertDateTime */ 
    {
        @NotNull MField<?> $field();
        int $style();
    }

    public /* sealed */ interface MCurrentDate<T>
        extends
            MField<T>,
            UEmpty
        /* permits
            CurrentDate */ 
    {}

    public /* sealed */ interface MCurrentTime<T>
        extends
            MField<T>,
            UEmpty
        /* permits
            CurrentTime */ 
    {}

    public /* sealed */ interface MCurrentTimestamp<T>
        extends
            MField<T>,
            UEmpty
        /* permits
            CurrentTimestamp */ 
    {}

    public /* sealed */ interface MXMLQuery
        extends
            MField<XML>
        /* permits
            XMLQuery */ 
    {
        @NotNull Field<String> $xpath();
        @NotNull Field<XML> $passing();
        @Nullable XmlPassingMechanism $passingMechanism();
    }

    public interface MXMLAttributes extends MQueryPart {
        @NotNull MList<? extends Field<?>> $attributes();
    }

    public /* sealed */ interface MXMLElement
        extends
            MField<XML>
        /* permits
            XMLElement */ 
    {
        @NotNull MName $elementName();
        @NotNull MXMLAttributes $attributes();
        @NotNull MList<? extends Field<?>> $content();
    }

    public /* sealed */ interface MXMLExists
        extends
            MCondition
        /* permits
            XMLExists */ 
    {
        @NotNull Field<String> $xpath();
        @NotNull Field<XML> $passing();
        @Nullable XmlPassingMechanism $passingMechanism();
    }

    public /* sealed */ interface MXMLParse
        extends
            MField<XML>
        /* permits
            XMLParse */ 
    {
        @NotNull Field<String> $content();
        @NotNull DocumentOrContent $documentOrContent();
    }



    public /* sealed */ interface MAlterDatabase
        extends
            MDDLQuery
        /* permits
            AlterDatabaseImpl */ 
    {
        @NotNull  MCatalog $database();
                  boolean $ifExists();
        @NotNull  MCatalog $renameTo();
        @NotNull  MAlterDatabase $database(MCatalog database);
        @NotNull  MAlterDatabase $ifExists(boolean ifExists);
        @NotNull  MAlterDatabase $renameTo(MCatalog renameTo);
    }

    public /* sealed */ interface MAlterDomain<T>
        extends
            MDDLQuery
        /* permits
            AlterDomainImpl */ 
    {
        @NotNull  MDomain<T> $domain();
                  boolean $ifExists();
        @Nullable MConstraint $addConstraint();
        @Nullable MConstraint $dropConstraint();
                  boolean $dropConstraintIfExists();
        @Nullable MDomain<?> $renameTo();
        @Nullable MConstraint $renameConstraint();
                  boolean $renameConstraintIfExists();
        @Nullable MField<T> $setDefault();
                  boolean $dropDefault();
                  boolean $setNotNull();
                  boolean $dropNotNull();
        @Nullable Cascade $cascade();
        @Nullable MConstraint $renameConstraintTo();
        @NotNull  MAlterDomain<T> $domain(MDomain<T> domain);
        @NotNull  MAlterDomain<T> $ifExists(boolean ifExists);
        @NotNull  MAlterDomain<T> $addConstraint(MConstraint addConstraint);
        @NotNull  MAlterDomain<T> $dropConstraint(MConstraint dropConstraint);
        @NotNull  MAlterDomain<T> $dropConstraintIfExists(boolean dropConstraintIfExists);
        @NotNull  MAlterDomain<T> $renameTo(MDomain<?> renameTo);
        @NotNull  MAlterDomain<T> $renameConstraint(MConstraint renameConstraint);
        @NotNull  MAlterDomain<T> $renameConstraintIfExists(boolean renameConstraintIfExists);
        @NotNull  MAlterDomain<T> $setDefault(MField<T> setDefault);
        @NotNull  MAlterDomain<T> $dropDefault(boolean dropDefault);
        @NotNull  MAlterDomain<T> $setNotNull(boolean setNotNull);
        @NotNull  MAlterDomain<T> $dropNotNull(boolean dropNotNull);
        @NotNull  MAlterDomain<T> $cascade(Cascade cascade);
        @NotNull  MAlterDomain<T> $renameConstraintTo(MConstraint renameConstraintTo);
    }

    public /* sealed */ interface MAlterIndex
        extends
            MDDLQuery
        /* permits
            AlterIndexImpl */ 
    {
        @NotNull  MIndex $index();
                  boolean $ifExists();
        @Nullable MTable<?> $on();
        @NotNull  MIndex $renameTo();
        @NotNull  MAlterIndex $index(MIndex index);
        @NotNull  MAlterIndex $ifExists(boolean ifExists);
        @NotNull  MAlterIndex $on(MTable<?> on);
        @NotNull  MAlterIndex $renameTo(MIndex renameTo);
    }

    public /* sealed */ interface MAlterSchema
        extends
            MDDLQuery
        /* permits
            AlterSchemaImpl */ 
    {
        @NotNull  MSchema $schema();
                  boolean $ifExists();
        @NotNull  MSchema $renameTo();
        @NotNull  MAlterSchema $schema(MSchema schema);
        @NotNull  MAlterSchema $ifExists(boolean ifExists);
        @NotNull  MAlterSchema $renameTo(MSchema renameTo);
    }

    public /* sealed */ interface MAlterSequence<T extends Number>
        extends
            MDDLQuery
        /* permits
            AlterSequenceImpl */ 
    {
        @NotNull  MSequence<T> $sequence();
                  boolean $ifExists();
        @Nullable MSequence<?> $renameTo();
                  boolean $restart();
        @Nullable MField<T> $restartWith();
        @Nullable MField<T> $startWith();
        @Nullable MField<T> $incrementBy();
        @Nullable MField<T> $minvalue();
                  boolean $noMinvalue();
        @Nullable MField<T> $maxvalue();
                  boolean $noMaxvalue();
        @Nullable CycleOption $cycle();
        @Nullable MField<T> $cache();
                  boolean $noCache();
        @NotNull  MAlterSequence<T> $sequence(MSequence<T> sequence);
        @NotNull  MAlterSequence<T> $ifExists(boolean ifExists);
        @NotNull  MAlterSequence<T> $renameTo(MSequence<?> renameTo);
        @NotNull  MAlterSequence<T> $restart(boolean restart);
        @NotNull  MAlterSequence<T> $restartWith(MField<T> restartWith);
        @NotNull  MAlterSequence<T> $startWith(MField<T> startWith);
        @NotNull  MAlterSequence<T> $incrementBy(MField<T> incrementBy);
        @NotNull  MAlterSequence<T> $minvalue(MField<T> minvalue);
        @NotNull  MAlterSequence<T> $noMinvalue(boolean noMinvalue);
        @NotNull  MAlterSequence<T> $maxvalue(MField<T> maxvalue);
        @NotNull  MAlterSequence<T> $noMaxvalue(boolean noMaxvalue);
        @NotNull  MAlterSequence<T> $cycle(CycleOption cycle);
        @NotNull  MAlterSequence<T> $cache(MField<T> cache);
        @NotNull  MAlterSequence<T> $noCache(boolean noCache);
    }

    public /* sealed */ interface MAlterType
        extends
            MDDLQuery
        /* permits
            AlterTypeImpl */ 
    {
        @NotNull  MName $type();
        @Nullable MName $renameTo();
        @Nullable MSchema $setSchema();
        @Nullable MField<String> $addValue();
        @Nullable MField<String> $renameValue();
        @Nullable MField<String> $renameValueTo();
        @NotNull  MAlterType $type(MName type);
        @NotNull  MAlterType $renameTo(MName renameTo);
        @NotNull  MAlterType $setSchema(MSchema setSchema);
        @NotNull  MAlterType $addValue(MField<String> addValue);
        @NotNull  MAlterType $renameValue(MField<String> renameValue);
        @NotNull  MAlterType $renameValueTo(MField<String> renameValueTo);
    }

    public /* sealed */ interface MAlterView
        extends
            MDDLQuery
        /* permits
            AlterViewImpl */ 
    {
        @NotNull  MTable<?> $view();
                  boolean $ifExists();
        @Nullable MComment $comment();
        @Nullable MTable<?> $renameTo();
        @NotNull  MAlterView $view(MTable<?> view);
        @NotNull  MAlterView $ifExists(boolean ifExists);
        @NotNull  MAlterView $comment(MComment comment);
        @NotNull  MAlterView $renameTo(MTable<?> renameTo);
    }

    public /* sealed */ interface MCommentOn
        extends
            MDDLQuery
        /* permits
            CommentOnImpl */ 
    {
        @Nullable MTable<?> $table();
                  boolean $isView();
        @Nullable MField<?> $field();
        @NotNull  MComment $comment();
        @NotNull  MCommentOn $table(MTable<?> table);
        @NotNull  MCommentOn $isView(boolean isView);
        @NotNull  MCommentOn $field(MField<?> field);
        @NotNull  MCommentOn $comment(MComment comment);
    }

    public /* sealed */ interface MCreateDatabase
        extends
            MDDLQuery
        /* permits
            CreateDatabaseImpl */ 
    {
        @NotNull  MCatalog $database();
                  boolean $ifNotExists();
        @NotNull  MCreateDatabase $database(MCatalog database);
        @NotNull  MCreateDatabase $ifNotExists(boolean ifNotExists);
    }

    public /* sealed */ interface MCreateDomain<T>
        extends
            MDDLQuery
        /* permits
            CreateDomainImpl */ 
    {
        @NotNull  MDomain<?> $domain();
                  boolean $ifNotExists();
        @NotNull  MDataType<T> $dataType();
        @Nullable MField<T> $default_();
        @NotNull  MList<? extends Constraint> $constraints();
        @NotNull  MCreateDomain<T> $domain(MDomain<?> domain);
        @NotNull  MCreateDomain<T> $ifNotExists(boolean ifNotExists);
        @NotNull  MCreateDomain<T> $dataType(MDataType<T> dataType);
        @NotNull  MCreateDomain<T> $default_(MField<T> default_);
        @NotNull  MCreateDomain<T> $constraints(MList<? extends Constraint> constraints);
    }






























    public /* sealed */ interface MCreateIndex
        extends
            MDDLQuery
        /* permits
            CreateIndexImpl */ 
    {
                  boolean $unique();
        @Nullable MIndex $index();
                  boolean $ifNotExists();
        @Nullable MTable<?> $table();
        @NotNull  MList<? extends OrderField<?>> $on();
        @NotNull  MList<? extends Field<?>> $include();
        @Nullable MCondition $where();
                  boolean $excludeNullKeys();
        @NotNull  MCreateIndex $unique(boolean unique);
        @NotNull  MCreateIndex $index(MIndex index);
        @NotNull  MCreateIndex $ifNotExists(boolean ifNotExists);
        @NotNull  MCreateIndex $table(MTable<?> table);
        @NotNull  MCreateIndex $on(MList<? extends OrderField<?>> on);
        @NotNull  MCreateIndex $include(MList<? extends Field<?>> include);
        @NotNull  MCreateIndex $where(MCondition where);
        @NotNull  MCreateIndex $excludeNullKeys(boolean excludeNullKeys);
    }































































    public /* sealed */ interface MCreateSchema
        extends
            MDDLQuery
        /* permits
            CreateSchemaImpl */ 
    {
        @NotNull  MSchema $schema();
                  boolean $ifNotExists();
        @NotNull  MCreateSchema $schema(MSchema schema);
        @NotNull  MCreateSchema $ifNotExists(boolean ifNotExists);
    }

    public /* sealed */ interface MCreateSequence
        extends
            MDDLQuery
        /* permits
            CreateSequenceImpl */ 
    {
        @NotNull  MSequence<?> $sequence();
                  boolean $ifNotExists();
        @Nullable MField<? extends Number> $startWith();
        @Nullable MField<? extends Number> $incrementBy();
        @Nullable MField<? extends Number> $minvalue();
                  boolean $noMinvalue();
        @Nullable MField<? extends Number> $maxvalue();
                  boolean $noMaxvalue();
        @Nullable CycleOption $cycle();
        @Nullable MField<? extends Number> $cache();
                  boolean $noCache();
        @NotNull  MCreateSequence $sequence(MSequence<?> sequence);
        @NotNull  MCreateSequence $ifNotExists(boolean ifNotExists);
        @NotNull  MCreateSequence $startWith(MField<? extends Number> startWith);
        @NotNull  MCreateSequence $incrementBy(MField<? extends Number> incrementBy);
        @NotNull  MCreateSequence $minvalue(MField<? extends Number> minvalue);
        @NotNull  MCreateSequence $noMinvalue(boolean noMinvalue);
        @NotNull  MCreateSequence $maxvalue(MField<? extends Number> maxvalue);
        @NotNull  MCreateSequence $noMaxvalue(boolean noMaxvalue);
        @NotNull  MCreateSequence $cycle(CycleOption cycle);
        @NotNull  MCreateSequence $cache(MField<? extends Number> cache);
        @NotNull  MCreateSequence $noCache(boolean noCache);
    }

    public /* sealed */ interface MDropDatabase
        extends
            MDDLQuery
        /* permits
            DropDatabaseImpl */ 
    {
        @NotNull  MCatalog $database();
                  boolean $ifExists();
        @NotNull  MDropDatabase $database(MCatalog database);
        @NotNull  MDropDatabase $ifExists(boolean ifExists);
    }

    public /* sealed */ interface MDropDomain
        extends
            MDDLQuery
        /* permits
            DropDomainImpl */ 
    {
        @NotNull  MDomain<?> $domain();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @NotNull  MDropDomain $domain(MDomain<?> domain);
        @NotNull  MDropDomain $ifExists(boolean ifExists);
        @NotNull  MDropDomain $cascade(Cascade cascade);
    }
















    public /* sealed */ interface MDropIndex
        extends
            MDDLQuery
        /* permits
            DropIndexImpl */ 
    {
        @NotNull  MIndex $index();
                  boolean $ifExists();
        @Nullable MTable<?> $on();
        @Nullable Cascade $cascade();
        @NotNull  MDropIndex $index(MIndex index);
        @NotNull  MDropIndex $ifExists(boolean ifExists);
        @NotNull  MDropIndex $on(MTable<?> on);
        @NotNull  MDropIndex $cascade(Cascade cascade);
    }
















    public /* sealed */ interface MDropSchema
        extends
            MDDLQuery
        /* permits
            DropSchemaImpl */ 
    {
        @NotNull  MSchema $schema();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @NotNull  MDropSchema $schema(MSchema schema);
        @NotNull  MDropSchema $ifExists(boolean ifExists);
        @NotNull  MDropSchema $cascade(Cascade cascade);
    }

    public /* sealed */ interface MDropSequence
        extends
            MDDLQuery
        /* permits
            DropSequenceImpl */ 
    {
        @NotNull  MSequence<?> $sequence();
                  boolean $ifExists();
        @NotNull  MDropSequence $sequence(MSequence<?> sequence);
        @NotNull  MDropSequence $ifExists(boolean ifExists);
    }

    public /* sealed */ interface MDropTable
        extends
            MDDLQuery
        /* permits
            DropTableImpl */ 
    {
                  boolean $temporary();
        @NotNull  MTable<?> $table();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @NotNull  MDropTable $temporary(boolean temporary);
        @NotNull  MDropTable $table(MTable<?> table);
        @NotNull  MDropTable $ifExists(boolean ifExists);
        @NotNull  MDropTable $cascade(Cascade cascade);
    }
















    public /* sealed */ interface MDropView
        extends
            MDDLQuery
        /* permits
            DropViewImpl */ 
    {
        @NotNull  MTable<?> $view();
                  boolean $ifExists();
        @NotNull  MDropView $view(MTable<?> view);
        @NotNull  MDropView $ifExists(boolean ifExists);
    }

    public /* sealed */ interface MGrant
        extends
            MDDLQuery
        /* permits
            GrantImpl */ 
    {
        @NotNull  MList<? extends Privilege> $privileges();
        @NotNull  MTable<?> $on();
        @Nullable MRole $to();
                  boolean $toPublic();
                  boolean $withGrantOption();
        @NotNull  MGrant $privileges(MList<? extends Privilege> privileges);
        @NotNull  MGrant $on(MTable<?> on);
        @NotNull  MGrant $to(MRole to);
        @NotNull  MGrant $toPublic(boolean toPublic);
        @NotNull  MGrant $withGrantOption(boolean withGrantOption);
    }

    public /* sealed */ interface MRevoke
        extends
            MDDLQuery
        /* permits
            RevokeImpl */ 
    {
        @NotNull  MList<? extends Privilege> $privileges();
                  boolean $grantOptionFor();
        @NotNull  MTable<?> $on();
        @Nullable MRole $from();
                  boolean $fromPublic();
        @NotNull  MRevoke $privileges(MList<? extends Privilege> privileges);
        @NotNull  MRevoke $grantOptionFor(boolean grantOptionFor);
        @NotNull  MRevoke $on(MTable<?> on);
        @NotNull  MRevoke $from(MRole from);
        @NotNull  MRevoke $fromPublic(boolean fromPublic);
    }

    public /* sealed */ interface MSetCommand
        extends
            MRowCountQuery
        /* permits
            SetCommand */ 
    {
        @NotNull  MName $name();
        @NotNull  MParam<?> $value();
                  boolean $local();
        @NotNull  MSetCommand $name(MName name);
        @NotNull  MSetCommand $value(MParam<?> value);
        @NotNull  MSetCommand $local(boolean local);
    }

    public /* sealed */ interface MSetCatalog
        extends
            MRowCountQuery
        /* permits
            SetCatalog */ 
    {
        @NotNull  MCatalog $catalog();
        @NotNull  MSetCatalog $catalog(MCatalog catalog);
    }

    public /* sealed */ interface MSetSchema
        extends
            MRowCountQuery
        /* permits
            SetSchema */ 
    {
        @NotNull  MSchema $schema();
        @NotNull  MSetSchema $schema(MSchema schema);
    }

    public /* sealed */ interface MTruncate<R extends Record>
        extends
            MDDLQuery
        /* permits
            TruncateImpl */ 
    {
        @NotNull  MTable<R> $table();
        @Nullable IdentityRestartOption $restartIdentity();
        @Nullable Cascade $cascade();
        @NotNull  MTruncate<R> $table(MTable<R> table);
        @NotNull  MTruncate<R> $restartIdentity(IdentityRestartOption restartIdentity);
        @NotNull  MTruncate<R> $cascade(Cascade cascade);
    }
















    public /* sealed */ interface MAnd
        extends
            MCombinedCondition
        /* permits
            And */ 
    {}

    public /* sealed */ interface MTableEq<R extends Record>
        extends
            MCondition,
            UOperator2<MTable<R>, MTable<R>, MCondition>
        /* permits
            TableEq */ 
    {}

    public /* sealed */ interface MEq<T>
        extends
            MCompareCondition<T>
        /* permits
            Eq */ 
    {}

    public /* sealed */ interface MExists
        extends
            MCondition
        /* permits
            Exists */ 
    {
        @NotNull  MSelect<?> $query();
        @NotNull  MExists $query(MSelect<?> query);
    }

    public /* sealed */ interface MGe<T>
        extends
            MCompareCondition<T>
        /* permits
            Ge */ 
    {}

    public /* sealed */ interface MGt<T>
        extends
            MCompareCondition<T>
        /* permits
            Gt */ 
    {}

    public /* sealed */ interface MIn<T>
        extends
            MCondition,
            UOperator2<MField<T>, MSelect<? extends Record1<T>>, MCondition>
        /* permits
            In */ 
    {}

    public /* sealed */ interface MIsDistinctFrom<T>
        extends
            MCompareCondition<T>
        /* permits
            IsDistinctFrom */ 
    {}

    public /* sealed */ interface MIsNull
        extends
            MCondition,
            UOperator1<MField<?>, MCondition>
        /* permits
            IsNull */ 
    {
        @NotNull  default MField<?> $field() { return $arg1(); }
    }

    public /* sealed */ interface MIsNotDistinctFrom<T>
        extends
            MCompareCondition<T>
        /* permits
            IsNotDistinctFrom */ 
    {}

    public /* sealed */ interface MIsNotNull
        extends
            MCondition,
            UOperator1<MField<?>, MCondition>
        /* permits
            IsNotNull */ 
    {
        @NotNull  default MField<?> $field() { return $arg1(); }
    }

    public /* sealed */ interface MLe<T>
        extends
            MCompareCondition<T>
        /* permits
            Le */ 
    {}

    public /* sealed */ interface MLike
        extends
            MCondition,
            UOperator3<MField<?>, MField<String>, Character, MCondition>
        /* permits
            Like */ 
    {
        @NotNull  default MField<?> $value() { return $arg1(); }
        @NotNull  default MField<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    public /* sealed */ interface MLikeIgnoreCase
        extends
            MCondition,
            UOperator3<MField<?>, MField<String>, Character, MCondition>
        /* permits
            LikeIgnoreCase */ 
    {
        @NotNull  default MField<?> $value() { return $arg1(); }
        @NotNull  default MField<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    public /* sealed */ interface MLt<T>
        extends
            MCompareCondition<T>
        /* permits
            Lt */ 
    {}

    public /* sealed */ interface MTableNe<R extends Record>
        extends
            MCondition,
            UOperator2<MTable<R>, MTable<R>, MCondition>
        /* permits
            TableNe */ 
    {}

    public /* sealed */ interface MNe<T>
        extends
            MCompareCondition<T>
        /* permits
            Ne */ 
    {}

    public /* sealed */ interface MNot
        extends
            MCondition,
            UOperator1<MCondition, MCondition>
        /* permits
            Not */ 
    {
        @NotNull  default MCondition $condition() { return $arg1(); }
    }

    public /* sealed */ interface MNotField
        extends
            MField<Boolean>,
            UOperator1<MField<Boolean>, MField<Boolean>>
        /* permits
            NotField */ 
    {
        @NotNull  default MField<Boolean> $field() { return $arg1(); }
    }

    public /* sealed */ interface MNotIn<T>
        extends
            MCondition,
            UOperator2<MField<T>, MSelect<? extends Record1<T>>, MCondition>
        /* permits
            NotIn */ 
    {}

    public /* sealed */ interface MNotLike
        extends
            MCondition,
            UOperator3<MField<?>, MField<String>, Character, MCondition>
        /* permits
            NotLike */ 
    {
        @NotNull  default MField<?> $value() { return $arg1(); }
        @NotNull  default MField<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    public /* sealed */ interface MNotLikeIgnoreCase
        extends
            MCondition,
            UOperator3<MField<?>, MField<String>, Character, MCondition>
        /* permits
            NotLikeIgnoreCase */ 
    {
        @NotNull  default MField<?> $value() { return $arg1(); }
        @NotNull  default MField<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    public /* sealed */ interface MNotSimilarTo
        extends
            MCondition,
            UOperator3<MField<?>, MField<String>, Character, MCondition>
        /* permits
            NotSimilarTo */ 
    {
        @NotNull  default MField<?> $value() { return $arg1(); }
        @NotNull  default MField<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    public /* sealed */ interface MOr
        extends
            MCombinedCondition
        /* permits
            Or */ 
    {}

    public /* sealed */ interface MSimilarTo
        extends
            MCondition,
            UOperator3<MField<?>, MField<String>, Character, MCondition>
        /* permits
            SimilarTo */ 
    {
        @NotNull  default MField<?> $value() { return $arg1(); }
        @NotNull  default MField<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    public /* sealed */ interface MUnique
        extends
            MCondition
        /* permits
            Unique */ 
    {
        @NotNull  MSelect<?> $query();
        @NotNull  MUnique $query(MSelect<?> query);
    }

    public /* sealed */ interface MIsDocument
        extends
            MCondition,
            UOperator1<MField<?>, MCondition>
        /* permits
            IsDocument */ 
    {
        @NotNull  default MField<?> $field() { return $arg1(); }
    }

    public /* sealed */ interface MIsNotDocument
        extends
            MCondition,
            UOperator1<MField<?>, MCondition>
        /* permits
            IsNotDocument */ 
    {
        @NotNull  default MField<?> $field() { return $arg1(); }
    }

    public /* sealed */ interface MIsJson
        extends
            MCondition,
            UOperator1<MField<?>, MCondition>
        /* permits
            IsJson */ 
    {
        @NotNull  default MField<?> $field() { return $arg1(); }
    }

    public /* sealed */ interface MIsNotJson
        extends
            MCondition,
            UOperator1<MField<?>, MCondition>
        /* permits
            IsNotJson */ 
    {
        @NotNull  default MField<?> $field() { return $arg1(); }
    }

    public /* sealed */ interface MQualifiedRowid
        extends
            MField<RowId>,
            UOperator1<MTable<?>, MField<RowId>>
        /* permits
            QualifiedRowid */ 
    {
        @NotNull  default MTable<?> $table() { return $arg1(); }
    }

    public /* sealed */ interface MAbs<T extends Number>
        extends
            MField<T>
        /* permits
            Abs */ 
    {
        @NotNull  MField<T> $number();
        @NotNull  MAbs<T> $number(MField<T> number);
    }

    public /* sealed */ interface MAcos
        extends
            MField<BigDecimal>
        /* permits
            Acos */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MAcos $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MAsin
        extends
            MField<BigDecimal>
        /* permits
            Asin */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MAsin $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MAtan
        extends
            MField<BigDecimal>
        /* permits
            Atan */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MAtan $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MAtan2
        extends
            MField<BigDecimal>
        /* permits
            Atan2 */ 
    {
        @NotNull  MField<? extends Number> $x();
        @NotNull  MField<? extends Number> $y();
        @NotNull  MAtan2 $x(MField<? extends Number> x);
        @NotNull  MAtan2 $y(MField<? extends Number> y);
    }

    public /* sealed */ interface MBitAnd<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<T>, MField<T>>
        /* permits
            BitAnd */ 
    {}

    public /* sealed */ interface MBitCount
        extends
            MField<Integer>
        /* permits
            BitCount */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MBitCount $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MBitNand<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<T>, MField<T>>
        /* permits
            BitNand */ 
    {}

    public /* sealed */ interface MBitNor<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<T>, MField<T>>
        /* permits
            BitNor */ 
    {}

    public /* sealed */ interface MBitNot<T extends Number>
        extends
            MField<T>,
            UOperator1<MField<T>, MField<T>>
        /* permits
            BitNot */ 
    {}

    public /* sealed */ interface MBitOr<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<T>, MField<T>>
        /* permits
            BitOr */ 
    {}

    public /* sealed */ interface MBitXNor<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<T>, MField<T>>
        /* permits
            BitXNor */ 
    {}

    public /* sealed */ interface MBitXor<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<T>, MField<T>>
        /* permits
            BitXor */ 
    {}

    public /* sealed */ interface MCeil<T extends Number>
        extends
            MField<T>
        /* permits
            Ceil */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MCeil<T> $value(MField<T> value);
    }

    public /* sealed */ interface MCos
        extends
            MField<BigDecimal>
        /* permits
            Cos */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MCos $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MCosh
        extends
            MField<BigDecimal>
        /* permits
            Cosh */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MCosh $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MCot
        extends
            MField<BigDecimal>
        /* permits
            Cot */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MCot $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MCoth
        extends
            MField<BigDecimal>
        /* permits
            Coth */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MCoth $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MDegrees
        extends
            MField<BigDecimal>
        /* permits
            Degrees */ 
    {
        @NotNull  MField<? extends Number> $radians();
        @NotNull  MDegrees $radians(MField<? extends Number> radians);
    }

    public /* sealed */ interface MEuler
        extends
            MField<BigDecimal>,
            UEmpty
        /* permits
            Euler */ 
    {}

    public /* sealed */ interface MExp
        extends
            MField<BigDecimal>
        /* permits
            Exp */ 
    {
        @NotNull  MField<? extends Number> $value();
        @NotNull  MExp $value(MField<? extends Number> value);
    }

    public /* sealed */ interface MFloor<T extends Number>
        extends
            MField<T>
        /* permits
            Floor */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MFloor<T> $value(MField<T> value);
    }

    public /* sealed */ interface MLog
        extends
            MField<BigDecimal>
        /* permits
            Log */ 
    {
        @NotNull  MField<? extends Number> $value();
        @Nullable MField<? extends Number> $base();
        @NotNull  MLog $value(MField<? extends Number> value);
        @NotNull  MLog $base(MField<? extends Number> base);
    }

    public /* sealed */ interface MLog10
        extends
            MField<BigDecimal>
        /* permits
            Log10 */ 
    {
        @NotNull  MField<? extends Number> $value();
        @NotNull  MLog10 $value(MField<? extends Number> value);
    }

    public /* sealed */ interface MMod<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<? extends Number>, MField<T>>
        /* permits
            Mod */ 
    {
        @NotNull  default MField<T> $dividend() { return $arg1(); }
        @NotNull  default MField<? extends Number> $divisor() { return $arg2(); }
    }

    public /* sealed */ interface MPi
        extends
            MField<BigDecimal>,
            UEmpty
        /* permits
            Pi */ 
    {}

    public /* sealed */ interface MPower
        extends
            MField<BigDecimal>,
            UOperator2<MField<? extends Number>, MField<? extends Number>, MField<BigDecimal>>
        /* permits
            Power */ 
    {
        @NotNull  default MField<? extends Number> $base() { return $arg1(); }
        @NotNull  default MField<? extends Number> $exponent() { return $arg2(); }
    }

    public /* sealed */ interface MRadians
        extends
            MField<BigDecimal>
        /* permits
            Radians */ 
    {
        @NotNull  MField<? extends Number> $degrees();
        @NotNull  MRadians $degrees(MField<? extends Number> degrees);
    }

    public /* sealed */ interface MRand
        extends
            MField<BigDecimal>,
            UEmpty
        /* permits
            Rand */ 
    {}

    public /* sealed */ interface MRound<T extends Number>
        extends
            MField<T>
        /* permits
            Round */ 
    {
        @NotNull  MField<T> $value();
        @Nullable MField<Integer> $decimals();
        @NotNull  MRound<T> $value(MField<T> value);
        @NotNull  MRound<T> $decimals(MField<Integer> decimals);
    }

    public /* sealed */ interface MShl<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<? extends Number>, MField<T>>
        /* permits
            Shl */ 
    {
        @NotNull  default MField<T> $value() { return $arg1(); }
        @NotNull  default MField<? extends Number> $count() { return $arg2(); }
    }

    public /* sealed */ interface MShr<T extends Number>
        extends
            MField<T>,
            UOperator2<MField<T>, MField<? extends Number>, MField<T>>
        /* permits
            Shr */ 
    {
        @NotNull  default MField<T> $value() { return $arg1(); }
        @NotNull  default MField<? extends Number> $count() { return $arg2(); }
    }

    public /* sealed */ interface MSign
        extends
            MField<Integer>
        /* permits
            Sign */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MSign $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MSin
        extends
            MField<BigDecimal>
        /* permits
            Sin */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MSin $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MSinh
        extends
            MField<BigDecimal>
        /* permits
            Sinh */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MSinh $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MSqrt
        extends
            MField<BigDecimal>
        /* permits
            Sqrt */ 
    {
        @NotNull  MField<? extends Number> $value();
        @NotNull  MSqrt $value(MField<? extends Number> value);
    }

    public /* sealed */ interface MSquare<T extends Number>
        extends
            MField<T>
        /* permits
            Square */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MSquare<T> $value(MField<T> value);
    }

    public /* sealed */ interface MTan
        extends
            MField<BigDecimal>
        /* permits
            Tan */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MTan $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MTanh
        extends
            MField<BigDecimal>
        /* permits
            Tanh */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MTanh $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MTau
        extends
            MField<BigDecimal>,
            UEmpty
        /* permits
            Tau */ 
    {}

    public /* sealed */ interface MTrunc<T extends Number>
        extends
            MField<T>
        /* permits
            Trunc */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MField<Integer> $decimals();
        @NotNull  MTrunc<T> $value(MField<T> value);
        @NotNull  MTrunc<T> $decimals(MField<Integer> decimals);
    }

    public /* sealed */ interface MWidthBucket<T extends Number>
        extends
            MField<T>
        /* permits
            WidthBucket */ 
    {
        @NotNull  MField<T> $field();
        @NotNull  MField<T> $low();
        @NotNull  MField<T> $high();
        @NotNull  MField<Integer> $buckets();
        @NotNull  MWidthBucket<T> $field(MField<T> field);
        @NotNull  MWidthBucket<T> $low(MField<T> low);
        @NotNull  MWidthBucket<T> $high(MField<T> high);
        @NotNull  MWidthBucket<T> $buckets(MField<Integer> buckets);
    }

    public /* sealed */ interface MAscii
        extends
            MField<Integer>
        /* permits
            Ascii */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MAscii $string(MField<String> string);
    }

    public /* sealed */ interface MBitLength
        extends
            MField<Integer>
        /* permits
            BitLength */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MBitLength $string(MField<String> string);
    }

    public /* sealed */ interface MCharLength
        extends
            MField<Integer>
        /* permits
            CharLength */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MCharLength $string(MField<String> string);
    }

    public /* sealed */ interface MChr
        extends
            MField<String>
        /* permits
            Chr */ 
    {
        @NotNull  MField<? extends Number> $number();
        @NotNull  MChr $number(MField<? extends Number> number);
    }

    public /* sealed */ interface MContains<T>
        extends
            MCompareCondition<T>
        /* permits
            Contains */ 
    {
        @NotNull  default MField<T> $value() { return $arg1(); }
        @NotNull  default MField<T> $content() { return $arg2(); }
    }

    public /* sealed */ interface MContainsIgnoreCase<T>
        extends
            MCompareCondition<T>
        /* permits
            ContainsIgnoreCase */ 
    {
        @NotNull  default MField<T> $value() { return $arg1(); }
        @NotNull  default MField<T> $content() { return $arg2(); }
    }

    public /* sealed */ interface MDigits
        extends
            MField<String>
        /* permits
            Digits */ 
    {
        @NotNull  MField<? extends Number> $value();
        @NotNull  MDigits $value(MField<? extends Number> value);
    }

    public /* sealed */ interface MEndsWith<T>
        extends
            MCompareCondition<T>
        /* permits
            EndsWith */ 
    {
        @NotNull  default MField<T> $string() { return $arg1(); }
        @NotNull  default MField<T> $suffix() { return $arg2(); }
    }

    public /* sealed */ interface MEndsWithIgnoreCase<T>
        extends
            MCompareCondition<T>
        /* permits
            EndsWithIgnoreCase */ 
    {
        @NotNull  default MField<T> $string() { return $arg1(); }
        @NotNull  default MField<T> $suffix() { return $arg2(); }
    }

    public /* sealed */ interface MLeft
        extends
            MField<String>
        /* permits
            Left */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<? extends Number> $length();
        @NotNull  MLeft $string(MField<String> string);
        @NotNull  MLeft $length(MField<? extends Number> length);
    }

    public /* sealed */ interface MLower
        extends
            MField<String>
        /* permits
            Lower */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MLower $string(MField<String> string);
    }

    public /* sealed */ interface MLpad
        extends
            MField<String>
        /* permits
            Lpad */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<? extends Number> $length();
        @Nullable MField<String> $character();
        @NotNull  MLpad $string(MField<String> string);
        @NotNull  MLpad $length(MField<? extends Number> length);
        @NotNull  MLpad $character(MField<String> character);
    }

    public /* sealed */ interface MLtrim
        extends
            MField<String>
        /* permits
            Ltrim */ 
    {
        @NotNull  MField<String> $string();
        @Nullable MField<String> $characters();
        @NotNull  MLtrim $string(MField<String> string);
        @NotNull  MLtrim $characters(MField<String> characters);
    }

    public /* sealed */ interface MMd5
        extends
            MField<String>
        /* permits
            Md5 */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MMd5 $string(MField<String> string);
    }

    public /* sealed */ interface MOctetLength
        extends
            MField<Integer>
        /* permits
            OctetLength */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MOctetLength $string(MField<String> string);
    }

    public /* sealed */ interface MOverlay
        extends
            MField<String>
        /* permits
            Overlay */ 
    {
        @NotNull  MField<String> $in();
        @NotNull  MField<String> $placing();
        @NotNull  MField<? extends Number> $startIndex();
        @Nullable MField<? extends Number> $length();
        @NotNull  MOverlay $in(MField<String> in);
        @NotNull  MOverlay $placing(MField<String> placing);
        @NotNull  MOverlay $startIndex(MField<? extends Number> startIndex);
        @NotNull  MOverlay $length(MField<? extends Number> length);
    }

    public /* sealed */ interface MPosition
        extends
            MField<Integer>
        /* permits
            Position */ 
    {
        @NotNull  MField<String> $in();
        @NotNull  MField<String> $search();
        @Nullable MField<? extends Number> $startIndex();
        @NotNull  MPosition $in(MField<String> in);
        @NotNull  MPosition $search(MField<String> search);
        @NotNull  MPosition $startIndex(MField<? extends Number> startIndex);
    }

    public /* sealed */ interface MRepeat
        extends
            MField<String>
        /* permits
            Repeat */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<? extends Number> $count();
        @NotNull  MRepeat $string(MField<String> string);
        @NotNull  MRepeat $count(MField<? extends Number> count);
    }

    public /* sealed */ interface MReplace
        extends
            MField<String>
        /* permits
            Replace */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<String> $search();
        @Nullable MField<String> $replace();
        @NotNull  MReplace $string(MField<String> string);
        @NotNull  MReplace $search(MField<String> search);
        @NotNull  MReplace $replace(MField<String> replace);
    }

    public /* sealed */ interface MReverse
        extends
            MField<String>
        /* permits
            Reverse */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MReverse $string(MField<String> string);
    }

    public /* sealed */ interface MRight
        extends
            MField<String>
        /* permits
            Right */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<? extends Number> $length();
        @NotNull  MRight $string(MField<String> string);
        @NotNull  MRight $length(MField<? extends Number> length);
    }

    public /* sealed */ interface MRpad
        extends
            MField<String>
        /* permits
            Rpad */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<? extends Number> $length();
        @Nullable MField<String> $character();
        @NotNull  MRpad $string(MField<String> string);
        @NotNull  MRpad $length(MField<? extends Number> length);
        @NotNull  MRpad $character(MField<String> character);
    }

    public /* sealed */ interface MRtrim
        extends
            MField<String>
        /* permits
            Rtrim */ 
    {
        @NotNull  MField<String> $string();
        @Nullable MField<String> $characters();
        @NotNull  MRtrim $string(MField<String> string);
        @NotNull  MRtrim $characters(MField<String> characters);
    }

    public /* sealed */ interface MSpace
        extends
            MField<String>
        /* permits
            Space */ 
    {
        @NotNull  MField<? extends Number> $count();
        @NotNull  MSpace $count(MField<? extends Number> count);
    }

    public /* sealed */ interface MSplitPart
        extends
            MField<String>
        /* permits
            SplitPart */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<String> $delimiter();
        @NotNull  MField<? extends Number> $n();
        @NotNull  MSplitPart $string(MField<String> string);
        @NotNull  MSplitPart $delimiter(MField<String> delimiter);
        @NotNull  MSplitPart $n(MField<? extends Number> n);
    }

    public /* sealed */ interface MStartsWith<T>
        extends
            MCompareCondition<T>
        /* permits
            StartsWith */ 
    {
        @NotNull  default MField<T> $string() { return $arg1(); }
        @NotNull  default MField<T> $prefix() { return $arg2(); }
    }

    public /* sealed */ interface MStartsWithIgnoreCase<T>
        extends
            MCompareCondition<T>
        /* permits
            StartsWithIgnoreCase */ 
    {
        @NotNull  default MField<T> $string() { return $arg1(); }
        @NotNull  default MField<T> $prefix() { return $arg2(); }
    }

    public /* sealed */ interface MSubstring
        extends
            MField<String>
        /* permits
            Substring */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<? extends Number> $startingPosition();
        @Nullable MField<? extends Number> $length();
        @NotNull  MSubstring $string(MField<String> string);
        @NotNull  MSubstring $startingPosition(MField<? extends Number> startingPosition);
        @NotNull  MSubstring $length(MField<? extends Number> length);
    }

    public /* sealed */ interface MSubstringIndex
        extends
            MField<String>
        /* permits
            SubstringIndex */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<String> $delimiter();
        @NotNull  MField<? extends Number> $n();
        @NotNull  MSubstringIndex $string(MField<String> string);
        @NotNull  MSubstringIndex $delimiter(MField<String> delimiter);
        @NotNull  MSubstringIndex $n(MField<? extends Number> n);
    }

    public /* sealed */ interface MToChar
        extends
            MField<String>
        /* permits
            ToChar */ 
    {
        @NotNull  MField<?> $value();
        @Nullable MField<String> $formatMask();
        @NotNull  MToChar $value(MField<?> value);
        @NotNull  MToChar $formatMask(MField<String> formatMask);
    }

    public /* sealed */ interface MToDate
        extends
            MField<Date>
        /* permits
            ToDate */ 
    {
        @NotNull  MField<String> $value();
        @NotNull  MField<String> $formatMask();
        @NotNull  MToDate $value(MField<String> value);
        @NotNull  MToDate $formatMask(MField<String> formatMask);
    }

    public /* sealed */ interface MToHex
        extends
            MField<String>
        /* permits
            ToHex */ 
    {
        @NotNull  MField<? extends Number> $value();
        @NotNull  MToHex $value(MField<? extends Number> value);
    }

    public /* sealed */ interface MToTimestamp
        extends
            MField<Timestamp>
        /* permits
            ToTimestamp */ 
    {
        @NotNull  MField<String> $value();
        @NotNull  MField<String> $formatMask();
        @NotNull  MToTimestamp $value(MField<String> value);
        @NotNull  MToTimestamp $formatMask(MField<String> formatMask);
    }

    public /* sealed */ interface MTranslate
        extends
            MField<String>
        /* permits
            Translate */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MField<String> $from();
        @NotNull  MField<String> $to();
        @NotNull  MTranslate $string(MField<String> string);
        @NotNull  MTranslate $from(MField<String> from);
        @NotNull  MTranslate $to(MField<String> to);
    }

    public /* sealed */ interface MTrim
        extends
            MField<String>
        /* permits
            Trim */ 
    {
        @NotNull  MField<String> $string();
        @Nullable MField<String> $characters();
        @NotNull  MTrim $string(MField<String> string);
        @NotNull  MTrim $characters(MField<String> characters);
    }

    public /* sealed */ interface MUpper
        extends
            MField<String>
        /* permits
            Upper */ 
    {
        @NotNull  MField<String> $string();
        @NotNull  MUpper $string(MField<String> string);
    }

    public /* sealed */ interface MUuid
        extends
            MField<UUID>,
            UEmpty
        /* permits
            Uuid */ 
    {}

    public /* sealed */ interface MDateAdd<T>
        extends
            MField<T>
        /* permits
            DateAdd */ 
    {
        @NotNull  MField<T> $date();
        @NotNull  MField<? extends Number> $interval();
        @Nullable DatePart $datePart();
        @NotNull  MDateAdd<T> $date(MField<T> date);
        @NotNull  MDateAdd<T> $interval(MField<? extends Number> interval);
        @NotNull  MDateAdd<T> $datePart(DatePart datePart);
    }

    public /* sealed */ interface MCardinality
        extends
            MField<Integer>
        /* permits
            Cardinality */ 
    {
        @NotNull  MField<? extends Object[]> $array();
        @NotNull  MCardinality $array(MField<? extends Object[]> array);
    }

    public /* sealed */ interface MArrayGet<T>
        extends
            MField<T>
        /* permits
            ArrayGet */ 
    {
        @NotNull  MField<T[]> $array();
        @NotNull  MField<Integer> $index();
        @NotNull  MArrayGet<T> $array(MField<T[]> array);
        @NotNull  MArrayGet<T> $index(MField<Integer> index);
    }

    public /* sealed */ interface MNvl<T>
        extends
            MField<T>
        /* permits
            Nvl */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MField<T> $defaultValue();
        @NotNull  MNvl<T> $value(MField<T> value);
        @NotNull  MNvl<T> $defaultValue(MField<T> defaultValue);
    }

    public /* sealed */ interface MNullif<T>
        extends
            MField<T>
        /* permits
            Nullif */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MField<T> $other();
        @NotNull  MNullif<T> $value(MField<T> value);
        @NotNull  MNullif<T> $other(MField<T> other);
    }

    public /* sealed */ interface MCurrentCatalog
        extends
            MField<String>,
            UEmpty
        /* permits
            CurrentCatalog */ 
    {}

    public /* sealed */ interface MCurrentSchema
        extends
            MField<String>,
            UEmpty
        /* permits
            CurrentSchema */ 
    {}

    public /* sealed */ interface MCurrentUser
        extends
            MField<String>,
            UEmpty
        /* permits
            CurrentUser */ 
    {}





















































































    public /* sealed */ interface MXmlcomment
        extends
            MField<XML>
        /* permits
            Xmlcomment */ 
    {
        @NotNull  MField<String> $comment();
        @NotNull  MXmlcomment $comment(MField<String> comment);
    }

    public /* sealed */ interface MXmlconcat
        extends
            MField<XML>
        /* permits
            Xmlconcat */ 
    {
        @NotNull  MList<? extends Field<?>> $args();
        @NotNull  MXmlconcat $args(MList<? extends Field<?>> args);
    }














    public /* sealed */ interface MXmlforest
        extends
            MField<XML>
        /* permits
            Xmlforest */ 
    {
        @NotNull  MList<? extends Field<?>> $fields();
        @NotNull  MXmlforest $fields(MList<? extends Field<?>> fields);
    }

    public /* sealed */ interface MXmlpi
        extends
            MField<XML>
        /* permits
            Xmlpi */ 
    {
        @NotNull  MName $target();
        @Nullable MField<?> $content();
        @NotNull  MXmlpi $target(MName target);
        @NotNull  MXmlpi $content(MField<?> content);
    }

    public /* sealed */ interface MXmlserialize<T>
        extends
            MField<T>
        /* permits
            Xmlserialize */ 
    {
                  boolean $content();
        @NotNull  MField<XML> $value();
        @NotNull  MDataType<T> $type();
        @NotNull  MXmlserialize<T> $content(boolean content);
        @NotNull  MXmlserialize<T> $value(MField<XML> value);
        @NotNull  MXmlserialize<T> $type(MDataType<T> type);
    }

    public /* sealed */ interface MJSONArray<T>
        extends
            MField<T>
        /* permits
            JSONArray */ 
    {
        @NotNull  MDataType<T> $type();
        @NotNull  MList<? extends Field<?>> $fields();
        @Nullable JSONOnNull $onNull();
        @Nullable MDataType<?> $returning();
        @NotNull  MJSONArray<T> $type(MDataType<T> type);
        @NotNull  MJSONArray<T> $fields(MList<? extends Field<?>> fields);
        @NotNull  MJSONArray<T> $onNull(JSONOnNull onNull);
        @NotNull  MJSONArray<T> $returning(MDataType<?> returning);
    }

    public /* sealed */ interface MJSONObject<T>
        extends
            MField<T>
        /* permits
            JSONObject */ 
    {
        @NotNull  MDataType<T> $type();
        @NotNull  MList<? extends JSONEntry<?>> $entries();
        @Nullable JSONOnNull $onNull();
        @Nullable MDataType<?> $returning();
        @NotNull  MJSONObject<T> $type(MDataType<T> type);
        @NotNull  MJSONObject<T> $entries(MList<? extends JSONEntry<?>> entries);
        @NotNull  MJSONObject<T> $onNull(JSONOnNull onNull);
        @NotNull  MJSONObject<T> $returning(MDataType<?> returning);
    }






























    public /* sealed */ interface MConditionAsField
        extends
            MField<Boolean>
        /* permits
            ConditionAsField */ 
    {
        @NotNull  MCondition $condition();
        @NotNull  MConditionAsField $condition(MCondition condition);
    }

    public /* sealed */ interface MFieldCondition
        extends
            MCondition
        /* permits
            FieldCondition */ 
    {
        @NotNull  MField<Boolean> $field();
        @NotNull  MFieldCondition $field(MField<Boolean> field);
    }

    public /* sealed */ interface MAnyValue<T>
        extends
            MAggregateFunction<T>
        /* permits
            AnyValue */ 
    {
        @NotNull  MField<T> $field();
        @NotNull  MAnyValue<T> $field(MField<T> field);
    }

    public /* sealed */ interface MAvg
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            Avg */ 
    {
        @NotNull  MField<? extends Number> $field();
                  boolean $distinct();
        @NotNull  MAvg $field(MField<? extends Number> field);
        @NotNull  MAvg $distinct(boolean distinct);
    }

    public /* sealed */ interface MBitAndAgg<T extends Number>
        extends
            MAggregateFunction<T>
        /* permits
            BitAndAgg */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MBitAndAgg<T> $value(MField<T> value);
    }

    public /* sealed */ interface MBitOrAgg<T extends Number>
        extends
            MAggregateFunction<T>
        /* permits
            BitOrAgg */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MBitOrAgg<T> $value(MField<T> value);
    }

    public /* sealed */ interface MBitXorAgg<T extends Number>
        extends
            MAggregateFunction<T>
        /* permits
            BitXorAgg */ 
    {
        @NotNull  MField<T> $value();
        @NotNull  MBitXorAgg<T> $value(MField<T> value);
    }

    public /* sealed */ interface MBoolAnd
        extends
            MAggregateFunction<Boolean>
        /* permits
            BoolAnd */ 
    {
        @NotNull  MCondition $condition();
        @NotNull  MBoolAnd $condition(MCondition condition);
    }

    public /* sealed */ interface MBoolOr
        extends
            MAggregateFunction<Boolean>
        /* permits
            BoolOr */ 
    {
        @NotNull  MCondition $condition();
        @NotNull  MBoolOr $condition(MCondition condition);
    }

    public /* sealed */ interface MCorr
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            Corr */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MCorr $y(MField<? extends Number> y);
        @NotNull  MCorr $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MCount
        extends
            MAggregateFunction<Integer>
        /* permits
            Count */ 
    {
        @NotNull  MField<?> $field();
                  boolean $distinct();
        @NotNull  MCount $field(MField<?> field);
        @NotNull  MCount $distinct(boolean distinct);
    }

    public /* sealed */ interface MCovarSamp
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            CovarSamp */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MCovarSamp $y(MField<? extends Number> y);
        @NotNull  MCovarSamp $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MCovarPop
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            CovarPop */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MCovarPop $y(MField<? extends Number> y);
        @NotNull  MCovarPop $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MMax<T>
        extends
            MAggregateFunction<T>
        /* permits
            Max */ 
    {
        @NotNull  MField<T> $field();
                  boolean $distinct();
        @NotNull  MMax<T> $field(MField<T> field);
        @NotNull  MMax<T> $distinct(boolean distinct);
    }

    public /* sealed */ interface MMedian
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            Median */ 
    {
        @NotNull  MField<? extends Number> $field();
        @NotNull  MMedian $field(MField<? extends Number> field);
    }

    public /* sealed */ interface MMin<T>
        extends
            MAggregateFunction<T>
        /* permits
            Min */ 
    {
        @NotNull  MField<T> $field();
                  boolean $distinct();
        @NotNull  MMin<T> $field(MField<T> field);
        @NotNull  MMin<T> $distinct(boolean distinct);
    }

    public /* sealed */ interface MProduct
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            Product */ 
    {
        @NotNull  MField<? extends Number> $field();
                  boolean $distinct();
        @NotNull  MProduct $field(MField<? extends Number> field);
        @NotNull  MProduct $distinct(boolean distinct);
    }

    public /* sealed */ interface MRegrAvgx
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrAvgx */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrAvgx $y(MField<? extends Number> y);
        @NotNull  MRegrAvgx $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MRegrAvgy
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrAvgy */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrAvgy $y(MField<? extends Number> y);
        @NotNull  MRegrAvgy $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MRegrCount
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrCount */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrCount $y(MField<? extends Number> y);
        @NotNull  MRegrCount $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MRegrIntercept
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrIntercept */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrIntercept $y(MField<? extends Number> y);
        @NotNull  MRegrIntercept $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MRegrR2
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrR2 */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrR2 $y(MField<? extends Number> y);
        @NotNull  MRegrR2 $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MRegrSlope
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrSlope */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrSlope $y(MField<? extends Number> y);
        @NotNull  MRegrSlope $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MRegrSxx
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrSxx */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrSxx $y(MField<? extends Number> y);
        @NotNull  MRegrSxx $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MRegrSxy
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrSxy */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrSxy $y(MField<? extends Number> y);
        @NotNull  MRegrSxy $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MRegrSyy
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            RegrSyy */ 
    {
        @NotNull  MField<? extends Number> $y();
        @NotNull  MField<? extends Number> $x();
        @NotNull  MRegrSyy $y(MField<? extends Number> y);
        @NotNull  MRegrSyy $x(MField<? extends Number> x);
    }

    public /* sealed */ interface MStddevPop
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            StddevPop */ 
    {
        @NotNull  MField<? extends Number> $field();
        @NotNull  MStddevPop $field(MField<? extends Number> field);
    }

    public /* sealed */ interface MStddevSamp
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            StddevSamp */ 
    {
        @NotNull  MField<? extends Number> $field();
        @NotNull  MStddevSamp $field(MField<? extends Number> field);
    }

    public /* sealed */ interface MSum
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            Sum */ 
    {
        @NotNull  MField<? extends Number> $field();
                  boolean $distinct();
        @NotNull  MSum $field(MField<? extends Number> field);
        @NotNull  MSum $distinct(boolean distinct);
    }

    public /* sealed */ interface MVarPop
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            VarPop */ 
    {
        @NotNull  MField<? extends Number> $field();
        @NotNull  MVarPop $field(MField<? extends Number> field);
    }

    public /* sealed */ interface MVarSamp
        extends
            MAggregateFunction<BigDecimal>
        /* permits
            VarSamp */ 
    {
        @NotNull  MField<? extends Number> $field();
        @NotNull  MVarSamp $field(MField<? extends Number> field);
    }




















































    /**
     * The <code>Cascade</code> type.
     * <p>
     * Cascade a DDL operation to all dependent objects, or restrict it to this object only.
     */
    public enum Cascade {
        CASCADE(keyword("cascade")),
        RESTRICT(keyword("restrict")),
        ;

        final Keyword keyword;

        private Cascade(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>CycleOption</code> type.
     * <p>
     * Specify whether a sequence cycles to its minvalue once it reaches its maxvalue.
     */
    public enum CycleOption {
        CYCLE(keyword("cycle")),
        NO_CYCLE(keyword("no cycle")),
        ;

        final Keyword keyword;

        private CycleOption(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>IdentityRestartOption</code> type.
     * <p>
     * Specify whether an identity column should be restarted upon truncation.
     */
    public enum IdentityRestartOption {
        CONTINUE_IDENTITY(keyword("continue identity")),
        RESTART_IDENTITY(keyword("restart identity")),
        ;

        final Keyword keyword;

        private IdentityRestartOption(Keyword keyword) {
            this.keyword = keyword;
        }
    }





























































    /**
     * The <code>NullOrdering</code> type.
     * <p>
     * The explicit ordering of NULL values in ORDER BY clauses. If unspecified, the behaviour
     * is implementation defined.
     */
    public enum NullOrdering {
        NULLS_FIRST(keyword("nulls first")),
        NULLS_LAST(keyword("nulls last")),
        ;

        final Keyword keyword;

        private NullOrdering(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>NullTreatment</code> type.
     * <p>
     * Specify whether to include NULL values or ignore NULL values in certain window functions.
     */
    public enum NullTreatment {
        RESPECT_NULLS(keyword("respect nulls")),
        IGNORE_NULLS(keyword("ignore nulls")),
        ;

        final Keyword keyword;

        private NullTreatment(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>FromFirstOrLast</code> type.
     * <p>
     * Specify whether the NTH_VALUE window function should count N values from the first
     * or last value in the window.
     */
    public enum FromFirstOrLast {
        FROM_FIRST(keyword("from first")),
        FROM_LAST(keyword("from last")),
        ;

        final Keyword keyword;

        private FromFirstOrLast(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>FrameUnits</code> type.
     * <p>
     * The window frame unit specification.
     */
    public enum FrameUnits {
        ROWS(keyword("rows")),
        RANGE(keyword("range")),
        GROUPS(keyword("groups")),
        ;

        final Keyword keyword;

        private FrameUnits(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>FrameExclude</code> type.
     * <p>
     * Specify which values within the window frame should be excluded.
     */
    public enum FrameExclude {
        CURRENT_ROW(keyword("current row")),
        TIES(keyword("ties")),
        GROUP(keyword("group")),
        NO_OTHERS(keyword("no others")),
        ;

        final Keyword keyword;

        private FrameExclude(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>JSONOnNull</code> type.
     * <p>
     * Specify whether a JSON array or object should include NULL values in the output.
     */
    public enum JSONOnNull {
        NULL_ON_NULL(keyword("null on null")),
        ABSENT_ON_NULL(keyword("absent on null")),
        ;

        final Keyword keyword;

        private JSONOnNull(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>XmlPassingMechanism</code> type.
     * <p>
     * Specify how XML contents should be passed to certain XML functions.
     */
    public enum XmlPassingMechanism {
        BY_REF(keyword("by ref")),
        BY_VALUE(keyword("by value")),
        DEFAULT(keyword("default")),
        ;

        final Keyword keyword;

        private XmlPassingMechanism(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>DocumentOrContent</code> type.
     * <p>
     * Specify whether XML content is a DOM document or a document fragment (content).
     */
    public enum DocumentOrContent {
        DOCUMENT(keyword("document")),
        CONTENT(keyword("content")),
        ;

        final Keyword keyword;

        private DocumentOrContent(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>Materialized</code> type.
     * <p>
     * Hint whether a CTE should be materialised or inlined. If unspecified, the optimiser
     * may produce implementation defined behaviour.
     */
    public enum Materialized {
        MATERIALIZED(keyword("materialized")),
        NOT_MATERIALIZED(keyword("not materialized")),
        ;

        final Keyword keyword;

        private Materialized(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>ResultOption</code> type.
     * <p>
     * The data change delta table result semantics.
     */
    public enum ResultOption {
        OLD(keyword("old")),
        NEW(keyword("new")),
        FINAL(keyword("final")),
        ;

        final Keyword keyword;

        private ResultOption(Keyword keyword) {
            this.keyword = keyword;
        }
    }



    // -------------------------------------------------------------------------
    // XXX: Utility API
    // -------------------------------------------------------------------------

    interface UOperator1<Q1, R extends MQueryPart> extends MQueryPart {
        Q1 $arg1();

        @NotNull default R $arg1(Q1 newArg1) { return constructor().apply(newArg1); }

        @NotNull
        Function1<? super Q1, ? extends R> constructor();

        default <T> T transform(Function1<? super Q1, ? extends T> function) {
            return function.apply($arg1());
        }

        @Override
        default <T> T traverse(
            T current,
            Predicate<? super T> abort,
            Predicate<? super MQueryPart> recurse,
            BiFunction<? super T, ? super MQueryPart, ? extends T> accumulate
        ) {
            return QOM.traverse(current, abort, recurse, accumulate, this, $arg1());
        };

        @NotNull
        @Override
        default MQueryPart replace(
            Predicate<? super MQueryPart> recurse,
            Function1<? super MQueryPart, ? extends MQueryPart> replacement
        ) {
            return QOM.replace(
                this,
                $arg1(),
                constructor(),
                recurse,
                replacement
            );
        }
    }

    interface UOperator2<Q1, Q2, R extends MQueryPart> extends MQueryPart {
        Q1 $arg1();
        Q2 $arg2();

        @NotNull default R $arg1(Q1 newArg1) { return constructor().apply(newArg1, $arg2()); }
        @NotNull default R $arg2(Q2 newArg2) { return constructor().apply($arg1(), newArg2); }

        @NotNull
        Function2<? super Q1, ? super Q2, ? extends R> constructor();

        default <T> T transform(Function2<? super Q1, ? super Q2, ? extends T> function) {
            return function.apply($arg1(), $arg2());
        }

        @Override
        default <T> T traverse(
            T current,
            Predicate<? super T> abort,
            Predicate<? super MQueryPart> recurse,
            BiFunction<? super T, ? super MQueryPart, ? extends T> accumulate
        ) {
            return QOM.traverse(current, abort, recurse, accumulate, this, $arg1(), $arg2());
        };

        @NotNull
        @Override
        default MQueryPart replace(
            Predicate<? super MQueryPart> recurse,
            Function1<? super MQueryPart, ? extends MQueryPart> replacement
        ) {
            return QOM.replace(
                this,
                $arg1(),
                $arg2(),
                constructor(),
                recurse,
                replacement
            );
        }
    }

    interface UOperator3<Q1, Q2, Q3, R extends MQueryPart> extends MQueryPart {
        Q1 $arg1();
        Q2 $arg2();
        Q3 $arg3();
        @NotNull default R $arg1(Q1 newArg1) { return constructor().apply(newArg1, $arg2(), $arg3()); }
        @NotNull default R $arg2(Q2 newArg2) { return constructor().apply($arg1(), newArg2, $arg3()); }
        @NotNull default R $arg3(Q3 newArg3) { return constructor().apply($arg1(), $arg2(), newArg3); }

        @NotNull
        Function3<? super Q1, ? super Q2, ? super Q3, ? extends R> constructor();

        default <T> T transform(Function3<? super Q1, ? super Q2, ? super Q3, ? extends T> function) {
            return function.apply($arg1(), $arg2(), $arg3());
        }

        @Override
        default <T> T traverse(
            T current,
            Predicate<? super T> abort,
            Predicate<? super MQueryPart> recurse,
            BiFunction<? super T, ? super MQueryPart, ? extends T> accumulate
        ) {
            return QOM.traverse(current, abort, recurse, accumulate, this, $arg1(), $arg2(), $arg3());
        };

        @NotNull
        @Override
        default MQueryPart replace(
            Predicate<? super MQueryPart> recurse,
            Function1<? super MQueryPart, ? extends MQueryPart> replacement
        ) {
            return QOM.replace(
                this,
                $arg1(),
                $arg2(),
                $arg3(),
                constructor(),
                recurse,
                replacement
            );
        }
    }

    /**
     * A marker interface for {@link QueryPart} implementations that are used
     * only to render SQL, i.e. they're transient to the expression tree and
     * don't persist in client code.
     */
    interface UTransient extends UEmpty {}

    /**
     * A marker interface for {@link QueryPart} implementations whose
     * {@link MQueryPart} semantics has not yet been implemented.
     *
     * @deprecated - [#12425] - 3.16.0 - Missing implementations should be added
     *             as soon as possible!
     */
    @Deprecated(forRemoval = true)
    interface UNotYetImplemented extends UEmpty {}

    /**
     * A marker interface for {@link MQueryPart} methods that have not yet been
     * implemented.
     *
     * @deprecated - [#12425] - 3.16.0 - Missing implementations should be added
     *             as soon as possible!
     */
    @Deprecated(forRemoval = true)
    static class UNotYetImplementedException extends RuntimeException {}

    interface UProxy<Q extends MQueryPart> extends MQueryPart {
        Q $delegate();

        @Override
        default <R> R traverse(
            R init,
            Predicate<? super R> abort,
            Predicate<? super MQueryPart> recurse,
            BiFunction<? super R, ? super MQueryPart, ? extends R> accumulate
        ) {
            return $delegate().traverse(init, abort, recurse, accumulate);
        }

        @Override
        default MQueryPart replace(
            Predicate<? super MQueryPart> recurse,
            Function1<? super MQueryPart, ? extends MQueryPart> replacement
        ) {
            MQueryPart r = $delegate().replace(recurse, replacement);
            return $delegate() == r ? this : r;
        }

        @Override
        default boolean $contains(MQueryPart part) {
            return $delegate().$contains(part);
        }
    }

    interface UEmpty extends MQueryPart {

        @Override
        default <R> R traverse(
            R init,
            Predicate<? super R> abort,
            Predicate<? super MQueryPart> recurse,
            BiFunction<? super R, ? super MQueryPart, ? extends R> accumulate
        ) {
            return QOM.traverse(init, abort, recurse, accumulate, this);
        }

        @Override
        default MQueryPart replace(
            Predicate<? super MQueryPart> recurse,
            Function1<? super MQueryPart, ? extends MQueryPart> replacement
        ) {
            if (recurse.test(this))
                return replacement.apply(this);
            else
                return this;
        }

        @Override
        default boolean $contains(MQueryPart part) {
            return equals(part);
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Undisclosed, internal query parts
    // -------------------------------------------------------------------------

    interface UEmptyCondition extends MCondition, UEmpty {}
    interface UEmptyField<T> extends MField<T>, UEmpty {}
    interface UEmptyTable<R extends Record> extends MTable<R>, UEmpty {}
    interface UEmptyStatement extends MStatement, UEmpty {}
    interface UEmptyQuery extends MQuery, UEmpty {}

    // -------------------------------------------------------------------------
    // XXX: Utilities
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    static final <Q> Q replace(
        Q q,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {

        // TODO: Support also arrays, sets, etc.
        if (q instanceof List) {
            List<?> l = (List<?>) q;
            List<Object> r = null;

            for (int i = 0; i < l.size(); i++) {
                Object o = l.get(i);
                Object x = replace(o, recurse, replacement);

                if (o != x) {

                    // TODO: What about other lists, e.g. QueryPartList?
                    if (r == null) {
                        r = Reflect.onClass(q.getClass()).create().get();
                        r.addAll(l.subList(0, i));
                    }

                    r.add(x);
                }
                else if (r != null)
                    r.add(o);
            }

            return r != null ? (Q) r : q;
        }


        return q instanceof MQueryPart && recurse.test((MQueryPart) q)
             ? (Q) ((MQueryPart) q).replace(recurse, replacement)
             : q;
    }

    static final <T> boolean test(Predicate<? super T> predicate, T value) {
        return predicate != null && predicate.test(value);
    }

    static final <T> T traverse(
        T current,
        Predicate<? super T> abort,
        Predicate<? super MQueryPart> recurse,
        BiFunction<? super T, ? super MQueryPart, ? extends T> accumulate,
        Object part,
        Object... parts
    ) {
        if (test(abort, current)) return current;
        if (part instanceof MQueryPart)
            current = accumulate.apply(current, (MQueryPart) part);
        if (test(abort, current)) return current;

        for (int i = 0; i < parts.length; i++) {
            if (parts[i] instanceof MQueryPart) {
                MQueryPart p = (MQueryPart) parts[i];

                if (test(recurse, p)) {
                    current = p.traverse(current, abort, recurse, accumulate);
                    if (test(abort, current)) return current;
                }
            }
        }

        return current;
    };

    @SuppressWarnings("unchecked")
    static final <QR extends MQueryPart, Q> QR replace(
        QR wrapper,
        Q[] q,
        Function1<? super Q[], ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q[] r = (Q[]) Array.newInstance(q.getClass().getComponentType(), q.length);

        for (int i = 0; i < r.length; i++)
            r[i] = replace(q[i], recurse, replacement);

        wrapIfReplaced: {
            for (int i = 0; i < r.length; i++) {
                if (r[i] != q[i]) {
                    wrapper = wrap.apply(r);
                    break wrapIfReplaced;
                }
            }
        }

        return replaceUntilStable(wrapper, recurse, replacement);
    }



    static final <QR extends MQueryPart, Q1> QR replace(
        QR wrapper,
        Q1 q1,
        Function1<? super Q1, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);

        if (r1 != q1)
            wrapper = wrap.apply(r1);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Function2<? super Q1, ? super Q2, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);

        if (r1 != q1 || r2 != q2)
            wrapper = wrap.apply(r1, r2);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Function3<? super Q1, ? super Q2, ? super Q3, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3)
            wrapper = wrap.apply(r1, r2, r3);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Function4<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4)
            wrapper = wrap.apply(r1, r2, r3, r4);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Function5<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5)
            wrapper = wrap.apply(r1, r2, r3, r4, r5);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Function6<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Function7<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Function8<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Function9<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Function10<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Function11<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Function12<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Function13<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Function14<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Q15 q15,
        Function15<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? super Q15, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);
        Q15 r15 = replace(q15, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14 || r15 != q15)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Q15 q15,
        Q16 q16,
        Function16<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? super Q15, ? super Q16, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);
        Q15 r15 = replace(q15, recurse, replacement);
        Q16 r16 = replace(q16, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14 || r15 != q15 || r16 != q16)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Q15 q15,
        Q16 q16,
        Q17 q17,
        Function17<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? super Q15, ? super Q16, ? super Q17, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);
        Q15 r15 = replace(q15, recurse, replacement);
        Q16 r16 = replace(q16, recurse, replacement);
        Q17 r17 = replace(q17, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14 || r15 != q15 || r16 != q16 || r17 != q17)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Q15 q15,
        Q16 q16,
        Q17 q17,
        Q18 q18,
        Function18<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? super Q15, ? super Q16, ? super Q17, ? super Q18, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);
        Q15 r15 = replace(q15, recurse, replacement);
        Q16 r16 = replace(q16, recurse, replacement);
        Q17 r17 = replace(q17, recurse, replacement);
        Q18 r18 = replace(q18, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14 || r15 != q15 || r16 != q16 || r17 != q17 || r18 != q18)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18, Q19> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Q15 q15,
        Q16 q16,
        Q17 q17,
        Q18 q18,
        Q19 q19,
        Function19<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? super Q15, ? super Q16, ? super Q17, ? super Q18, ? super Q19, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);
        Q15 r15 = replace(q15, recurse, replacement);
        Q16 r16 = replace(q16, recurse, replacement);
        Q17 r17 = replace(q17, recurse, replacement);
        Q18 r18 = replace(q18, recurse, replacement);
        Q19 r19 = replace(q19, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14 || r15 != q15 || r16 != q16 || r17 != q17 || r18 != q18 || r19 != q19)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18, Q19, Q20> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Q15 q15,
        Q16 q16,
        Q17 q17,
        Q18 q18,
        Q19 q19,
        Q20 q20,
        Function20<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? super Q15, ? super Q16, ? super Q17, ? super Q18, ? super Q19, ? super Q20, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);
        Q15 r15 = replace(q15, recurse, replacement);
        Q16 r16 = replace(q16, recurse, replacement);
        Q17 r17 = replace(q17, recurse, replacement);
        Q18 r18 = replace(q18, recurse, replacement);
        Q19 r19 = replace(q19, recurse, replacement);
        Q20 r20 = replace(q20, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14 || r15 != q15 || r16 != q16 || r17 != q17 || r18 != q18 || r19 != q19 || r20 != q20)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18, Q19, Q20, Q21> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Q15 q15,
        Q16 q16,
        Q17 q17,
        Q18 q18,
        Q19 q19,
        Q20 q20,
        Q21 q21,
        Function21<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? super Q15, ? super Q16, ? super Q17, ? super Q18, ? super Q19, ? super Q20, ? super Q21, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);
        Q15 r15 = replace(q15, recurse, replacement);
        Q16 r16 = replace(q16, recurse, replacement);
        Q17 r17 = replace(q17, recurse, replacement);
        Q18 r18 = replace(q18, recurse, replacement);
        Q19 r19 = replace(q19, recurse, replacement);
        Q20 r20 = replace(q20, recurse, replacement);
        Q21 r21 = replace(q21, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14 || r15 != q15 || r16 != q16 || r17 != q17 || r18 != q18 || r19 != q19 || r20 != q20 || r21 != q21)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends MQueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18, Q19, Q20, Q21, Q22> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Q8 q8,
        Q9 q9,
        Q10 q10,
        Q11 q11,
        Q12 q12,
        Q13 q13,
        Q14 q14,
        Q15 q15,
        Q16 q16,
        Q17 q17,
        Q18 q18,
        Q19 q19,
        Q20 q20,
        Q21 q21,
        Q22 q22,
        Function22<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? super Q8, ? super Q9, ? super Q10, ? super Q11, ? super Q12, ? super Q13, ? super Q14, ? super Q15, ? super Q16, ? super Q17, ? super Q18, ? super Q19, ? super Q20, ? super Q21, ? super Q22, ? extends QR> wrap,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);
        Q5 r5 = replace(q5, recurse, replacement);
        Q6 r6 = replace(q6, recurse, replacement);
        Q7 r7 = replace(q7, recurse, replacement);
        Q8 r8 = replace(q8, recurse, replacement);
        Q9 r9 = replace(q9, recurse, replacement);
        Q10 r10 = replace(q10, recurse, replacement);
        Q11 r11 = replace(q11, recurse, replacement);
        Q12 r12 = replace(q12, recurse, replacement);
        Q13 r13 = replace(q13, recurse, replacement);
        Q14 r14 = replace(q14, recurse, replacement);
        Q15 r15 = replace(q15, recurse, replacement);
        Q16 r16 = replace(q16, recurse, replacement);
        Q17 r17 = replace(q17, recurse, replacement);
        Q18 r18 = replace(q18, recurse, replacement);
        Q19 r19 = replace(q19, recurse, replacement);
        Q20 r20 = replace(q20, recurse, replacement);
        Q21 r21 = replace(q21, recurse, replacement);
        Q22 r22 = replace(q22, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4 || r5 != q5 || r6 != q6 || r7 != q7 || r8 != q8 || r9 != q9 || r10 != q10 || r11 != q11 || r12 != q12 || r13 != q13 || r14 != q14 || r15 != q15 || r16 != q16 || r17 != q17 || r18 != q18 || r19 != q19 || r20 != q20 || r21 != q21 || r22 != q22)
            wrapper = wrap.apply(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22);

        return replaceUntilStable(wrapper, recurse, replacement);
    }



    private static <QR extends MQueryPart> QR replaceUntilStable(
        QR wrapper,
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        QR q = wrapper;
        QR r = wrapper;

        do {
            if (recurse.test(q))
                r = (QR) replacement.apply(q);
        }
        while (r != null && r != q && (q = (QR) r.replace(recurse, replacement)) != null);
        return r;
    }
}
