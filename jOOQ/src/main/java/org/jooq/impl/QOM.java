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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

// ...
import org.jooq.Catalog;
import org.jooq.CheckReturnValue;
import org.jooq.Collation;
import org.jooq.Comment;
import org.jooq.CommonTableExpression;
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
// ...
import org.jooq.Name;
import org.jooq.OrderField;
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
import org.jooq.Table;
import org.jooq.TableElement;
import org.jooq.TableLike;
// ...
import org.jooq.UniqueKey;
// ...
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;
import org.jooq.XML;
import org.jooq.XMLAttributes;
import org.jooq.conf.Settings;
import org.jooq.impl.QOM.UCommutativeOperator;
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
 * via {@link QueryPart#$replace(Function1)} as well as via per-querypart
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

    // This class uses a lot of fully qualified types, because of some javac bug
    // In Java 1.8.0_302, which hasn't been analysed and reproduced yet in a more
    // minimal example. Without the qualification, the types cannot be found
    // despite being imported

    /**
     * A generic tuple of degree 2 for use in {@link QOM} types.
     */
    public sealed interface Tuple2<Q1 extends org.jooq.QueryPart, Q2 extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart
        permits
            TupleImpl2
    {
        @NotNull Q1 $1();
        @NotNull Q2 $2();

        @CheckReturnValue
        @NotNull Tuple2<Q1, Q2> $1(Q1 newPart1);
        @CheckReturnValue
        @NotNull Tuple2<Q1, Q2> $2(Q2 newPart2);
    }

    /**
     * An unmodifiable {@link Map} of {@link QueryPart} keys and values.
     */
    public sealed interface UnmodifiableMap<K extends org.jooq.QueryPart, V extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart,
            java.util.Map<K, V>
        permits
            AbstractQueryPartMap
    {
        @NotNull UnmodifiableList<Tuple2<K, V>> $tuples();
    }

    /**
     * An unmodifiable {@link Collection} of {@link QueryPart} elements.
     */
    public sealed interface UnmodifiableCollection<Q extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart,
            java.util.Collection<Q>
        permits
            UnmodifiableList,
            QueryPartCollectionView
    {}

    /**
     * An unmodifiable {@link List} of {@link QueryPart} elements.
     */
    public sealed interface UnmodifiableList<Q extends org.jooq.QueryPart>
        extends
            UnmodifiableCollection<Q>,
            java.util.List<Q>
        permits
            QueryPartListView
    {

        // TODO: These methods could return unmodifiable views instead, to avoid
        //       copying things around...

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

    public sealed interface With
        extends
            org.jooq.QueryPart
        permits
            WithImpl
    {
        @NotNull UnmodifiableList<? extends CommonTableExpression<?>> $commonTableExpressions();
        boolean $recursive();
    }

    public interface Aliasable<Q extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart
    {
        @NotNull Q $aliased();
        @Nullable Name $alias();
    }

    // -------------------------------------------------------------------------
    // XXX: Queries
    // -------------------------------------------------------------------------

    public sealed interface Insert<R extends Record>
        extends
            DMLQuery<R>
        permits
            InsertImpl,
            InsertQueryImpl
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

    public sealed interface InsertReturning<R extends Record>
        extends
            ResultQuery<R>
        permits
            InsertAsResultQuery
    {
        @NotNull Insert<?> $insert();
        @CheckReturnValue
        @NotNull InsertReturning<R> $insert(Insert<?> insert);
        @NotNull UnmodifiableList<? extends SelectFieldOrAsterisk> $returning();
        @CheckReturnValue
        @NotNull InsertReturning<?> $returning(Collection<? extends SelectFieldOrAsterisk> returning);
    }

    public sealed interface Update<R extends Record>
        extends
            DMLQuery<R>
        permits
            UpdateImpl,
            UpdateQueryImpl
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

    public sealed interface UpdateReturning<R extends Record>
        extends
            ResultQuery<R>
        permits
            UpdateAsResultQuery
    {
        @NotNull Update<?> $update();
        @CheckReturnValue
        @NotNull UpdateReturning<R> $update(Update<?> update);
        @NotNull UnmodifiableList<? extends SelectFieldOrAsterisk> $returning();
        @CheckReturnValue
        @NotNull UpdateReturning<?> $returning(Collection<? extends SelectFieldOrAsterisk> returning);
    }

    public sealed interface Delete<R extends Record>
        extends
            DMLQuery<R>
        permits
            DeleteImpl,
            DeleteQueryImpl
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

    public sealed interface DeleteReturning<R extends Record>
        extends
            ResultQuery<R>
        permits
            DeleteAsResultQuery
    {
        @NotNull Delete<?> $delete();
        @CheckReturnValue
        @NotNull DeleteReturning<R> $delete(Delete<?> delete);
        @NotNull UnmodifiableList<? extends SelectFieldOrAsterisk> $returning();
        @CheckReturnValue
        @NotNull DeleteReturning<?> $returning(Collection<? extends SelectFieldOrAsterisk> returning);
    }

    public sealed interface CreateType
        extends
            DDLQuery
        permits
            CreateTypeImpl
    {
        @NotNull Name $name();
        @NotNull UnmodifiableList<? extends Field<String>> $values();
    }

    public sealed interface DropType
        extends
            DDLQuery
        permits
            DropTypeImpl
    {
        @NotNull UnmodifiableList<? extends Name> $names();
        boolean $ifExists();
        @Nullable Cascade $cascade();
    }

    public sealed interface CreateView<R extends Record>
        extends
            DDLQuery
        permits
            CreateViewImpl
    {
        boolean $ifNotExists();
        boolean $orReplace();
        @NotNull Table<?> $view();
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @NotNull ResultQuery<R> $query();
    }

    // -------------------------------------------------------------------------
    // XXX: Schema
    // -------------------------------------------------------------------------














    public interface PrimaryKey extends Constraint {
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
    }
    public interface UniqueKey extends Constraint {
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
    }
    public interface ForeignKey extends Constraint {
        @NotNull UnmodifiableList<? extends Field<?>> $fields();
        @NotNull Constraint $references();
    }
    public interface Check extends Constraint {
        @NotNull Condition $condition();
    }

    // -------------------------------------------------------------------------
    // XXX: Statements
    // -------------------------------------------------------------------------

    public sealed interface NullStatement
        extends
            Statement
        permits
            org.jooq.impl.NullStatement
    {}








































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

    public sealed interface Dual
        extends
            Table<Record>,
            UEmpty
        permits
            org.jooq.impl.Dual
    {}

    public sealed interface Lateral<R extends Record>
        extends
            Table<R>,
            UOperator1<Table<R>, Table<R>>
        permits
            org.jooq.impl.Lateral
    {}

    public sealed interface DerivedTable<R extends org.jooq.Record>
        extends
            org.jooq.Table<R>,
            UOperator1<org.jooq.Select<R>, org.jooq.Table<R>>
        permits
            org.jooq.impl.DerivedTable
    {}

    public sealed interface Values<R extends Record>
        extends
            Table<R>,
            UOperator1<UnmodifiableList<? extends Row>, Table<R>>
        permits
            org.jooq.impl.Values
    {}

    public sealed interface DataChangeDeltaTable<R extends Record>
        extends
            Table<R>
        permits
            org.jooq.impl.DataChangeDeltaTable
    {
        @NotNull ResultOption $resultOption();
        @NotNull DMLQuery<R> $query();
    }

    public sealed interface RowsFrom
        extends
            Table<Record>
        permits
            org.jooq.impl.RowsFrom
    {
        @NotNull UnmodifiableList<? extends Table<?>> $tables();
    }

    public sealed interface GenerateSeries<T>
        extends
            Table<Record1<T>>,
            UOperator3<Field<T>, Field<T>, Field<T>, Table<Record1<T>>>
        permits
            org.jooq.impl.GenerateSeries
    {
        @NotNull default Field<T> $from() { return $arg1(); }
        @NotNull default Field<T> $to() { return $arg2(); }
        @Nullable default Field<T> $step() { return $arg3(); }
    }

    // -------------------------------------------------------------------------
    // XXX: Conditions
    // -------------------------------------------------------------------------

    public sealed interface CombinedCondition
        extends
            Condition,
            UCommutativeOperator<Condition, Condition>
        permits
            And,
            Or,
            Xor
    {}

    public sealed interface CompareCondition<T>
        extends
            Condition,
            UOperator2<Field<T>, Field<T>, Condition>
        permits
            Eq,
            Ne,
            Lt,
            Le,
            Gt,
            Ge,
            IsDistinctFrom,
            IsNotDistinctFrom,
            Contains,
            ContainsIgnoreCase,
            StartsWith,
            StartsWithIgnoreCase,
            EndsWith,
            EndsWithIgnoreCase
    {}

    public sealed interface Between<T>
        extends
            Condition,
            UOperator3<Field<T>, Field<T>, Field<T>, Condition>
        permits
            org.jooq.impl.BetweenCondition
    {
        boolean $symmetric();
        @NotNull Between<T> $symmetric(boolean symmetric);
    }

    public sealed interface InList<T>
        extends
            Condition,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, Condition>
        permits
            org.jooq.impl.InList
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        @NotNull default UnmodifiableList<? extends Field<T>> $list() { return $arg2(); }
    }

    public sealed interface NotInList<T>
        extends
            Condition,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, Condition>
        permits
            org.jooq.impl.NotInList
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        @NotNull default UnmodifiableList<? extends Field<T>> $list() { return $arg2(); }
    }

    public sealed interface RegexpLike
        extends
            Condition
        permits
            org.jooq.impl.RegexpLike
    {
        @NotNull Field<?> $search();
        @NotNull Field<String> $pattern();
    }

    public sealed interface Extract
        extends
            Field<Integer>
        permits
            org.jooq.impl.Extract
    {
        @NotNull Field<?> $field();
        @NotNull DatePart $datePart();
    }

    public sealed interface RowIsNull
        extends
            Condition,
            UOperator1<Row, Condition>
        permits
            org.jooq.impl.RowIsNull
    {
        @NotNull default Row $field() { return $arg1(); }
    }

    public sealed interface RowIsNotNull
        extends
            Condition,
            UOperator1<Row, Condition>
        permits
            org.jooq.impl.RowIsNotNull
    {
        @NotNull default Row $field() { return $arg1(); }
    }

    public sealed interface RowOverlaps
        extends
            Condition,
            UOperator2<Row, Row, Condition>
        permits
            org.jooq.impl.RowOverlaps
    {}

    public sealed interface SelectIsNull
        extends
            Condition,
            UOperator1<Select<?>, Condition>
        permits
            org.jooq.impl.SelectIsNull
    {
        @NotNull default Select<?> $field() { return $arg1(); }
    }

    public sealed interface SelectIsNotNull
        extends
            Condition,
            UOperator1<Select<?>, Condition>
        permits
            org.jooq.impl.SelectIsNotNull
    {
        @NotNull default Select<?> $field() { return $arg1(); }
    }


    // -------------------------------------------------------------------------
    // XXX: Rows
    // -------------------------------------------------------------------------

    public sealed interface RowAsField<R extends org.jooq.Record>
        extends
            org.jooq.Field<R>
        permits
            org.jooq.impl.RowAsField
    {
        @NotNull Row $row();
    }

    public sealed interface TableAsField<R extends org.jooq.Record>
        extends
            org.jooq.Field<R>
        permits
            org.jooq.impl.TableAsField
    {
        @NotNull Table<R> $table();
    }

    public sealed interface JoinTable<R extends org.jooq.Record, J extends JoinTable<R, J>>
        extends
            org.jooq.Table<R>
        permits
            CrossJoin,
            CrossApply,
            OuterApply,
            NaturalJoin,
            NaturalLeftJoin,
            NaturalRightJoin,
            NaturalFullJoin,
            QualifiedJoin
    {
        @NotNull Table<?> $table1();
        @NotNull Table<?> $table2();
        @NotNull J $table1(Table<?> table1);
        @NotNull J $table2(Table<?> table2);
    }

    public sealed interface CrossJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, CrossJoin<R>>
        permits
            org.jooq.impl.CrossJoin
    {}

    public sealed interface CrossApply<R extends org.jooq.Record>
        extends
            JoinTable<R, CrossApply<R>>
        permits
            org.jooq.impl.CrossApply
    {}

    public sealed interface OuterApply<R extends org.jooq.Record>
        extends
            JoinTable<R, OuterApply<R>>
        permits
            org.jooq.impl.OuterApply
    {}

    public sealed interface NaturalJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, NaturalJoin<R>>
        permits
            org.jooq.impl.NaturalJoin
    {}

    public sealed interface NaturalLeftJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, NaturalLeftJoin<R>>
        permits
            org.jooq.impl.NaturalLeftJoin
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy2();
        @NotNull NaturalLeftJoin<R> $partitionBy2(Collection<? extends Field<?>> partitionBy2);
    }

    public sealed interface NaturalRightJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, NaturalRightJoin<R>>
        permits
            org.jooq.impl.NaturalRightJoin
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy1();
        @NotNull NaturalRightJoin<R> $partitionBy1(Collection<? extends Field<?>> partitionBy1);
    }

    public sealed interface NaturalFullJoin<R extends org.jooq.Record>
        extends
            JoinTable<R, NaturalFullJoin<R>>
        permits
            org.jooq.impl.NaturalFullJoin
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy1();
        @NotNull NaturalFullJoin<R> $partitionBy1(Collection<? extends Field<?>> partitionBy1);
        @NotNull UnmodifiableList<Field<?>> $partitionBy2();
        @NotNull NaturalFullJoin<R> $partitionBy2(Collection<? extends Field<?>> partitionBy2);
    }

    public sealed interface QualifiedJoin<R extends org.jooq.Record, J extends QualifiedJoin<R, J>>
        extends
            JoinTable<R, J>
        permits
            Join,
            StraightJoin,
            LeftJoin,
            RightJoin,
            FullJoin,
            LeftSemiJoin,
            LeftAntiJoin
    {
        @Nullable Condition $on();
        @NotNull J $on(Condition on);
        @NotNull UnmodifiableList<Field<?>> $using();
        @NotNull J $using(Collection<? extends Field<?>> using);
    }

    public sealed interface Join<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, Join<R>>
        permits
            org.jooq.impl.Join
    {}

    public sealed interface StraightJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, StraightJoin<R>>
        permits
            org.jooq.impl.StraightJoin
    {}

    public sealed interface LeftJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, LeftJoin<R>>
        permits
            org.jooq.impl.LeftJoin
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy2();
        @NotNull LeftJoin<R> $partitionBy2(Collection<? extends Field<?>> partitionBy2);
    }

    public sealed interface RightJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, RightJoin<R>>
        permits
            org.jooq.impl.RightJoin
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy1();
        @NotNull RightJoin<R> $partitionBy1(Collection<? extends Field<?>> partitionBy1);
    }

    public sealed interface FullJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, FullJoin<R>>
        permits
            org.jooq.impl.FullJoin
    {
        @NotNull UnmodifiableList<Field<?>> $partitionBy1();
        @NotNull FullJoin<R> $partitionBy1(Collection<? extends Field<?>> partitionBy1);
        @NotNull UnmodifiableList<Field<?>> $partitionBy2();
        @NotNull FullJoin<R> $partitionBy2(Collection<? extends Field<?>> partitionBy2);
    }

    public sealed interface LeftSemiJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, LeftSemiJoin<R>>
        permits
            org.jooq.impl.LeftSemiJoin
    {}

    public sealed interface LeftAntiJoin<R extends org.jooq.Record>
        extends
            QualifiedJoin<R, LeftAntiJoin<R>>
        permits
            org.jooq.impl.LeftAntiJoin
    {}

    // -------------------------------------------------------------------------
    // XXX: SelectFields, GroupFields and SortFields
    // -------------------------------------------------------------------------

    public sealed interface EmptyGroupingSet
        extends
            GroupField,
            UEmpty
        permits
            org.jooq.impl.EmptyGroupingSet
    {}

    public sealed interface Rollup
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends FieldOrRow>, GroupField>
        permits
            org.jooq.impl.Rollup
    {}

    public sealed interface Cube
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends FieldOrRow>, GroupField>
        permits
            org.jooq.impl.Cube
    {}

    public sealed interface GroupingSets
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends UnmodifiableList<? extends FieldOrRow>>, GroupField>
        permits
            org.jooq.impl.GroupingSets
    {}

    // -------------------------------------------------------------------------
    // XXX: Aggregate functions and window functions
    // -------------------------------------------------------------------------

    public sealed interface RatioToReport
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RatioToReport
    {
        @NotNull Field<? extends Number> $field();
    }

    public sealed interface Mode<T>
        extends
            org.jooq.AggregateFunction<T>,
            UOperator1<Field<T>, org.jooq.AggregateFunction<T>>
        permits
            org.jooq.impl.Mode
    {
        @NotNull default Field<T> $field() { return $arg1(); }
    }

    public sealed interface MultisetAgg<R extends Record>
        extends
            org.jooq.AggregateFunction<Result<R>>
        permits
            org.jooq.impl.MultisetAgg
    {
        @NotNull Row $row();
    }

    public sealed interface ArrayAgg<T>
        extends
            org.jooq.AggregateFunction<T[]>,
            UOperator1<Field<T>, org.jooq.AggregateFunction<T[]>>
        permits
            org.jooq.impl.ArrayAgg
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        boolean $distinct();
    }

    public sealed interface XMLAgg
        extends
            org.jooq.AggregateFunction<XML>,
            UOperator1<Field<XML>, org.jooq.AggregateFunction<XML>>
        permits
            org.jooq.impl.XMLAgg
    {
        @NotNull default Field<XML> $field() { return $arg1(); }
    }

    public sealed interface JSONArrayAgg<J>
        extends
            org.jooq.AggregateFunction<J>,
            UOperator1<org.jooq.Field<?>, org.jooq.AggregateFunction<J>>
        permits
            org.jooq.impl.JSONArrayAgg
    {
        @NotNull default Field<?> $field() { return $arg1(); }
        boolean $distinct();
        @Nullable JSONOnNull $onNull();
        @Nullable DataType<?> $returning();
    }

    public sealed interface JSONObjectAgg<J>
        extends
            org.jooq.AggregateFunction<J>,
            UOperator1<JSONEntry<?>, org.jooq.AggregateFunction<J>>
        permits
            org.jooq.impl.JSONObjectAgg
    {
        @NotNull default JSONEntry<?> $entry() { return $arg1(); }
        @Nullable JSONOnNull $onNull();
        @Nullable DataType<?> $returning();
    }

    public sealed interface CountTable
        extends
            org.jooq.AggregateFunction<Integer>
        permits
            org.jooq.impl.CountTable
    {
        @NotNull Table<?> $table();
        boolean $distinct();
    }
















    public interface WindowFunction<T> extends org.jooq.Field<T> {
        @Nullable WindowSpecification $windowSpecification();
        @Nullable WindowDefinition $windowDefinition();
    }

    public sealed interface RowNumber
        extends
            WindowFunction<Integer>
        permits
            org.jooq.impl.RowNumber
    {}

    public sealed interface Rank
        extends
            WindowFunction<Integer>
        permits
            org.jooq.impl.Rank
    {}

    public sealed interface DenseRank
        extends
            WindowFunction<Integer>
        permits
            org.jooq.impl.DenseRank
    {}

    public sealed interface PercentRank
        extends
            WindowFunction<BigDecimal>
        permits
            org.jooq.impl.PercentRank
    {}

    public sealed interface CumeDist
        extends
            WindowFunction<BigDecimal>
        permits
            org.jooq.impl.CumeDist
    {}

    public sealed interface Ntile
        extends
            WindowFunction<Integer>
        permits
            org.jooq.impl.Ntile
    {
        @NotNull Field<Integer> $tiles();
    }

    public sealed interface Lead<T>
        extends
            WindowFunction<T>
        permits
            org.jooq.impl.Lead
    {
        @NotNull Field<T> $field();
        @Nullable Field<Integer> $offset();
        @Nullable Field<T> $defaultValue();
        @Nullable NullTreatment $nullTreatment();
    }

    public sealed interface Lag<T>
        extends
            WindowFunction<T>
        permits
            org.jooq.impl.Lag
    {
        @NotNull Field<T> $field();
        @Nullable Field<Integer> $offset();
        @Nullable Field<T> $defaultValue();
        @Nullable NullTreatment $nullTreatment();
    }

    public sealed interface FirstValue<T>
        extends
            WindowFunction<T>
        permits
            org.jooq.impl.FirstValue
    {
        @NotNull Field<T> $field();
        @Nullable NullTreatment $nullTreatment();
    }

    public sealed interface LastValue<T>
        extends
            WindowFunction<T>
        permits
            org.jooq.impl.LastValue
    {
        @NotNull Field<T> $field();
        @Nullable NullTreatment $nullTreatment();
    }

    public sealed interface NthValue<T>
        extends
            WindowFunction<T>
        permits
            org.jooq.impl.NthValue
    {
        @NotNull Field<T> $field();
        @Nullable FromFirstOrLast $fromFirstOrLast();
        @Nullable NullTreatment $nullTreatment();
    }

    // -------------------------------------------------------------------------
    // XXX: Fields
    // -------------------------------------------------------------------------

    public /*sealed*/ interface FieldAlias<T>
        extends
            Field<T>,
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
            Field<T>
        /*permits
            org.jooq.impl.Function,
            org.jooq.impl.Function1*/
    {
        @NotNull UnmodifiableList<? extends Field<?>> $args();
    }

    public sealed interface Cast<T>
        extends
            Field<T>
        permits
            org.jooq.impl.Cast
    {
        @NotNull Field<?> $field();
    }

    public sealed interface Coerce<T>
        extends
            Field<T>
        permits
            org.jooq.impl.Coerce
    {
        @NotNull Field<?> $field();
    }

    public sealed interface Default<T>
        extends
            Field<T>,
            UEmpty
        permits
            org.jooq.impl.Default
    {}

    public sealed interface Collated
        extends
            Field<String>
        permits
            org.jooq.impl.Collated
    {
        @NotNull Field<?> $field();
        @NotNull Collation $collation();
    }

    public /*sealed*/ interface Array<T>
        extends
            Field<T[]>
        /*permits
            org.jooq.impl.Array*/
    {
        @NotNull UnmodifiableList<? extends Field<?>> $elements();
    }

    public /*sealed*/ interface ArrayQuery<T>
        extends
            Field<T[]>
        /*permits
            ArrayQuery*/
    {
        @NotNull Select<? extends Record1<T>> $select();
    }

    public sealed interface Multiset<R extends org.jooq.Record>
        extends
            org.jooq.Field<org.jooq.Result<R>>
        permits
            org.jooq.impl.Multiset
    {
        @NotNull TableLike<R> $table();
    }

    public sealed interface ScalarSubquery<T>
        extends
            Field<T>,
            UOperator1<Select<? extends Record1<T>>, Field<T>>
        permits
            org.jooq.impl.ScalarSubquery
    {}

    public sealed interface Neg<T>
        extends
            UReturnsNullOnNullInput,
            Field<T>,
            UOperator1<Field<T>, Field<T>>
        permits
            org.jooq.impl.Neg
    {}

    public sealed interface Greatest<T>
        extends
            Field<T>,
            UOperator1<UnmodifiableList<? extends Field<T>>, Field<T>>
        permits
            org.jooq.impl.Greatest
    {}

    public sealed interface Least<T>
        extends
            Field<T>,
            UOperator1<UnmodifiableList<? extends Field<T>>, Field<T>>
        permits
            org.jooq.impl.Least
    {}

    public sealed interface Choose<T>
        extends
            Field<T>,
            UOperator2<Field<Integer>, UnmodifiableList<? extends Field<T>>, Field<T>>
        permits
            org.jooq.impl.Choose
    {}

    public sealed interface FieldFunction<T>
        extends
            Field<Integer>,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, Field<Integer>>
        permits
            org.jooq.impl.FieldFunction
    {}

    public sealed interface Nvl2<T>
        extends
            Field<T>,
            UOperator3<Field<?>, Field<T>, Field<T>, Field<T>>
        permits
            org.jooq.impl.Nvl2
    {
        @NotNull default Field<?> $value() { return $arg1(); }
        @NotNull default Field<T> $ifNotNull() { return $arg2(); }
        @NotNull default Field<T> $ifIfNull() { return $arg3(); }
    }

    public sealed interface Iif<T>
        extends
            Field<T>,
            UOperator3<Condition, Field<T>, Field<T>, Field<T>>
        permits
            org.jooq.impl.Iif
    {
        @NotNull default Condition $condition() { return $arg1(); }
        @NotNull default Field<T> $ifTrue() { return $arg2(); }
        @NotNull default Field<T> $ifFalse() { return $arg3(); }
    }

    public sealed interface Coalesce<T>
        extends
            Field<T>,
            UOperator1<UnmodifiableList<? extends Field<T>>, Field<T>>
        permits
            org.jooq.impl.Coalesce
    {}

    public sealed interface CaseSimple<V, T>
        extends
            Field<T>,
            UOperator3<Field<V>, UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>>, Field<T>, CaseSimple<V, T>>
        permits
            org.jooq.impl.CaseSimple
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

    public sealed interface CaseSearched<T>
        extends
            Field<T>,
            UOperator2<UnmodifiableList<? extends Tuple2<Condition, Field<T>>>, Field<T>, CaseSearched<T>>
        permits
            org.jooq.impl.CaseSearched
    {
        @NotNull  default UnmodifiableList<? extends Tuple2<Condition, Field<T>>> $when() { return $arg1(); }
        @CheckReturnValue
        @NotNull  default CaseSearched<T> $when(UnmodifiableList<? extends Tuple2<Condition, Field<T>>> when) { return $arg1(when); }
        @Nullable default Field<T> $else() { return $arg2(); }
        @CheckReturnValue
        @NotNull  default CaseSearched<T> $else(Field<T> else_) { return $arg2(else_); }
    }

    public sealed interface Decode<V, T>
        extends
            Field<T>,
            UOperator3<Field<V>, UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>>, Field<T>, Decode<V, T>>
        permits
            org.jooq.impl.Decode
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

    public sealed interface Concat
        extends
            Field<String>,
            UOperator1<UnmodifiableList<? extends Field<?>>, Field<String>>
        permits
            org.jooq.impl.Concat
    {}

    public sealed interface TimestampDiff<T>
        extends
            Field<DayToSecond>,
            UOperator2<Field<T>, Field<T>, Field<DayToSecond>>
        permits
            org.jooq.impl.TimestampDiff
    {
        @NotNull default Field<T> $minuend() { return $arg1(); }
        @NotNull default Field<T> $subtrahend() { return $arg2(); }
    }

    public sealed interface Convert<T>
        extends
            Field<T>
        permits
            org.jooq.impl.ConvertDateTime
    {
        @NotNull Field<?> $field();
        int $style();
    }

    public sealed interface CurrentDate<T>
        extends
            Field<T>,
            UEmpty
        permits
            org.jooq.impl.CurrentDate
    {}

    public sealed interface CurrentTime<T>
        extends
            Field<T>,
            UEmpty
        permits
            org.jooq.impl.CurrentTime
    {}

    public sealed interface CurrentTimestamp<T>
        extends
            Field<T>,
            UEmpty
        permits
            org.jooq.impl.CurrentTimestamp
    {}

    public sealed interface XMLQuery
        extends
            Field<XML>
        permits
            org.jooq.impl.XMLQuery
    {
        @NotNull Field<String> $xpath();
        @NotNull Field<XML> $passing();
        @Nullable XMLPassingMechanism $passingMechanism();
    }

    public sealed interface XMLElement
        extends
            Field<XML>
        permits
            org.jooq.impl.XMLElement
    {
        @NotNull Name $elementName();
        @NotNull XMLAttributes $attributes();
        @NotNull UnmodifiableList<? extends Field<?>> $content();
    }

    public sealed interface XMLExists
        extends
            Condition
        permits
            org.jooq.impl.XMLExists
    {
        @NotNull Field<String> $xpath();
        @NotNull Field<XML> $passing();
        @Nullable XMLPassingMechanism $passingMechanism();
    }

    public sealed interface XMLParse
        extends
            Field<XML>
        permits
            org.jooq.impl.XMLParse
    {
        @NotNull Field<String> $content();
        @NotNull DocumentOrContent $documentOrContent();
    }



    /**
     * The <code>ALTER DATABASE</code> statement.
     */
    public sealed interface AlterDatabase
        extends
            DDLQuery
        permits
            org.jooq.impl.AlterDatabaseImpl
    {
        @NotNull  Catalog $database();
                  boolean $ifExists();
        @NotNull  Catalog $renameTo();
        @CheckReturnValue
        @NotNull  AlterDatabase $database(Catalog database);
        @CheckReturnValue
        @NotNull  AlterDatabase $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  AlterDatabase $renameTo(Catalog renameTo);
    }

    /**
     * The <code>ALTER DOMAIN</code> statement.
     */
    public sealed interface AlterDomain<T>
        extends
            DDLQuery
        permits
            org.jooq.impl.AlterDomainImpl
    {
        @NotNull  Domain<T> $domain();
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
        @NotNull  AlterDomain<T> $domain(Domain<T> domain);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $addConstraint(Constraint addConstraint);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $dropConstraint(Constraint dropConstraint);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $dropConstraintIfExists(boolean dropConstraintIfExists);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $renameTo(Domain<?> renameTo);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $renameConstraint(Constraint renameConstraint);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $renameConstraintIfExists(boolean renameConstraintIfExists);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $setDefault(Field<T> setDefault);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $dropDefault(boolean dropDefault);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $setNotNull(boolean setNotNull);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $dropNotNull(boolean dropNotNull);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $cascade(Cascade cascade);
        @CheckReturnValue
        @NotNull  AlterDomain<T> $renameConstraintTo(Constraint renameConstraintTo);
    }

    /**
     * The <code>ALTER INDEX</code> statement.
     */
    public sealed interface AlterIndex
        extends
            DDLQuery
        permits
            org.jooq.impl.AlterIndexImpl
    {
        @NotNull  Index $index();
                  boolean $ifExists();
        @Nullable Table<?> $on();
        @NotNull  Index $renameTo();
        @CheckReturnValue
        @NotNull  AlterIndex $index(Index index);
        @CheckReturnValue
        @NotNull  AlterIndex $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  AlterIndex $on(Table<?> on);
        @CheckReturnValue
        @NotNull  AlterIndex $renameTo(Index renameTo);
    }

    /**
     * The <code>ALTER SCHEMA</code> statement.
     */
    public sealed interface AlterSchema
        extends
            DDLQuery
        permits
            org.jooq.impl.AlterSchemaImpl
    {
        @NotNull  Schema $schema();
                  boolean $ifExists();
        @NotNull  Schema $renameTo();
        @CheckReturnValue
        @NotNull  AlterSchema $schema(Schema schema);
        @CheckReturnValue
        @NotNull  AlterSchema $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  AlterSchema $renameTo(Schema renameTo);
    }

    /**
     * The <code>ALTER SEQUENCE</code> statement.
     */
    public sealed interface AlterSequence<T extends Number>
        extends
            DDLQuery
        permits
            org.jooq.impl.AlterSequenceImpl
    {
        @NotNull  Sequence<T> $sequence();
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
        @NotNull  AlterSequence<T> $sequence(Sequence<T> sequence);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $renameTo(Sequence<?> renameTo);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $restart(boolean restart);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $restartWith(Field<T> restartWith);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $startWith(Field<T> startWith);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $incrementBy(Field<T> incrementBy);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $minvalue(Field<T> minvalue);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $noMinvalue(boolean noMinvalue);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $maxvalue(Field<T> maxvalue);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $noMaxvalue(boolean noMaxvalue);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $cycle(CycleOption cycle);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $cache(Field<T> cache);
        @CheckReturnValue
        @NotNull  AlterSequence<T> $noCache(boolean noCache);
    }

    /**
     * The <code>ALTER TYPE</code> statement.
     */
    public sealed interface AlterType
        extends
            DDLQuery
        permits
            org.jooq.impl.AlterTypeImpl
    {
        @NotNull  Name $type();
        @Nullable Name $renameTo();
        @Nullable Schema $setSchema();
        @Nullable Field<String> $addValue();
        @Nullable Field<String> $renameValue();
        @Nullable Field<String> $renameValueTo();
        @CheckReturnValue
        @NotNull  AlterType $type(Name type);
        @CheckReturnValue
        @NotNull  AlterType $renameTo(Name renameTo);
        @CheckReturnValue
        @NotNull  AlterType $setSchema(Schema setSchema);
        @CheckReturnValue
        @NotNull  AlterType $addValue(Field<String> addValue);
        @CheckReturnValue
        @NotNull  AlterType $renameValue(Field<String> renameValue);
        @CheckReturnValue
        @NotNull  AlterType $renameValueTo(Field<String> renameValueTo);
    }

    /**
     * The <code>ALTER VIEW</code> statement.
     */
    public sealed interface AlterView
        extends
            DDLQuery
        permits
            org.jooq.impl.AlterViewImpl
    {
        @NotNull  Table<?> $view();
        @NotNull  UnmodifiableList<? extends Field<?>> $fields();
                  boolean $ifExists();
        @Nullable Comment $comment();
        @Nullable Table<?> $renameTo();
        @Nullable Select<?> $as();
        @CheckReturnValue
        @NotNull  AlterView $view(Table<?> view);
        @CheckReturnValue
        @NotNull  AlterView $fields(Collection<? extends Field<?>> fields);
        @CheckReturnValue
        @NotNull  AlterView $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  AlterView $comment(Comment comment);
        @CheckReturnValue
        @NotNull  AlterView $renameTo(Table<?> renameTo);
        @CheckReturnValue
        @NotNull  AlterView $as(Select<?> as);
    }

    /**
     * The <code>COMMENT ON TABLE</code> statement.
     */
    public sealed interface CommentOn
        extends
            DDLQuery
        permits
            org.jooq.impl.CommentOnImpl
    {
        @Nullable Table<?> $table();
                  boolean $isView();
        @Nullable Field<?> $field();
        @NotNull  Comment $comment();
        @CheckReturnValue
        @NotNull  CommentOn $table(Table<?> table);
        @CheckReturnValue
        @NotNull  CommentOn $isView(boolean isView);
        @CheckReturnValue
        @NotNull  CommentOn $field(Field<?> field);
        @CheckReturnValue
        @NotNull  CommentOn $comment(Comment comment);
    }

    /**
     * The <code>CREATE DATABASE</code> statement.
     */
    public sealed interface CreateDatabase
        extends
            DDLQuery
        permits
            org.jooq.impl.CreateDatabaseImpl
    {
        @NotNull  Catalog $database();
                  boolean $ifNotExists();
        @CheckReturnValue
        @NotNull  CreateDatabase $database(Catalog database);
        @CheckReturnValue
        @NotNull  CreateDatabase $ifNotExists(boolean ifNotExists);
    }

    /**
     * The <code>CREATE DOMAIN</code> statement.
     */
    public sealed interface CreateDomain<T>
        extends
            DDLQuery
        permits
            org.jooq.impl.CreateDomainImpl
    {
        @NotNull  Domain<?> $domain();
                  boolean $ifNotExists();
        @NotNull  DataType<T> $dataType();
        @Nullable Field<T> $default_();
        @NotNull  UnmodifiableList<? extends Constraint> $constraints();
        @CheckReturnValue
        @NotNull  CreateDomain<T> $domain(Domain<?> domain);
        @CheckReturnValue
        @NotNull  CreateDomain<T> $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull  CreateDomain<T> $dataType(DataType<T> dataType);
        @CheckReturnValue
        @NotNull  CreateDomain<T> $default_(Field<T> default_);
        @CheckReturnValue
        @NotNull  CreateDomain<T> $constraints(Collection<? extends Constraint> constraints);
    }










































    /**
     * The <code>CREATE INDEX</code> statement.
     */
    public sealed interface CreateIndex
        extends
            DDLQuery
        permits
            org.jooq.impl.CreateIndexImpl
    {
                  boolean $unique();
        @Nullable Index $index();
                  boolean $ifNotExists();
        @Nullable Table<?> $table();
        @NotNull  UnmodifiableList<? extends OrderField<?>> $on();
        @NotNull  UnmodifiableList<? extends Field<?>> $include();
        @Nullable Condition $where();
                  boolean $excludeNullKeys();
        @CheckReturnValue
        @NotNull  CreateIndex $unique(boolean unique);
        @CheckReturnValue
        @NotNull  CreateIndex $index(Index index);
        @CheckReturnValue
        @NotNull  CreateIndex $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull  CreateIndex $table(Table<?> table);
        @CheckReturnValue
        @NotNull  CreateIndex $on(Collection<? extends OrderField<?>> on);
        @CheckReturnValue
        @NotNull  CreateIndex $include(Collection<? extends Field<?>> include);
        @CheckReturnValue
        @NotNull  CreateIndex $where(Condition where);
        @CheckReturnValue
        @NotNull  CreateIndex $excludeNullKeys(boolean excludeNullKeys);
    }






























    /**
     * The <code>CREATE TABLE</code> statement.
     */
    public sealed interface CreateTable
        extends
            DDLQuery
        permits
            org.jooq.impl.CreateTableImpl
    {
        @NotNull  Table<?> $table();
                  boolean $temporary();
                  boolean $ifNotExists();
        @NotNull  UnmodifiableList<? extends TableElement> $tableElements();
        @Nullable Select<?> $select();
        @Nullable WithOrWithoutData $withData();
        @Nullable TableCommitAction $onCommit();
        @Nullable Comment $comment();
        @Nullable SQL $storage();
        @CheckReturnValue
        @NotNull  CreateTable $table(Table<?> table);
        @CheckReturnValue
        @NotNull  CreateTable $temporary(boolean temporary);
        @CheckReturnValue
        @NotNull  CreateTable $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull  CreateTable $tableElements(Collection<? extends TableElement> tableElements);
        @CheckReturnValue
        @NotNull  CreateTable $select(Select<?> select);
        @CheckReturnValue
        @NotNull  CreateTable $withData(WithOrWithoutData withData);
        @CheckReturnValue
        @NotNull  CreateTable $onCommit(TableCommitAction onCommit);
        @CheckReturnValue
        @NotNull  CreateTable $comment(Comment comment);
        @CheckReturnValue
        @NotNull  CreateTable $storage(SQL storage);
    }































































    /**
     * The <code>CREATE SCHEMA</code> statement.
     */
    public sealed interface CreateSchema
        extends
            DDLQuery
        permits
            org.jooq.impl.CreateSchemaImpl
    {
        @NotNull  Schema $schema();
                  boolean $ifNotExists();
        @CheckReturnValue
        @NotNull  CreateSchema $schema(Schema schema);
        @CheckReturnValue
        @NotNull  CreateSchema $ifNotExists(boolean ifNotExists);
    }

    /**
     * The <code>CREATE SEQUENCE</code> statement.
     */
    public sealed interface CreateSequence
        extends
            DDLQuery
        permits
            org.jooq.impl.CreateSequenceImpl
    {
        @NotNull  Sequence<?> $sequence();
                  boolean $ifNotExists();
        @Nullable Field<? extends Number> $startWith();
        @Nullable Field<? extends Number> $incrementBy();
        @Nullable Field<? extends Number> $minvalue();
                  boolean $noMinvalue();
        @Nullable Field<? extends Number> $maxvalue();
                  boolean $noMaxvalue();
        @Nullable CycleOption $cycle();
        @Nullable Field<? extends Number> $cache();
                  boolean $noCache();
        @CheckReturnValue
        @NotNull  CreateSequence $sequence(Sequence<?> sequence);
        @CheckReturnValue
        @NotNull  CreateSequence $ifNotExists(boolean ifNotExists);
        @CheckReturnValue
        @NotNull  CreateSequence $startWith(Field<? extends Number> startWith);
        @CheckReturnValue
        @NotNull  CreateSequence $incrementBy(Field<? extends Number> incrementBy);
        @CheckReturnValue
        @NotNull  CreateSequence $minvalue(Field<? extends Number> minvalue);
        @CheckReturnValue
        @NotNull  CreateSequence $noMinvalue(boolean noMinvalue);
        @CheckReturnValue
        @NotNull  CreateSequence $maxvalue(Field<? extends Number> maxvalue);
        @CheckReturnValue
        @NotNull  CreateSequence $noMaxvalue(boolean noMaxvalue);
        @CheckReturnValue
        @NotNull  CreateSequence $cycle(CycleOption cycle);
        @CheckReturnValue
        @NotNull  CreateSequence $cache(Field<? extends Number> cache);
        @CheckReturnValue
        @NotNull  CreateSequence $noCache(boolean noCache);
    }

    /**
     * The <code>DROP DATABASE</code> statement.
     */
    public sealed interface DropDatabase
        extends
            DDLQuery
        permits
            org.jooq.impl.DropDatabaseImpl
    {
        @NotNull  Catalog $database();
                  boolean $ifExists();
        @CheckReturnValue
        @NotNull  DropDatabase $database(Catalog database);
        @CheckReturnValue
        @NotNull  DropDatabase $ifExists(boolean ifExists);
    }

    /**
     * The <code>DROP DOMAIN</code> statement.
     */
    public sealed interface DropDomain
        extends
            DDLQuery
        permits
            org.jooq.impl.DropDomainImpl
    {
        @NotNull  Domain<?> $domain();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull  DropDomain $domain(Domain<?> domain);
        @CheckReturnValue
        @NotNull  DropDomain $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  DropDomain $cascade(Cascade cascade);
    }





















    /**
     * The <code>DROP INDEX</code> statement.
     */
    public sealed interface DropIndex
        extends
            DDLQuery
        permits
            org.jooq.impl.DropIndexImpl
    {
        @NotNull  Index $index();
                  boolean $ifExists();
        @Nullable Table<?> $on();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull  DropIndex $index(Index index);
        @CheckReturnValue
        @NotNull  DropIndex $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  DropIndex $on(Table<?> on);
        @CheckReturnValue
        @NotNull  DropIndex $cascade(Cascade cascade);
    }





















    /**
     * The <code>DROP SCHEMA</code> statement.
     */
    public sealed interface DropSchema
        extends
            DDLQuery
        permits
            org.jooq.impl.DropSchemaImpl
    {
        @NotNull  Schema $schema();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull  DropSchema $schema(Schema schema);
        @CheckReturnValue
        @NotNull  DropSchema $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  DropSchema $cascade(Cascade cascade);
    }

    /**
     * The <code>DROP SEQUENCE</code> statement.
     */
    public sealed interface DropSequence
        extends
            DDLQuery
        permits
            org.jooq.impl.DropSequenceImpl
    {
        @NotNull  Sequence<?> $sequence();
                  boolean $ifExists();
        @CheckReturnValue
        @NotNull  DropSequence $sequence(Sequence<?> sequence);
        @CheckReturnValue
        @NotNull  DropSequence $ifExists(boolean ifExists);
    }

    /**
     * The <code>DROP TABLE</code> statement.
     */
    public sealed interface DropTable
        extends
            DDLQuery
        permits
            org.jooq.impl.DropTableImpl
    {
                  boolean $temporary();
        @NotNull  Table<?> $table();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull  DropTable $temporary(boolean temporary);
        @CheckReturnValue
        @NotNull  DropTable $table(Table<?> table);
        @CheckReturnValue
        @NotNull  DropTable $ifExists(boolean ifExists);
        @CheckReturnValue
        @NotNull  DropTable $cascade(Cascade cascade);
    }





















    /**
     * The <code>DROP VIEW</code> statement.
     */
    public sealed interface DropView
        extends
            DDLQuery
        permits
            org.jooq.impl.DropViewImpl
    {
        @NotNull  Table<?> $view();
                  boolean $ifExists();
        @CheckReturnValue
        @NotNull  DropView $view(Table<?> view);
        @CheckReturnValue
        @NotNull  DropView $ifExists(boolean ifExists);
    }

    /**
     * The <code>GRANT</code> statement.
     */
    public sealed interface Grant
        extends
            DDLQuery
        permits
            org.jooq.impl.GrantImpl
    {
        @NotNull  UnmodifiableList<? extends Privilege> $privileges();
        @NotNull  Table<?> $on();
        @Nullable Role $to();
                  boolean $toPublic();
                  boolean $withGrantOption();
        @CheckReturnValue
        @NotNull  Grant $privileges(Collection<? extends Privilege> privileges);
        @CheckReturnValue
        @NotNull  Grant $on(Table<?> on);
        @CheckReturnValue
        @NotNull  Grant $to(Role to);
        @CheckReturnValue
        @NotNull  Grant $toPublic(boolean toPublic);
        @CheckReturnValue
        @NotNull  Grant $withGrantOption(boolean withGrantOption);
    }

    /**
     * The <code>REVOKE</code> statement.
     */
    public sealed interface Revoke
        extends
            DDLQuery
        permits
            org.jooq.impl.RevokeImpl
    {
        @NotNull  UnmodifiableList<? extends Privilege> $privileges();
                  boolean $grantOptionFor();
        @NotNull  Table<?> $on();
        @Nullable Role $from();
                  boolean $fromPublic();
        @CheckReturnValue
        @NotNull  Revoke $privileges(Collection<? extends Privilege> privileges);
        @CheckReturnValue
        @NotNull  Revoke $grantOptionFor(boolean grantOptionFor);
        @CheckReturnValue
        @NotNull  Revoke $on(Table<?> on);
        @CheckReturnValue
        @NotNull  Revoke $from(Role from);
        @CheckReturnValue
        @NotNull  Revoke $fromPublic(boolean fromPublic);
    }

    /**
     * The <code>SET</code> statement.
     * <p>
     * Set a vendor specific session configuration to a new value.
     */
    public sealed interface SetCommand
        extends
            org.jooq.RowCountQuery
        permits
            org.jooq.impl.SetCommand
    {
        @NotNull  Name $name();
        @NotNull  Param<?> $value();
                  boolean $local();
        @CheckReturnValue
        @NotNull  SetCommand $name(Name name);
        @CheckReturnValue
        @NotNull  SetCommand $value(Param<?> value);
        @CheckReturnValue
        @NotNull  SetCommand $local(boolean local);
    }

    /**
     * The <code>SET CATALOG</code> statement.
     * <p>
     * Set the current catalog to a new value.
     */
    public sealed interface SetCatalog
        extends
            org.jooq.RowCountQuery
        permits
            org.jooq.impl.SetCatalog
    {
        @NotNull  Catalog $catalog();
        @CheckReturnValue
        @NotNull  SetCatalog $catalog(Catalog catalog);
    }

    /**
     * The <code>SET SCHEMA</code> statement.
     * <p>
     * Set the current schema to a new value.
     */
    public sealed interface SetSchema
        extends
            org.jooq.RowCountQuery
        permits
            org.jooq.impl.SetSchema
    {
        @NotNull  Schema $schema();
        @CheckReturnValue
        @NotNull  SetSchema $schema(Schema schema);
    }

    /**
     * The <code>TRUNCATE</code> statement.
     */
    public sealed interface Truncate<R extends Record>
        extends
            DDLQuery
        permits
            org.jooq.impl.TruncateImpl
    {
        @NotNull  Table<R> $table();
        @Nullable IdentityRestartOption $restartIdentity();
        @Nullable Cascade $cascade();
        @CheckReturnValue
        @NotNull  Truncate<R> $table(Table<R> table);
        @CheckReturnValue
        @NotNull  Truncate<R> $restartIdentity(IdentityRestartOption restartIdentity);
        @CheckReturnValue
        @NotNull  Truncate<R> $cascade(Cascade cascade);
    }























    /**
     * The <code>AND</code> operator.
     */
    public sealed interface And
        extends
            UCommutativeOperator<Condition, Condition>,
            CombinedCondition
        permits
            org.jooq.impl.And
    {}

    /**
     * The <code>EQ</code> operator.
     */
    public sealed interface TableEq<R extends Record>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Table<R>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.TableEq
    {}

    /**
     * The <code>EQ</code> operator.
     */
    public sealed interface Eq<T>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.Eq
    {}

    /**
     * The <code>EXISTS</code> function.
     */
    public sealed interface Exists
        extends
            UOperator1<Select<?>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.Exists
    {
        @NotNull  default Select<?> $query() { return $arg1(); }
    }

    /**
     * The <code>GE</code> operator.
     */
    public sealed interface Ge<T>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.Ge
    {
        @Override
        default Condition $converse() {
            return $arg2().le($arg1());
        }
    }

    /**
     * The <code>GT</code> operator.
     */
    public sealed interface Gt<T>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.Gt
    {
        @Override
        default Condition $converse() {
            return $arg2().lt($arg1());
        }
    }

    /**
     * The <code>IN</code> operator.
     * <p>
     * The subquery must return exactly one field. This is not checked
     * by jOOQ and will result in syntax errors in the database, if not used
     * correctly.
     */
    public sealed interface In<T>
        extends
            UOperator2<Field<T>, Select<? extends Record1<T>>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.In
    {}

    /**
     * The <code>IS DISTINCT FROM</code> operator.
     * <p>
     * The DISTINCT predicate allows for creating NULL safe comparisons where the two operands
     * are tested for non-equality
     */
    public sealed interface IsDistinctFrom<T>
        extends
            UCommutativeOperator<Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.IsDistinctFrom
    {}

    /**
     * The <code>IS NULL</code> operator.
     */
    public sealed interface IsNull
        extends
            UOperator1<Field<?>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.IsNull
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>IS NOT DISTINCT FROM</code> operator.
     * <p>
     * The NOT DISTINCT predicate allows for creating NULL safe comparisons where the two
     * operands are tested for equality
     */
    public sealed interface IsNotDistinctFrom<T>
        extends
            UCommutativeOperator<Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.IsNotDistinctFrom
    {}

    /**
     * The <code>IS NOT NULL</code> operator.
     */
    public sealed interface IsNotNull
        extends
            UOperator1<Field<?>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.IsNotNull
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>LE</code> operator.
     */
    public sealed interface Le<T>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.Le
    {
        @Override
        default Condition $converse() {
            return $arg2().ge($arg1());
        }
    }

    /**
     * The <code>LIKE</code> operator.
     */
    public sealed interface Like
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, Condition>,
            Condition
        permits
            org.jooq.impl.Like
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>LIKE IGNORE CASE</code> operator.
     * <p>
     * Create a condition to case-insensitively pattern-check this field against
     * a value.
     * <p>
     * This translates to <code>this not ilike value</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) not like lower(value)</code> in all other dialects.
     */
    public sealed interface LikeIgnoreCase
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, Condition>,
            Condition
        permits
            org.jooq.impl.LikeIgnoreCase
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>LT</code> operator.
     */
    public sealed interface Lt<T>
        extends
            UReturnsNullOnNullInput,
            UConvertibleOperator<Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.Lt
    {
        @Override
        default Condition $converse() {
            return $arg2().gt($arg1());
        }
    }

    /**
     * The <code>NE</code> operator.
     */
    public sealed interface TableNe<R extends Record>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Table<R>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.TableNe
    {}

    /**
     * The <code>NE</code> operator.
     */
    public sealed interface Ne<T>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.Ne
    {}

    /**
     * The <code>NOT</code> operator.
     */
    public sealed interface Not
        extends
            UReturnsNullOnNullInput,
            UOperator1<Condition, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.Not
    {
        @NotNull  default Condition $condition() { return $arg1(); }
    }

    /**
     * The <code>NOT</code> operator.
     */
    public sealed interface NotField
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<Boolean>, Field<Boolean>>,
            org.jooq.Field<Boolean>
        permits
            org.jooq.impl.NotField
    {
        @NotNull  default Field<Boolean> $field() { return $arg1(); }
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
    public sealed interface NotIn<T>
        extends
            UOperator2<Field<T>, Select<? extends Record1<T>>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.NotIn
    {}

    /**
     * The <code>NOT LIKE</code> operator.
     */
    public sealed interface NotLike
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, Condition>,
            Condition
        permits
            org.jooq.impl.NotLike
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
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
    public sealed interface NotLikeIgnoreCase
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, Condition>,
            Condition
        permits
            org.jooq.impl.NotLikeIgnoreCase
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>NOT SIMILAR TO</code> operator.
     */
    public sealed interface NotSimilarTo
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, Condition>,
            Condition
        permits
            org.jooq.impl.NotSimilarTo
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>OR</code> operator.
     */
    public sealed interface Or
        extends
            UCommutativeOperator<Condition, Condition>,
            CombinedCondition
        permits
            org.jooq.impl.Or
    {}

    /**
     * The <code>SIMILAR TO</code> operator.
     */
    public sealed interface SimilarTo
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<?>, Field<String>, Character, Condition>,
            Condition
        permits
            org.jooq.impl.SimilarTo
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>UNIQUE</code> function.
     */
    public sealed interface Unique
        extends
            UOperator1<Select<?>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.Unique
    {
        @NotNull  default Select<?> $query() { return $arg1(); }
    }

    /**
     * The <code>XOR</code> operator.
     */
    public sealed interface Xor
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Condition, Condition>,
            CombinedCondition
        permits
            org.jooq.impl.Xor
    {}

    /**
     * The <code>IS DOCUMENT</code> operator.
     * <p>
     * Create a condition to check if this field contains XML data.
     */
    public sealed interface IsDocument
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<?>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.IsDocument
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>IS NOT DOCUMENT</code> operator.
     * <p>
     * Create a condition to check if this field does not contain XML data.
     */
    public sealed interface IsNotDocument
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<?>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.IsNotDocument
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>IS JSON</code> operator.
     * <p>
     * Create a condition to check if this field contains JSON data.
     */
    public sealed interface IsJson
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<?>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.IsJson
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>IS NOT JSON</code> operator.
     * <p>
     * Create a condition to check if this field does not contain JSON data.
     */
    public sealed interface IsNotJson
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<?>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.IsNotJson
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>EXCLUDED</code> function.
     * <p>
     * Provide "EXCLUDED" qualification for a column for use in ON CONFLICT or ON DUPLICATE
     * KEY UPDATE.
     */
    public sealed interface Excluded<T>
        extends
            UOperator1<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Excluded
    {

        /**
         * The excluded field.
         */
        @NotNull  default Field<T> $field() { return $arg1(); }
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
    public sealed interface QualifiedRowid
        extends
            UOperator1<Table<?>, Field<RowId>>,
            org.jooq.Field<RowId>
        permits
            org.jooq.impl.QualifiedRowid
    {
        @NotNull  default Table<?> $table() { return $arg1(); }
    }

    /**
     * The <code>ABS</code> function.
     */
    public sealed interface Abs<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Abs
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
    }

    /**
     * The <code>ACOS</code> function.
     */
    public sealed interface Acos
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Acos
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>ACOSH</code> function.
     */
    public sealed interface Acosh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Acosh
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>ACOTH</code> function.
     */
    public sealed interface Acoth
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Acoth
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>ADD</code> operator.
     */
    public sealed interface Add<T>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.IAdd,
            org.jooq.impl.Add
    {}

    /**
     * The <code>ASIN</code> function.
     */
    public sealed interface Asin
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Asin
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>ASINH</code> function.
     */
    public sealed interface Asinh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Asinh
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>ATAN</code> function.
     */
    public sealed interface Atan
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Atan
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>ATAN2</code> function.
     */
    public sealed interface Atan2
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<? extends Number>, Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Atan2
    {
        @NotNull  default Field<? extends Number> $x() { return $arg1(); }
        @NotNull  default Field<? extends Number> $y() { return $arg2(); }
    }

    /**
     * The <code>ATANH</code> function.
     */
    public sealed interface Atanh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Atanh
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>BIT AND</code> operator.
     */
    public sealed interface BitAnd<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.BitAnd
    {}

    /**
     * The <code>BIT COUNT</code> function.
     * <p>
     * Count the number of bits set in a number
     */
    public sealed interface BitCount
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<Integer>>,
            org.jooq.Field<Integer>
        permits
            org.jooq.impl.BitCount
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>BIT NAND</code> operator.
     */
    public sealed interface BitNand<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.BitNand
    {}

    /**
     * The <code>BIT NOR</code> operator.
     */
    public sealed interface BitNor<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.BitNor
    {}

    /**
     * The <code>BIT NOT</code> operator.
     */
    public sealed interface BitNot<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.BitNot
    {}

    /**
     * The <code>BIT OR</code> operator.
     */
    public sealed interface BitOr<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.BitOr
    {}

    /**
     * The <code>BIT X NOR</code> operator.
     */
    public sealed interface BitXNor<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.BitXNor
    {}

    /**
     * The <code>BIT XOR</code> operator.
     */
    public sealed interface BitXor<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.BitXor
    {}

    /**
     * The <code>CEIL</code> function.
     * <p>
     * Get the smallest integer value equal or greater to a value.
     */
    public sealed interface Ceil<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Ceil
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
    }

    /**
     * The <code>COS</code> function.
     */
    public sealed interface Cos
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Cos
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>COSH</code> function.
     */
    public sealed interface Cosh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Cosh
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>COT</code> function.
     */
    public sealed interface Cot
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Cot
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>COTH</code> function.
     */
    public sealed interface Coth
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Coth
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>DEGREES</code> function.
     * <p>
     * Turn a value in radians to degrees.
     */
    public sealed interface Degrees
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Degrees
    {

        /**
         * The value in radians.
         */
        @NotNull  default Field<? extends Number> $radians() { return $arg1(); }
    }

    /**
     * The <code>DIV</code> operator.
     */
    public sealed interface Div<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.IDiv,
            org.jooq.impl.Div
    {}

    /**
     * The <code>E</code> function.
     * <p>
     * The E literal (Euler number).
     */
    public sealed interface Euler
        extends
            UOperator0<Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Euler
    {}

    /**
     * The <code>EXP</code> function.
     */
    public sealed interface Exp
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Exp
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>FLOOR</code> function.
     * <p>
     * Get the biggest integer value equal or less than a value.
     */
    public sealed interface Floor<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Floor
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
    }

    /**
     * The <code>LN</code> function.
     * <p>
     * Get the natural logarithm of a value.
     */
    public sealed interface Ln
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Ln
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>LOG</code> function.
     * <p>
     * Get the logarithm of a value for a base.
     */
    public sealed interface Log
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<? extends Number>, Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Log
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
        @NotNull  default Field<? extends Number> $base() { return $arg2(); }
    }

    /**
     * The <code>LOG10</code> function.
     * <p>
     * Get the logarithm of a value for base 10.
     */
    public sealed interface Log10
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Log10
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>MOD</code> operator.
     */
    public sealed interface Mod<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<? extends Number>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Mod
    {
        @NotNull  default Field<T> $dividend() { return $arg1(); }
        @NotNull  default Field<? extends Number> $divisor() { return $arg2(); }
    }

    /**
     * The <code>MUL</code> operator.
     */
    public sealed interface Mul<T>
        extends
            UReturnsNullOnNullInput,
            UCommutativeOperator<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.IMul,
            org.jooq.impl.Mul
    {}

    /**
     * The <code>PI</code> function.
     * <p>
     * The π literal.
     */
    public sealed interface Pi
        extends
            UOperator0<Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Pi
    {}

    /**
     * The <code>POWER</code> operator.
     */
    public sealed interface Power
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<? extends Number>, Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Power
    {
        @NotNull  default Field<? extends Number> $base() { return $arg1(); }
        @NotNull  default Field<? extends Number> $exponent() { return $arg2(); }
    }

    /**
     * The <code>RADIANS</code> function.
     * <p>
     * Turn a value in degrees to radians.
     */
    public sealed interface Radians
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Radians
    {

        /**
         * The value in degrees.
         */
        @NotNull  default Field<? extends Number> $degrees() { return $arg1(); }
    }

    /**
     * The <code>RAND</code> function.
     * <p>
     * Get a random numeric value.
     */
    public sealed interface Rand
        extends
            UOperator0<Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Rand
    {}

    /**
     * The <code>ROUND</code> function.
     * <p>
     * Round a numeric value to the nearest decimal precision.
     */
    public sealed interface Round<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<Integer>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Round
    {

        /**
         * The number to be rounded.
         */
        @NotNull  default Field<T> $value() { return $arg1(); }

        /**
         * The decimals to round to.
         */
        @Nullable default Field<Integer> $decimals() { return $arg2(); }
    }

    /**
     * The <code>SHL</code> operator.
     * <p>
     * Left shift all bits in a number
     */
    public sealed interface Shl<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<? extends Number>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Shl
    {

        /**
         * The number whose bits to shift left.
         */
        @NotNull  default Field<T> $value() { return $arg1(); }

        /**
         * The number of bits to shift.
         */
        @NotNull  default Field<? extends Number> $count() { return $arg2(); }
    }

    /**
     * The <code>SHR</code> operator.
     * <p>
     * Right shift all bits in a number
     */
    public sealed interface Shr<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<? extends Number>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Shr
    {

        /**
         * The number whose bits to shift right
         */
        @NotNull  default Field<T> $value() { return $arg1(); }

        /**
         * The number of bits to shift.
         */
        @NotNull  default Field<? extends Number> $count() { return $arg2(); }
    }

    /**
     * The <code>SIGN</code> function.
     * <p>
     * Get the sign of a number and return it as any of +1, 0, -1.
     */
    public sealed interface Sign
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<Integer>>,
            org.jooq.Field<Integer>
        permits
            org.jooq.impl.Sign
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>SIN</code> function.
     */
    public sealed interface Sin
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Sin
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>SINH</code> function.
     */
    public sealed interface Sinh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Sinh
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>SQRT</code> function.
     */
    public sealed interface Sqrt
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Sqrt
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>SQUARE</code> function.
     */
    public sealed interface Square<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Square
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
    }

    /**
     * The <code>SUB</code> operator.
     */
    public sealed interface Sub<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.ISub,
            org.jooq.impl.Sub
    {}

    /**
     * The <code>TAN</code> function.
     */
    public sealed interface Tan
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Tan
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>TANH</code> function.
     */
    public sealed interface Tanh
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Tanh
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>TAU</code> function.
     * <p>
     * The τ literal, or π, in a better world.
     */
    public sealed interface Tau
        extends
            UOperator0<Field<BigDecimal>>,
            org.jooq.Field<BigDecimal>
        permits
            org.jooq.impl.Tau
    {}

    /**
     * The <code>TRUNC</code> function.
     * <p>
     * Truncate a number to a given number of decimals.
     */
    public sealed interface Trunc<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<Integer>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Trunc
    {

        /**
         * The number to be truncated
         */
        @NotNull  default Field<T> $value() { return $arg1(); }

        /**
         * The decimals to truncate to.
         */
        @NotNull  default Field<Integer> $decimals() { return $arg2(); }
    }

    /**
     * The <code>WIDTH BUCKET</code> function.
     * <p>
     * Divide a range into buckets of equal size.
     */
    public sealed interface WidthBucket<T extends Number>
        extends
            UReturnsNullOnNullInput,
            UOperator4<Field<T>, Field<T>, Field<T>, Field<Integer>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.WidthBucket
    {

        /**
         * The value to divide into the range.
         */
        @NotNull  default Field<T> $field() { return $arg1(); }

        /**
         * The lower bound of the range.
         */
        @NotNull  default Field<T> $low() { return $arg2(); }

        /**
         * The upper bound of the range.
         */
        @NotNull  default Field<T> $high() { return $arg3(); }

        /**
         * The number of buckets to produce.
         */
        @NotNull  default Field<Integer> $buckets() { return $arg4(); }
    }

    /**
     * The <code>ASCII</code> function.
     * <p>
     * The ASCII value of a character.
     */
    public sealed interface Ascii
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Field<Integer>>,
            org.jooq.Field<Integer>
        permits
            org.jooq.impl.Ascii
    {
        @NotNull  default Field<String> $string() { return $arg1(); }
    }

    /**
     * The <code>BIT LENGTH</code> function.
     * <p>
     * The length of a string in bits.
     */
    public sealed interface BitLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Field<Integer>>,
            org.jooq.Field<Integer>
        permits
            org.jooq.impl.BitLength
    {
        @NotNull  default Field<String> $string() { return $arg1(); }
    }

    /**
     * The <code>CHAR LENGTH</code> function.
     * <p>
     * The length of a string in characters.
     */
    public sealed interface CharLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Field<Integer>>,
            org.jooq.Field<Integer>
        permits
            org.jooq.impl.CharLength
    {
        @NotNull  default Field<String> $string() { return $arg1(); }
    }

    /**
     * The <code>CHR</code> function.
     */
    public sealed interface Chr
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Chr
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>CONTAINS</code> operator.
     * <p>
     * Convenience method for {@link #like(String, char)} including proper
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
    public sealed interface Contains<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.Contains
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
        @NotNull  default Field<T> $content() { return $arg2(); }
    }

    /**
     * The <code>CONTAINS IGNORE CASE</code> operator.
     * <p>
     * Convenience method for {@link #likeIgnoreCase(String, char)} including
     * proper adding of wildcards and escaping.
     * <p>
     * This translates to
     * <code>this ilike ('%' || escape(value, '\') || '%') escape '\'</code> in
     * {@link SQLDialect#POSTGRES}, or to
     * <code>lower(this) not like lower(('%' || escape(value, '\') || '%') escape '\')</code>
     * in all other dialects.
     */
    public sealed interface ContainsIgnoreCase<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.ContainsIgnoreCase
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
        @NotNull  default Field<T> $content() { return $arg2(); }
    }

    /**
     * The <code>DIGITS</code> function.
     */
    public sealed interface Digits
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Digits
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>ENDS WITH</code> operator.
     * <p>
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like ('%' || escape(value, '\')) escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).endsWith(33)</code>
     */
    public sealed interface EndsWith<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.EndsWith
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $suffix() { return $arg2(); }
    }

    /**
     * The <code>ENDS WITH IGNORE CASE</code> operator.
     * <p>
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>lower(this) like ('%' || lower(escape(value, '\'))) escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).endsWithIgnoreCase(33)</code>
     */
    public sealed interface EndsWithIgnoreCase<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.EndsWithIgnoreCase
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $suffix() { return $arg2(); }
    }

    /**
     * The <code>LEFT</code> function.
     * <p>
     * Get the left outermost characters from a string.
     */
    public sealed interface Left
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Left
    {

        /**
         * The string whose characters are extracted.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The number of characters to extract from the string.
         */
        @NotNull  default Field<? extends Number> $length() { return $arg2(); }
    }

    /**
     * The <code>LOWER</code> function.
     * <p>
     * Turn a string into lower case.
     */
    public sealed interface Lower
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Lower
    {
        @NotNull  default Field<String> $string() { return $arg1(); }
    }

    /**
     * The <code>LPAD</code> function.
     * <p>
     * Left-pad a string with a character (whitespace as default) for a number of times.
     */
    public sealed interface Lpad
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<? extends Number>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Lpad
    {

        /**
         * The string to be padded.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The maximum length to pad the string to.
         */
        @NotNull  default Field<? extends Number> $length() { return $arg2(); }

        /**
         * The padding character, if different from whitespace
         */
        @Nullable default Field<String> $character() { return $arg3(); }
    }

    /**
     * The <code>LTRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from both sides of a string.
     */
    public sealed interface Ltrim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Ltrim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The characters to be trimmed.
         */
        @Nullable default Field<String> $characters() { return $arg2(); }
    }

    /**
     * The <code>MD5</code> function.
     * <p>
     * Calculate an MD5 hash from a string.
     */
    public sealed interface Md5
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Md5
    {
        @NotNull  default Field<String> $string() { return $arg1(); }
    }

    /**
     * The <code>OCTET LENGTH</code> function.
     * <p>
     * The length of a string in octets.
     */
    public sealed interface OctetLength
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Field<Integer>>,
            org.jooq.Field<Integer>
        permits
            org.jooq.impl.OctetLength
    {
        @NotNull  default Field<String> $string() { return $arg1(); }
    }

    /**
     * The <code>OVERLAY</code> function.
     * <p>
     * Place a string on top of another string, replacing the original contents.
     */
    public sealed interface Overlay
        extends
            UReturnsNullOnNullInput,
            UOperator4<Field<String>, Field<String>, Field<? extends Number>, Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Overlay
    {

        /**
         * The original string on top of which the overlay is placed.
         */
        @NotNull  default Field<String> $in() { return $arg1(); }

        /**
         * The string that is being placed on top of the other string.
         */
        @NotNull  default Field<String> $placing() { return $arg2(); }

        /**
         * The start index (1-based) starting from where the overlay is placed.
         */
        @NotNull  default Field<? extends Number> $startIndex() { return $arg3(); }

        /**
         * The length in the original string that will be replaced, if different from the overlay length.
         */
        @Nullable default Field<? extends Number> $length() { return $arg4(); }
    }

    /**
     * The <code>POSITION</code> function.
     * <p>
     * Search the position (1-based) of a substring in another string.
     */
    public sealed interface Position
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<? extends Number>, Field<Integer>>,
            org.jooq.Field<Integer>
        permits
            org.jooq.impl.Position
    {

        /**
         * The string in which to search the substring.
         */
        @NotNull  default Field<String> $in() { return $arg1(); }

        /**
         * The substring to search for.
         */
        @NotNull  default Field<String> $search() { return $arg2(); }

        /**
         * The start index (1-based) from which to start looking for the substring.
         */
        @Nullable default Field<? extends Number> $startIndex() { return $arg3(); }
    }

    /**
     * The <code>REPEAT</code> function.
     * <p>
     * Repeat a string a number of times.
     */
    public sealed interface Repeat
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Repeat
    {

        /**
         * The string to be repeated.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The number of times to repeat the string.
         */
        @NotNull  default Field<? extends Number> $count() { return $arg2(); }
    }

    /**
     * The <code>REPLACE</code> function.
     * <p>
     * Replace all occurrences of a substring in another string.
     */
    public sealed interface Replace
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Replace
    {

        /**
         * The string in which to replace contents.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The substring to search for.
         */
        @NotNull  default Field<String> $search() { return $arg2(); }

        /**
         * The replacement for each substring, if not empty.
         */
        @Nullable default Field<String> $replace() { return $arg3(); }
    }

    /**
     * The <code>REVERSE</code> function.
     * <p>
     * Reverse a string.
     */
    public sealed interface Reverse
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Reverse
    {
        @NotNull  default Field<String> $string() { return $arg1(); }
    }

    /**
     * The <code>RIGHT</code> function.
     * <p>
     * Get the right outermost characters from a string.
     */
    public sealed interface Right
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Right
    {

        /**
         * The string whose characters are extracted.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The number of characters to extract from the string.
         */
        @NotNull  default Field<? extends Number> $length() { return $arg2(); }
    }

    /**
     * The <code>RPAD</code> function.
     * <p>
     * Right-pad a string with a character (whitespace as default) for a number of times.
     */
    public sealed interface Rpad
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<? extends Number>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Rpad
    {

        /**
         * The string to be padded.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The maximum length to pad the string to.
         */
        @NotNull  default Field<? extends Number> $length() { return $arg2(); }

        /**
         * The padding character, if different from whitespace
         */
        @Nullable default Field<String> $character() { return $arg3(); }
    }

    /**
     * The <code>RTRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from both sides of a string.
     */
    public sealed interface Rtrim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Rtrim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The characters to be trimmed.
         */
        @Nullable default Field<String> $characters() { return $arg2(); }
    }

    /**
     * The <code>SPACE</code> function.
     * <p>
     * Get a string of spaces of a given length.
     */
    public sealed interface Space
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Space
    {

        /**
         * The number of spaces to produce.
         */
        @NotNull  default Field<? extends Number> $count() { return $arg1(); }
    }

    /**
     * The <code>SPLIT PART</code> function.
     * <p>
     * Split a string into tokens, and retrieve the nth token.
     */
    public sealed interface SplitPart
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.SplitPart
    {

        /**
         * The string to be split into parts.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The delimiter used for splitting.
         */
        @NotNull  default Field<String> $delimiter() { return $arg2(); }

        /**
         * The token number (1-based).
         */
        @NotNull  default Field<? extends Number> $n() { return $arg3(); }
    }

    /**
     * The <code>STARTS WITH</code> operator.
     * <p>
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>this like (escape(value, '\') || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).startsWith(11)</code>
     */
    public sealed interface StartsWith<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.StartsWith
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $prefix() { return $arg2(); }
    }

    /**
     * The <code>STARTS WITH IGNORE CASE</code> operator.
     * <p>
     * Convenience method for {@link #like(String, char)} including proper
     * adding of wildcards and escaping.
     * <p>
     * SQL: <code>lower(this) like (lower(escape(value, '\')) || '%') escape '\'</code>
     * <p>
     * Note: This also works with numbers, for instance
     * <code>val(1133).startsWithIgnoreCase(11)</code>
     */
    public sealed interface StartsWithIgnoreCase<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T>, Field<T>, Condition>,
            CompareCondition<T>
        permits
            org.jooq.impl.StartsWithIgnoreCase
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $prefix() { return $arg2(); }
    }

    /**
     * The <code>SUBSTRING</code> function.
     * <p>
     * Get a substring of a string, from a given position.
     */
    public sealed interface Substring
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<? extends Number>, Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Substring
    {

        /**
         * The string from which to get the substring.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The position (1-based) from which to get the substring.
         */
        @NotNull  default Field<? extends Number> $startingPosition() { return $arg2(); }

        /**
         * The maximum length of the substring.
         */
        @Nullable default Field<? extends Number> $length() { return $arg3(); }
    }

    /**
     * The <code>SUBSTRING INDEX</code> function.
     * <p>
     * Get a substring of a string, from the beginning until the nth occurrence of a substring.
     */
    public sealed interface SubstringIndex
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.SubstringIndex
    {

        /**
         * The string from which to get the substring.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The delimiter.
         */
        @NotNull  default Field<String> $delimiter() { return $arg2(); }

        /**
         * The number of occurrences of the delimiter.
         */
        @NotNull  default Field<? extends Number> $n() { return $arg3(); }
    }

    /**
     * The <code>TO CHAR</code> function.
     * <p>
     * Format an arbitrary value as a string.
     */
    public sealed interface ToChar
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<?>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.ToChar
    {

        /**
         * The value to be formatted.
         */
        @NotNull  default Field<?> $value() { return $arg1(); }

        /**
         * The vendor-specific formatting string.
         */
        @Nullable default Field<String> $formatMask() { return $arg2(); }
    }

    /**
     * The <code>TO DATE</code> function.
     * <p>
     * Parse a string-formatted date value to a date.
     */
    public sealed interface ToDate
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, Field<Date>>,
            org.jooq.Field<Date>
        permits
            org.jooq.impl.ToDate
    {

        /**
         * The formatted DATE value.
         */
        @NotNull  default Field<String> $value() { return $arg1(); }

        /**
         * The vendor-specific formatting string.
         */
        @NotNull  default Field<String> $formatMask() { return $arg2(); }
    }

    /**
     * The <code>TO HEX</code> function.
     * <p>
     * Format a number to its hex value.
     */
    public sealed interface ToHex
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Number>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.ToHex
    {
        @NotNull  default Field<? extends Number> $value() { return $arg1(); }
    }

    /**
     * The <code>TO TIMESTAMP</code> function.
     * <p>
     * Parse a string-formatted timestamp value to a timestamp.
     */
    public sealed interface ToTimestamp
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, Field<Timestamp>>,
            org.jooq.Field<Timestamp>
        permits
            org.jooq.impl.ToTimestamp
    {

        /**
         * The formatted TIMESTAMP value.
         */
        @NotNull  default Field<String> $value() { return $arg1(); }

        /**
         * The vendor-specific formatting string.
         */
        @NotNull  default Field<String> $formatMask() { return $arg2(); }
    }

    /**
     * The <code>TRANSLATE</code> function.
     * <p>
     * Translate a set of characters to another set of characters in a string.
     */
    public sealed interface Translate
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<String>, Field<String>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Translate
    {

        /**
         * The string to translate.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The set of source characters.
         */
        @NotNull  default Field<String> $from() { return $arg2(); }

        /**
         * The set of target characters, matched with source characters by position.
         */
        @NotNull  default Field<String> $to() { return $arg3(); }
    }

    /**
     * The <code>TRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from both sides of a string.
     */
    public sealed interface Trim
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<String>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Trim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull  default Field<String> $string() { return $arg1(); }

        /**
         * The characters to be trimmed.
         */
        @Nullable default Field<String> $characters() { return $arg2(); }
    }

    /**
     * The <code>UPPER</code> function.
     * <p>
     * Turn a string into upper case.
     */
    public sealed interface Upper
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.Upper
    {
        @NotNull  default Field<String> $string() { return $arg1(); }
    }

    /**
     * The <code>UUID</code> function.
     * <p>
     * Generate a random UUID.
     */
    public sealed interface Uuid
        extends
            UOperator0<Field<UUID>>,
            org.jooq.Field<UUID>
        permits
            org.jooq.impl.Uuid
    {}

    /**
     * The <code>DATE ADD</code> function.
     * <p>
     * Add an interval to a date.
     */
    public sealed interface DateAdd<T>
        extends
            UReturnsNullOnNullInput,
            UOperator3<Field<T>, Field<? extends Number>, DatePart, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.DateAdd
    {

        /**
         * The date to add an interval to
         */
        @NotNull  default Field<T> $date() { return $arg1(); }

        /**
         * The interval to add to the date
         */
        @NotNull  default Field<? extends Number> $interval() { return $arg2(); }

        /**
         * The date part describing the interval
         */
        @Nullable default DatePart $datePart() { return $arg3(); }
    }

    /**
     * The <code>CARDINALITY</code> function.
     * <p>
     * Calculate the cardinality of an array field.
     */
    public sealed interface Cardinality
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<? extends Object[]>, Field<Integer>>,
            org.jooq.Field<Integer>
        permits
            org.jooq.impl.Cardinality
    {
        @NotNull  default Field<? extends Object[]> $array() { return $arg1(); }
    }

    /**
     * The <code>ARRAY GET</code> function.
     * <p>
     * Get an array element at a given index (1 based).
     */
    public sealed interface ArrayGet<T>
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<T[]>, Field<Integer>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.ArrayGet
    {
        @NotNull  default Field<T[]> $array() { return $arg1(); }
        @NotNull  default Field<Integer> $index() { return $arg2(); }
    }

    /**
     * The <code>NVL</code> function.
     * <p>
     * Return the first non-null argument.
     */
    public sealed interface Nvl<T>
        extends
            UOperator2<Field<T>, Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Nvl
    {

        /**
         * The nullable value.
         */
        @NotNull  default Field<T> $value() { return $arg1(); }

        /**
         * The default value if the other value is null.
         */
        @NotNull  default Field<T> $defaultValue() { return $arg2(); }
    }

    /**
     * The <code>NULLIF</code> function.
     */
    public sealed interface Nullif<T>
        extends
            UOperator2<Field<T>, Field<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.Nullif
    {

        /**
         * The result value if the other value is not equal.
         */
        @NotNull  default Field<T> $value() { return $arg1(); }

        /**
         * The value to compare the result value with.
         */
        @NotNull  default Field<T> $other() { return $arg2(); }
    }

    /**
     * The <code>CURRENT CATALOG</code> function.
     */
    public sealed interface CurrentCatalog
        extends
            UOperator0<Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.CurrentCatalog
    {}

    /**
     * The <code>CURRENT SCHEMA</code> function.
     */
    public sealed interface CurrentSchema
        extends
            UOperator0<Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.CurrentSchema
    {}

    /**
     * The <code>CURRENT USER</code> function.
     */
    public sealed interface CurrentUser
        extends
            UOperator0<Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.CurrentUser
    {}













































































































































    /**
     * The <code>XMLCOMMENT</code> function.
     */
    public sealed interface XMLComment
        extends
            UOperator1<Field<String>, Field<XML>>,
            org.jooq.Field<XML>
        permits
            org.jooq.impl.XMLComment
    {
        @NotNull  default Field<String> $comment() { return $arg1(); }
    }

    /**
     * The <code>XMLCONCAT</code> function.
     */
    public sealed interface XMLConcat
        extends
            UOperator1<UnmodifiableList<? extends Field<?>>, Field<XML>>,
            org.jooq.Field<XML>
        permits
            org.jooq.impl.XMLConcat
    {}

















    /**
     * The <code>XMLFOREST</code> function.
     */
    public sealed interface XMLForest
        extends
            UOperator1<UnmodifiableList<? extends Field<?>>, Field<XML>>,
            org.jooq.Field<XML>
        permits
            org.jooq.impl.XMLForest
    {
        @NotNull  default UnmodifiableList<? extends Field<?>> $fields() { return $arg1(); }
    }

    /**
     * The <code>XMLPI</code> function.
     */
    public sealed interface XMLPi
        extends
            UOperator2<Name, Field<?>, Field<XML>>,
            org.jooq.Field<XML>
        permits
            org.jooq.impl.XMLPi
    {
        @NotNull  default Name $target() { return $arg1(); }
        @Nullable default Field<?> $content() { return $arg2(); }
    }

    /**
     * The <code>XMLSERIALIZE</code> function.
     */
    public sealed interface XMLSerialize<T>
        extends
            UOperator3<Boolean, Field<XML>, DataType<T>, Field<T>>,
            org.jooq.Field<T>
        permits
            org.jooq.impl.XMLSerialize
    {
        @NotNull  default Boolean $content() { return $arg1(); }
        @NotNull  default Field<XML> $value() { return $arg2(); }
        @NotNull  default DataType<T> $type() { return $arg3(); }
    }

    /**
     * The <code>JSON ARRAY</code> function.
     */
    public sealed interface JSONArray<T>
        extends
            UOperator4<DataType<T>, UnmodifiableList<? extends Field<?>>, JSONOnNull, DataType<?>, Field<T>>,
            Field<T>
        permits
            org.jooq.impl.JSONArray
    {
        @NotNull  default DataType<T> $type() { return $arg1(); }
        @NotNull  default UnmodifiableList<? extends Field<?>> $fields() { return $arg2(); }
        @Nullable default JSONOnNull $onNull() { return $arg3(); }
        @Nullable default DataType<?> $returning() { return $arg4(); }
    }

    /**
     * The <code>JSON OBJECT</code> function.
     */
    public sealed interface JSONObject<T>
        extends
            UOperator4<DataType<T>, UnmodifiableList<? extends JSONEntry<?>>, JSONOnNull, DataType<?>, Field<T>>,
            Field<T>
        permits
            org.jooq.impl.JSONObject
    {
        @NotNull  default DataType<T> $type() { return $arg1(); }
        @NotNull  default UnmodifiableList<? extends JSONEntry<?>> $entries() { return $arg2(); }
        @Nullable default JSONOnNull $onNull() { return $arg3(); }
        @Nullable default DataType<?> $returning() { return $arg4(); }
    }

    /**
     * The <code>JSON GET ELEMENT</code> function.
     * <p>
     * Access an array element from a JSON array expression.
     */
    public sealed interface JSONGetElement
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<Integer>, Field<JSON>>,
            org.jooq.Field<JSON>
        permits
            org.jooq.impl.JSONGetElement
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
        @NotNull  default Field<Integer> $index() { return $arg2(); }
    }

    /**
     * The <code>JSONB GET ELEMENT</code> function.
     * <p>
     * Access an array element from a JSONB array expression.
     */
    public sealed interface JSONBGetElement
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<Integer>, Field<JSONB>>,
            org.jooq.Field<JSONB>
        permits
            org.jooq.impl.JSONBGetElement
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
        @NotNull  default Field<Integer> $index() { return $arg2(); }
    }

    /**
     * The <code>JSON GET ELEMENT AS TEXT</code> function.
     * <p>
     * Access an array element from a JSON array expression and return it as a string.
     */
    public sealed interface JSONGetElementAsText
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<Integer>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.JSONGetElementAsText
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
        @NotNull  default Field<Integer> $index() { return $arg2(); }
    }

    /**
     * The <code>JSONB GET ELEMENT AS TEXT</code> function.
     * <p>
     * Access an array element from a JSONB array expression and return it as a string.
     */
    public sealed interface JSONBGetElementAsText
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<Integer>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.JSONBGetElementAsText
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
        @NotNull  default Field<Integer> $index() { return $arg2(); }
    }

    /**
     * The <code>JSON GET ATTRIBUTE</code> function.
     * <p>
     * Access an object attribute value from a JSON object expression.
     */
    public sealed interface JSONGetAttribute
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<String>, Field<JSON>>,
            org.jooq.Field<JSON>
        permits
            org.jooq.impl.JSONGetAttribute
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
        @NotNull  default Field<String> $attribute() { return $arg2(); }
    }

    /**
     * The <code>JSONB GET ATTRIBUTE</code> function.
     * <p>
     * Access an object attribute value from a JSONB object expression.
     */
    public sealed interface JSONBGetAttribute
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<String>, Field<JSONB>>,
            org.jooq.Field<JSONB>
        permits
            org.jooq.impl.JSONBGetAttribute
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
        @NotNull  default Field<String> $attribute() { return $arg2(); }
    }

    /**
     * The <code>JSON GET ATTRIBUTE AS TEXT</code> function.
     * <p>
     * Access an object attribute value from a JSON object expression and return it as string.
     */
    public sealed interface JSONGetAttributeAsText
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.JSONGetAttributeAsText
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
        @NotNull  default Field<String> $attribute() { return $arg2(); }
    }

    /**
     * The <code>JSONB GET ATTRIBUTE AS TEXT</code> function.
     * <p>
     * Access an object attribute value from a JSONB object expression and return it as
     * string.
     */
    public sealed interface JSONBGetAttributeAsText
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<String>, Field<String>>,
            org.jooq.Field<String>
        permits
            org.jooq.impl.JSONBGetAttributeAsText
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
        @NotNull  default Field<String> $attribute() { return $arg2(); }
    }

    /**
     * The <code>JSON KEYS</code> function.
     * <p>
     * Retrieve all keys from a JSON object as an array of strings.
     */
    public sealed interface JSONKeys
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<JSON>, Field<JSON>>,
            org.jooq.Field<JSON>
        permits
            org.jooq.impl.JSONKeys
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
    }

    /**
     * The <code>JSONB KEYS</code> function.
     * <p>
     * Retrieve all keys from a JSONB object as an array of strings.
     */
    public sealed interface JSONBKeys
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<JSONB>, Field<JSONB>>,
            org.jooq.Field<JSONB>
        permits
            org.jooq.impl.JSONBKeys
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
    }

    /**
     * The <code>JSON SET</code> function.
     * <p>
     * Add or replace a JSON value to a JSON field at a given path.
     */
    public sealed interface JSONSet
        extends
            UOperator3<Field<JSON>, Field<String>, Field<?>, Field<JSON>>,
            org.jooq.Field<JSON>
        permits
            org.jooq.impl.JSONSet
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
        @NotNull  default Field<String> $path() { return $arg2(); }
        @NotNull  default Field<?> $value() { return $arg3(); }
    }

    /**
     * The <code>JSONB SET</code> function.
     * <p>
     * Add or replace a JSONB value to a JSONB field at a given path.
     */
    public sealed interface JSONBSet
        extends
            UOperator3<Field<JSONB>, Field<String>, Field<?>, Field<JSONB>>,
            org.jooq.Field<JSONB>
        permits
            org.jooq.impl.JSONBSet
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
        @NotNull  default Field<String> $path() { return $arg2(); }
        @NotNull  default Field<?> $value() { return $arg3(); }
    }

    /**
     * The <code>JSON INSERT</code> function.
     * <p>
     * Add (but not replace) a JSON value to a JSON field at a given path.
     */
    public sealed interface JSONInsert
        extends
            UOperator3<Field<JSON>, Field<String>, Field<?>, Field<JSON>>,
            org.jooq.Field<JSON>
        permits
            org.jooq.impl.JSONInsert
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
        @NotNull  default Field<String> $path() { return $arg2(); }
        @NotNull  default Field<?> $value() { return $arg3(); }
    }

    /**
     * The <code>JSONB INSERT</code> function.
     * <p>
     * Add (but not replace) a JSON value to a JSON field at a given path.
     */
    public sealed interface JSONBInsert
        extends
            UOperator3<Field<JSONB>, Field<String>, Field<?>, Field<JSONB>>,
            org.jooq.Field<JSONB>
        permits
            org.jooq.impl.JSONBInsert
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
        @NotNull  default Field<String> $path() { return $arg2(); }
        @NotNull  default Field<?> $value() { return $arg3(); }
    }

    /**
     * The <code>JSON REPLACE</code> function.
     * <p>
     * Replace (but not add) a JSON value to a JSON field at a given path.
     */
    public sealed interface JSONReplace
        extends
            UOperator3<Field<JSON>, Field<String>, Field<?>, Field<JSON>>,
            org.jooq.Field<JSON>
        permits
            org.jooq.impl.JSONReplace
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
        @NotNull  default Field<String> $path() { return $arg2(); }
        @NotNull  default Field<?> $value() { return $arg3(); }
    }

    /**
     * The <code>JSONB REPLACE</code> function.
     * <p>
     * Replace (but not add) a JSONB value to a JSONB field at a given path.
     */
    public sealed interface JSONBReplace
        extends
            UOperator3<Field<JSONB>, Field<String>, Field<?>, Field<JSONB>>,
            org.jooq.Field<JSONB>
        permits
            org.jooq.impl.JSONBReplace
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
        @NotNull  default Field<String> $path() { return $arg2(); }
        @NotNull  default Field<?> $value() { return $arg3(); }
    }

    /**
     * The <code>JSON REMOVE</code> function.
     * <p>
     * Remove a JSON value from a JSON field at a given path.
     */
    public sealed interface JSONRemove
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSON>, Field<String>, Field<JSON>>,
            org.jooq.Field<JSON>
        permits
            org.jooq.impl.JSONRemove
    {
        @NotNull  default Field<JSON> $field() { return $arg1(); }
        @NotNull  default Field<String> $path() { return $arg2(); }
    }

    /**
     * The <code>JSONB REMOVE</code> function.
     * <p>
     * Remove a JSONB value from a JSONB field at a given path.
     */
    public sealed interface JSONBRemove
        extends
            UReturnsNullOnNullInput,
            UOperator2<Field<JSONB>, Field<String>, Field<JSONB>>,
            org.jooq.Field<JSONB>
        permits
            org.jooq.impl.JSONBRemove
    {
        @NotNull  default Field<JSONB> $field() { return $arg1(); }
        @NotNull  default Field<String> $path() { return $arg2(); }
    }







































    /**
     * The <code>FIELD</code> function.
     * <p>
     * Wrap a condition in a boolean field.
     */
    public sealed interface ConditionAsField
        extends
            UReturnsNullOnNullInput,
            UOperator1<Condition, Field<Boolean>>,
            org.jooq.Field<Boolean>
        permits
            org.jooq.impl.ConditionAsField
    {
        @NotNull  default Condition $condition() { return $arg1(); }
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
    public sealed interface FieldCondition
        extends
            UReturnsNullOnNullInput,
            UOperator1<Field<Boolean>, Condition>,
            org.jooq.Condition
        permits
            org.jooq.impl.FieldCondition
    {
        @NotNull  default Field<Boolean> $field() { return $arg1(); }
    }

    /**
     * The <code>ANY VALUE</code> function.
     * <p>
     * Get any arbitrary value from the group.
     */
    public sealed interface AnyValue<T>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.AnyValue
    {
        @NotNull  Field<T> $field();
        @CheckReturnValue
        @NotNull  AnyValue<T> $field(Field<T> field);
    }

    /**
     * The <code>AVG</code> function.
     */
    public sealed interface Avg
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.Avg
    {
        @NotNull  Field<? extends Number> $field();
                  boolean $distinct();
        @CheckReturnValue
        @NotNull  Avg $field(Field<? extends Number> field);
        @CheckReturnValue
        @NotNull  Avg $distinct(boolean distinct);
    }

    /**
     * The <code>BIT AND AGG</code> function.
     * <p>
     * Calculate the bitwise <code>AND</code> aggregate value.
     */
    public sealed interface BitAndAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.BitAndAgg
    {
        @NotNull  Field<T> $value();
        @CheckReturnValue
        @NotNull  BitAndAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT OR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>OR</code> aggregate value.
     */
    public sealed interface BitOrAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.BitOrAgg
    {
        @NotNull  Field<T> $value();
        @CheckReturnValue
        @NotNull  BitOrAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT XOR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>XOR</code> aggregate value.
     */
    public sealed interface BitXorAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.BitXorAgg
    {
        @NotNull  Field<T> $value();
        @CheckReturnValue
        @NotNull  BitXorAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT NAND AGG</code> function.
     * <p>
     * Calculate the bitwise <code>NAND</code> aggregate value.
     */
    public sealed interface BitNandAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.BitNandAgg
    {
        @NotNull  Field<T> $value();
        @CheckReturnValue
        @NotNull  BitNandAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT NOR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>NOR</code> aggregate value.
     */
    public sealed interface BitNorAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.BitNorAgg
    {
        @NotNull  Field<T> $value();
        @CheckReturnValue
        @NotNull  BitNorAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT X NOR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>XNOR</code> aggregate value.
     */
    public sealed interface BitXNorAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.BitXNorAgg
    {
        @NotNull  Field<T> $value();
        @CheckReturnValue
        @NotNull  BitXNorAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BOOL AND</code> function.
     */
    public sealed interface BoolAnd
        extends
            org.jooq.AggregateFunction<Boolean>
        permits
            org.jooq.impl.BoolAnd
    {
        @NotNull  Condition $condition();
        @CheckReturnValue
        @NotNull  BoolAnd $condition(Condition condition);
    }

    /**
     * The <code>BOOL OR</code> function.
     */
    public sealed interface BoolOr
        extends
            org.jooq.AggregateFunction<Boolean>
        permits
            org.jooq.impl.BoolOr
    {
        @NotNull  Condition $condition();
        @CheckReturnValue
        @NotNull  BoolOr $condition(Condition condition);
    }

    /**
     * The <code>CORR</code> function.
     * <p>
     * Calculate the correlation coefficient. This standard SQL function may be supported
     * natively, or emulated using {@link #covarPop(Field, Field)} and {@link #stddevPop(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface Corr
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.Corr
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  Corr $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  Corr $x(Field<? extends Number> x);
    }

    /**
     * The <code>COUNT</code> function.
     */
    public sealed interface Count
        extends
            org.jooq.AggregateFunction<Integer>
        permits
            org.jooq.impl.Count
    {
        @NotNull  Field<?> $field();
                  boolean $distinct();
        @CheckReturnValue
        @NotNull  Count $field(Field<?> field);
        @CheckReturnValue
        @NotNull  Count $distinct(boolean distinct);
    }

    /**
     * The <code>COVAR SAMP</code> function.
     * <p>
     * Calculate the sample covariance. This standard SQL function may be supported natively,
     * or emulated using {@link #sum(Field)} and {@link #count(Field)}. If an emulation
     * is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface CovarSamp
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.CovarSamp
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  CovarSamp $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  CovarSamp $x(Field<? extends Number> x);
    }

    /**
     * The <code>COVAR POP</code> function.
     * <p>
     * Calculate the population covariance. This standard SQL function may be supported
     * natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}. If an
     * emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface CovarPop
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.CovarPop
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  CovarPop $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  CovarPop $x(Field<? extends Number> x);
    }

    /**
     * The <code>MAX</code> function.
     */
    public sealed interface Max<T>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.Max
    {
        @NotNull  Field<T> $field();
                  boolean $distinct();
        @CheckReturnValue
        @NotNull  Max<T> $field(Field<T> field);
        @CheckReturnValue
        @NotNull  Max<T> $distinct(boolean distinct);
    }

    /**
     * The <code>MEDIAN</code> function.
     */
    public sealed interface Median
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.Median
    {
        @NotNull  Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull  Median $field(Field<? extends Number> field);
    }

    /**
     * The <code>MIN</code> function.
     */
    public sealed interface Min<T>
        extends
            org.jooq.AggregateFunction<T>
        permits
            org.jooq.impl.Min
    {
        @NotNull  Field<T> $field();
                  boolean $distinct();
        @CheckReturnValue
        @NotNull  Min<T> $field(Field<T> field);
        @CheckReturnValue
        @NotNull  Min<T> $distinct(boolean distinct);
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
    public sealed interface Product
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.Product
    {
        @NotNull  Field<? extends Number> $field();
                  boolean $distinct();
        @CheckReturnValue
        @NotNull  Product $field(Field<? extends Number> field);
        @CheckReturnValue
        @NotNull  Product $distinct(boolean distinct);
    }

    /**
     * The <code>REGR AVGX</code> function.
     * <p>
     * Calculate the average of the independent values (x). This standard SQL function may
     * be supported natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrAvgX
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrAvgX
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrAvgX $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrAvgX $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR AVGY</code> function.
     * <p>
     * Calculate the average of the dependent values (y). This standard SQL function may
     * be supported natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrAvgY
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrAvgY
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrAvgY $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrAvgY $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR COUNT</code> function.
     * <p>
     * Calculate the number of non-<code>NULL</code> pairs. This standard SQL function may
     * be supported natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrCount
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrCount
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrCount $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrCount $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR INTERCEPT</code> function.
     * <p>
     * Calculate the y intercept of the regression line. This standard SQL function may
     * be supported natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrIntercept
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrIntercept
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrIntercept $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrIntercept $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR R2</code> function.
     * <p>
     * Calculate the coefficient of determination. This standard SQL function may be supported
     * natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}. If an
     * emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrR2
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrR2
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrR2 $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrR2 $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR SLOPE</code> function.
     * <p>
     * Calculate the slope of the regression line. This standard SQL function may be supported
     * natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}. If an
     * emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrSlope
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrSlope
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrSlope $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrSlope $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR SXX</code> function.
     * <p>
     * Calculate the <code>REGR_SXX</code> auxiliary function. This standard SQL function
     * may be supported natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrSxx
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrSxx
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrSxx $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrSxx $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR SXY</code> function.
     * <p>
     * Calculate the <code>REGR_SXY</code> auxiliary function. This standard SQL function
     * may be supported natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrSxy
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrSxy
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrSxy $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrSxy $x(Field<? extends Number> x);
    }

    /**
     * The <code>REGR SYY</code> function.
     * <p>
     * Calculate the <code>REGR_SYY</code> auxiliary function. This standard SQL function
     * may be supported natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}.
     * If an emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface RegrSyy
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.RegrSyy
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @CheckReturnValue
        @NotNull  RegrSyy $y(Field<? extends Number> y);
        @CheckReturnValue
        @NotNull  RegrSyy $x(Field<? extends Number> x);
    }

    /**
     * The <code>STDDEV POP</code> function.
     * <p>
     * Calculate the population standard deviation. This standard SQL function may be supported
     * natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}. If an
     * emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface StddevPop
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.StddevPop
    {
        @NotNull  Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull  StddevPop $field(Field<? extends Number> field);
    }

    /**
     * The <code>STDDEV SAMP</code> function.
     * <p>
     * Calculate the sample standard deviation. This standard SQL function may be supported
     * natively, or emulated using {@link #sum(Field)} and {@link #count(Field)}. If an
     * emulation is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface StddevSamp
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.StddevSamp
    {
        @NotNull  Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull  StddevSamp $field(Field<? extends Number> field);
    }

    /**
     * The <code>SUM</code> function.
     */
    public sealed interface Sum
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.Sum
    {
        @NotNull  Field<? extends Number> $field();
                  boolean $distinct();
        @CheckReturnValue
        @NotNull  Sum $field(Field<? extends Number> field);
        @CheckReturnValue
        @NotNull  Sum $distinct(boolean distinct);
    }

    /**
     * The <code>VAR POP</code> function.
     * <p>
     * Calculate the population variance. This standard SQL function may be supported natively,
     * or emulated using {@link #sum(Field)} and {@link #count(Field)}. If an emulation
     * is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface VarPop
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.VarPop
    {
        @NotNull  Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull  VarPop $field(Field<? extends Number> field);
    }

    /**
     * The <code>VAR SAMP</code> function.
     * <p>
     * Calculate the sample variance. This standard SQL function may be supported natively,
     * or emulated using {@link #sum(Field)} and {@link #count(Field)}. If an emulation
     * is applied, beware of the risk of "<a href="https://en.wikipedia.org/wiki/Catastrophic_cancellation">Catastrophic
     * cancellation</a>" in case the calculations are performed using floating point arithmetic.
     */
    public sealed interface VarSamp
        extends
            org.jooq.AggregateFunction<BigDecimal>
        permits
            org.jooq.impl.VarSamp
    {
        @NotNull  Field<? extends Number> $field();
        @CheckReturnValue
        @NotNull  VarSamp $field(Field<? extends Number> field);
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



    // -------------------------------------------------------------------------
    // XXX: Utility API
    // -------------------------------------------------------------------------

    interface UOperator<R extends org.jooq.QueryPart> extends org.jooq.QueryPart {
        @NotNull
        List<?> $args();
    }
    interface UOperator0<R extends org.jooq.QueryPart> extends UOperator<R> {

        @NotNull
        Function0<? extends R> $constructor();

        @NotNull
        @Override
        default List<?> $args() {
            return Collections.emptyList();
        }



















    }

    interface UOperator1<Q1, R extends org.jooq.QueryPart> extends UOperator<R> {
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

    interface UOperator2<Q1, Q2, R extends org.jooq.QueryPart> extends UOperator<R> {
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

    interface UOperator3<Q1, Q2, Q3, R extends org.jooq.QueryPart> extends UOperator<R> {
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

    interface UOperator4<Q1, Q2, Q3, Q4, R extends org.jooq.QueryPart> extends UOperator<R> {
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

    /**
     * A binary {@link UOperator2} whose operands can be swapped using
     * {@link #$converse()} producing the converse relation.
     */
    interface UConvertibleOperator<Q, R extends org.jooq.QueryPart> extends UOperator2<Q, Q, R> {

        /**
         * Create a new expression with swapped operands, using the converse
         * operator.
         */
        R $converse();
    }

    /**
     * A binary {@link UOperator2} whose operands can be swapped using
     * {@link #$swap()} without changing the operator's semantics.
     */
    interface UCommutativeOperator<Q, R extends org.jooq.QueryPart> extends UConvertibleOperator<Q, R> {

        /**
         * Create a new expression with swapped operands.
         */
        default R $swap() {
            return $constructor().apply($arg2(), $arg1());
        }

        @Override
        default R $converse() {
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
    sealed interface UOpaque
        extends
            UEmpty
        permits



            CustomField,
            CustomTable,
            CustomCondition,
            CustomQueryPart
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
