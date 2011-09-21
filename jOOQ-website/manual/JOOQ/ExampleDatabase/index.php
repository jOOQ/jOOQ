
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function printH1() {
    print "The example database";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							For the examples in this manual, the same database will always be
							referred to. It essentially consists of these entities created using
							the Oracle dialect
						";
}
function printContent() {
    global $root;
?>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/ExampleDatabase/">The example database</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/JOOQ/" title="Previous section: jOOQ classes and their usage">previous</a> : <a href="<?=$root?>/manual/JOOQ/Factory/" title="Next section: The Factory class">next</a></td>
</tr>
</table>
							<h2>Example CREATE TABLE statements</h2>
							<pre class="prettyprint lang-sql">
CREATE TABLE t_language (
  id NUMBER(7) NOT NULL PRIMARY KEY,
  cd CHAR(2) NOT NULL,
  description VARCHAR2(50)
)

CREATE TABLE t_author (
  id NUMBER(7) NOT NULL PRIMARY KEY,
  first_name VARCHAR2(50),
  last_name VARCHAR2(50) NOT NULL,
  date_of_birth DATE,
  year_of_birth NUMBER(7)
)

CREATE TABLE t_book (
  id NUMBER(7) NOT NULL PRIMARY KEY,
  author_id NUMBER(7) NOT NULL,
  title VARCHAR2(400) NOT NULL,
  published_in NUMBER(7) NOT NULL,
  language_id NUMBER(7) NOT NULL,
  FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID)
)

CREATE TABLE t_book_store (
  name VARCHAR2(400) NOT NULL UNIQUE
)

CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR2(400) NOT NULL,
  book_id INTEGER NOT NULL,
  stock INTEGER,
  PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT b2bs_book_store_id
    FOREIGN KEY (book_store_name)
    REFERENCES t_book_store (name)
    ON DELETE CASCADE,
  CONSTRAINT b2bs_book_id
    FOREIGN KEY (book_id)
    REFERENCES t_book (id)
    ON DELETE CASCADE
)							
							</pre>
							<p>
								More entities, types (e.g. UDT's, ARRAY types, ENUM types, etc),
								stored procedures and packages are introduced for specific examples
							</p>
						<br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/ExampleDatabase/">The example database</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/JOOQ/" title="Previous section: jOOQ classes and their usage">previous</a> : <a href="<?=$root?>/manual/JOOQ/Factory/" title="Next section: The Factory class">next</a></td>
</tr>
</table>
<?php 
}
?>

