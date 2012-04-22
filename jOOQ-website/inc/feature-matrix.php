<table>
	<tbody>
		<tr>
			<th colspan="13">SQL statements and features</th>
		</tr>
		<tr>
			<th width="200">Feature</th>
			<th>ASE</th>
			<th>DB2</th>
			<th>Derby</th>
			<th>H2</th>
			<th>HSQLDB</th>
			<th>Ingres</th>
			<th>MySQL</th>
			<th>Oracle</th>
			<th>Postgres</th>
			<th>SQLite</th>
			<th>SQL Server</th>
			<th>Sybase</th>
		</tr>
		<tr>
			<td>INSERT with VALUES</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>INSERT with VALUES (multiple records)</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>INSERT with SELECT</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>INSERT with ON DUPLICATE KEY UPDATE</td>
			<td>no</td>
			<td>yes [1]</td>
			<td>no</td>
			<td>no</td>
			<td>yes [1]</td>
			<td>no</td>
			<td>yes</td>
			<td>yes [1]</td>
			<td>no</td>
			<td>no</td>
			<td>yes [1]</td>
			<td>yes [1]</td>
		</tr>
		<tr>
			<td>INSERT with RETURNING</td>
			<td>partially [3]</td>
			<td>yes [4]</td>
			<td>partially [3]</td>
			<td>partially [3]</td>
			<td>yes [4]</td>
			<td>partially [3]</td>
			<td>yes [3]</td>
			<td>partially [4]</td>
			<td>yes</td>
			<td>partially [2]</td>
			<td>partially [3]</td>
			<td>partially [2]</td>
		</tr>
		<tr>
			<td>SELECT</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>SELECT with CONNECT BY</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
		</tr>
		<tr>
			<td>SELECT with LIMIT .. OFFSET</td>
			<td>partially [7]</td>
			<td>yes [8]</td>
			<td>yes [9]</td>
			<td>yes [10]</td>
			<td>yes [10]</td>
			<td>yes [9]</td>
			<td>yes [10]</td>
			<td>yes [11]</td>
			<td>yes [10]</td>
			<td>yes [10]</td>
			<td>yes [8]</td>
			<td>yes [12]</td>
		</tr>
		<tr>
			<td>SELECT with FOR UPDATE</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>SELECT with FOR UPDATE OF (field-list)</td>
			<td>no</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>SELECT with FOR UPDATE OF (table-list)</td>
			<td>no</td>
			<td>yes [13]</td>
			<td>yes [13]</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes [13]</td>
			<td>no</td>
			<td>yes [13]</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>SELECT with FOR UPDATE WAIT, NOWAIT, SKIP LOCKED</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
		</tr>
		<tr>
			<td>UPDATE</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>DELETE</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>MERGE</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>no [6]</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>MERGE (Oracle extensions)</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
			<td>no</td>
		</tr>
		<tr>
			<td>TRUNCATE</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes [14]</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes [14]</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<td>Sequence CURRVAL</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>yes</td>
			<td>no</td>
			<td>yes</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
		</tr>

		<tr>
			<td>Sequence NEXTVAL</td>
			<td>no</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>no</td>
			<td>yes</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
		</tr>


		<tr>
			<th colspan="13">JDBC features</th>
		</tr>
		<tr>
			<th>Feature</th>
			<th>ASE</th>
			<th>DB2</th>
			<th>Derby</th>
			<th>H2</th>
			<th>HSQLDB</th>
			<th>Ingres</th>
			<th>MySQL</th>
			<th>Oracle</th>
			<th>Postgres</th>
			<th>SQLite</th>
			<th>SQL Server</th>
			<th>Sybase</th>
		</tr>
		<tr>
			<td>batch operations</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
		</tr>

		<tr>
			<th colspan="13">jOOQ features</th>
		</tr>
		<tr>
			<th>Feature</th>
			<th>ASE</th>
			<th>DB2</th>
			<th>Derby</th>
			<th>H2</th>
			<th>HSQLDB</th>
			<th>Ingres</th>
			<th>MySQL</th>
			<th>Oracle</th>
			<th>Postgres</th>
			<th>SQLite</th>
			<th>SQL Server</th>
			<th>Sybase</th>
		</tr>
		<tr>
			<td>CSV Data loader</td>
			<td>yes [5]</td>
			<td>yes</td>
			<td>yes [5]</td>
			<td>yes [5]</td>
			<td>yes</td>
			<td>yes [5]</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes [5]</td>
			<td>yes [5]</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
		<tr>
			<th colspan="13">Functions</th>
		</tr>
		<tr>
			<th>Feature</th>
			<th>ASE</th>
			<th>DB2</th>
			<th>Derby</th>
			<th>H2</th>
			<th>HSQLDB</th>
			<th>Ingres</th>
			<th>MySQL</th>
			<th>Oracle</th>
			<th>Postgres</th>
			<th>SQLite</th>
			<th>SQL Server</th>
			<th>Sybase</th>
		</tr>
		<tr>
			<td>Last inserted ID</td>
			<td>yes</td>
			<td>no</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
			<td>no</td>
			<td>no</td>
			<td>yes</td>
			<td>yes</td>
			<td>yes</td>
		</tr>
	</tbody>
</table>

<ul>
	<li>[1] MySQL's INSERT .. ON DUPLICATE KEY UPDATE statement is simulated using an equivalent MERGE statement</li>
	<li>[2] Postgres' INSERT .. RETURNING statement is simulated by fetching the last inserted ID explicitly with a new statement. Other fields than the ID are fetched with another statement. Client code must ensure transactional integrity between the three statements to prevent race conditions (e.g. set autocommit to off). This does not work for multi-record inserts.</li>
	<li>[3] Postgres' INSERT .. RETURNING statement is simulated by using JDBC's PreparedStatement.getGeneratedKeys() functionality. Other fields than the ID are fetched with another statement. Client code must ensure transactional integrity between the two statements to prevent race conditions (e.g. set autocommit to off). This may not work for multi-record inserts in some dialects</li>
	<li>[4] Postgres' INSERT .. RETURNING statement is simulated by using JDBC's PreparedStatement.getGeneratedKeys() functionality. All fields can be fetched in a single statement. This may not work for multi-record inserts in some dialects</li>
	<li>[5] The loader API has an option to use MySQL's INSERT .. ON DUPLICATE KEY UPDATE statement, which only works in some dialects. See also [1]</li>
	<li>[6] H2's MERGE statement is quite different from the SQL:2003 standard MERGE statement. It is currently not supported by jOOQ</li>
	<li>[7] Sybase ASE has no means of specifying an OFFSET like other dialects. LIMIT is translated to the TOP clause</li>
	<li>[8] DB2 knows a FIRST ROWS clause without OFFSET. SQL Server knows a TOP clause without OFFSET. LIMIT .. OFFSET is simulated using nested SELECT and filtering on ROW_NUMBER() OVER()</li>
	<li>[9] Derby knows the OFFSET .. ROWS FETCH NEXT .. ROWS ONLY clause, Ingres has a similar OFFSET .. ROWS FETCH NEXT .. ROWS ONLY clause. They work just the same as [10]</li>
	<li>[10] Many databases know the LIMIT .. OFFSET clause as declared in the jOOQ API</li>
	<li>[11] Oracle has no native support for LIMIT .. OFFSET clauses. Instead, LIMIT .. OFFSET is simulated using nested SELECT and filtering on ROW_NUMBER() OVER()</li>
	<li>[12] Sybase knows a TOP .. START AT .. clause that works just the same as [10]</li>
	<li>[13] Some databases do not support a FOR UPDATE OF (table-list) clause. But jOOQ knows all columns in generated tables, hence jOOQ renders an appropriate FOR UPDATE OF (field-list) clause instead</li>
	<li>[14] Some databases do not support a TRUNCATE statement. An equivalent DELETE statement is issued instead.</li>
</ul>