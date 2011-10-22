
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function printH1() {
    print "Procedures and packages";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Procedure support is one of the most important reasons why you should consider
							jOOQ. jOOQ heavily facilitates the use of stored procedures and
							functions via its source code generation.
						";
}
function printContent() {
    global $root;
?>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/PROCEDURE/">Procedures and packages</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/META/TABLE/" title="Previous section: Tables, views and their corresponding records">previous</a> : <a href="<?=$root?>/manual/META/UDT/" title="Next section: UDT's including ARRAY and ENUM types">next</a></td>
</tr>
</table>
							<h2>Stored procedures in modern RDBMS</h2>
							<p>This is one of the most important reasons why you should consider
								jOOQ. Read also my 
								<a href="http://java.dzone.com/articles/2011-great-year-stored" title="Article on stored procedures and how to use them with jOOQ">article on dzone</a>
								about why stored procedures become
								more and more important in future versions of RDMBS. In this section
								of the manual, we will learn how jOOQ handles stored procedures in
								code generation. Especially before 
								<a href="<?=$root?>/manual/META/UDT/" title="jOOQ Manual reference: UDT's including ARRAY and ENUM types">UDT and ARRAY support</a> was
								introduced to major RDBMS, these procedures tend to have dozens of
								parameters, with IN, OUT, IN OUT parameters mixed in all variations.
								JDBC only knows very basic, low-level support for those constructs.
								jOOQ heavily facilitates the use of stored procedures and functions
								via its source code generation. Essentially, it comes down to this:
							</p>
							
							<h3>"Standalone" stored procedures and functions</h3>
							<p>Let's say you have these stored procedures and functions in your Oracle database </p>
							<pre class="prettyprint lang-sql">
-- Check whether there is an author in T_AUTHOR by that name
CREATE OR REPLACE FUNCTION f_author_exists (author_name VARCHAR2) RETURN NUMBER;

-- Check whether there is an author in T_AUTHOR by that name
CREATE OR REPLACE PROCEDURE p_author_exists (author_name VARCHAR2, result OUT NUMBER);

-- Check whether there is an author in T_AUTHOR by that name and get his ID
CREATE OR REPLACE PROCEDURE p_author_exists_2 (author_name VARCHAR2, result OUT NUMBER, id OUT NUMBER);</pre>
							
							<p>jOOQ will essentially generate two artefacts for every procedure/function: </p>
							<ul>
								
<li>A class holding a formal Java representation of the procedure/function</li>
    							
<li>Some convenience methods to facilitate calling that procedure/function </li>
							
</ul>
							<p>Let's see what these things look like, in Java. The classes (simplified for the example): </p>
							
							<pre class="prettyprint lang-java">
// The function has a generic type parameter &lt;T&gt; bound to its return value
public class FAuthorExists extends org.jooq.impl.AbstractRoutine&lt;BigDecimal&gt; {

    // Much like Tables, functions have static parameter definitions
    public static final Parameter&lt;String&gt; AUTHOR_NAME = // [...]

    // And much like TableRecords, they have setters for their parameters
    public void setAuthorName(String value) { // [...]
    public void setAuthorName(Field&lt;String&gt; value) { // [...]
}

public class PAuthorExists extends org.jooq.impl.AbstractRoutine&lt;java.lang.Void&gt; {

    // In procedures, IN, OUT, IN OUT parameters are all represented
    // as static parameter definitions as well
    public static final Parameter&lt;String&gt; AUTHOR_NAME = // [...]
    public static final Parameter&lt;BigDecimal&gt; RESULT = // [...]

    // IN and IN OUT parameters have generated setters
    public void setAuthorName(String value) { // [...]

    // OUT and IN OUT parameters have generated getters
    public BigDecimal getResult() { // [...]
}

public class PAuthorExists_2 extends org.jooq.impl.AbstractRoutine&lt;java.lang.Void&gt; {
    public static final Parameter&lt;String&gt; AUTHOR_NAME = // [...]
    public static final Parameter&lt;BigDecimal&gt; RESULT = // [...]
    public static final Parameter&lt;BigDecimal&gt; ID = // [...]

    // the setters...
    public void setAuthorName(String value) { // [...]

    // the getters...
    public BigDecimal getResult() { // [...]
    public BigDecimal getId() { // [...]
}</pre>

							<p>An example invocation of such a stored procedure might look like this: </p>
							
							<pre class="prettyprint lang-java">
PAuthorExists p = new PAuthorExists();
p.setAuthorName("Paulo");
p.execute(configuration);
assertEquals(BigDecimal.ONE, p.getResult());</pre>

							<p>If you use the generated convenience methods, however, things are much simpler, still: </p>
							<pre class="prettyprint lang-java">
// Every schema has a single Routines class with convenience methods
public final class Routines {

    // Convenience method to directly call the stored function
    public static BigDecimal fAuthorExists(Configuration configuration, String authorName) { // [...]

    // Convenience methods to transform the stored function into a
    // Field&lt;BigDecimal&gt;, such that it can be used in SQL
    public static Field&lt;BigDecimal&gt; fAuthorExists(Field&lt;String&gt; authorName) { // [...]
    public static Field&lt;BigDecimal&gt; fAuthorExists(String authorName) { // [...]

    // Procedures with 0 OUT parameters create void methods
    // Procedures with 1 OUT parameter create methods as such:
    public static BigDecimal pAuthorExists(Configuration configuration, String authorName) { // [...]

    // Procedures with more than 1 OUT parameter return the procedure
    // object (see above example)
    public static PAuthorExists_2 pAuthorExists_2(Configuration configuration, String authorName) { // [...]
}</pre>

							<p>An sample invocation, equivalent to the previous example:</p>
							<pre class="prettyprint lang-java">
assertEquals(BigDecimal.ONE, Procedures.pAuthorExists(configuration, "Paulo"));</pre>


							<h3>jOOQ's understanding of procedures vs functions</h3>
							<p>
								jOOQ does not formally distinguish procedures from functions.
								jOOQ only knows about routines, which can have return values
								and/or OUT parameters. This is the best option to handle the
								variety of stored procedure / function support across the
								various supported RDBMS. For more details, read on about this
								topic, here:
							</p>
							<p>
								
<a href="http://lukaseder.wordpress.com/2011/10/17/what-are-procedures-and-functions-after-all/" title="Blog post about the difference between procedures and functions in various RDBMS">lukaseder.wordpress.com/2011/10/17/what-are-procedures-and-functions-after-all/</a>
							
</p>
							
							<h3>Packages in Oracle</h3>
							<p>
								Oracle uses the concept of a PACKAGE to group several
								procedures/functions into a sort of namespace. The
								<a href="http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt" title="SQL 92 standard">SQL standard</a>
								talks about "modules", to represent this concept, even if this is
								rarely implemented. This is reflected in jOOQ by the use of Java
								sub-packages in the source code generation destination package. Every
								Oracle package will be reflected by
							</p>
							<ul>
								
<li>A Java package holding classes for formal Java representations of
									the procedure/function in that package
								</li>
								
<li>A Java class holding convenience methods to facilitate calling
									those procedures/functions
								</li>
							
</ul>
							<p>
								Apart from this, the generated source code looks exactly like the
								one for
								standalone procedures/functions.
							</p>
						<br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/PROCEDURE/">Procedures and packages</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/META/TABLE/" title="Previous section: Tables, views and their corresponding records">previous</a> : <a href="<?=$root?>/manual/META/UDT/" title="Next section: UDT's including ARRAY and ENUM types">next</a></td>
</tr>
</table>
<?php 
}
?>

