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
package org.jooq.impl;

import java.util.Arrays;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.ExecuteType;
import org.jooq.Record;
import org.jooq.RecordContext;
import org.jooq.RecordType;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;

/**
 * A default implementation for {@link RecordContext}.
 *
 * @author Lukas Eder
 */
class DefaultRecordContext implements RecordContext {

    private final Configuration       configuration;
    private final Map<Object, Object> data;
    private final ExecuteType         type;
    private final Record[]            records;
    Exception                         exception;

    DefaultRecordContext(Configuration configuration, ExecuteType type, Record... records) {
        this.configuration = configuration;
        this.type = type;
        this.data = new DataMap();
        this.records = records;
    }

    @Override
    public final Map<Object, Object> data() {
        return data;
    }

    @Override
    public final Object data(Object key) {
        return data.get(key);
    }

    @Override
    public final Object data(Object key, Object value) {
        return data.put(key, value);
    }

    @Override
    public final Configuration configuration() {
        return configuration;
    }

    @Override
    public final Settings settings() {
        return Tools.settings(configuration());
    }

    @Override
    public final SQLDialect dialect() {
        return Tools.configuration(configuration()).dialect();
    }

    @Override
    public final SQLDialect family() {
        return dialect().family();
    }

    @Override
    public final ExecuteType type() {
        return type;
    }

    @Override
    public final Record record() {
        return records != null && records.length > 0 ? records[0] : null;
    }

    @Override
    public final Record[] batchRecords() {
        return records;
    }

    @Override
    public final RecordType<?> recordType() {
        Record record = record();
        return record != null ? new Fields<Record>(record.fields()) : null;
    }

    @Override
    public final Exception exception() {
        return exception;
    }

    @Override
    public String toString() {
        if (records != null && records.length > 0) {
            Result<Record> result = DSL.using(configuration).newResult(records[0].fields());
            result.addAll(Arrays.asList(records));
            return result.toString();
        }
        else {
            return "No Records";
        }
    }
}
