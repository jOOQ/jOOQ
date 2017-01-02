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
package org.jooq.tools.jdbc;

import static java.lang.reflect.Array.newInstance;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;

/**
 * A mock {@link Array}.
 *
 * @author Lukas Eder
 * @see MockConnection
 */
public class MockArray<T> implements Array {

    private final SQLDialect           dialect;
    private final T[]                  array;
    private final Class<? extends T[]> type;

    public MockArray(SQLDialect dialect, T[] array, Class<? extends T[]> type) {
        this.dialect = dialect;
        this.array = array;
        this.type = type;
    }

    @Override
    public String getBaseTypeName() {
        return DefaultDataType.getDataType(dialect, type.getComponentType()).getTypeName();
    }

    @Override
    public int getBaseType() {
        return DefaultDataType.getDataType(dialect, type.getComponentType()).getSQLType();
    }

    @Override
    public T[] getArray() {
        return array;
    }

    @Override
    public T[] getArray(Map<String, Class<?>> map) {
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] getArray(long index, int count) throws SQLException {
        if (index - 1 > Integer.MAX_VALUE)
            throw new SQLException("Cannot access array indexes beyond Integer.MAX_VALUE");

        return array == null ? null : Arrays
            .asList(array)
            .subList(((int) index) - 1, ((int) index) - 1 + count)
            .toArray((T[]) newInstance(array.getClass().getComponentType(), count));
    }

    @Override
    public T[] getArray(long index, int count, Map<String, Class<?>> map) throws SQLException {
        return getArray(index, count);
    }

    @Override
    public ResultSet getResultSet() {
        return getResultSet0(array);
    }

    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map) {
        return getResultSet();
    }

    @Override
    public ResultSet getResultSet(long index, int count) throws SQLException {
        return getResultSet0(getArray(index, count));
    }

    @Override
    public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
        return getResultSet(index, count);
    }

    @SuppressWarnings("unchecked")
    private ResultSet getResultSet0(T[] a) {
        DSLContext create = DSL.using(dialect);

        Field<Long> index = field(name("INDEX"), Long.class);
        Field<T> value = (Field<T>) field(name("VALUE"), type.getComponentType());
        Result<Record2<Long, T>> result = create.newResult(index, value);

        for (int i = 0; i < a.length; i++) {
            Record2<Long, T> record = create.newRecord(index, value);

            record.setValue(index, i + 1L);
            record.setValue(value, a[i]);

            result.add(record);
        }

        return new MockResultSet(result);
    }

    @Override
    public void free() {
    }
}
