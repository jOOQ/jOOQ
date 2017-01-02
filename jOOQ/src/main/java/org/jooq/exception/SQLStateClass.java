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

import java.util.HashMap;
import java.util.Map;

/**
 * The class of the SQL state as specified by the SQL:2011 standard, or by individual
 * vendors.
 * <p>
 * <table border="1">
 * <tr><th>Class</th><th>Description</th></tr>
 * <tr><td>00</td><td>Successful completion</td></tr>
 * <tr><td>01</td><td>Warning</td></tr>
 * <tr><td>02</td><td>No data</td></tr>
 * <tr><td>07</td><td>Dynamic SQL Error</td></tr>
 * <tr><td>08</td><td>Connection exception</td></tr>
 * <tr><td>09</td><td>Triggered action exception</td></tr>
 * <tr><td>0A</td><td>Feature not supported</td></tr>
 * <tr><td>0D</td><td>Invalid target type specification</td></tr>
 * <tr><td>0E</td><td>Invalid schema name list specification</td></tr>
 * <tr><td>0F</td><td>Locator exception</td></tr>
 * <tr><td>0L</td><td>Invalid grantor</td></tr>
 * <tr><td>0M</td><td>Invalid SQL-invoked procedure reference</td></tr>
 * <tr><td>0P</td><td>Invalid role specification</td></tr>
 * <tr><td>0S</td><td>Invalid transform group name specification</td></tr>
 * <tr><td>0T</td><td>Target table disagrees with cursor specification</td></tr>
 * <tr><td>0U</td><td>Attempt to assign to non-updatable column</td></tr>
 * <tr><td>0V</td><td>Attempt to assign to ordering column</td></tr>
 * <tr><td>0W</td><td>Prohibited statement encountered during trigger execution</td></tr>
 * <tr><td>0Z</td><td>Diagnostics exception</td></tr>
 * <tr><td>21</td><td>Cardinality violation</td></tr>
 * <tr><td>22</td><td>Data exception</td></tr>
 * <tr><td>23</td><td>Integrity constraint violation</td></tr>
 * <tr><td>24</td><td>Invalid cursor state</td></tr>
 * <tr><td>25</td><td>Invalid transaction state</td></tr>
 * <tr><td>26</td><td>Invalid SQL statement name</td></tr>
 * <tr><td>27</td><td>Triggered data change violation</td></tr>
 * <tr><td>28</td><td>Invalid authorization specification</td></tr>
 * <tr><td>2B</td><td>Dependent privilege descriptors still exist</td></tr>
 * <tr><td>2C</td><td>Invalid character set name</td></tr>
 * <tr><td>2D</td><td>Invalid transaction termination</td></tr>
 * <tr><td>2E</td><td>Invalid connection name</td></tr>
 * <tr><td>2F</td><td>SQL routine exception</td></tr>
 * <tr><td>2H</td><td>Invalid collation name</td></tr>
 * <tr><td>30</td><td>Invalid SQL statement identifier</td></tr>
 * <tr><td>33</td><td>Invalid SQL descriptor name</td></tr>
 * <tr><td>34</td><td>Invalid cursor name</td></tr>
 * <tr><td>35</td><td>Invalid condition number</td></tr>
 * <tr><td>36</td><td>Cursor sensitivity exception</td></tr>
 * <tr><td>38</td><td>External routine exception</td></tr>
 * <tr><td>39</td><td>External routine invocation exception</td></tr>
 * <tr><td>3B</td><td>Savepoint exception</td></tr>
 * <tr><td>3C</td><td>Ambiguous cursor name</td></tr>
 * <tr><td>3D</td><td>Invalid catalog name</td></tr>
 * <tr><td>3F</td><td>Invalid schema name</td></tr>
 * <tr><td>40</td><td>Transaction rollback</td></tr>
 * <tr><td>42</td><td>Syntax error or access rule violation</td></tr>
 * <tr><td>44</td><td>With check option violation</td></tr>
 * <tr><td>HZ</td><td>Remote database access</td></tr>
 * </table>
 *
 * @author Lukas Eder
 */
public enum SQLStateClass {

    C00_SUCCESSFUL_COMPLETION("00"),
    C01_WARNING("01"),
    C02_NO_DATA("02"),
    C07_DYNAMIC_SQL_ERROR("07"),
    C08_CONNECTION_EXCEPTION("08"),
    C09_TRIGGERED_ACTION_EXCEPTION("09"),
    C0A_FEATURE_NOT_SUPPORTED("0A"),
    C0D_INVALID_TARGET_TYPE_SPECIFICATION("0D"),
    C0E_INVALID_SCHEMA_NAME_LIST_SPECIFICATION("0E"),
    C0F_LOCATOR_EXCEPTION("0F"),
    C0L_INVALID_GRANTOR("0L"),
    C0M_INVALID_SQL_INVOKED_PROCEDURE_REFERENCE("0M"),
    C0P_INVALID_ROLE_SPECIFICATION("0P"),
    C0S_INVALID_TRANSFORM_GROUP_NAME_SPECIFICATION("0S"),
    C0T_TARGET_TABLE_DISAGREES_WITH_CURSOR_SPECIFICATION("0T"),
    C0U_ATTEMPT_TO_ASSIGN_TO_NON_UPDATABLE_COLUMN("0U"),
    C0V_ATTEMPT_TO_ASSIGN_TO_ORDERING_COLUMN("0V"),
    C0W_PROHIBITED_STATEMENT_ENCOUNTERED_DURING_TRIGGER_EXECUTION("0W"),
    C0Z_DIAGNOSTICS_EXCEPTION("0Z"),
    C21_CARDINALITY_VIOLATION("21"),
    C22_DATA_EXCEPTION("22"),
    C23_INTEGRITY_CONSTRAINT_VIOLATION("23"),
    C24_INVALID_CURSOR_STATE("24"),
    C25_INVALID_TRANSACTION_STATE("25"),
    C26_INVALID_SQL_STATEMENT_NAME("26"),
    C27_TRIGGERED_DATA_CHANGE_VIOLATION("27"),
    C28_INVALID_AUTHORIZATION_SPECIFICATION("28"),
    C2B_DEPENDENT_PRIVILEGE_DESCRIPTORS_STILL_EXIST("2B"),
    C2C_INVALID_CHARACTER_SET_NAME("2C"),
    C2D_INVALID_TRANSACTION_TERMINATION("2D"),
    C2E_INVALID_CONNECTION_NAME("2E"),
    C2F_SQL_ROUTINE_EXCEPTION("2F"),
    C2H_INVALID_COLLATION_NAME("2H"),
    C30_INVALID_SQL_STATEMENT_IDENTIFIER("30"),
    C33_INVALID_SQL_DESCRIPTOR_NAME("33"),
    C34_INVALID_CURSOR_NAME("34"),
    C35_INVALID_CONDITION_NUMBER("35"),
    C36_CURSOR_SENSITIVITY_EXCEPTION("36"),
    C38_EXTERNAL_ROUTINE_EXCEPTION("38"),
    C39_EXTERNAL_ROUTINE_INVOCATION_EXCEPTION("39"),
    C3B_SAVEPOINT_EXCEPTION("3B"),
    C3C_AMBIGUOUS_CURSOR_NAME("3C"),
    C3D_INVALID_CATALOG_NAME("3D"),
    C3F_INVALID_SCHEMA_NAME("3F"),
    C40_TRANSACTION_ROLLBACK("40"),
    C42_SYNTAX_ERROR_OR_ACCESS_RULE_VIOLATION("42"),
    CHZ_REMOTE_DATABASE_ACCESS("HZ"),

    OTHER(""),
    NONE("")
    ;

    private static final Map<String, SQLStateClass> lookup = new HashMap<String, SQLStateClass>();
    private final String                            className;

    static {
        for (SQLStateClass clazz : SQLStateClass.values()) {
            lookup.put(clazz.className, clazz);
        }
    }

    private SQLStateClass(String className) {
        this.className = className;
    }

    public String className() {
        return className;
    }

    public static SQLStateClass fromCode(String code) {
        if (code == null || code.length() < 2)
            return SQLStateClass.OTHER;

        SQLStateClass result = lookup.get(code.substring(0, 2));
        return result != null ? result : SQLStateClass.OTHER;
    }
}
