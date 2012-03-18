
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "jOOQ's relational division syntax";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
				    	    Relational division is a rather academic topic that is not used in
				    	    every-day SQL. Nevertheless, it can be extremely powerful in some
				    	    remote cases. jOOQ supports it intuitively
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/DIVISION/">jOOQ's relational division syntax</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Oracle 11g PIVOT clause" href="<?=$root?>/manual/ADVANCED/PIVOT/">previous</a> : <a title="Next section: Exporting to XML, CSV, JSON, HTML, Text" href="<?=$root?>/manual/ADVANCED/Export/">next</a></td>
</tr>
</table>
							<h2>Relational division</h2>
							<p>
							    There is one operation in relational algebra that is not given
							    a lot of attention, because it is rarely used in real-world
							    applications. It is the relational division, the opposite operation
							    of the cross product (or, relational multiplication).
								The following is an approximate definition of a relational division:
							</p>

<pre class="prettyprint">Assume the following cross join / cartesian product
C = A &times; B

Then it can be said that
A = C &divide; B
B = C &divide; A</pre>


							<p>
							   With jOOQ, you can simplify using relational divisions
							   by using the following syntax:
							</p>

<pre class="prettyprint lang-java">C.divideBy(B).on(C.ID.equal(B.C_ID)).returning(C.TEXT)</pre>

							<p>The above roughly translates to</p>

<pre class="prettyprint lang-sql">SELECT DISTINCT C.TEXT FROM C "c1"
WHERE NOT EXISTS (
  SELECT 1 FROM B
  WHERE NOT EXISTS (
    SELECT 1 FROM C "c2"
    WHERE "c2".TEXT = "c1".TEXT
    AND "c2".ID = B.C_ID
  )
)</pre>

							<p>
								Or in plain text: Find those TEXT values in C
								whose ID's correspond to all ID's in B. Note
								that from the above SQL statement, it is immediately
								clear that proper indexing is of the essence.
								Be sure to have indexes on all columns referenced
								from the on(...) and returning(...) clauses.
							</p>

							<p>
								For more information about relational division
								and some nice, real-life examples, see
							</p>

							<ul>
								
<li>
									
<a href="http://en.wikipedia.org/wiki/Relational_algebra#Division" title="Wikipedia article on relational division">http://en.wikipedia.org/wiki/Relational_algebra#Division</a>
								
</li>
								
<li>
									
<a href="http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-sql-of-relational-division/" title="A nice summary of what relational division is and how it is best implemented in SQL">http://www.simple-talk.com/sql/t-sql-programming/divided-we-stand-the-sql-of-relational-division/</a>
								
</li>
							
</ul>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/DIVISION/">jOOQ's relational division syntax</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Oracle 11g PIVOT clause" href="<?=$root?>/manual/ADVANCED/PIVOT/">previous</a> : <a title="Next section: Exporting to XML, CSV, JSON, HTML, Text" href="<?=$root?>/manual/ADVANCED/Export/">next</a></td>
</tr>
</table>
<?php 
}
?>

