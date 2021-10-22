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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
import org.jooq.GroupField;
import org.jooq.Index;
import org.jooq.Internal;
import org.jooq.JSONEntry;
import org.jooq.Keyword;
// ...
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.Param;
import org.jooq.Parameter;
import org.jooq.Privilege;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Role;
import org.jooq.Row;
import org.jooq.RowCountQuery;
import org.jooq.RowId;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.Statement;
import org.jooq.Table;
import org.jooq.Traverser;
// ...
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;
import org.jooq.XML;
import org.jooq.XMLAttributes;
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
 * Every {@link QueryPart} from the DSL API has a matching {@link QueryPart}
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
 * {@link QueryPart}</li>
 * <li>All private model API utility types are named <code>UXyz</code>, e.g.
 * {@link UEmpty}</li>
 * <li>All accessor methods are named <code>$property()</code>, e.g.
 * {@link Not#$arg1()}</li>
 * </ul>
 * <p>
 * Furthermore, the current draft design lets each {@link QueryPart} publicly
 * extend its matching {@link QueryPart}. <strong>This may not be the case in
 * the future, as long as this API is experimental, a backwards incompatible
 * change may revert this</strong>. Alternative possible designs include:
 * <ul>
 * <li>There's no public relationship between the two types (though accessor
 * methods might be provided)</li>
 * <li>The relationship might be inversed to let {@link QueryPart} extend
 * {@link QueryPart}.</li>
 * <li>The publicly available {@link QueryPart} types don't have an
 * {@link QueryPart} equivalence, but they <em>are</em> the
 * {@link QueryPart}.</li>
 * </ul>
 * <p>
 * <h3>Limitations</h3>
 * <p>
 * The API offers public access to jOOQ's internal representation, and as such,
 * is prone to incompatible changes between minor releases, in addition to the
 * incompatible changes that may arise due to this API being experimental. In
 * this experimental stage, not all {@link QueryPart} implementations have a
 * corresponding public {@link QueryPart} type, but may just implement the API
 * via a {@link UEmpty} or {@link UNotYetImplemented} subtype, and may not
 * provide access to contents via accessor methods.
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
 * The Java 17 distribution of jOOQ will make use of sealed types to improve the
 * usability of the model API in pattern matching expressions etc. The
 * implementations currently can't be made publicly available
 * {@link java.lang.Record} types, because of the existing internal type
 * hierarchy.
 *
 * @author Lukas Eder
 */
@Internal
public final class QOM {

    // -------------------------------------------------------------------------
    // XXX: Model
    // -------------------------------------------------------------------------

    // This class uses a lot of fully qualified types, because of some javac bug
    // In Java 1.8.0_302, which hasn't been analysed and reproduced yet in a more
    // minimal example. Without the qualification, the types cannot be found
    // despite being imported

    public interface MCollection<Q extends org.jooq.QueryPart> extends org.jooq.QueryPart, java.util.Collection<Q> {}
    public interface MList<Q extends org.jooq.QueryPart> extends MCollection<Q>, java.util.List<Q> {}

    public /*sealed*/ interface With
        extends
            org.jooq.QueryPart
        /*permits
            WithImpl*/
    {
        @NotNull MList<? extends CommonTableExpression<?>> $commonTableExpressions();
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
        @NotNull MList<? extends Field<String>> $values();
    }

    public /*sealed*/ interface DropType
        extends
            DDLQuery
        /*permits
            DropTypeImpl*/
    {
        @NotNull MList<? extends Name> $names();
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
        @NotNull MList<? extends Field<?>> $fields();
        @NotNull ResultQuery<R> $query();
    }

    // -------------------------------------------------------------------------
    // XXX: Schema
    // -------------------------------------------------------------------------









    public interface PrimaryKey extends Constraint {
        @NotNull MList<? extends Field<?>> $fields();
    }
    public interface UniqueKey extends Constraint {
        @NotNull MList<? extends Field<?>> $fields();
    }
    public interface ForeignKey extends Constraint {
        @NotNull MList<? extends Field<?>> $fields();
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
    public interface Values<R extends Record> extends Table<R>, UOperator1<MList<? extends Row>, Table<R>> {}
    public interface DataChangeDeltaTable<R extends Record> extends Table<R> {
        @NotNull ResultOption $resultOption();
        @NotNull DMLQuery<R> $query();
    }
    public interface RowsFrom extends Table<Record> {
        @NotNull MList<? extends Table<?>> $tables();
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
            UOperator2<Field<T>, MList<? extends Field<T>>, Condition>
        /*permits
            InList*/
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        @NotNull default MList<? extends Field<T>> $list() { return $arg2(); }
    }

    public /*sealed*/ interface NotInList<T>
        extends
            Condition,
            UOperator2<Field<T>, MList<? extends Field<T>>, Condition>
        /*permits
            NotInList*/
    {
        @NotNull default Field<T> $field() { return $arg1(); }
        @NotNull default MList<? extends Field<T>> $list() { return $arg2(); }
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

    public /*sealed*/ interface Rollup
        extends
            GroupField,
            UOperator1<MList<? extends FieldOrRow>, GroupField>
        /*permits
            Rollup*/
    {}

    public /*sealed*/ interface Cube
        extends
            GroupField,
            UOperator1<MList<? extends FieldOrRow>, GroupField>
        /*permits
            Cube*/
    {}

    public /*sealed*/ interface GroupingSets
        extends
            GroupField,
            UOperator1<MList<? extends MList<? extends FieldOrRow>>, GroupField>
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
        @NotNull MList<? extends Field<?>> $args();
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
        @NotNull MList<? extends Field<?>> $elements();
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
            UOperator1<MList<? extends Field<T>>, Field<T>>
        /*permits
            Greatest*/
    {}

    public /*sealed*/ interface Least<T>
        extends
            Field<T>,
            UOperator1<MList<? extends Field<T>>, Field<T>>
        /*permits
            Least*/
    {}

    public /*sealed*/ interface Choose<T>
        extends
            Field<T>,
            UOperator2<Field<Integer>, MList<? extends Field<T>>, Field<T>>
        /*permits
            Choose*/
    {}

    public /*sealed*/ interface FieldFunction<T>
        extends
            Field<Integer>,
            UOperator2<Field<T>, MList<? extends Field<T>>, Field<Integer>>
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
            UOperator1<MList<? extends Field<T>>, Field<T>>
        /*permits
            Coalesce*/
    {}

    public /*sealed*/ interface Concat
        extends
            Field<String>,
            UOperator1<MList<? extends Field<?>>, Field<String>>
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
        @NotNull MList<? extends Field<?>> $content();
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
        @NotNull  MList<? extends Constraint> $constraints();
        @NotNull  CreateDomain<T> $domain(Domain<?> domain);
        @NotNull  CreateDomain<T> $ifNotExists(boolean ifNotExists);
        @NotNull  CreateDomain<T> $dataType(DataType<T> dataType);
        @NotNull  CreateDomain<T> $default_(Field<T> default_);
        @NotNull  CreateDomain<T> $constraints(MList<? extends Constraint> constraints);
    }






























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
        @NotNull  MList<? extends OrderField<?>> $on();
        @NotNull  MList<? extends Field<?>> $include();
        @Nullable Condition $where();
                  boolean $excludeNullKeys();
        @NotNull  CreateIndex $unique(boolean unique);
        @NotNull  CreateIndex $index(Index index);
        @NotNull  CreateIndex $ifNotExists(boolean ifNotExists);
        @NotNull  CreateIndex $table(Table<?> table);
        @NotNull  CreateIndex $on(MList<? extends OrderField<?>> on);
        @NotNull  CreateIndex $include(MList<? extends Field<?>> include);
        @NotNull  CreateIndex $where(Condition where);
        @NotNull  CreateIndex $excludeNullKeys(boolean excludeNullKeys);
    }































































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

    public /*sealed*/ interface Grant
        extends
            DDLQuery
        //permits
        //    GrantImpl
    {
        @NotNull  MList<? extends Privilege> $privileges();
        @NotNull  Table<?> $on();
        @Nullable Role $to();
                  boolean $toPublic();
                  boolean $withGrantOption();
        @NotNull  Grant $privileges(MList<? extends Privilege> privileges);
        @NotNull  Grant $on(Table<?> on);
        @NotNull  Grant $to(Role to);
        @NotNull  Grant $toPublic(boolean toPublic);
        @NotNull  Grant $withGrantOption(boolean withGrantOption);
    }

    public /*sealed*/ interface Revoke
        extends
            DDLQuery
        //permits
        //    RevokeImpl
    {
        @NotNull  MList<? extends Privilege> $privileges();
                  boolean $grantOptionFor();
        @NotNull  Table<?> $on();
        @Nullable Role $from();
                  boolean $fromPublic();
        @NotNull  Revoke $privileges(MList<? extends Privilege> privileges);
        @NotNull  Revoke $grantOptionFor(boolean grantOptionFor);
        @NotNull  Revoke $on(Table<?> on);
        @NotNull  Revoke $from(Role from);
        @NotNull  Revoke $fromPublic(boolean fromPublic);
    }

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

    public /*sealed*/ interface SetCatalog
        extends
            org.jooq.RowCountQuery
        //permits
        //    SetCatalog
    {
        @NotNull  Catalog $catalog();
        @NotNull  SetCatalog $catalog(Catalog catalog);
    }

    public /*sealed*/ interface SetSchema
        extends
            org.jooq.RowCountQuery
        //permits
        //    SetSchema
    {
        @NotNull  Schema $schema();
        @NotNull  SetSchema $schema(Schema schema);
    }

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
















    public /*sealed*/ interface And
        extends
            CombinedCondition
        //permits
        //    And
    {}

    public /*sealed*/ interface TableEq<R extends Record>
        extends
            org.jooq.Condition,
            UOperator2<Table<R>, Table<R>, Condition>
        //permits
        //    TableEq
    {}

    public /*sealed*/ interface Eq<T>
        extends
            CompareCondition<T>
        //permits
        //    Eq
    {}

    public /*sealed*/ interface Exists
        extends
            org.jooq.Condition
        //permits
        //    Exists
    {
        @NotNull  Select<?> $query();
        @NotNull  Exists $query(Select<?> query);
    }

    public /*sealed*/ interface Ge<T>
        extends
            CompareCondition<T>
        //permits
        //    Ge
    {}

    public /*sealed*/ interface Gt<T>
        extends
            CompareCondition<T>
        //permits
        //    Gt
    {}

    public /*sealed*/ interface In<T>
        extends
            org.jooq.Condition,
            UOperator2<Field<T>, Select<? extends Record1<T>>, Condition>
        //permits
        //    In
    {}

    public /*sealed*/ interface IsDistinctFrom<T>
        extends
            CompareCondition<T>
        //permits
        //    IsDistinctFrom
    {}

    public /*sealed*/ interface IsNull
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsNull
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    public /*sealed*/ interface IsNotDistinctFrom<T>
        extends
            CompareCondition<T>
        //permits
        //    IsNotDistinctFrom
    {}

    public /*sealed*/ interface IsNotNull
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsNotNull
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    public /*sealed*/ interface Le<T>
        extends
            CompareCondition<T>
        //permits
        //    Le
    {}

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

    public /*sealed*/ interface Lt<T>
        extends
            CompareCondition<T>
        //permits
        //    Lt
    {}

    public /*sealed*/ interface TableNe<R extends Record>
        extends
            org.jooq.Condition,
            UOperator2<Table<R>, Table<R>, Condition>
        //permits
        //    TableNe
    {}

    public /*sealed*/ interface Ne<T>
        extends
            CompareCondition<T>
        //permits
        //    Ne
    {}

    public /*sealed*/ interface Not
        extends
            org.jooq.Condition,
            UOperator1<Condition, Condition>
        //permits
        //    Not
    {
        @NotNull  default Condition $condition() { return $arg1(); }
    }

    public /*sealed*/ interface NotField
        extends
            org.jooq.Field<Boolean>,
            UOperator1<Field<Boolean>, Field<Boolean>>
        //permits
        //    NotField
    {
        @NotNull  default Field<Boolean> $field() { return $arg1(); }
    }

    public /*sealed*/ interface NotIn<T>
        extends
            org.jooq.Condition,
            UOperator2<Field<T>, Select<? extends Record1<T>>, Condition>
        //permits
        //    NotIn
    {}

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

    public /*sealed*/ interface Or
        extends
            CombinedCondition
        //permits
        //    Or
    {}

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

    public /*sealed*/ interface Unique
        extends
            org.jooq.Condition
        //permits
        //    Unique
    {
        @NotNull  Select<?> $query();
        @NotNull  Unique $query(Select<?> query);
    }

    public /*sealed*/ interface IsDocument
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsDocument
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    public /*sealed*/ interface IsNotDocument
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsNotDocument
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    public /*sealed*/ interface IsJson
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsJson
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    public /*sealed*/ interface IsNotJson
        extends
            org.jooq.Condition,
            UOperator1<Field<?>, Condition>
        //permits
        //    IsNotJson
    {
        @NotNull  default Field<?> $field() { return $arg1(); }
    }

    public /*sealed*/ interface QualifiedRowid
        extends
            org.jooq.Field<RowId>,
            UOperator1<Table<?>, Field<RowId>>
        //permits
        //    QualifiedRowid
    {
        @NotNull  default Table<?> $table() { return $arg1(); }
    }

    public /*sealed*/ interface Abs<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Abs
    {
        @NotNull  Field<T> $number();
        @NotNull  Abs<T> $number(Field<T> number);
    }

    public /*sealed*/ interface Acos
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Acos
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Acos $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Asin
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Asin
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Asin $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Atan
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Atan
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Atan $number(Field<? extends Number> number);
    }

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

    public /*sealed*/ interface BitAnd<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitAnd
    {}

    public /*sealed*/ interface BitCount
        extends
            org.jooq.Field<Integer>
        //permits
        //    BitCount
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  BitCount $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface BitNand<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitNand
    {}

    public /*sealed*/ interface BitNor<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitNor
    {}

    public /*sealed*/ interface BitNot<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator1<Field<T>, Field<T>>
        //permits
        //    BitNot
    {}

    public /*sealed*/ interface BitOr<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitOr
    {}

    public /*sealed*/ interface BitXNor<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitXNor
    {}

    public /*sealed*/ interface BitXor<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<T>, Field<T>>
        //permits
        //    BitXor
    {}

    public /*sealed*/ interface Ceil<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Ceil
    {
        @NotNull  Field<T> $value();
        @NotNull  Ceil<T> $value(Field<T> value);
    }

    public /*sealed*/ interface Cos
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Cos
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Cos $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Cosh
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Cosh
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Cosh $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Cot
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Cot
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Cot $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Coth
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Coth
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Coth $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Degrees
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Degrees
    {
        @NotNull  Field<? extends Number> $radians();
        @NotNull  Degrees $radians(Field<? extends Number> radians);
    }

    public /*sealed*/ interface Euler
        extends
            org.jooq.Field<BigDecimal>,
            UEmpty
        //permits
        //    Euler
    {}

    public /*sealed*/ interface Exp
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Exp
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  Exp $value(Field<? extends Number> value);
    }

    public /*sealed*/ interface Floor<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Floor
    {
        @NotNull  Field<T> $value();
        @NotNull  Floor<T> $value(Field<T> value);
    }

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

    public /*sealed*/ interface Log10
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Log10
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  Log10 $value(Field<? extends Number> value);
    }

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

    public /*sealed*/ interface Pi
        extends
            org.jooq.Field<BigDecimal>,
            UEmpty
        //permits
        //    Pi
    {}

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

    public /*sealed*/ interface Radians
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Radians
    {
        @NotNull  Field<? extends Number> $degrees();
        @NotNull  Radians $degrees(Field<? extends Number> degrees);
    }

    public /*sealed*/ interface Rand
        extends
            org.jooq.Field<BigDecimal>,
            UEmpty
        //permits
        //    Rand
    {}

    public /*sealed*/ interface Round<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Round
    {
        @NotNull  Field<T> $value();
        @Nullable Field<Integer> $decimals();
        @NotNull  Round<T> $value(Field<T> value);
        @NotNull  Round<T> $decimals(Field<Integer> decimals);
    }

    public /*sealed*/ interface Shl<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<? extends Number>, Field<T>>
        //permits
        //    Shl
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
        @NotNull  default Field<? extends Number> $count() { return $arg2(); }
    }

    public /*sealed*/ interface Shr<T extends Number>
        extends
            org.jooq.Field<T>,
            UOperator2<Field<T>, Field<? extends Number>, Field<T>>
        //permits
        //    Shr
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
        @NotNull  default Field<? extends Number> $count() { return $arg2(); }
    }

    public /*sealed*/ interface Sign
        extends
            org.jooq.Field<Integer>
        //permits
        //    Sign
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Sign $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Sin
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Sin
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Sin $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Sinh
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Sinh
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Sinh $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Sqrt
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Sqrt
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  Sqrt $value(Field<? extends Number> value);
    }

    public /*sealed*/ interface Square<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Square
    {
        @NotNull  Field<T> $value();
        @NotNull  Square<T> $value(Field<T> value);
    }

    public /*sealed*/ interface Tan
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Tan
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Tan $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Tanh
        extends
            org.jooq.Field<BigDecimal>
        //permits
        //    Tanh
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Tanh $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Tau
        extends
            org.jooq.Field<BigDecimal>,
            UEmpty
        //permits
        //    Tau
    {}

    public /*sealed*/ interface Trunc<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    Trunc
    {
        @NotNull  Field<T> $value();
        @NotNull  Field<Integer> $decimals();
        @NotNull  Trunc<T> $value(Field<T> value);
        @NotNull  Trunc<T> $decimals(Field<Integer> decimals);
    }

    public /*sealed*/ interface WidthBucket<T extends Number>
        extends
            org.jooq.Field<T>
        //permits
        //    WidthBucket
    {
        @NotNull  Field<T> $field();
        @NotNull  Field<T> $low();
        @NotNull  Field<T> $high();
        @NotNull  Field<Integer> $buckets();
        @NotNull  WidthBucket<T> $field(Field<T> field);
        @NotNull  WidthBucket<T> $low(Field<T> low);
        @NotNull  WidthBucket<T> $high(Field<T> high);
        @NotNull  WidthBucket<T> $buckets(Field<Integer> buckets);
    }

    public /*sealed*/ interface Ascii
        extends
            org.jooq.Field<Integer>
        //permits
        //    Ascii
    {
        @NotNull  Field<String> $string();
        @NotNull  Ascii $string(Field<String> string);
    }

    public /*sealed*/ interface BitLength
        extends
            org.jooq.Field<Integer>
        //permits
        //    BitLength
    {
        @NotNull  Field<String> $string();
        @NotNull  BitLength $string(Field<String> string);
    }

    public /*sealed*/ interface CharLength
        extends
            org.jooq.Field<Integer>
        //permits
        //    CharLength
    {
        @NotNull  Field<String> $string();
        @NotNull  CharLength $string(Field<String> string);
    }

    public /*sealed*/ interface Chr
        extends
            org.jooq.Field<String>
        //permits
        //    Chr
    {
        @NotNull  Field<? extends Number> $number();
        @NotNull  Chr $number(Field<? extends Number> number);
    }

    public /*sealed*/ interface Contains<T>
        extends
            CompareCondition<T>
        //permits
        //    Contains
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
        @NotNull  default Field<T> $content() { return $arg2(); }
    }

    public /*sealed*/ interface ContainsIgnoreCase<T>
        extends
            CompareCondition<T>
        //permits
        //    ContainsIgnoreCase
    {
        @NotNull  default Field<T> $value() { return $arg1(); }
        @NotNull  default Field<T> $content() { return $arg2(); }
    }

    public /*sealed*/ interface Digits
        extends
            org.jooq.Field<String>
        //permits
        //    Digits
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  Digits $value(Field<? extends Number> value);
    }

    public /*sealed*/ interface EndsWith<T>
        extends
            CompareCondition<T>
        //permits
        //    EndsWith
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $suffix() { return $arg2(); }
    }

    public /*sealed*/ interface EndsWithIgnoreCase<T>
        extends
            CompareCondition<T>
        //permits
        //    EndsWithIgnoreCase
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $suffix() { return $arg2(); }
    }

    public /*sealed*/ interface Left
        extends
            org.jooq.Field<String>
        //permits
        //    Left
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<? extends Number> $length();
        @NotNull  Left $string(Field<String> string);
        @NotNull  Left $length(Field<? extends Number> length);
    }

    public /*sealed*/ interface Lower
        extends
            org.jooq.Field<String>
        //permits
        //    Lower
    {
        @NotNull  Field<String> $string();
        @NotNull  Lower $string(Field<String> string);
    }

    public /*sealed*/ interface Lpad
        extends
            org.jooq.Field<String>
        //permits
        //    Lpad
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<? extends Number> $length();
        @Nullable Field<String> $character();
        @NotNull  Lpad $string(Field<String> string);
        @NotNull  Lpad $length(Field<? extends Number> length);
        @NotNull  Lpad $character(Field<String> character);
    }

    public /*sealed*/ interface Ltrim
        extends
            org.jooq.Field<String>
        //permits
        //    Ltrim
    {
        @NotNull  Field<String> $string();
        @Nullable Field<String> $characters();
        @NotNull  Ltrim $string(Field<String> string);
        @NotNull  Ltrim $characters(Field<String> characters);
    }

    public /*sealed*/ interface Md5
        extends
            org.jooq.Field<String>
        //permits
        //    Md5
    {
        @NotNull  Field<String> $string();
        @NotNull  Md5 $string(Field<String> string);
    }

    public /*sealed*/ interface OctetLength
        extends
            org.jooq.Field<Integer>
        //permits
        //    OctetLength
    {
        @NotNull  Field<String> $string();
        @NotNull  OctetLength $string(Field<String> string);
    }

    public /*sealed*/ interface Overlay
        extends
            org.jooq.Field<String>
        //permits
        //    Overlay
    {
        @NotNull  Field<String> $in();
        @NotNull  Field<String> $placing();
        @NotNull  Field<? extends Number> $startIndex();
        @Nullable Field<? extends Number> $length();
        @NotNull  Overlay $in(Field<String> in);
        @NotNull  Overlay $placing(Field<String> placing);
        @NotNull  Overlay $startIndex(Field<? extends Number> startIndex);
        @NotNull  Overlay $length(Field<? extends Number> length);
    }

    public /*sealed*/ interface Position
        extends
            org.jooq.Field<Integer>
        //permits
        //    Position
    {
        @NotNull  Field<String> $in();
        @NotNull  Field<String> $search();
        @Nullable Field<? extends Number> $startIndex();
        @NotNull  Position $in(Field<String> in);
        @NotNull  Position $search(Field<String> search);
        @NotNull  Position $startIndex(Field<? extends Number> startIndex);
    }

    public /*sealed*/ interface Repeat
        extends
            org.jooq.Field<String>
        //permits
        //    Repeat
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<? extends Number> $count();
        @NotNull  Repeat $string(Field<String> string);
        @NotNull  Repeat $count(Field<? extends Number> count);
    }

    public /*sealed*/ interface Replace
        extends
            org.jooq.Field<String>
        //permits
        //    Replace
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<String> $search();
        @Nullable Field<String> $replace();
        @NotNull  Replace $string(Field<String> string);
        @NotNull  Replace $search(Field<String> search);
        @NotNull  Replace $replace(Field<String> replace);
    }

    public /*sealed*/ interface Reverse
        extends
            org.jooq.Field<String>
        //permits
        //    Reverse
    {
        @NotNull  Field<String> $string();
        @NotNull  Reverse $string(Field<String> string);
    }

    public /*sealed*/ interface Right
        extends
            org.jooq.Field<String>
        //permits
        //    Right
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<? extends Number> $length();
        @NotNull  Right $string(Field<String> string);
        @NotNull  Right $length(Field<? extends Number> length);
    }

    public /*sealed*/ interface Rpad
        extends
            org.jooq.Field<String>
        //permits
        //    Rpad
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<? extends Number> $length();
        @Nullable Field<String> $character();
        @NotNull  Rpad $string(Field<String> string);
        @NotNull  Rpad $length(Field<? extends Number> length);
        @NotNull  Rpad $character(Field<String> character);
    }

    public /*sealed*/ interface Rtrim
        extends
            org.jooq.Field<String>
        //permits
        //    Rtrim
    {
        @NotNull  Field<String> $string();
        @Nullable Field<String> $characters();
        @NotNull  Rtrim $string(Field<String> string);
        @NotNull  Rtrim $characters(Field<String> characters);
    }

    public /*sealed*/ interface Space
        extends
            org.jooq.Field<String>
        //permits
        //    Space
    {
        @NotNull  Field<? extends Number> $count();
        @NotNull  Space $count(Field<? extends Number> count);
    }

    public /*sealed*/ interface SplitPart
        extends
            org.jooq.Field<String>
        //permits
        //    SplitPart
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<String> $delimiter();
        @NotNull  Field<? extends Number> $n();
        @NotNull  SplitPart $string(Field<String> string);
        @NotNull  SplitPart $delimiter(Field<String> delimiter);
        @NotNull  SplitPart $n(Field<? extends Number> n);
    }

    public /*sealed*/ interface StartsWith<T>
        extends
            CompareCondition<T>
        //permits
        //    StartsWith
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $prefix() { return $arg2(); }
    }

    public /*sealed*/ interface StartsWithIgnoreCase<T>
        extends
            CompareCondition<T>
        //permits
        //    StartsWithIgnoreCase
    {
        @NotNull  default Field<T> $string() { return $arg1(); }
        @NotNull  default Field<T> $prefix() { return $arg2(); }
    }

    public /*sealed*/ interface Substring
        extends
            org.jooq.Field<String>
        //permits
        //    Substring
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<? extends Number> $startingPosition();
        @Nullable Field<? extends Number> $length();
        @NotNull  Substring $string(Field<String> string);
        @NotNull  Substring $startingPosition(Field<? extends Number> startingPosition);
        @NotNull  Substring $length(Field<? extends Number> length);
    }

    public /*sealed*/ interface SubstringIndex
        extends
            org.jooq.Field<String>
        //permits
        //    SubstringIndex
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<String> $delimiter();
        @NotNull  Field<? extends Number> $n();
        @NotNull  SubstringIndex $string(Field<String> string);
        @NotNull  SubstringIndex $delimiter(Field<String> delimiter);
        @NotNull  SubstringIndex $n(Field<? extends Number> n);
    }

    public /*sealed*/ interface ToChar
        extends
            org.jooq.Field<String>
        //permits
        //    ToChar
    {
        @NotNull  Field<?> $value();
        @Nullable Field<String> $formatMask();
        @NotNull  ToChar $value(Field<?> value);
        @NotNull  ToChar $formatMask(Field<String> formatMask);
    }

    public /*sealed*/ interface ToDate
        extends
            org.jooq.Field<Date>
        //permits
        //    ToDate
    {
        @NotNull  Field<String> $value();
        @NotNull  Field<String> $formatMask();
        @NotNull  ToDate $value(Field<String> value);
        @NotNull  ToDate $formatMask(Field<String> formatMask);
    }

    public /*sealed*/ interface ToHex
        extends
            org.jooq.Field<String>
        //permits
        //    ToHex
    {
        @NotNull  Field<? extends Number> $value();
        @NotNull  ToHex $value(Field<? extends Number> value);
    }

    public /*sealed*/ interface ToTimestamp
        extends
            org.jooq.Field<Timestamp>
        //permits
        //    ToTimestamp
    {
        @NotNull  Field<String> $value();
        @NotNull  Field<String> $formatMask();
        @NotNull  ToTimestamp $value(Field<String> value);
        @NotNull  ToTimestamp $formatMask(Field<String> formatMask);
    }

    public /*sealed*/ interface Translate
        extends
            org.jooq.Field<String>
        //permits
        //    Translate
    {
        @NotNull  Field<String> $string();
        @NotNull  Field<String> $from();
        @NotNull  Field<String> $to();
        @NotNull  Translate $string(Field<String> string);
        @NotNull  Translate $from(Field<String> from);
        @NotNull  Translate $to(Field<String> to);
    }

    public /*sealed*/ interface Trim
        extends
            org.jooq.Field<String>
        //permits
        //    Trim
    {
        @NotNull  Field<String> $string();
        @Nullable Field<String> $characters();
        @NotNull  Trim $string(Field<String> string);
        @NotNull  Trim $characters(Field<String> characters);
    }

    public /*sealed*/ interface Upper
        extends
            org.jooq.Field<String>
        //permits
        //    Upper
    {
        @NotNull  Field<String> $string();
        @NotNull  Upper $string(Field<String> string);
    }

    public /*sealed*/ interface Uuid
        extends
            org.jooq.Field<UUID>,
            UEmpty
        //permits
        //    Uuid
    {}

    public /*sealed*/ interface DateAdd<T>
        extends
            org.jooq.Field<T>
        //permits
        //    DateAdd
    {
        @NotNull  Field<T> $date();
        @NotNull  Field<? extends Number> $interval();
        @Nullable DatePart $datePart();
        @NotNull  DateAdd<T> $date(Field<T> date);
        @NotNull  DateAdd<T> $interval(Field<? extends Number> interval);
        @NotNull  DateAdd<T> $datePart(DatePart datePart);
    }

    public /*sealed*/ interface Cardinality
        extends
            org.jooq.Field<Integer>
        //permits
        //    Cardinality
    {
        @NotNull  Field<? extends Object[]> $array();
        @NotNull  Cardinality $array(Field<? extends Object[]> array);
    }

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

    public /*sealed*/ interface Nvl<T>
        extends
            org.jooq.Field<T>
        //permits
        //    Nvl
    {
        @NotNull  Field<T> $value();
        @NotNull  Field<T> $defaultValue();
        @NotNull  Nvl<T> $value(Field<T> value);
        @NotNull  Nvl<T> $defaultValue(Field<T> defaultValue);
    }

    public /*sealed*/ interface Nullif<T>
        extends
            org.jooq.Field<T>
        //permits
        //    Nullif
    {
        @NotNull  Field<T> $value();
        @NotNull  Field<T> $other();
        @NotNull  Nullif<T> $value(Field<T> value);
        @NotNull  Nullif<T> $other(Field<T> other);
    }

    public /*sealed*/ interface CurrentCatalog
        extends
            org.jooq.Field<String>,
            UEmpty
        //permits
        //    CurrentCatalog
    {}

    public /*sealed*/ interface CurrentSchema
        extends
            org.jooq.Field<String>,
            UEmpty
        //permits
        //    CurrentSchema
    {}

    public /*sealed*/ interface CurrentUser
        extends
            org.jooq.Field<String>,
            UEmpty
        //permits
        //    CurrentUser
    {}





















































































    public /*sealed*/ interface XMLComment
        extends
            org.jooq.Field<XML>
        //permits
        //    XMLComment
    {
        @NotNull  Field<String> $comment();
        @NotNull  XMLComment $comment(Field<String> comment);
    }

    public /*sealed*/ interface XMLConcat
        extends
            org.jooq.Field<XML>
        //permits
        //    XMLConcat
    {
        @NotNull  MList<? extends Field<?>> $args();
        @NotNull  XMLConcat $args(MList<? extends Field<?>> args);
    }














    public /*sealed*/ interface XMLForest
        extends
            org.jooq.Field<XML>
        //permits
        //    XMLForest
    {
        @NotNull  MList<? extends Field<?>> $fields();
        @NotNull  XMLForest $fields(MList<? extends Field<?>> fields);
    }

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

    public /*sealed*/ interface JSONArray<T>
        extends
            Field<T>
        //permits
        //    JSONArray
    {
        @NotNull  DataType<T> $type();
        @NotNull  MList<? extends Field<?>> $fields();
        @Nullable JSONOnNull $onNull();
        @Nullable DataType<?> $returning();
        @NotNull  JSONArray<T> $type(DataType<T> type);
        @NotNull  JSONArray<T> $fields(MList<? extends Field<?>> fields);
        @NotNull  JSONArray<T> $onNull(JSONOnNull onNull);
        @NotNull  JSONArray<T> $returning(DataType<?> returning);
    }

    public /*sealed*/ interface JSONObject<T>
        extends
            Field<T>
        //permits
        //    JSONObject
    {
        @NotNull  DataType<T> $type();
        @NotNull  MList<? extends JSONEntry<?>> $entries();
        @Nullable JSONOnNull $onNull();
        @Nullable DataType<?> $returning();
        @NotNull  JSONObject<T> $type(DataType<T> type);
        @NotNull  JSONObject<T> $entries(MList<? extends JSONEntry<?>> entries);
        @NotNull  JSONObject<T> $onNull(JSONOnNull onNull);
        @NotNull  JSONObject<T> $returning(DataType<?> returning);
    }






























    public /*sealed*/ interface ConditionAsField
        extends
            org.jooq.Field<Boolean>
        //permits
        //    ConditionAsField
    {
        @NotNull  Condition $condition();
        @NotNull  ConditionAsField $condition(Condition condition);
    }

    public /*sealed*/ interface FieldCondition
        extends
            org.jooq.Condition
        //permits
        //    FieldCondition
    {
        @NotNull  Field<Boolean> $field();
        @NotNull  FieldCondition $field(Field<Boolean> field);
    }

    public /*sealed*/ interface AnyValue<T>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    AnyValue
    {
        @NotNull  Field<T> $field();
        @NotNull  AnyValue<T> $field(Field<T> field);
    }

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

    public /*sealed*/ interface BitAndAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    BitAndAgg
    {
        @NotNull  Field<T> $value();
        @NotNull  BitAndAgg<T> $value(Field<T> value);
    }

    public /*sealed*/ interface BitOrAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    BitOrAgg
    {
        @NotNull  Field<T> $value();
        @NotNull  BitOrAgg<T> $value(Field<T> value);
    }

    public /*sealed*/ interface BitXorAgg<T extends Number>
        extends
            org.jooq.AggregateFunction<T>
        //permits
        //    BitXorAgg
    {
        @NotNull  Field<T> $value();
        @NotNull  BitXorAgg<T> $value(Field<T> value);
    }

    public /*sealed*/ interface BoolAnd
        extends
            org.jooq.AggregateFunction<Boolean>
        //permits
        //    BoolAnd
    {
        @NotNull  Condition $condition();
        @NotNull  BoolAnd $condition(Condition condition);
    }

    public /*sealed*/ interface BoolOr
        extends
            org.jooq.AggregateFunction<Boolean>
        //permits
        //    BoolOr
    {
        @NotNull  Condition $condition();
        @NotNull  BoolOr $condition(Condition condition);
    }

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

    public /*sealed*/ interface Median
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    Median
    {
        @NotNull  Field<? extends Number> $field();
        @NotNull  Median $field(Field<? extends Number> field);
    }

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

    public /*sealed*/ interface RegrAvgx
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrAvgx
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrAvgx $y(Field<? extends Number> y);
        @NotNull  RegrAvgx $x(Field<? extends Number> x);
    }

    public /*sealed*/ interface RegrAvgy
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    RegrAvgy
    {
        @NotNull  Field<? extends Number> $y();
        @NotNull  Field<? extends Number> $x();
        @NotNull  RegrAvgy $y(Field<? extends Number> y);
        @NotNull  RegrAvgy $x(Field<? extends Number> x);
    }

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

    public /*sealed*/ interface StddevPop
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    StddevPop
    {
        @NotNull  Field<? extends Number> $field();
        @NotNull  StddevPop $field(Field<? extends Number> field);
    }

    public /*sealed*/ interface StddevSamp
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    StddevSamp
    {
        @NotNull  Field<? extends Number> $field();
        @NotNull  StddevSamp $field(Field<? extends Number> field);
    }

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

    public /*sealed*/ interface VarPop
        extends
            org.jooq.AggregateFunction<BigDecimal>
        //permits
        //    VarPop
    {
        @NotNull  Field<? extends Number> $field();
        @NotNull  VarPop $field(Field<? extends Number> field);
    }

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

        default <T> T transform(Function1<? super Q1, ? extends T> function) {
            return function.apply($arg1());
        }

        @Override
        default <T> T $traverse(Traverser<?, T> traverser) {
            return QOM.traverse(traverser, this, $arg1());
        };

        @NotNull
        @Override
        default org.jooq.QueryPart $replace(
            Predicate<? super org.jooq.QueryPart> recurse,
            Function1<? super org.jooq.QueryPart, ? extends org.jooq.QueryPart> replacement
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

    interface UOperator2<Q1, Q2, R extends org.jooq.QueryPart> extends org.jooq.QueryPart {
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
        default <T> T $traverse(Traverser<?, T> traverser) {
            return QOM.traverse(traverser, this, $arg1(), $arg2());
        };

        @NotNull
        @Override
        default org.jooq.QueryPart $replace(
            Predicate<? super org.jooq.QueryPart> recurse,
            Function1<? super org.jooq.QueryPart, ? extends org.jooq.QueryPart> replacement
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

    interface UOperator3<Q1, Q2, Q3, R extends org.jooq.QueryPart> extends org.jooq.QueryPart {
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
        default <T> T $traverse(Traverser<?, T> traverser) {
            return QOM.traverse(traverser, this, $arg1(), $arg2(), $arg3());
        };

        @NotNull
        @Override
        default org.jooq.QueryPart $replace(
            Predicate<? super org.jooq.QueryPart> recurse,
            Function1<? super org.jooq.QueryPart, ? extends org.jooq.QueryPart> replacement
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
    static class UNotYetImplementedException extends RuntimeException {}

    interface UProxy<Q extends org.jooq.QueryPart> extends org.jooq.QueryPart {
        Q $delegate();

        @Override
        default <R> R $traverse(Traverser<?, R> traverser) {
            return $delegate().$traverse(traverser);
        }

        @Override
        default org.jooq.QueryPart $replace(
            Predicate<? super org.jooq.QueryPart> recurse,
            Function1<? super org.jooq.QueryPart, ? extends org.jooq.QueryPart> replacement
        ) {
            org.jooq.QueryPart r = $delegate().$replace(recurse, replacement);
            return $delegate() == r ? this : r;
        }
    }

    interface UEmpty extends org.jooq.QueryPart {

        @Override
        default <R> R $traverse(Traverser<?, R> traverser) {
            return QOM.traverse(traverser, this);
        }

        @Override
        default org.jooq.QueryPart $replace(
            Predicate<? super org.jooq.QueryPart> recurse,
            Function1<? super org.jooq.QueryPart, ? extends org.jooq.QueryPart> replacement
        ) {
            if (recurse.test(this))
                return replacement.apply(this);
            else
                return this;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Undisclosed, internal query parts
    // -------------------------------------------------------------------------

    interface UEmptyCondition extends Condition, UEmpty {}
    interface UEmptyField<T> extends Field<T>, UEmpty {}
    interface UEmptyTable<R extends Record> extends Table<R>, UEmpty {}
    interface UEmptyStatement extends Statement, UEmpty {}
    interface UEmptyQuery extends Query, UEmpty {}

    // -------------------------------------------------------------------------
    // XXX: Utilities
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    static final <Q> Q replace(
        Q q,
        Predicate<? super org.jooq.QueryPart> recurse,
        Function1<? super org.jooq.QueryPart, ? extends org.jooq.QueryPart> replacement
    ) {

        // TODO: Support also arrays, sets, etc.
        if (q instanceof List) { List<?> l = (List<?>) q;
            List<Object> r = null;

            for (int i = 0; i < l.size(); i++) {
                Object o = l.get(i);
                Object x = replace(o, recurse, replacement);

                if (o != x) {

                    // TODO: What about other lists, e.g. org.jooq.QueryPartList?
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


        return q instanceof org.jooq.QueryPart && recurse.test((org.jooq.QueryPart) q)
             ? (Q) ((org.jooq.QueryPart) q).$replace(recurse, replacement)
             : q;
    }

    static final <T> boolean test(Predicate<? super T> predicate, T value) {
        return predicate != null && predicate.test(value);
    }

    static final <A, T> T traverse(Traverser<A, T> t, Object part, Object... parts) {
        if (test(t.abort(), t.supplied()))
            return t.finished();

        try {
            if (part instanceof QueryPart)
                t.before().apply(t.supplied(), (QueryPart) part);
            if (test(t.abort(), t.supplied()))
                return t.finished();

            for (int i = 0; i < parts.length; i++) {
                if (parts[i] instanceof QueryPart) { QueryPart p = (QueryPart) parts[i];
                    if (test(t.recurse(), p)) {
                        p.$traverse(t);
                        if (test(t.abort(), t.supplied()))
                            return t.finished();
                    }
                }
            }
        }
        finally {
            if (part instanceof QueryPart)
                t.after().apply(t.supplied(), (QueryPart) part);
        }

        return t.finished();
    };

    @SuppressWarnings("unchecked")
    static final <QR extends org.jooq.QueryPart, Q> QR replace(
        QR wrapper,
        Q[] q,
        Function1<? super Q[], ? extends QR> wrap,
        Predicate<? super org.jooq.QueryPart> recurse,
        Function1<? super org.jooq.QueryPart, ? extends org.jooq.QueryPart> replacement
    ) {
        Q[] r = (Q[]) java.lang.reflect.Array.newInstance(q.getClass().getComponentType(), q.length);

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



    static final <QR extends QueryPart, Q1> QR replace(
        QR wrapper,
        Q1 q1,
        Function1<? super Q1, ? extends QR> wrap,
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);

        if (r1 != q1)
            wrapper = wrap.apply(r1);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends QueryPart, Q1, Q2> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Function2<? super Q1, ? super Q2, ? extends QR> wrap,
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);

        if (r1 != q1 || r2 != q2)
            wrapper = wrap.apply(r1, r2);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends QueryPart, Q1, Q2, Q3> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Function3<? super Q1, ? super Q2, ? super Q3, ? extends QR> wrap,
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3)
            wrapper = wrap.apply(r1, r2, r3);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Function4<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? extends QR> wrap,
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
    ) {
        Q1 r1 = replace(q1, recurse, replacement);
        Q2 r2 = replace(q2, recurse, replacement);
        Q3 r3 = replace(q3, recurse, replacement);
        Q4 r4 = replace(q4, recurse, replacement);

        if (r1 != q1 || r2 != q2 || r3 != q3 || r4 != q4)
            wrapper = wrap.apply(r1, r2, r3, r4);

        return replaceUntilStable(wrapper, recurse, replacement);
    }

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Function5<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? extends QR> wrap,
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Function6<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? extends QR> wrap,
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7> QR replace(
        QR wrapper,
        Q1 q1,
        Q2 q2,
        Q3 q3,
        Q4 q4,
        Q5 q5,
        Q6 q6,
        Q7 q7,
        Function7<? super Q1, ? super Q2, ? super Q3, ? super Q4, ? super Q5, ? super Q6, ? super Q7, ? extends QR> wrap,
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18, Q19> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18, Q19, Q20> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18, Q19, Q20, Q21> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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

    static final <QR extends QueryPart, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, Q15, Q16, Q17, Q18, Q19, Q20, Q21, Q22> QR replace(
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
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
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



    private static <QR extends org.jooq.QueryPart> QR replaceUntilStable(
        QR wrapper,
        Predicate<? super org.jooq.QueryPart> recurse,
        Function1<? super org.jooq.QueryPart, ? extends org.jooq.QueryPart> replacement
    ) {
        QR q = wrapper;
        QR r = wrapper;

        do {
            if (recurse.test(q))
                r = (QR) replacement.apply(q);
        }
        while (r != null && r != q && (q = (QR) r.$replace(recurse, replacement)) != null);
        return r;
    }
}
