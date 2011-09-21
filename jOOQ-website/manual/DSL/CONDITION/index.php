
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function printH1() {
    print "Conditions";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							The creation of conditions is the part of any database abstraction that
							attracts the most attention.
						";
}
function printContent() {
    global $root;
?>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/CONDITION/">Conditions</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/SELECT/" title="Previous section: Complete SELECT syntax">previous</a> : <a href="<?=$root?>/manual/DSL/ALIAS/" title="Next section: Aliased tables and fields">next</a></td>
</tr>
</table>
							<h2>Conditions are the SELECT's core business</h2>
							<p>In your average application, you will typically have 3-4 SQL queries
								that have quite a long list of predicates (and possibly JOINs), such
								that you start to lose track over the overall boolean expression that
								you're trying to apply.</p>
							<p>In jOOQ, most Conditions can be created and combined almost as
								easily as in SQL itself. The two main participants for creating
								Conditions are the <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Field.java" title="Internal API reference: org.jooq.Field">Field</a>, 
								which is typically a participant of a
								condition, and the <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Condition.java" title="Internal API reference: org.jooq.Condition">Condition</a> 
								itself: </p>
							<pre class="prettyprint lang-java">
public interface Condition {
    Condition and(Condition other);
    Condition and(String sql);
    Condition and(String sql, Object... bindings);
    Condition andNot(Condition other);
    Condition andExists(Select&lt;?&gt; select);
    Condition andNotExists(Select&lt;?&gt; select);
    Condition or(Condition other);
    Condition or(String sql);
    Condition or(String sql, Object... bindings);
    Condition orNot(Condition other);
    Condition orExists(Select&lt;?&gt; select);
    Condition orNotExists(Select&lt;?&gt; select);
    Condition not();
}</pre>

							<p>The above example describes the essence of boolean logic in jOOQ. As
								soon as you have a Condition object, you can connect that to other
								Conditions, which will then give you a combined condition with exactly
								the same properties. There are also convenience methods to create an
								EXISTS clause and connect it to an existing condition. In order to
								create a new Condition you are going to depart from a Field in most
								cases. Here are some important API elements in the Field interface:
							</p>
							
							<pre class="prettyprint lang-java">
public interface Field&lt;T&gt; {
    Condition isNull();
    Condition isNotNull();
    Condition like(T value);
    Condition notLike(T value);
    Condition in(T... values);
    Condition in(Select&lt;?&gt; query);
    Condition notIn(Collection&lt;T&gt; values);
    Condition notIn(T... values);
    Condition notIn(Select&lt;?&gt; query);
    Condition in(Collection&lt;T&gt; values);
    Condition between(T minValue, T maxValue);
    Condition equal(T value);
    Condition equal(Field&lt;T&gt; field);
    Condition equal(Select&lt;?&gt; query);
    Condition equalAny(Select&lt;?&gt; query);
    Condition equalSome(Select&lt;?&gt; query);
    Condition equalAll(Select&lt;?&gt; query);
    Condition notEqual(T value);
    Condition notEqual(Field&lt;T&gt; field);
    Condition notEqual(Select&lt;?&gt; query);
    Condition notEqualAny(Select&lt;?&gt; query);
    Condition notEqualSome(Select&lt;?&gt; query);
    Condition notEqualAll(Select&lt;?&gt; query);
    Condition lessThan(T value);
    Condition lessOrEqual(T value);
    Condition greaterThan(T value);
    Condition greaterOrEqual(T value);
}</pre>

							<p>As you see in the partially displayed API above, you can compare a
								Field either with other Fields, with constant values (which is a
								shortcut for calling Factory.val(T value)), or with a nested SELECT
								statement. See some more 
								<a href="<?=$root?>/manual/DSL/NESTED/" title="jOOQ Manual reference: Other types of nested SELECT">Examples of nested SELECTs</a>. </p>
							<p>Combining the API of Field and Condition you can express complex predicates like this: </p>
							
							<pre class="prettyprint lang-sql">
(T_BOOK.TYPE_CODE IN (1, 2, 5, 8, 13, 21)       AND T_BOOK.LANGUAGE = 'DE') OR
(T_BOOK.TYPE_CODE IN (2, 3, 5, 7, 11, 13)       AND T_BOOK.LANGUAGE = 'FR') OR
(T_BOOK.TYPE_CODE IN (SELECT CODE FROM T_TYPES) AND T_BOOK.LANGUAGE = 'EN')</pre>

							<p>Just write: </p>
							<pre class="prettyprint lang-java">
TBook.TYPE_CODE.in(1, 2, 5, 8, 13, 21)                      .and(TBook.LANGUAGE.equal("DE")).or(
TBook.TYPE_CODE.in(2, 3, 5, 7, 11, 13)                      .and(TBook.LANGUAGE.equal("FR")).or(
TBook.TYPE_CODE.in(create.select(TTypes.CODE).from(T_TYPES)).and(TBook.LANGUAGE.equal("EN"))));</pre>
						<br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/CONDITION/">Conditions</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/SELECT/" title="Previous section: Complete SELECT syntax">previous</a> : <a href="<?=$root?>/manual/DSL/ALIAS/" title="Next section: Aliased tables and fields">next</a></td>
</tr>
</table>
<?php 
}
?>

