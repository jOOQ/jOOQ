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

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
import static org.jooq.impl.DSL.keyword;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

// ...
import org.jooq.Catalog;
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
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.Spatial;
import org.jooq.Statement;
import org.jooq.Table;
// ...
// ...
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;
import org.jooq.XML;
import org.jooq.XMLAttributes;
import org.jooq.conf.Settings;
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
     * An unmodifiable {@link Collection} of {@link QueryPart} elements.
     */
    public /* sealed */ interface UnmodifiableCollection<Q extends org.jooq.QueryPart>
        extends
            org.jooq.QueryPart,
            java.util.Collection<Q>
        /* permits
            UnmodifiableList,
            QueryPartCollectionView */ 
    {}

    /**
     * An unmodifiable {@link List} of {@link QueryPart} elements.
     */
    public /* sealed */ interface UnmodifiableList<Q extends org.jooq.QueryPart>
        extends
            UnmodifiableCollection<Q>,
            java.util.List<Q>
        /* permits
            QueryPartListView */ 
    {}

    public /*sealed*/ interface With
        extends
            org.jooq.QueryPart
        /*permits
            WithImpl*/
    {
        @NotNull UnmodifiableList<? extends CommonTableExpression<?>> $commonTableExpressions();
        boolean $recursive();
    }

    // -------------------------------------------------------------------------
    // XXX: Queries
    // -------------------------------------------------------------------------

    public /*sealed*/ interface CreateType
        extends
            DDLQuery
        /*permits
            CreateTypeImpl*/
    {
        @NotNull Name $name();
        @NotNull UnmodifiableList<? extends Field<String>> $values();
    }

    public /*sealed*/ interface DropType
        extends
            DDLQuery
        /*permits
            DropTypeImpl*/
    {
        @NotNull UnmodifiableList<? extends Name> $names();
        boolean $ifExists();
        @Nullable Cascade $cascade();
    }

    public /*sealed*/ interface CreateView<R extends Record>
        extends
            DDLQuery
        /*permits
            CreateViewImpl*/
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

    public interface NullStatement extends Statement {}








































    // -------------------------------------------------------------------------
    // XXX: Tables
    // -------------------------------------------------------------------------

    public interface TableAlias<R extends Record> extends Table<R> {
        @NotNull Table<R> $table();
        @NotNull Name $alias();
        // TODO [#12425] Reuse MDerivedColumnList
    }
    public interface Dual extends Table<Record>, UEmpty {}
    public interface Lateral<R extends Record> extends Table<R>, UOperator1<Table<R>, Table<R>> {}
    public interface DerivedTable<R extends Record> extends Table<R>, UOperator1<Select<R>, Table<R>> {}
    public interface Values<R extends Record> extends Table<R>, UOperator1<UnmodifiableList<? extends Row>, Table<R>> {}
    public interface DataChangeDeltaTable<R extends Record> extends Table<R> {
        @NotNull ResultOption $resultOption();
        @NotNull DMLQuery<R> $query();
    }
    public interface RowsFrom extends Table<Record> {
        @NotNull UnmodifiableList<? extends Table<?>> $tables();
    }
    public interface GenerateSeries<T> extends Table<Record1<T>>, UOperator3<Field<T>, Field<T>, Field<T>, Table<Record1<T>>> {
        @NotNull default Field<T> $from() { return $arg1(); }
        @NotNull default Field<T> $to() { return $arg2(); }
        @Nullable default Field<T> $step() { return $arg3(); }
    }

    // -------------------------------------------------------------------------
    // XXX: Conditions
    // -------------------------------------------------------------------------

    public /*sealed*/ interface CombinedCondition
        extends
            Condition,
            UOperator2<Condition, Condition, Condition>
        /*permits
            MAnd,
            MOr*/
    {}

    public /*sealed*/ interface CompareCondition<T>
        extends
            Condition,
            UOperator2<Field<T>, Field<T>, Condition>
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

    public interface Between<T> extends UOperator3<Field<T>, Field<T>, Field<T>, Condition> {
        boolean $symmetric();
    }

    public /*sealed*/ interface InList<T>
        extends
            Condition,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, Condition>
        /*permits
            InList*/
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        @NotNull default UnmodifiableList<? extends Field<T>> $list() { return $arg2(); }
    }

    public /*sealed*/ interface NotInList<T>
        extends
            Condition,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, Condition>
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
            UOperator1<Row, Condition>
        /*permits
            RowIsNull*/
    {
        @NotNull default Row $field() { return $arg1(); }
    }

    public /*sealed*/ interface RowIsNotNull
        extends
            Condition,
            UOperator1<Row, Condition>
        /*permits
            RowIsNotNull*/
    {
        @NotNull default Row $field() { return $arg1(); }
    }

    public /*sealed*/ interface RowOverlaps
        extends
            Condition,
            UOperator2<Row, Row, Condition>
        /*permits
            RowOverlaps*/
    {}

    public /*sealed*/ interface SelectIsNull
        extends
            Condition,
            UOperator1<Select<?>, Condition>
        /*permits
            SelectIsNull*/
    {
        @NotNull default Select<?> $field() { return $arg1(); }
    }

    public /*sealed*/ interface SelectIsNotNull
        extends
            Condition,
            UOperator1<Select<?>, Condition>
        /*permits
            SelectIsNotNull*/
    {
        @NotNull default Select<?> $field() { return $arg1(); }
    }


    // -------------------------------------------------------------------------
    // XXX: Rows
    // -------------------------------------------------------------------------

    public /*sealed*/ interface RowField<R extends Record>
        extends
            Field<R>
        /*permits
            RowField*/
    {
        @NotNull Row $row();
    }

    // -------------------------------------------------------------------------
    // XXX: SelectFields, GroupFields and SortFields
    // -------------------------------------------------------------------------

    // Can't seal these types yet because of https://bugs.eclipse.org/bugs/show_bug.cgi?id=577872

    public /* non-sealed */ interface Rollup
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends FieldOrRow>, GroupField>
        /*permits
            Rollup*/
    {}

    public /* non-sealed */ interface Cube
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends FieldOrRow>, GroupField>
        /*permits
            Cube*/
    {}

    public /* non-sealed */ interface GroupingSets
        extends
            GroupField,
            UOperator1<UnmodifiableList<? extends UnmodifiableList<? extends FieldOrRow>>, GroupField>
        /*permits
            GroupingSets*/
    {}

    // -------------------------------------------------------------------------
    // XXX: Aggregate functions and window functions
    // -------------------------------------------------------------------------

    public /*sealed*/ interface RatioToReport
        extends
            org.jooq.AggregateFunction<BigDecimal>
        /*permits
            RatioToReport*/
    {
        @NotNull Field<? extends Number> $field();
    }

    public /*sealed*/ interface Mode<T>
        extends
            org.jooq.AggregateFunction<T>,
            UOperator1<Field<T>, org.jooq.AggregateFunction<T>>
        /*permits
            Mode*/
    {
        @NotNull default Field<T> $field() { return $arg1(); }
    }

    public /*sealed*/ interface MultisetAgg<R extends Record>
        extends
            org.jooq.AggregateFunction<Result<R>>
        /*permits
            MultisetAgg*/
    {
        @NotNull Row $row();
    }

    public /*sealed*/ interface ArrayAgg<T>
        extends
            org.jooq.AggregateFunction<T[]>,
            UOperator1<Field<T>, org.jooq.AggregateFunction<T[]>>
        /*permits
            ArrayAgg*/
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        boolean $distinct();
    }

    public /*sealed*/ interface XMLAgg
        extends
            org.jooq.AggregateFunction<XML>,
            UOperator1<Field<XML>, org.jooq.AggregateFunction<XML>>
        /*permits
            XMLAgg*/
    {
        @NotNull default Field<XML> $field() { return $arg1(); }
    }

    public /*sealed*/ interface JSONArrayAgg<J>
        extends
            org.jooq.AggregateFunction<J>,
            UOperator1<org.jooq.Field<?>, org.jooq.AggregateFunction<J>>
        /*permits
            JSONArrayAgg*/
    {
        @NotNull default Field<?> $field() { return $arg1(); }
        @Nullable JSONOnNull $onNull();
        @Nullable DataType<?> $returning();
    }

    public /*sealed*/ interface JSONObjectAgg<J>
        extends
            org.jooq.AggregateFunction<J>,
            UOperator1<JSONEntry<?>, org.jooq.AggregateFunction<J>>
        /*permits JSONObjectAgg*/
    {
        @NotNull default JSONEntry<?> $entry() { return $arg1(); }
        @Nullable JSONOnNull $onNull();
        @Nullable DataType<?> $returning();
    }

    public /*sealed*/ interface CountTable
        extends
            org.jooq.AggregateFunction<Integer>
        /*permits
            CountTable*/
    {
        @NotNull Table<?> $table();
        boolean $distinct();
    }











    public interface WindowFunction<T> extends org.jooq.Field<T> {
        @Nullable WindowSpecification $windowSpecification();
        @Nullable WindowDefinition $windowDefinition();
    }

    public /*sealed*/ interface RowNumber
        extends
            WindowFunction<Integer>
        /*permits
            RowNumber*/
    {}

    public /*sealed*/ interface Rank
        extends
            WindowFunction<Integer>
        /*permits
            Rank*/
    {}

    public /*sealed*/ interface DenseRank
        extends
            WindowFunction<Integer>
        /*permits
            DenseRank*/
    {}

    public /*sealed*/ interface PercentRank
        extends
            WindowFunction<BigDecimal>
        /*permits
            PercentRank*/
    {}

    public /*sealed*/ interface CumeDist
        extends
            WindowFunction<BigDecimal>
        /*permits
            CumeDist*/
    {}

    public /*sealed*/ interface Ntile
        extends
            WindowFunction<Integer>
        /*permits Ntile*/
    {
        @NotNull Field<Integer> $tiles();
    }

    public /*sealed*/ interface Lead<T>
        extends
            WindowFunction<T>
        /*permits
            Lead*/
    {
        @NotNull Field<T> $field();
        @Nullable Field<Integer> $offset();
        @Nullable Field<T> $defaultValue();
        @Nullable NullTreatment $nullTreatment();
    }

    public /*sealed*/ interface Lag<T>
        extends
            WindowFunction<T>
        /*permits
            Lag*/
    {
        @NotNull Field<T> $field();
        @Nullable Field<Integer> $offset();
        @Nullable Field<T> $defaultValue();
        @Nullable NullTreatment $nullTreatment();
    }

    public /*sealed*/ interface FirstValue<T>
        extends
            WindowFunction<T>
        /*permits
            FirstValue*/
    {
        @NotNull Field<T> $field();
        @Nullable NullTreatment $nullTreatment();
    }

    public /*sealed*/ interface LastValue<T>
        extends
            WindowFunction<T>
        /*permits
            LastValue*/
    {
        @NotNull Field<T> $field();
        @Nullable NullTreatment $nullTreatment();
    }

    public /*sealed*/ interface NthValue<T>
        extends
            WindowFunction<T>
        /*permits
            NthValue*/
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
            Field<T>
        /*permits
            FieldAlias*/
    {
        @NotNull Field<T> $field();
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

    public /*sealed*/ interface Cast<T>
        extends
            Field<T>
        /*permits
            Cast*/
    {
        @NotNull Field<?> $field();
    }

    public /*sealed*/ interface Coerce<T>
        extends
            Field<T>
        /*permits
            Coerce*/
    {
        @NotNull Field<?> $field();
    }

    public interface Inline<T> extends Param<T> {}
    public interface Val<T> extends Param<T> {}

    public /*sealed*/ interface Default<T>
        extends
            Field<T>,
            UEmpty
        /*permits
            Default*/
    {}

    public /*sealed*/ interface Collated
        extends
            Field<String>
        /*permits
            Collated*/
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

    public /*sealed*/ interface Multiset<R extends org.jooq.Record>
        extends
            org.jooq.Field<org.jooq.Result<R>>
        /*permits
            Multiset*/
    {
        @NotNull Select<R> $select();
    }

    public /*sealed*/ interface ScalarSubquery<T>
        extends
            Field<T>,
            UOperator1<Select<? extends Record1<T>>, Field<T>>
        /*permits
            ScalarSubquery*/
    {}

    public /*sealed*/ interface Neg<T>
        extends
            Field<T>,
            UOperator1<Field<T>, Field<T>>
        /*permits
            Neg*/
    {}

    public /*sealed*/ interface Greatest<T>
        extends
            Field<T>,
            UOperator1<UnmodifiableList<? extends Field<T>>, Field<T>>
        /*permits
            Greatest*/
    {}

    public /*sealed*/ interface Least<T>
        extends
            Field<T>,
            UOperator1<UnmodifiableList<? extends Field<T>>, Field<T>>
        /*permits
            Least*/
    {}

    public /*sealed*/ interface Choose<T>
        extends
            Field<T>,
            UOperator2<Field<Integer>, UnmodifiableList<? extends Field<T>>, Field<T>>
        /*permits
            Choose*/
    {}

    public /*sealed*/ interface FieldFunction<T>
        extends
            Field<Integer>,
            UOperator2<Field<T>, UnmodifiableList<? extends Field<T>>, Field<Integer>>
        /*permits
            FieldFunction*/
    {}

    public /*sealed*/ interface Nvl2<T>
        extends
            Field<T>,
            UOperator3<Field<?>, Field<T>, Field<T>, Field<T>>
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
            UOperator3<Condition, Field<T>, Field<T>, Field<T>>
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
            UOperator1<UnmodifiableList<? extends Field<T>>, Field<T>>
        /*permits
            Coalesce*/
    {}

    public /*sealed*/ interface Concat
        extends
            Field<String>,
            UOperator1<UnmodifiableList<? extends Field<?>>, Field<String>>
        /*permits
            Concat*/
    {}

    public /*sealed*/ interface TimestampDiff<T>
        extends
            Field<DayToSecond>,
            UOperator2<Field<T>, Field<T>, Field<DayToSecond>>
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
        @NotNull  Catalog $database();
                  boolean $ifExists();
        @NotNull  Catalog $renameTo();
        @NotNull  AlterDatabase $database(Catalog database);
        @NotNull  AlterDatabase $ifExists(boolean ifExists);
        @NotNull  AlterDatabase $renameTo(Catalog renameTo);
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
        @NotNull  AlterDomain<T> $domain(Domain<T> domain);
        @NotNull  AlterDomain<T> $ifExists(boolean ifExists);
        @NotNull  AlterDomain<T> $addConstraint(Constraint addConstraint);
        @NotNull  AlterDomain<T> $dropConstraint(Constraint dropConstraint);
        @NotNull  AlterDomain<T> $dropConstraintIfExists(boolean dropConstraintIfExists);
        @NotNull  AlterDomain<T> $renameTo(Domain<?> renameTo);
        @NotNull  AlterDomain<T> $renameConstraint(Constraint renameConstraint);
        @NotNull  AlterDomain<T> $renameConstraintIfExists(boolean renameConstraintIfExists);
        @NotNull  AlterDomain<T> $setDefault(Field<T> setDefault);
        @NotNull  AlterDomain<T> $dropDefault(boolean dropDefault);
        @NotNull  AlterDomain<T> $setNotNull(boolean setNotNull);
        @NotNull  AlterDomain<T> $dropNotNull(boolean dropNotNull);
        @NotNull  AlterDomain<T> $cascade(Cascade cascade);
        @NotNull  AlterDomain<T> $renameConstraintTo(Constraint renameConstraintTo);
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
        @NotNull  Index $index();
                  boolean $ifExists();
        @Nullable Table<?> $on();
        @NotNull  Index $renameTo();
        @NotNull  AlterIndex $index(Index index);
        @NotNull  AlterIndex $ifExists(boolean ifExists);
        @NotNull  AlterIndex $on(Table<?> on);
        @NotNull  AlterIndex $renameTo(Index renameTo);
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
        @NotNull  Schema $schema();
                  boolean $ifExists();
        @NotNull  Schema $renameTo();
        @NotNull  AlterSchema $schema(Schema schema);
        @NotNull  AlterSchema $ifExists(boolean ifExists);
        @NotNull  AlterSchema $renameTo(Schema renameTo);
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
        @NotNull  AlterSequence<T> $sequence(Sequence<T> sequence);
        @NotNull  AlterSequence<T> $ifExists(boolean ifExists);
        @NotNull  AlterSequence<T> $renameTo(Sequence<?> renameTo);
        @NotNull  AlterSequence<T> $restart(boolean restart);
        @NotNull  AlterSequence<T> $restartWith(Field<T> restartWith);
        @NotNull  AlterSequence<T> $startWith(Field<T> startWith);
        @NotNull  AlterSequence<T> $incrementBy(Field<T> incrementBy);
        @NotNull  AlterSequence<T> $minvalue(Field<T> minvalue);
        @NotNull  AlterSequence<T> $noMinvalue(boolean noMinvalue);
        @NotNull  AlterSequence<T> $maxvalue(Field<T> maxvalue);
        @NotNull  AlterSequence<T> $noMaxvalue(boolean noMaxvalue);
        @NotNull  AlterSequence<T> $cycle(CycleOption cycle);
        @NotNull  AlterSequence<T> $cache(Field<T> cache);
        @NotNull  AlterSequence<T> $noCache(boolean noCache);
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
        @NotNull  Name $type();
        @Nullable Name $renameTo();
        @Nullable Schema $setSchema();
        @Nullable Field<String> $addValue();
        @Nullable Field<String> $renameValue();
        @Nullable Field<String> $renameValueTo();
        @NotNull  AlterType $type(Name type);
        @NotNull  AlterType $renameTo(Name renameTo);
        @NotNull  AlterType $setSchema(Schema setSchema);
        @NotNull  AlterType $addValue(Field<String> addValue);
        @NotNull  AlterType $renameValue(Field<String> renameValue);
        @NotNull  AlterType $renameValueTo(Field<String> renameValueTo);
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
        @NotNull  Table<?> $view();
                  boolean $ifExists();
        @Nullable Comment $comment();
        @Nullable Table<?> $renameTo();
        @NotNull  AlterView $view(Table<?> view);
        @NotNull  AlterView $ifExists(boolean ifExists);
        @NotNull  AlterView $comment(Comment comment);
        @NotNull  AlterView $renameTo(Table<?> renameTo);
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
        @Nullable Table<?> $table();
                  boolean $isView();
        @Nullable Field<?> $field();
        @NotNull  Comment $comment();
        @NotNull  CommentOn $table(Table<?> table);
        @NotNull  CommentOn $isView(boolean isView);
        @NotNull  CommentOn $field(Field<?> field);
        @NotNull  CommentOn $comment(Comment comment);
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
        @NotNull  Catalog $database();
                  boolean $ifNotExists();
        @NotNull  CreateDatabase $database(Catalog database);
        @NotNull  CreateDatabase $ifNotExists(boolean ifNotExists);
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
        @NotNull  Domain<?> $domain();
                  boolean $ifNotExists();
        @NotNull  DataType<T> $dataType();
        @Nullable Field<T> $default_();
        @NotNull  UnmodifiableList<? extends Constraint> $constraints();
        @NotNull  CreateDomain<T> $domain(Domain<?> domain);
        @NotNull  CreateDomain<T> $ifNotExists(boolean ifNotExists);
        @NotNull  CreateDomain<T> $dataType(DataType<T> dataType);
        @NotNull  CreateDomain<T> $default_(Field<T> default_);
        @NotNull  CreateDomain<T> $constraints(UnmodifiableList<? extends Constraint> constraints);
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
        @NotNull  UnmodifiableList<? extends OrderField<?>> $on();
        @NotNull  UnmodifiableList<? extends Field<?>> $include();
        @Nullable Condition $where();
                  boolean $excludeNullKeys();
        @NotNull  CreateIndex $unique(boolean unique);
        @NotNull  CreateIndex $index(Index index);
        @NotNull  CreateIndex $ifNotExists(boolean ifNotExists);
        @NotNull  CreateIndex $table(Table<?> table);
        @NotNull  CreateIndex $on(UnmodifiableList<? extends OrderField<?>> on);
        @NotNull  CreateIndex $include(UnmodifiableList<? extends Field<?>> include);
        @NotNull  CreateIndex $where(Condition where);
        @NotNull  CreateIndex $excludeNullKeys(boolean excludeNullKeys);
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
        @NotNull  Schema $schema();
                  boolean $ifNotExists();
        @NotNull  CreateSchema $schema(Schema schema);
        @NotNull  CreateSchema $ifNotExists(boolean ifNotExists);
    }

    /**
     * The <code>CREATE SEQUENCE</code> statement.
     */
    public /*sealed*/ interface CreateSequence
        extends
            DDLQuery
        //permits
        //    CreateSequenceImpl
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
        @NotNull  CreateSequence $sequence(Sequence<?> sequence);
        @NotNull  CreateSequence $ifNotExists(boolean ifNotExists);
        @NotNull  CreateSequence $startWith(Field<? extends Number> startWith);
        @NotNull  CreateSequence $incrementBy(Field<? extends Number> incrementBy);
        @NotNull  CreateSequence $minvalue(Field<? extends Number> minvalue);
        @NotNull  CreateSequence $noMinvalue(boolean noMinvalue);
        @NotNull  CreateSequence $maxvalue(Field<? extends Number> maxvalue);
        @NotNull  CreateSequence $noMaxvalue(boolean noMaxvalue);
        @NotNull  CreateSequence $cycle(CycleOption cycle);
        @NotNull  CreateSequence $cache(Field<? extends Number> cache);
        @NotNull  CreateSequence $noCache(boolean noCache);
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
        @NotNull  Catalog $database();
                  boolean $ifExists();
        @NotNull  DropDatabase $database(Catalog database);
        @NotNull  DropDatabase $ifExists(boolean ifExists);
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
        @NotNull  Domain<?> $domain();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @NotNull  DropDomain $domain(Domain<?> domain);
        @NotNull  DropDomain $ifExists(boolean ifExists);
        @NotNull  DropDomain $cascade(Cascade cascade);
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
        @NotNull  Index $index();
                  boolean $ifExists();
        @Nullable Table<?> $on();
        @Nullable Cascade $cascade();
        @NotNull  DropIndex $index(Index index);
        @NotNull  DropIndex $ifExists(boolean ifExists);
        @NotNull  DropIndex $on(Table<?> on);
        @NotNull  DropIndex $cascade(Cascade cascade);
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
        @NotNull  Schema $schema();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @NotNull  DropSchema $schema(Schema schema);
        @NotNull  DropSchema $ifExists(boolean ifExists);
        @NotNull  DropSchema $cascade(Cascade cascade);
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
        @NotNull  Sequence<?> $sequence();
                  boolean $ifExists();
        @NotNull  DropSequence $sequence(Sequence<?> sequence);
        @NotNull  DropSequence $ifExists(boolean ifExists);
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
                  boolean $temporary();
        @NotNull  Table<?> $table();
                  boolean $ifExists();
        @Nullable Cascade $cascade();
        @NotNull  DropTable $temporary(boolean temporary);
        @NotNull  DropTable $table(Table<?> table);
        @NotNull  DropTable $ifExists(boolean ifExists);
        @NotNull  DropTable $cascade(Cascade cascade);
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
        @NotNull  Table<?> $view();
                  boolean $ifExists();
        @NotNull  DropView $view(Table<?> view);
        @NotNull  DropView $ifExists(boolean ifExists);
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
        @NotNull  UnmodifiableList<? extends Privilege> $privileges();
        @NotNull  Table<?> $on();
        @Nullable Role $to();
                  boolean $toPublic();
                  boolean $withGrantOption();
        @NotNull  Grant $privileges(UnmodifiableList<? extends Privilege> privileges);
        @NotNull  Grant $on(Table<?> on);
        @NotNull  Grant $to(Role to);
        @NotNull  Grant $toPublic(boolean toPublic);
        @NotNull  Grant $withGrantOption(boolean withGrantOption);
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
        @NotNull  UnmodifiableList<? extends Privilege> $privileges();
                  boolean $grantOptionFor();
        @NotNull  Table<?> $on();
        @Nullable Role $from();
                  boolean $fromPublic();
        @NotNull  Revoke $privileges(UnmodifiableList<? extends Privilege> privileges);
        @NotNull  Revoke $grantOptionFor(boolean grantOptionFor);
        @NotNull  Revoke $on(Table<?> on);
        @NotNull  Revoke $from(Role from);
        @NotNull  Revoke $fromPublic(boolean fromPublic);
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
        @NotNull  Name $name();
        @NotNull  Param<?> $value();
                  boolean $local();
        @NotNull  SetCommand $name(Name name);
        @NotNull  SetCommand $value(Param<?> value);
        @NotNull  SetCommand $local(boolean local);
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
        @NotNull  Catalog $catalog();
        @NotNull  SetCatalog $catalog(Catalog catalog);
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
        @NotNull  Schema $schema();
        @NotNull  SetSchema $schema(Schema schema);
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
        @NotNull  Table<R> $table();
        @Nullable IdentityRestartOption $restartIdentity();
        @Nullable Cascade $cascade();
        @NotNull  Truncate<R> $table(Table<R> table);
        @NotNull  Truncate<R> $restartIdentity(IdentityRestartOption restartIdentity);
        @NotNull  Truncate<R> $cascade(Cascade cascade);
    }





















    /**
     * The <code>AND</code> operator.
     */
    public /*sealed*/ interface And
        extends
            CombinedCondition
        //permits
        //    And
    {}

    /**
     * The <code>EQ</code> operator.
     */
    public /*sealed*/ interface TableEq<R extends Record>
        extends
            org.jooq.Condition,
            UOperator2<Table<R>, Table<R>, Condition>
        //permits
        //    TableEq
    {}

    /**
     * The <code>EQ</code> operator.
     */
    public /*sealed*/ interface Eq<T>
        extends
            CompareCondition<T>
        //permits
        //    Eq
    {}

    /**
     * The <code>EXISTS</code> function.
     */
    public /*sealed*/ interface Exists
        extends
            org.jooq.Condition
        //permits
        //    Exists
    {
        @NotNull  Select<?> $query();
        @NotNull  Exists $query(Select<?> query);
    }

    /**
     * The <code>GE</code> operator.
     */
    public /*sealed*/ interface Ge<T>
        extends
            CompareCondition<T>
        //permits
        //    Ge
    {}

    /**
     * The <code>GT</code> operator.
     */
    public /*sealed*/ interface Gt<T>
        extends
            CompareCondition<T>
        //permits
        //    Gt
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
            org.jooq.Condition,
            UOperator2<Field<T>, Select<? extends Record1<T>>, Condition>
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
            CompareCondition<T>
        //permits
        //    IsDistinctFrom
    {}

    /**
     * The <code>IS NULL</code> operator.
     */
    public /*sealed*/ interface IsNull
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsNull
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>IS NOT DISTINCT FROM</code> operator.
     * <p>
     * The NOT DISTINCT predicate allows for creating NULL safe comparisons where the two
     * operands are tested for equality
     */
    public /*sealed*/ interface IsNotDistinctFrom<T>
        extends
            CompareCondition<T>
        //permits
        //    IsNotDistinctFrom
    {}

    /**
     * The <code>IS NOT NULL</code> operator.
     */
    public /*sealed*/ interface IsNotNull
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsNotNull
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>LE</code> operator.
     */
    public /*sealed*/ interface Le<T>
        extends
            CompareCondition<T>
        //permits
        //    Le
    {}

    /**
     * The <code>LIKE</code> operator.
     */
    public /*sealed*/ interface Like
        extends
            Condition,
            UOperator3<Field<?>, Field<String>, Character, Condition>
        //permits
        //    Like
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
    public /*sealed*/ interface LikeIgnoreCase
        extends
            Condition,
            UOperator3<Field<?>, Field<String>, Character, Condition>
        //permits
        //    LikeIgnoreCase
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>LT</code> operator.
     */
    public /*sealed*/ interface Lt<T>
        extends
            CompareCondition<T>
        //permits
        //    Lt
    {}

    /**
     * The <code>NE</code> operator.
     */
    public /*sealed*/ interface TableNe<R extends Record>
        extends
            org.jooq.Condition,
            UOperator2<Table<R>, Table<R>, Condition>
        //permits
        //    TableNe
    {}

    /**
     * The <code>NE</code> operator.
     */
    public /*sealed*/ interface Ne<T>
        extends
            CompareCondition<T>
        //permits
        //    Ne
    {}

    /**
     * The <code>NOT</code> operator.
     */
    public /*sealed*/ interface Not
        extends
            org.jooq.Condition,
            UOperator1<Condition, Condition>
        //permits
        //    Not
    {
        @NotNull  default Condition $condition() { return $arg1(); }
    }

    /**
     * The <code>NOT</code> operator.
     */
    public /*sealed*/ interface NotField
        extends
            org.jooq.Field<Boolean>,
            UOperator1<Field<Boolean>, Field<Boolean>>
        //permits
        //    NotField
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
    public /*sealed*/ interface NotIn<T>
        extends
            org.jooq.Condition,
            UOperator2<Field<T>, Select<? extends Record1<T>>, Condition>
        //permits
        //    NotIn
    {}

    /**
     * The <code>NOT LIKE</code> operator.
     */
    public /*sealed*/ interface NotLike
        extends
            Condition,
            UOperator3<Field<?>, Field<String>, Character, Condition>
        //permits
        //    NotLike
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
    public /*sealed*/ interface NotLikeIgnoreCase
        extends
            Condition,
            UOperator3<Field<?>, Field<String>, Character, Condition>
        //permits
        //    NotLikeIgnoreCase
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>NOT SIMILAR TO</code> operator.
     */
    public /*sealed*/ interface NotSimilarTo
        extends
            Condition,
            UOperator3<Field<?>, Field<String>, Character, Condition>
        //permits
        //    NotSimilarTo
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>OR</code> operator.
     */
    public /*sealed*/ interface Or
        extends
            CombinedCondition
        //permits
        //    Or
    {}

    /**
     * The <code>SIMILAR TO</code> operator.
     */
    public /*sealed*/ interface SimilarTo
        extends
            Condition,
            UOperator3<Field<?>, Field<String>, Character, Condition>
        //permits
        //    SimilarTo
    {
        @NotNull  default Field<?> $value() { return $arg1(); }
        @NotNull  default Field<String> $pattern() { return $arg2(); }
        @Nullable default Character $escape() { return $arg3(); }
    }

    /**
     * The <code>UNIQUE</code> function.
     */
    public /*sealed*/ interface Unique
        extends
            org.jooq.Condition
        //permits
        //    Unique
    {
        @NotNull  Select<?> $query();
        @NotNull  Unique $query(Select<?> query);
    }

    /**
     * The <code>IS DOCUMENT</code> operator.
     * <p>
     * Create a condition to check if this field contains XML data.
     */
    public /*sealed*/ interface IsDocument
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsDocument
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>IS NOT DOCUMENT</code> operator.
     * <p>
     * Create a condition to check if this field does not contain XML data.
     */
    public /*sealed*/ interface IsNotDocument
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsNotDocument
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>IS JSON</code> operator.
     * <p>
     * Create a condition to check if this field contains JSON data.
     */
    public /*sealed*/ interface IsJson
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsJson
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    /**
     * The <code>IS NOT JSON</code> operator.
     * <p>
     * Create a condition to check if this field does not contain JSON data.
     */
    public /*sealed*/ interface IsNotJson
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsNotJson
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
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
            org.jooq.Field<RowId>,
            UOperator1<Table<?>, Field<RowId>>
        //permits
        //    QualifiedRowid
    {
        @NotNull  default Table<?> $table() { return $arg1(); }
    }

    /**
     * The <code>ABS</code> function.
     */
    public /*sealed*/ interface Abs<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Abs
    {
        @NotNull  Field<T> $number();
        @NotNull  Abs<T> $number(Field<T> number);
    }

    /**
     * The <code>ACOS</code> function.
     */
    public /*sealed*/ interface Acos
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Acos
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Acos $number(Field<? extends Number> number);
    }

    /**
     * The <code>ADD</code> operator.
     */
    public /*sealed*/ interface Add<T>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    Add
    {}

    /**
     * The <code>ASIN</code> function.
     */
    public /*sealed*/ interface Asin
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Asin
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Asin $number(Field<? extends Number> number);
    }

    /**
     * The <code>ATAN</code> function.
     */
    public /*sealed*/ interface Atan
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Atan
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Atan $number(Field<? extends Number> number);
    }

    /**
     * The <code>ATAN2</code> function.
     */
    public /*sealed*/ interface Atan2
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Atan2
    {
        @NotNull  Field<? extends Number> $x();
        @NotNull  Field<? extends Number> $y();
        @NotNull  Atan2 $x(Field<? extends Number> x);
        @NotNull  Atan2 $y(Field<? extends Number> y);
    }

    /**
     * The <code>BIT AND</code> operator.
     */
    public /*sealed*/ interface BitAnd<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
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
            org.jooq.Field<Integer>
        //permits
        //    BitCount
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  BitCount $number(Field<? extends Number> number);
    }

    /**
     * The <code>BIT NAND</code> operator.
     */
    public /*sealed*/ interface BitNand<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitNand
    {}

    /**
     * The <code>BIT NOR</code> operator.
     */
    public /*sealed*/ interface BitNor<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitNor
    {}

    /**
     * The <code>BIT NOT</code> operator.
     */
    public /*sealed*/ interface BitNot<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator1<Field<T>, Field<T>>
        //permits
        //    BitNot
    {}

    /**
     * The <code>BIT OR</code> operator.
     */
    public /*sealed*/ interface BitOr<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitOr
    {}

    /**
     * The <code>BIT X NOR</code> operator.
     */
    public /*sealed*/ interface BitXNor<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitXNor
    {}

    /**
     * The <code>BIT XOR</code> operator.
     */
    public /*sealed*/ interface BitXor<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitXor
    {}

    /**
     * The <code>CEIL</code> function.
     * <p>
     * Get the smallest integer value equal or greater to a value.
     */
    public /*sealed*/ interface Ceil<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Ceil
    {
        @NotNull  Field<T> $value();
        @NotNull  Ceil<T> $value(Field<T> value);
    }

    /**
     * The <code>COS</code> function.
     */
    public /*sealed*/ interface Cos
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Cos
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Cos $number(Field<? extends Number> number);
    }

    /**
     * The <code>COSH</code> function.
     */
    public /*sealed*/ interface Cosh
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Cosh
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Cosh $number(Field<? extends Number> number);
    }

    /**
     * The <code>COT</code> function.
     */
    public /*sealed*/ interface Cot
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Cot
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Cot $number(Field<? extends Number> number);
    }

    /**
     * The <code>COTH</code> function.
     */
    public /*sealed*/ interface Coth
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Coth
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Coth $number(Field<? extends Number> number);
    }

    /**
     * The <code>DEGREES</code> function.
     * <p>
     * Turn a value in radians to degrees.
     */
    public /*sealed*/ interface Degrees
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Degrees
    {

        /**
         * The value in radians.
         */
        @NotNull  Field<? extends Number> $radians();

        /**
         * The value in radians.
         */
        @NotNull  Degrees $radians(Field<? extends Number> radians);
    }

    /**
     * The <code>DIV</code> operator.
     */
    public /*sealed*/ interface Div<T>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
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
            org.jooq.Field<BigDecimal>,
            UEmpty
        //permits
        //    Euler
    {}

    /**
     * The <code>EXP</code> function.
     */
    public /*sealed*/ interface Exp
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Exp
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  Exp $value(Field<? extends Number> value);
    }

    /**
     * The <code>FLOOR</code> function.
     * <p>
     * Get the biggest integer value equal or less than a value.
     */
    public /*sealed*/ interface Floor<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Floor
    {
        @NotNull  Field<T> $value();
        @NotNull  Floor<T> $value(Field<T> value);
    }

    /**
     * The <code>LN</code> function.
     */
    public /*sealed*/ interface Log
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Log
    {
        @NotNull  Field<? extends Number> $value();
        @Nullable Field<? extends Number> $base();
        @NotNull  Log $value(Field<? extends Number> value);
        @NotNull  Log $base(Field<? extends Number> base);
    }

    /**
     * The <code>LOG10</code> function.
     */
    public /*sealed*/ interface Log10
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Log10
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  Log10 $value(Field<? extends Number> value);
    }

    /**
     * The <code>MOD</code> operator.
     */
    public /*sealed*/ interface Mod<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<? extends Number>, Field<T>>
        //permits
        //    Mod
    {
        @NotNull  default Field<T> $dividend() { return $arg1(); }
        @NotNull  default Field<? extends Number> $divisor() { return $arg2(); }
    }

    /**
     * The <code>MUL</code> operator.
     */
    public /*sealed*/ interface Mul<T>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
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
            org.jooq.Field<BigDecimal>,
            UEmpty
        //permits
        //    Pi
    {}

    /**
     * The <code>POWER</code> operator.
     */
    public /*sealed*/ interface Power
        extends
            org.jooq.Field<BigDecimal>,
            UOperator2<Field<? extends Number>, Field<? extends Number>, Field<BigDecimal>>
        //permits
        //    Power
    {
        @NotNull  default Field<? extends Number> $base() { return $arg1(); }
        @NotNull  default Field<? extends Number> $exponent() { return $arg2(); }
    }

    /**
     * The <code>RADIANS</code> function.
     * <p>
     * Turn a value in degrees to radians.
     */
    public /*sealed*/ interface Radians
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Radians
    {

        /**
         * The value in degrees.
         */
        @NotNull  Field<? extends Number> $degrees();

        /**
         * The value in degrees.
         */
        @NotNull  Radians $degrees(Field<? extends Number> degrees);
    }

    /**
     * The <code>RAND</code> function.
     * <p>
     * Get a random numeric value.
     */
    public /*sealed*/ interface Rand
        extends
            org.jooq.Field<BigDecimal>,
            UEmpty
        //permits
        //    Rand
    {}

    /**
     * The <code>ROUND</code> function.
     * <p>
     * Round a numeric value to the nearest decimal precision.
     */
    public /*sealed*/ interface Round<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Round
    {

        /**
         * The number to be rounded.
         */
        @NotNull  Field<T> $value();

        /**
         * The decimals to round to.
         */
        @Nullable Field<Integer> $decimals();

        /**
         * The number to be rounded.
         */
        @NotNull  Round<T> $value(Field<T> value);

        /**
         * The decimals to round to.
         */
        @NotNull  Round<T> $decimals(Field<Integer> decimals);
    }

    /**
     * The <code>SHL</code> operator.
     * <p>
     * Left shift all bits in a number
     */
    public /*sealed*/ interface Shl<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<? extends Number>, Field<T>>
        //permits
        //    Shl
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
    public /*sealed*/ interface Shr<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<? extends Number>, Field<T>>
        //permits
        //    Shr
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
    public /*sealed*/ interface Sign
        extends
            org.jooq.Field<Integer>
        //permits
        //    Sign
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Sign $number(Field<? extends Number> number);
    }

    /**
     * The <code>SIN</code> function.
     */
    public /*sealed*/ interface Sin
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Sin
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Sin $number(Field<? extends Number> number);
    }

    /**
     * The <code>SINH</code> function.
     */
    public /*sealed*/ interface Sinh
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Sinh
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Sinh $number(Field<? extends Number> number);
    }

    /**
     * The <code>SQRT</code> function.
     */
    public /*sealed*/ interface Sqrt
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Sqrt
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  Sqrt $value(Field<? extends Number> value);
    }

    /**
     * The <code>SQUARE</code> function.
     */
    public /*sealed*/ interface Square<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Square
    {
        @NotNull  Field<T> $value();
        @NotNull  Square<T> $value(Field<T> value);
    }

    /**
     * The <code>SUB</code> operator.
     */
    public /*sealed*/ interface Sub<T>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    Sub
    {}

    /**
     * The <code>TAN</code> function.
     */
    public /*sealed*/ interface Tan
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Tan
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Tan $number(Field<? extends Number> number);
    }

    /**
     * The <code>TANH</code> function.
     */
    public /*sealed*/ interface Tanh
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Tanh
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Tanh $number(Field<? extends Number> number);
    }

    /**
     * The <code>TAU</code> function.
     * <p>
     * The τ literal, or π, in a better world.
     */
    public /*sealed*/ interface Tau
        extends
            org.jooq.Field<BigDecimal>,
            UEmpty
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
            org.jooq.Field<T>
        //permits
        //    Trunc
    {

        /**
         * The number to be truncated
         */
        @NotNull  Field<T> $value();

        /**
         * The decimals to truncate to.
         */
        @NotNull  Field<Integer> $decimals();

        /**
         * The number to be truncated
         */
        @NotNull  Trunc<T> $value(Field<T> value);

        /**
         * The decimals to truncate to.
         */
        @NotNull  Trunc<T> $decimals(Field<Integer> decimals);
    }

    /**
     * The <code>WIDTH BUCKET</code> function.
     * <p>
     * Divide a range into buckets of equal size.
     */
    public /*sealed*/ interface WidthBucket<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    WidthBucket
    {

        /**
         * The value to divide into the range.
         */
        @NotNull  Field<T> $field();

        /**
         * The lower bound of the range.
         */
        @NotNull  Field<T> $low();

        /**
         * The upper bound of the range.
         */
        @NotNull  Field<T> $high();

        /**
         * The number of buckets to produce.
         */
        @NotNull  Field<Integer> $buckets();

        /**
         * The value to divide into the range.
         */
        @NotNull  WidthBucket<T> $field(Field<T> field);

        /**
         * The lower bound of the range.
         */
        @NotNull  WidthBucket<T> $low(Field<T> low);

        /**
         * The upper bound of the range.
         */
        @NotNull  WidthBucket<T> $high(Field<T> high);

        /**
         * The number of buckets to produce.
         */
        @NotNull  WidthBucket<T> $buckets(Field<Integer> buckets);
    }

    /**
     * The <code>ASCII</code> function.
     * <p>
     * The ASCII value of a character.
     */
    public /*sealed*/ interface Ascii
        extends
            org.jooq.Field<Integer>
        //permits
        //    Ascii
    {
        @NotNull  Field<String> $string();
        @NotNull  Ascii $string(Field<String> string);
    }

    /**
     * The <code>BIT LENGTH</code> function.
     * <p>
     * The length of a string in bits.
     */
    public /*sealed*/ interface BitLength
        extends
            org.jooq.Field<Integer>
        //permits
        //    BitLength
    {
        @NotNull  Field<String> $string();
        @NotNull  BitLength $string(Field<String> string);
    }

    /**
     * The <code>CHAR LENGTH</code> function.
     * <p>
     * The length of a string in characters.
     */
    public /*sealed*/ interface CharLength
        extends
            org.jooq.Field<Integer>
        //permits
        //    CharLength
    {
        @NotNull  Field<String> $string();
        @NotNull  CharLength $string(Field<String> string);
    }

    /**
     * The <code>CHR</code> function.
     */
    public /*sealed*/ interface Chr
        extends
            org.jooq.Field<String>
        //permits
        //    Chr
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Chr $number(Field<? extends Number> number);
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
    public /*sealed*/ interface Contains<T>
        extends
            CompareCondition<T>
        //permits
        //    Contains
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
    public /*sealed*/ interface ContainsIgnoreCase<T>
        extends
            CompareCondition<T>
        //permits
        //    ContainsIgnoreCase
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
        @NotNull  default Field<T> $content() { return $arg2(); }
    }

    /**
     * The <code>DIGITS</code> function.
     */
    public /*sealed*/ interface Digits
        extends
            org.jooq.Field<String>
        //permits
        //    Digits
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  Digits $value(Field<? extends Number> value);
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
    public /*sealed*/ interface EndsWith<T>
        extends
            CompareCondition<T>
        //permits
        //    EndsWith
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
    public /*sealed*/ interface EndsWithIgnoreCase<T>
        extends
            CompareCondition<T>
        //permits
        //    EndsWithIgnoreCase
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $suffix() { return $arg2(); }
    }

    /**
     * The <code>LEFT</code> function.
     * <p>
     * Get the left outermost characters from a string.
     */
    public /*sealed*/ interface Left
        extends
            org.jooq.Field<String>
        //permits
        //    Left
    {

        /**
         * The string whose characters are extracted.
         */
        @NotNull  Field<String> $string();

        /**
         * The number of characters to extract from the string.
         */
        @NotNull  Field<? extends Number> $length();

        /**
         * The string whose characters are extracted.
         */
        @NotNull  Left $string(Field<String> string);

        /**
         * The number of characters to extract from the string.
         */
        @NotNull  Left $length(Field<? extends Number> length);
    }

    /**
     * The <code>LOWER</code> function.
     * <p>
     * Turn a string into lower case.
     */
    public /*sealed*/ interface Lower
        extends
            org.jooq.Field<String>
        //permits
        //    Lower
    {
        @NotNull  Field<String> $string();
        @NotNull  Lower $string(Field<String> string);
    }

    /**
     * The <code>LPAD</code> function.
     * <p>
     * Left-pad a string with a character (whitespace as default) for a number of times.
     */
    public /*sealed*/ interface Lpad
        extends
            org.jooq.Field<String>
        //permits
        //    Lpad
    {

        /**
         * The string to be padded.
         */
        @NotNull  Field<String> $string();

        /**
         * The maximum length to pad the string to.
         */
        @NotNull  Field<? extends Number> $length();

        /**
         * The padding character, if different from whitespace
         */
        @Nullable Field<String> $character();

        /**
         * The string to be padded.
         */
        @NotNull  Lpad $string(Field<String> string);

        /**
         * The maximum length to pad the string to.
         */
        @NotNull  Lpad $length(Field<? extends Number> length);

        /**
         * The padding character, if different from whitespace
         */
        @NotNull  Lpad $character(Field<String> character);
    }

    /**
     * The <code>LTRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from both sides of a string.
     */
    public /*sealed*/ interface Ltrim
        extends
            org.jooq.Field<String>
        //permits
        //    Ltrim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull  Field<String> $string();

        /**
         * The characters to be trimmed.
         */
        @Nullable Field<String> $characters();

        /**
         * The string to be trimmed.
         */
        @NotNull  Ltrim $string(Field<String> string);

        /**
         * The characters to be trimmed.
         */
        @NotNull  Ltrim $characters(Field<String> characters);
    }

    /**
     * The <code>MD5</code> function.
     * <p>
     * Calculate an MD5 hash from a string.
     */
    public /*sealed*/ interface Md5
        extends
            org.jooq.Field<String>
        //permits
        //    Md5
    {
        @NotNull  Field<String> $string();
        @NotNull  Md5 $string(Field<String> string);
    }

    /**
     * The <code>OCTET LENGTH</code> function.
     * <p>
     * The length of a string in octets.
     */
    public /*sealed*/ interface OctetLength
        extends
            org.jooq.Field<Integer>
        //permits
        //    OctetLength
    {
        @NotNull  Field<String> $string();
        @NotNull  OctetLength $string(Field<String> string);
    }

    /**
     * The <code>OVERLAY</code> function.
     * <p>
     * Place a string on top of another string, replacing the original contents.
     */
    public /*sealed*/ interface Overlay
        extends
            org.jooq.Field<String>
        //permits
        //    Overlay
    {

        /**
         * The original string on top of which the overlay is placed.
         */
        @NotNull  Field<String> $in();

        /**
         * The string that is being placed on top of the other string.
         */
        @NotNull  Field<String> $placing();

        /**
         * The start index (1-based) starting from where the overlay is placed.
         */
        @NotNull  Field<? extends Number> $startIndex();

        /**
         * The length in the original string that will be replaced, if different from the overlay length.
         */
        @Nullable Field<? extends Number> $length();

        /**
         * The original string on top of which the overlay is placed.
         */
        @NotNull  Overlay $in(Field<String> in);

        /**
         * The string that is being placed on top of the other string.
         */
        @NotNull  Overlay $placing(Field<String> placing);

        /**
         * The start index (1-based) starting from where the overlay is placed.
         */
        @NotNull  Overlay $startIndex(Field<? extends Number> startIndex);

        /**
         * The length in the original string that will be replaced, if different from the overlay length.
         */
        @NotNull  Overlay $length(Field<? extends Number> length);
    }

    /**
     * The <code>POSITION</code> function.
     * <p>
     * Search the position (1-based) of a substring in another string.
     */
    public /*sealed*/ interface Position
        extends
            org.jooq.Field<Integer>
        //permits
        //    Position
    {

        /**
         * The string in which to search the substring.
         */
        @NotNull  Field<String> $in();

        /**
         * The substring to search for.
         */
        @NotNull  Field<String> $search();

        /**
         * The start index (1-based) from which to start looking for the substring.
         */
        @Nullable Field<? extends Number> $startIndex();

        /**
         * The string in which to search the substring.
         */
        @NotNull  Position $in(Field<String> in);

        /**
         * The substring to search for.
         */
        @NotNull  Position $search(Field<String> search);

        /**
         * The start index (1-based) from which to start looking for the substring.
         */
        @NotNull  Position $startIndex(Field<? extends Number> startIndex);
    }

    /**
     * The <code>REPEAT</code> function.
     * <p>
     * Repeat a string a number of times.
     */
    public /*sealed*/ interface Repeat
        extends
            org.jooq.Field<String>
        //permits
        //    Repeat
    {

        /**
         * The string to be repeated.
         */
        @NotNull  Field<String> $string();

        /**
         * The number of times to repeat the string.
         */
        @NotNull  Field<? extends Number> $count();

        /**
         * The string to be repeated.
         */
        @NotNull  Repeat $string(Field<String> string);

        /**
         * The number of times to repeat the string.
         */
        @NotNull  Repeat $count(Field<? extends Number> count);
    }

    /**
     * The <code>REPLACE</code> function.
     * <p>
     * Replace all occurrences of a substring in another string.
     */
    public /*sealed*/ interface Replace
        extends
            org.jooq.Field<String>
        //permits
        //    Replace
    {

        /**
         * The string in which to replace contents.
         */
        @NotNull  Field<String> $string();

        /**
         * The substring to search for.
         */
        @NotNull  Field<String> $search();

        /**
         * The replacement for each substring, if not empty.
         */
        @Nullable Field<String> $replace();

        /**
         * The string in which to replace contents.
         */
        @NotNull  Replace $string(Field<String> string);

        /**
         * The substring to search for.
         */
        @NotNull  Replace $search(Field<String> search);

        /**
         * The replacement for each substring, if not empty.
         */
        @NotNull  Replace $replace(Field<String> replace);
    }

    /**
     * The <code>REVERSE</code> function.
     * <p>
     * Reverse a string.
     */
    public /*sealed*/ interface Reverse
        extends
            org.jooq.Field<String>
        //permits
        //    Reverse
    {
        @NotNull  Field<String> $string();
        @NotNull  Reverse $string(Field<String> string);
    }

    /**
     * The <code>RIGHT</code> function.
     * <p>
     * Get the right outermost characters from a string.
     */
    public /*sealed*/ interface Right
        extends
            org.jooq.Field<String>
        //permits
        //    Right
    {

        /**
         * The string whose characters are extracted.
         */
        @NotNull  Field<String> $string();

        /**
         * The number of characters to extract from the string.
         */
        @NotNull  Field<? extends Number> $length();

        /**
         * The string whose characters are extracted.
         */
        @NotNull  Right $string(Field<String> string);

        /**
         * The number of characters to extract from the string.
         */
        @NotNull  Right $length(Field<? extends Number> length);
    }

    /**
     * The <code>RPAD</code> function.
     * <p>
     * Right-pad a string with a character (whitespace as default) for a number of times.
     */
    public /*sealed*/ interface Rpad
        extends
            org.jooq.Field<String>
        //permits
        //    Rpad
    {

        /**
         * The string to be padded.
         */
        @NotNull  Field<String> $string();

        /**
         * The maximum length to pad the string to.
         */
        @NotNull  Field<? extends Number> $length();

        /**
         * The padding character, if different from whitespace
         */
        @Nullable Field<String> $character();

        /**
         * The string to be padded.
         */
        @NotNull  Rpad $string(Field<String> string);

        /**
         * The maximum length to pad the string to.
         */
        @NotNull  Rpad $length(Field<? extends Number> length);

        /**
         * The padding character, if different from whitespace
         */
        @NotNull  Rpad $character(Field<String> character);
    }

    /**
     * The <code>RTRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from both sides of a string.
     */
    public /*sealed*/ interface Rtrim
        extends
            org.jooq.Field<String>
        //permits
        //    Rtrim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull  Field<String> $string();

        /**
         * The characters to be trimmed.
         */
        @Nullable Field<String> $characters();

        /**
         * The string to be trimmed.
         */
        @NotNull  Rtrim $string(Field<String> string);

        /**
         * The characters to be trimmed.
         */
        @NotNull  Rtrim $characters(Field<String> characters);
    }

    /**
     * The <code>SPACE</code> function.
     * <p>
     * Get a string of spaces of a given length.
     */
    public /*sealed*/ interface Space
        extends
            org.jooq.Field<String>
        //permits
        //    Space
    {

        /**
         * The number of spaces to produce.
         */
        @NotNull  Field<? extends Number> $count();

        /**
         * The number of spaces to produce.
         */
        @NotNull  Space $count(Field<? extends Number> count);
    }

    /**
     * The <code>SPLIT PART</code> function.
     * <p>
     * Split a string into tokens, and retrieve the nth token.
     */
    public /*sealed*/ interface SplitPart
        extends
            org.jooq.Field<String>
        //permits
        //    SplitPart
    {

        /**
         * The string to be split into parts.
         */
        @NotNull  Field<String> $string();

        /**
         * The delimiter used for splitting.
         */
        @NotNull  Field<String> $delimiter();

        /**
         * The token number (1-based).
         */
        @NotNull  Field<? extends Number> $n();

        /**
         * The string to be split into parts.
         */
        @NotNull  SplitPart $string(Field<String> string);

        /**
         * The delimiter used for splitting.
         */
        @NotNull  SplitPart $delimiter(Field<String> delimiter);

        /**
         * The token number (1-based).
         */
        @NotNull  SplitPart $n(Field<? extends Number> n);
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
    public /*sealed*/ interface StartsWith<T>
        extends
            CompareCondition<T>
        //permits
        //    StartsWith
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
    public /*sealed*/ interface StartsWithIgnoreCase<T>
        extends
            CompareCondition<T>
        //permits
        //    StartsWithIgnoreCase
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $prefix() { return $arg2(); }
    }

    /**
     * The <code>SUBSTRING</code> function.
     * <p>
     * Get a substring of a string, from a given position.
     */
    public /*sealed*/ interface Substring
        extends
            org.jooq.Field<String>
        //permits
        //    Substring
    {

        /**
         * The string from which to get the substring.
         */
        @NotNull  Field<String> $string();

        /**
         * The position (1-based) from which to get the substring.
         */
        @NotNull  Field<? extends Number> $startingPosition();

        /**
         * The maximum length of the substring.
         */
        @Nullable Field<? extends Number> $length();

        /**
         * The string from which to get the substring.
         */
        @NotNull  Substring $string(Field<String> string);

        /**
         * The position (1-based) from which to get the substring.
         */
        @NotNull  Substring $startingPosition(Field<? extends Number> startingPosition);

        /**
         * The maximum length of the substring.
         */
        @NotNull  Substring $length(Field<? extends Number> length);
    }

    /**
     * The <code>SUBSTRING INDEX</code> function.
     * <p>
     * Get a substring of a string, from the beginning until the nth occurrence of a substring.
     */
    public /*sealed*/ interface SubstringIndex
        extends
            org.jooq.Field<String>
        //permits
        //    SubstringIndex
    {

        /**
         * The string from which to get the substring.
         */
        @NotNull  Field<String> $string();

        /**
         * The delimiter.
         */
        @NotNull  Field<String> $delimiter();

        /**
         * The number of occurrences of the delimiter.
         */
        @NotNull  Field<? extends Number> $n();

        /**
         * The string from which to get the substring.
         */
        @NotNull  SubstringIndex $string(Field<String> string);

        /**
         * The delimiter.
         */
        @NotNull  SubstringIndex $delimiter(Field<String> delimiter);

        /**
         * The number of occurrences of the delimiter.
         */
        @NotNull  SubstringIndex $n(Field<? extends Number> n);
    }

    /**
     * The <code>TO CHAR</code> function.
     * <p>
     * Format an arbitrary value as a string.
     */
    public /*sealed*/ interface ToChar
        extends
            org.jooq.Field<String>
        //permits
        //    ToChar
    {

        /**
         * The value to be formatted.
         */
        @NotNull  Field<?> $value();

        /**
         * The vendor-specific formatting string.
         */
        @Nullable Field<String> $formatMask();

        /**
         * The value to be formatted.
         */
        @NotNull  ToChar $value(Field<?> value);

        /**
         * The vendor-specific formatting string.
         */
        @NotNull  ToChar $formatMask(Field<String> formatMask);
    }

    /**
     * The <code>TO DATE</code> function.
     * <p>
     * Parse a string-formatted date value to a date.
     */
    public /*sealed*/ interface ToDate
        extends
            org.jooq.Field<Date>
        //permits
        //    ToDate
    {

        /**
         * The formatted DATE value.
         */
        @NotNull  Field<String> $value();

        /**
         * The vendor-specific formatting string.
         */
        @NotNull  Field<String> $formatMask();

        /**
         * The formatted DATE value.
         */
        @NotNull  ToDate $value(Field<String> value);

        /**
         * The vendor-specific formatting string.
         */
        @NotNull  ToDate $formatMask(Field<String> formatMask);
    }

    /**
     * The <code>TO HEX</code> function.
     * <p>
     * Format a number to its hex value.
     */
    public /*sealed*/ interface ToHex
        extends
            org.jooq.Field<String>
        //permits
        //    ToHex
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  ToHex $value(Field<? extends Number> value);
    }

    /**
     * The <code>TO TIMESTAMP</code> function.
     * <p>
     * Parse a string-formatted timestamp value to a timestamp.
     */
    public /*sealed*/ interface ToTimestamp
        extends
            org.jooq.Field<Timestamp>
        //permits
        //    ToTimestamp
    {

        /**
         * The formatted TIMESTAMP value.
         */
        @NotNull  Field<String> $value();

        /**
         * The vendor-specific formatting string.
         */
        @NotNull  Field<String> $formatMask();

        /**
         * The formatted TIMESTAMP value.
         */
        @NotNull  ToTimestamp $value(Field<String> value);

        /**
         * The vendor-specific formatting string.
         */
        @NotNull  ToTimestamp $formatMask(Field<String> formatMask);
    }

    /**
     * The <code>TRANSLATE</code> function.
     * <p>
     * Translate a set of characters to another set of characters in a string.
     */
    public /*sealed*/ interface Translate
        extends
            org.jooq.Field<String>
        //permits
        //    Translate
    {

        /**
         * The string to translate.
         */
        @NotNull  Field<String> $string();

        /**
         * The set of source characters.
         */
        @NotNull  Field<String> $from();

        /**
         * The set of target characters, matched with source characters by position.
         */
        @NotNull  Field<String> $to();

        /**
         * The string to translate.
         */
        @NotNull  Translate $string(Field<String> string);

        /**
         * The set of source characters.
         */
        @NotNull  Translate $from(Field<String> from);

        /**
         * The set of target characters, matched with source characters by position.
         */
        @NotNull  Translate $to(Field<String> to);
    }

    /**
     * The <code>TRIM</code> function.
     * <p>
     * Trim characters (whitespace as default) from both sides of a string.
     */
    public /*sealed*/ interface Trim
        extends
            org.jooq.Field<String>
        //permits
        //    Trim
    {

        /**
         * The string to be trimmed.
         */
        @NotNull  Field<String> $string();

        /**
         * The characters to be trimmed.
         */
        @Nullable Field<String> $characters();

        /**
         * The string to be trimmed.
         */
        @NotNull  Trim $string(Field<String> string);

        /**
         * The characters to be trimmed.
         */
        @NotNull  Trim $characters(Field<String> characters);
    }

    /**
     * The <code>UPPER</code> function.
     * <p>
     * Turn a string into upper case.
     */
    public /*sealed*/ interface Upper
        extends
            org.jooq.Field<String>
        //permits
        //    Upper
    {
        @NotNull  Field<String> $string();
        @NotNull  Upper $string(Field<String> string);
    }

    /**
     * The <code>UUID</code> function.
     * <p>
     * Generate a random UUID.
     */
    public /*sealed*/ interface Uuid
        extends
            org.jooq.Field<UUID>,
            UEmpty
        //permits
        //    Uuid
    {}

    /**
     * The <code>DATE ADD</code> function.
     * <p>
     * Add an interval to a date.
     */
    public /*sealed*/ interface DateAdd<T>
        extends
            org.jooq.Field<T>
        //permits
        //    DateAdd
    {

        /**
         * The date to add an interval to
         */
        @NotNull  Field<T> $date();

        /**
         * The interval to add to the date
         */
        @NotNull  Field<? extends Number> $interval();

        /**
         * The date part describing the interval
         */
        @Nullable DatePart $datePart();

        /**
         * The date to add an interval to
         */
        @NotNull  DateAdd<T> $date(Field<T> date);

        /**
         * The interval to add to the date
         */
        @NotNull  DateAdd<T> $interval(Field<? extends Number> interval);

        /**
         * The date part describing the interval
         */
        @NotNull  DateAdd<T> $datePart(DatePart datePart);
    }

    /**
     * The <code>CARDINALITY</code> function.
     * <p>
     * Calculate the cardinality of an array field.
     */
    public /*sealed*/ interface Cardinality
        extends
            org.jooq.Field<Integer>
        //permits
        //    Cardinality
    {
        @NotNull  Field<? extends Object[]> $array();
        @NotNull  Cardinality $array(Field<? extends Object[]> array);
    }

    /**
     * The <code>ARRAY GET</code> function.
     * <p>
     * Get an array element at a given index (1 based).
     */
    public /*sealed*/ interface ArrayGet<T>
        extends
            org.jooq.Field<T>
        //permits
        //    ArrayGet
    {
        @NotNull  Field<T[]> $array();
        @NotNull  Field<Integer> $index();
        @NotNull  ArrayGet<T> $array(Field<T[]> array);
        @NotNull  ArrayGet<T> $index(Field<Integer> index);
    }

    /**
     * The <code>NVL</code> function.
     * <p>
     * Return the first non-null argument.
     */
    public /*sealed*/ interface Nvl<T>
        extends
            org.jooq.Field<T>
        //permits
        //    Nvl
    {

        /**
         * The nullable value.
         */
        @NotNull  Field<T> $value();

        /**
         * The default value if the other value is null.
         */
        @NotNull  Field<T> $defaultValue();

        /**
         * The nullable value.
         */
        @NotNull  Nvl<T> $value(Field<T> value);

        /**
         * The default value if the other value is null.
         */
        @NotNull  Nvl<T> $defaultValue(Field<T> defaultValue);
    }

    /**
     * The <code>NULLIF</code> function.
     */
    public /*sealed*/ interface Nullif<T>
        extends
            org.jooq.Field<T>
        //permits
        //    Nullif
    {

        /**
         * The result value if the other value is not equal.
         */
        @NotNull  Field<T> $value();

        /**
         * The value to compare the result value with.
         */
        @NotNull  Field<T> $other();

        /**
         * The result value if the other value is not equal.
         */
        @NotNull  Nullif<T> $value(Field<T> value);

        /**
         * The value to compare the result value with.
         */
        @NotNull  Nullif<T> $other(Field<T> other);
    }

    /**
     * The <code>CURRENT CATALOG</code> function.
     */
    public /*sealed*/ interface CurrentCatalog
        extends
            org.jooq.Field<String>,
            UEmpty
        //permits
        //    CurrentCatalog
    {}

    /**
     * The <code>CURRENT SCHEMA</code> function.
     */
    public /*sealed*/ interface CurrentSchema
        extends
            org.jooq.Field<String>,
            UEmpty
        //permits
        //    CurrentSchema
    {}

    /**
     * The <code>CURRENT USER</code> function.
     */
    public /*sealed*/ interface CurrentUser
        extends
            org.jooq.Field<String>,
            UEmpty
        //permits
        //    CurrentUser
    {}














































































































































    /**
     * The <code>XMLCOMMENT</code> function.
     */
    public /*sealed*/ interface XMLComment
        extends
            org.jooq.Field<XML>
        //permits
        //    XMLComment
    {
        @NotNull  Field<String> $comment();
        @NotNull  XMLComment $comment(Field<String> comment);
    }

    /**
     * The <code>XMLCONCAT</code> function.
     */
    public /*sealed*/ interface XMLConcat
        extends
            org.jooq.Field<XML>
        //permits
        //    XMLConcat
    {
        @NotNull  UnmodifiableList<? extends Field<?>> $args();
        @NotNull  XMLConcat $args(UnmodifiableList<? extends Field<?>> args);
    }

















    /**
     * The <code>XMLFOREST</code> function.
     */
    public /*sealed*/ interface XMLForest
        extends
            org.jooq.Field<XML>
        //permits
        //    XMLForest
    {
        @NotNull  UnmodifiableList<? extends Field<?>> $fields();
        @NotNull  XMLForest $fields(UnmodifiableList<? extends Field<?>> fields);
    }

    /**
     * The <code>XMLPI</code> function.
     */
    public /*sealed*/ interface XMLPi
        extends
            org.jooq.Field<XML>
        //permits
        //    XMLPi
    {
        @NotNull  Name $target();
        @Nullable Field<?> $content();
        @NotNull  XMLPi $target(Name target);
        @NotNull  XMLPi $content(Field<?> content);
    }

    /**
     * The <code>XMLSERIALIZE</code> function.
     */
    public /*sealed*/ interface XMLSerialize<T>
        extends
            org.jooq.Field<T>
        //permits
        //    XMLSerialize
    {
                  boolean $content();
        @NotNull  Field<XML> $value();
        @NotNull  DataType<T> $type();
        @NotNull  XMLSerialize<T> $content(boolean content);
        @NotNull  XMLSerialize<T> $value(Field<XML> value);
        @NotNull  XMLSerialize<T> $type(DataType<T> type);
    }

    /**
     * The <code>JSON ARRAY</code> function.
     */
    public /*sealed*/ interface JSONArray<T>
        extends
            Field<T>
        //permits
        //    JSONArray
    {
        @NotNull  DataType<T> $type();
        @NotNull  UnmodifiableList<? extends Field<?>> $fields();
        @Nullable JSONOnNull $onNull();
        @Nullable DataType<?> $returning();
        @NotNull  JSONArray<T> $type(DataType<T> type);
        @NotNull  JSONArray<T> $fields(UnmodifiableList<? extends Field<?>> fields);
        @NotNull  JSONArray<T> $onNull(JSONOnNull onNull);
        @NotNull  JSONArray<T> $returning(DataType<?> returning);
    }

    /**
     * The <code>JSON OBJECT</code> function.
     */
    public /*sealed*/ interface JSONObject<T>
        extends
            Field<T>
        //permits
        //    JSONObject
    {
        @NotNull  DataType<T> $type();
        @NotNull  UnmodifiableList<? extends JSONEntry<?>> $entries();
        @Nullable JSONOnNull $onNull();
        @Nullable DataType<?> $returning();
        @NotNull  JSONObject<T> $type(DataType<T> type);
        @NotNull  JSONObject<T> $entries(UnmodifiableList<? extends JSONEntry<?>> entries);
        @NotNull  JSONObject<T> $onNull(JSONOnNull onNull);
        @NotNull  JSONObject<T> $returning(DataType<?> returning);
    }







































    /**
     * The <code>FIELD</code> function.
     * <p>
     * Wrap a condition in a boolean field.
     */
    public /*sealed*/ interface ConditionAsField
        extends
            org.jooq.Field<Boolean>
        //permits
        //    ConditionAsField
    {
        @NotNull  Condition $condition();
        @NotNull  ConditionAsField $condition(Condition condition);
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
            org.jooq.Condition
        //permits
        //    FieldCondition
    {
        @NotNull  Field<Boolean> $field();
        @NotNull  FieldCondition $field(Field<Boolean> field);
    }

    /**
     * The <code>ANY VALUE</code> function.
     * <p>
     * Get any arbitrary value from the group.
     */
    public /*sealed*/ interface AnyValue<T>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    AnyValue
    {
        @NotNull  Field<T> $field();
        @NotNull  AnyValue<T> $field(Field<T> field);
    }

    /**
     * The <code>AVG</code> function.
     */
    public /*sealed*/ interface Avg
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    Avg
    {
        @NotNull  Field<? extends Number> $field();
                  boolean $distinct();
        @NotNull  Avg $field(Field<? extends Number> field);
        @NotNull  Avg $distinct(boolean distinct);
    }

    /**
     * The <code>BIT AND AGG</code> function.
     * <p>
     * Calculate the bitwise <code>AND</code> aggregate value.
     */
    public /*sealed*/ interface BitAndAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    BitAndAgg
    {
        @NotNull  Field<T> $value();
        @NotNull  BitAndAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT OR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>OR</code> aggregate value.
     */
    public /*sealed*/ interface BitOrAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    BitOrAgg
    {
        @NotNull  Field<T> $value();
        @NotNull  BitOrAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BIT XOR AGG</code> function.
     * <p>
     * Calculate the bitwise <code>XOR</code> aggregate value.
     */
    public /*sealed*/ interface BitXorAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    BitXorAgg
    {
        @NotNull  Field<T> $value();
        @NotNull  BitXorAgg<T> $value(Field<T> value);
    }

    /**
     * The <code>BOOL AND</code> function.
     */
    public /*sealed*/ interface BoolAnd
        extends
            org.jooq.AggregateFunction<Boolean>
        //permits
        //    BoolAnd
    {
        @NotNull  Condition $condition();
        @NotNull  BoolAnd $condition(Condition condition);
    }

    /**
     * The <code>BOOL OR</code> function.
     */
    public /*sealed*/ interface BoolOr
        extends
            org.jooq.AggregateFunction<Boolean>
        //permits
        //    BoolOr
    {
        @NotNull  Condition $condition();
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
    public /*sealed*/ interface Corr
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    Corr
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  Corr $y(Field<? extends Number> y);
        @NotNull  Corr $x(Field<? extends Number> x);
    }

    /**
     * The <code>COUNT</code> function.
     */
    public /*sealed*/ interface Count
        extends
            org.jooq.AggregateFunction<Integer>
        //permits
        //    Count
    {
        @NotNull  Field<?> $field();
                  boolean $distinct();
        @NotNull  Count $field(Field<?> field);
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
    public /*sealed*/ interface CovarSamp
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    CovarSamp
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  CovarSamp $y(Field<? extends Number> y);
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
    public /*sealed*/ interface CovarPop
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    CovarPop
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  CovarPop $y(Field<? extends Number> y);
        @NotNull  CovarPop $x(Field<? extends Number> x);
    }

    /**
     * The <code>MAX</code> function.
     */
    public /*sealed*/ interface Max<T>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    Max
    {
        @NotNull  Field<T> $field();
                  boolean $distinct();
        @NotNull  Max<T> $field(Field<T> field);
        @NotNull  Max<T> $distinct(boolean distinct);
    }

    /**
     * The <code>MEDIAN</code> function.
     */
    public /*sealed*/ interface Median
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    Median
    {
        @NotNull  Field<? extends Number> $field();
        @NotNull  Median $field(Field<? extends Number> field);
    }

    /**
     * The <code>MIN</code> function.
     */
    public /*sealed*/ interface Min<T>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    Min
    {
        @NotNull  Field<T> $field();
                  boolean $distinct();
        @NotNull  Min<T> $field(Field<T> field);
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
    public /*sealed*/ interface Product
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    Product
    {
        @NotNull  Field<? extends Number> $field();
                  boolean $distinct();
        @NotNull  Product $field(Field<? extends Number> field);
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
    public /*sealed*/ interface RegrAvgX
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrAvgX
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrAvgX $y(Field<? extends Number> y);
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
    public /*sealed*/ interface RegrAvgY
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrAvgY
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrAvgY $y(Field<? extends Number> y);
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
    public /*sealed*/ interface RegrCount
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrCount
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrCount $y(Field<? extends Number> y);
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
    public /*sealed*/ interface RegrIntercept
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrIntercept
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrIntercept $y(Field<? extends Number> y);
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
    public /*sealed*/ interface RegrR2
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrR2
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrR2 $y(Field<? extends Number> y);
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
    public /*sealed*/ interface RegrSlope
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrSlope
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrSlope $y(Field<? extends Number> y);
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
    public /*sealed*/ interface RegrSxx
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrSxx
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrSxx $y(Field<? extends Number> y);
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
    public /*sealed*/ interface RegrSxy
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrSxy
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrSxy $y(Field<? extends Number> y);
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
    public /*sealed*/ interface RegrSyy
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrSyy
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrSyy $y(Field<? extends Number> y);
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
    public /*sealed*/ interface StddevPop
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    StddevPop
    {
        @NotNull  Field<? extends Number> $field();
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
    public /*sealed*/ interface StddevSamp
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    StddevSamp
    {
        @NotNull  Field<? extends Number> $field();
        @NotNull  StddevSamp $field(Field<? extends Number> field);
    }

    /**
     * The <code>SUM</code> function.
     */
    public /*sealed*/ interface Sum
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    Sum
    {
        @NotNull  Field<? extends Number> $field();
                  boolean $distinct();
        @NotNull  Sum $field(Field<? extends Number> field);
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
    public /*sealed*/ interface VarPop
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    VarPop
    {
        @NotNull  Field<? extends Number> $field();
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
    public /*sealed*/ interface VarSamp
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    VarSamp
    {
        @NotNull  Field<? extends Number> $field();
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

    interface UOperator1<Q1, R extends org.jooq.QueryPart> extends org.jooq.QueryPart {
        Q1 $arg1();

        @NotNull default R $arg1(Q1 newArg1) { return constructor().apply(newArg1); }

        @NotNull
        Function1<? super Q1, ? extends R> constructor();




















    }

    interface UOperator2<Q1, Q2, R extends org.jooq.QueryPart> extends org.jooq.QueryPart {
        Q1 $arg1();
        Q2 $arg2();

        @NotNull default R $arg1(Q1 newArg1) { return constructor().apply(newArg1, $arg2()); }
        @NotNull default R $arg2(Q2 newArg2) { return constructor().apply($arg1(), newArg2); }

        @NotNull
        Function2<? super Q1, ? super Q2, ? extends R> constructor();





















    }

    interface UOperator3<Q1, Q2, Q3, R extends org.jooq.QueryPart> extends org.jooq.QueryPart {
        Q1 $arg1();
        Q2 $arg2();
        Q3 $arg3();
        @NotNull default R $arg1(Q1 newArg1) { return constructor().apply(newArg1, $arg2(), $arg3()); }
        @NotNull default R $arg2(Q2 newArg2) { return constructor().apply($arg1(), newArg2, $arg3()); }
        @NotNull default R $arg3(Q3 newArg3) { return constructor().apply($arg1(), $arg2(), newArg3); }

        @NotNull
        Function3<? super Q1, ? super Q2, ? super Q3, ? extends R> constructor();






















    }

    /**
     * A marker interface for {@link QueryPart} implementations that are used
     * only to render SQL, i.e. they're transient to the expression tree and
     * don't persist in client code.
     */
    interface UTransient extends UEmpty {}

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
}
