
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function printH1() {
    print "Nested SELECT using the EXISTS operator";
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
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/EXISTS/">Nested SELECT using the EXISTS operator</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/IN/" title="Previous section: Nested SELECT using the IN operator">previous</a> : <a href="<?=$root?>/manual/DSL/NESTED/" title="Next section: Other types of nested SELECT">next</a></td>
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
								created directly from the Factory. While this is more verbose than all
								the other SQL constructs, there is no other way, when static QueryPart
								creation is not an option (for now). Here is how it's done in the
								Factory: </p>
								
							<pre class="prettyprint lang-java">
Condition exists(Select&lt;?&gt; query);
Condition notExists(Select&lt;?&gt; query);</pre>

							<p>When you create such a Condition, it can then be connected to any
								other condition using AND, OR operators (see also the manual's section
								on 
								<a href="<?=$root?>/manual/DSL/CONDITION/" title="jOOQ Manual reference: Conditions">Conditions</a>). Because of this verbosity, there are also quite a few
								convenience methods, where they might be useful. For instance in the
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Condition.java" title="Internal API reference: org.jooq.Condition">org.jooq.Condition</a> itself: </p>
								
							<pre class="prettyprint lang-java">
Condition andExists(Select&lt;?&gt; select);
Condition andNotExists(Select&lt;?&gt; select);
Condition orExists(Select&lt;?&gt; select);
Condition orNotExists(Select&lt;?&gt; select);</pre>

							<p>Or in the <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectWhereStep.java" title="Internal API reference: org.jooq.SelectWhereStep">org.jooq.SelectWhereStep</a>:</p>
							
							<pre class="prettyprint lang-java">
SelectConditionStep whereExists(Select&lt;?&gt; select);
SelectConditionStep whereNotExists(Select&lt;?&gt; select);</pre>

							<p>Or in the <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/SelectConditionStep.java" title="Internal API reference: org.jooq.SelectConditionStep">org.jooq.SelectConditionStep</a>: </p>
							
							<pre class="prettyprint lang-java">
SelectConditionStep andExists(Select&lt;?&gt; select);
SelectConditionStep andNotExists(Select&lt;?&gt; select);
SelectConditionStep orExists(Select&lt;?&gt; select);
SelectConditionStep orNotExists(Select&lt;?&gt; select);</pre>

							<p>An example of how to use it is quickly given. Get all authors that haven't written any books: </p>
							<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td width="50%" class="left">
<pre class="prettyprint lang-sql">
SELECT *
  FROM T_AUTHOR
 WHERE NOT EXISTS (SELECT 1 
                     FROM T_BOOK 
                    WHERE T_BOOK.AUTHOR_ID = T_AUTHOR.ID)</pre>
</td><td width="50%" class="right">
<pre class="prettyprint lang-java">
create.select()
      .from(T_AUTHOR)
      .whereNotExists(create.select(1)
                  .from(T_BOOK)
                  .where(TBook.AUTHOR_ID.equal(T_AUTHOR.ID)));</pre>
</td>
</tr>
</table>
						<br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/EXISTS/">Nested SELECT using the EXISTS operator</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/IN/" title="Previous section: Nested SELECT using the IN operator">previous</a> : <a href="<?=$root?>/manual/DSL/NESTED/" title="Next section: Other types of nested SELECT">next</a></td>
</tr>
</table>
<?php 
}
?>

