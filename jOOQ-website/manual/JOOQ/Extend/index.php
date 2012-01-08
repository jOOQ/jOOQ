
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Extend jOOQ with custom types";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "Maybe jOOQ is missing functionality that you would like to see,
							or you can't wait for the next release... In this case, you can extend
							any of the following open jOOQ implementation classes";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/Extend/">Extend jOOQ with custom types</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Serializability of QueryParts and Results" href="<?=$root?>/manual/JOOQ/Serializability/">previous</a> : <a title="Next section: Meta model code generation" href="<?=$root?>/manual/META/">next</a></td>
</tr>
</table>
							<h2>Write your own QueryPart implementations</h2>
							<p>If a SQL clause is too complex to express with jOOQ, you can extend
								either one of the following types for use directly in a jOOQ query:</p>
<pre class="prettyprint lang-java">public abstract class CustomField&lt;T&gt; extends AbstractField&lt;T&gt; {
  // [...]
}
public abstract class CustomCondition extends AbstractCondition {
  // [...]
}</pre>

							<p>These two classes are declared public and covered by integration
								tests. When you extend these classes, you will have to provide your
								own implementations for the <a href="<?=$root?>/manual/JOOQ/QueryPart/" title="jOOQ Manual reference: QueryParts and the global architecture">QueryParts</a>'
								bind(<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/BindContext.java" title="Internal API reference: org.jooq.BindContext">BindContext</a>) and
								toSQL(<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/RenderContext.java" title="Internal API reference: org.jooq.RenderContext">RenderContext</a>) methods:</p>
<pre class="prettyprint lang-java">// This method must produce valid SQL. If your QueryPart contains other QueryParts, you may delegate SQL code generation to them
// in the correct order, passing the render context.
//
// If context.inline() is true, you must inline all bind variables
// If context.inline() is false, you must generate ? for your bind variables
public void toSQL(RenderContext context);

// This method must bind all bind variables to a PreparedStatement. If your QueryPart contains other QueryParts, you may delegate
// variable binding to them in the correct order, passing the bind context.
//
// Every QueryPart must ensure, that it starts binding its variables at context.nextIndex().
public void bind(BindContext context) throws DataAccessException;</pre>

							<p>The above contract may be a bit tricky to understand at first. The
								best thing is to check out jOOQ source code and have a look at a
								couple of QueryParts, to see how it's done.</p>
							<h2>Plain SQL as an alternative</h2>
							<p>If you don't need integration of rather complex QueryParts into
								jOOQ, then you might be safer using simple
								<a href="<?=$root?>/manual/DSL/SQL/" title="jOOQ Manual reference: When it's just easier: Plain SQL">Plain SQL</a> functionality,
								where you can provide jOOQ with a simple String representation of your
								embedded SQL. </p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/Extend/">Extend jOOQ with custom types</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Serializability of QueryParts and Results" href="<?=$root?>/manual/JOOQ/Serializability/">previous</a> : <a title="Next section: Meta model code generation" href="<?=$root?>/manual/META/">next</a></td>
</tr>
</table>
<?php 
}
?>

