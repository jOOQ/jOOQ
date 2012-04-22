
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Nested SELECT using the EXISTS operator";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "The EXISTS operator is a bit different from all other SQL
							elements, as it cannot really be applied to any object in a DSL.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/EXISTS/">Nested SELECT using the EXISTS operator</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Nested SELECT using the IN operator" href="<?=$root?>/manual/DSL/IN/">previous</a> : <a title="Next section: Other types of nested SELECT" href="<?=$root?>/manual/DSL/NESTED/">next</a></td>
</tr>
</table>
							<h2>The EXISTS operator for use in semi-joins or anti-joins</h2>
							<p>The EXISTS operator is rather independent and can stand any place
								where there may be a new condition: </p>
							<ul>
								
<li>It may be placed right after a WHERE keyword </li>
								
<li>It may be the right-hand-side of a boolean operator</li>
								
<li>It may be placed right after a ON or HAVING keyword (although, this is less likely to be done...) </li>
							
</ul>

							<p>This is reflected by the fact that an EXISTS clause is usually
								created directly from the Factory: </p>

<pre class="prettyprint lang-java">Condition exists(Select&lt;?&gt; query);
Condition notExists(Select&lt;?&gt; query);</pre>

							<p>When you create such a Condition, it can then be connected to any
								other condition using AND, OR operators (see also the manual's section
								on
								<a href="<?=$root?>/manual/DSL/CONDITION/" title="jOOQ Manual reference: Conditions">Conditions</a>). There are also quite a few
								convenience methods, where they might be useful. For instance in the
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/Condition.html" title="Internal API reference: org.jooq.Condition">org.jooq.Condition</a> itself: </p>

<pre class="prettyprint lang-java">Condition andExists(Select&lt;?&gt; select);
Condition andNotExists(Select&lt;?&gt; select);
Condition orExists(Select&lt;?&gt; select);
Condition orNotExists(Select&lt;?&gt; select);</pre>

							<p>Or in the <a href="http://www.jooq.org/javadoc/latest/org/jooq/SelectWhereStep.html" title="Internal API reference: org.jooq.SelectWhereStep">org.jooq.SelectWhereStep</a>:</p>

<pre class="prettyprint lang-java">SelectConditionStep whereExists(Select&lt;?&gt; select);
SelectConditionStep whereNotExists(Select&lt;?&gt; select);</pre>

							<p>Or in the <a href="http://www.jooq.org/javadoc/latest/org/jooq/SelectConditionStep.html" title="Internal API reference: org.jooq.SelectConditionStep">org.jooq.SelectConditionStep</a>: </p>

<pre class="prettyprint lang-java">SelectConditionStep andExists(Select&lt;?&gt; select);
SelectConditionStep andNotExists(Select&lt;?&gt; select);
SelectConditionStep orExists(Select&lt;?&gt; select);
SelectConditionStep orNotExists(Select&lt;?&gt; select);</pre>

							<p>An example of how to use it is quickly given. Get all authors that haven't written any books: </p>
							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">SELECT *
  FROM T_AUTHOR
 WHERE NOT EXISTS (SELECT 1
                     FROM T_BOOK
                    WHERE T_BOOK.AUTHOR_ID = T_AUTHOR.ID)</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">create.select()
      .from(T_AUTHOR)
      .whereNotExists(create.selectOne()
            .from(T_BOOK)
            .where(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID)));</pre>
</td>
</tr>
</table>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/EXISTS/">Nested SELECT using the EXISTS operator</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Nested SELECT using the IN operator" href="<?=$root?>/manual/DSL/IN/">previous</a> : <a title="Next section: Other types of nested SELECT" href="<?=$root?>/manual/DSL/NESTED/">next</a></td>
</tr>
</table>
<?php 
}
?>

