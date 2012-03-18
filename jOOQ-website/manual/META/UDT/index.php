
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "UDT's including ARRAY and ENUM types";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Databases become more powerful when you can structure your data in user
							defined types. It's time for Java developers to give some credit to
							that.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/UDT/">UDT's including ARRAY and ENUM types</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Procedures and packages" href="<?=$root?>/manual/META/PROCEDURE/">previous</a> : <a title="Next section: Sequences" href="<?=$root?>/manual/META/SEQUENCE/">next</a></td>
</tr>
</table>
							<h2>Increased RDBMS support for UDT's</h2>
							<p>
								In recent years, most RDBMS have started to implement some support for
								advanced data types. This support has not been adopted very well by
								database users in the Java world, for several reasons:
							</p>
							<ul>
								
<li>They are usually orthogonal to relational concepts. It is not easy
									to modify a UDT once it is referenced by a table column.</li>
								
<li>There is little standard support of accessing them from JDBC (and
									probably other database connectivity standards). </li>
							
</ul>
							<p>
								On the other hand, especially with stored procedures, these data types
								are likely to become more and more useful in the future. If you have a
								look at Postgres' capabilities of dealing with advanced data types
								(<a href="http://www.postgresql.org/docs/9.0/interactive/datatype-enum.html">ENUMs</a>,
								<a href="http://www.postgresql.org/docs/9.0/interactive/arrays.html">ARRAYs</a>,
								<a href="http://www.postgresql.org/docs/9.0/interactive/rowtypes.html">UDT's</a>),
								 this becomes more and more obvious.
							</p>
							<p>It is a central strategy for jOOQ, to standardise access to these
								kinds of types (as well as to
								<a href="<?=$root?>/manual/META/PROCEDURE/" title="jOOQ Manual reference: Procedures and packages">stored procedures</a>, of course) across all
								RDBMS, where these types are supported. </p>

							<h2>UDT types</h2>
							<p>User Defined Types (UDT) are helpful in major RDMBS with lots
							of proprietary functionality. The biggest player is clearly Oracle.
							Currently, jOOQ provides UDT support for only two databases: </p>
							<ul>
								
<li>Oracle</li>
								
<li>Postgres</li>
							
</ul>
							<p>Apart from that, </p>
							<ul>
								
<li>
									DB2 UDT's are not supported as they are very tough to
									serialise/deserialise. We don't think that this is a big enough
									requirement to put more effort in those, right now (see also the
									developers' discussion on
									<a href="https://sourceforge.net/apps/trac/jooq/ticket/164" title="Trac ticket: #164">#164</a>)
								</li>
							
</ul>

							<p>In Oracle, you would define UDTs like this: </p>
<pre class="prettyprint lang-sql">CREATE TYPE u_street_type AS OBJECT (
  street VARCHAR2(100),
  no VARCHAR2(30)
)

CREATE TYPE u_address_type AS OBJECT (
  street u_street_type,
  zip VARCHAR2(50),
  city VARCHAR2(50),
  country VARCHAR2(50),
  since DATE,
  code NUMBER(7)
)</pre>

							<p>These types could then be used in tables and/or stored procedures like such: </p>
<pre class="prettyprint lang-sql">CREATE TABLE t_author (
  id NUMBER(7) NOT NULL PRIMARY KEY,
  -- [...]
  address u_address_type
)

CREATE OR REPLACE PROCEDURE p_check_address (address IN OUT u_address_type);</pre>

							<p>
								Standard JDBC UDT support encourages JDBC-driver developers to implement
								interfaces such as
								<a href="http://download.oracle.com/javase/6/docs/api/java/sql/SQLData.html" title="External API reference: java.sql.SQLData">java.sql.SQLData</a>,
								<a href="http://download.oracle.com/javase/6/docs/api/java/sql/SQLInput.html" title="External API reference: java.sql.SQLInput">java.sql.SQLInput</a> and
								<a href="http://download.oracle.com/javase/6/docs/api/java/sql/SQLOutput.html" title="External API reference: java.sql.SQLOutput">java.sql.SQLOutput</a>.
								Those interfaces are non-trivial to implement, or
								to hook into. Also access to
								<a href="http://download.oracle.com/javase/6/docs/api/java/sql/Struct.html" title="External API reference: java.sql.Struct">java.sql.Struct</a>
								is not really simple. Due
								to the lack of a well-defined JDBC standard, Oracle's JDBC driver
								rolls their own proprietary methods of dealing with these types. jOOQ
								goes a different way, it hides those facts from you entirely. With
								jOOQ, the above UDT's will be generated in simple
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/UDT.html" title="Internal API reference: org.jooq.UDT">UDT meta-model classes</a> and
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/UDTRecord.html" title="Internal API reference: org.jooq.UDTRecord">UDT record classes</a> as such:
							</p>
<pre class="prettyprint lang-java">// There is an analogy between UDT/Table and UDTRecord/TableRecord...
public class UAddressType extends UDTImpl&lt;UAddressTypeRecord&gt; {

    // The UDT meta-model singleton instance
    public static final UAddressType U_ADDRESS_TYPE = new UAddressType();

    // UDT attributes are modeled as static members. Nested UDT's
    // behave similarly
    public static final UDTField&lt;UAddressTypeRecord, UStreetTypeRecord&gt; STREET = // [...]
    public static final UDTField&lt;UAddressTypeRecord, String&gt; ZIP =               // [...]
    public static final UDTField&lt;UAddressTypeRecord, String&gt; CITY =              // [...]
    public static final UDTField&lt;UAddressTypeRecord, String&gt; COUNTRY =           // [...]
    public static final UDTField&lt;UAddressTypeRecord, Date&gt; SINCE =               // [...]
    public static final UDTField&lt;UAddressTypeRecord, Integer&gt; CODE =             // [...]
}</pre>

							<p>Now, when you interact with entities or procedures that hold UDT's, that's very simple as well. Here is an example: </p>
<pre class="prettyprint lang-java">// Fetch any author from the T_AUTHOR table
TAuthorRecord author = create.selectFrom(T_AUTHOR).fetchAny();

// Print out the author's address's house number
System.out.println(author.getAddress().getStreet().getNo());</pre>

							<p>A similar thing can be achieved when interacting with the example stored procedure: </p>
<pre class="prettyprint lang-java">// Create a new UDTRecord of type U_ADDRESS_TYPE
UAddressTypeRecord address = new UAddressTypeRecord();
address.setCountry("Switzerland");

// Call the stored procedure with IN OUT parameter of type U_ADDRESS_TYPE
address = Procedures.pCheckAddress(connection, address);</pre>


							<h2>ARRAY types</h2>
							<p>
								The notion of ARRAY types in RDBMS is not standardised at all. Very
								modern databases (especially the Java-based ones) have implemented
								ARRAY types exactly as what they are. "ARRAYs of something". In other
								words, an ARRAY OF VARCHAR would be something very similar to Java's
								notion of String[]. An ARRAY OF ARRAY OF VARCHAR would then be a
								String[][] in Java. Some RDMBS, however, enforce stronger typing and
								need the explicit creation of types for every ARRAY as well. These are
								example String[] ARRAY types in various SQL dialects supported by jOOQ
								1.5.4:
							</p>
							<ul>
								
<li>Oracle: VARRAY OF VARCHAR2. A strongly typed object encapsulating an ARRAY of a given type. See the <a href="http://download.oracle.com/docs/cd/B19306_01/appdev.102/b14261/collection_definition.htm">documentation.</a>
</li>
								
<li>Postgres: text[]. Any data type can be turned into an array by suffixing it with []. See the <a href="http://www.postgresql.org/docs/9.0/interactive/arrays.html">documentation</a>
</li>
								
<li>HSQLDB: VARCHAR ARRAY. Any data type can be turned into an array by suffixing it with ARRAY. See the <a href="http://hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html#N1070F">documentation</a>
</li>
								
<li>H2: ARRAY. H2 does not know of typed arrays. All ARRAYs are mapped to Object[]. See the <a href="http://www.h2database.com/html/datatypes.html#array_type">documentation</a>
</li>
							
</ul>
							<p>Soon to be supported: </p>
							<ul>
								
<li>DB2: Knows a similar strongly-typed ARRAY type, like Oracle </li>
							
</ul>
							<p>
								From jOOQ's perspective, the ARRAY types fit in just like any other
								type wherever the
								&lt;T&gt; generic type parameter is existent. It integrates well with tables
									and stored procedures.
							</p>

							<h3>Example: General ARRAY types</h3>
							<p>An example usage of ARRAYs is given here for the Postgres dialect </p>

<pre class="prettyprint lang-sql">CREATE TABLE t_arrays (
  id integer not null primary key,
  string_array VARCHAR(20)[],
  number_array INTEGER[]
)

CREATE FUNCTION f_arrays(in_array IN text[]) RETURNS text[]</pre>

							<p>When generating source code from the above entities, these artefacts will be created in Java: </p>
<pre class="prettyprint lang-java">public class TArrays extends UpdatableTableImpl&lt;TArraysRecord&gt; {

    // The generic type parameter &lt;T&gt; is bound to an array of a matching type
    public static final TableField&lt;TArraysRecord, String[]&gt; STRING_ARRAY =  // [...]
    public static final TableField&lt;TArraysRecord, Integer[]&gt; NUMBER_ARRAY = // [...]
}

// The convenience class is enhanced with these methods
public final class Functions {
    public static String[] fArrays(Connection connection, String[] inArray) { // [...]
    public static Field&lt;String[]&gt; fArrays(String[] inArray) {                                     // [...]
    public static Field&lt;String[]&gt; fArrays(Field&lt;String[]&gt; inArray) {                              // [...]
}</pre>

							<h3>Example: Oracle VARRAY types</h3>
							<p>In Oracle, a VARRAY type is something slightly different than in
								other RDMBS. It is a type that encapsules the actual ARRAY and creates
								a new type from it. While all text[] types are equal and thus
								compatible in Postgres, this does not apply for all VARRAY OF VARCHAR2
								types. Hence, it is important to provide access to VARRAY types and
								generated objects from those types as well. The example above would
								read like this in Oracle: </p>

<pre class="prettyprint lang-sql">CREATE TYPE u_string_array AS VARRAY(4) OF VARCHAR2(20)
CREATE TYPE u_number_array AS VARRAY(4) OF NUMBER(7)

CREATE TABLE t_arrays (
  id NUMBER(7) not null primary key,
  string_array u_string_array,
  number_array u_number_array
)

CREATE OR REPLACE FUNCTION f_arrays (in_array u_string_array)
RETURN u_string_array</pre>

							<p>Note that it becomes clear immediately, that a mapping from
								U_STRING_ARRAY to String[] is obvious. But a mapping from String[] to
								U_STRING_ARRAY is not. These are the generated
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/ArrayRecord.html" title="Internal API reference: org.jooq.ArrayRecord">org.jooq.ArrayRecord</a> and other
								artefacts in Oracle: </p>

<pre class="prettyprint lang-java">public class UStringArrayRecord extends ArrayRecordImpl&lt;String&gt; {  // [...]
public class UNumberArrayRecord extends ArrayRecordImpl&lt;Integer&gt; { // [...]

public class TArrays extends UpdatableTableImpl&lt;TArraysRecord&gt; {
    public static final TableField&lt;TArraysRecord, UStringArrayRecord&gt; STRING_ARRAY = // [...]
    public static final TableField&lt;TArraysRecord, UNumberArrayRecord&gt; NUMBER_ARRAY = // [...]
}

public final class Functions {
    public static UStringArrayRecord fArrays3(Connection connection, UStringArrayRecord inArray) { // [...]
    public static Field&lt;UStringArrayRecord&gt; fArrays3(UStringArrayRecord inArray) {                 // [...]
    public static Field&lt;UStringArrayRecord&gt; fArrays3(Field&lt;UStringArrayRecord&gt; inArray) {          // [...]
}</pre>


							<h2>ENUM types</h2>
							<p>True ENUM types are a rare species in the RDBMS world. Currently,
								MySQL and Postgres are the only RDMBS supported by jOOQ, that provide
								ENUM types. </p>

							<ul>
								
<li>In MySQL, an ENUM type is declared directly upon a column. It cannot be reused as a type. See the <a href="http://dev.mysql.com/doc/refman/5.5/en/enum.html">documentation.</a> 
</li>
								
<li>In Postgres, the ENUM type is declared independently and can be reused among tables, functions, etc. See the <a href="http://www.postgresql.org/docs/9.0/interactive/datatype-enum.html">documentation.</a> 
</li>
								
<li>Other RDMBS know about "ENUM constraints", such as the Oracle CHECK constraint. These are not true ENUMS, however. jOOQ refrains from using their information for source code generation </li>
							
</ul>

							<p>Some examples: </p>
<pre class="prettyprint lang-sql">-- An example enum type
CREATE TYPE u_book_status AS ENUM ('SOLD OUT', 'ON STOCK', 'ORDERED')

-- An example useage of that enum type
CREATE TABLE t_book (
  id INTEGER NOT NULL PRIMARY KEY,

  -- [...]
  status u_book_status
)</pre>

							<p>The above Postgres ENUM type will be generated as </p>
<pre class="prettyprint lang-java">public enum UBookStatus implements EnumType {
    ORDERED("ORDERED"),
    ON_STOCK("ON STOCK"),
    SOLD_OUT("SOLD OUT");

    // [...]
}</pre>
							<p>Intuitively, the generated classes for the T_BOOK table in Postgres would look like this: </p>
<pre class="prettyprint lang-sql">// The meta-model class
public class TBook extends UpdatableTableImpl&lt;TBookRecord&gt; {

    // The TableField STATUS binds &lt;T&gt; to UBookStatus
    public static final TableField&lt;TBookRecord, UBookStatus&gt; STATUS = // [...]

    // [...]
}

// The record class
public class TBookRecord extends UpdatableRecordImpl&lt;TBookRecord&gt; {

    // Corresponding to the Table meta-model, also setters and getters
    // deal with the generated UBookStatus
    public void setStatus(UBookStatus value) { // [...]
    public UBookStatus getStatus() {           // [...]
}</pre>

							<p>Note that jOOQ allows you to simulate ENUM types where this makes
								sense in your data model. See the section on
								<a href="<?=$root?>/manual/ADVANCED/MasterData/" title="jOOQ Manual reference: Master data generation. Enumeration tables">master data</a> for more
								details. </p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/UDT/">UDT's including ARRAY and ENUM types</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Procedures and packages" href="<?=$root?>/manual/META/PROCEDURE/">previous</a> : <a title="Next section: Sequences" href="<?=$root?>/manual/META/SEQUENCE/">next</a></td>
</tr>
</table>
<?php 
}
?>

