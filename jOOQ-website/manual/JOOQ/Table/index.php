
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Tables and Fields";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Tables and their Fields are probably the most important objects in
							jOOQ. Tables represent any entity in your underlying RDBMS, that holds
							data for selection, insertion, updates, and deletion. In other words,
							views are also considered tables by jOOQ.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/Table/">Tables and Fields</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Factory class" href="<?=$root?>/manual/JOOQ/Factory/">previous</a> : <a title="Next section: Results and Records" href="<?=$root?>/manual/JOOQ/Result/">next</a></td>
</tr>
</table>
							<h2>The Table</h2>
							<p>The formal definition of a <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Table.java" title="Internal API reference: org.jooq.Table">org.jooq.Table</a> starts with </p>
							<pre class="prettyprint lang-java">public interface Table&lt;R extends Record&gt; // [...]</pre>
							<p>
								This means that every table is associated with a subtype of the
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Record.java" title="Internal API reference: org.jooq.Record">org.jooq.Record</a>
								class (see also
								<a href="<?=$root?>/manual/JOOQ/Result/" title="jOOQ Manual reference: Results and Records">Results and Records</a>
								). For anonymous or ad-hoc tables,
								&lt;R&gt; will always bind to Record itself.
							</p>
							<p>
								Unlike in the
								<a href="http://download.oracle.com/javaee/6/tutorial/doc/gjitv.html" title="Tutorial about JPA CriteriaQuery">JPA CriteriaQuery API</a>,
								this generic type
								&lt;R&gt;
								is not given so much importance as far as
								type-safety is concerned.
								SQL itself is highly typesafe. You have
								incredible flexibility of creating anonymous or ad-hoc
								types and
								reusing them from
								<a href="<?=$root?>/manual/DSL/NESTED/" title="jOOQ Manual reference: Other types of nested SELECT">NESTED SELECT statements</a>
								or from many other
								use-cases. There is no way that this typesafety can be
								mapped to the Java world in a convenient way. If
								&lt;R&gt; would play a role as important
								as in JPA, jOOQ would suffer from the same verbosity, or inflexibility
								that JPA CriteriaQueries may have.
							</p>

							<h2>The Field</h2>
							<p>The formal definition of a Field starts with </p>
							<pre class="prettyprint lang-java">public interface Field&lt;T&gt; // [...]</pre>
							<p>
								Fields are generically parameterised with a Java type
								&lt;T&gt;
								that reflects the closest match to the RDMBS's underlying datatype for that
								field. For instance, if you have a VARCHAR2 type Field in Oracle,
								&lt;T&gt;
								would bind to
								<a href="http://download.oracle.com/javase/6/docs/api/java/lang/String.html" title="External API reference: java.lang.String">java.lang.String</a>
								for that Field in jOOQ. Oracle's NUMBER(7) would
								let
								&lt;T&gt;
								bind to
								<a href="http://download.oracle.com/javase/6/docs/api/java/lang/Integer.html" title="External API reference: java.lang.Integer">java.lang.Integer</a>,
								etc. This generic type is useful for two purposes:
							</p>
							<ul>
								
<li>It allows you to write type safe queries. For instance, you cannot
									compare Field
									&lt;String&gt;
									with Field
									&lt;Integer&gt;</li>

								
<li>It
									allows you to fetch correctly cast and converted values from
									your database result set. This is especially useful when &lt;T&gt; binds
									to
									advanced data types, such as
									<a href="<?=$root?>/manual/META/UDT/" title="jOOQ Manual reference: UDT's including ARRAY and ENUM types">UDT's, ARRAY or ENUM types</a>
									, where jOOQ
									does the difficult non-standardised JDBC data type conversions for you.
								</li>
							
</ul>

							<h2>Fields and tables put into action</h2>
							<p>The Field itself is a very broad concept. Other tools, or databases
								refer to it as expression or column. When you just want to </p>

							<pre class="prettyprint lang-sql">SELECT 1 FROM DUAL</pre>
							<p>
								Then 1 is considered a Field or more explicitly, a
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/impl/Constant.java" title="Internal API reference: org.jooq.impl.Constant">org.jooq.impl.Constant</a>,
								which implements Field, and DUAL is considered a Table or more explicitly
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/impl/Dual.java" title="Internal API reference: org.jooq.impl.Dual">org.jooq.impl.Dual</a>, which implements Table
							</p>
							<p>
								More advanced uses become clear quickly, when you do things like
							</p>
							<pre class="prettyprint lang-sql">SELECT 1 + 1 FROM DUAL</pre>
							<p>
								Where 1 + 1 itself is a Field or more explicitly, an
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/impl/Expression.java" title="Internal API reference: org.jooq.impl.Expression">org.jooq.impl.Expression</a>
								joining two Constants together.
							</p>
							<p>
								See some details about how to create these queries in the
								<a href="<?=$root?>/manual/JOOQ/Query/" title="jOOQ Manual reference: The Query and its various subtypes">Query section</a> of the manual
							</p>

							<h2>TableFields</h2>
							<p>
								A specific type of field is the
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/TableField.java" title="Internal API reference: org.jooq.TableField">org.jooq.TableField</a>,
								which represents a physical
								Field in a physical Table. Both the
								TableField and its referenced Table
								know each other. The physical aspect
								of their nature is represented in
								jOOQ by
								<a href="<?=$root?>/manual/META/TABLE/" title="jOOQ Manual reference: Tables, views and their corresponding records">meta model code generation</a>,
								where every entity in your database
								schema will be generated into a
								corresponding Java class.
							</p>
							<p>
								TableFields join both &lt;R&gt; and &lt;T&gt; generic parameters into their specification:
							</p>
							<pre class="prettyprint lang-java">public interface TableField&lt;R extends Record, T&gt; // [...]</pre>
							<p>
								This can be used for additional type safety in the future, or by client code.
							</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/Table/">Tables and Fields</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Factory class" href="<?=$root?>/manual/JOOQ/Factory/">previous</a> : <a title="Next section: Results and Records" href="<?=$root?>/manual/JOOQ/Result/">next</a></td>
</tr>
</table>
<?php 
}
?>

