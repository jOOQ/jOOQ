
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Custom data types and type conversion";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							SQL data types are very limited in number. While some databases offer
							enumeration types or user-defined types, others only ship with the SQL
							standard. Read this chapter to see how to use custom types with jOOQ
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/CustomTypes/">Custom data types and type conversion</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Master data generation. Enumeration tables" href="<?=$root?>/manual/ADVANCED/MasterData/">previous</a> : <a title="Next section: Mapping generated schemata and tables" href="<?=$root?>/manual/ADVANCED/SchemaMapping/">next</a></td>
</tr>
</table>
							<h2>Your custom type and its associated Converter</h2>
							<p>
								When using a custom type in jOOQ, you need to let jOOQ know about
								its associated <a href="http://www.jooq.org/javadoc/latest/org/jooq/Converter.html" title="Internal API reference: org.jooq.Converter">org.jooq.Converter</a>.
								A converter essentially has two generic type parameters:
							</p>
							<ul>
								
<li>&lt;U&gt;: The user-defined Java type. This could be <a href="http://download.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html" title="External API reference: java.util.GregorianCalendar">java.util.GregorianCalendar</a>, for instance.</li>
								
<li>&lt;T&gt;: The database / SQL type. This could be <a href="http://download.oracle.com/javase/6/docs/api/java/sql/Timestamp.html" title="External API reference: java.sql.Timestamp">java.sql.Timestamp</a>, for instance.</li>
							
</ul>
							<p>
								The above conversion implies that you may want to use a GregorianCalendar for
								SQL timestamps, rather than the timestamp type itself. You could then write
								a Converter like this:
							</p>

<pre class="prettyprint lang-java">package com.example;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jooq.Converter;

// Bind T to Timestamp and U to Gregorian calendar, here
public class CalendarConverter implements Converter&lt;Timestamp, GregorianCalendar&gt; {

    // Provide jOOQ with Class&lt;?&gt; objects of &lt;U&gt; and &lt;T&gt;. These are used by
    // jOOQ to discover your converter based on your custom type
    // --------------------------------------------------------------------
    @Override
    public Class&lt;Timestamp&gt; fromType() {
        return Timestamp.class;
    }

    @Override
    public Class&lt;GregorianCalendar&gt; toType() {
        return GregorianCalendar.class;
    }

    // Implement the type conversion methods. Convert your user-defined type
    // "from" the SQL type when reading "from" the database, or "to" the SQL
    // type when writing "to" the database.
    @Override
    public GregorianCalendar from(Timestamp databaseObject) {
        GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance();
        calendar.setTimeInMillis(databaseObject.getTime());
        return calendar;
    }

    @Override
    public Timestamp to(GregorianCalendar userObject) {
        return new Timestamp(userObject.getTime().getTime());
    }
}
</pre>

							<p>
								Such a Converter can now be used in various places of the jOOQ
								API, especially when reading data from the database:
							</p>

<pre class="prettyprint lang-java">List&lt;GregorianCalendar&gt; result =
create.select(T_AUTHOR.DATE_OF_BIRTH)
      .from(T_AUTHOR)
      .fetch(0, new CalendarConverter());</pre>

      						<h3>Using Converters in generated code</h3>
      						<p>
								A more common use-case, however, is to let jOOQ know about custom
								types at code generation time. Use the following configuration elements
								to specify, that you'd like to use GregorianCalendar for all database
								fields that start with DATE_OF_
      						</p>

<pre class="prettyprint lang-xml">&lt;database&gt;
  &lt;!-- First, register your custom types here --&gt;
  &lt;customTypes&gt;
    &lt;customType&gt;
      &lt;!-- Specify the fully-qualified class name of your custom type --&gt;
      &lt;name&gt;java.util.GregorianCalendar&lt;/name&gt;

      &lt;!-- Associate that custom type with your converter. Note, a
           custom type can only have one converter in jOOQ --&gt;
      &lt;converter&gt;com.example.CalendarConverter&lt;/converter&gt;
    &lt;/customType&gt;
  &lt;/customTypes&gt;

  &lt;!-- Then, associate custom types with database columns --&gt;
  &lt;forcedTypes&gt;
    &lt;forcedType&gt;
      &lt;!-- Specify again he fully-qualified class name of your custom type --&gt;
      &lt;name&gt;java.util.GregorianCalendar&lt;/name&gt;

      &lt;!-- Add a list of comma-separated regular expressions matching columns --&gt;
      &lt;expressions&gt;.*\.DATE_OF_.*&lt;/expressions&gt;
    &lt;/forcedType&gt;
  &lt;/forcedTypes&gt;
&lt;/database&gt;</pre>

                            <p>
                            	The above configuration will lead to T_AUTHOR.DATE_OF_BIRTH
                            	being generated like this:
                            </p>

<pre class="prettyprint lang-java">public class TAuthor extends UpdatableTableImpl&lt;TAuthorRecord&gt; {

    // [...]
    public final TableField&lt;TAuthorRecord, GregorianCalendar&gt; DATE_OF_BIRTH =    // [...]
    // [...]

}</pre>

                            <p>
                            	This means that the bound of &lt;T&gt; will be GregorianCalendar,
                            	wherever you reference DATE_OF_BIRTH. jOOQ will use your custom
                            	converter when binding variables and when fetching data from
                            	<a href="http://download.oracle.com/javase/6/docs/api/java/util/ResultSet.html" title="External API reference: java.util.ResultSet">java.util.ResultSet</a>:
                            </p>

<pre class="prettyprint lang-java">// Get all date of births of authors born after 1980
List&lt;GregorianCalendar&gt; result =
create.selectFrom(T_AUTHOR)
      .where(T_AUTHOR.DATE_OF_BIRTH.greaterThan(new GregorianCalendar(1980, 0, 1)))
      .fetch(T_AUTHOR.DATE_OF_BIRTH);</pre>

      						<p>
      						    Read more about advanced code generation configuration in
      						    <a href="<?=$root?>/manual/META/AdvancedConfiguration/" title="jOOQ Manual reference: Advanced configuration of the generator">the manual's section about advanced code generation configuration</a>.
      						</p>

      						<h3>Using Converters for enum types</h3>
      						<p>
      							Java's Enum types can be very useful in SQL too.
      							Some databases support enumeration types natively (MySQL, Postgres).
      							In other cases, you can use the above custom type configuration
      							also to provide jOOQ with Converters for your custom Enum types.
      							Instead of implementing <a href="http://www.jooq.org/javadoc/latest/org/jooq/Converter.html" title="Internal API reference: org.jooq.Converter">org.jooq.Converter</a>,
      							you may choose to extend <a href="http://www.jooq.org/javadoc/latest/org/jooq/impl/EnumConverter.html" title="Internal API reference: org.jooq.impl.EnumConverter">org.jooq.impl.EnumConverter</a>
      							instead, which provides some enum-specific default behaviour.
      						</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/CustomTypes/">Custom data types and type conversion</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Master data generation. Enumeration tables" href="<?=$root?>/manual/ADVANCED/MasterData/">previous</a> : <a title="Next section: Mapping generated schemata and tables" href="<?=$root?>/manual/ADVANCED/SchemaMapping/">next</a></td>
</tr>
</table>
<?php 
}
?>

