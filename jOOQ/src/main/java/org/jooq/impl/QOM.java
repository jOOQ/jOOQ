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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static org.jooq.impl.DSL.keyword;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collector;

// ...
import org.jooq.Catalog;
import org.jooq.CheckReturnValue;
import org.jooq.Collation;
import org.jooq.Comment;
import org.jooq.CommonTableExpression;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Constraint;
import org.jooq.DDLQuery;
import org.jooq.DMLQuery;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Domain;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
import org.jooq.Function0;
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
import org.jooq.Geometry;
import org.jooq.GroupField;
import org.jooq.Index;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.JSONEntry;
import org.jooq.Keyword;
import org.jooq.Lambda1;
// ...
import org.jooq.Name;
import org.jooq.Operator;
import org.jooq.OptionallyOrderedAggregateFunction;
import org.jooq.OrderField;
import org.jooq.OrderedAggregateFunction;
import org.jooq.Param;
import org.jooq.Parameter;
import org.jooq.Privilege;
// ...
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
// ...
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Role;
import org.jooq.Row;
import org.jooq.RowCountQuery;
import org.jooq.RowId;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.Sequence;
import org.jooq.SortField;
import org.jooq.Spatial;
import org.jooq.Statement;
// ...
import org.jooq.Table;
import org.jooq.TableElement;
import org.jooq.TableLike;
// ...
// ...
import org.jooq.Type;
import org.jooq.UDT;
import org.jooq.UDTRecord;
// ...
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;
import org.jooq.XML;
import org.jooq.XMLAttributes;
import org.jooq.conf.Settings;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.types.DayToSecond;
// ...

import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
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
 * Every {@link QueryPart} from the DSL API has a matching {@link QueryPart}
 * representation in this query object model API, and a shared internal
 * implementation in the <code>org.jooq.impl</code> package, that covers both
 * the DSL and model API functionality.
 * <p>
 * The goal of this model API is to allow for expression tree transformations
 * via {@link QueryPart#$replace(Replacer)} as well as via per-querypart
 * methods, such as for example {@link Substring#$startingPosition(Field)}, and
 * traversals via {@link QueryPart#$traverse(Traverser)} that are independent of
 * the DSL API that would otherwise be too noisy for this task.
 * <p>
 * <h3>Design</h3>
 * <p>
 * In order to avoid conflicts between the model API and the DSL API, all model
 * API in this class follows these naming conventions:
 * <ul>
 * <li>All public model API types are nested in the {@link QOM} class, whereas
 * DSL API types are top level types in the <code>org.jooq</code> package.</li>
 * <li>All accessor methods and their corresponding "immutable setters"
 * (returning a copy containing the modification) are named
 * <code>$property()</code>, e.g. {@link Substring#$startingPosition()} and
 * {@link Substring#$startingPosition(Field)}.</li>
 * <li>All private model API utility types are named <code>UXyz</code>, e.g.
 * {@link UEmpty}</li>
 * </ul>
 * <p>
 * <h3>Limitations</h3>
 * <p>
 * The API offers public access to jOOQ's internal representation, and as such,
 * is prone to incompatible changes between minor releases, in addition to the
 * incompatible changes that may arise due to this API being experimental. In
 * this experimental stage, the following limitations are accepted:
 * <ul>
 * <li>Not all {@link QueryPart} implementations have a corresponding public
 * {@link QueryPart} type yet, but may just implement the API via a
 * {@link UEmpty} or {@link UNotYetImplemented} subtype, and may not provide
 * access to contents via accessor methods.</li>
 * <li>Some child elements of a {@link QueryPart} may not yet be represented in
 * the model API, such as for example the <code>SELECT … FOR UPDATE</code>
 * clause, as substantial changes to the internal model are still required
 * before being able to offer public access to it.</li>
 * </ul>
 * <p>
 * <h3>Mutability</h3>
 * <p>
 * While some elements of this API are historically mutable (either mutable
 * objects are returned from {@link QueryPart} subtypes, or argument objects
 * when constructing an {@link QueryPart} remains mutable, rather than copied),
 * users must not rely on this mutable behaviour. Once this API stabilises, all
 * mutability will be gone, accidental remaining mutability will be considered a
 * bug.
 * <p>
 * <h3>Notes</h3>
 * <p>
 * A future Java 17 distribution of jOOQ might make use of sealed types to
 * improve the usability of the model API in pattern matching expressions etc.
 * Other Java language features that benefit pattern matching expression trees
 * might be adopted in the future in this area of the jOOQ API.
 *
 * @author Lukas Eder
 */
@Experimental
public final class QOM {

    // -------------------------------------------------------------------------
    // XXX: Model
    // -------------------------------------------------------------------------

//    public interface Lambda1<T1, R>
//        extends
//            org.jooq.QueryPart
//    {
//
//        /**
//         * The first argument of the lambda.
//         */
//        @NotNull Field<T1> $arg1();
//
//        /**
//         * The lambda result.
//         */
//        @NotNull Field<R> $result();
//    }

    // This class uses a lot of fully qualified types, because of some javac bug
    // In Java 1.8.0_302, which hasn't been analysed and reproduced yet in a more
    // minimal example. Without the qualification, the types cannot be found
    // despite being imported

    /**
     * A generic tuple of degree 2 for use in {@link QOM} types.
     */
    public interface Tuple2<Q1 extends org.jooq.QueryPart, Q2 extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart
    {

        /**
         * The first value in the tuple.
         */
        @NotNull Q1 $1();

        /**
         * The second value in the tuple.
         */
        @NotNull Q2 $2();

        /**
         * Set the first value in the tuple.
         */
        @CheckReturnValue
        @NotNull Tuple2<Q1, Q2> $1(Q1 newPart1);

        /**
         * Set the second value in the tuple.
         */
        @CheckReturnValue
        @NotNull Tuple2<Q1, Q2> $2(Q2 newPart2);
    }

    /**
     * An unmodifiable {@link Map} of {@link QueryPart} keys and values.
     */
    public interface UnmodifiableMap<K extends org.jooq.QueryPart, V extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart,
            java.util.Map<K, V>
    {

        /**
         * Get the {@link #entrySet()} of this map as a list of tuples.
         */
        @NotNull UnmodifiableList<Tuple2<K, V>> $tuples();
    }

    /**
     * An unmodifiable {@link Collection} of {@link QueryPart} elements.
     */
    public interface UnmodifiableCollection<Q extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart,
            java.util.Collection<Q>
    {}

    /**
     * An unmodifiable {@link List} of {@link QueryPart} elements.
     */
    public interface UnmodifiableList<Q extends org.jooq.QueryPart>
        extends
            UnmodifiableCollection<Q>,
            java.util.List<Q>
    {

        // TODO: These methods could return unmodifiable views instead, to avoid
        //       copying things around...

        /**
         * Collect the contents of this list using a {@link Collector}.
         */
        default <R> R $collect(Collector<Q, ?, R> collector) {
            return stream().collect(collector);
        }

        /**
         * Concatenate a collection to this UnmodifiableList, returning a new
         * UnmodifiableList from the combined data.
         */
        @NotNull
        @CheckReturnValue
        default UnmodifiableList<Q> $concat(Collection<? extends Q> other) {
            QueryPartList<Q> r = new QueryPartList<>(this);
            r.addAll(other);
            return unmodifiable(r);
        }

        /**
         * Return a new UnmodifiableList without the element at the argument
         * position.
         */
        @NotNull
        @CheckReturnValue
        default UnmodifiableList<Q> $remove(int position) {
            QueryPartList<Q> r = new QueryPartList<>();

            for (int i = 0; i < size(); i++)
                if (i != position)
                    r.add(get(i));

            return unmodifiable(r);
        }

        /**
         * Access the first element if available.
         */
        @Nullable
        default Q $first() {
            return isEmpty() ? null : get(0);
        }

        /**
         * Access the last element if available.
         */
        @Nullable
        default Q $last() {
            return isEmpty() ? null : get(size() - 1);
        }

        /**
         * Return a new UnmodifiableList without the {@link #$first()} element.
         */
        @NotNull
        @CheckReturnValue
        default UnmodifiableList<Q> $removeFirst() {
            QueryPartList<Q> r = new QueryPartList<>();

            for (int i = 1; i < size(); i++)
                r.add(get(i));

            return unmodifiable(r);
        }

        /**
         * Return a new {@link UnmodifiableList} without the {@link #$last()}
         * element.
         */
        @NotNull
        @CheckReturnValue
        default UnmodifiableList<Q> $removeLast() {
            QueryPartList<Q> r = new QueryPartList<>();

            for (int i = 0; i < size() - 1; i++)
                r.add(get(i));

            return unmodifiable(r);
        }

        /**
         * Return a new {@link UnmodifiableList} with a new, replaced value at
         * the argument position.
         */
        @NotNull
        @CheckReturnValue
        default UnmodifiableList<Q> $set(int position, Q newValue) {
            QueryPartList<Q> r = new QueryPartList<>();

            for (int i = 0; i < size(); i++)
                if (i == position)
                    r.add(newValue);
                else
                    r.add(get(i));

            return unmodifiable(r);
        }

        /**
         * Return a new {@link UnmodifiableList} with a new, replaced set of
         * values at the argument position.
         */
        @NotNull
        @CheckReturnValue
        default UnmodifiableList<Q> $setAll(int position, Collection<? extends Q> newValues) {
            QueryPartList<Q> r = new QueryPartList<>();

            for (int i = 0; i < size(); i++)
                if (i == position)
                    r.addAll(newValues);
                else
                    r.add(get(i));

            return unmodifiable(r);
        }
    }

    /**
     * A <code>WITH</code> clause of a {@link Select}, {@link Insert},
     * {@link Update}, or {@link Delete} statement.
     */
    public interface With
        extends
            org.jooq.QueryPart
    {
        @NotNull UnmodifiableList<? extends CommonTableExpression<?>> $commonTableExpressions();
        @CheckReturnValue
        @NotNull
        With $commonTableExpressions(UnmodifiableList<? extends CommonTableExpression<?>> commonTableExpressions);
        boolean $recursive();
        @CheckReturnValue
        @NotNull
        With $recursive(boolean recursive);
    }

    /**
     * A {@link QueryPart} that may associate an {@link #$alias()} with the
     * {@link #$aliased()} part.
     */
    public interface Aliasable<Q extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart
    {

        /**
         * The aliased part (a {@link Field} or a {@link Table}).
         */
        @NotNull Q $aliased();

        /**
         * The alias if any.
         */
        @Nullable Name $alias();
    }

    // -------------------------------------------------------------------------
    // XXX: Queries
    // -------------------------------------------------------------------------

    /**
     * The <code>INSERT</code> statement.
     */
    public interface Insert<R extends org.jooq.Record>
        extends
            org.jooq.DMLQuery<R>
    {
        @Nullable With $with();
        @NotNull  Table<R> $into();
        @CheckReturnValue
        @NotNull  Insert<?> $into(Table<?> into);
        @NotNull  UnmodifiableList<? extends Field<?>> $columns();
        @CheckReturnValue
        @NotNull  Insert<?> $columns(Collection<? extends Field<?>> columns);
        @Nullable Select<?> $select();
        @CheckReturnValue
        @NotNull  Insert<?> $select(Select<?> select);
                  boolean $defaultValues();
        @CheckReturnValue
        @NotNull  Insert<?> $defaultValues(boolean defaultValues);
        @NotNull  UnmodifiableList<? extends Row> $values();
        @CheckReturnValue
        @NotNull  Insert<?> $values(Collection<? extends Row> values);
                  boolean $onDuplicateKeyIgnore();
        @CheckReturnValue
        @NotNull  Insert<?> $onDuplicateKeyIgnore(boolean onDuplicateKeyIgnore);
                  boolean $onDuplicateKeyUpdate();
        @CheckReturnValue
        @NotNull  Insert<?> $onDuplicateKeyUpdate(boolean onDuplicateKeyUpdate);
        @NotNull  UnmodifiableList<? extends Field<?>> $onConflict();
        @CheckReturnValue
        @NotNull  Insert<?> $onConflict(Collection<? extends Field<?>> onConflictFields);
        // [#13640] TODO: What to do about the CONSTRAINT? Re-design this model?
        @Nullable Condition $onConflictWhere();
        @CheckReturnValue
        @NotNull  Insert<?> $onConflictWhere(Condition where);
        @NotNull  UnmodifiableMap<? extends FieldOrRow, ? extends FieldOrRowOrSelect> $updateSet();
        @CheckReturnValue
        @NotNull  Insert<?> $updateSet(Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> updateSet);
        @Nullable Condition $updateWhere();
        @CheckReturnValue
        @NotNull  Insert<?> $updateWhere(Condition where);
    }

    /**
     * An <code>INSERT</code> statement with a <code>RETURNING</code> clause.
     */
    public interface InsertReturning<R extends org.jooq.Record>
        extends
            org.jooq.ResultQuery<R>
    {
        @NotNull Insert<?> $insert();
        @CheckReturnValue
        @NotNull InsertReturning<R> $insert(Insert<?> insert);
        @NotNull UnmodifiableList<? extends SelectFieldOrAsterisk> $returning();
        @CheckReturnValue
        @NotNull InsertReturning<?> $returning(Collection<? extends SelectFieldOrAsterisk> returning);
    }

    /**
     * The <code>UPDATE</code> statement.
     */
    public interface Update<R extends org.jooq.Record>
        extends
            org.jooq.DMLQuery<R>
    {
        @Nullable With $with();
        @NotNull  Table<R> $table();
        @CheckReturnValue
        @NotNull  Update<?> $table(Table<?> table);
        @NotNull  UnmodifiableList<? extends Table<?>> $from();
        @CheckReturnValue
        @NotNull  Update<R> $from(Collection<? extends Table<?>> from);
        @NotNull  UnmodifiableMap<? extends FieldOrRow, ? extends FieldOrRowOrSelect> $set();
        @CheckReturnValue
        @NotNull  Update<R> $set(Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> set);
        @Nullable Condition $where();
        @CheckReturnValue
        @NotNull  Update<R> $where(Condition condition);
        @NotNull  UnmodifiableList<? extends SortField<?>> $orderBy();
        @CheckReturnValue
        @NotNull  Update<R> $orderBy(Collection<? extends SortField<?>> orderBy);
        @Nullable Field<? extends Number> $limit();
        @CheckReturnValue
        @NotNull  Update<R> $limit(Field<? extends Number> limit);
    }

    /**
     * An <code>UPDATE</code> statement with a <code>RETURNING</code> clause.
     */
    public interface UpdateReturning<R extends org.jooq.Record>
        extends
            org.jooq.ResultQuery<R>
    {
        @NotNull Update<?> $update();
        @CheckReturnValue
        @NotNull UpdateReturning<R> $update(Update<?> update);
        @NotNull UnmodifiableList<? extends SelectFieldOrAsterisk> $returning();
        @CheckReturnValue
        @NotNull UpdateReturning<?> $returning(Collection<? extends SelectFieldOrAsterisk> returning);
    }

    /**
     * The <code>DELETE</code> statement.
     */
    public interface Delete<R extends org.jooq.Record>
        extends
            org.jooq.DMLQuery<R>
    {
        @Nullable With $with();
        @NotNull  Table<R> $from();
        @CheckReturnValue
        @NotNull  Delete<?> $from(Table<?> table);
        @NotNull  UnmodifiableList<? extends Table<?>> $using();
        @CheckReturnValue
        @NotNull  Delete<R> $using(Collection<? extends Table<?>> using);
        @Nullable Condition $where();
        @CheckReturnValue
        @NotNull  Delete<R> $where(Condition condition);
        @NotNull  UnmodifiableList<? extends SortField<?>> $orderBy();
        @CheckReturnValue
        @NotNull  Delete<R> $orderBy(Collection<? extends SortField<?>> orderBy);
        @Nullable Field<? extends Number> $limit();
        @CheckReturnValue
        @NotNull  Delete<R> $limit(Field<? extends Number> limit);
    }

    /**
     * An <code>DELETE</code> statement with a <code>RETURNING</code> clause.
     */
    public interface DeleteReturning<R extends org.jooq.Record>
        extends
            org.jooq.ResultQuery<R>
    {
        @NotNull Delete<?> $delete();
        @CheckReturnValue
        @NotNull DeleteReturning<R> $delete(Delete<?> delete);
        @NotNull UnmodifiableList<? extends SelectFieldOrAsterisk> $returning();
        @CheckReturnValue
        @NotNull DeleteReturning<?> $returning(Collection<? extends SelectFieldOrAsterisk> returning);
    }

    public interface Merge<R extends org.jooq.Record>
        extends
            org.jooq.DMLQuery<R>
    {
        @Nullable With $with();
        @NotNull  Table<R> $into();
        @CheckReturnValue
        @NotNull  Merge<?> $into(Table<?> into);
        @Nullable  TableLike<?> $using();
        @CheckReturnValue
        @NotNull  Merge<R> $using(TableLike<?> into);
        @Nullable Condition $on();
        @CheckReturnValue
        @NotNull  Merge<R> $on(Condition condition);
        @NotNull  UnmodifiableList<? extends MergeMatched> $whenMatched();
        @CheckReturnValue
        @NotNull  Merge<R> $whenMatched(Collection<? extends MergeMatched> whenMatched);
        @NotNull  UnmodifiableList<? extends MergeNotMatched> $whenNotMatched();
        @CheckReturnValue
        @NotNull  Merge<R> $whenNotMatched(Collection<? extends MergeNotMatched> whenNotMatched);
        @NotNull  UnmodifiableList<? extends MergeNotMatchedBySource> $whenNotMatchedBySource();
        @CheckReturnValue
        @NotNull  Merge<R> $whenNotMatchedBySource(Collection<? extends MergeNotMatchedBySource> whenNotMatchedBySource);
    }

    public interface MergeMatched
        extends
            org.jooq.QueryPart
    {
        @NotNull  UnmodifiableMap<? extends FieldOrRow, ? extends FieldOrRowOrSelect> $updateSet();
        @CheckReturnValue
        @NotNull  MergeMatched $updateSet(Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> updateSet);
                  boolean $delete();
        @CheckReturnValue
        @NotNull  MergeMatched $delete(boolean delete);
        @Nullable Condition $where();
        @CheckReturnValue
        @NotNull  MergeMatched $where(Condition condition);
    }

    public interface MergeNotMatched
        extends
            org.jooq.QueryPart
    {
        @NotNull  UnmodifiableList<? extends Field<?>> $columns();
        @CheckReturnValue
        @NotNull  MergeNotMatched $columns(Collection<? extends Field<?>> columns);
        @NotNull  UnmodifiableList<? extends Row> $values();
        @CheckReturnValue
        @NotNull  MergeNotMatched $values(Collection<? extends Row> values);
        @Nullable Condition $where();
        @CheckReturnValue
        @NotNull  MergeNotMatched $where(Condition condition);
    }

    public interface MergeNotMatchedBySource
        extends
            org.jooq.QueryPart
    {
        @NotNull  UnmodifiableMap<? extends FieldOrRow, ? extends FieldOrRowOrSelect> $updateSet();
        @CheckReturnValue
        @NotNull  MergeMatched $updateSet(Map<? extends FieldOrRow, ? extends FieldOrRowOrSelect> updateSet);
                  boolean $delete();
        @CheckReturnValue
        @NotNull  MergeMatched $delete(boolean delete);
        @Nullable Condition $where();
        @CheckReturnValue
        @NotNull  MergeMatched $where(Condition condition);
    }

    // -------------------------------------------------------------------------
    // XXX: Schema
    // -------------------------------------------------------------------------


































    /**
     * A table with a MySQL style index access hint.
     */
    public interface HintedTable<R extends org.jooq.Record>
        extends
            org.jooq.Table<R>
    {
        @NotNull Table<R> $table();
        @CheckReturnValue
        @NotNull <O extends Record> HintedTable<O> $table(Table<O> newTable);
    }

    /**
     * A collection derived table or table valued function with a
     * <code>WITH ORDINALITY</code> clause.
     */
    public interface WithOrdinalityTable<R extends Record>
        extends
            Table<R>
    {
        @NotNull Table<?> $table();
        @CheckReturnValue
        @NotNull WithOrdinalityTable<?> $table(Table<?> newTable);
    }

    /**
     * A <code>PRIMARY KEY</code> constraint.
     */
    public interface PrimaryKey
        extends
            org.jooq.Constraint
    {
        @Override
        @NotNull Name $name();
        @CheckReturnValue
        @NotNull Constraint $name(Name newName);
        boolean $enforced();
        @CheckReturnValue
        @NotNull Constraint $enforced(boolean newEnforced);
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @CheckReturnValue
        @NotNull PrimaryKey $fields(UnmodifiableList<? extends Field<?>> newFields);
    }

    /**
     * A <code>UNIQUE</code> constraint.
     */
    public interface UniqueKey
        extends
            org.jooq.Constraint
    {
        @Override
        @NotNull Name $name();
        @CheckReturnValue
        @NotNull Constraint $name(Name newName);
        boolean $enforced();
        @CheckReturnValue
        @NotNull Constraint $enforced(boolean newEnforced);
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @CheckReturnValue
        @NotNull UniqueKey $fields(UnmodifiableList<? extends Field<?>> newFields);
    }

    /**
     * A <code>FOREIGN KEY</code> constraint.
     */
    public interface ForeignKey
        extends
            org.jooq.Constraint
    {
        @Override
        @NotNull Name $name();
        @CheckReturnValue
        @NotNull Constraint $name(Name newName);
        boolean $enforced();
        @CheckReturnValue
        @NotNull Constraint $enforced(boolean newEnforced);
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @CheckReturnValue
        @NotNull ForeignKey $fields(UnmodifiableList<? extends Field<?>> newFields);
        @NotNull Table<?> $referencesTable();
        @CheckReturnValue
        @NotNull ForeignKey $referencesTable(Table<?> newReferencesTable);
        @NotNull UnmodifiableList<? extends Field<?>> $referencesFields();
        @CheckReturnValue
        @NotNull ForeignKey $referencesFields(UnmodifiableList<? extends Field<?>> newReferencesFields);
        @Nullable ForeignKeyRule $deleteRule();
        @CheckReturnValue
        @NotNull ForeignKey $deleteRule(ForeignKeyRule newDeleteRule);
        @Nullable ForeignKeyRule $updateRule();
        @CheckReturnValue
        @NotNull ForeignKey $updateRule(ForeignKeyRule newDeleteRule);
    }

    /**
     * A <code>CHECK</code> constraint.
     */
    public interface Check
        extends
            org.jooq.Constraint
    {
        @Override
        @NotNull Name $name();
        @CheckReturnValue
        @NotNull Constraint $name(Name newName);
        boolean $enforced();
        @CheckReturnValue
        @NotNull Constraint $enforced(boolean newEnforced);
        @NotNull Condition $condition();
        @CheckReturnValue
        @NotNull Check $condition(Condition newCondition);
    }

    // -------------------------------------------------------------------------
    // XXX: Statements
    // -------------------------------------------------------------------------

    public interface NullStatement extends Statement {}



















































    // -------------------------------------------------------------------------
    // XXX: Tables
    // -------------------------------------------------------------------------

    public interface TableAlias<R extends Record>
        extends
            Table<R>,
            Aliasable<Table<R>>
    {
        @NotNull Table<R> $table();
        @Override
        @NotNull default Table<R> $aliased() {
            return $table();
        }
        @Override
        @NotNull Name $alias();
        // TODO [#12425] Reuse MDerivedColumnList
    }
    public interface Dual extends Table<Record>, UEmpty {}
    public interface Lateral<R extends Record> extends Table<R>, UOperator1<Table<R>, Lateral<R>> {}
    public interface DerivedTable<R extends org.jooq.Record> extends org.jooq.Table<R>, UOperator1<org.jooq.Select<R>, DerivedTable<R>> {}
    public interface Values<R extends Record> extends Table<R>, UOperator1<UnmodifiableList<? extends Row>, Values<R>> {}
    public interface DataChangeDeltaTable<R extends Record> extends Table<R> {
        @NotNull ResultOption $resultOption();
        @NotNull DMLQuery<R> $query();
    }
    public interface RowsFrom extends Table<Record> {
        @NotNull UnmodifiableList<? extends Table<?>> $tables();
    }
    public interface GenerateSeries<T> extends Table<Record1<T>>, UOperator3<Field<T>, Field<T>, Field<T>, GenerateSeries<T>> {
        @NotNull default Field<T> $from() { return $arg1(); }
        @NotNull default Field<T> $to() { return $arg2(); }
        @Nullable default Field<T> $step() { return $arg3(); }
    }

    // -------------------------------------------------------------------------
    // XXX: UDT expressions
    // -------------------------------------------------------------------------

    /**
     * A constructor call for {@link UDT} types.
     */
    public interface UDTConstructor<R extends UDTRecord<R>>
        extends
            Field<R>
    {
        @NotNull UDT<R> $udt();
        @NotNull UnmodifiableList<Field<?>> $args();
    }

    // -------------------------------------------------------------------------
    // XXX: Conditions
    // -------------------------------------------------------------------------

    /**
     * A {@link Condition} that is always <code>TRUE</code>.
     */
    public interface True
        extends
            Condition
    {}

    /**
     * A {@link Condition} that is always <code>FALSE</code>.
     */
    public interface False
        extends
            Condition
    {}

    /**
     * A {@link Condition} that is always <code>NULL</code>.
     */
    public interface Null
        extends
            Condition
    {}

    /**
     * A {@link Condition} consisting of two {@link Condition} operands and a
     * binary logic {@link Operator}.
     */
    public /*sealed*/ interface CombinedCondition<R extends CombinedCondition<R>>
        extends
            Condition,
            UCommutativeOperator<Condition, R>
        /*permits
            MAnd,
            MOr*/
    {}

    /**
     * A {@link Condition} consisting of two {@link Field} operands and a
     * {@link Comparator} operator.
     */
    public /*sealed*/ interface CompareCondition<T, R extends CompareCondition<T, R>>
        extends
            Condition,
            UOperator2<Field<T>, Field<T>, R>
        /*permits
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
            MEndsWithIgnoreCase*/
    {}

    /**
     * The <code>BETWEEN</code> predicate.
     */
    public interface Between<T>
        extends
            Condition,
            UOperator3<Field<T>, Field<T>, Field<T>, Between<T>>
    {
        boolean $symmetric();
        @NotNull Between<T> $symmetric(boolean symmetric);
    }

    /**
     * The <code>IN</code> predicate accepting a list of values.
     */
    public /*sealed*/ interface InList<T>
        extends
            Condition,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, InList<T>>
        /*permits
            InList*/
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        @NotNull default UnmodifiableList<? extends Field<T>> $list() { return $arg2(); }
    }

    /**
     * The <code>NOT IN</code> predicate accepting a list of values.
     */
    public /*sealed*/ interface NotInList<T>
        extends
            Condition,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, NotInList<T>>
        /*permits
            NotInList*/
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        @NotNull default UnmodifiableList<? extends Field<T>> $list() { return $arg2(); }
    }

    public /*sealed*/ interface RegexpLike
        extends
            Condition
        /*permits
            RegexpLike*/
    {
        @NotNull Field<?> $search();
        @NotNull Field<String> $pattern();
    }

    public /*sealed*/ interface Extract
        extends
            Field<Integer>
        /*permits
            Extract*/
    {
        @NotNull Field<?> $field();
        @NotNull DatePart $datePart();
    }

    public /*sealed*/ interface RowIsNull
        extends
            Condition,
            UOperator1<Row, RowIsNull>
        /*permits
            RowIsNull*/
    {
        @NotNull default Row $field() { return $arg1(); }
    }

    public /*sealed*/ interface RowIsNotNull
        extends
            Condition,
            UOperator1<Row, RowIsNotNull>
        /*permits
            RowIsNotNull*/
    {
        @NotNull default Row $field() { return $arg1(); }
    }

    public /*sealed*/ interface RowOverlaps
        extends
            Condition,
            UOperator2<Row, Row, RowOverlaps>
        /*permits
            RowOverlaps*/
    {}

    public /*sealed*/ interface SelectIsNull
        extends
            Condition,
            UOperator1<Select<?>, SelectIsNull>
        /*permits
            SelectIsNull*/
    {
        @NotNull default Select<?> $field() { return $arg1(); }
    }

    public /*sealed*/ interface SelectIsNotNull
        extends
            Condition,
            UOperator1<Select<?>, SelectIsNotNull>
        /*permits
            SelectIsNotNull*/
    {
        @NotNull default Select<?> $field() { return $arg1(); }
    }

    public interface Quantified
        extends
            org.jooq.QueryPart
    {}

    public interface QuantifiedSelect<R extends Record>
        extends
            Quantified,
            UOperator2<Quantifier, Select<R>, QuantifiedSelect<R>>
    {
        @NotNull default Quantifier $quantifier() { return $arg1(); }
        @NotNull default QuantifiedSelect<R> $quantifier(Quantifier newQuantifier) { return $arg1(newQuantifier); }
        @NotNull default Select<R> $query() { return $arg2(); }
        @NotNull default QuantifiedSelect<R> $query(Select<R> newSelect) { return $arg2(newSelect); }
    }

    public interface QuantifiedArray<T>
        extends
            Quantified,
            UOperator2<Quantifier, Field<T[]>, QuantifiedArray<T>>
    {
        @NotNull default Quantifier $quantifier() { return $arg1(); }
        @NotNull default QuantifiedArray<T> $quantifier(Quantifier newQuantifier) { return $arg1(newQuantifier); }
        @NotNull default Field<T[]> $array() { return $arg2(); }
        @NotNull default QuantifiedArray<T> $array(Field<T[]> newArray) { return $arg2(newArray); }
    }

    // -------------------------------------------------------------------------
    // XXX: Rows
    // -------------------------------------------------------------------------

    public /*sealed*/ interface RowAsField<R extends org.jooq.Record>
        extends
            org.jooq.Field<R>
        /*permits
            RowAsField*/
    {
        @NotNull Row $row();
    }

    public /*sealed*/ interface TableAsField<R extends org.jooq.Record>
        extends
            org.jooq.Field<R>
        /*permits
            TableAsField*/
    {
        @NotNull Table<R> $table();
    }

    public interface JoinTable<R extends org.jooq.Record, J extends JoinTable<R, J>>
        extends
            org.jooq.Table<R>
    {
        @NotNull Table<?> $table1();
        @NotNull Table<?> $table2();
        @Nullable JoinHint $hint();
        @NotNull J $table1(Table<?> table1);
        @NotNull J $table2(Table<?> table2);
        @NotNull J $hint(JoinHint hint);
    }

    public interface CrossJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, CrossJoin<R>>
    {}

    public interface CrossApply<R extends org.jooq.Record>
        extends
            JoinTable<R, CrossApply<R>>
    {}

    public interface OuterApply<R extends org.jooq.Record>
        extends
            JoinTable<R, OuterApply<R>>
    {}

    public interface NaturalJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, NaturalJoin<R>>
    {}

    public interface NaturalLeftJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, NaturalLeftJoin<R>>
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy2();
        @NotNull NaturalLeftJoin<R> $partitionBy2(Collection<? extends Field<?>> partitionBy2);
    }

    public interface NaturalRightJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, NaturalRightJoin<R>>
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy1();
        @NotNull NaturalRightJoin<R> $partitionBy1(Collection<? extends Field<?>> partitionBy1);
    }

    public interface NaturalFullJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, NaturalFullJoin<R>>
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy1();
        @NotNull NaturalFullJoin<R> $partitionBy1(Collection<? extends Field<?>> partitionBy1);
        @NotNull UnmodifiableList<Field<?>> $partitionBy2();
        @NotNull NaturalFullJoin<R> $partitionBy2(Collection<? extends Field<?>> partitionBy2);
    }

    public interface QualifiedJoin<R extends org.jooq.Record, J extends QualifiedJoin<R, J>>
        extends
            JoinTable<R, J>
    {
        @Nullable Condition $on();
        @NotNull J $on(Condition on);
        @NotNull UnmodifiableList<Field<?>> $using();
        @NotNull J $using(Collection<? extends Field<?>> using);
    }

    public interface Join<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, Join<R>>
    {}

    public interface StraightJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, StraightJoin<R>>
    {}

    public interface LeftJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, LeftJoin<R>>
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy2();
        @NotNull LeftJoin<R> $partitionBy2(Collection<? extends Field<?>> partitionBy2);
    }

    public interface RightJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, RightJoin<R>>
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy1();
        @NotNull RightJoin<R> $partitionBy1(Collection<? extends Field<?>> partitionBy1);
    }

    public interface FullJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, FullJoin<R>>
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy1();
        @NotNull FullJoin<R> $partitionBy1(Collection<? extends Field<?>> partitionBy1);
        @NotNull UnmodifiableList<Field<?>> $partitionBy2();
        @NotNull FullJoin<R> $partitionBy2(Collection<? extends Field<?>> partitionBy2);
    }

    public interface LeftSemiJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, LeftSemiJoin<R>>
    {}

    public interface LeftAntiJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, LeftAntiJoin<R>>
    {}

    // -------------------------------------------------------------------------
    // XXX: SelectFields, GroupFields and SortFields
    // -------------------------------------------------------------------------

    // Can't seal these types yet because of https://bugs.eclipse.org/bugs/show_bug.cgi?id=577872
    // See also: https://github.com/jOOQ/jOOQ/issues/16444

    public interface EmptyGroupingSet
        extends
            GroupField,
            UEmpty
        /*permits
            org.jooq.impl.EmptyGroupingSet*/
    {}

    public interface Rollup
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends FieldOrRow>, Rollup>
        /*permits
            Rollup*/
    {}

    public interface Cube
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends FieldOrRow>, Cube>
        /*permits
            Cube*/
    {}

    public interface GroupingSets
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends UnmodifiableList<? extends FieldOrRow>>, GroupingSets>
        /*permits
            GroupingSets*/
    {}

    // -------------------------------------------------------------------------
    // XXX: Aggregate functions and window functions
    // -------------------------------------------------------------------------

    public /*sealed*/ interface RatioToReport
        extends
            AggregateFunction<BigDecimal, RatioToReport>
        /*permits
            RatioToReport*/
    {
        @NotNull Field<? extends Number> $field();
    }

    public /*sealed*/ interface Mode<T>
        extends
            AggregateFunction<T, Mode<T>>,
            UOperator1<Field<T>, Mode<T>>
        /*permits
            Mode*/
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Mode<T> $field(Field<T> field) { return $arg1(field); }
    }

    public /*sealed*/ interface ModeOrdered<T>
        extends
            OrderedAggregateFunction<T, ModeOrdered<T>>,
            UOperator0<ModeOrdered<T>>
        /*permits
            ModeOrdered*/
    {}

    public /*sealed*/ interface MultisetAgg<R extends Record>
        extends
            OrderedAggregateFunction<Result<R>, MultisetAgg<R>>,
            UOperator1<QOM.UnmodifiableList<? extends Field<?>>, MultisetAgg<R>>
        /*permits
            MultisetAgg*/
    {
        /**
         * @deprecated - 3.21.0 - [#19277] - Use {@link #$fields()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        @NotNull Row $row();
        @NotNull default QOM.UnmodifiableList<? extends Field<?>> $fields() { return $arg1(); }
        @CheckReturnValue
        @NotNull default MultisetAgg<R> $fields(QOM.UnmodifiableList<? extends Field<?>> newFields) { return $arg1(newFields); }
    }

    public /*sealed*/ interface ArrayAgg<T>
        extends
            OrderedAggregateFunction<T[], ArrayAgg<T>>,
            UOperator1<Field<T>, ArrayAgg<T>>
        /*permits
            ArrayAgg*/
    {
        boolean $distinct();
        @NotNull default Field<T> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default ArrayAgg<T> $field(Field<T> field) { return $arg1(field); }
    }

    public /*sealed*/ interface ListAgg
        extends
            OrderedAggregateFunction<String, ListAgg>,
            UOperator2<Field<?>, Field<String>, ListAgg>
        /*permits
            ListAgg*/
    {
        boolean $distinct();
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default ListAgg $field(Field<?> field) { return $arg1(field); }
        @Nullable default Field<String> $separator() { return $arg2(); }
        @CheckReturnValue
        @NotNull default ListAgg $separator(Field<String> separator) { return $arg2(separator); }
    }

    public /*sealed*/ interface BinaryListAgg
        extends
            OrderedAggregateFunction<byte[], BinaryListAgg>,
            UOperator2<Field<?>, Field<byte[]>, BinaryListAgg>
        /*permits
            BinaryListAgg*/
    {
        boolean $distinct();
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BinaryListAgg $field(Field<?> field) { return $arg1(field); }
        @Nullable default Field<byte[]> $separator() { return $arg2(); }
        @CheckReturnValue
        @NotNull default BinaryListAgg $separator(Field<byte[]> separator) { return $arg2(separator); }
    }

    public /*sealed*/ interface XMLAgg
        extends
            OrderedAggregateFunction<XML, XMLAgg>,
            UOperator1<Field<XML>, XMLAgg>
        /*permits
            XMLAgg*/
    {
        @NotNull default Field<XML> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default XMLAgg $field(Field<XML> field) { return $arg1(field); }
    }

    public /*sealed*/ interface JSONArrayAgg<J>
        extends
            OrderedAggregateFunction<J, JSONArrayAgg<J>>,
            UOperator1<org.jooq.Field<?>, JSONArrayAgg<J>>
        /*permits
            JSONArrayAgg*/
    {
        boolean $distinct();
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONArrayAgg<J> $field(Field<?> field) { return $arg1(field); }
        @Nullable JSONOnNull $onNull();
        @CheckReturnValue
        @NotNull JSONArrayAgg<J> $onNull(JSONOnNull onNull);
        @Nullable DataType<?> $returning();
        @CheckReturnValue
        @NotNull JSONArrayAgg<J> $returning(DataType<?> returning);
    }

    public /*sealed*/ interface JSONObjectAgg<J>
        extends
            AggregateFunction<J, JSONObjectAgg<J>>,
            UOperator1<JSONEntry<?>, JSONObjectAgg<J>>
        /*permits
            JSONObjectAgg*/
    {
        @NotNull default JSONEntry<?> $entry() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONObjectAgg<J> $entry(JSONEntry<?> entry) { return $arg1(entry); }
        @Nullable JSONOnNull $onNull();
        @CheckReturnValue
        @NotNull JSONObjectAgg<J> $onNull(JSONOnNull onNull);
        @Nullable DataType<?> $returning();
        @CheckReturnValue
        @NotNull JSONObjectAgg<J> $returning(DataType<?> returning);
    }

    public /*sealed*/ interface CountTable
        extends
            AggregateFunction<Integer, CountTable>
        /*permits
            CountTable*/
    {
        @NotNull Table<?> $table();
        boolean $distinct();
    }















    /**
     * An aggregate function with an aggregate <code>ORDER BY</code> clause.
     */
    public interface OrderedAggregateFunction<T, Q extends OrderedAggregateFunction<T, Q>>
        extends
            AggregateFunction<T, Q>
    {
        @NotNull UnmodifiableList<? extends SortField<?>> $withinGroupOrderBy();
        @CheckReturnValue
        @NotNull Q $withinGroupOrderBy(Collection<? extends SortField<?>> orderBy);
    }

    public interface AggregateFunction<T, Q extends AggregateFunction<T, Q>>
        extends
            org.jooq.AggregateFunction<T>,
            WindowFunction<T, Q>
    {
        @Override
        @Nullable Condition $filterWhere();
        @CheckReturnValue
        @NotNull  Q $filterWhere(Condition condition);
    }

    public interface NullTreatmentWindowFunction<T, Q extends NullTreatmentWindowFunction<T, Q>>
        extends
            WindowFunction<T, Q>
    {
        @Nullable NullTreatment $nullTreatment();
        @CheckReturnValue
        @NotNull  Q $nullTreatment(NullTreatment nullTreatment);
    }

    public interface WindowFunction<T, Q extends WindowFunction<T, Q>>
        extends
            org.jooq.Field<T>
    {
        @Nullable WindowSpecification $windowSpecification();
        @CheckReturnValue
        @NotNull  Q $windowSpecification(WindowSpecification windowSpecification);
        @Nullable WindowDefinition $windowDefinition();
        @CheckReturnValue
        @NotNull  Q $windowDefinition(WindowDefinition windowDefinition);
        @Nullable Name $windowName();
        @CheckReturnValue
        @NotNull  Q $windowName(Name name);
    }

    public /*sealed*/ interface RowNumber
        extends
            WindowFunction<Integer, RowNumber>
        /*permits
            RowNumber*/
    {}

    public /*sealed*/ interface Rank
        extends
            WindowFunction<Integer, Rank>
        /*permits
            Rank*/
    {}

    public /*sealed*/ interface DenseRank
        extends
            WindowFunction<Integer, DenseRank>
        /*permits
            DenseRank*/
    {}

    public /*sealed*/ interface PercentRank
        extends
            WindowFunction<BigDecimal, PercentRank>
        /*permits
            PercentRank*/
    {}

    public /*sealed*/ interface CumeDist
        extends
            WindowFunction<BigDecimal, CumeDist>
        /*permits
            CumeDist*/
    {}

    public /*sealed*/ interface Ntile
        extends
            WindowFunction<Integer, Ntile>
        /*permits Ntile*/
    {
        @NotNull Field<Integer> $tiles();
        @CheckReturnValue
        @NotNull Ntile $tiles(Field<Integer> tiles);
    }

    public /*sealed*/ interface Lead<T>
        extends
            NullTreatmentWindowFunction<T, Lead<T>>
        /*permits
            Lead*/
    {
        @NotNull  Field<T> $field();
        @CheckReturnValue
        @NotNull  Lead<T> $field(Field<T> field);
        @Nullable Field<Integer> $offset();
        @CheckReturnValue
        @NotNull  Lead<T> $offset(Field<Integer> offset);
        @Nullable Field<T> $defaultValue();
        @CheckReturnValue
        @NotNull  Lead<T> $defaultValue(Field<T> defaultValue);
    }

    public /*sealed*/ interface Lag<T>
        extends
            NullTreatmentWindowFunction<T, Lag<T>>
        /*permits
            Lag*/
    {
        @NotNull  Field<T> $field();
        @CheckReturnValue
        @NotNull  Lag<T> $field(Field<T> field);
        @Nullable Field<Integer> $offset();
        @CheckReturnValue
        @NotNull  Lag<T> $offset(Field<Integer> offset);
        @Nullable Field<T> $defaultValue();
        @CheckReturnValue
        @NotNull  Lag<T> $defaultValue(Field<T> defaultValue);
    }

    public /*sealed*/ interface FirstValue<T>
        extends
            NullTreatmentWindowFunction<T, FirstValue<T>>
        /*permits
            FirstValue*/
    {
        @NotNull  Field<T> $field();
        @CheckReturnValue
        @NotNull  FirstValue<T> $field(Field<T> field);
    }

    public /*sealed*/ interface LastValue<T>
        extends
            NullTreatmentWindowFunction<T, LastValue<T>>
        /*permits
            LastValue*/
    {
        @NotNull  Field<T> $field();
        @CheckReturnValue
        @NotNull  LastValue<T> $field(Field<T> field);
    }

    public /*sealed*/ interface NthValue<T>
        extends
            NullTreatmentWindowFunction<T, NthValue<T>>
        /*permits
            NthValue*/
    {
        @NotNull  Field<T> $field();
        @CheckReturnValue
        @NotNull  NthValue<T> $field(Field<T> field);
        @NotNull  Field<Integer> $offset();
        @CheckReturnValue
        @NotNull  NthValue<T> $offset(Field<Integer> offset);
        @Nullable FromFirstOrLast $fromFirstOrLast();
        @CheckReturnValue
        @NotNull  NthValue<T> $fromFirstOrLast(FromFirstOrLast fromFirstOrLast);
    }

    // -------------------------------------------------------------------------
    // XXX: Fields
    // -------------------------------------------------------------------------

    public /*sealed*/ interface FieldAlias<T>
        extends
            org.jooq.Field<T>,
            Aliasable<Field<?>>
        /*permits
            FieldAlias*/
    {
        @NotNull Field<T> $field();
        @Override
        @NotNull default Field<?> $aliased() {
            return $field();
        }
        @Override
        @NotNull Name $alias();
    }

    public /*sealed*/ interface Function<T>
        extends
            org.jooq.Field<T>
        /*permits
            org.jooq.impl.Function,
            org.jooq.impl.Function1*/
    {
        @NotNull UnmodifiableList<? extends Field<?>> $args();
    }

    public /*sealed*/ interface Cast<T>
        extends
            org.jooq.Field<T>
        /*permits
            Cast*/
    {
        @NotNull Field<?> $field();
    }

    public /*sealed*/ interface Coerce<T>
        extends
            org.jooq.Field<T>
        /*permits
            Coerce*/
    {
        @NotNull Field<?> $field();
    }

    public /*sealed*/ interface Default<T>
        extends
            org.jooq.Field<T>,
            UEmpty
        /*permits
            Default*/
    {}

    public /*sealed*/ interface Collated
        extends
            org.jooq.Field<String>
        /*permits
            Collated*/
    {
        @NotNull Field<?> $field();
        @NotNull Collation $collation();
    }

    public /*sealed*/ interface Array<T>
        extends
            org.jooq.Field<T[]>
        /*permits
            org.jooq.impl.Array*/
    {
        @NotNull UnmodifiableList<? extends Field<T>> $elements();
    }

    public /*sealed*/ interface ArrayQuery<T>
        extends
            org.jooq.Field<T[]>
        /*permits
            ArrayQuery*/
    {
        @NotNull Select<? extends Record1<T>> $query();
    }

    public /*sealed*/ interface Multiset<R extends org.jooq.Record>
        extends
            org.jooq.Field<org.jooq.Result<R>>
        /*permits
            Multiset*/
    {
        @NotNull TableLike<R> $table();
    }

    public /*sealed*/ interface ScalarSubquery<T>
        extends
            org.jooq.Field<T>,
            UOperator1<org.jooq.Select<? extends org.jooq.Record1<T>>, ScalarSubquery<T>>
        /*permits
            ScalarSubquery*/
    {}

    public /*sealed*/ interface RowSubquery
        extends
            org.jooq.Row,
            UOperator1<org.jooq.Select<?>, RowSubquery>
    {}

    public /*sealed*/ interface Neg<T>
        extends
            UReturnsNullOnNullInput,
            Field<T>,
            UOperator1<Field<T>, Neg<T>>
        /*permits
            Neg*/
    {}

    public /*sealed*/ interface Greatest<T>
        extends
            Field<T>,
            UOperator1<UnmodifiableList<? extends Field<T>>, Greatest<T>>
        /*permits
            Greatest*/
    {}

    public /*sealed*/ interface Least<T>
        extends
            Field<T>,
            UOperator1<UnmodifiableList<? extends Field<T>>, Least<T>>
        /*permits
            Least*/
    {}

    public /*sealed*/ interface Choose<T>
        extends
            Field<T>,
            UOperator2<Field<Integer>, UnmodifiableList<? extends Field<T>>, Choose<T>>
        /*permits
            Choose*/
    {}

    public /*sealed*/ interface FieldFunction<T>
        extends
            Field<Integer>,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, FieldFunction<T>>
        /*permits
            FieldFunction*/
    {}

    public /*sealed*/ interface Nvl2<T>
        extends
            Field<T>,
            UOperator3<Field<?>, Field<T>, Field<T>, Nvl2<T>>
        /*permits
            Nvl2*/
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @NotNull default Field<T> $ifNotNull() { return $arg2(); }
        @NotNull default Field<T> $ifIfNull() { return $arg3(); }
    }

    public /*sealed*/ interface Iif<T>
        extends
            Field<T>,
            UOperator3<Condition, Field<T>, Field<T>, Iif<T>>
        /*permits
            Iif*/
    {
        @NotNull default Condition $condition() { return $arg1(); }
        @NotNull default Field<T> $ifTrue() { return $arg2(); }
        @NotNull default Field<T> $ifFalse() { return $arg3(); }
    }

    public /*sealed*/ interface Coalesce<T>
        extends
            Field<T>,
            UOperator1<UnmodifiableList<? extends Field<T>>, Coalesce<T>>
        /*permits
            Coalesce*/
    {}

    public /*sealed*/ interface CaseSimple<V, T>
        extends
            Field<T>,
            UOperator3<Field<V>, UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>>, Field<T>, CaseSimple<V, T>>
    {
        @NotNull  default Field<V> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull  default CaseSimple<V, T> $value(Field<V> value) { return $arg1(value); }
        @NotNull  default UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>> $when() { return $arg2(); }
        @CheckReturnValue
        @NotNull  default CaseSimple<V, T> $when(UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>> when) { return $arg2(when); }
        @Nullable default Field<T> $else() { return $arg3(); }
        @CheckReturnValue
        @NotNull  default CaseSimple<V, T> $else(Field<T> else_) { return $arg3(else_); }
    }

    public /*sealed*/ interface CaseSearched<T>
        extends
            Field<T>,
            UOperator2<UnmodifiableList<? extends Tuple2<Condition, Field<T>>>, Field<T>, CaseSearched<T>>
    {
        @NotNull  default UnmodifiableList<? extends Tuple2<Condition, Field<T>>> $when() { return $arg1(); }
        @CheckReturnValue
        @NotNull  default CaseSearched<T> $when(UnmodifiableList<? extends Tuple2<Condition, Field<T>>> when) { return $arg1(when); }
        @Nullable default Field<T> $else() { return $arg2(); }
        @CheckReturnValue
        @NotNull  default CaseSearched<T> $else(Field<T> else_) { return $arg2(else_); }
    }

    public /*sealed*/ interface Decode<V, T>
        extends
            Field<T>,
            UOperator3<Field<V>, UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>>, Field<T>, Decode<V, T>>
    {
        @NotNull  default Field<V> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull  default Decode<V, T> $value(Field<V> value) { return $arg1(value); }
        @NotNull  default UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>> $when() { return $arg2(); }
        @CheckReturnValue
        @NotNull  default Decode<V, T> $when(UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>> when) { return $arg2(when); }
        @Nullable default Field<T> $else() { return $arg3(); }
        @CheckReturnValue
        @NotNull  default Decode<V, T> $else(Field<T> else_) { return $arg3(else_); }
    }

    public /*sealed*/ interface Concat
        extends
            Field<String>,
            UOperator1<UnmodifiableList<? extends Field<?>>, Concat>
        /*permits
            Concat*/
    {}

    public /*sealed*/ interface TimestampDiff<T>
        extends
            Field<DayToSecond>,
            UOperator2<Field<T>, Field<T>, TimestampDiff<T>>
        /*permits
            TimestampDiff*/
    {
        @NotNull default Field<T> $minuend() { return $arg1(); }
        @NotNull default Field<T> $subtrahend() { return $arg2(); }
    }

    public /*sealed*/ interface Convert<T>
        extends
            Field<T>
        /*permits
            ConvertDateTime*/
    {
        @NotNull Field<?> $field();
        int $style();
    }

    public /*sealed*/ interface CurrentDate<T>
        extends
            Field<T>,
            UEmpty
        /*permits
            CurrentDate*/
    {}

    public /*sealed*/ interface CurrentTime<T>
        extends
            Field<T>,
            UEmpty
        /*permits
            CurrentTime*/
    {}

    public /*sealed*/ interface CurrentTimestamp<T>
        extends
            Field<T>,
            UEmpty
        /*permits
            CurrentTimestamp*/
    {}

    public /*sealed*/ interface XMLQuery
        extends
            Field<XML>
        /*permits
            XMLQuery*/
    {
        @NotNull Field<String> $xpath();
        @NotNull Field<XML> $passing();
        @Nullable XMLPassingMechanism $passingMechanism();
    }

    public /*sealed*/ interface XMLElement
        extends
            Field<XML>
        /*permits
            XMLElement*/
    {
        @NotNull Name $elementName();
        @NotNull XMLAttributes $attributes();
        @NotNull UnmodifiableList<? extends Field<?>> $content();
    }

    public /*sealed*/ interface XMLExists
        extends
            Condition
        /*permits
            XMLExists*/
    {
        @NotNull Field<String> $xpath();
        @NotNull Field<XML> $passing();
        @Nullable XMLPassingMechanism $passingMechanism();
    }

    public /*sealed*/ interface XMLParse
        extends
            Field<XML>
        /*permits
            XMLParse*/
    {
        @NotNull Field<String> $content();
        @NotNull DocumentOrContent $documentOrContent();
    }



    /**
     * The <code>ALTER DATABASE</code> statement.
     */
    public /*sealed*/ interface AlterDatabase
        extends
            DDLQuery
        //permits
        //    AlterDatabaseImpl
    {
        @NotNull Catalog $database();
        boolean $ifExists();
        @NotNull Catalog $renameTo();
        @CheckReturnValue
        @NotNull AlterDatabase $database(Catalog database);
        @CheckReturnValue
        @NotNull AlterDatabase $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull AlterDatabase $renameTo(Catalog renameTo);
    }

    /**
     * The <code>ALTER DOMAIN</code> statement.
     */
    public /*sealed*/ interface AlterDomain<T>
        extends
            DDLQuery
        //permits
        //    AlterDomainImpl
    {
        @NotNull Domain<T> $domain();
        boolean $ifExists();
        @Nullable Constraint $addConstraint();
        @Nullable Constraint $dropConstraint();
        boolean $dropConstraintIfExists();
        @Nullable Domain<?> $renameTo();
        @Nullable Constraint $renameConstraint();
        boolean $renameConstraintIfExists();
        @Nullable Field<T> $setDefault();
        boolean $dropDefault();
        boolean $setNotNull();
        boolean $dropNotNull();
        @Nullable Cascade $cascade();
        @Nullable Constraint $renameConstraintTo();
        @CheckReturnValue
        @NotNull AlterDomain<T> $domain(Domain<T> domain);
        @CheckReturnValue
        @NotNull AlterDomain<T> $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull AlterDomain<T> $addConstraint(Constraint addConstraint);
        @CheckReturnValue
        @NotNull AlterDomain<T> $dropConstraint(Constraint dropConstraint);
        @CheckReturnValue
        @NotNull AlterDomain<T> $dropConstraintIfExists(boolean dropConstraintIfExists);
        @CheckReturnValue
        @NotNull AlterDomain<T> $renameTo(Domain<?> renameTo);
        @CheckReturnValue
        @NotNull AlterDomain<T> $renameConstraint(Constraint renameConstraint);
        @CheckReturnValue
        @NotNull AlterDomain<T> $renameConstraintIfExists(boolean renameConstraintIfExists);
        @CheckReturnValue
        @NotNull AlterDomain<T> $setDefault(Field<T> setDefault);
        @CheckReturnValue
        @NotNull AlterDomain<T> $dropDefault(boolean dropDefault);
        @CheckReturnValue
        @NotNull AlterDomain<T> $setNotNull(boolean setNotNull);
        @CheckReturnValue
        @NotNull AlterDomain<T> $dropNotNull(boolean dropNotNull);
        @CheckReturnValue
        @NotNull AlterDomain<T> $cascade(Cascade cascade);
        @CheckReturnValue
        @NotNull AlterDomain<T> $renameConstraintTo(Constraint renameConstraintTo);
    }

    /**
     * The <code>ALTER INDEX</code> statement.
     */
    public /*sealed*/ interface AlterIndex
        extends
            DDLQuery
        //permits
        //    AlterIndexImpl
    {
        @NotNull Index $index();
        boolean $ifExists();
        @Nullable Table<?> $on();
        @NotNull Index $renameTo();
        @CheckReturnValue
        @NotNull AlterIndex $index(Index index);
        @CheckReturnValue
        @NotNull AlterIndex $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull AlterIndex $on(Table<?> on);
        @CheckReturnValue
        @NotNull AlterIndex $renameTo(Index renameTo);
    }

    /**
     * The <code>ALTER SCHEMA</code> statement.
     */
    public /*sealed*/ interface AlterSchema
        extends
            DDLQuery
        //permits
        //    AlterSchemaImpl
    {
        @NotNull Schema $schema();
        boolean $ifExists();
        @NotNull Schema $renameTo();
        @CheckReturnValue
        @NotNull AlterSchema $schema(Schema schema);
        @CheckReturnValue
        @NotNull AlterSchema $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull AlterSchema $renameTo(Schema renameTo);
    }

    /**
     * The <code>ALTER SEQUENCE</code> statement.
     */
    public /*sealed*/ interface AlterSequence<T extends Number>
        extends
            DDLQuery
        //permits
        //    AlterSequenceImpl
    {
        @NotNull Sequence<T> $sequence();
        boolean $ifExists();
        @Nullable Sequence<?> $renameTo();
        boolean $restart();
        @Nullable Field<T> $restartWith();
        @Nullable Field<T> $startWith();
        @Nullable Field<T> $incrementBy();
        @Nullable Field<T> $minvalue();
        boolean $noMinvalue();
        @Nullable Field<T> $maxvalue();
        boolean $noMaxvalue();
        @Nullable CycleOption $cycle();
        @Nullable Field<T> $cache();
        boolean $noCache();
        @CheckReturnValue
        @NotNull AlterSequence<T> $sequence(Sequence<T> sequence);
        @CheckReturnValue
        @NotNull AlterSequence<T> $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull AlterSequence<T> $renameTo(Sequence<?> renameTo);
        @CheckReturnValue
        @NotNull AlterSequence<T> $restart(boolean restart);
        @CheckReturnValue
        @NotNull AlterSequence<T> $restartWith(Field<T> restartWith);
        @CheckReturnValue
        @NotNull AlterSequence<T> $startWith(Field<T> startWith);
        @CheckReturnValue
        @NotNull AlterSequence<T> $incrementBy(Field<T> incrementBy);
        @CheckReturnValue
        @NotNull AlterSequence<T> $minvalue(Field<T> minvalue);
        @CheckReturnValue
        @NotNull AlterSequence<T> $noMinvalue(boolean noMinvalue);
        @CheckReturnValue
        @NotNull AlterSequence<T> $maxvalue(Field<T> maxvalue);
        @CheckReturnValue
        @NotNull AlterSequence<T> $noMaxvalue(boolean noMaxvalue);
        @CheckReturnValue
        @NotNull AlterSequence<T> $cycle(CycleOption cycle);
        @CheckReturnValue
        @NotNull AlterSequence<T> $cache(Field<T> cache);
        @CheckReturnValue
        @NotNull AlterSequence<T> $noCache(boolean noCache);
    }

    /**
     * The <code>ALTER TYPE</code> statement.
     */
    public /*sealed*/ interface AlterType
        extends
            DDLQuery
        //permits
        //    AlterTypeImpl
    {
        @NotNull Type<?> $type();
        boolean $ifExists();
        @Nullable Type<?> $renameTo();
        @Nullable Schema $setSchema();
        @Nullable Field<String> $addValue();
        @Nullable Field<String> $renameValue();
        @Nullable Field<String> $renameValueTo();
        @CheckReturnValue
        @NotNull AlterType $type(Type<?> type);
        @CheckReturnValue
        @NotNull AlterType $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull AlterType $renameTo(Type<?> renameTo);
        @CheckReturnValue
        @NotNull AlterType $setSchema(Schema setSchema);
        @CheckReturnValue
        @NotNull AlterType $addValue(Field<String> addValue);
        @CheckReturnValue
        @NotNull AlterType $renameValue(Field<String> renameValue);
        @CheckReturnValue
        @NotNull AlterType $renameValueTo(Field<String> renameValueTo);
    }

    /**
     * The <code>ALTER VIEW</code> statement.
     */
    public /*sealed*/ interface AlterView
        extends
            DDLQuery
        //permits
        //    AlterViewImpl
    {
        @NotNull Table<?> $view();
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        boolean $materialized();
        boolean $ifExists();
        @Nullable Comment $comment();
        @Nullable Table<?> $renameTo();
        @Nullable Select<?> $as();
        @CheckReturnValue
        @NotNull AlterView $view(Table<?> view);
        @CheckReturnValue
        @NotNull AlterView $fields(Collection<? extends Field<?>> fields);
        @CheckReturnValue
        @NotNull AlterView $materialized(boolean materialized);
        @CheckReturnValue
        @NotNull AlterView $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull AlterView $comment(Comment comment);
        @CheckReturnValue
        @NotNull AlterView $renameTo(Table<?> renameTo);
        @CheckReturnValue
        @NotNull AlterView $as(Select<?> as);
    }

    /**
     * The <code>COMMENT ON TABLE</code> statement.
     */
    public /*sealed*/ interface CommentOn
        extends
            DDLQuery
        //permits
        //    CommentOnImpl
    {
        @NotNull CommentObjectType $objectType();
        @Nullable Table<?> $table();
        @Nullable Field<?> $field();
        @Nullable Name $function();
        @Nullable Name $procedure();

        /**
         * @deprecated - 3.21.0 - [#18684] - Use {@link #$objectType()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        boolean $isView();

        /**
         * @deprecated - 3.21.0 - [#18684] - Use {@link #$objectType()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        boolean $isMaterializedView();
        @NotNull UnmodifiableList<? extends Parameter<?>> $parameters();
        @NotNull Comment $comment();
        @CheckReturnValue
        @NotNull CommentOn $objectType(CommentObjectType objectType);
        @CheckReturnValue
        @NotNull CommentOn $table(Table<?> table);
        @CheckReturnValue
        @NotNull CommentOn $field(Field<?> field);
        @CheckReturnValue
        @NotNull CommentOn $function(Name function);
        @CheckReturnValue
        @NotNull CommentOn $procedure(Name procedure);

        /**
         * @deprecated - 3.21.0 - [#18684] - Use {@link #$objectType()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        @CheckReturnValue
        @NotNull CommentOn $isView(boolean isView);

        /**
         * @deprecated - 3.21.0 - [#18684] - Use {@link #$objectType()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        @CheckReturnValue
        @NotNull CommentOn $isMaterializedView(boolean isMaterializedView);
        @CheckReturnValue
        @NotNull CommentOn $parameters(Collection<? extends Parameter<?>> parameters);
        @CheckReturnValue
        @NotNull CommentOn $comment(Comment comment);
    }

    /**
     * The <code>CREATE DATABASE</code> statement.
     */
    public /*sealed*/ interface CreateDatabase
        extends
            DDLQuery
        //permits
        //    CreateDatabaseImpl
    {
        @NotNull Catalog $database();
        boolean $ifNotExists();
        @CheckReturnValue
        @NotNull CreateDatabase $database(Catalog database);
        @CheckReturnValue
        @NotNull CreateDatabase $ifNotExists(boolean ifNotExists);
    }

    /**
     * The <code>CREATE DOMAIN</code> statement.
     */
    public /*sealed*/ interface CreateDomain<T>
        extends
            DDLQuery
        //permits
        //    CreateDomainImpl
    {
        @NotNull Domain<?> $domain();
        boolean $ifNotExists();
        @NotNull DataType<T> $dataType();
        @Nullable Field<T> $default_();
        @NotNull UnmodifiableList<? extends Constraint> $constraints();
        @CheckReturnValue
        @NotNull CreateDomain<T> $domain(Domain<?> domain);
        @CheckReturnValue
        @NotNull CreateDomain<T> $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull CreateDomain<T> $dataType(DataType<T> dataType);
        @CheckReturnValue
        @NotNull CreateDomain<T> $default_(Field<T> default_);
        @CheckReturnValue
        @NotNull CreateDomain<T> $constraints(Collection<? extends Constraint> constraints);
    }













































    /**
     * The <code>CREATE INDEX</code> statement.
     */
    public /*sealed*/ interface CreateIndex
        extends
            DDLQuery
        //permits
        //    CreateIndexImpl
    {
        boolean $unique();
        @Nullable Index $index();
        boolean $ifNotExists();
        @Nullable Table<?> $table();
        @NotNull UnmodifiableList<? extends OrderField<?>> $on();
        @NotNull UnmodifiableList<? extends Field<?>> $include();
        @Nullable Condition $where();
        boolean $excludeNullKeys();
        @CheckReturnValue
        @NotNull CreateIndex $unique(boolean unique);
        @CheckReturnValue
        @NotNull CreateIndex $index(Index index);
        @CheckReturnValue
        @NotNull CreateIndex $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull CreateIndex $table(Table<?> table);
        @CheckReturnValue
        @NotNull CreateIndex $on(Collection<? extends OrderField<?>> on);
        @CheckReturnValue
        @NotNull CreateIndex $include(Collection<? extends Field<?>> include);
        @CheckReturnValue
        @NotNull CreateIndex $where(Condition where);
        @CheckReturnValue
        @NotNull CreateIndex $excludeNullKeys(boolean excludeNullKeys);
    }

































    /**
     * The <code>CREATE TABLE</code> statement.
     */
    public /*sealed*/ interface CreateTable
        extends
            DDLQuery
        //permits
        //    CreateTableImpl
    {
        @NotNull Table<?> $table();
        @Nullable TableScope $tableScope();
        boolean $ifNotExists();

        /**
         * @deprecated - 3.21.0 - [#18603] - Use {@link #$tableScope()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        boolean $temporary();
        @NotNull UnmodifiableList<? extends TableElement> $tableElements();
        @Nullable Select<?> $select();
        @Nullable WithOrWithoutData $withData();
        @Nullable TableCommitAction $onCommit();
        @Nullable Comment $comment();
        @Nullable SQL $storage();
        @CheckReturnValue
        @NotNull CreateTable $table(Table<?> table);
        @CheckReturnValue
        @NotNull CreateTable $tableScope(TableScope tableScope);
        @CheckReturnValue
        @NotNull CreateTable $ifNotExists(boolean ifNotExists);

        /**
         * @deprecated - 3.21.0 - [#18603] - Use {@link #$tableScope()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        @CheckReturnValue
        @NotNull CreateTable $temporary(boolean temporary);
        @CheckReturnValue
        @NotNull CreateTable $tableElements(Collection<? extends TableElement> tableElements);
        @CheckReturnValue
        @NotNull CreateTable $select(Select<?> select);
        @CheckReturnValue
        @NotNull CreateTable $withData(WithOrWithoutData withData);
        @CheckReturnValue
        @NotNull CreateTable $onCommit(TableCommitAction onCommit);
        @CheckReturnValue
        @NotNull CreateTable $comment(Comment comment);
        @CheckReturnValue
        @NotNull CreateTable $storage(SQL storage);
    }

    /**
     * The <code>CREATE VIEW</code> statement.
     */
    public /*sealed*/ interface CreateView<R extends Record>
        extends
            DDLQuery
        //permits
        //    CreateViewImpl
    {
        @NotNull Table<?> $view();
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        boolean $orReplace();
        boolean $materialized();
        boolean $ifNotExists();
        @Nullable ResultQuery<? extends R> $query();
        @CheckReturnValue
        @NotNull CreateView<R> $view(Table<?> view);
        @CheckReturnValue
        @NotNull CreateView<R> $fields(Collection<? extends Field<?>> fields);
        @CheckReturnValue
        @NotNull CreateView<R> $orReplace(boolean orReplace);
        @CheckReturnValue
        @NotNull CreateView<R> $materialized(boolean materialized);
        @CheckReturnValue
        @NotNull CreateView<R> $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull CreateView<R> $query(ResultQuery<? extends R> query);
    }































































    /**
     * The <code>CREATE TYPE</code> statement.
     */
    public /*sealed*/ interface CreateType
        extends
            DDLQuery
        //permits
        //    CreateTypeImpl
    {
        @NotNull Type<?> $type();
        boolean $ifNotExists();
        @NotNull UnmodifiableList<? extends Field<String>> $values();
        @NotNull UnmodifiableList<? extends Field<?>> $attributes();
        @CheckReturnValue
        @NotNull CreateType $type(Type<?> type);
        @CheckReturnValue
        @NotNull CreateType $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull CreateType $values(Collection<? extends Field<String>> values);
        @CheckReturnValue
        @NotNull CreateType $attributes(Collection<? extends Field<?>> attributes);
    }

    /**
     * The <code>CREATE SCHEMA</code> statement.
     */
    public /*sealed*/ interface CreateSchema
        extends
            DDLQuery
        //permits
        //    CreateSchemaImpl
    {
        @NotNull Schema $schema();
        boolean $ifNotExists();
        @CheckReturnValue
        @NotNull CreateSchema $schema(Schema schema);
        @CheckReturnValue
        @NotNull CreateSchema $ifNotExists(boolean ifNotExists);
    }

    /**
     * The <code>CREATE SEQUENCE</code> statement.
     */
    public /*sealed*/ interface CreateSequence<T extends Number>
        extends
            DDLQuery
        //permits
        //    CreateSequenceImpl
    {
        @NotNull Sequence<?> $sequence();
        boolean $ifNotExists();
        @Nullable DataType<T> $dataType();
        @Nullable Field<T> $startWith();
        @Nullable Field<T> $incrementBy();
        @Nullable Field<T> $minvalue();
        boolean $noMinvalue();
        @Nullable Field<T> $maxvalue();
        boolean $noMaxvalue();
        @Nullable CycleOption $cycle();
        @Nullable Field<T> $cache();
        boolean $noCache();
        @CheckReturnValue
        @NotNull CreateSequence<T> $sequence(Sequence<?> sequence);
        @CheckReturnValue
        @NotNull CreateSequence<T> $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull CreateSequence<T> $dataType(DataType<T> dataType);
        @CheckReturnValue
        @NotNull CreateSequence<T> $startWith(Field<T> startWith);
        @CheckReturnValue
        @NotNull CreateSequence<T> $incrementBy(Field<T> incrementBy);
        @CheckReturnValue
        @NotNull CreateSequence<T> $minvalue(Field<T> minvalue);
        @CheckReturnValue
        @NotNull CreateSequence<T> $noMinvalue(boolean noMinvalue);
        @CheckReturnValue
        @NotNull CreateSequence<T> $maxvalue(Field<T> maxvalue);
        @CheckReturnValue
        @NotNull CreateSequence<T> $noMaxvalue(boolean noMaxvalue);
        @CheckReturnValue
        @NotNull CreateSequence<T> $cycle(CycleOption cycle);
        @CheckReturnValue
        @NotNull CreateSequence<T> $cache(Field<T> cache);
        @CheckReturnValue
        @NotNull CreateSequence<T> $noCache(boolean noCache);
    }



























    /**
     * The <code>DROP DATABASE</code> statement.
     */
    public /*sealed*/ interface DropDatabase
        extends
            DDLQuery
        //permits
        //    DropDatabaseImpl
    {
        @NotNull Catalog $database();
        boolean $ifExists();
        @CheckReturnValue
        @NotNull DropDatabase $database(Catalog database);
        @CheckReturnValue
        @NotNull DropDatabase $ifExists(boolean ifExists);
    }

    /**
     * The <code>DROP DOMAIN</code> statement.
     */
    public /*sealed*/ interface DropDomain
        extends
            DDLQuery
        //permits
        //    DropDomainImpl
    {
        @NotNull Domain<?> $domain();
        boolean $ifExists();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull DropDomain $domain(Domain<?> domain);
        @CheckReturnValue
        @NotNull DropDomain $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull DropDomain $cascade(Cascade cascade);
    }
























    /**
     * The <code>DROP INDEX</code> statement.
     */
    public /*sealed*/ interface DropIndex
        extends
            DDLQuery
        //permits
        //    DropIndexImpl
    {
        @NotNull Index $index();
        boolean $ifExists();
        @Nullable Table<?> $on();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull DropIndex $index(Index index);
        @CheckReturnValue
        @NotNull DropIndex $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull DropIndex $on(Table<?> on);
        @CheckReturnValue
        @NotNull DropIndex $cascade(Cascade cascade);
    }
























    /**
     * The <code>DROP SCHEMA</code> statement.
     */
    public /*sealed*/ interface DropSchema
        extends
            DDLQuery
        //permits
        //    DropSchemaImpl
    {
        @NotNull Schema $schema();
        boolean $ifExists();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull DropSchema $schema(Schema schema);
        @CheckReturnValue
        @NotNull DropSchema $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull DropSchema $cascade(Cascade cascade);
    }

    /**
     * The <code>DROP SEQUENCE</code> statement.
     */
    public /*sealed*/ interface DropSequence
        extends
            DDLQuery
        //permits
        //    DropSequenceImpl
    {
        @NotNull Sequence<?> $sequence();
        boolean $ifExists();
        @CheckReturnValue
        @NotNull DropSequence $sequence(Sequence<?> sequence);
        @CheckReturnValue
        @NotNull DropSequence $ifExists(boolean ifExists);
    }



























    /**
     * The <code>DROP TABLE</code> statement.
     */
    public /*sealed*/ interface DropTable
        extends
            DDLQuery
        //permits
        //    DropTableImpl
    {
        @Nullable TableScope $tableScope();
        @NotNull Table<?> $table();
        boolean $ifExists();

        /**
         * @deprecated - 3.21.0 - [#18603] - Use {@link #$tableScope()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        boolean $temporary();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull DropTable $tableScope(TableScope tableScope);
        @CheckReturnValue
        @NotNull DropTable $table(Table<?> table);
        @CheckReturnValue
        @NotNull DropTable $ifExists(boolean ifExists);

        /**
         * @deprecated - 3.21.0 - [#18603] - Use {@link #$tableScope()} instead.
         */
        @Deprecated(forRemoval = true, since = "3.21")
        @CheckReturnValue
        @NotNull DropTable $temporary(boolean temporary);
        @CheckReturnValue
        @NotNull DropTable $cascade(Cascade cascade);
    }
























    /**
     * The <code>DROP TYPE</code> statement.
     */
    public /*sealed*/ interface DropType
        extends
            DDLQuery
        //permits
        //    DropTypeImpl
    {
        @NotNull UnmodifiableList<? extends Type<?>> $types();
        boolean $ifExists();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull DropType $types(Collection<? extends Type<?>> types);
        @CheckReturnValue
        @NotNull DropType $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull DropType $cascade(Cascade cascade);
    }

    /**
     * The <code>DROP VIEW</code> statement.
     */
    public /*sealed*/ interface DropView
        extends
            DDLQuery
        //permits
        //    DropViewImpl
    {
        @NotNull Table<?> $view();
        boolean $materialized();
        boolean $ifExists();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull DropView $view(Table<?> view);
        @CheckReturnValue
        @NotNull DropView $materialized(boolean materialized);
        @CheckReturnValue
        @NotNull DropView $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull DropView $cascade(Cascade cascade);
    }

    /**
     * The <code>GRANT</code> statement.
     */
    public /*sealed*/ interface Grant
        extends
            DDLQuery
        //permits
        //    GrantImpl
    {
        @NotNull UnmodifiableList<? extends Privilege> $privileges();
        @NotNull Table<?> $on();
        @Nullable Role $to();
        boolean $toPublic();
        boolean $withGrantOption();
        @CheckReturnValue
        @NotNull Grant $privileges(Collection<? extends Privilege> privileges);
        @CheckReturnValue
        @NotNull Grant $on(Table<?> on);
        @CheckReturnValue
        @NotNull Grant $to(Role to);
        @CheckReturnValue
        @NotNull Grant $toPublic(boolean toPublic);
        @CheckReturnValue
        @NotNull Grant $withGrantOption(boolean withGrantOption);
    }

    /**
     * The <code>REVOKE</code> statement.
     */
    public /*sealed*/ interface Revoke
        extends
            DDLQuery
        //permits
        //    RevokeImpl
    {
        @NotNull UnmodifiableList<? extends Privilege> $privileges();
        boolean $grantOptionFor();
        @NotNull Table<?> $on();
        @Nullable Role $from();
        boolean $fromPublic();
        @CheckReturnValue
        @NotNull Revoke $privileges(Collection<? extends Privilege> privileges);
        @CheckReturnValue
        @NotNull Revoke $grantOptionFor(boolean grantOptionFor);
        @CheckReturnValue
        @NotNull Revoke $on(Table<?> on);
        @CheckReturnValue
        @NotNull Revoke $from(Role from);
        @CheckReturnValue
        @NotNull Revoke $fromPublic(boolean fromPublic);
    }

    /**
     * The <code>SET</code> statement.
     * <p>
     * Set a vendor specific session configuration to a new value.
     */
    public /*sealed*/ interface SetCommand
        extends
            org.jooq.RowCountQuery
        //permits
        //    SetCommand
    {
        @NotNull Name $name();
        @NotNull Param<?> $value();
        boolean $local();
        @CheckReturnValue
        @NotNull SetCommand $name(Name name);
        @CheckReturnValue
        @NotNull SetCommand $value(Param<?> value);
        @CheckReturnValue
        @NotNull SetCommand $local(boolean local);
    }

    /**
     * The <code>SET CATALOG</code> statement.
     * <p>
     * Set the current catalog to a new value.
     */
    public /*sealed*/ interface SetCatalog
        extends
            org.jooq.RowCountQuery
        //permits
        //    SetCatalog
    {
        @NotNull Catalog $catalog();
        @CheckReturnValue
        @NotNull SetCatalog $catalog(Catalog catalog);
    }

    /**
     * The <code>SET SCHEMA</code> statement.
     * <p>
     * Set the current schema to a new value.
     */
    public /*sealed*/ interface SetSchema
        extends
            org.jooq.RowCountQuery
        //permits
        //    SetSchema
    {
        @NotNull Schema $schema();
        @CheckReturnValue
        @NotNull SetSchema $schema(Schema schema);
    }

    /**
     * The <code>TRUNCATE</code> statement.
     */
    public /*sealed*/ interface Truncate<R extends Record>
        extends
            DDLQuery
        //permits
        //    TruncateImpl
    {
        @NotNull UnmodifiableList<? extends Table<?>> $table();
        @Nullable IdentityRestartOption $restartIdentity();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull Truncate<R> $table(Collection<? extends Table<?>> table);
        @CheckReturnValue
        @NotNull Truncate<R> $restartIdentity(IdentityRestartOption restartIdentity);
        @CheckReturnValue
        @NotNull Truncate<R> $cascade(Cascade cascade);
    }























    /**
     * The <code>START TRANSACTION</code> statement.
     * <p>
     * Start a transaction
     */
    public /*sealed*/ interface StartTransaction
        extends
            UEmpty,
            org.jooq.RowCountQuery
        //permits
        //    StartTransaction
    {}

    /**
     * The <code>SAVEPOINT</code> statement.
     * <p>
     * Specify a savepoint
     */
    public /*sealed*/ interface Savepoint
        extends
            org.jooq.RowCountQuery
        //permits
        //    Savepoint
    {
        @NotNull Name $name();
        @CheckReturnValue
        @NotNull Savepoint $name(Name name);
    }

    /**
     * The <code>RELEASE SAVEPOINT</code> statement.
     * <p>
     * Release a savepoint
     */
    public /*sealed*/ interface ReleaseSavepoint
        extends
            org.jooq.RowCountQuery
        //permits
        //    ReleaseSavepoint
    {
        @NotNull Name $name();
        @CheckReturnValue
        @NotNull ReleaseSavepoint $name(Name name);
    }

    /**
     * The <code>COMMIT</code> statement.
     * <p>
     * Commit a transaction
     */
    public /*sealed*/ interface Commit
        extends
            UEmpty,
            org.jooq.RowCountQuery
        //permits
        //    Commit
    {}

    /**
     * The <code>ROLLBACK</code> statement.
     * <p>
     * Rollback a transaction
     */
    public /*sealed*/ interface Rollback
        extends
            RowCountQuery
        //permits
        //    Rollback
    {
        @Nullable Name $toSavepoint();
        @CheckReturnValue
        @NotNull Rollback $toSavepoint(Name toSavepoint);
    }

    /**
     * The <code>AND</code> operator.
     */
    public /*sealed*/ interface And
        extends
            UCommutativeOperator<Condition, And>,
            CombinedCondition<And>
        //permits
        //    And
    {}

    /**
     * The <code>BINARY LIKE</code> operator.
     * <p>
     * The LIKE operator for binary strings
     */
    public /*sealed*/ interface BinaryLike
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<?>, Field<byte[]>, BinaryLike>,
            org.jooq.Condition
        //permits
        //    BinaryLike
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BinaryLike $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default Field<byte[]> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default BinaryLike $pattern(Field<byte[]> newPattern) { return $arg2(newPattern); }
    }

    /**
     * The <code>BINARY LIKE</code> operator.
     * <p>
     * The LIKE operator for binary strings
     */
    public /*sealed*/ interface BinaryLikeQuantified
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<?>, org.jooq.QuantifiedSelect<? extends Record1<byte[]>>, BinaryLikeQuantified>,
            org.jooq.Condition
        //permits
        //    BinaryLikeQuantified
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BinaryLikeQuantified $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default org.jooq.QuantifiedSelect<? extends Record1<byte[]>> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default BinaryLikeQuantified $pattern(org.jooq.QuantifiedSelect<? extends Record1<byte[]>> newPattern) { return $arg2(newPattern); }
    }

    /**
     * The <code>EQ</code> operator.
     */
    public /*sealed*/ interface TableEq<R extends Record>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Table<R>, TableEq<R>>,
            org.jooq.Condition
        //permits
        //    TableEq
    {}

    /**
     * The <code>EQ</code> operator.
     */
    public /*sealed*/ interface Eq<T>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Eq<T>>,
            CompareCondition<T, Eq<T>>
        //permits
        //    Eq
    {}

    /**
     * The <code>EQ</code> operator.
     */
    public /*sealed*/ interface EqQuantified<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, org.jooq.QuantifiedSelect<? extends Record1<T>>, EqQuantified<T>>,
            org.jooq.Condition
        //permits
        //    EqQuantified
    {}

    /**
     * The <code>EXISTS</code> function.
     */
    public /*sealed*/ interface Exists
        extends
            UOperator1<Select<?>, Exists>,
            org.jooq.Condition
        //permits
        //    Exists
    {
        @NotNull default Select<?> $query() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Exists $query(Select<?> newQuery) { return $arg1(newQuery); }
    }

    /**
     * The <code>GE</code> operator.
     */
    public /*sealed*/ interface Ge<T>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<Field<T>, Ge<T>, Le<T>>,
            CompareCondition<T, Ge<T>>
        //permits
        //    Ge
    {
        @Override
        @CheckReturnValue
        @NotNull default Le<T> $converse() {
            return new org.jooq.impl.Le<>($arg2(), $arg1());
        }
    }

    /**
     * The <code>GE</code> operator.
     */
    public /*sealed*/ interface GeQuantified<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, org.jooq.QuantifiedSelect<? extends Record1<T>>, GeQuantified<T>>,
            org.jooq.Condition
        //permits
        //    GeQuantified
    {}

    /**
     * The <code>GT</code> operator.
     */
    public /*sealed*/ interface Gt<T>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<Field<T>, Gt<T>, Lt<T>>,
            CompareCondition<T, Gt<T>>
        //permits
        //    Gt
    {
        @Override
        @CheckReturnValue
        @NotNull default Lt<T> $converse() {
            return new org.jooq.impl.Lt<>($arg2(), $arg1());
        }
    }

    /**
     * The <code>GT</code> operator.
     */
    public /*sealed*/ interface GtQuantified<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, org.jooq.QuantifiedSelect<? extends Record1<T>>, GtQuantified<T>>,
            org.jooq.Condition
        //permits
        //    GtQuantified
    {}

    /**
     * The <code>IN</code> operator.
     * <p>
     * The subquery must return exactly one field. This is not checked
     * by jOOQ and will result in syntax errors in the database, if not used
     * correctly.
     */
    public /*sealed*/ interface In<T>
        extends
            UOperator2<Field<T>, Select<? extends Record1<T>>, In<T>>,
            org.jooq.Condition
        //permits
        //    In
    {}

    /**
     * The <code>IS DISTINCT FROM</code> operator.
     * <p>
     * The DISTINCT predicate allows for creating NULL safe comparisons where the two operands
     * are tested for non-equality
     */
    public /*sealed*/ interface IsDistinctFrom<T>
        extends
            UCommutativeOperator<Field<T>, IsDistinctFrom<T>>,
            CompareCondition<T, IsDistinctFrom<T>>
        //permits
        //    IsDistinctFrom
    {}

    /**
     * The <code>IS NULL</code> operator.
     */
    public /*sealed*/ interface IsNull
        extends
            UOperator1<Field<?>, IsNull>,
            org.jooq.Condition
        //permits
        //    IsNull
    {
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default IsNull $field(Field<?> newField) { return $arg1(newField); }
    }

    /**
     * The <code>IS NOT DISTINCT FROM</code> operator.
     * <p>
     * The NOT DISTINCT predicate allows for creating NULL safe comparisons where the two
     * operands are tested for equality
     */
    public /*sealed*/ interface IsNotDistinctFrom<T>
        extends
            UCommutativeOperator<Field<T>, IsNotDistinctFrom<T>>,
            CompareCondition<T, IsNotDistinctFrom<T>>
        //permits
        //    IsNotDistinctFrom
    {}

    /**
     * The <code>IS NOT NULL</code> operator.
     */
    public /*sealed*/ interface IsNotNull
        extends
            UOperator1<Field<?>, IsNotNull>,
            org.jooq.Condition
        //permits
        //    IsNotNull
    {
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default IsNotNull $field(Field<?> newField) { return $arg1(newField); }
    }

    /**
     * The <code>LE</code> operator.
     */
    public /*sealed*/ interface Le<T>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<Field<T>, Le<T>, Ge<T>>,
            CompareCondition<T, Le<T>>
        //permits
        //    Le
    {
        @Override
        @CheckReturnValue
        @NotNull default Ge<T> $converse() {
            return new org.jooq.impl.Ge<>($arg2(), $arg1());
        }
    }

    /**
     * The <code>LE</code> operator.
     */
    public /*sealed*/ interface LeQuantified<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, org.jooq.QuantifiedSelect<? extends Record1<T>>, LeQuantified<T>>,
            org.jooq.Condition
        //permits
        //    LeQuantified
    {}

    /**
     * The <code>LIKE</code> operator.
     */
    public /*sealed*/ interface Like
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, Like>,
            Condition
        //permits
        //    Like
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Like $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default Field<String> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default Like $pattern(Field<String> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default Like $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>LIKE</code> operator.
     */
    public /*sealed*/ interface LikeQuantified
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, org.jooq.QuantifiedSelect<? extends Record1<String>>, Character, LikeQuantified>,
            Condition
        //permits
        //    LikeQuantified
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default LikeQuantified $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default org.jooq.QuantifiedSelect<? extends Record1<String>> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default LikeQuantified $pattern(org.jooq.QuantifiedSelect<? extends Record1<String>> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default LikeQuantified $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>LIKE IGNORE CASE</code> operator.
     * <p>
     * Create a condition to case-insensitively pattern-check this field against
     * a value.
     * <p>
     * This translates to <code>this ilike value</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) like lower(value)</code> in all other dialects.
     */
    public /*sealed*/ interface LikeIgnoreCase
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, LikeIgnoreCase>,
            Condition
        //permits
        //    LikeIgnoreCase
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default LikeIgnoreCase $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default Field<String> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default LikeIgnoreCase $pattern(Field<String> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default LikeIgnoreCase $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>LT</code> operator.
     */
    public /*sealed*/ interface Lt<T>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<Field<T>, Lt<T>, Gt<T>>,
            CompareCondition<T, Lt<T>>
        //permits
        //    Lt
    {
        @Override
        @CheckReturnValue
        @NotNull default Gt<T> $converse() {
            return new org.jooq.impl.Gt<>($arg2(), $arg1());
        }
    }

    /**
     * The <code>LT</code> operator.
     */
    public /*sealed*/ interface LtQuantified<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, org.jooq.QuantifiedSelect<? extends Record1<T>>, LtQuantified<T>>,
            org.jooq.Condition
        //permits
        //    LtQuantified
    {}

    /**
     * The <code>NE</code> operator.
     */
    public /*sealed*/ interface TableNe<R extends Record>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Table<R>, TableNe<R>>,
            org.jooq.Condition
        //permits
        //    TableNe
    {}

    /**
     * The <code>NE</code> operator.
     */
    public /*sealed*/ interface Ne<T>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Ne<T>>,
            CompareCondition<T, Ne<T>>
        //permits
        //    Ne
    {}

    /**
     * The <code>NE</code> operator.
     */
    public /*sealed*/ interface NeQuantified<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, org.jooq.QuantifiedSelect<? extends Record1<T>>, NeQuantified<T>>,
            org.jooq.Condition
        //permits
        //    NeQuantified
    {}

    /**
     * The <code>NOT</code> operator.
     */
    public /*sealed*/ interface Not
        extends
            UReturnsNullOnNullInput,
            UOperator1<Condition, Not>,
            org.jooq.Condition
        //permits
        //    Not
    {
        @NotNull default Condition $condition() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Not $condition(Condition newCondition) { return $arg1(newCondition); }
    }

    /**
     * The <code>NOT</code> operator.
     */
    public /*sealed*/ interface NotField
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<Boolean>, NotField>,
            org.jooq.Field<Boolean>
        //permits
        //    NotField
    {
        @NotNull default Field<Boolean> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default NotField $field(Field<Boolean> newField) { return $arg1(newField); }
    }

    /**
     * The <code>NOT BINARY LIKE</code> operator.
     * <p>
     * The NOT LIKE operator for binary strings
     */
    public /*sealed*/ interface NotBinaryLike
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<?>, Field<byte[]>, NotBinaryLike>,
            org.jooq.Condition
        //permits
        //    NotBinaryLike
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default NotBinaryLike $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default Field<byte[]> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default NotBinaryLike $pattern(Field<byte[]> newPattern) { return $arg2(newPattern); }
    }

    /**
     * The <code>NOT BINARY LIKE</code> operator.
     * <p>
     * The NOT LIKE operator for binary strings
     */
    public /*sealed*/ interface NotBinaryLikeQuantified
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<?>, org.jooq.QuantifiedSelect<? extends Record1<byte[]>>, NotBinaryLikeQuantified>,
            org.jooq.Condition
        //permits
        //    NotBinaryLikeQuantified
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default NotBinaryLikeQuantified $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default org.jooq.QuantifiedSelect<? extends Record1<byte[]>> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default NotBinaryLikeQuantified $pattern(org.jooq.QuantifiedSelect<? extends Record1<byte[]>> newPattern) { return $arg2(newPattern); }
    }

    /**
     * The <code>NOT IN</code> operator.
     * <p>
     * The subquery must return exactly one field. This is not checked
     * by jOOQ and will result in syntax errors in the database, if not used
     * correctly.
     * <p>
     * If any of the passed values is <code>NULL</code>, then the
     * condition will be <code>NULL</code> (or <code>false</code>, depending on
     * the dialect) as well. This is standard SQL behaviour.
     */
    public /*sealed*/ interface NotIn<T>
        extends
            UOperator2<Field<T>, Select<? extends Record1<T>>, NotIn<T>>,
            org.jooq.Condition
        //permits
        //    NotIn
    {}

    /**
     * The <code>NOT LIKE</code> operator.
     */
    public /*sealed*/ interface NotLike
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, NotLike>,
            Condition
        //permits
        //    NotLike
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default NotLike $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default Field<String> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default NotLike $pattern(Field<String> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default NotLike $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>NOT LIKE</code> operator.
     */
    public /*sealed*/ interface NotLikeQuantified
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, org.jooq.QuantifiedSelect<? extends Record1<String>>, Character, NotLikeQuantified>,
            Condition
        //permits
        //    NotLikeQuantified
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default NotLikeQuantified $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default org.jooq.QuantifiedSelect<? extends Record1<String>> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default NotLikeQuantified $pattern(org.jooq.QuantifiedSelect<? extends Record1<String>> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default NotLikeQuantified $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>NOT LIKE IGNORE CASE</code> operator.
     * <p>
     * Create a condition to case-insensitively pattern-check this field against
     * a value.
     * <p>
     * This translates to <code>this not ilike value</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) not like lower(value)</code> in all other dialects.
     */
    public /*sealed*/ interface NotLikeIgnoreCase
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, NotLikeIgnoreCase>,
            Condition
        //permits
        //    NotLikeIgnoreCase
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default NotLikeIgnoreCase $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default Field<String> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default NotLikeIgnoreCase $pattern(Field<String> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default NotLikeIgnoreCase $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>NOT SIMILAR TO</code> operator.
     */
    public /*sealed*/ interface NotSimilarTo
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, NotSimilarTo>,
            Condition
        //permits
        //    NotSimilarTo
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default NotSimilarTo $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default Field<String> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default NotSimilarTo $pattern(Field<String> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default NotSimilarTo $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>NOT SIMILAR TO</code> operator.
     */
    public /*sealed*/ interface NotSimilarToQuantified
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, org.jooq.QuantifiedSelect<? extends Record1<String>>, Character, NotSimilarToQuantified>,
            Condition
        //permits
        //    NotSimilarToQuantified
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default NotSimilarToQuantified $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default org.jooq.QuantifiedSelect<? extends Record1<String>> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default NotSimilarToQuantified $pattern(org.jooq.QuantifiedSelect<? extends Record1<String>> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default NotSimilarToQuantified $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>OR</code> operator.
     */
    public /*sealed*/ interface Or
        extends
            UCommutativeOperator<Condition, Or>,
            CombinedCondition<Or>
        //permits
        //    Or
    {}

    /**
     * The <code>SIMILAR TO</code> operator.
     */
    public /*sealed*/ interface SimilarTo
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, SimilarTo>,
            Condition
        //permits
        //    SimilarTo
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default SimilarTo $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default Field<String> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default SimilarTo $pattern(Field<String> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default SimilarTo $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>SIMILAR TO</code> operator.
     */
    public /*sealed*/ interface SimilarToQuantified
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, org.jooq.QuantifiedSelect<? extends Record1<String>>, Character, SimilarToQuantified>,
            Condition
        //permits
        //    SimilarToQuantified
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default SimilarToQuantified $value(Field<?> newValue) { return $arg1(newValue); }
        @NotNull default org.jooq.QuantifiedSelect<? extends Record1<String>> $pattern() { return $arg2(); }
        @CheckReturnValue
        @NotNull default SimilarToQuantified $pattern(org.jooq.QuantifiedSelect<? extends Record1<String>> newPattern) { return $arg2(newPattern); }
        @Nullable default Character $escape() { return $arg3(); }
        @CheckReturnValue
        @NotNull default SimilarToQuantified $escape(Character newEscape) { return $arg3(newEscape); }
    }

    /**
     * The <code>UNIQUE</code> function.
     */
    public /*sealed*/ interface Unique
        extends
            UOperator1<Select<?>, Unique>,
            org.jooq.Condition
        //permits
        //    Unique
    {
        @NotNull default Select<?> $query() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Unique $query(Select<?> newQuery) { return $arg1(newQuery); }
    }

    /**
     * The <code>XOR</code> operator.
     */
    public /*sealed*/ interface Xor
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Condition, Xor>,
            CombinedCondition<Xor>
        //permits
        //    Xor
    {}

    /**
     * The <code>ROW EQ</code> operator.
     */
    public /*sealed*/ interface RowEq<T extends Row>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<T, RowEq<T>>,
            org.jooq.Condition
        //permits
        //    RowEq
    {}

    /**
     * The <code>ROW NE</code> operator.
     */
    public /*sealed*/ interface RowNe<T extends Row>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<T, RowNe<T>>,
            org.jooq.Condition
        //permits
        //    RowNe
    {}

    /**
     * The <code>ROW GT</code> operator.
     */
    public /*sealed*/ interface RowGt<T extends Row>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<T, RowGt<T>, RowLt<T>>,
            org.jooq.Condition
        //permits
        //    RowGt
    {
        @Override
        @CheckReturnValue
        @NotNull default RowLt<T> $converse() {
            return new org.jooq.impl.RowLt<>($arg2(), $arg1());
        }
    }

    /**
     * The <code>ROW GE</code> operator.
     */
    public /*sealed*/ interface RowGe<T extends Row>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<T, RowGe<T>, RowLe<T>>,
            org.jooq.Condition
        //permits
        //    RowGe
    {
        @Override
        @CheckReturnValue
        @NotNull default RowLe<T> $converse() {
            return new org.jooq.impl.RowLe<>($arg2(), $arg1());
        }
    }

    /**
     * The <code>ROW LT</code> operator.
     */
    public /*sealed*/ interface RowLt<T extends Row>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<T, RowLt<T>, RowGt<T>>,
            org.jooq.Condition
        //permits
        //    RowLt
    {
        @Override
        @CheckReturnValue
        @NotNull default RowGt<T> $converse() {
            return new org.jooq.impl.RowGt<>($arg2(), $arg1());
        }
    }

    /**
     * The <code>ROW LE</code> operator.
     */
    public /*sealed*/ interface RowLe<T extends Row>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<T, RowLe<T>, RowGe<T>>,
            org.jooq.Condition
        //permits
        //    RowLe
    {
        @Override
        @CheckReturnValue
        @NotNull default RowGe<T> $converse() {
            return new org.jooq.impl.RowGe<>($arg2(), $arg1());
        }
    }

    /**
     * The <code>IS DOCUMENT</code> operator.
     * <p>
     * Create a condition to check if this field contains XML data.
     */
    public /*sealed*/ interface IsDocument
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<?>, IsDocument>,
            org.jooq.Condition
        //permits
        //    IsDocument
    {
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default IsDocument $field(Field<?> newField) { return $arg1(newField); }
    }

    /**
     * The <code>IS NOT DOCUMENT</code> operator.
     * <p>
     * Create a condition to check if this field does not contain XML data.
     */
    public /*sealed*/ interface IsNotDocument
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<?>, IsNotDocument>,
            org.jooq.Condition
        //permits
        //    IsNotDocument
    {
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default IsNotDocument $field(Field<?> newField) { return $arg1(newField); }
    }

    /**
     * The <code>IS JSON</code> operator.
     * <p>
     * Create a condition to check if this field contains JSON data.
     */
    public /*sealed*/ interface IsJson
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<?>, IsJson>,
            org.jooq.Condition
        //permits
        //    IsJson
    {
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default IsJson $field(Field<?> newField) { return $arg1(newField); }
    }

    /**
     * The <code>IS NOT JSON</code> operator.
     * <p>
     * Create a condition to check if this field does not contain JSON data.
     */
    public /*sealed*/ interface IsNotJson
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<?>, IsNotJson>,
            org.jooq.Condition
        //permits
        //    IsNotJson
    {
        @NotNull default Field<?> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default IsNotJson $field(Field<?> newField) { return $arg1(newField); }
    }

    /**
     * The <code>EXCLUDED</code> function.
     * <p>
     * Provide "EXCLUDED" qualification for a column for use in ON CONFLICT or ON DUPLICATE
     * KEY UPDATE.
     */
    public /*sealed*/ interface Excluded<T>
        extends
            UOperator1<Field<T>, Excluded<T>>,
            org.jooq.Field<T>
        //permits
        //    Excluded
    {

        /**
         * The excluded field.
         */
        @NotNull default Field<T> $field() { return $arg1(); }

        /**
         * The excluded field.
         */
        @CheckReturnValue
        @NotNull default Excluded<T> $field(Field<T> newField) { return $arg1(newField); }
    }

    /**
     * The <code>ROWID</code> operator.
     * <p>
     * Get a <code>table.rowid</code> reference from this table.
     * <p>
     * A rowid value describes the physical location of a row on the disk, which
     * can be used as a replacement for a primary key in some situations -
     * especially within a query, e.g. to self-join a table:
     * <p>
     * <pre><code>
     * -- Emulating this MySQL statement...
     * DELETE FROM x ORDER BY x.y LIMIT 1
     *
     * -- ... in other databases
     * DELETE FROM x
     * WHERE x.rowid IN (
     *   SELECT x.rowid FROM x ORDER BY x.a LIMIT 1
     * )
     * </code></pre>
     * <p>
     * It is <em>not</em> recommended to use <code>rowid</code> values in client
     * applications as actual row identifiers as the database system may move a
     * row to a different physical location at any time, thus changing the rowid
     * value. In general, use primary keys, instead.
     */
    public /*sealed*/ interface QualifiedRowid
        extends
            UOperator1<Table<?>, QualifiedRowid>,
            org.jooq.Field<RowId>
        //permits
        //    QualifiedRowid
    {
        @NotNull default Table<?> $table() { return $arg1(); }
        @CheckReturnValue
        @NotNull default QualifiedRowid $table(Table<?> newTable) { return $arg1(newTable); }
    }

    /**
     * The <code>TABLESAMPLE</code> operator.
     * <p>
     * Get a <code>TABLESAMPLE</code> expression for this table using the default sample
     * method.
     */
    public /*sealed*/ interface SampleTable<R extends Record>
        extends
            UOperator5<Table<R>, Field<? extends Number>, SampleMethod, SampleSizeType, Field<? extends Number>, SampleTable<R>>,
            Table<R>
        //permits
        //    SampleTable
    {
        @NotNull default Table<R> $table() { return $arg1(); }
        @CheckReturnValue
        @NotNull default SampleTable<R> $table(Table<R> newTable) { return $arg1(newTable); }
        @NotNull default Field<? extends Number> $size() { return $arg2(); }
        @CheckReturnValue
        @NotNull default SampleTable<R> $size(Field<? extends Number> newSize) { return $arg2(newSize); }
        @Nullable default SampleMethod $method() { return $arg3(); }
        @CheckReturnValue
        @NotNull default SampleTable<R> $method(SampleMethod newMethod) { return $arg3(newMethod); }
        @Nullable default SampleSizeType $sizeType() { return $arg4(); }
        @CheckReturnValue
        @NotNull default SampleTable<R> $sizeType(SampleSizeType newSizeType) { return $arg4(newSizeType); }
        @Nullable default Field<? extends Number> $seed() { return $arg5(); }
        @CheckReturnValue
        @NotNull default SampleTable<R> $seed(Field<? extends Number> newSeed) { return $arg5(newSeed); }
    }

    /**
     * The <code>ABS</code> function.
     */
    public /*sealed*/ interface Abs<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Abs<T>>,
            org.jooq.Field<T>
        //permits
        //    Abs
    {
        @NotNull default Field<T> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Abs<T> $value(Field<T> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>ACOS</code> function.
     */
    public /*sealed*/ interface Acos
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Acos>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Acos
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Acos $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>ACOSH</code> function.
     */
    public /*sealed*/ interface Acosh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Acosh>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Acosh
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Acosh $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>ACOTH</code> function.
     */
    public /*sealed*/ interface Acoth
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Acoth>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Acoth
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Acoth $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>ADD</code> operator.
     */
    public /*sealed*/ interface Add<T>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Add<T>>,
            org.jooq.Field<T>
        //permits
        //    Add
    {}

    /**
     * The <code>ASIN</code> function.
     */
    public /*sealed*/ interface Asin
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Asin>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Asin
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Asin $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>ASINH</code> function.
     */
    public /*sealed*/ interface Asinh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Asinh>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Asinh
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Asinh $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>ATAN</code> function.
     */
    public /*sealed*/ interface Atan
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Atan>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Atan
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Atan $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>ATAN2</code> function.
     */
    public /*sealed*/ interface Atan2
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<? extends Number>, Field<? extends Number>, Atan2>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Atan2
    {
        @NotNull default Field<? extends Number> $x() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Atan2 $x(Field<? extends Number> newX) { return $arg1(newX); }
        @NotNull default Field<? extends Number> $y() { return $arg2(); }
        @CheckReturnValue
        @NotNull default Atan2 $y(Field<? extends Number> newY) { return $arg2(newY); }
    }

    /**
     * The <code>ATANH</code> function.
     */
    public /*sealed*/ interface Atanh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Atanh>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Atanh
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Atanh $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>BIT AND</code> operator.
     */
    public /*sealed*/ interface BitAnd<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, BitAnd<T>>,
            org.jooq.Field<T>
        //permits
        //    BitAnd
    {}

    /**
     * The <code>BIT COUNT</code> function.
     * <p>
     * Count the number of bits set in a number
     */
    public /*sealed*/ interface BitCount
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, BitCount>,
            org.jooq.Field<Integer>
        //permits
        //    BitCount
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BitCount $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>BIT GET</code> function.
     */
    public /*sealed*/ interface BitGet<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<? extends Number>, BitGet<T>>,
            org.jooq.Field<T>
        //permits
        //    BitGet
    {
        @NotNull default Field<T> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BitGet<T> $value(Field<T> newValue) { return $arg1(newValue); }
        @NotNull default Field<? extends Number> $bit() { return $arg2(); }
        @CheckReturnValue
        @NotNull default BitGet<T> $bit(Field<? extends Number> newBit) { return $arg2(newBit); }
    }

    /**
     * The <code>BIT NAND</code> operator.
     */
    public /*sealed*/ interface BitNand<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, BitNand<T>>,
            org.jooq.Field<T>
        //permits
        //    BitNand
    {}

    /**
     * The <code>BIT NOR</code> operator.
     */
    public /*sealed*/ interface BitNor<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, BitNor<T>>,
            org.jooq.Field<T>
        //permits
        //    BitNor
    {}

    /**
     * The <code>BIT NOT</code> operator.
     */
    public /*sealed*/ interface BitNot<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, BitNot<T>>,
            org.jooq.Field<T>
        //permits
        //    BitNot
    {}

    /**
     * The <code>BIT OR</code> operator.
     */
    public /*sealed*/ interface BitOr<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, BitOr<T>>,
            org.jooq.Field<T>
        //permits
        //    BitOr
    {}

    /**
     * The <code>BIT SET</code> function.
     */
    public /*sealed*/ interface BitSet<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<T>, Field<? extends Number>, Field<T>, BitSet<T>>,
            org.jooq.Field<T>
        //permits
        //    BitSet
    {
        @NotNull default Field<T> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BitSet<T> $value(Field<T> newValue) { return $arg1(newValue); }
        @NotNull default Field<? extends Number> $bit() { return $arg2(); }
        @CheckReturnValue
        @NotNull default BitSet<T> $bit(Field<? extends Number> newBit) { return $arg2(newBit); }
        @Nullable default Field<T> $newValue() { return $arg3(); }
        @CheckReturnValue
        @NotNull default BitSet<T> $newValue(Field<T> newNewValue) { return $arg3(newNewValue); }
    }

    /**
     * The <code>BIT X NOR</code> operator.
     */
    public /*sealed*/ interface BitXNor<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, BitXNor<T>>,
            org.jooq.Field<T>
        //permits
        //    BitXNor
    {}

    /**
     * The <code>BIT XOR</code> operator.
     */
    public /*sealed*/ interface BitXor<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, BitXor<T>>,
            org.jooq.Field<T>
        //permits
        //    BitXor
    {}

    /**
     * The <code>CBRT</code> function.
     */
    public /*sealed*/ interface Cbrt
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Cbrt>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Cbrt
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Cbrt $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>CEIL</code> function.
     * <p>
     * Get the smallest integer value equal or greater to a value.
     */
    public /*sealed*/ interface Ceil<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Ceil<T>>,
            org.jooq.Field<T>
        //permits
        //    Ceil
    {
        @NotNull default Field<T> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Ceil<T> $value(Field<T> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>COS</code> function.
     */
    public /*sealed*/ interface Cos
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Cos>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Cos
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Cos $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>COSH</code> function.
     */
    public /*sealed*/ interface Cosh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Cosh>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Cosh
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Cosh $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>COT</code> function.
     */
    public /*sealed*/ interface Cot
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Cot>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Cot
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Cot $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>COTH</code> function.
     */
    public /*sealed*/ interface Coth
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Coth>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Coth
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Coth $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>DEGREES</code> function.
     * <p>
     * Turn a value in radians to degrees.
     */
    public /*sealed*/ interface Degrees
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Degrees>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Degrees
    {

        /**
         * The value in radians.
         */
        @NotNull default Field<? extends Number> $radians() { return $arg1(); }

        /**
         * The value in radians.
         */
        @CheckReturnValue
        @NotNull default Degrees $radians(Field<? extends Number> newRadians) { return $arg1(newRadians); }
    }

    /**
     * The <code>DIV</code> operator.
     */
    public /*sealed*/ interface Div<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Div<T>>,
            org.jooq.Field<T>
        //permits
        //    Div
    {}

    /**
     * The <code>E</code> function.
     * <p>
     * The E literal (Euler number).
     */
    public /*sealed*/ interface Euler
        extends
            UOperator0<Euler>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Euler
    {}

    /**
     * The <code>EXP</code> function.
     */
    public /*sealed*/ interface Exp
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Exp>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Exp
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Exp $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>FLOOR</code> function.
     * <p>
     * Get the biggest integer value equal or less than a value.
     */
    public /*sealed*/ interface Floor<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Floor<T>>,
            org.jooq.Field<T>
        //permits
        //    Floor
    {
        @NotNull default Field<T> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Floor<T> $value(Field<T> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>LN</code> function.
     * <p>
     * Get the natural logarithm of a value.
     */
    public /*sealed*/ interface Ln
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Ln>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Ln
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Ln $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>LOG</code> function.
     * <p>
     * Get the logarithm of a value for a base.
     */
    public /*sealed*/ interface Log
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<? extends Number>, Field<? extends Number>, Log>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Log
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Log $value(Field<? extends Number> newValue) { return $arg1(newValue); }
        @NotNull default Field<? extends Number> $base() { return $arg2(); }
        @CheckReturnValue
        @NotNull default Log $base(Field<? extends Number> newBase) { return $arg2(newBase); }
    }

    /**
     * The <code>LOG10</code> function.
     * <p>
     * Get the logarithm of a value for base 10.
     */
    public /*sealed*/ interface Log10
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Log10>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Log10
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Log10 $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>MOD</code> operator.
     */
    public /*sealed*/ interface Mod<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<? extends Number>, Mod<T>>,
            org.jooq.Field<T>
        //permits
        //    Mod
    {
        @NotNull default Field<T> $dividend() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Mod<T> $dividend(Field<T> newDividend) { return $arg1(newDividend); }
        @NotNull default Field<? extends Number> $divisor() { return $arg2(); }
        @CheckReturnValue
        @NotNull default Mod<T> $divisor(Field<? extends Number> newDivisor) { return $arg2(newDivisor); }
    }

    /**
     * The <code>MUL</code> operator.
     */
    public /*sealed*/ interface Mul<T>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Mul<T>>,
            org.jooq.Field<T>
        //permits
        //    Mul
    {}

    /**
     * The <code>PI</code> function.
     * <p>
     * The π literal.
     */
    public /*sealed*/ interface Pi
        extends
            UOperator0<Pi>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Pi
    {}

    /**
     * The <code>POWER</code> operator.
     */
    public /*sealed*/ interface Power
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<? extends Number>, Field<? extends Number>, Power>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Power
    {
        @NotNull default Field<? extends Number> $base() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Power $base(Field<? extends Number> newBase) { return $arg1(newBase); }
        @NotNull default Field<? extends Number> $exponent() { return $arg2(); }
        @CheckReturnValue
        @NotNull default Power $exponent(Field<? extends Number> newExponent) { return $arg2(newExponent); }
    }

    /**
     * The <code>RADIANS</code> function.
     * <p>
     * Turn a value in degrees to radians.
     */
    public /*sealed*/ interface Radians
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Radians>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Radians
    {

        /**
         * The value in degrees.
         */
        @NotNull default Field<? extends Number> $degrees() { return $arg1(); }

        /**
         * The value in degrees.
         */
        @CheckReturnValue
        @NotNull default Radians $degrees(Field<? extends Number> newDegrees) { return $arg1(newDegrees); }
    }

    /**
     * The <code>RAND</code> function.
     * <p>
     * Get a random numeric value.
     */
    public /*sealed*/ interface Rand
        extends
            UOperator0<Rand>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Rand
    {}

    /**
     * The <code>ROOT</code> function.
     */
    public /*sealed*/ interface Root
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<? extends Number>, Field<? extends Number>, Root>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Root
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Root $value(Field<? extends Number> newValue) { return $arg1(newValue); }
        @NotNull default Field<? extends Number> $degree() { return $arg2(); }
        @CheckReturnValue
        @NotNull default Root $degree(Field<? extends Number> newDegree) { return $arg2(newDegree); }
    }

    /**
     * The <code>ROUND</code> function.
     * <p>
     * Round a numeric value to the nearest decimal precision.
     */
    public /*sealed*/ interface Round<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<Integer>, Round<T>>,
            org.jooq.Field<T>
        //permits
        //    Round
    {

        /**
         * The number to be rounded.
         */
        @NotNull default Field<T> $value() { return $arg1(); }

        /**
         * The number to be rounded.
         */
        @CheckReturnValue
        @NotNull default Round<T> $value(Field<T> newValue) { return $arg1(newValue); }

        /**
         * The decimals to round to.
         */
        @Nullable default Field<Integer> $decimals() { return $arg2(); }

        /**
         * The decimals to round to.
         */
        @CheckReturnValue
        @NotNull default Round<T> $decimals(Field<Integer> newDecimals) { return $arg2(newDecimals); }
    }

    /**
     * The <code>SHL</code> operator.
     * <p>
     * Left shift all bits in a number
     */
    public /*sealed*/ interface Shl<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<? extends Number>, Shl<T>>,
            org.jooq.Field<T>
        //permits
        //    Shl
    {

        /**
         * The number whose bits to shift left.
         */
        @NotNull default Field<T> $value() { return $arg1(); }

        /**
         * The number whose bits to shift left.
         */
        @CheckReturnValue
        @NotNull default Shl<T> $value(Field<T> newValue) { return $arg1(newValue); }

        /**
         * The number of bits to shift.
         */
        @NotNull default Field<? extends Number> $count() { return $arg2(); }

        /**
         * The number of bits to shift.
         */
        @CheckReturnValue
        @NotNull default Shl<T> $count(Field<? extends Number> newCount) { return $arg2(newCount); }
    }

    /**
     * The <code>SHR</code> operator.
     * <p>
     * Right shift all bits in a number
     */
    public /*sealed*/ interface Shr<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<? extends Number>, Shr<T>>,
            org.jooq.Field<T>
        //permits
        //    Shr
    {

        /**
         * The number whose bits to shift right
         */
        @NotNull default Field<T> $value() { return $arg1(); }

        /**
         * The number whose bits to shift right
         */
        @CheckReturnValue
        @NotNull default Shr<T> $value(Field<T> newValue) { return $arg1(newValue); }

        /**
         * The number of bits to shift.
         */
        @NotNull default Field<? extends Number> $count() { return $arg2(); }

        /**
         * The number of bits to shift.
         */
        @CheckReturnValue
        @NotNull default Shr<T> $count(Field<? extends Number> newCount) { return $arg2(newCount); }
    }

    /**
     * The <code>SIGN</code> function.
     * <p>
     * Get the sign of a number and return it as any of +1, 0, -1.
     */
    public /*sealed*/ interface Sign
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Sign>,
            org.jooq.Field<Integer>
        //permits
        //    Sign
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Sign $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>SIN</code> function.
     */
    public /*sealed*/ interface Sin
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Sin>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Sin
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Sin $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>SINH</code> function.
     */
    public /*sealed*/ interface Sinh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Sinh>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Sinh
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Sinh $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>SQRT</code> function.
     */
    public /*sealed*/ interface Sqrt
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Sqrt>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Sqrt
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Sqrt $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>SQUARE</code> function.
     */
    public /*sealed*/ interface Square<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Square<T>>,
            org.jooq.Field<T>
        //permits
        //    Square
    {
        @NotNull default Field<T> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Square<T> $value(Field<T> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>SUB</code> operator.
     */
    public /*sealed*/ interface Sub<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Sub<T>>,
            org.jooq.Field<T>
        //permits
        //    Sub
    {}

    /**
     * The <code>TAN</code> function.
     */
    public /*sealed*/ interface Tan
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Tan>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Tan
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Tan $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>TANH</code> function.
     */
    public /*sealed*/ interface Tanh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Tanh>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Tanh
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Tanh $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>TAU</code> function.
     * <p>
     * The τ literal, or π, in a better world.
     */
    public /*sealed*/ interface Tau
        extends
            UOperator0<Tau>,
            org.jooq.Field<BigDecimal>
        //permits
        //    Tau
    {}

    /**
     * The <code>TRUNC</code> function.
     * <p>
     * Truncate a number to a given number of decimals.
     */
    public /*sealed*/ interface Trunc<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<Integer>, Trunc<T>>,
            org.jooq.Field<T>
        //permits
        //    Trunc
    {

        /**
         * The number to be truncated
         */
        @NotNull default Field<T> $value() { return $arg1(); }

        /**
         * The number to be truncated
         */
        @CheckReturnValue
        @NotNull default Trunc<T> $value(Field<T> newValue) { return $arg1(newValue); }

        /**
         * The decimals to truncate to.
         */
        @NotNull default Field<Integer> $decimals() { return $arg2(); }

        /**
         * The decimals to truncate to.
         */
        @CheckReturnValue
        @NotNull default Trunc<T> $decimals(Field<Integer> newDecimals) { return $arg2(newDecimals); }
    }

    /**
     * The <code>WIDTH BUCKET</code> function.
     * <p>
     * Divide a range into buckets of equal size.
     */
    public /*sealed*/ interface WidthBucket<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator4<Field<T>, Field<T>, Field<T>, Field<Integer>, WidthBucket<T>>,
            org.jooq.Field<T>
        //permits
        //    WidthBucket
    {

        /**
         * The value to divide into the range.
         */
        @NotNull default Field<T> $field() { return $arg1(); }

        /**
         * The value to divide into the range.
         */
        @CheckReturnValue
        @NotNull default WidthBucket<T> $field(Field<T> newField) { return $arg1(newField); }

        /**
         * The lower bound of the range.
         */
        @NotNull default Field<T> $low() { return $arg2(); }

        /**
         * The lower bound of the range.
         */
        @CheckReturnValue
        @NotNull default WidthBucket<T> $low(Field<T> newLow) { return $arg2(newLow); }

        /**
         * The upper bound of the range.
         */
        @NotNull default Field<T> $high() { return $arg3(); }

        /**
         * The upper bound of the range.
         */
        @CheckReturnValue
        @NotNull default WidthBucket<T> $high(Field<T> newHigh) { return $arg3(newHigh); }

        /**
         * The number of buckets to produce.
         */
        @NotNull default Field<Integer> $buckets() { return $arg4(); }

        /**
         * The number of buckets to produce.
         */
        @CheckReturnValue
        @NotNull default WidthBucket<T> $buckets(Field<Integer> newBuckets) { return $arg4(newBuckets); }
    }

    /**
     * The <code>ASCII</code> function.
     * <p>
     * The ASCII value of a character.
     */
    public /*sealed*/ interface Ascii
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Ascii>,
            org.jooq.Field<Integer>
        //permits
        //    Ascii
    {
        @NotNull default Field<String> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Ascii $string(Field<String> newString) { return $arg1(newString); }
    }

    /**
     * The <code>BIT LENGTH</code> function.
     * <p>
     * The length of a string in bits.
     */
    public /*sealed*/ interface BitLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, BitLength>,
            org.jooq.Field<Integer>
        //permits
        //    BitLength
    {
        @NotNull default Field<String> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BitLength $string(Field<String> newString) { return $arg1(newString); }
    }

    /**
     * The <code>CHAR LENGTH</code> function.
     * <p>
     * The length of a string in characters.
     */
    public /*sealed*/ interface CharLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, CharLength>,
            org.jooq.Field<Integer>
        //permits
        //    CharLength
    {
        @NotNull default Field<String> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default CharLength $string(Field<String> newString) { return $arg1(newString); }
    }

    /**
     * The <code>CHR</code> function.
     */
    public /*sealed*/ interface Chr
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Chr>,
            org.jooq.Field<String>
        //permits
        //    Chr
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Chr $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>CONTAINS</code> operator.
     * <p>
     * Convenience method for {@link Field#like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\') || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).contains(13)</code>
     * <p>
     * If you're using {@link SQLDialect#POSTGRES}, then you can use this method
     * also to express the "ARRAY contains" operator. For example: <pre><code>
     * // Use this expression
     * val(new Integer[] { 1, 2, 3 }).contains(new Integer[] { 1, 2 })
     *
     * // ... to render this SQL
     * ARRAY[1, 2, 3] @&gt; ARRAY[1, 2]
     * </code></pre>
     * <p>
     * Note, this does not correspond to the Oracle Text <code>CONTAINS()</code>
     * function. Refer to {@link OracleDSL#contains(Field, String)} instead.
     */
    public /*sealed*/ interface Contains<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Contains<T>>,
            CompareCondition<T, Contains<T>>
        //permits
        //    Contains
    {
        @NotNull default Field<T> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Contains<T> $value(Field<T> newValue) { return $arg1(newValue); }
        @NotNull default Field<T> $content() { return $arg2(); }
        @CheckReturnValue
        @NotNull default Contains<T> $content(Field<T> newContent) { return $arg2(newContent); }
    }

    /**
     * The <code>CONTAINS IGNORE CASE</code> operator.
     * <p>
     * Convenience method for {@link Field#likeIgnoreCase(String, char)} including
     * proper adding of wildcards and escaping.
     * <p>
     * This translates to
     * <code>this ilike ('%' || escape(value, '\') || '%') escape '\'</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) like lower(('%' || escape(value, '\') || '%') escape '\')</code>
     * in all other dialects.
     */
    public /*sealed*/ interface ContainsIgnoreCase<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, ContainsIgnoreCase<T>>,
            CompareCondition<T, ContainsIgnoreCase<T>>
        //permits
        //    ContainsIgnoreCase
    {
        @NotNull default Field<T> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default ContainsIgnoreCase<T> $value(Field<T> newValue) { return $arg1(newValue); }
        @NotNull default Field<T> $content() { return $arg2(); }
        @CheckReturnValue
        @NotNull default ContainsIgnoreCase<T> $content(Field<T> newContent) { return $arg2(newContent); }
    }

    /**
     * The <code>DIGITS</code> function.
     */
    public /*sealed*/ interface Digits
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Digits>,
            org.jooq.Field<String>
        //permits
        //    Digits
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Digits $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>ENDS WITH</code> operator.
     * <p>
     * Convenience method for {@link Field#like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\')) escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).endsWith(33)</code>
     */
    public /*sealed*/ interface EndsWith<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, EndsWith<T>>,
            CompareCondition<T, EndsWith<T>>
        //permits
        //    EndsWith
    {
        @NotNull default Field<T> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default EndsWith<T> $string(Field<T> newString) { return $arg1(newString); }
        @NotNull default Field<T> $suffix() { return $arg2(); }
        @CheckReturnValue
        @NotNull default EndsWith<T> $suffix(Field<T> newSuffix) { return $arg2(newSuffix); }
    }

    /**
     * The <code>ENDS WITH IGNORE CASE</code> operator.
     * <p>
     * Convenience method for {@link Field#like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>lower(this) like ('%' || lower(escape(value, '\'))) escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).endsWithIgnoreCase(33)</code>
     */
    public /*sealed*/ interface EndsWithIgnoreCase<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, EndsWithIgnoreCase<T>>,
            CompareCondition<T, EndsWithIgnoreCase<T>>
        //permits
        //    EndsWithIgnoreCase
    {
        @NotNull default Field<T> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default EndsWithIgnoreCase<T> $string(Field<T> newString) { return $arg1(newString); }
        @NotNull default Field<T> $suffix() { return $arg2(); }
        @CheckReturnValue
        @NotNull default EndsWithIgnoreCase<T> $suffix(Field<T> newSuffix) { return $arg2(newSuffix); }
    }

    /**
     * The <code>LEFT</code> function.
     * <p>
     * Get the left outermost characters from a string.
     */
    public /*sealed*/ interface Left
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<? extends Number>, Left>,
            org.jooq.Field<String>
        //permits
        //    Left
    {

        /**
         * The string whose characters are extracted.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string whose characters are extracted.
         */
        @CheckReturnValue
        @NotNull default Left $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The number of characters to extract from the string.
         */
        @NotNull default Field<? extends Number> $length() { return $arg2(); }

        /**
         * The number of characters to extract from the string.
         */
        @CheckReturnValue
        @NotNull default Left $length(Field<? extends Number> newLength) { return $arg2(newLength); }
    }

    /**
     * The <code>LOWER</code> function.
     * <p>
     * Turn a string into lower case.
     */
    public /*sealed*/ interface Lower
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Lower>,
            org.jooq.Field<String>
        //permits
        //    Lower
    {
        @NotNull default Field<String> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Lower $string(Field<String> newString) { return $arg1(newString); }
    }

    /**
     * The <code>LPAD</code> function.
     * <p>
     * Left-pad a string with a character (whitespace as default) for a number of times.
     */
    public /*sealed*/ interface Lpad
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<? extends Number>, Field<String>, Lpad>,
            org.jooq.Field<String>
        //permits
        //    Lpad
    {

        /**
         * The string to be padded.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to be padded.
         */
        @CheckReturnValue
        @NotNull default Lpad $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The maximum length to pad the string to.
         */
        @NotNull default Field<? extends Number> $length() { return $arg2(); }

        /**
         * The maximum length to pad the string to.
         */
        @CheckReturnValue
        @NotNull default Lpad $length(Field<? extends Number> newLength) { return $arg2(newLength); }

        /**
         * The padding character, if different from whitespace
         */
        @Nullable default Field<String> $character() { return $arg3(); }

        /**
         * The padding character, if different from whitespace
         */
        @CheckReturnValue
        @NotNull default Lpad $character(Field<String> newCharacter) { return $arg3(newCharacter); }
    }

    /**
     * The <code>LTRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from the left side of a string.
     */
    public /*sealed*/ interface Ltrim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, Ltrim>,
            org.jooq.Field<String>
        //permits
        //    Ltrim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to be trimmed.
         */
        @CheckReturnValue
        @NotNull default Ltrim $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The characters to be removed.
         */
        @Nullable default Field<String> $characters() { return $arg2(); }

        /**
         * The characters to be removed.
         */
        @CheckReturnValue
        @NotNull default Ltrim $characters(Field<String> newCharacters) { return $arg2(newCharacters); }
    }

    /**
     * The <code>MD5</code> function.
     * <p>
     * Calculate an MD5 hash from a string.
     */
    public /*sealed*/ interface Md5
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Md5>,
            org.jooq.Field<String>
        //permits
        //    Md5
    {
        @NotNull default Field<String> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Md5 $string(Field<String> newString) { return $arg1(newString); }
    }

    /**
     * The <code>OCTET LENGTH</code> function.
     * <p>
     * The length of a string in octets.
     */
    public /*sealed*/ interface OctetLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, OctetLength>,
            org.jooq.Field<Integer>
        //permits
        //    OctetLength
    {
        @NotNull default Field<String> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default OctetLength $string(Field<String> newString) { return $arg1(newString); }
    }

    /**
     * The <code>OVERLAY</code> function.
     * <p>
     * Place a string on top of another string, replacing the original contents.
     */
    public /*sealed*/ interface Overlay
        extends
            UReturnsNullOnNullInput,
            UOperator4<Field<String>, Field<String>, Field<? extends Number>, Field<? extends Number>, Overlay>,
            org.jooq.Field<String>
        //permits
        //    Overlay
    {

        /**
         * The original string on top of which the overlay is placed.
         */
        @NotNull default Field<String> $in() { return $arg1(); }

        /**
         * The original string on top of which the overlay is placed.
         */
        @CheckReturnValue
        @NotNull default Overlay $in(Field<String> newIn) { return $arg1(newIn); }

        /**
         * The string that is being placed on top of the other string.
         */
        @NotNull default Field<String> $placing() { return $arg2(); }

        /**
         * The string that is being placed on top of the other string.
         */
        @CheckReturnValue
        @NotNull default Overlay $placing(Field<String> newPlacing) { return $arg2(newPlacing); }

        /**
         * The start index (1-based) starting from where the overlay is placed.
         */
        @NotNull default Field<? extends Number> $startIndex() { return $arg3(); }

        /**
         * The start index (1-based) starting from where the overlay is placed.
         */
        @CheckReturnValue
        @NotNull default Overlay $startIndex(Field<? extends Number> newStartIndex) { return $arg3(newStartIndex); }

        /**
         * The length in the original string that will be replaced, if different from the overlay length.
         */
        @Nullable default Field<? extends Number> $length() { return $arg4(); }

        /**
         * The length in the original string that will be replaced, if different from the overlay length.
         */
        @CheckReturnValue
        @NotNull default Overlay $length(Field<? extends Number> newLength) { return $arg4(newLength); }
    }

    /**
     * The <code>POSITION</code> function.
     * <p>
     * Search the position (1-based) of a substring in another string.
     */
    public /*sealed*/ interface Position
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<? extends Number>, Position>,
            org.jooq.Field<Integer>
        //permits
        //    Position
    {

        /**
         * The string in which to search the substring.
         */
        @NotNull default Field<String> $in() { return $arg1(); }

        /**
         * The string in which to search the substring.
         */
        @CheckReturnValue
        @NotNull default Position $in(Field<String> newIn) { return $arg1(newIn); }

        /**
         * The substring to search for.
         */
        @NotNull default Field<String> $search() { return $arg2(); }

        /**
         * The substring to search for.
         */
        @CheckReturnValue
        @NotNull default Position $search(Field<String> newSearch) { return $arg2(newSearch); }

        /**
         * The start index (1-based) from which to start looking for the substring.
         */
        @Nullable default Field<? extends Number> $startIndex() { return $arg3(); }

        /**
         * The start index (1-based) from which to start looking for the substring.
         */
        @CheckReturnValue
        @NotNull default Position $startIndex(Field<? extends Number> newStartIndex) { return $arg3(newStartIndex); }
    }

    /**
     * The <code>REPEAT</code> function.
     * <p>
     * Repeat a string a number of times.
     */
    public /*sealed*/ interface Repeat
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<? extends Number>, Repeat>,
            org.jooq.Field<String>
        //permits
        //    Repeat
    {

        /**
         * The string to be repeated.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to be repeated.
         */
        @CheckReturnValue
        @NotNull default Repeat $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The number of times to repeat the string.
         */
        @NotNull default Field<? extends Number> $count() { return $arg2(); }

        /**
         * The number of times to repeat the string.
         */
        @CheckReturnValue
        @NotNull default Repeat $count(Field<? extends Number> newCount) { return $arg2(newCount); }
    }

    /**
     * The <code>REPLACE</code> function.
     * <p>
     * Replace all occurrences of a substring in another string.
     */
    public /*sealed*/ interface Replace
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<String>, Replace>,
            org.jooq.Field<String>
        //permits
        //    Replace
    {

        /**
         * The string in which to replace contents.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string in which to replace contents.
         */
        @CheckReturnValue
        @NotNull default Replace $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The substring to search for.
         */
        @NotNull default Field<String> $search() { return $arg2(); }

        /**
         * The substring to search for.
         */
        @CheckReturnValue
        @NotNull default Replace $search(Field<String> newSearch) { return $arg2(newSearch); }

        /**
         * The replacement for each substring, if not empty.
         */
        @Nullable default Field<String> $replace() { return $arg3(); }

        /**
         * The replacement for each substring, if not empty.
         */
        @CheckReturnValue
        @NotNull default Replace $replace(Field<String> newReplace) { return $arg3(newReplace); }
    }

    /**
     * The <code>REVERSE</code> function.
     * <p>
     * Reverse a string.
     */
    public /*sealed*/ interface Reverse
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Reverse>,
            org.jooq.Field<String>
        //permits
        //    Reverse
    {
        @NotNull default Field<String> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Reverse $string(Field<String> newString) { return $arg1(newString); }
    }

    /**
     * The <code>RIGHT</code> function.
     * <p>
     * Get the right outermost characters from a string.
     */
    public /*sealed*/ interface Right
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<? extends Number>, Right>,
            org.jooq.Field<String>
        //permits
        //    Right
    {

        /**
         * The string whose characters are extracted.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string whose characters are extracted.
         */
        @CheckReturnValue
        @NotNull default Right $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The number of characters to extract from the string.
         */
        @NotNull default Field<? extends Number> $length() { return $arg2(); }

        /**
         * The number of characters to extract from the string.
         */
        @CheckReturnValue
        @NotNull default Right $length(Field<? extends Number> newLength) { return $arg2(newLength); }
    }

    /**
     * The <code>RPAD</code> function.
     * <p>
     * Right-pad a string with a character (whitespace as default) for a number of times.
     */
    public /*sealed*/ interface Rpad
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<? extends Number>, Field<String>, Rpad>,
            org.jooq.Field<String>
        //permits
        //    Rpad
    {

        /**
         * The string to be padded.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to be padded.
         */
        @CheckReturnValue
        @NotNull default Rpad $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The maximum length to pad the string to.
         */
        @NotNull default Field<? extends Number> $length() { return $arg2(); }

        /**
         * The maximum length to pad the string to.
         */
        @CheckReturnValue
        @NotNull default Rpad $length(Field<? extends Number> newLength) { return $arg2(newLength); }

        /**
         * The padding character, if different from whitespace
         */
        @Nullable default Field<String> $character() { return $arg3(); }

        /**
         * The padding character, if different from whitespace
         */
        @CheckReturnValue
        @NotNull default Rpad $character(Field<String> newCharacter) { return $arg3(newCharacter); }
    }

    /**
     * The <code>RTRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from the right side of a string.
     */
    public /*sealed*/ interface Rtrim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, Rtrim>,
            org.jooq.Field<String>
        //permits
        //    Rtrim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to be trimmed.
         */
        @CheckReturnValue
        @NotNull default Rtrim $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The characters to be removed.
         */
        @Nullable default Field<String> $characters() { return $arg2(); }

        /**
         * The characters to be removed.
         */
        @CheckReturnValue
        @NotNull default Rtrim $characters(Field<String> newCharacters) { return $arg2(newCharacters); }
    }

    /**
     * The <code>SPACE</code> function.
     * <p>
     * Get a string of spaces of a given length.
     */
    public /*sealed*/ interface Space
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Space>,
            org.jooq.Field<String>
        //permits
        //    Space
    {

        /**
         * The number of spaces to produce.
         */
        @NotNull default Field<? extends Number> $count() { return $arg1(); }

        /**
         * The number of spaces to produce.
         */
        @CheckReturnValue
        @NotNull default Space $count(Field<? extends Number> newCount) { return $arg1(newCount); }
    }

    /**
     * The <code>SPLIT PART</code> function.
     * <p>
     * Split a string into tokens, and retrieve the nth token.
     */
    public /*sealed*/ interface SplitPart
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<? extends Number>, SplitPart>,
            org.jooq.Field<String>
        //permits
        //    SplitPart
    {

        /**
         * The string to be split into parts.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to be split into parts.
         */
        @CheckReturnValue
        @NotNull default SplitPart $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The delimiter used for splitting.
         */
        @NotNull default Field<String> $delimiter() { return $arg2(); }

        /**
         * The delimiter used for splitting.
         */
        @CheckReturnValue
        @NotNull default SplitPart $delimiter(Field<String> newDelimiter) { return $arg2(newDelimiter); }

        /**
         * The token number (1-based).
         */
        @NotNull default Field<? extends Number> $n() { return $arg3(); }

        /**
         * The token number (1-based).
         */
        @CheckReturnValue
        @NotNull default SplitPart $n(Field<? extends Number> newN) { return $arg3(newN); }
    }

    /**
     * The <code>STARTS WITH</code> operator.
     * <p>
     * Convenience method for {@link Field#like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like (escape(value, '\') || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).startsWith(11)</code>
     */
    public /*sealed*/ interface StartsWith<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, StartsWith<T>>,
            CompareCondition<T, StartsWith<T>>
        //permits
        //    StartsWith
    {
        @NotNull default Field<T> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default StartsWith<T> $string(Field<T> newString) { return $arg1(newString); }
        @NotNull default Field<T> $prefix() { return $arg2(); }
        @CheckReturnValue
        @NotNull default StartsWith<T> $prefix(Field<T> newPrefix) { return $arg2(newPrefix); }
    }

    /**
     * The <code>STARTS WITH IGNORE CASE</code> operator.
     * <p>
     * Convenience method for {@link Field#like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>lower(this) like (lower(escape(value, '\')) || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).startsWithIgnoreCase(11)</code>
     */
    public /*sealed*/ interface StartsWithIgnoreCase<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, StartsWithIgnoreCase<T>>,
            CompareCondition<T, StartsWithIgnoreCase<T>>
        //permits
        //    StartsWithIgnoreCase
    {
        @NotNull default Field<T> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default StartsWithIgnoreCase<T> $string(Field<T> newString) { return $arg1(newString); }
        @NotNull default Field<T> $prefix() { return $arg2(); }
        @CheckReturnValue
        @NotNull default StartsWithIgnoreCase<T> $prefix(Field<T> newPrefix) { return $arg2(newPrefix); }
    }

    /**
     * The <code>SUBSTRING</code> function.
     * <p>
     * Get a substring of a string, from a given position.
     */
    public /*sealed*/ interface Substring
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<? extends Number>, Field<? extends Number>, Substring>,
            org.jooq.Field<String>
        //permits
        //    Substring
    {

        /**
         * The string from which to get the substring.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string from which to get the substring.
         */
        @CheckReturnValue
        @NotNull default Substring $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The position (1-based) from which to get the substring.
         */
        @NotNull default Field<? extends Number> $startingPosition() { return $arg2(); }

        /**
         * The position (1-based) from which to get the substring.
         */
        @CheckReturnValue
        @NotNull default Substring $startingPosition(Field<? extends Number> newStartingPosition) { return $arg2(newStartingPosition); }

        /**
         * The maximum length of the substring.
         */
        @Nullable default Field<? extends Number> $length() { return $arg3(); }

        /**
         * The maximum length of the substring.
         */
        @CheckReturnValue
        @NotNull default Substring $length(Field<? extends Number> newLength) { return $arg3(newLength); }
    }

    /**
     * The <code>SUBSTRING INDEX</code> function.
     * <p>
     * Get a substring of a string, from the beginning until the nth occurrence of a substring.
     */
    public /*sealed*/ interface SubstringIndex
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<? extends Number>, SubstringIndex>,
            org.jooq.Field<String>
        //permits
        //    SubstringIndex
    {

        /**
         * The string from which to get the substring.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string from which to get the substring.
         */
        @CheckReturnValue
        @NotNull default SubstringIndex $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The delimiter.
         */
        @NotNull default Field<String> $delimiter() { return $arg2(); }

        /**
         * The delimiter.
         */
        @CheckReturnValue
        @NotNull default SubstringIndex $delimiter(Field<String> newDelimiter) { return $arg2(newDelimiter); }

        /**
         * The number of occurrences of the delimiter.
         */
        @NotNull default Field<? extends Number> $n() { return $arg3(); }

        /**
         * The number of occurrences of the delimiter.
         */
        @CheckReturnValue
        @NotNull default SubstringIndex $n(Field<? extends Number> newN) { return $arg3(newN); }
    }

    /**
     * The <code>TO CHAR</code> function.
     * <p>
     * Format an arbitrary value as a string.
     */
    public /*sealed*/ interface ToChar
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<?>, Field<String>, ToChar>,
            org.jooq.Field<String>
        //permits
        //    ToChar
    {

        /**
         * The value to be formatted.
         */
        @NotNull default Field<?> $value() { return $arg1(); }

        /**
         * The value to be formatted.
         */
        @CheckReturnValue
        @NotNull default ToChar $value(Field<?> newValue) { return $arg1(newValue); }

        /**
         * The vendor-specific formatting string.
         */
        @Nullable default Field<String> $formatMask() { return $arg2(); }

        /**
         * The vendor-specific formatting string.
         */
        @CheckReturnValue
        @NotNull default ToChar $formatMask(Field<String> newFormatMask) { return $arg2(newFormatMask); }
    }

    /**
     * The <code>TO DATE</code> function.
     * <p>
     * Parse a string-formatted date value to a date.
     */
    public /*sealed*/ interface ToDate
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, ToDate>,
            org.jooq.Field<Date>
        //permits
        //    ToDate
    {

        /**
         * The formatted DATE value.
         */
        @NotNull default Field<String> $value() { return $arg1(); }

        /**
         * The formatted DATE value.
         */
        @CheckReturnValue
        @NotNull default ToDate $value(Field<String> newValue) { return $arg1(newValue); }

        /**
         * The vendor-specific formatting string.
         */
        @NotNull default Field<String> $formatMask() { return $arg2(); }

        /**
         * The vendor-specific formatting string.
         */
        @CheckReturnValue
        @NotNull default ToDate $formatMask(Field<String> newFormatMask) { return $arg2(newFormatMask); }
    }

    /**
     * The <code>TO HEX</code> function.
     * <p>
     * Format a number to its hex value.
     */
    public /*sealed*/ interface ToHex
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, ToHex>,
            org.jooq.Field<String>
        //permits
        //    ToHex
    {
        @NotNull default Field<? extends Number> $value() { return $arg1(); }
        @CheckReturnValue
        @NotNull default ToHex $value(Field<? extends Number> newValue) { return $arg1(newValue); }
    }

    /**
     * The <code>TO TIMESTAMP</code> function.
     * <p>
     * Parse a string-formatted timestamp value to a timestamp.
     */
    public /*sealed*/ interface ToTimestamp
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, ToTimestamp>,
            org.jooq.Field<Timestamp>
        //permits
        //    ToTimestamp
    {

        /**
         * The formatted TIMESTAMP value.
         */
        @NotNull default Field<String> $value() { return $arg1(); }

        /**
         * The formatted TIMESTAMP value.
         */
        @CheckReturnValue
        @NotNull default ToTimestamp $value(Field<String> newValue) { return $arg1(newValue); }

        /**
         * The vendor-specific formatting string.
         */
        @NotNull default Field<String> $formatMask() { return $arg2(); }

        /**
         * The vendor-specific formatting string.
         */
        @CheckReturnValue
        @NotNull default ToTimestamp $formatMask(Field<String> newFormatMask) { return $arg2(newFormatMask); }
    }

    /**
     * The <code>TRANSLATE</code> function.
     * <p>
     * Translate a set of characters to another set of characters in a string.
     */
    public /*sealed*/ interface Translate
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<String>, Translate>,
            org.jooq.Field<String>
        //permits
        //    Translate
    {

        /**
         * The string to translate.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to translate.
         */
        @CheckReturnValue
        @NotNull default Translate $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The set of source characters.
         */
        @NotNull default Field<String> $from() { return $arg2(); }

        /**
         * The set of source characters.
         */
        @CheckReturnValue
        @NotNull default Translate $from(Field<String> newFrom) { return $arg2(newFrom); }

        /**
         * The set of target characters, matched with source characters by position.
         */
        @NotNull default Field<String> $to() { return $arg3(); }

        /**
         * The set of target characters, matched with source characters by position.
         */
        @CheckReturnValue
        @NotNull default Translate $to(Field<String> newTo) { return $arg3(newTo); }
    }

    /**
     * The <code>TRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from both sides of a string.
     */
    public /*sealed*/ interface Trim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, Trim>,
            org.jooq.Field<String>
        //permits
        //    Trim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to be trimmed.
         */
        @CheckReturnValue
        @NotNull default Trim $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The characters to be removed.
         */
        @Nullable default Field<String> $characters() { return $arg2(); }

        /**
         * The characters to be removed.
         */
        @CheckReturnValue
        @NotNull default Trim $characters(Field<String> newCharacters) { return $arg2(newCharacters); }
    }

    /**
     * The <code>UPPER</code> function.
     * <p>
     * Turn a string into upper case.
     */
    public /*sealed*/ interface Upper
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Upper>,
            org.jooq.Field<String>
        //permits
        //    Upper
    {
        @NotNull default Field<String> $string() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Upper $string(Field<String> newString) { return $arg1(newString); }
    }

    /**
     * The <code>UUID</code> function.
     * <p>
     * Generate a random UUID.
     */
    public /*sealed*/ interface Uuid
        extends
            UOperator0<Uuid>,
            org.jooq.Field<UUID>
        //permits
        //    Uuid
    {}

    /**
     * The <code>BIN TO UUID</code> function.
     * <p>
     * Convert a {@link SQLDataType#BINARY} representation of a UUID to a {@link SQLDataType#UUID}
     * representation.
     */
    public /*sealed*/ interface BinToUuid
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<byte[]>, BinToUuid>,
            org.jooq.Field<UUID>
        //permits
        //    BinToUuid
    {
        @NotNull default Field<byte[]> $bytes() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BinToUuid $bytes(Field<byte[]> newBytes) { return $arg1(newBytes); }
    }

    /**
     * The <code>UUID TO BIN</code> function.
     * <p>
     * Convert a {@link SQLDataType#BINARY} representation of a UUID to a {@link SQLDataType#UUID}
     * representation.
     */
    public /*sealed*/ interface UuidToBin
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<UUID>, UuidToBin>,
            org.jooq.Field<byte[]>
        //permits
        //    UuidToBin
    {
        @NotNull default Field<UUID> $uuid() { return $arg1(); }
        @CheckReturnValue
        @NotNull default UuidToBin $uuid(Field<UUID> newUuid) { return $arg1(newUuid); }
    }

    /**
     * The <code>BINARY BIT LENGTH</code> function.
     * <p>
     * The length of a binary string in bits.
     */
    public /*sealed*/ interface BinaryBitLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<byte[]>, BinaryBitLength>,
            org.jooq.Field<Integer>
        //permits
        //    BinaryBitLength
    {
        @NotNull default Field<byte[]> $bytes() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BinaryBitLength $bytes(Field<byte[]> newBytes) { return $arg1(newBytes); }
    }

    /**
     * The <code>BINARY CONCAT</code> function.
     * <p>
     * The concatenation of binary strings.
     */
    public /*sealed*/ interface BinaryConcat
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<byte[]>, Field<byte[]>, BinaryConcat>,
            org.jooq.Field<byte[]>
        //permits
        //    BinaryConcat
    {

        /**
         * The first binary string.
         */
        @NotNull default Field<byte[]> $bytes1() { return $arg1(); }

        /**
         * The first binary string.
         */
        @CheckReturnValue
        @NotNull default BinaryConcat $bytes1(Field<byte[]> newBytes1) { return $arg1(newBytes1); }

        /**
         * The second binary string.
         */
        @NotNull default Field<byte[]> $bytes2() { return $arg2(); }

        /**
         * The second binary string.
         */
        @CheckReturnValue
        @NotNull default BinaryConcat $bytes2(Field<byte[]> newBytes2) { return $arg2(newBytes2); }
    }

    /**
     * The <code>BINARY LENGTH</code> function.
     * <p>
     * The length of a binary string in bytes.
     */
    public /*sealed*/ interface BinaryLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<byte[]>, BinaryLength>,
            org.jooq.Field<Integer>
        //permits
        //    BinaryLength
    {
        @NotNull default Field<byte[]> $bytes() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BinaryLength $bytes(Field<byte[]> newBytes) { return $arg1(newBytes); }
    }

    /**
     * The <code>BINARY LTRIM</code> function.
     * <p>
     * Trim bytes from the left side of a binary string.
     */
    public /*sealed*/ interface BinaryLtrim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<byte[]>, Field<byte[]>, BinaryLtrim>,
            org.jooq.Field<byte[]>
        //permits
        //    BinaryLtrim
    {

        /**
         * The binary string to be trimmed.
         */
        @NotNull default Field<byte[]> $bytes() { return $arg1(); }

        /**
         * The binary string to be trimmed.
         */
        @CheckReturnValue
        @NotNull default BinaryLtrim $bytes(Field<byte[]> newBytes) { return $arg1(newBytes); }

        /**
         * The binary characters to be removed.
         */
        @NotNull default Field<byte[]> $characters() { return $arg2(); }

        /**
         * The binary characters to be removed.
         */
        @CheckReturnValue
        @NotNull default BinaryLtrim $characters(Field<byte[]> newCharacters) { return $arg2(newCharacters); }
    }

    /**
     * The <code>BINARY MD5</code> function.
     * <p>
     * Calculate an MD5 hash from a binary string.
     */
    public /*sealed*/ interface BinaryMd5
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<byte[]>, BinaryMd5>,
            org.jooq.Field<byte[]>
        //permits
        //    BinaryMd5
    {
        @NotNull default Field<byte[]> $bytes() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BinaryMd5 $bytes(Field<byte[]> newBytes) { return $arg1(newBytes); }
    }

    /**
     * The <code>BINARY OCTET LENGTH</code> function.
     * <p>
     * The length of a binary string in octets.
     */
    public /*sealed*/ interface BinaryOctetLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<byte[]>, BinaryOctetLength>,
            org.jooq.Field<Integer>
        //permits
        //    BinaryOctetLength
    {
        @NotNull default Field<byte[]> $bytes() { return $arg1(); }
        @CheckReturnValue
        @NotNull default BinaryOctetLength $bytes(Field<byte[]> newBytes) { return $arg1(newBytes); }
    }

    /**
     * The <code>BINARY OVERLAY</code> function.
     * <p>
     * Place a binary string on top of another binary string, replacing the original contents.
     */
    public /*sealed*/ interface BinaryOverlay
        extends
            UReturnsNullOnNullInput,
            UOperator4<Field<byte[]>, Field<byte[]>, Field<? extends Number>, Field<? extends Number>, BinaryOverlay>,
            org.jooq.Field<byte[]>
        //permits
        //    BinaryOverlay
    {

        /**
         * The original binary string on top of which the overlay is placed.
         */
        @NotNull default Field<byte[]> $in() { return $arg1(); }

        /**
         * The original binary string on top of which the overlay is placed.
         */
        @CheckReturnValue
        @NotNull default BinaryOverlay $in(Field<byte[]> newIn) { return $arg1(newIn); }

        /**
         * The binary string that is being placed on top of the other binary string.
         */
        @NotNull default Field<byte[]> $placing() { return $arg2(); }

        /**
         * The binary string that is being placed on top of the other binary string.
         */
        @CheckReturnValue
        @NotNull default BinaryOverlay $placing(Field<byte[]> newPlacing) { return $arg2(newPlacing); }

        /**
         * The start index (1-based) starting from where the overlay is placed.
         */
        @NotNull default Field<? extends Number> $startIndex() { return $arg3(); }

        /**
         * The start index (1-based) starting from where the overlay is placed.
         */
        @CheckReturnValue
        @NotNull default BinaryOverlay $startIndex(Field<? extends Number> newStartIndex) { return $arg3(newStartIndex); }

        /**
         * The length in the original string that will be replaced, if different from the overlay length.
         */
        @Nullable default Field<? extends Number> $length() { return $arg4(); }

        /**
         * The length in the original string that will be replaced, if different from the overlay length.
         */
        @CheckReturnValue
        @NotNull default BinaryOverlay $length(Field<? extends Number> newLength) { return $arg4(newLength); }
    }

    /**
     * The <code>BINARY POSITION</code> function.
     * <p>
     * Search the position (1-based) of a substring in another string.
     */
    public /*sealed*/ interface BinaryPosition
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<byte[]>, Field<byte[]>, Field<? extends Number>, BinaryPosition>,
            org.jooq.Field<Integer>
        //permits
        //    BinaryPosition
    {

        /**
         * The string in which to search the substring.
         */
        @NotNull default Field<byte[]> $in() { return $arg1(); }

        /**
         * The string in which to search the substring.
         */
        @CheckReturnValue
        @NotNull default BinaryPosition $in(Field<byte[]> newIn) { return $arg1(newIn); }

        /**
         * The substring to search for.
         */
        @NotNull default Field<byte[]> $search() { return $arg2(); }

        /**
         * The substring to search for.
         */
        @CheckReturnValue
        @NotNull default BinaryPosition $search(Field<byte[]> newSearch) { return $arg2(newSearch); }

        /**
         * The start index (1-based) from which to start looking for the substring.
         */
        @Nullable default Field<? extends Number> $startIndex() { return $arg3(); }

        /**
         * The start index (1-based) from which to start looking for the substring.
         */
        @CheckReturnValue
        @NotNull default BinaryPosition $startIndex(Field<? extends Number> newStartIndex) { return $arg3(newStartIndex); }
    }

    /**
     * The <code>BINARY RTRIM</code> function.
     * <p>
     * Trim bytes from the right side of a binary string.
     */
    public /*sealed*/ interface BinaryRtrim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<byte[]>, Field<byte[]>, BinaryRtrim>,
            org.jooq.Field<byte[]>
        //permits
        //    BinaryRtrim
    {

        /**
         * The binary string to be trimmed.
         */
        @NotNull default Field<byte[]> $bytes() { return $arg1(); }

        /**
         * The binary string to be trimmed.
         */
        @CheckReturnValue
        @NotNull default BinaryRtrim $bytes(Field<byte[]> newBytes) { return $arg1(newBytes); }

        /**
         * The characters to be removed.
         */
        @NotNull default Field<byte[]> $characters() { return $arg2(); }

        /**
         * The characters to be removed.
         */
        @CheckReturnValue
        @NotNull default BinaryRtrim $characters(Field<byte[]> newCharacters) { return $arg2(newCharacters); }
    }

    /**
     * The <code>BINARY SUBSTRING</code> function.
     * <p>
     * Get a substring of a binary string, from a given position.
     */
    public /*sealed*/ interface BinarySubstring
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<byte[]>, Field<? extends Number>, Field<? extends Number>, BinarySubstring>,
            org.jooq.Field<byte[]>
        //permits
        //    BinarySubstring
    {

        /**
         * The binary string from which to get the substring.
         */
        @NotNull default Field<byte[]> $string() { return $arg1(); }

        /**
         * The binary string from which to get the substring.
         */
        @CheckReturnValue
        @NotNull default BinarySubstring $string(Field<byte[]> newString) { return $arg1(newString); }

        /**
         * The position (1-based) from which to get the substring.
         */
        @NotNull default Field<? extends Number> $startingPosition() { return $arg2(); }

        /**
         * The position (1-based) from which to get the substring.
         */
        @CheckReturnValue
        @NotNull default BinarySubstring $startingPosition(Field<? extends Number> newStartingPosition) { return $arg2(newStartingPosition); }

        /**
         * The maximum length of the substring.
         */
        @Nullable default Field<? extends Number> $length() { return $arg3(); }

        /**
         * The maximum length of the substring.
         */
        @CheckReturnValue
        @NotNull default BinarySubstring $length(Field<? extends Number> newLength) { return $arg3(newLength); }
    }

    /**
     * The <code>BINARY TRIM</code> function.
     * <p>
     * Trim characters from both sides of a string.
     */
    public /*sealed*/ interface BinaryTrim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<byte[]>, Field<byte[]>, BinaryTrim>,
            org.jooq.Field<byte[]>
        //permits
        //    BinaryTrim
    {

        /**
         * The binary string to be trimmed.
         */
        @NotNull default Field<byte[]> $bytes() { return $arg1(); }

        /**
         * The binary string to be trimmed.
         */
        @CheckReturnValue
        @NotNull default BinaryTrim $bytes(Field<byte[]> newBytes) { return $arg1(newBytes); }

        /**
         * The characters to be removed.
         */
        @NotNull default Field<byte[]> $characters() { return $arg2(); }

        /**
         * The characters to be removed.
         */
        @CheckReturnValue
        @NotNull default BinaryTrim $characters(Field<byte[]> newCharacters) { return $arg2(newCharacters); }
    }

    /**
     * The <code>DATE ADD</code> function.
     * <p>
     * Add an interval to a date.
     */
    public /*sealed*/ interface DateAdd<T>
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<T>, Field<? extends Number>, DatePart, DateAdd<T>>,
            org.jooq.Field<T>
        //permits
        //    DateAdd
    {

        /**
         * The date to add an interval to
         */
        @NotNull default Field<T> $date() { return $arg1(); }

        /**
         * The date to add an interval to
         */
        @CheckReturnValue
        @NotNull default DateAdd<T> $date(Field<T> newDate) { return $arg1(newDate); }

        /**
         * The interval to add to the date
         */
        @NotNull default Field<? extends Number> $interval() { return $arg2(); }

        /**
         * The interval to add to the date
         */
        @CheckReturnValue
        @NotNull default DateAdd<T> $interval(Field<? extends Number> newInterval) { return $arg2(newInterval); }

        /**
         * The date part describing the interval
         */
        @Nullable default DatePart $datePart() { return $arg3(); }

        /**
         * The date part describing the interval
         */
        @CheckReturnValue
        @NotNull default DateAdd<T> $datePart(DatePart newDatePart) { return $arg3(newDatePart); }
    }

    /**
     * The <code>CARDINALITY</code> function.
     * <p>
     * Calculate the cardinality of an array field.
     */
    public /*sealed*/ interface Cardinality
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Object[]>, Cardinality>,
            org.jooq.Field<Integer>
        //permits
        //    Cardinality
    {
        @NotNull default Field<? extends Object[]> $array() { return $arg1(); }
        @CheckReturnValue
        @NotNull default Cardinality $array(Field<? extends Object[]> newArray) { return $arg1(newArray); }
    }

    /**
     * The <code>ARRAY GET</code> function.
     * <p>
     * Get an array element at a given index (1 based).
     */
    public /*sealed*/ interface ArrayGet<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T[]>, Field<Integer>, ArrayGet<T>>,
            org.jooq.Field<T>
        //permits
        //    ArrayGet
    {
        @NotNull default Field<T[]> $array() { return $arg1(); }
        @CheckReturnValue
        @NotNull default ArrayGet<T> $array(Field<T[]> newArray) { return $arg1(newArray); }
        @NotNull default Field<Integer> $index() { return $arg2(); }
        @CheckReturnValue
        @NotNull default ArrayGet<T> $index(Field<Integer> newIndex) { return $arg2(newIndex); }
    }

    /**
     * The <code>ARRAY CONCAT</code> function.
     * <p>
     * Concatenate two arrays.
     */
    public /*sealed*/ interface ArrayConcat<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T[]>, Field<T[]>, ArrayConcat<T>>,
            org.jooq.Field<T[]>
        //permits
        //    ArrayConcat
    {

        /**
         * The first array.
         */
        @NotNull default Field<T[]> $array1() { return $arg1(); }

        /**
         * The first array.
         */
        @CheckReturnValue
        @NotNull default ArrayConcat<T> $array1(Field<T[]> newArray1) { return $arg1(newArray1); }

        /**
         * The second array.
         */
        @NotNull default Field<T[]> $array2() { return $arg2(); }

        /**
         * The second array.
         */
        @CheckReturnValue
        @NotNull default ArrayConcat<T> $array2(Field<T[]> newArray2) { return $arg2(newArray2); }
    }

    /**
     * The <code>ARRAY APPEND</code> function.
     * <p>
     * Append an element to an array.
     */
    public /*sealed*/ interface ArrayAppend<T>
        extends
            UOperator2<Field<T[]>, Field<T>, ArrayAppend<T>>,
            org.jooq.Field<T[]>
        //permits
        //    ArrayAppend
    {

        /**
         * The array to which to append an element.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array to which to append an element.
         */
        @CheckReturnValue
        @NotNull default ArrayAppend<T> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * The element to append to the array.
         */
        @NotNull default Field<T> $append() { return $arg2(); }

        /**
         * The element to append to the array.
         */
        @CheckReturnValue
        @NotNull default ArrayAppend<T> $append(Field<T> newAppend) { return $arg2(newAppend); }
    }

    /**
     * The <code>ARRAY PREPEND</code> function.
     * <p>
     * Prepend an element to an array.
     */
    public /*sealed*/ interface ArrayPrepend<T>
        extends
            UOperator2<Field<T>, Field<T[]>, ArrayPrepend<T>>,
            org.jooq.Field<T[]>
        //permits
        //    ArrayPrepend
    {

        /**
         * The element to prepend to the array.
         */
        @NotNull default Field<T> $prepend() { return $arg1(); }

        /**
         * The element to prepend to the array.
         */
        @CheckReturnValue
        @NotNull default ArrayPrepend<T> $prepend(Field<T> newPrepend) { return $arg1(newPrepend); }

        /**
         * The array to which to prepend an element.
         */
        @NotNull default Field<T[]> $array() { return $arg2(); }

        /**
         * The array to which to prepend an element.
         */
        @CheckReturnValue
        @NotNull default ArrayPrepend<T> $array(Field<T[]> newArray) { return $arg2(newArray); }
    }

    /**
     * The <code>ARRAY CONTAINS</code> function.
     * <p>
     * Check if a value is contained in an array.
     */
    public /*sealed*/ interface ArrayContains<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T[]>, Field<T>, ArrayContains<T>>,
            org.jooq.Condition
        //permits
        //    ArrayContains
    {

        /**
         * The array.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array.
         */
        @CheckReturnValue
        @NotNull default ArrayContains<T> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * The array element.
         */
        @NotNull default Field<T> $value() { return $arg2(); }

        /**
         * The array element.
         */
        @CheckReturnValue
        @NotNull default ArrayContains<T> $value(Field<T> newValue) { return $arg2(newValue); }
    }

    /**
     * The <code>ARRAY OVERLAP</code> function.
     * <p>
     * Check if 2 arrays overlap.
     */
    public /*sealed*/ interface ArrayOverlap<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T[]>, Field<T[]>, ArrayOverlap<T>>,
            org.jooq.Condition
        //permits
        //    ArrayOverlap
    {

        /**
         * The first array.
         */
        @NotNull default Field<T[]> $array1() { return $arg1(); }

        /**
         * The first array.
         */
        @CheckReturnValue
        @NotNull default ArrayOverlap<T> $array1(Field<T[]> newArray1) { return $arg1(newArray1); }

        /**
         * The second array.
         */
        @NotNull default Field<T[]> $array2() { return $arg2(); }

        /**
         * The second array.
         */
        @CheckReturnValue
        @NotNull default ArrayOverlap<T> $array2(Field<T[]> newArray2) { return $arg2(newArray2); }
    }

    /**
     * The <code>ARRAY REMOVE</code> function.
     * <p>
     * Remove an element from an array.
     */
    public /*sealed*/ interface ArrayRemove<T>
        extends
            UOperator2<Field<T[]>, Field<T>, ArrayRemove<T>>,
            org.jooq.Field<T[]>
        //permits
        //    ArrayRemove
    {

        /**
         * The array whose elements are to be removed.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array whose elements are to be removed.
         */
        @CheckReturnValue
        @NotNull default ArrayRemove<T> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * The array element that should be removed.
         */
        @NotNull default Field<T> $remove() { return $arg2(); }

        /**
         * The array element that should be removed.
         */
        @CheckReturnValue
        @NotNull default ArrayRemove<T> $remove(Field<T> newRemove) { return $arg2(newRemove); }
    }

    /**
     * The <code>ARRAY REPLACE</code> function.
     * <p>
     * Replace an element in an array.
     */
    public /*sealed*/ interface ArrayReplace<T>
        extends
            UOperator3<Field<T[]>, Field<T>, Field<T>, ArrayReplace<T>>,
            org.jooq.Field<T[]>
        //permits
        //    ArrayReplace
    {

        /**
         * The array whose elements are to be replaced.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array whose elements are to be replaced.
         */
        @CheckReturnValue
        @NotNull default ArrayReplace<T> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * The expression to search for in the array.
         */
        @NotNull default Field<T> $search() { return $arg2(); }

        /**
         * The expression to search for in the array.
         */
        @CheckReturnValue
        @NotNull default ArrayReplace<T> $search(Field<T> newSearch) { return $arg2(newSearch); }

        /**
         * The value to replace a value by.
         */
        @NotNull default Field<T> $replace() { return $arg3(); }

        /**
         * The value to replace a value by.
         */
        @CheckReturnValue
        @NotNull default ArrayReplace<T> $replace(Field<T> newReplace) { return $arg3(newReplace); }
    }

    /**
     * The <code>ARRAY TO STRING</code> function.
     * <p>
     * Join array elements into a string.
     */
    public /*sealed*/ interface ArrayToString<T>
        extends
            UOperator3<Field<? extends Object[]>, Field<String>, Field<String>, ArrayToString<T>>,
            org.jooq.Field<String>
        //permits
        //    ArrayToString
    {

        /**
         * The array whose elements are joined
         */
        @NotNull default Field<? extends Object[]> $array() { return $arg1(); }

        /**
         * The array whose elements are joined
         */
        @CheckReturnValue
        @NotNull default ArrayToString<T> $array(Field<? extends Object[]> newArray) { return $arg1(newArray); }

        /**
         * The delimiter to place between elements
         */
        @NotNull default Field<String> $delimiter() { return $arg2(); }

        /**
         * The delimiter to place between elements
         */
        @CheckReturnValue
        @NotNull default ArrayToString<T> $delimiter(Field<String> newDelimiter) { return $arg2(newDelimiter); }

        /**
         * The NULL encoding
         */
        @Nullable default Field<String> $nullString() { return $arg3(); }

        /**
         * The NULL encoding
         */
        @CheckReturnValue
        @NotNull default ArrayToString<T> $nullString(Field<String> newNullString) { return $arg3(newNullString); }
    }

    /**
     * The <code>ARRAY FILTER</code> function.
     * <p>
     * Filter elements out of an array.
     */
    public /*sealed*/ interface ArrayFilter<T>
        extends
            UOperator2<Field<T[]>, Lambda1<Field<T>, Condition>, ArrayFilter<T>>,
            org.jooq.Field<T[]>
        //permits
        //    ArrayFilter
    {

        /**
         * The array whose elements are filtered.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array whose elements are filtered.
         */
        @CheckReturnValue
        @NotNull default ArrayFilter<T> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * A predicate defining which elements to keep in the array.
         */
        @NotNull default Lambda1<Field<T>, Condition> $predicate() { return $arg2(); }

        /**
         * A predicate defining which elements to keep in the array.
         */
        @CheckReturnValue
        @NotNull default ArrayFilter<T> $predicate(Lambda1<Field<T>, Condition> newPredicate) { return $arg2(newPredicate); }
    }

    /**
     * The <code>ARRAY MAP</code> function.
     * <p>
     * Map elements of an array.
     */
    public /*sealed*/ interface ArrayMap<T, U>
        extends
            UOperator2<Field<T[]>, Lambda1<Field<T>, Field<U>>, ArrayMap<T, U>>,
            org.jooq.Field<U[]>
        //permits
        //    ArrayMap
    {

        /**
         * The array whose elements are mapped.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array whose elements are mapped.
         */
        @CheckReturnValue
        @NotNull default ArrayMap<T, U> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * The function that defines the mapping between source elements and result elements.
         */
        @NotNull default Lambda1<Field<T>, Field<U>> $mapper() { return $arg2(); }

        /**
         * The function that defines the mapping between source elements and result elements.
         */
        @CheckReturnValue
        @NotNull default ArrayMap<T, U> $mapper(Lambda1<Field<T>, Field<U>> newMapper) { return $arg2(newMapper); }
    }

    /**
     * The <code>ARRAY ALL MATCH</code> function.
     * <p>
     * Check if all elements of an array match a given predicate.
     */
    public /*sealed*/ interface ArrayAllMatch<T>
        extends
            UOperator2<Field<T[]>, Lambda1<Field<T>, Condition>, ArrayAllMatch<T>>,
            org.jooq.Condition
        //permits
        //    ArrayAllMatch
    {

        /**
         * The array to be checked.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array to be checked.
         */
        @CheckReturnValue
        @NotNull default ArrayAllMatch<T> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * The predicate that must be true for all array elements.
         */
        @NotNull default Lambda1<Field<T>, Condition> $predicate() { return $arg2(); }

        /**
         * The predicate that must be true for all array elements.
         */
        @CheckReturnValue
        @NotNull default ArrayAllMatch<T> $predicate(Lambda1<Field<T>, Condition> newPredicate) { return $arg2(newPredicate); }
    }

    /**
     * The <code>ARRAY ANY MATCH</code> function.
     * <p>
     * Check if any elements of an array match a given predicate.
     */
    public /*sealed*/ interface ArrayAnyMatch<T>
        extends
            UOperator2<Field<T[]>, Lambda1<Field<T>, Condition>, ArrayAnyMatch<T>>,
            org.jooq.Condition
        //permits
        //    ArrayAnyMatch
    {

        /**
         * The array to be checked.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array to be checked.
         */
        @CheckReturnValue
        @NotNull default ArrayAnyMatch<T> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * The predicate that must be true for at least 1 array element
         */
        @NotNull default Lambda1<Field<T>, Condition> $predicate() { return $arg2(); }

        /**
         * The predicate that must be true for at least 1 array element
         */
        @CheckReturnValue
        @NotNull default ArrayAnyMatch<T> $predicate(Lambda1<Field<T>, Condition> newPredicate) { return $arg2(newPredicate); }
    }

    /**
     * The <code>ARRAY NONE MATCH</code> function.
     * <p>
     * Check if none of the elements of an array match a given predicate.
     */
    public /*sealed*/ interface ArrayNoneMatch<T>
        extends
            UOperator2<Field<T[]>, Lambda1<Field<T>, Condition>, ArrayNoneMatch<T>>,
            org.jooq.Condition
        //permits
        //    ArrayNoneMatch
    {

        /**
         * The array to be checked.
         */
        @NotNull default Field<T[]> $array() { return $arg1(); }

        /**
         * The array to be checked.
         */
        @CheckReturnValue
        @NotNull default ArrayNoneMatch<T> $array(Field<T[]> newArray) { return $arg1(newArray); }

        /**
         * The predicate that must be false for all elements.
         */
        @NotNull default Lambda1<Field<T>, Condition> $predicate() { return $arg2(); }

        /**
         * The predicate that must be false for all elements.
         */
        @CheckReturnValue
        @NotNull default ArrayNoneMatch<T> $predicate(Lambda1<Field<T>, Condition> newPredicate) { return $arg2(newPredicate); }
    }

    /**
     * The <code>STRING TO ARRAY</code> function.
     * <p>
     * Split a string into array elements.
     */
    public /*sealed*/ interface StringToArray
        extends
            UOperator3<Field<String>, Field<String>, Field<String>, StringToArray>,
            org.jooq.Field<String[]>
        //permits
        //    StringToArray
    {

        /**
         * The string to split
         */
        @NotNull default Field<String> $string() { return $arg1(); }

        /**
         * The string to split
         */
        @CheckReturnValue
        @NotNull default StringToArray $string(Field<String> newString) { return $arg1(newString); }

        /**
         * The delimiter to parse between elements
         */
        @NotNull default Field<String> $delimiter() { return $arg2(); }

        /**
         * The delimiter to parse between elements
         */
        @CheckReturnValue
        @NotNull default StringToArray $delimiter(Field<String> newDelimiter) { return $arg2(newDelimiter); }

        /**
         * The NULL encoding
         */
        @Nullable default Field<String> $nullString() { return $arg3(); }

        /**
         * The NULL encoding
         */
        @CheckReturnValue
        @NotNull default StringToArray $nullString(Field<String> newNullString) { return $arg3(newNullString); }
    }

    /**
     * The <code>NVL</code> function.
     * <p>
     * Return the first non-null argument.
     */
    public /*sealed*/ interface Nvl<T>
        extends
            UOperator2<Field<T>, Field<T>, Nvl<T>>,
            org.jooq.Field<T>
        //permits
        //    Nvl
    {

        /**
         * The nullable value.
         */
        @NotNull default Field<T> $value() { return $arg1(); }

        /**
         * The nullable value.
         */
        @CheckReturnValue
        @NotNull default Nvl<T> $value(Field<T> newValue) { return $arg1(newValue); }

        /**
         * The default value if the other value is null.
         */
        @NotNull default Field<T> $defaultValue() { return $arg2(); }

        /**
         * The default value if the other value is null.
         */
        @CheckReturnValue
        @NotNull default Nvl<T> $defaultValue(Field<T> newDefaultValue) { return $arg2(newDefaultValue); }
    }

    /**
     * The <code>NULLIF</code> function.
     */
    public /*sealed*/ interface Nullif<T>
        extends
            UOperator2<Field<T>, Field<T>, Nullif<T>>,
            org.jooq.Field<T>
        //permits
        //    Nullif
    {

        /**
         * The result value if the other value is not equal.
         */
        @NotNull default Field<T> $value() { return $arg1(); }

        /**
         * The result value if the other value is not equal.
         */
        @CheckReturnValue
        @NotNull default Nullif<T> $value(Field<T> newValue) { return $arg1(newValue); }

        /**
         * The value to compare the result value with.
         */
        @NotNull default Field<T> $other() { return $arg2(); }

        /**
         * The value to compare the result value with.
         */
        @CheckReturnValue
        @NotNull default Nullif<T> $other(Field<T> newOther) { return $arg2(newOther); }
    }

    /**
     * The <code>TRY CAST</code> function.
     */
    public /*sealed*/ interface TryCast<T>
        extends
            UOperator2<Field<?>, DataType<T>, TryCast<T>>,
            org.jooq.Field<T>
        //permits
        //    TryCast
    {

        /**
         * The value to be cast to a data type
         */
        @NotNull default Field<?> $value() { return $arg1(); }

        /**
         * The value to be cast to a data type
         */
        @CheckReturnValue
        @NotNull default TryCast<T> $value(Field<?> newValue) { return $arg1(newValue); }

        /**
         * The data type to try to cast the value to
         */
        @Override
        @NotNull default DataType<T> $dataType() { return $arg2(); }

        /**
         * The data type to try to cast the value to
         */
        @CheckReturnValue
        @NotNull default TryCast<T> $dataType(DataType<T> newDataType) { return $arg2(newDataType); }
    }

    /**
     * The <code>CURRENT CATALOG</code> function.
     * <p>
     * The CURRENT_CATALOG of the current session
     */
    public /*sealed*/ interface CurrentCatalog
        extends
            UOperator0<CurrentCatalog>,
            org.jooq.Field<String>
        //permits
        //    CurrentCatalog
    {}

    /**
     * The <code>CURRENT SCHEMA</code> function.
     * <p>
     * The CURRENT_SCHEMA of the current session
     */
    public /*sealed*/ interface CurrentSchema
        extends
            UOperator0<CurrentSchema>,
            org.jooq.Field<String>
        //permits
        //    CurrentSchema
    {}

    /**
     * The <code>CURRENT USER</code> function.
     * <p>
     * The CURRENT_USER of the current session with the database
     */
    public /*sealed*/ interface CurrentUser
        extends
            UOperator0<CurrentUser>,
            org.jooq.Field<String>
        //permits
        //    CurrentUser
    {}























































































































































    /**
     * The <code>XMLCOMMENT</code> function.
     */
    public /*sealed*/ interface XMLComment
        extends
            UOperator1<Field<String>, XMLComment>,
            org.jooq.Field<XML>
        //permits
        //    XMLComment
    {
        @NotNull default Field<String> $comment() { return $arg1(); }
        @CheckReturnValue
        @NotNull default XMLComment $comment(Field<String> newComment) { return $arg1(newComment); }
    }

    /**
     * The <code>XMLCONCAT</code> function.
     */
    public /*sealed*/ interface XMLConcat
        extends
            UOperator1<QOM.UnmodifiableList<? extends Field<?>>, XMLConcat>,
            org.jooq.Field<XML>
        //permits
        //    XMLConcat
    {}



















    /**
     * The <code>XMLFOREST</code> function.
     */
    public /*sealed*/ interface XMLForest
        extends
            UOperator1<QOM.UnmodifiableList<? extends Field<?>>, XMLForest>,
            org.jooq.Field<XML>
        //permits
        //    XMLForest
    {
        @NotNull default QOM.UnmodifiableList<? extends Field<?>> $fields() { return $arg1(); }
        @CheckReturnValue
        @NotNull default XMLForest $fields(QOM.UnmodifiableList<? extends Field<?>> newFields) { return $arg1(newFields); }
    }

    /**
     * The <code>XMLPI</code> function.
     */
    public /*sealed*/ interface XMLPi
        extends
            UOperator2<Name, Field<?>, XMLPi>,
            org.jooq.Field<XML>
        //permits
        //    XMLPi
    {
        @NotNull default Name $target() { return $arg1(); }
        @CheckReturnValue
        @NotNull default XMLPi $target(Name newTarget) { return $arg1(newTarget); }
        @Nullable default Field<?> $content() { return $arg2(); }
        @CheckReturnValue
        @NotNull default XMLPi $content(Field<?> newContent) { return $arg2(newContent); }
    }

    /**
     * The <code>XMLSERIALIZE</code> function.
     */
    public /*sealed*/ interface XMLSerialize<T>
        extends
            UOperator3<Boolean, Field<XML>, DataType<T>, XMLSerialize<T>>,
            org.jooq.Field<T>
        //permits
        //    XMLSerialize
    {
        @NotNull default Boolean $content() { return $arg1(); }
        @CheckReturnValue
        @NotNull default XMLSerialize<T> $content(Boolean newContent) { return $arg1(newContent); }
        @NotNull default Field<XML> $value() { return $arg2(); }
        @CheckReturnValue
        @NotNull default XMLSerialize<T> $value(Field<XML> newValue) { return $arg2(newValue); }
        @NotNull default DataType<T> $type() { return $arg3(); }
        @CheckReturnValue
        @NotNull default XMLSerialize<T> $type(DataType<T> newType) { return $arg3(newType); }
    }

    /**
     * The <code>JSON ARRAY</code> function.
     */
    public /*sealed*/ interface JSONArray<T>
        extends
            UOperator4<DataType<T>, QOM.UnmodifiableList<? extends Field<?>>, JSONOnNull, DataType<?>, JSONArray<T>>,
            Field<T>
        //permits
        //    JSONArray
    {
        @NotNull default DataType<T> $type() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONArray<T> $type(DataType<T> newType) { return $arg1(newType); }
        @NotNull default QOM.UnmodifiableList<? extends Field<?>> $fields() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONArray<T> $fields(QOM.UnmodifiableList<? extends Field<?>> newFields) { return $arg2(newFields); }
        @Nullable default JSONOnNull $onNull() { return $arg3(); }
        @CheckReturnValue
        @NotNull default JSONArray<T> $onNull(JSONOnNull newOnNull) { return $arg3(newOnNull); }
        @Nullable default DataType<?> $returning() { return $arg4(); }
        @CheckReturnValue
        @NotNull default JSONArray<T> $returning(DataType<?> newReturning) { return $arg4(newReturning); }
    }

    /**
     * The <code>JSON OBJECT</code> function.
     */
    public /*sealed*/ interface JSONObject<T>
        extends
            UOperator4<DataType<T>, QOM.UnmodifiableList<? extends JSONEntry<?>>, JSONOnNull, DataType<?>, JSONObject<T>>,
            Field<T>
        //permits
        //    JSONObject
    {
        @NotNull default DataType<T> $type() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONObject<T> $type(DataType<T> newType) { return $arg1(newType); }
        @NotNull default QOM.UnmodifiableList<? extends JSONEntry<?>> $entries() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONObject<T> $entries(QOM.UnmodifiableList<? extends JSONEntry<?>> newEntries) { return $arg2(newEntries); }
        @Nullable default JSONOnNull $onNull() { return $arg3(); }
        @CheckReturnValue
        @NotNull default JSONObject<T> $onNull(JSONOnNull newOnNull) { return $arg3(newOnNull); }
        @Nullable default DataType<?> $returning() { return $arg4(); }
        @CheckReturnValue
        @NotNull default JSONObject<T> $returning(DataType<?> newReturning) { return $arg4(newReturning); }
    }

    /**
     * The <code>JSON GET ELEMENT</code> function.
     * <p>
     * Access an array element from a JSON array expression.
     */
    public /*sealed*/ interface JSONGetElement
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<Integer>, JSONGetElement>,
            org.jooq.Field<JSON>
        //permits
        //    JSONGetElement
    {

        /**
         * The JSON document
         */
        @NotNull default Field<JSON> $field() { return $arg1(); }

        /**
         * The JSON document
         */
        @CheckReturnValue
        @NotNull default JSONGetElement $field(Field<JSON> newField) { return $arg1(newField); }

        /**
         * The 0-based JSON array index
         */
        @NotNull default Field<Integer> $index() { return $arg2(); }

        /**
         * The 0-based JSON array index
         */
        @CheckReturnValue
        @NotNull default JSONGetElement $index(Field<Integer> newIndex) { return $arg2(newIndex); }
    }

    /**
     * The <code>JSONB GET ELEMENT</code> function.
     * <p>
     * Access an array element from a JSONB array expression.
     */
    public /*sealed*/ interface JSONBGetElement
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<Integer>, JSONBGetElement>,
            org.jooq.Field<JSONB>
        //permits
        //    JSONBGetElement
    {

        /**
         * The JSONB document
         */
        @NotNull default Field<JSONB> $field() { return $arg1(); }

        /**
         * The JSONB document
         */
        @CheckReturnValue
        @NotNull default JSONBGetElement $field(Field<JSONB> newField) { return $arg1(newField); }

        /**
         * The 0-based JSONB array index
         */
        @NotNull default Field<Integer> $index() { return $arg2(); }

        /**
         * The 0-based JSONB array index
         */
        @CheckReturnValue
        @NotNull default JSONBGetElement $index(Field<Integer> newIndex) { return $arg2(newIndex); }
    }

    /**
     * The <code>JSON GET ELEMENT AS TEXT</code> function.
     * <p>
     * Access an array element from a JSON array expression and return it as a string.
     */
    public /*sealed*/ interface JSONGetElementAsText
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<Integer>, JSONGetElementAsText>,
            org.jooq.Field<String>
        //permits
        //    JSONGetElementAsText
    {

        /**
         * The JSON document
         */
        @NotNull default Field<JSON> $field() { return $arg1(); }

        /**
         * The JSON document
         */
        @CheckReturnValue
        @NotNull default JSONGetElementAsText $field(Field<JSON> newField) { return $arg1(newField); }

        /**
         * The 0-based JSON array index
         */
        @NotNull default Field<Integer> $index() { return $arg2(); }

        /**
         * The 0-based JSON array index
         */
        @CheckReturnValue
        @NotNull default JSONGetElementAsText $index(Field<Integer> newIndex) { return $arg2(newIndex); }
    }

    /**
     * The <code>JSONB GET ELEMENT AS TEXT</code> function.
     * <p>
     * Access an array element from a JSONB array expression and return it as a string.
     */
    public /*sealed*/ interface JSONBGetElementAsText
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<Integer>, JSONBGetElementAsText>,
            org.jooq.Field<String>
        //permits
        //    JSONBGetElementAsText
    {

        /**
         * The JSONB document
         */
        @NotNull default Field<JSONB> $field() { return $arg1(); }

        /**
         * The JSONB document
         */
        @CheckReturnValue
        @NotNull default JSONBGetElementAsText $field(Field<JSONB> newField) { return $arg1(newField); }

        /**
         * The 0-based JSONB array index
         */
        @NotNull default Field<Integer> $index() { return $arg2(); }

        /**
         * The 0-based JSONB array index
         */
        @CheckReturnValue
        @NotNull default JSONBGetElementAsText $index(Field<Integer> newIndex) { return $arg2(newIndex); }
    }

    /**
     * The <code>JSON GET ATTRIBUTE</code> function.
     * <p>
     * Access an object attribute value from a JSON object expression.
     */
    public /*sealed*/ interface JSONGetAttribute
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<String>, JSONGetAttribute>,
            org.jooq.Field<JSON>
        //permits
        //    JSONGetAttribute
    {

        /**
         * The JSON document
         */
        @NotNull default Field<JSON> $field() { return $arg1(); }

        /**
         * The JSON document
         */
        @CheckReturnValue
        @NotNull default JSONGetAttribute $field(Field<JSON> newField) { return $arg1(newField); }

        /**
         * The JSON object attribute name
         */
        @NotNull default Field<String> $attribute() { return $arg2(); }

        /**
         * The JSON object attribute name
         */
        @CheckReturnValue
        @NotNull default JSONGetAttribute $attribute(Field<String> newAttribute) { return $arg2(newAttribute); }
    }

    /**
     * The <code>JSONB GET ATTRIBUTE</code> function.
     * <p>
     * Access an object attribute value from a JSONB object expression.
     */
    public /*sealed*/ interface JSONBGetAttribute
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<String>, JSONBGetAttribute>,
            org.jooq.Field<JSONB>
        //permits
        //    JSONBGetAttribute
    {

        /**
         * The JSONB document
         */
        @NotNull default Field<JSONB> $field() { return $arg1(); }

        /**
         * The JSONB document
         */
        @CheckReturnValue
        @NotNull default JSONBGetAttribute $field(Field<JSONB> newField) { return $arg1(newField); }

        /**
         * The JSONB object attribute name
         */
        @NotNull default Field<String> $attribute() { return $arg2(); }

        /**
         * The JSONB object attribute name
         */
        @CheckReturnValue
        @NotNull default JSONBGetAttribute $attribute(Field<String> newAttribute) { return $arg2(newAttribute); }
    }

    /**
     * The <code>JSON GET ATTRIBUTE AS TEXT</code> function.
     * <p>
     * Access an object attribute value from a JSON object expression and return it as string.
     */
    public /*sealed*/ interface JSONGetAttributeAsText
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<String>, JSONGetAttributeAsText>,
            org.jooq.Field<String>
        //permits
        //    JSONGetAttributeAsText
    {

        /**
         * The JSON document
         */
        @NotNull default Field<JSON> $field() { return $arg1(); }

        /**
         * The JSON document
         */
        @CheckReturnValue
        @NotNull default JSONGetAttributeAsText $field(Field<JSON> newField) { return $arg1(newField); }

        /**
         * The JSON object attribute name
         */
        @NotNull default Field<String> $attribute() { return $arg2(); }

        /**
         * The JSON object attribute name
         */
        @CheckReturnValue
        @NotNull default JSONGetAttributeAsText $attribute(Field<String> newAttribute) { return $arg2(newAttribute); }
    }

    /**
     * The <code>JSONB GET ATTRIBUTE AS TEXT</code> function.
     * <p>
     * Access an object attribute value from a JSONB object expression and return it as
     * string.
     */
    public /*sealed*/ interface JSONBGetAttributeAsText
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<String>, JSONBGetAttributeAsText>,
            org.jooq.Field<String>
        //permits
        //    JSONBGetAttributeAsText
    {

        /**
         * The JSONB document
         */
        @NotNull default Field<JSONB> $field() { return $arg1(); }

        /**
         * The JSONB document
         */
        @CheckReturnValue
        @NotNull default JSONBGetAttributeAsText $field(Field<JSONB> newField) { return $arg1(newField); }

        /**
         * The JSONB object attribute name
         */
        @NotNull default Field<String> $attribute() { return $arg2(); }

        /**
         * The JSONB object attribute name
         */
        @CheckReturnValue
        @NotNull default JSONBGetAttributeAsText $attribute(Field<String> newAttribute) { return $arg2(newAttribute); }
    }

    /**
     * The <code>JSON ARRAY LENGTH</code> function.
     * <p>
     * Calculate the length of a JSON array.
     */
    public /*sealed*/ interface JSONArrayLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<JSON>, JSONArrayLength>,
            org.jooq.Field<Integer>
        //permits
        //    JSONArrayLength
    {
        @NotNull default Field<JSON> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONArrayLength $field(Field<JSON> newField) { return $arg1(newField); }
    }

    /**
     * The <code>JSONB ARRAY LENGTH</code> function.
     * <p>
     * Calculate the length of a JSONB array.
     */
    public /*sealed*/ interface JSONBArrayLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<JSONB>, JSONBArrayLength>,
            org.jooq.Field<Integer>
        //permits
        //    JSONBArrayLength
    {
        @NotNull default Field<JSONB> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONBArrayLength $field(Field<JSONB> newField) { return $arg1(newField); }
    }

    /**
     * The <code>JSON KEY EXISTS</code> function.
     * <p>
     * Check if a key exists in a JSON object
     */
    public /*sealed*/ interface JSONKeyExists
        extends
            UOperator2<Field<JSON>, Field<String>, JSONKeyExists>,
            org.jooq.Condition
        //permits
        //    JSONKeyExists
    {

        /**
         * The JSON object
         */
        @NotNull default Field<JSON> $json() { return $arg1(); }

        /**
         * The JSON object
         */
        @CheckReturnValue
        @NotNull default JSONKeyExists $json(Field<JSON> newJson) { return $arg1(newJson); }

        /**
         * The key in the JSON object
         */
        @NotNull default Field<String> $key() { return $arg2(); }

        /**
         * The key in the JSON object
         */
        @CheckReturnValue
        @NotNull default JSONKeyExists $key(Field<String> newKey) { return $arg2(newKey); }
    }

    /**
     * The <code>JSONB KEY EXISTS</code> function.
     * <p>
     * Check if a key exists in a JSONB object
     */
    public /*sealed*/ interface JSONBKeyExists
        extends
            UOperator2<Field<JSONB>, Field<String>, JSONBKeyExists>,
            org.jooq.Condition
        //permits
        //    JSONBKeyExists
    {

        /**
         * The JSONB object
         */
        @NotNull default Field<JSONB> $json() { return $arg1(); }

        /**
         * The JSONB object
         */
        @CheckReturnValue
        @NotNull default JSONBKeyExists $json(Field<JSONB> newJson) { return $arg1(newJson); }

        /**
         * The key in the JSONB object
         */
        @NotNull default Field<String> $key() { return $arg2(); }

        /**
         * The key in the JSONB object
         */
        @CheckReturnValue
        @NotNull default JSONBKeyExists $key(Field<String> newKey) { return $arg2(newKey); }
    }

    /**
     * The <code>JSON KEYS</code> function.
     * <p>
     * Retrieve all keys from a JSON object as an array of strings.
     */
    public /*sealed*/ interface JSONKeys
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<JSON>, JSONKeys>,
            org.jooq.Field<JSON>
        //permits
        //    JSONKeys
    {
        @NotNull default Field<JSON> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONKeys $field(Field<JSON> newField) { return $arg1(newField); }
    }

    /**
     * The <code>JSONB KEYS</code> function.
     * <p>
     * Retrieve all keys from a JSONB object as an array of strings.
     */
    public /*sealed*/ interface JSONBKeys
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<JSONB>, JSONBKeys>,
            org.jooq.Field<JSONB>
        //permits
        //    JSONBKeys
    {
        @NotNull default Field<JSONB> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONBKeys $field(Field<JSONB> newField) { return $arg1(newField); }
    }

    /**
     * The <code>JSON SET</code> function.
     * <p>
     * Add or replace a JSON value to a JSON field at a given path.
     */
    public /*sealed*/ interface JSONSet
        extends
            UOperator3<Field<JSON>, Field<String>, Field<?>, JSONSet>,
            org.jooq.Field<JSON>
        //permits
        //    JSONSet
    {
        @NotNull default Field<JSON> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONSet $field(Field<JSON> newField) { return $arg1(newField); }
        @NotNull default Field<String> $path() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONSet $path(Field<String> newPath) { return $arg2(newPath); }
        @NotNull default Field<?> $value() { return $arg3(); }
        @CheckReturnValue
        @NotNull default JSONSet $value(Field<?> newValue) { return $arg3(newValue); }
    }

    /**
     * The <code>JSONB SET</code> function.
     * <p>
     * Add or replace a JSONB value to a JSONB field at a given path.
     */
    public /*sealed*/ interface JSONBSet
        extends
            UOperator3<Field<JSONB>, Field<String>, Field<?>, JSONBSet>,
            org.jooq.Field<JSONB>
        //permits
        //    JSONBSet
    {
        @NotNull default Field<JSONB> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONBSet $field(Field<JSONB> newField) { return $arg1(newField); }
        @NotNull default Field<String> $path() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONBSet $path(Field<String> newPath) { return $arg2(newPath); }
        @NotNull default Field<?> $value() { return $arg3(); }
        @CheckReturnValue
        @NotNull default JSONBSet $value(Field<?> newValue) { return $arg3(newValue); }
    }

    /**
     * The <code>JSON INSERT</code> function.
     * <p>
     * Add (but not replace) a JSON value to a JSON field at a given path.
     */
    public /*sealed*/ interface JSONInsert
        extends
            UOperator3<Field<JSON>, Field<String>, Field<?>, JSONInsert>,
            org.jooq.Field<JSON>
        //permits
        //    JSONInsert
    {
        @NotNull default Field<JSON> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONInsert $field(Field<JSON> newField) { return $arg1(newField); }
        @NotNull default Field<String> $path() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONInsert $path(Field<String> newPath) { return $arg2(newPath); }
        @NotNull default Field<?> $value() { return $arg3(); }
        @CheckReturnValue
        @NotNull default JSONInsert $value(Field<?> newValue) { return $arg3(newValue); }
    }

    /**
     * The <code>JSONB INSERT</code> function.
     * <p>
     * Add (but not replace) a JSON value to a JSON field at a given path.
     */
    public /*sealed*/ interface JSONBInsert
        extends
            UOperator3<Field<JSONB>, Field<String>, Field<?>, JSONBInsert>,
            org.jooq.Field<JSONB>
        //permits
        //    JSONBInsert
    {
        @NotNull default Field<JSONB> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONBInsert $field(Field<JSONB> newField) { return $arg1(newField); }
        @NotNull default Field<String> $path() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONBInsert $path(Field<String> newPath) { return $arg2(newPath); }
        @NotNull default Field<?> $value() { return $arg3(); }
        @CheckReturnValue
        @NotNull default JSONBInsert $value(Field<?> newValue) { return $arg3(newValue); }
    }

    /**
     * The <code>JSON REPLACE</code> function.
     * <p>
     * Replace (but not add) a JSON value to a JSON field at a given path.
     */
    public /*sealed*/ interface JSONReplace
        extends
            UOperator3<Field<JSON>, Field<String>, Field<?>, JSONReplace>,
            org.jooq.Field<JSON>
        //permits
        //    JSONReplace
    {
        @NotNull default Field<JSON> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONReplace $field(Field<JSON> newField) { return $arg1(newField); }
        @NotNull default Field<String> $path() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONReplace $path(Field<String> newPath) { return $arg2(newPath); }
        @NotNull default Field<?> $value() { return $arg3(); }
        @CheckReturnValue
        @NotNull default JSONReplace $value(Field<?> newValue) { return $arg3(newValue); }
    }

    /**
     * The <code>JSONB REPLACE</code> function.
     * <p>
     * Replace (but not add) a JSONB value to a JSONB field at a given path.
     */
    public /*sealed*/ interface JSONBReplace
        extends
            UOperator3<Field<JSONB>, Field<String>, Field<?>, JSONBReplace>,
            org.jooq.Field<JSONB>
        //permits
        //    JSONBReplace
    {
        @NotNull default Field<JSONB> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONBReplace $field(Field<JSONB> newField) { return $arg1(newField); }
        @NotNull default Field<String> $path() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONBReplace $path(Field<String> newPath) { return $arg2(newPath); }
        @NotNull default Field<?> $value() { return $arg3(); }
        @CheckReturnValue
        @NotNull default JSONBReplace $value(Field<?> newValue) { return $arg3(newValue); }
    }

    /**
     * The <code>JSON REMOVE</code> function.
     * <p>
     * Remove a JSON value from a JSON field at a given path.
     */
    public /*sealed*/ interface JSONRemove
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<String>, JSONRemove>,
            org.jooq.Field<JSON>
        //permits
        //    JSONRemove
    {
        @NotNull default Field<JSON> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONRemove $field(Field<JSON> newField) { return $arg1(newField); }
        @NotNull default Field<String> $path() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONRemove $path(Field<String> newPath) { return $arg2(newPath); }
    }

    /**
     * The <code>JSONB REMOVE</code> function.
     * <p>
     * Remove a JSONB value from a JSONB field at a given path.
     */
    public /*sealed*/ interface JSONBRemove
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<String>, JSONBRemove>,
            org.jooq.Field<JSONB>
        //permits
        //    JSONBRemove
    {
        @NotNull default Field<JSONB> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default JSONBRemove $field(Field<JSONB> newField) { return $arg1(newField); }
        @NotNull default Field<String> $path() { return $arg2(); }
        @CheckReturnValue
        @NotNull default JSONBRemove $path(Field<String> newPath) { return $arg2(newPath); }
    }







































    /**
     * The <code>FIELD</code> function.
     * <p>
     * Wrap a condition in a boolean field.
     */
    public /*sealed*/ interface ConditionAsField
        extends
            UReturnsNullOnNullInput,
            UOperator1<Condition, ConditionAsField>,
            org.jooq.Field<Boolean>
        //permits
        //    ConditionAsField
    {
        @NotNull default Condition $condition() { return $arg1(); }
        @CheckReturnValue
        @NotNull default ConditionAsField $condition(Condition newCondition) { return $arg1(newCondition); }
    }

    /**
     * The <code>CONDITION</code> function.
     * <p>
     * Create a condition from a boolean field.
     * <p>
     * Databases that support boolean data types can use boolean expressions
     * as predicates or as columns interchangeably. This extends to any type
     * of field, including functions. A Postgres example:
     * <p>
     * <pre><code>
     * select 1 where texteq('a', 'a');
     * </code></pre>
     */
    public /*sealed*/ interface FieldCondition
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<Boolean>, FieldCondition>,
            org.jooq.Condition
        //permits
        //    FieldCondition
    {
        @NotNull default Field<Boolean> $field() { return $arg1(); }
        @CheckReturnValue
        @NotNull default FieldCondition $field(Field<Boolean> newField) { return $arg1(newField); }
    }

    /**
     * The <code>ANY VALUE</code> function.
     * <p>
     * Get any arbitrary value from the group.
     */
    public /*sealed*/ interface AnyValue<T>
        extends
            AggregateFunction<T, AnyValue<T>>
        //permits
        //    AnyValue
    {
        @NotNull Field<T> $field();
        @CheckReturnValue
        @NotNull AnyValue<T> $field(Field<T> field);
    }

    /**
     * The <code>AVG</code> function.
     */
    public /*sealed*/ interface Avg
        extends
            AggregateFunction<BigDecimal, Avg>
        //permits
        //    Avg
    {
        @NotNull Field<? extends Number> $field();
        boolean $distinct();
        @CheckReturnValue
        @NotNull Avg $field(Field<? extends Number> field);
        @CheckReturnValue
        @NotNull Avg $distinct(boolean distinct);
    }

    /**
     * The <code>APPROX COUNT DISTINCT</code> function.
     * <p>
     * Calculate the approximate value for {@link #countDistinct(Field)} if such a function
     * is available in the dialect, or fall back to the exact value, otherwise.
     */
    public /*sealed*/ interface ApproxCountDistinct
        extends
            AggregateFunction<Integer, ApproxCountDistinct>
        //permits
        //    ApproxCountDistinct
    {
        @NotNull Field<?> $field();
        @CheckReturnValue
        @NotNull ApproxCountDistinct $field(Field<?> field);
    }

    /**
     * The <code>APPROX COUNT LARGE DISTINCT</code> function.
     * <p>
     * Calculate the approximate value for {@link #countDistinct(Field)} if such a function
     * is available in the dialect, or fall back to the exact value, otherwise.
     */
    public /*sealed*/ interface ApproxCountLargeDistinct
        extends
            AggregateFunction<Long, ApproxCountLargeDistinct>
        //permits
        //    ApproxCountLargeDistinct
    {
        @NotNull Field<?> $field();
        @CheckReturnValue
        @NotNull ApproxCountLargeDistinct $field(Field<?> field);
    }

    /**
     * The <code>BIT AND AGG</code> function.
     * <p>
     * Calculate the bitwise <code>AND</code> aggregate value.
     */
    public /*sealed*/ interface BitAndAgg<T extends Number>
        extends
            AggregateFunction<T, BitAndAgg<T>>
        //permits
        //    BitAndAgg
    {
        @NotNull Field<T> $value();
        @CheckReturnValue
        @NotNull BitAndAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT OR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>OR</code> aggregate value.
     */
    public /*sealed*/ interface BitOrAgg<T extends Number>
        extends
            AggregateFunction<T, BitOrAgg<T>>
        //permits
        //    BitOrAgg
    {
        @NotNull Field<T> $value();
        @CheckReturnValue
        @NotNull BitOrAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT XOR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>XOR</code> aggregate value.
     */
    public /*sealed*/ interface BitXorAgg<T extends Number>
        extends
            AggregateFunction<T, BitXorAgg<T>>
        //permits
        //    BitXorAgg
    {
        @NotNull Field<T> $value();
        @CheckReturnValue
        @NotNull BitXorAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT NAND AGG</code> function.
     * <p>
     * Calculate the bitwise <code>NAND</code> aggregate value.
     */
    public /*sealed*/ interface BitNandAgg<T extends Number>
        extends
            AggregateFunction<T, BitNandAgg<T>>
        //permits
        //    BitNandAgg
    {
        @NotNull Field<T> $value();
        @CheckReturnValue
        @NotNull BitNandAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT NOR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>NOR</code> aggregate value.
     */
    public /*sealed*/ interface BitNorAgg<T extends Number>
        extends
            AggregateFunction<T, BitNorAgg<T>>
        //permits
        //    BitNorAgg
    {
        @NotNull Field<T> $value();
        @CheckReturnValue
        @NotNull BitNorAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT X NOR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>XNOR</code> aggregate value.
     */
    public /*sealed*/ interface BitXNorAgg<T extends Number>
        extends
            AggregateFunction<T, BitXNorAgg<T>>
        //permits
        //    BitXNorAgg
    {
        @NotNull Field<T> $value();
        @CheckReturnValue
        @NotNull BitXNorAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BOOL AND</code> function.
     */
    public /*sealed*/ interface BoolAnd
        extends
            AggregateFunction<Boolean, BoolAnd>
        //permits
        //    BoolAnd
    {
        @NotNull Condition $condition();
        @CheckReturnValue
        @NotNull BoolAnd $condition(Condition condition);
    }

    /**
     * The <code>BOOL OR</code> function.
     */
    public /*sealed*/ interface BoolOr
        extends
            AggregateFunction<Boolean, BoolOr>
        //permits
        //    BoolOr
    {
        @NotNull Condition $condition();
        @CheckReturnValue
        @NotNull BoolOr $condition(Condition condition);
    }

    /**
     * The <code>CORR</code> function.
     * <p>
     * Calculate the correlation coefficient. This standard SQL function may be supported
     * natively, or emulated using {@link DSL#covarPop(Field, Field)} and {@link DSL#stddevPop(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface Corr
        extends
            AggregateFunction<BigDecimal, Corr>
        //permits
        //    Corr
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull Corr $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull Corr $x(Field<? extends Number> x);
    }

    /**
     * The <code>COUNT</code> function.
     */
    public /*sealed*/ interface Count
        extends
            AggregateFunction<Integer, Count>
        //permits
        //    Count
    {
        @Nullable Field<?> $field();
        boolean $distinct();
        @CheckReturnValue
        @NotNull Count $field(Field<?> field);
        @CheckReturnValue
        @NotNull Count $distinct(boolean distinct);
    }

    /**
     * The <code>COVAR SAMP</code> function.
     * <p>
     * Calculate the sample covariance. This standard SQL function may be supported natively,
     * or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}. If an emulation
     * is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface CovarSamp
        extends
            AggregateFunction<BigDecimal, CovarSamp>
        //permits
        //    CovarSamp
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull CovarSamp $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull CovarSamp $x(Field<? extends Number> x);
    }

    /**
     * The <code>COVAR POP</code> function.
     * <p>
     * Calculate the population covariance. This standard SQL function may be supported
     * natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface CovarPop
        extends
            AggregateFunction<BigDecimal, CovarPop>
        //permits
        //    CovarPop
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull CovarPop $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull CovarPop $x(Field<? extends Number> x);
    }

    /**
     * The <code>MAX</code> function.
     */
    public /*sealed*/ interface Max<T>
        extends
            AggregateFunction<T, Max<T>>
        //permits
        //    Max
    {
        @NotNull Field<T> $field();
        boolean $distinct();
        @CheckReturnValue
        @NotNull Max<T> $field(Field<T> field);
        @CheckReturnValue
        @NotNull Max<T> $distinct(boolean distinct);
    }

    /**
     * The <code>MAX BY</code> function.
     * <p>
     * Evaluate <code>value</code> at the row having the maximum value for <code>by</code>.
     */
    public /*sealed*/ interface MaxBy<T>
        extends
            OrderedAggregateFunction<T, MaxBy<T>>
        //permits
        //    MaxBy
    {

        /**
         * The returned value.
         */
        @NotNull Field<T> $value();

        /**
         * The expression to use to evaluate the maximum.
         */
        @NotNull Field<?> $by();

        /**
         * The returned value.
         */
        @CheckReturnValue
        @NotNull MaxBy<T> $value(Field<T> value);

        /**
         * The expression to use to evaluate the maximum.
         */
        @CheckReturnValue
        @NotNull MaxBy<T> $by(Field<?> by);
    }

    /**
     * The <code>MEDIAN</code> function.
     */
    public /*sealed*/ interface Median
        extends
            AggregateFunction<BigDecimal, Median>
        //permits
        //    Median
    {
        @NotNull Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull Median $field(Field<? extends Number> field);
    }

    /**
     * The <code>MIN</code> function.
     */
    public /*sealed*/ interface Min<T>
        extends
            AggregateFunction<T, Min<T>>
        //permits
        //    Min
    {
        @NotNull Field<T> $field();
        boolean $distinct();
        @CheckReturnValue
        @NotNull Min<T> $field(Field<T> field);
        @CheckReturnValue
        @NotNull Min<T> $distinct(boolean distinct);
    }

    /**
     * The <code>MIN BY</code> function.
     * <p>
     * Evaluate <code>value</code> at the row having the minimum value for <code>by</code>.
     */
    public /*sealed*/ interface MinBy<T>
        extends
            OrderedAggregateFunction<T, MinBy<T>>
        //permits
        //    MinBy
    {

        /**
         * The returned value.
         */
        @NotNull Field<T> $value();

        /**
         * The expression to use to evaluate the minimum
         */
        @NotNull Field<?> $by();

        /**
         * The returned value.
         */
        @CheckReturnValue
        @NotNull MinBy<T> $value(Field<T> value);

        /**
         * The expression to use to evaluate the minimum
         */
        @CheckReturnValue
        @NotNull MinBy<T> $by(Field<?> by);
    }

    /**
     * The <code>PRODUCT</code> function.
     * <p>
     * Get the sum over a numeric field: product(distinct field).
     * <p>
     * Few dialects currently support multiplicative aggregation natively. jOOQ
     * emulates this using <code>exp(sum(log(arg)))</code> for strictly positive
     * numbers, and does some additional handling for zero and negative numbers.
     * <p>
     * Note that this implementation may introduce rounding errors, even for
     * integer multiplication.
     * <p>
     * More information here: <a href=
     * "https://blog.jooq.org/how-to-write-a-multiplication-aggregate-function-in-sql">https://blog.jooq.org/how-to-write-a-multiplication-aggregate-function-in-sql</a>.
     */
    public /*sealed*/ interface Product
        extends
            AggregateFunction<BigDecimal, Product>
        //permits
        //    Product
    {
        @NotNull Field<? extends Number> $field();
        boolean $distinct();
        @CheckReturnValue
        @NotNull Product $field(Field<? extends Number> field);
        @CheckReturnValue
        @NotNull Product $distinct(boolean distinct);
    }

    /**
     * The <code>RANK</code> function.
     * <p>
     * The <code>RANK</code> hypothetical set aggregate function.
     */
    public /*sealed*/ interface RankAgg
        extends
            OrderedAggregateFunction<Integer, RankAgg>
        //permits
        //    RankAgg
    {
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @CheckReturnValue
        @NotNull RankAgg $fields(Collection<? extends Field<?>> fields);
    }

    /**
     * The <code>DENSE RANK</code> function.
     * <p>
     * The <code>DENSE_RANK</code> hypothetical set aggregate function.
     */
    public /*sealed*/ interface DenseRankAgg
        extends
            OrderedAggregateFunction<Integer, DenseRankAgg>
        //permits
        //    DenseRankAgg
    {
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @CheckReturnValue
        @NotNull DenseRankAgg $fields(Collection<? extends Field<?>> fields);
    }

    /**
     * The <code>PERCENT RANK</code> function.
     * <p>
     * The <code>PERCENT_RANK</code> hypothetical set aggregate function.
     */
    public /*sealed*/ interface PercentRankAgg
        extends
            OrderedAggregateFunction<BigDecimal, PercentRankAgg>
        //permits
        //    PercentRankAgg
    {
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @CheckReturnValue
        @NotNull PercentRankAgg $fields(Collection<? extends Field<?>> fields);
    }

    /**
     * The <code>CUME DIST</code> function.
     * <p>
     * The <code>CUME_DIST</code> hypothetical set aggregate function.
     */
    public /*sealed*/ interface CumeDistAgg
        extends
            OrderedAggregateFunction<BigDecimal, CumeDistAgg>
        //permits
        //    CumeDistAgg
    {
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @CheckReturnValue
        @NotNull CumeDistAgg $fields(Collection<? extends Field<?>> fields);
    }

    /**
     * The <code>PERCENTILE CONT</code> function.
     * <p>
     * Calculate the <code>PERCENTILE_CONT</code> inverse distribution aggregate function.
     */
    public /*sealed*/ interface PercentileCont
        extends
            OrderedAggregateFunction<BigDecimal, PercentileCont>
        //permits
        //    PercentileCont
    {
        @NotNull Field<? extends Number> $percentile();
        @CheckReturnValue
        @NotNull PercentileCont $percentile(Field<? extends Number> percentile);
    }

    /**
     * The <code>PERCENTILE DISC</code> function.
     * <p>
     * Calculate the <code>PERCENTILE_DISC</code> inverse distribution aggregate function.
     */
    public /*sealed*/ interface PercentileDisc
        extends
            OrderedAggregateFunction<BigDecimal, PercentileDisc>
        //permits
        //    PercentileDisc
    {
        @NotNull Field<? extends Number> $percentile();
        @CheckReturnValue
        @NotNull PercentileDisc $percentile(Field<? extends Number> percentile);
    }

    /**
     * The <code>REGR AVGX</code> function.
     * <p>
     * Calculate the average of the independent values (x). This standard SQL function may
     * be supported natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrAvgX
        extends
            AggregateFunction<BigDecimal, RegrAvgX>
        //permits
        //    RegrAvgX
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrAvgX $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrAvgX $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR AVGY</code> function.
     * <p>
     * Calculate the average of the dependent values (y). This standard SQL function may
     * be supported natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrAvgY
        extends
            AggregateFunction<BigDecimal, RegrAvgY>
        //permits
        //    RegrAvgY
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrAvgY $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrAvgY $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR COUNT</code> function.
     * <p>
     * Calculate the number of non-<code>NULL</code> pairs. This standard SQL function may
     * be supported natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrCount
        extends
            AggregateFunction<BigDecimal, RegrCount>
        //permits
        //    RegrCount
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrCount $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrCount $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR INTERCEPT</code> function.
     * <p>
     * Calculate the y intercept of the regression line. This standard SQL function may
     * be supported natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrIntercept
        extends
            AggregateFunction<BigDecimal, RegrIntercept>
        //permits
        //    RegrIntercept
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrIntercept $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrIntercept $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR R2</code> function.
     * <p>
     * Calculate the coefficient of determination. This standard SQL function may be supported
     * natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrR2
        extends
            AggregateFunction<BigDecimal, RegrR2>
        //permits
        //    RegrR2
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrR2 $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrR2 $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR SLOPE</code> function.
     * <p>
     * Calculate the slope of the regression line. This standard SQL function may be supported
     * natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrSlope
        extends
            AggregateFunction<BigDecimal, RegrSlope>
        //permits
        //    RegrSlope
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrSlope $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrSlope $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR SXX</code> function.
     * <p>
     * Calculate the <code>REGR_SXX</code> auxiliary function. This standard SQL function
     * may be supported natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrSxx
        extends
            AggregateFunction<BigDecimal, RegrSxx>
        //permits
        //    RegrSxx
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrSxx $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrSxx $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR SXY</code> function.
     * <p>
     * Calculate the <code>REGR_SXY</code> auxiliary function. This standard SQL function
     * may be supported natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrSxy
        extends
            AggregateFunction<BigDecimal, RegrSxy>
        //permits
        //    RegrSxy
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrSxy $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrSxy $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR SYY</code> function.
     * <p>
     * Calculate the <code>REGR_SYY</code> auxiliary function. This standard SQL function
     * may be supported natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface RegrSyy
        extends
            AggregateFunction<BigDecimal, RegrSyy>
        //permits
        //    RegrSyy
    {
        @NotNull Field<? extends Number> $y();
        @NotNull Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull RegrSyy $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull RegrSyy $x(Field<? extends Number> x);
    }

    /**
     * The <code>STDDEV POP</code> function.
     * <p>
     * Calculate the population standard deviation. This standard SQL function may be supported
     * natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface StddevPop
        extends
            AggregateFunction<BigDecimal, StddevPop>
        //permits
        //    StddevPop
    {
        @NotNull Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull StddevPop $field(Field<? extends Number> field);
    }

    /**
     * The <code>STDDEV SAMP</code> function.
     * <p>
     * Calculate the sample standard deviation. This standard SQL function may be supported
     * natively, or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface StddevSamp
        extends
            AggregateFunction<BigDecimal, StddevSamp>
        //permits
        //    StddevSamp
    {
        @NotNull Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull StddevSamp $field(Field<? extends Number> field);
    }

    /**
     * The <code>SUM</code> function.
     */
    public /*sealed*/ interface Sum
        extends
            AggregateFunction<BigDecimal, Sum>
        //permits
        //    Sum
    {
        @NotNull Field<? extends Number> $field();
        boolean $distinct();
        @CheckReturnValue
        @NotNull Sum $field(Field<? extends Number> field);
        @CheckReturnValue
        @NotNull Sum $distinct(boolean distinct);
    }

    /**
     * The <code>VAR POP</code> function.
     * <p>
     * Calculate the population variance. This standard SQL function may be supported natively,
     * or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}. If an emulation
     * is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface VarPop
        extends
            AggregateFunction<BigDecimal, VarPop>
        //permits
        //    VarPop
    {
        @NotNull Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull VarPop $field(Field<? extends Number> field);
    }

    /**
     * The <code>VAR SAMP</code> function.
     * <p>
     * Calculate the sample variance. This standard SQL function may be supported natively,
     * or emulated using {@link DSL#sum(Field)} and {@link DSL#count(Field)}. If an emulation
     * is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public /*sealed*/ interface VarSamp
        extends
            AggregateFunction<BigDecimal, VarSamp>
        //permits
        //    VarSamp
    {
        @NotNull Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull VarSamp $field(Field<? extends Number> field);
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
     * The <code>GenerationMode</code> type.
     * <p>
     * Specify whether an identity values is always generated, or only if an explicit value
     * is absent.
     */
    public enum GenerationMode {
        ALWAYS(keyword("always")),
        BY_DEFAULT(keyword("by default")),
        ;

        final Keyword keyword;

        private GenerationMode(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>GenerationLocation</code> type.
     * <p>
     * Specify where a computed column should be computed, i.e. in the client or on the
     * server.
     */
    public enum GenerationLocation {
        CLIENT(keyword("client")),
        SERVER(keyword("server")),
        ;

        final Keyword keyword;

        private GenerationLocation(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>GenerationOption</code> type.
     * <p>
     * Specify whether a computed column should be stored, or computed virtually / on the
     * fly.
     */
    public enum GenerationOption {
        STORED(keyword("stored")),
        VIRTUAL(keyword("virtual")),
        DEFAULT(keyword("default")),
        ;

        final Keyword keyword;

        private GenerationOption(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>WithOrWithoutData</code> type.
     * <p>
     * Specify whether a table created from a subquery should include the subquery's data.
     */
    public enum WithOrWithoutData {
        WITH_DATA(keyword("with data")),
        WITH_NO_DATA(keyword("with no data")),
        ;

        final Keyword keyword;

        private WithOrWithoutData(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>TableCommitAction</code> type.
     * <p>
     * Specify the action to be taken on temporary tables when committing.
     */
    public enum TableCommitAction {
        DELETE_ROWS(keyword("delete rows")),
        PRESERVE_ROWS(keyword("preserve rows")),
        DROP(keyword("drop")),
        ;

        final Keyword keyword;

        private TableCommitAction(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>ForeignKeyRule</code> type.
     * <p>
     * Specify the update rule or delete rule of a foreign key, when the referenced primary
     * key is updated or deleted.
     */
    public enum ForeignKeyRule {
        CASCADE(keyword("cascade")),
        SET_NULL(keyword("set null")),
        SET_DEFAULT(keyword("set default")),
        RESTRICT(keyword("restrict")),
        NO_ACTION(keyword("no action")),
        ;

        final Keyword keyword;

        private ForeignKeyRule(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>TableScope</code> type.
     * <p>
     * Specify the table scope of a temporary table.
     */
    public enum TableScope {
        LOCAL_TEMPORARY(keyword("local temporary")),
        GLOBAL_TEMPORARY(keyword("global temporary")),
        TEMPORARY(keyword("temporary")),
        ;

        final Keyword keyword;

        private TableScope(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>CommentObjectType</code> type.
     * <p>
     * Specify the object type receiving a COMMENT.
     */
    public enum CommentObjectType {
        COLUMN(keyword("column")),
        FUNCTION(keyword("function")),
        MATERIALIZED_VIEW(keyword("materialized view")),
        PROCEDURE(keyword("procedure")),
        TABLE(keyword("table")),
        VIEW(keyword("view")),
        ;

        final Keyword keyword;

        private CommentObjectType(Keyword keyword) {
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
     * The <code>XMLPassingMechanism</code> type.
     * <p>
     * Specify how XML contents should be passed to certain XML functions.
     */
    public enum XMLPassingMechanism {
        BY_REF(keyword("by ref")),
        BY_VALUE(keyword("by value")),
        DEFAULT(keyword("default")),
        ;

        final Keyword keyword;

        private XMLPassingMechanism(Keyword keyword) {
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

    /**
     * The <code>Quantifier</code> type.
     * <p>
     * A quantifier for quantified selects.
     */
    public enum Quantifier {
        ANY(keyword("any")),
        ALL(keyword("all")),
        ;

        final Keyword keyword;

        private Quantifier(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>JoinHint</code> type.
     * <p>
     * A hint for join algorithms.
     */
    public enum JoinHint {
        HASH(keyword("hash")),
        LOOP(keyword("loop")),
        MERGE(keyword("merge")),
        ;

        final Keyword keyword;

        private JoinHint(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>SampleMethod</code> type.
     * <p>
     * The table sampling method.
     */
    public enum SampleMethod {
        BERNOULLI(keyword("bernoulli")),
        SYSTEM(keyword("system")),
        ;

        final Keyword keyword;

        private SampleMethod(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * The <code>SampleSizeType</code> type.
     * <p>
     * The table sampling size type.
     */
    public enum SampleSizeType {
        ROWS(keyword("rows")),
        PERCENT(keyword("percent")),
        ;

        final Keyword keyword;

        private SampleSizeType(Keyword keyword) {
            this.keyword = keyword;
        }
    }



    // -------------------------------------------------------------------------
    // XXX: Utility API
    // -------------------------------------------------------------------------

    interface UOperator<R extends org.jooq.QueryPart> extends org.jooq.QueryPart {
        @NotNull
        List<?> $args();
    }
    interface UOperator0<R extends UOperator0<R>> extends UOperator<R> {

        @NotNull
        Function0<? extends R> $constructor();

        @NotNull
        @Override
        default List<?> $args() {
            return Collections.emptyList();
        }



















    }

    interface UOperator1<Q1, R extends UOperator1<Q1, R>> extends UOperator<R> {
        Q1 $arg1();

        @CheckReturnValue
        @NotNull default R $arg1(Q1 newArg1) { return $constructor().apply(newArg1); }

        @NotNull
        Function1<? super Q1, ? extends R> $constructor();

        @NotNull
        @Override
        default List<?> $args() {
            return unmodifiableList(asList($arg1()));
        }




















    }

    interface UOperator2<Q1, Q2, R extends UOperator2<Q1, Q2, R>> extends UOperator<R> {
        Q1 $arg1();
        Q2 $arg2();

        @CheckReturnValue
        @NotNull default R $arg1(Q1 newArg1) { return $constructor().apply(newArg1, $arg2()); }
        @CheckReturnValue
        @NotNull default R $arg2(Q2 newArg2) { return $constructor().apply($arg1(), newArg2); }

        @NotNull
        Function2<? super Q1, ? super Q2, ? extends R> $constructor();

        @NotNull
        @Override
        default List<?> $args() {
            return unmodifiableList(asList($arg1(), $arg2()));
        }





















    }

    interface UOperator3<Q1, Q2, Q3, R extends UOperator3<Q1, Q2, Q3, R>> extends UOperator<R> {
        Q1 $arg1();
        Q2 $arg2();
        Q3 $arg3();

        @CheckReturnValue
        @NotNull default R $arg1(Q1 newArg1) { return $constructor().apply(newArg1, $arg2(), $arg3()); }
        @CheckReturnValue
        @NotNull default R $arg2(Q2 newArg2) { return $constructor().apply($arg1(), newArg2, $arg3()); }
        @CheckReturnValue
        @NotNull default R $arg3(Q3 newArg3) { return $constructor().apply($arg1(), $arg2(), newArg3); }

        @NotNull
        Function3<? super Q1, ? super Q2, ? super Q3, ? extends R> $constructor();

        @NotNull
        @Override
        default List<?> $args() {
            return unmodifiableList(asList($arg1(), $arg2(), $arg3()));
        }






















    }

    interface UOperator4<Q1, Q2, Q3, Q4, R extends UOperator4<Q1, Q2, Q3, Q4, R>> extends UOperator<R> {
        Q1 $arg1();
        Q2 $arg2();
        Q3 $arg3();
        Q4 $arg4();

        @CheckReturnValue
        @NotNull default R $arg1(Q1 newArg1) { return $constructor().apply(newArg1, $arg2(), $arg3(), $arg4()); }
        @CheckReturnValue
        @NotNull default R $arg2(Q2 newArg2) { return $constructor().apply($arg1(), newArg2, $arg3(), $arg4()); }
        @CheckReturnValue
        @NotNull default R $arg3(Q3 newArg3) { return $constructor().apply($arg1(), $arg2(), newArg3, $arg4()); }
        @CheckReturnValue
        @NotNull default R $arg4(Q4 newArg4) { return $constructor().apply($arg1(), $arg2(), $arg3(), newArg4); }

        @NotNull
        Function4<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? extends R> $constructor();

        @NotNull
        @Override
        default List<?> $args() {
            return unmodifiableList(asList($arg1(), $arg2(), $arg3(), $arg4()));
        }























    }

    interface UOperator5<Q1, Q2, Q3, Q4, Q5, R extends UOperator5<Q1, Q2, Q3, Q4, Q5, R>> extends UOperator<R> {
        Q1 $arg1();
        Q2 $arg2();
        Q3 $arg3();
        Q4 $arg4();
        Q5 $arg5();

        @CheckReturnValue
        @NotNull default R $arg1(Q1 newArg1) { return $constructor().apply(newArg1, $arg2(), $arg3(), $arg4(), $arg5()); }
        @CheckReturnValue
        @NotNull default R $arg2(Q2 newArg2) { return $constructor().apply($arg1(), newArg2, $arg3(), $arg4(), $arg5()); }
        @CheckReturnValue
        @NotNull default R $arg3(Q3 newArg3) { return $constructor().apply($arg1(), $arg2(), newArg3, $arg4(), $arg5()); }
        @CheckReturnValue
        @NotNull default R $arg4(Q4 newArg4) { return $constructor().apply($arg1(), $arg2(), $arg3(), newArg4, $arg5()); }
        @CheckReturnValue
        @NotNull default R $arg5(Q5 newArg5) { return $constructor().apply($arg1(), $arg2(), $arg3(), $arg4(), newArg5); }

        @NotNull
        Function5<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? extends R> $constructor();

        @NotNull
        @Override
        default List<?> $args() {
            return unmodifiableList(asList($arg1(), $arg2(), $arg3(), $arg4(), $arg5()));
        }
























    }

    /**
     * A binary {@link UOperator2} whose operands can be swapped using
     * {@link #$converse()} producing the converse relation.
     */
    interface UConvertibleOperator<Q, R extends UConvertibleOperator<Q, R, C>, C extends UConvertibleOperator<Q, C, R>> extends UOperator2<Q, Q, R> {

        /**
         * Create a new expression with swapped operands, using the converse
         * operator.
         */
        @CheckReturnValue
        @NotNull C $converse();
    }

    /**
     * A binary {@link UOperator2} whose operands can be swapped using
     * {@link #$swap()} without changing the operator's semantics.
     */
    interface UCommutativeOperator<Q, R extends UCommutativeOperator<Q, R>> extends UConvertibleOperator<Q, R, R> {

        /**
         * Create a new expression with swapped operands.
         */
        @CheckReturnValue
        @NotNull default R $swap() {
            return $constructor().apply($arg2(), $arg1());
        }

        @Override
        @CheckReturnValue
        @NotNull default R $converse() {
            return $swap();
        }
    }

    /**
     * A marker interface for {@link QueryPart} implementations that represent
     * functions or operators who evaluate to <code>NULL</code> as soon as at
     * least one argument is <code>NULL</code>.
     */
    interface UReturnsNullOnNullInput extends org.jooq.QueryPart {}

    /**
     * A marker interface for {@link QueryPart} implementations that are used
     * only to render SQL, i.e. they're transient to the expression tree and
     * don't persist in client code.
     */
    interface UTransient extends UEmpty {}

    /**
     * A marker interface for {@link QueryPart} implementations that are mainly
     * used to render SQL, but unlike {@link UTransient} parts, can also appear
     * in user expression trees.
     */
    interface UOpaque
        extends
            UEmpty
    {}

    /**
     * A marker interface for {@link QueryPart} implementations whose
     * {@link QueryPart} semantics has not yet been implemented.
     *
     * @deprecated - [#12425] - 3.16.0 - Missing implementations should be added
     *             as soon as possible!
     */
    @Deprecated(forRemoval = true)
    interface UNotYetImplemented extends UEmpty {}

    /**
     * A marker interface for {@link QueryPart} methods that have not yet been
     * implemented.
     *
     * @deprecated - [#12425] - 3.16.0 - Missing implementations should be added
     *             as soon as possible!
     */
    @Deprecated(forRemoval = true)
    public static class NotYetImplementedException extends RuntimeException {}

    interface UProxy<Q extends org.jooq.QueryPart> extends org.jooq.QueryPart {
        Q $delegate();















    }

    interface UEmpty extends org.jooq.QueryPart {

















    }

    // -------------------------------------------------------------------------
    // XXX: Undisclosed, internal query parts
    // -------------------------------------------------------------------------

    interface UEmptyCondition extends Condition, UEmpty {}
    interface UEmptyField<T> extends Field<T>, UEmpty {}
    interface UEmptyTable<R extends Record> extends Table<R>, UEmpty {}
    interface UEmptyStatement extends Statement, UEmpty {}
    interface UEmptyQuery extends Query, UEmpty {}



















































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































    /**
     * Turn an array into an unmodifiable {@link UnmodifiableList}.
     */
    @Internal
    public static final <Q extends QueryPart> UnmodifiableList<Q> unmodifiable(Q[] array) {
        return unmodifiable(QueryPartListView.wrap(array));
    }

    /**
     * Turn a {@link List} into an unmodifiable {@link UnmodifiableList}.
     */
    @Internal
    public static final <Q extends QueryPart> UnmodifiableList<Q> unmodifiable(List<Q> list) {
        return QueryPartListView.wrap(unmodifiableList(list));
    }

    /**
     * Turn a {@link Collection} into an unmodifiable {@link UnmodifiableList}.
     */
    @Internal
    public static final <Q extends QueryPart> UnmodifiableList<Q> unmodifiable(Collection<Q> collection) {
        if (collection instanceof List)
            return unmodifiable((List<Q>) collection);
        else
            return new QueryPartList<>(unmodifiableCollection(collection));
    }

    /**
     * Turn a {@link Map} into an unmodifiable {@link UnmodifiableMap}.
     */
    @Internal
    public static final <K extends QueryPart, V extends QueryPart> UnmodifiableMap<K, V> unmodifiable(Map<K, V> map) {
        return new QueryPartMapView<>(unmodifiableMap(map));
    }

    @Internal
    public static final <Q1 extends QueryPart, Q2 extends QueryPart> Tuple2<Q1, Q2> tuple(Q1 q1, Q2 q2) {
        return new TupleImpl2<>(q1, q2);
    }

    static final <Q extends QueryPart> boolean commutativeCheck(UCommutativeOperator<Q, ?> op, Predicate<? super Q> f) {
        return f.test(op.$arg1()) || f.test(op.$arg2());
    }

    static final <Q extends QueryPart> boolean commutativeCheck(UCommutativeOperator<Q, ?> op, BiPredicate<? super Q, ? super Q> f) {
        return f.test(op.$arg1(), op.$arg2()) || f.test(op.$arg2(), op.$arg1());
    }
}
