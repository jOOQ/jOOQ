
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Complete SELECT syntax";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							A SELECT statement is more than just the R in CRUD. It allows for
							transforming your relational data into any other form using concepts
							such as equi-join, semi-join, anti-join, outer-join and much more. jOOQ
							helps you think in precisely those relational concepts.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/SELECT/">Complete SELECT syntax</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: DSL or fluent API. Where SQL meets Java" href="<?=$root?>/manual/DSL/">previous</a> : <a title="Next section: Conditions" href="<?=$root?>/manual/DSL/CONDITION/">next</a></td>
</tr>
</table>
							<h2>SELECT from anonymous or ad-hoc types</h2>
							<p>When you don't just perform CRUD (i.e. SELECT * FROM your_table WHERE ID = ?),
							you're usually generating new types using custom projections. With jOOQ, this is
							as intuitive, as if using SQL directly. A more or less complete example of the "standard" SQL syntax, plus
							some extensions, is provided by a query like this:
							</p>
							
<pre class="prettyprint lang-sql">-- get all authors' first and last names, and the number 
-- of books they've written in German, if they have written
-- more than five books in German in the last three years 
-- (from 2011), and sort those authors by last names
-- limiting results to the second and third row, locking
-- the rows for a subsequent update... whew!

  SELECT T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, COUNT(*)
    FROM T_AUTHOR
    JOIN T_BOOK ON T_AUTHOR.ID = T_BOOK.AUTHOR_ID
   WHERE T_BOOK.LANGUAGE = 'DE'
     AND T_BOOK.PUBLISHED &gt; '2008-01-01'
GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
  HAVING COUNT(*) &gt; 5
ORDER BY T_AUTHOR.LAST_NAME ASC NULLS FIRST
   LIMIT 2 
  OFFSET 1
     FOR UPDATE</pre>			
     
     						<p>So that's daily business. How to do it with jOOQ: When you first create a SELECT statement using the Factory's select() methods </p>
<pre class="prettyprint lang-java">SelectFromStep select(Field&lt;?&gt;... fields);

// Example:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count());</pre>

							<p>
								jOOQ will return an "intermediary" type to you, representing the
								SELECT statement about to be created (by the way, check out the
								section on <a href="<?=$root?>/manual/DSL/FUNCTIONS/" title="jOOQ Manual reference: Functions and aggregate operators">aggregate operators</a>
								 to learn more about the COUNT(*)
								function). This type is the 
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectFromStep.java" title="Internal API reference: org.jooq.SelectFromStep">org.jooq.SelectFromStep</a>. 
								When you have a reference
								to this type, you may add a FROM clause, although that clause is
								optional. This is reflected by the fact, that the SelectFromStep type
								extends 
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectJoinStep.java" title="Internal API reference: org.jooq.SelectJoinStep">org.jooq.SelectJoinStep</a>, 
								which allows for adding the subsequent
								clauses. Let's say you do decide to add a FROM clause, then you can
								use this method for instance:
							</p>			
<pre class="prettyprint lang-java">SelectJoinStep from(TableLike&lt;?&gt;... table);

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR);</pre>

							<p>After adding the table-like structures (mostly just Tables) to
								select from, you may optionally choose to add a JOIN clause, as the
								type returned by jOOQ is the step where you can add JOINs. Again,
								adding these clauses is optional, as the 
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectJoinStep.java" title="Internal API reference: org.jooq.SelectJoinStep">org.jooq.SelectJoinStep</a> extends
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectWhereStep.java" title="Internal API reference: org.jooq.SelectWhereStep">org.jooq.SelectWhereStep</a>. 
								But let's say we add a JOIN: </p>
<pre class="prettyprint lang-java">// These join types are supported
SelectOnStep                    join(Table&lt;?&gt; table);
SelectOnStep           leftOuterJoin(Table&lt;?&gt; table);
SelectOnStep          rightOuterJoin(Table&lt;?&gt; table);
SelectOnStep           fullOuterJoin(Table&lt;?&gt; table);
SelectJoinStep             crossJoin(Table&lt;?&gt; table);
SelectJoinStep           naturalJoin(Table&lt;?&gt; table);
SelectJoinStep  naturalLeftOuterJoin(Table&lt;?&gt; table);
SelectJoinStep naturalRightOuterJoin(Table&lt;?&gt; table);

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK);</pre>

							<p>Now, if you do add a JOIN clause, you have to specify the JOIN .. ON
								condition before you can add more clauses. That's not an optional step
								for some JOIN types. This is reflected by the fact that 
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectOnStep.java" title="Internal API reference: org.jooq.SelectOnStep">org.jooq.SelectOnStep</a>
								is a top-level interface. </p>
								
<pre class="prettyprint lang-java">// These join conditions are supported
SelectJoinStep    on(Condition... conditions);
SelectJoinStep using(Field&lt;?&gt;... fields);

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK).on(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID));</pre>

							<p>See the section about 
								<a href="<?=$root?>/manual/DSL/CONDITION/" title="jOOQ Manual reference: Conditions">Conditions</a> 
								to learn more about the many ways
								to create Conditions in jOOQ. Now we're half way through. As you can
								see above, we're back to the SelectJoinStep. This means, we can
								re-iterate and add another JOIN clause, just like in SQL. Or we go on
								to the next step, adding conditions in the 
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectWhereStep.java" title="Internal API reference: org.jooq.SelectWhereStep">org.jooq.SelectWhereStep</a>: </p>
<pre class="prettyprint lang-java">SelectConditionStep where(Condition... conditions);

// The example, continued:
create.select(TAuthor.FIRST_NAME, TAuthor.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK).on(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID))
      .where(T_BOOK.LANGUAGE.equal("DE"));</pre>

							<p>Now the returned type 
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectConditionStep.java" title="Internal API reference: org.jooq.SelectConditionStep">org.jooq.SelectConditionStep</a> is a special one, where
								you can add more conditions to the already existing WHERE clause.
								Every time you add a condition, you will return to that
								SelectConditionStep, as the number of additional conditions is
								unlimited. Note that of course you can also just add a single combined
								condition, if that is more readable or suitable for your use-case.
								Here's how we add another condition: </p>
<pre class="prettyprint lang-java">SelectConditionStep and(Condition condition);

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK).on(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID))
      .where(T_BOOK.LANGUAGE.equal("DE"))
      .and(T_BOOK.PUBLISHED.greaterThan(parseDate('2008-01-01')));</pre>

							<p>Let's assume we have that method parseDate() creating a
								<a href="http://download.oracle.com/javase/6/docs/api/java/sql/Date.html" title="External API reference: java.sql.Date">java.sql.Date</a> for us. 
								Then we'll continue adding the GROUP BY clause
							</p>
<pre class="prettyprint lang-java">SelectHavingStep groupBy(Field&lt;?&gt;... fields);

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK).on(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID))
      .where(T_BOOK.LANGUAGE.equal("DE"))
      .and(T_BOOK.PUBLISHED.greaterThan(parseDate('2008-01-01')))
      .groupBy(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME);</pre>
      
      						<p>and the HAVING clause: </p>
<pre class="prettyprint lang-java">SelectOrderByStep having(Condition... conditions);

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK).on(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID))
      .where(T_BOOK.LANGUAGE.equal("DE"))
      .and(T_BOOK.PUBLISHED.greaterThan(parseDate('2008-01-01')))
      .groupBy(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME)
      .having(count().greaterThan(5));</pre>

							<p>and the ORDER BY clause. Some RDBMS support NULLS FIRST and NULLS
								LAST extensions to the ORDER BY clause. If this is not supported by
								the RDBMS, then the behaviour is simulated with an additional CASE
								WHEN ... IS NULL THEN 1 ELSE 0 END clause. </p>
<pre class="prettyprint lang-java">SelectLimitStep orderBy(Field&lt;?&gt;... fields);

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK).on(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID))
      .where(T_BOOK.LANGUAGE.equal("DE"))
      .and(T_BOOK.PUBLISHED.greaterThan(parseDate('2008-01-01')))
      .groupBy(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME)
      .having(count().greaterThan(5))
      .orderBy(T_AUTHOR.LAST_NAME.asc().nullsFirst());</pre>

							<p>and finally the LIMIT clause. Most dialects have a means of limiting
								the number of result records (except Oracle). Some even support having
								an OFFSET to the LIMIT clause. Even if your RDBMS does not support the
								full LIMIT ... OFFSET ... (or TOP ... START AT ..., or FETCH FIRST ... ROWS ONLY, etc) 
								clause, jOOQ will simulate the LIMIT clause using nested selects and filtering on
								ROWNUM (for Oracle), or on ROW_NUMBER() (for DB2 and SQL
								Server): </p>
<pre class="prettyprint lang-java">SelectFinalStep limit(int offset, int numberOfRows);

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK).on(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID))
      .where(T_BOOK.LANGUAGE.equal("DE"))
      .and(T_BOOK.PUBLISHED.greaterThan(parseDate('2008-01-01')))
      .groupBy(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME)
      .having(count().greaterThan(5))
      .orderBy(T_AUTHOR.LAST_NAME.asc().nullsFirst())
      .limit(1, 2);</pre>

							<p>In the final step, there are some proprietary extensions available
								only in some RDBMS. One of those extensions are the FOR UPDATE
								(supported in most RDBMS) and FOR SHARE clauses (supported only in
								MySQL and Postgres): </p>
<pre class="prettyprint lang-java">SelectFinalStep forUpdate();

// The example, continued:
create.select(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, count())
      .from(T_AUTHOR)
      .join(T_BOOK).on(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID))
      .where(T_BOOK.LANGUAGE.equal("DE"))
      .and(T_BOOK.PUBLISHED.greaterThan(parseDate('2008-01-01')))
      .groupBy(T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME)
      .having(count().greaterThan(5))
      .orderBy(T_AUTHOR.LAST_NAME.asc().nullsFirst())
      .limit(1, 2)
      .forUpdate();</pre>

							<p>
								Now the most relevant super-type of the object we have just created is 
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Select.java" title="Internal API reference: org.jooq.Select">org.jooq.Select</a>. 
								This type can be reused in various expressions such as in the
								<a href="<?=$root?>/manual/DSL/UNION/" title="jOOQ Manual reference: UNION and other set operations">UNION and other set operations</a>, 
								<a href="<?=$root?>/manual/DSL/EXISTS/" title="jOOQ Manual reference: Nested SELECT using the EXISTS operator">Nested select statements using the EXISTS operator</a>, 
								etc. If you just want to execute this select
								statement, you can choose any of these methods as discussed in the 
								section about the <a href="<?=$root?>/manual/JOOQ/ResultQuery/" title="jOOQ Manual reference: ResultQuery and various ways of fetching data">ResultQuery</a>:
							</p>
							
<pre class="prettyprint lang-java">// Just execute the query.
int execute();

// Execute the query and retrieve the results
Result&lt;Record&gt; fetch();

// Execute the query and retrieve the first Record
Record fetchAny();

// Execute the query and retrieve the single Record
// An Exception is thrown if more records were available
Record fetchOne();

// [...]</pre>


							<h2>SELECT from single physical tables</h2>
							<p>A very similar, but limited API is available, if you want to select from single
								physical tables in order to retrieve TableRecords or even
								UpdatableRecords (see also the manual's section on 
								<a href="<?=$root?>/manual/JOOQ/Query/" title="jOOQ Manual reference: The Query and its various subtypes">SelectQuery vs SimpleSelectQuery</a>). 
								The decision, which type of select to create is
								already made at the very first step, when you create the SELECT
								statement with the Factory: </p>
								
							<pre class="prettyprint lang-java">public &lt;R extends Record&gt; SimpleSelectWhereStep&lt;R&gt; selectFrom(Table&lt;R&gt; table);</pre>
							<p>As you can see, there is no way to further restrict/project the selected
								fields. This just selects all known TableFields in the supplied Table,
								and it also binds &lt;R extends Record&gt; to your Table's associated
								Record. An example of such a Query would then be: </p>
<pre class="prettyprint lang-java">TBook book = create.selectFrom(T_BOOK)
                   .where(TBook.LANGUAGE.equal("DE"))
                   .orderBy(TBook.TITLE)
                   .fetchAny();</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/SELECT/">Complete SELECT syntax</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: DSL or fluent API. Where SQL meets Java" href="<?=$root?>/manual/DSL/">previous</a> : <a title="Next section: Conditions" href="<?=$root?>/manual/DSL/CONDITION/">next</a></td>
</tr>
</table>
<?php 
}
?>

