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
package org.jooq.util.sqlite.sqlite_master;


import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.jooq.util.sqlite.SQLiteDataType;


/**
 * This class is NOT generated, as SQLite does not support meta-querying the
 * meta-model.
 *
 * @author Lukas Eder
 */
public class SQLiteMaster extends TableImpl<Record> {

    private static final long serialVersionUID = -823335201;

    /**
     * The singleton instance of tables
     */
    public static final SQLiteMaster                SQLITE_MASTER = new SQLiteMaster();

    public static final TableField<Record, String>  TYPE          = createField("type",     SQLiteDataType.VARCHAR, SQLITE_MASTER);
    public static final TableField<Record, String>  NAME          = createField("name",     SQLiteDataType.VARCHAR, SQLITE_MASTER);
    public static final TableField<Record, String>  TBL_NAME      = createField("tbl_name", SQLiteDataType.VARCHAR, SQLITE_MASTER);
    public static final TableField<Record, Integer> ROOTPAGE      = createField("rootpage", SQLiteDataType.INTEGER, SQLITE_MASTER);
    public static final TableField<Record, String>  SQL           = createField("sql",      SQLiteDataType.VARCHAR, SQLITE_MASTER);

    /**
     * No further instances allowed
     */
    private SQLiteMaster() {
        super("sqlite_master");
    }
}
