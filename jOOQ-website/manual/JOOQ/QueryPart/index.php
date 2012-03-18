
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "QueryParts and the global architecture";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "When constructing Query objects in jOOQ, everything is
							considered a QueryPart. The purpose of this quickly becomes clear when
							checking out the QueryPart API essentials";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/QueryPart/">QueryParts and the global architecture</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Bind values" href="<?=$root?>/manual/JOOQ/BindValues/">previous</a> : <a title="Next section: Serializability of QueryParts and Results" href="<?=$root?>/manual/JOOQ/Serializability/">next</a></td>
</tr>
</table>
							<h2>Everything is a QueryPart</h2>
							<p>
								A
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/Query.html" title="Internal API reference: org.jooq.Query">org.jooq.Query</a>
								and all its contained objects is a
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/QueryPart.html" title="Internal API reference: org.jooq.QueryPart">org.jooq.QueryPart</a>.
								QueryParts essentially provide this functionality:
							</p>
							<ul>
								
<li>they can render SQL using the toSQL(RenderContext) method</li>
								
<li>they can bind variables using the bind(BindContext) method</li>
							
</ul>

							<p>Both of these methods are contained in jOOQ's internal API's
							   <a href="http://www.jooq.org/javadoc/latest/org/jooq/QueryPartInternal.html" title="Internal API reference: org.jooq.QueryPartInternal">org.jooq.QueryPartInternal</a>, which is
							   internally implemented by every QueryPart. QueryPart internals are best
							   illustrated with an example.</p>

						   <h2>Example: CompareCondition</h2>
						   <p>A simple example can be provided by checking out jOOQ's internal
								representation of a
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/impl/CompareCondition.html" title="Internal API reference: org.jooq.impl.CompareCondition">org.jooq.impl.CompareCondition</a>.
								It is used for any condition
								comparing two fields as for example the T_AUTHOR.ID = T_BOOK.AUTHOR_ID
								condition here: </p>
<pre class="prettyprint lang-sql">-- [...]
FROM T_AUTHOR
JOIN T_BOOK ON T_AUTHOR.ID = T_BOOK.AUTHOR_ID
-- [...]</pre>

							<p>This is how jOOQ implements such a condition: </p>

<pre class="prettyprint lang-java">@Override
public final void bind(BindContext context) throws SQLException {
    // The CompareCondition itself does not bind any variables.
    // But the two fields involved in the condition might do so...
    context.bind(field1).bind(field2);
}

@Override
public final void toSQL(RenderContext context) {
    // The CompareCondition delegates rendering of the Fields to the Fields
    // themselves and connects them using the Condition's comparator operator:
    context.sql(field1)
           .sql(" ");

    // If the second field is null, some convenience behaviour can be
    // implemented here
    if (field2.isNullLiteral()) {
        switch (comparator) {
            case EQUALS:
                context.sql("is null");
                break;

            case NOT_EQUALS:
                context.sql("is not null");
                break;

            default:
                throw new IllegalStateException("Cannot compare null with " + comparator);
        }
    }

    // By default, also delegate the right hand side's SQL rendering to the
    // underlying field
    else {
        context.sql(comparator.toSQL())
               .sql(" ")
               .sql(field2);
    }
}
</pre>
							<p>For more complex examples, please refer to the codebase, directly</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/QueryPart/">QueryParts and the global architecture</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Bind values" href="<?=$root?>/manual/JOOQ/BindValues/">previous</a> : <a title="Next section: Serializability of QueryParts and Results" href="<?=$root?>/manual/JOOQ/Serializability/">next</a></td>
</tr>
</table>
<?php 
}
?>

