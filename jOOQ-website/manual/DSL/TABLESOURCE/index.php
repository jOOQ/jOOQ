
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Table sources";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							When OLTP selects/updates/inserts/deletes records in single tables, OLAP is all about
							creating custom table sources or ad-hoc row types
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/TABLESOURCE/">Table sources</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Complete SELECT syntax" href="<?=$root?>/manual/DSL/SELECT/">previous</a> : <a title="Next section: Conditions" href="<?=$root?>/manual/DSL/CONDITION/">next</a></td>
</tr>
</table>
							<h2>Create complex and nested table sources with jOOQ</h2>
							<p>
								In the <a href="http://en.wikipedia.org/wiki/Relational_model" title="The Relational Data Model">relational data model</a>,
								there are many operations performed on entities, i.e. tables in order to join them together
								before applying predicates, renaming operations and projections. Apart from the convenience
								methods for joining table sources in the
								<a href="<?=$root?>/manual/DSL/SELECT/" title="jOOQ Manual reference: Complete SELECT syntax">manual's section about the full SELECT syntax</a>,
								the <a href="http://www.jooq.org/javadoc/latest/org/jooq/Table.html" title="Internal API reference: org.jooq.Table">Table</a> type itself provides a
								rich API for creating joined table sources. See an extract of the Table API:
							</p>
<pre class="prettyprint lang-java">// These are the various supported JOIN clauses. These JOIN types
// are followed by an additional ON / ON KEY / USING clause
TableOnStep join(TableLike&lt;?&gt; table);
TableOnStep join(String sql);
TableOnStep join(String sql, Object... bindings);

// All other JOIN types are equally overloaded with "String sql" convenience methods...
TableOnStep  leftOuterJoin(TableLike&lt;?&gt; table);
TableOnStep rightOuterJoin(TableLike&lt;?&gt; table);
TableOnStep  fullOuterJoin(TableLike&lt;?&gt; table);

// These JOIN types don't take any additional clause
Table&lt;Record&gt;             crossJoin(TableLike&lt;?&gt; table);
Table&lt;Record&gt;           naturalJoin(TableLike&lt;?&gt; table);
Table&lt;Record&gt;  naturalLeftOuterJoin(TableLike&lt;?&gt; table);
Table&lt;Record&gt; naturalRightOuterJoin(TableLike&lt;?&gt; table);

// Oracle and SQL Server also know PIVOT / UNPIVOT clauses for transforming a
// table into another one using a list of PIVOT values
PivotForStep pivot(Field&lt;?&gt;... aggregateFunctions);
PivotForStep pivot(Collection&lt;? extends Field&lt;?&gt;&gt; aggregateFunctions);

// Relational division can be applied to a table, transforming it into a
// "quotient" using an intuitive syntax
DivideByOnStep divideBy(Table&lt;?&gt; divisor);</pre>

							<p>
								The <a href="http://www.jooq.org/javadoc/latest/org/jooq/TableOnStep.html" title="Internal API reference: org.jooq.TableOnStep">TableOnStep</a> type
								contains methods for constructing the ON / ON KEY / USING clauses
							</p>

<pre class="prettyprint lang-java">// The ON clause is the most widely used JOIN condition. Provide arbitrary conditions as arguments
TableOnConditionStep on(Condition... conditions);
TableOnConditionStep on(String sql);
TableOnConditionStep on(String sql, Object... bindings);

// The USING clause is also part of the SQL standard. Use this if joined tables contain identical field names.
// The USING clause is simulated in databases that do not support it.
Table&lt;Record&gt; using(Field&lt;?&gt;... fields);
Table&lt;Record&gt; using(Collection&lt;? extends Field&lt;?&gt;&gt; fields);

// The ON KEY clause is a "synthetic" clause that does not exist in any SQL dialect. jOOQ usually has all
// foreign key relationship information to dynamically render "ON [ condition ... ]" clauses
TableOnConditionStep onKey() throws DataAccessException;
TableOnConditionStep onKey(TableField&lt;?, ?&gt;... keyFields) throws DataAccessException;
TableOnConditionStep onKey(ForeignKey&lt;?, ?&gt; key);</pre>

							<ul>
							
<li>
								For more details about the PIVOT clause, see the
								<a href="<?=$root?>/manual/ADVANCED/PIVOT/" title="jOOQ Manual reference: The Oracle 11g PIVOT clause">manual's section about the Oracle PIVOT syntax</a>
							
</li>
							
<li>
								For more details about the DIVIDE BY clause, see the
								<a href="<?=$root?>/manual/ADVANCED/DIVISION/" title="jOOQ Manual reference: jOOQ's relational division syntax">manual's section about the relational division syntax</a>
							
</li>
							
</ul>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/TABLESOURCE/">Table sources</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Complete SELECT syntax" href="<?=$root?>/manual/DSL/SELECT/">previous</a> : <a title="Next section: Conditions" href="<?=$root?>/manual/DSL/CONDITION/">next</a></td>
</tr>
</table>
<?php 
}
?>

