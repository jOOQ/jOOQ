
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Updatable Records";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							UpdatableRecords are a specific subtype of TableRecord that have
							primary key information associated with them.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/UpdatableRecord/">Updatable Records</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Results and Records" href="<?=$root?>/manual/JOOQ/Result/">previous</a> : <a title="Next section: The Query and its various subtypes" href="<?=$root?>/manual/JOOQ/Query/">next</a></td>
</tr>
</table>
							<h2>CRUD Operations</h2>
							<p>As of jOOQ 1.5, the UpdatableRecord essentially contains three additional
							 methods <a href="http://de.wikipedia.org/wiki/CRUD">CRUD</a>
							 (Create Read Update Delete) operations: </p>
<pre class="prettyprint lang-java">// Store any changes made to this record to the database.
// The record executes an INSERT if the PRIMARY KEY is NULL or has been changed. Otherwise, an UPDATE is performed.
int store();

// Deletes the record from the database.
int delete();

// Reflects changes made in the database to this Record
void refresh();</pre>
							<p>An example lifecycle of a book can be implemented as such:</p>
<pre class="prettyprint lang-java">// Create a new record and insert it into the database
TBookRecord book = create.newRecord(T_BOOK);
book.setTitle("My first book");
book.store();

// Update it with new values
book.setPublishedIn(2010);
book.store();

// Delete it
book.delete();</pre>
							<p>These operations are very simple utilities. They do not
							reflect the functionality offered by <a href="http://www.hibernate.org/">Hibernate</a>
							or other persistence managers. </p>

							<h2>Performing CRUD on non-updatable records</h2>
							<p>
								If the jOOQ code-generator cannot detect any PRIMARY KEY, or UNIQUE KEY
								on your tables, then the generated artefacts implement TableRecord,
								instead of UpdatableRecord. A TableRecord can perform the same CRUD
								operations as we have seen before, if you provide it with the necessary
								key fields. The API looks like this:
							</p>

<pre class="prettyprint lang-java">// INSERT or UPDATE the record using the provided keys
int storeUsing(TableField&lt;R, ?&gt;... keys)

// DELETE a record using the provided keys
int deleteUsing(TableField&lt;R, ?&gt;... keys);

// Reflects changes made in the database to this Record
void refreshUsing(TableField&lt;R, ?&gt;... keys);</pre>

							<p>
								This is useful if your RDBMS does not support referential constraints (e.g. MySQL's
								<a href="http://en.wikipedia.org/wiki/MyISAM">MyISAM</a>), or if you want to
								store records to an unconstrained view. An example lifecycle of a book without
								any keys can then be implemented as such:
							</p>
<pre class="prettyprint lang-java">// Create a new record and insert it into the database
TBookRecord book = create.newRecord(T_BOOK);
book.setTitle("My first book");
book.storeUsing(TBook.ID);

// Update it with new values
book.setPublishedIn(2010);
book.storeUsing(TBook.ID);

// Delete it
book.deleteUsing(TBook.ID);</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/UpdatableRecord/">Updatable Records</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Results and Records" href="<?=$root?>/manual/JOOQ/Result/">previous</a> : <a title="Next section: The Query and its various subtypes" href="<?=$root?>/manual/JOOQ/Query/">next</a></td>
</tr>
</table>
<?php 
}
?>

