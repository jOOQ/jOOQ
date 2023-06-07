jOOQ
====

jOOQ is an internal DSL and source code generator, modelling the SQL language as a type safe Java API to help you write better SQL. 

Its main features include:

- [The source code generator](https://blog.jooq.org/why-you-should-use-jooq-with-code-generation/)
- The [DSL API for type safe query construction](https://www.jooq.org/doc/latest/manual/sql-building/dsl-api/) and [dynamic SQL](https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql/)

Secondary features include:

- [DAOs](https://www.jooq.org/doc/latest/manual/sql-execution/daos/)
- [Data export](https://www.jooq.org/doc/latest/manual/sql-execution/exporting/) and [import](https://www.jooq.org/doc/latest/manual/sql-execution/importing/)
- [Data type conversion](https://www.jooq.org/doc/latest/manual/sql-execution/fetching/data-type-conversion/)
- [DDL statement support](https://www.jooq.org/doc/latest/manual/sql-building/ddl-statements/)
- [DML statement support](https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/)
- [Diagnostics](https://www.jooq.org/doc/latest/manual/sql-execution/diagnostics/)
- [Dialect agnosticity for 30+ RDBMS](https://www.jooq.org/download/#databases)
- [Embedded types](https://www.jooq.org/doc/latest/manual/code-generation/codegen-embeddable-types/)
- [Formatting and pretty printing](https://www.jooq.org/doc/latest/manual/sql-building/queryparts/pretty-printing/)
- [Implicit joins](https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/implicit-join/)
- [Kotlin support](https://www.jooq.org/doc/latest/manual/sql-building/kotlin-sql-building/)
- [Mapping](https://www.jooq.org/doc/latest/manual/sql-execution/fetching/recordmapper/)
- [Meta data API](https://www.jooq.org/doc/latest/manual/sql-execution/meta-data/)
- [Mocking API for JDBC](https://www.jooq.org/doc/latest/manual/sql-execution/mocking-connection/)
- [Model API for use in traversal and replacement](https://www.jooq.org/doc/latest/manual/sql-building/model-api/)
- [`MULTISET` and `ROW` nested collections and records](https://blog.jooq.org/jooq-3-15s-new-multiset-operator-will-change-how-you-think-about-sql/)
- [Multitenancy](https://www.jooq.org/doc/latest/manual/sql-building/dsl-context/custom-settings/settings-render-mapping/)
- [Parser (and translator)](https://www.jooq.org/doc/latest/manual/sql-building/sql-parser/)
- [Pattern based transformation](https://www.jooq.org/doc/latest/manual/sql-building/queryparts/sql-transformation/transform-patterns/)
- [Plain SQL templating](https://www.jooq.org/doc/latest/manual/sql-building/plain-sql-templating/)
- [Procedural logic API](https://blog.jooq.org/vendor-agnostic-dynamic-procedural-logic-with-jooq/)
- [Reactive support via R2DBC](https://www.jooq.org/doc/latest/manual/sql-execution/fetching/reactive-fetching/)
- [Readonly columns](https://www.jooq.org/doc/latest/manual/code-generation/codegen-advanced/codegen-config-database/codegen-database-readonly-columns/)
- [Scala support](https://www.jooq.org/doc/latest/manual/sql-building/scala-sql-building/)
- [Schema diff](https://www.jooq.org/doc/latest/manual/sql-building/schema-diff/)
- [SQL transformation](https://www.jooq.org/doc/latest/manual/sql-building/queryparts/sql-transformation/)
- [SQL translation](https://www.jooq.org/translate/)
- [Stored procedure support](https://blog.jooq.org/the-best-way-to-call-stored-procedures-from-java-with-jooq/)
- [Transaction API](https://www.jooq.org/doc/latest/manual/sql-execution/transaction-management/)
- [UpdatableRecords for simplified CRUD, with opt-in optimistic locking](https://www.jooq.org/doc/latest/manual/sql-execution/crud-with-updatablerecords/simple-crud/)
- And much more

Examples
========

Typesafe, embedded SQL
----------------------

jOOQ's main feature is typesafe, embedded SQL, allowing for IDE auto completion of SQL syntax...

![image](https://github.com/jOOQ/jOOQ/assets/734593/a62305d7-c8a7-4a32-aa32-30708f70337d)

... as well as of schema meta data:

![image](https://github.com/jOOQ/jOOQ/assets/734593/a8e23067-254c-4a03-89b2-82985325ee69)

This allows for preventing errors of various types, including typos of identifiers:

![image](https://github.com/jOOQ/jOOQ/assets/734593/d2659a0c-7d45-4851-9455-81ac4bc18485)

Or data type mismatches:

![image](https://github.com/jOOQ/jOOQ/assets/734593/8d230f16-ce82-4de8-88b2-64997451ebfe)

[The examples are from the code generation blog post](https://blog.jooq.org/why-you-should-use-jooq-with-code-generation/).

A more powerful example using nested collections
------------------------------------------------

For many more examples, [please have a look at the demo](https://github.com/jOOQ/demo). A key example showing jOOQ's various strengths is from the [`MULTISET` operator announcement blog post](https://blog.jooq.org/jooq-3-15s-new-multiset-operator-will-change-how-you-think-about-sql/):

Given these target DTOs:

```java
record Actor(String firstName, String lastName) {}
record Film(
  String title,
  List<Actor> actors,
  List<String> categories
) {}
```

You can now write the following query to fetch films, their nested actors and their nested categorise in a single, type safe query:

```java
List<Film> result =
dsl.select(
      FILM.TITLE,
      multiset(
        select(
          FILM.actor().FIRST_NAME, 
          FILM.actor().LAST_NAME)
        .from(FILM.actor())
      ).as("actors").convertFrom(r -> r.map(mapping(Actor::new))),
      multiset(
        select(FILM.category().NAME)
        .from(FILM.category())
      ).as("categories").convertFrom(r -> r.map(Record1::value1))
   )
   .from(FILM)
   .orderBy(FILM.TITLE)
   .fetch(mapping(Film::new));
```

The query is completely type safe. Change a column type, name, or the target DTO, and it will stop compiling! Trust only your own eyes:

![multiset](https://github.com/jOOQ/jOOQ/assets/734593/948f8e62-2a93-4152-86d6-42a6eceb7133)

And here you see the nested result in action from the logs:

![execute](https://github.com/jOOQ/jOOQ/assets/734593/ba2f2a9f-218c-4ec9-8fb2-c2b8b7df2f4d)

How does it work? Look at this annotated example:


```java
List<Film> result =
dsl.select(
      FILM.TITLE,

      // MULTISET is a standard SQL operator that allows for nesting collections
      // directly in SQL. It is either
      // - supported natively
      // - emulated using SQL/JSON or SQL/XML
      multiset(

        // Implicit path based joins allow for simpler navigation of foreign
        // key relationships.
        select(
          FILM.actor().FIRST_NAME, 
          FILM.actor().LAST_NAME)

        // Implicit correlation to outer queries allows for avoiding repetitive
        // writing of predicates.
        .from(FILM.actor())

      // Ad-hoc conversion allows for mapping structural Record2<String, String>
      // types to your custom DTO using constructor references
      ).as("actors").convertFrom(r -> r.map(mapping(Actor::new))),
      multiset(
        select(FILM.category().NAME)
        .from(FILM.category())
      ).as("categories").convertFrom(r -> r.map(Record1::value1))
   )
   .from(FILM)
   .orderBy(FILM.TITLE)
   .fetch(mapping(Film::new));
```

The generated SQL query might look like this, in PostgreSQL:

```sql
select
  film.title,
  (
    select coalesce(
      jsonb_agg(jsonb_build_object(
        'first_name', t.first_name,
        'last_name', t.last_name
      )),
      jsonb_build_array()
    )
    from (
      select
        alias_78509018.first_name, 
        alias_78509018.last_name
      from (
        film_actor
          join actor as alias_78509018
            on film_actor.actor_id = alias_78509018.actor_id
        )
      where film_actor.film_id = film.film_id
    ) as t
  ) as actors,
  (
    select coalesce(
      jsonb_agg(jsonb_build_object('name', t.name)),
      jsonb_build_array()
    )
    from (
      select alias_130639425.name
      from (
        film_category
          join category as alias_130639425
            on film_category.category_id = alias_130639425.category_id
        )
      where film_category.film_id = film.film_id
    ) as t
  ) as categories
from film
order by film.title
```

This particular example is explained more in detail in the [`MULTISET` operator announcement blog post](https://blog.jooq.org/jooq-3-15s-new-multiset-operator-will-change-how-you-think-about-sql/). For many more examples, [please have a look at the demo](https://github.com/jOOQ/demo).