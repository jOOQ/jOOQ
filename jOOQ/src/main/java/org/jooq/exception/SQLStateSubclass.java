/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.exception;

import static org.jooq.exception.SQLStateClass.C00_SUCCESSFUL_COMPLETION;
import static org.jooq.exception.SQLStateClass.C01_WARNING;
import static org.jooq.exception.SQLStateClass.C02_NO_DATA;
import static org.jooq.exception.SQLStateClass.C07_DYNAMIC_SQL_ERROR;
import static org.jooq.exception.SQLStateClass.C08_CONNECTION_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C09_TRIGGERED_ACTION_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C0A_FEATURE_NOT_SUPPORTED;
import static org.jooq.exception.SQLStateClass.C0D_INVALID_TARGET_TYPE_SPECIFICATION;
import static org.jooq.exception.SQLStateClass.C0E_INVALID_SCHEMA_NAME_LIST_SPECIFICATION;
import static org.jooq.exception.SQLStateClass.C0F_LOCATOR_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C0L_INVALID_GRANTOR;
import static org.jooq.exception.SQLStateClass.C0M_INVALID_SQL_INVOKED_PROCEDURE_REFERENCE;
import static org.jooq.exception.SQLStateClass.C0P_INVALID_ROLE_SPECIFICATION;
import static org.jooq.exception.SQLStateClass.C0S_INVALID_TRANSFORM_GROUP_NAME_SPECIFICATION;
import static org.jooq.exception.SQLStateClass.C0T_TARGET_TABLE_DISAGREES_WITH_CURSOR_SPECIFICATION;
import static org.jooq.exception.SQLStateClass.C0U_ATTEMPT_TO_ASSIGN_TO_NON_UPDATABLE_COLUMN;
import static org.jooq.exception.SQLStateClass.C0V_ATTEMPT_TO_ASSIGN_TO_ORDERING_COLUMN;
import static org.jooq.exception.SQLStateClass.C0W_PROHIBITED_STATEMENT_ENCOUNTERED_DURING_TRIGGER_EXECUTION;
import static org.jooq.exception.SQLStateClass.C0Z_DIAGNOSTICS_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C21_CARDINALITY_VIOLATION;
import static org.jooq.exception.SQLStateClass.C22_DATA_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C23_INTEGRITY_CONSTRAINT_VIOLATION;
import static org.jooq.exception.SQLStateClass.C24_INVALID_CURSOR_STATE;
import static org.jooq.exception.SQLStateClass.C25_INVALID_TRANSACTION_STATE;
import static org.jooq.exception.SQLStateClass.C26_INVALID_SQL_STATEMENT_NAME;
import static org.jooq.exception.SQLStateClass.C27_TRIGGERED_DATA_CHANGE_VIOLATION;
import static org.jooq.exception.SQLStateClass.C28_INVALID_AUTHORIZATION_SPECIFICATION;
import static org.jooq.exception.SQLStateClass.C2B_DEPENDENT_PRIVILEGE_DESCRIPTORS_STILL_EXIST;
import static org.jooq.exception.SQLStateClass.C2C_INVALID_CHARACTER_SET_NAME;
import static org.jooq.exception.SQLStateClass.C2D_INVALID_TRANSACTION_TERMINATION;
import static org.jooq.exception.SQLStateClass.C2E_INVALID_CONNECTION_NAME;
import static org.jooq.exception.SQLStateClass.C2F_SQL_ROUTINE_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C2H_INVALID_COLLATION_NAME;
import static org.jooq.exception.SQLStateClass.C30_INVALID_SQL_STATEMENT_IDENTIFIER;
import static org.jooq.exception.SQLStateClass.C33_INVALID_SQL_DESCRIPTOR_NAME;
import static org.jooq.exception.SQLStateClass.C34_INVALID_CURSOR_NAME;
import static org.jooq.exception.SQLStateClass.C35_INVALID_CONDITION_NUMBER;
import static org.jooq.exception.SQLStateClass.C36_CURSOR_SENSITIVITY_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C38_EXTERNAL_ROUTINE_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C39_EXTERNAL_ROUTINE_INVOCATION_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C3B_SAVEPOINT_EXCEPTION;
import static org.jooq.exception.SQLStateClass.C3C_AMBIGUOUS_CURSOR_NAME;
import static org.jooq.exception.SQLStateClass.C3D_INVALID_CATALOG_NAME;
import static org.jooq.exception.SQLStateClass.C3F_INVALID_SCHEMA_NAME;
import static org.jooq.exception.SQLStateClass.C40_TRANSACTION_ROLLBACK;
import static org.jooq.exception.SQLStateClass.C42_SYNTAX_ERROR_OR_ACCESS_RULE_VIOLATION;
import static org.jooq.exception.SQLStateClass.CHZ_REMOTE_DATABASE_ACCESS;

import java.util.HashMap;
import java.util.Map;

/**
 * The subclass of the SQL state class as specified by the SQL standard, or by individual
 * vendors.
 * <p>
 * <table border="1">
 * <tr><th>Class</th><th>Description</th><th>Subclass</th><th>Description</th></tr>
 * <tr><td>00</td><td>Successful completion</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>01</td><td>Warning</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>01</td><td>Warning</td><td>001</td><td>Cursor operation conflict</td></tr>
 * <tr><td>01</td><td>Warning</td><td>002</td><td>Disconnect error</td></tr>
 * <tr><td>01</td><td>Warning</td><td>003</td><td>Null value eliminated in set function</td></tr>
 * <tr><td>01</td><td>Warning</td><td>004</td><td>String data, right truncation</td></tr>
 * <tr><td>01</td><td>Warning</td><td>005</td><td>Insufficient item descriptor areas</td></tr>
 * <tr><td>01</td><td>Warning</td><td>006</td><td>Privilege not revoked</td></tr>
 * <tr><td>01</td><td>Warning</td><td>007</td><td>Privilege not granted</td></tr>
 * <tr><td>01</td><td>Warning</td><td>009</td><td>Search condition too long for information schema</td></tr>
 * <tr><td>01</td><td>Warning</td><td>00A</td><td>Query expression too long for information schema</td></tr>
 * <tr><td>01</td><td>Warning</td><td>00B</td><td>Default value too long for information schema</td></tr>
 * <tr><td>01</td><td>Warning</td><td>00C</td><td>Result sets returned</td></tr>
 * <tr><td>01</td><td>Warning</td><td>00D</td><td>Additional result sets returned</td></tr>
 * <tr><td>01</td><td>Warning</td><td>00E</td><td>Attempt to return too many result sets</td></tr>
 * <tr><td>01</td><td>Warning</td><td>00F</td><td>Statement too long for information schema</td></tr>
 * <tr><td>01</td><td>Warning</td><td>012</td><td>Invalid number of conditions</td></tr>
 * <tr><td>01</td><td>Warning</td><td>02F</td><td>Array data, right truncation</td></tr>
 * <tr><td>02</td><td>No data</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>02</td><td>No data</td><td>001</td><td>No additional result sets returned</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>001</td><td>Using clause does not match dynamic parameter specifications</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>002</td><td>Using clause does not match target specifications</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>003</td><td>Cursor specification cannot be executed</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>004</td><td>Using clause required for dynamic parameters</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>005</td><td>Prepared statement not a cursor specification</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>006</td><td>Restricted data type attribute violation</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>007</td><td>Using clause required for result fields</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>008</td><td>Invalid descriptor count</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>009</td><td>Invalid descriptor index</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>00B</td><td>Data type transform function violation</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>00C</td><td>Undefined DATA value</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>00D</td><td>Invalid DATA target</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>00E</td><td>Invalid LEVEL value</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td><td>00F</td><td>Invalid DATETIME_INTERVAL_CODE</td></tr>
 * <tr><td>08</td><td>Connection exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>08</td><td>Connection exception</td><td>001</td><td>SQL-client unable to establish SQL-connection</td></tr>
 * <tr><td>08</td><td>Connection exception</td><td>002</td><td>Connection name in use</td></tr>
 * <tr><td>08</td><td>Connection exception</td><td>003</td><td>Connection does not exist</td></tr>
 * <tr><td>08</td><td>Connection exception</td><td>004</td><td>SQL-server rejected establishment of SQL-connection</td></tr>
 * <tr><td>08</td><td>Connection exception</td><td>006</td><td>Connection failure</td></tr>
 * <tr><td>08</td><td>Connection exception</td><td>007</td><td>Transaction resolution unknown</td></tr>
 * <tr><td>09</td><td>Triggered action exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0A</td><td>Feature not supported</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0A</td><td>Feature not supported</td><td>001</td><td>Multiple server transactions</td></tr>
 * <tr><td>0D</td><td>Invalid target type specification</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0E</td><td>Invalid schema name list specification</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0F</td><td>Locator exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0F</td><td>Locator exception</td><td>001</td><td>Invalid specification</td></tr>
 * <tr><td>0L</td><td>Invalid grantor</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0M</td><td>Invalid SQL-invoked procedure reference</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0P</td><td>Invalid role specification</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0S</td><td>Invalid transform group name specification</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0T</td><td>Target table disagrees with cursor specification</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0U</td><td>Attempt to assign to non-updatable column</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0V</td><td>Attempt to assign to ordering column</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0W</td><td>Prohibited statement encountered during trigger execution</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0W</td><td>Prohibited statement encountered during trigger execution</td><td>001</td><td>Modify table modified by data change delta table</td></tr>
 * <tr><td>0Z</td><td>Diagnostics exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>0Z</td><td>Diagnostics exception</td><td>001</td><td>Maximum number of stacked diagnostics areas exceeded</td></tr>
 * <tr><td>21</td><td>Cardinality violation</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>001</td><td>String data, right truncation</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>002</td><td>Null value, no indicator parameter</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>003</td><td>Numeric value out of range</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>004</td><td>Null value not allowed</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>005</td><td>Error in assignment</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>006</td><td>Invalid interval format</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>007</td><td>Invalid datetime format</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>008</td><td>Datetime field overflow</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>009</td><td>Invalid time zone displacement value</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00B</td><td>Escape character conflict</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00C</td><td>Invalid use of escape character</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00D</td><td>Invalid escape octet</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00E</td><td>Null value in array target</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00F</td><td>Zero-length character string</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00G</td><td>Most specific type mismatch</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00H</td><td>Sequence generator limit exceeded</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00P</td><td>Interval value out of range</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>00Q</td><td>Multiset value overflow</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>010</td><td>Invalid indicator parameter value</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>011</td><td>Substring error</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>012</td><td>Division by zero</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>013</td><td>Invalid preceding or following size in window function</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>014</td><td>Invalid argument for NTILE function</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>015</td><td>Interval field overflow</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>016</td><td>Invalid argument for NTH_VALUE function</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>018</td><td>Invalid character value for cast</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>019</td><td>Invalid escape character</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01B</td><td>Invalid regular expression</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01C</td><td>Null row not permitted in table</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01E</td><td>Invalid argument for natural logarithm</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01F</td><td>Invalid argument for power function</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01G</td><td>Invalid argument for width bucket function</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01H</td><td>Invalid row version</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01S</td><td>Invalid XQuery regular expression</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01T</td><td>Invalid XQuery option flag</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01U</td><td>Attempt to replace a zero-length string</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01V</td><td>Invalid XQuery replacement string</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01W</td><td>Invalid row count in fetch first clause</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>01X</td><td>Invalid row count in result offset clause</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>020</td><td>Invalid period value</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>021</td><td>Character not in repertoire</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>022</td><td>Indicator overflow</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>023</td><td>Invalid parameter value</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>024</td><td>Unterminated C string</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>025</td><td>Invalid escape sequence</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>026</td><td>String data, length mismatch</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>027</td><td>Trim error</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>029</td><td>Noncharacter in UCS string</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>02D</td><td>Null value substituted for mutator subject parameter</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>02E</td><td>Array element error</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>02F</td><td>Array data, right truncation</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>02G</td><td>Invalid repeat argument in sample clause</td></tr>
 * <tr><td>22</td><td>Data exception</td><td>02H</td><td>Invalid sample size</td></tr>
 * <tr><td>23</td><td>Integrity constraint violation</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>23</td><td>Integrity constraint violation</td><td>001</td><td>Restrict violation</td></tr>
 * <tr><td>24</td><td>Invalid cursor state</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>001</td><td>Active SQL-transaction</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>002</td><td>Branch transaction already active</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>003</td><td>Inappropriate access mode for branch transaction</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>004</td><td>Inappropriate isolation level for branch transaction</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>005</td><td>No active SQL-transaction for branch transaction</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>006</td><td>Read-only SQL-transaction</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>007</td><td>Schema and data statement mixing not supported</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td><td>008</td><td>Held cursor requires same isolation level</td></tr>
 * <tr><td>26</td><td>Invalid SQL statement name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>27</td><td>Triggered data change violation</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>27</td><td>Triggered data change violation</td><td>001</td><td>Modify table modified by data change delta table</td></tr>
 * <tr><td>28</td><td>Invalid authorization specification</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>2B</td><td>Dependent privilege descriptors still exist</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>2C</td><td>Invalid character set name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>2C</td><td>Invalid character set name</td><td>001</td><td>Cannot drop SQL-session default character set</td></tr>
 * <tr><td>2D</td><td>Invalid transaction termination</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>2E</td><td>Invalid connection name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>2F</td><td>SQL routine exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>2F</td><td>SQL routine exception</td><td>002</td><td>Modifying SQL-data not permitted</td></tr>
 * <tr><td>2F</td><td>SQL routine exception</td><td>003</td><td>Prohibited SQL-statement attempted</td></tr>
 * <tr><td>2F</td><td>SQL routine exception</td><td>004</td><td>Reading SQL-data not permitted</td></tr>
 * <tr><td>2F</td><td>SQL routine exception</td><td>005</td><td>Function executed no return statement</td></tr>
 * <tr><td>2H</td><td>Invalid collation name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>30</td><td>Invalid SQL statement identifier</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>33</td><td>Invalid SQL descriptor name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>34</td><td>Invalid cursor name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>35</td><td>Invalid condition number</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>36</td><td>Cursor sensitivity exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>36</td><td>Cursor sensitivity exception</td><td>001</td><td>request rejected</td></tr>
 * <tr><td>36</td><td>Cursor sensitivity exception</td><td>002</td><td>request failed</td></tr>
 * <tr><td>38</td><td>External routine exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>38</td><td>External routine exception</td><td>001</td><td>Containing SQL not permitted</td></tr>
 * <tr><td>38</td><td>External routine exception</td><td>002</td><td>Modifying SQL-data not permitted</td></tr>
 * <tr><td>38</td><td>External routine exception</td><td>003</td><td>Prohibited SQL-statement attempted</td></tr>
 * <tr><td>38</td><td>External routine exception</td><td>004</td><td>Reading SQL-data not permitted</td></tr>
 * <tr><td>39</td><td>External routine invocation exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>39</td><td>External routine invocation exception</td><td>004</td><td>Null value not allowed</td></tr>
 * <tr><td>3B</td><td>Savepoint exception</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>3B</td><td>Savepoint exception</td><td>001</td><td>Invalid specification</td></tr>
 * <tr><td>3B</td><td>Savepoint exception</td><td>002</td><td>Too many</td></tr>
 * <tr><td>3C</td><td>Ambiguous cursor name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>3D</td><td>Invalid catalog name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>3F</td><td>Invalid schema name</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>40</td><td>Transaction rollback</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>40</td><td>Transaction rollback</td><td>001</td><td>Serialization failure</td></tr>
 * <tr><td>40</td><td>Transaction rollback</td><td>002</td><td>Integrity constraint violation</td></tr>
 * <tr><td>40</td><td>Transaction rollback</td><td>003</td><td>Statement completion unknown</td></tr>
 * <tr><td>40</td><td>Transaction rollback</td><td>004</td><td>Triggered action exception</td></tr>
 * <tr><td>42</td><td>Syntax error or access rule violation</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>44</td><td>With check option violation</td><td>000</td><td>No subclass</td></tr>
 * <tr><td>HZ</td><td>Remote database access</td><td>000</td><td>No subclass</td></tr>
 * </table>
 *
 * @author Lukas Eder
 */
public enum SQLStateSubclass {

    C00000_NO_SUBCLASS(C00_SUCCESSFUL_COMPLETION, "000"),
    C01000_NO_SUBCLASS(C01_WARNING, "000"),
    C01001_CURSOR_OPERATION_CONFLICT(C01_WARNING, "001"),
    C01002_DISCONNECT_ERROR(C01_WARNING, "002"),
    C01003_NULL_VALUE_ELIMINATED_IN_SET_FUNCTION(C01_WARNING, "003"),
    C01004_STRING_DATA_RIGHT_TRUNCATION(C01_WARNING, "004"),
    C01005_INSUFFICIENT_ITEM_DESCRIPTOR_AREAS(C01_WARNING, "005"),
    C01006_PRIVILEGE_NOT_REVOKED(C01_WARNING, "006"),
    C01007_PRIVILEGE_NOT_GRANTED(C01_WARNING, "007"),
    C01009_SEARCH_CONDITION_TOO_LONG_FOR_INFORMATION_SCHEMA(C01_WARNING, "009"),
    C0100A_QUERY_EXPRESSION_TOO_LONG_FOR_INFORMATION_SCHEMA(C01_WARNING, "00A"),
    C0100B_DEFAULT_VALUE_TOO_LONG_FOR_INFORMATION_SCHEMA(C01_WARNING, "00B"),
    C0100C_RESULT_SETS_RETURNED(C01_WARNING, "00C"),
    C0100D_ADDITIONAL_RESULT_SETS_RETURNED(C01_WARNING, "00D"),
    C0100E_ATTEMPT_TO_RETURN_TOO_MANY_RESULT_SETS(C01_WARNING, "00E"),
    C0100F_STATEMENT_TOO_LONG_FOR_INFORMATION_SCHEMA(C01_WARNING, "00F"),
    C01012_INVALID_NUMBER_OF_CONDITIONS(C01_WARNING, "012"),
    C0102F_ARRAY_DATA_RIGHT_TRUNCATION(C01_WARNING, "02F"),
    C02000_NO_SUBCLASS(C02_NO_DATA, "000"),
    C02001_NO_ADDITIONAL_RESULT_SETS_RETURNED(C02_NO_DATA, "001"),
    C07000_NO_SUBCLASS(C07_DYNAMIC_SQL_ERROR, "000"),
    C07001_USING_CLAUSE_DOES_NOT_MATCH_DYNAMIC_PARAMETER_SPECIFICATIONS(C07_DYNAMIC_SQL_ERROR, "001"),
    C07002_USING_CLAUSE_DOES_NOT_MATCH_TARGET_SPECIFICATIONS(C07_DYNAMIC_SQL_ERROR, "002"),
    C07003_CURSOR_SPECIFICATION_CANNOT_BE_EXECUTED(C07_DYNAMIC_SQL_ERROR, "003"),
    C07004_USING_CLAUSE_REQUIRED_FOR_DYNAMIC_PARAMETERS(C07_DYNAMIC_SQL_ERROR, "004"),
    C07005_PREPARED_STATEMENT_NOT_A_CURSOR_SPECIFICATION(C07_DYNAMIC_SQL_ERROR, "005"),
    C07006_RESTRICTED_DATA_TYPE_ATTRIBUTE_VIOLATION(C07_DYNAMIC_SQL_ERROR, "006"),
    C07007_USING_CLAUSE_REQUIRED_FOR_RESULT_FIELDS(C07_DYNAMIC_SQL_ERROR, "007"),
    C07008_INVALID_DESCRIPTOR_COUNT(C07_DYNAMIC_SQL_ERROR, "008"),
    C07009_INVALID_DESCRIPTOR_INDEX(C07_DYNAMIC_SQL_ERROR, "009"),
    C0700B_DATA_TYPE_TRANSFORM_FUNCTION_VIOLATION(C07_DYNAMIC_SQL_ERROR, "00B"),
    C0700C_UNDEFINED_DATA_VALUE(C07_DYNAMIC_SQL_ERROR, "00C"),
    C0700D_INVALID_DATA_TARGET(C07_DYNAMIC_SQL_ERROR, "00D"),
    C0700E_INVALID_LEVEL_VALUE(C07_DYNAMIC_SQL_ERROR, "00E"),
    C0700F_INVALID_DATETIME_INTERVAL_CODE(C07_DYNAMIC_SQL_ERROR, "00F"),
    C08000_NO_SUBCLASS(C08_CONNECTION_EXCEPTION, "000"),
    C08001_SQL_CLIENT_UNABLE_TO_ESTABLISH_SQL_CONNECTION(C08_CONNECTION_EXCEPTION, "001"),
    C08002_CONNECTION_NAME_IN_USE(C08_CONNECTION_EXCEPTION, "002"),
    C08003_CONNECTION_DOES_NOT_EXIST(C08_CONNECTION_EXCEPTION, "003"),
    C08004_SQL_SERVER_REJECTED_ESTABLISHMENT_OF_SQL_CONNECTION(C08_CONNECTION_EXCEPTION, "004"),
    C08006_CONNECTION_FAILURE(C08_CONNECTION_EXCEPTION, "006"),
    C08007_TRANSACTION_RESOLUTION_UNKNOWN(C08_CONNECTION_EXCEPTION, "007"),
    C09000_NO_SUBCLASS(C09_TRIGGERED_ACTION_EXCEPTION, "000"),
    C0A000_NO_SUBCLASS(C0A_FEATURE_NOT_SUPPORTED, "000"),
    C0A001_MULTIPLE_SERVER_TRANSACTIONS(C0A_FEATURE_NOT_SUPPORTED, "001"),
    C0D000_NO_SUBCLASS(C0D_INVALID_TARGET_TYPE_SPECIFICATION, "000"),
    C0E000_NO_SUBCLASS(C0E_INVALID_SCHEMA_NAME_LIST_SPECIFICATION, "000"),
    C0F000_NO_SUBCLASS(C0F_LOCATOR_EXCEPTION, "000"),
    C0F001_INVALID_SPECIFICATION(C0F_LOCATOR_EXCEPTION, "001"),
    C0L000_NO_SUBCLASS(C0L_INVALID_GRANTOR, "000"),
    C0M000_NO_SUBCLASS(C0M_INVALID_SQL_INVOKED_PROCEDURE_REFERENCE, "000"),
    C0P000_NO_SUBCLASS(C0P_INVALID_ROLE_SPECIFICATION, "000"),
    C0S000_NO_SUBCLASS(C0S_INVALID_TRANSFORM_GROUP_NAME_SPECIFICATION, "000"),
    C0T000_NO_SUBCLASS(C0T_TARGET_TABLE_DISAGREES_WITH_CURSOR_SPECIFICATION, "000"),
    C0U000_NO_SUBCLASS(C0U_ATTEMPT_TO_ASSIGN_TO_NON_UPDATABLE_COLUMN, "000"),
    C0V000_NO_SUBCLASS(C0V_ATTEMPT_TO_ASSIGN_TO_ORDERING_COLUMN, "000"),
    C0W000_NO_SUBCLASS(C0W_PROHIBITED_STATEMENT_ENCOUNTERED_DURING_TRIGGER_EXECUTION, "000"),
    C0W001_MODIFY_TABLE_MODIFIED_BY_DATA_CHANGE_DELTA_TABLE(C0W_PROHIBITED_STATEMENT_ENCOUNTERED_DURING_TRIGGER_EXECUTION, "001"),
    C0Z000_NO_SUBCLASS(C0Z_DIAGNOSTICS_EXCEPTION, "000"),
    C0Z001_MAXIMUM_NUMBER_OF_STACKED_DIAGNOSTICS_AREAS_EXCEEDED(C0Z_DIAGNOSTICS_EXCEPTION, "001"),
    C21000_NO_SUBCLASS(C21_CARDINALITY_VIOLATION, "000"),
    C22000_NO_SUBCLASS(C22_DATA_EXCEPTION, "000"),
    C22001_STRING_DATA_RIGHT_TRUNCATION(C22_DATA_EXCEPTION, "001"),
    C22002_NULL_VALUE_NO_INDICATOR_PARAMETER(C22_DATA_EXCEPTION, "002"),
    C22003_NUMERIC_VALUE_OUT_OF_RANGE(C22_DATA_EXCEPTION, "003"),
    C22004_NULL_VALUE_NOT_ALLOWED(C22_DATA_EXCEPTION, "004"),
    C22005_ERROR_IN_ASSIGNMENT(C22_DATA_EXCEPTION, "005"),
    C22006_INVALID_INTERVAL_FORMAT(C22_DATA_EXCEPTION, "006"),
    C22007_INVALID_DATETIME_FORMAT(C22_DATA_EXCEPTION, "007"),
    C22008_DATETIME_FIELD_OVERFLOW(C22_DATA_EXCEPTION, "008"),
    C22009_INVALID_TIME_ZONE_DISPLACEMENT_VALUE(C22_DATA_EXCEPTION, "009"),
    C2200B_ESCAPE_CHARACTER_CONFLICT(C22_DATA_EXCEPTION, "00B"),
    C2200C_INVALID_USE_OF_ESCAPE_CHARACTER(C22_DATA_EXCEPTION, "00C"),
    C2200D_INVALID_ESCAPE_OCTET(C22_DATA_EXCEPTION, "00D"),
    C2200E_NULL_VALUE_IN_ARRAY_TARGET(C22_DATA_EXCEPTION, "00E"),
    C2200F_ZERO_LENGTH_CHARACTER_STRING(C22_DATA_EXCEPTION, "00F"),
    C2200G_MOST_SPECIFIC_TYPE_MISMATCH(C22_DATA_EXCEPTION, "00G"),
    C2200H_SEQUENCE_GENERATOR_LIMIT_EXCEEDED(C22_DATA_EXCEPTION, "00H"),
    C2200P_INTERVAL_VALUE_OUT_OF_RANGE(C22_DATA_EXCEPTION, "00P"),
    C2200Q_MULTISET_VALUE_OVERFLOW(C22_DATA_EXCEPTION, "00Q"),
    C22010_INVALID_INDICATOR_PARAMETER_VALUE(C22_DATA_EXCEPTION, "010"),
    C22011_SUBSTRING_ERROR(C22_DATA_EXCEPTION, "011"),
    C22012_DIVISION_BY_ZERO(C22_DATA_EXCEPTION, "012"),
    C22013_INVALID_PRECEDING_OR_FOLLOWING_SIZE_IN_WINDOW_FUNCTION(C22_DATA_EXCEPTION, "013"),
    C22014_INVALID_ARGUMENT_FOR_NTILE_FUNCTION(C22_DATA_EXCEPTION, "014"),
    C22015_INTERVAL_FIELD_OVERFLOW(C22_DATA_EXCEPTION, "015"),
    C22016_INVALID_ARGUMENT_FOR_NTH_VALUE_FUNCTION(C22_DATA_EXCEPTION, "016"),
    C22018_INVALID_CHARACTER_VALUE_FOR_CAST(C22_DATA_EXCEPTION, "018"),
    C22019_INVALID_ESCAPE_CHARACTER(C22_DATA_EXCEPTION, "019"),
    C2201B_INVALID_REGULAR_EXPRESSION(C22_DATA_EXCEPTION, "01B"),
    C2201C_NULL_ROW_NOT_PERMITTED_IN_TABLE(C22_DATA_EXCEPTION, "01C"),
    C2201E_INVALID_ARGUMENT_FOR_NATURAL_LOGARITHM(C22_DATA_EXCEPTION, "01E"),
    C2201F_INVALID_ARGUMENT_FOR_POWER_FUNCTION(C22_DATA_EXCEPTION, "01F"),
    C2201G_INVALID_ARGUMENT_FOR_WIDTH_BUCKET_FUNCTION(C22_DATA_EXCEPTION, "01G"),
    C2201H_INVALID_ROW_VERSION(C22_DATA_EXCEPTION, "01H"),
    C2201S_INVALID_XQUERY_REGULAR_EXPRESSION(C22_DATA_EXCEPTION, "01S"),
    C2201T_INVALID_XQUERY_OPTION_FLAG(C22_DATA_EXCEPTION, "01T"),
    C2201U_ATTEMPT_TO_REPLACE_A_ZERO_LENGTH_STRING(C22_DATA_EXCEPTION, "01U"),
    C2201V_INVALID_XQUERY_REPLACEMENT_STRING(C22_DATA_EXCEPTION, "01V"),
    C2201W_INVALID_ROW_COUNT_IN_FETCH_FIRST_CLAUSE(C22_DATA_EXCEPTION, "01W"),
    C2201X_INVALID_ROW_COUNT_IN_RESULT_OFFSET_CLAUSE(C22_DATA_EXCEPTION, "01X"),
    C22020_INVALID_PERIOD_VALUE(C22_DATA_EXCEPTION, "020"),
    C22021_CHARACTER_NOT_IN_REPERTOIRE(C22_DATA_EXCEPTION, "021"),
    C22022_INDICATOR_OVERFLOW(C22_DATA_EXCEPTION, "022"),
    C22023_INVALID_PARAMETER_VALUE(C22_DATA_EXCEPTION, "023"),
    C22024_UNTERMINATED_C_STRING(C22_DATA_EXCEPTION, "024"),
    C22025_INVALID_ESCAPE_SEQUENCE(C22_DATA_EXCEPTION, "025"),
    C22026_STRING_DATA_LENGTH_MISMATCH(C22_DATA_EXCEPTION, "026"),
    C22027_TRIM_ERROR(C22_DATA_EXCEPTION, "027"),
    C22029_NONCHARACTER_IN_UCS_STRING(C22_DATA_EXCEPTION, "029"),
    C2202D_NULL_VALUE_SUBSTITUTED_FOR_MUTATOR_SUBJECT_PARAMETER(C22_DATA_EXCEPTION, "02D"),
    C2202E_ARRAY_ELEMENT_ERROR(C22_DATA_EXCEPTION, "02E"),
    C2202F_ARRAY_DATA_RIGHT_TRUNCATION(C22_DATA_EXCEPTION, "02F"),
    C2202G_INVALID_REPEAT_ARGUMENT_IN_SAMPLE_CLAUSE(C22_DATA_EXCEPTION, "02G"),
    C2202H_INVALID_SAMPLE_SIZE(C22_DATA_EXCEPTION, "02H"),
    C23000_NO_SUBCLASS(C23_INTEGRITY_CONSTRAINT_VIOLATION, "000"),
    C23001_RESTRICT_VIOLATION(C23_INTEGRITY_CONSTRAINT_VIOLATION, "001"),
    C24000_NO_SUBCLASS(C24_INVALID_CURSOR_STATE, "000"),
    C25000_NO_SUBCLASS(C25_INVALID_TRANSACTION_STATE, "000"),
    C25001_ACTIVE_SQL_TRANSACTION(C25_INVALID_TRANSACTION_STATE, "001"),
    C25002_BRANCH_TRANSACTION_ALREADY_ACTIVE(C25_INVALID_TRANSACTION_STATE, "002"),
    C25003_INAPPROPRIATE_ACCESS_MODE_FOR_BRANCH_TRANSACTION(C25_INVALID_TRANSACTION_STATE, "003"),
    C25004_INAPPROPRIATE_ISOLATION_LEVEL_FOR_BRANCH_TRANSACTION(C25_INVALID_TRANSACTION_STATE, "004"),
    C25005_NO_ACTIVE_SQL_TRANSACTION_FOR_BRANCH_TRANSACTION(C25_INVALID_TRANSACTION_STATE, "005"),
    C25006_READ_ONLY_SQL_TRANSACTION(C25_INVALID_TRANSACTION_STATE, "006"),
    C25007_SCHEMA_AND_DATA_STATEMENT_MIXING_NOT_SUPPORTED(C25_INVALID_TRANSACTION_STATE, "007"),
    C25008_HELD_CURSOR_REQUIRES_SAME_ISOLATION_LEVEL(C25_INVALID_TRANSACTION_STATE, "008"),
    C26000_NO_SUBCLASS(C26_INVALID_SQL_STATEMENT_NAME, "000"),
    C27000_NO_SUBCLASS(C27_TRIGGERED_DATA_CHANGE_VIOLATION, "000"),
    C27001_MODIFY_TABLE_MODIFIED_BY_DATA_CHANGE_DELTA_TABLE(C27_TRIGGERED_DATA_CHANGE_VIOLATION, "001"),
    C28000_NO_SUBCLASS(C28_INVALID_AUTHORIZATION_SPECIFICATION, "000"),
    C2B000_NO_SUBCLASS(C2B_DEPENDENT_PRIVILEGE_DESCRIPTORS_STILL_EXIST, "000"),
    C2C000_NO_SUBCLASS(C2C_INVALID_CHARACTER_SET_NAME, "000"),
    C2C001_CANNOT_DROP_SQL_SESSION_DEFAULT_CHARACTER_SET(C2C_INVALID_CHARACTER_SET_NAME, "001"),
    C2D000_NO_SUBCLASS(C2D_INVALID_TRANSACTION_TERMINATION, "000"),
    C2E000_NO_SUBCLASS(C2E_INVALID_CONNECTION_NAME, "000"),
    C2F000_NO_SUBCLASS(C2F_SQL_ROUTINE_EXCEPTION, "000"),
    C2F002_MODIFYING_SQL_DATA_NOT_PERMITTED(C2F_SQL_ROUTINE_EXCEPTION, "002"),
    C2F003_PROHIBITED_SQL_STATEMENT_ATTEMPTED(C2F_SQL_ROUTINE_EXCEPTION, "003"),
    C2F004_READING_SQL_DATA_NOT_PERMITTED(C2F_SQL_ROUTINE_EXCEPTION, "004"),
    C2F005_FUNCTION_EXECUTED_NO_RETURN_STATEMENT(C2F_SQL_ROUTINE_EXCEPTION, "005"),
    C2H000_NO_SUBCLASS(C2H_INVALID_COLLATION_NAME, "000"),
    C30000_NO_SUBCLASS(C30_INVALID_SQL_STATEMENT_IDENTIFIER, "000"),
    C33000_NO_SUBCLASS(C33_INVALID_SQL_DESCRIPTOR_NAME, "000"),
    C34000_NO_SUBCLASS(C34_INVALID_CURSOR_NAME, "000"),
    C35000_NO_SUBCLASS(C35_INVALID_CONDITION_NUMBER, "000"),
    C36000_NO_SUBCLASS(C36_CURSOR_SENSITIVITY_EXCEPTION, "000"),
    C36001_REQUEST_REJECTED(C36_CURSOR_SENSITIVITY_EXCEPTION, "001"),
    C36002_REQUEST_FAILED(C36_CURSOR_SENSITIVITY_EXCEPTION, "002"),
    C38000_NO_SUBCLASS(C38_EXTERNAL_ROUTINE_EXCEPTION, "000"),
    C38001_CONTAINING_SQL_NOT_PERMITTED(C38_EXTERNAL_ROUTINE_EXCEPTION, "001"),
    C38002_MODIFYING_SQL_DATA_NOT_PERMITTED(C38_EXTERNAL_ROUTINE_EXCEPTION, "002"),
    C38003_PROHIBITED_SQL_STATEMENT_ATTEMPTED(C38_EXTERNAL_ROUTINE_EXCEPTION, "003"),
    C38004_READING_SQL_DATA_NOT_PERMITTED(C38_EXTERNAL_ROUTINE_EXCEPTION, "004"),
    C39000_NO_SUBCLASS(C39_EXTERNAL_ROUTINE_INVOCATION_EXCEPTION, "000"),
    C39004_NULL_VALUE_NOT_ALLOWED(C39_EXTERNAL_ROUTINE_INVOCATION_EXCEPTION, "004"),
    C3B000_NO_SUBCLASS(C3B_SAVEPOINT_EXCEPTION, "000"),
    C3B001_INVALID_SPECIFICATION(C3B_SAVEPOINT_EXCEPTION, "001"),
    C3B002_TOO_MANY(C3B_SAVEPOINT_EXCEPTION, "002"),
    C3C000_NO_SUBCLASS(C3C_AMBIGUOUS_CURSOR_NAME, "000"),
    C3D000_NO_SUBCLASS(C3D_INVALID_CATALOG_NAME, "000"),
    C3F000_NO_SUBCLASS(C3F_INVALID_SCHEMA_NAME, "000"),
    C40000_NO_SUBCLASS(C40_TRANSACTION_ROLLBACK, "000"),
    C40001_SERIALIZATION_FAILURE(C40_TRANSACTION_ROLLBACK, "001"),
    C40002_INTEGRITY_CONSTRAINT_VIOLATION(C40_TRANSACTION_ROLLBACK, "002"),
    C40003_STATEMENT_COMPLETION_UNKNOWN(C40_TRANSACTION_ROLLBACK, "003"),
    C40004_TRIGGERED_ACTION_EXCEPTION(C40_TRANSACTION_ROLLBACK, "004"),
    C42000_NO_SUBCLASS(C42_SYNTAX_ERROR_OR_ACCESS_RULE_VIOLATION, "000"),
    CHZ000_NO_SUBCLASS(CHZ_REMOTE_DATABASE_ACCESS, "000"),


    OTHER(SQLStateClass.OTHER, ""),
    NONE(SQLStateClass.OTHER, ""),
    ;

    private static final Map<String, SQLStateSubclass> lookup = new HashMap<String, SQLStateSubclass>();
    private final SQLStateClass                        clazz;
    private final String                               subclass;

    static {
        for (SQLStateSubclass clazz : SQLStateSubclass.values()) {
            lookup.put(clazz.sqlStateClassName() + clazz.subclass, clazz);
        }
    }

    private SQLStateSubclass(SQLStateClass clazz, String subclass) {
        this.clazz = clazz;
        this.subclass = subclass;
    }

    public String sqlStateSubclassName() {
        return subclass;
    }

    public SQLStateClass sqlStateClass() {
        return clazz;
    }

    public String sqlStateClassName() {
        return sqlStateClass().className();
    }

    public static SQLStateSubclass fromCode(String code) {
        if (code == null || code.length() != 5)
            return SQLStateSubclass.OTHER;

        SQLStateSubclass result;
        result = lookup.get(code);
        if (result != null)
            return result;

        result = lookup.get(code.substring(0, 2) + "000");
        if (result != null)
            return result;
        else
            return SQLStateSubclass.OTHER;
    }
}
