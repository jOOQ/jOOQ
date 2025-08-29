/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.Tools.converterContext;
import static org.jooq.impl.Tools.getAnnotatedGetter;
import static org.jooq.impl.Tools.getAnnotatedMembers;
import static org.jooq.impl.Tools.getMatchingGetter;
import static org.jooq.impl.Tools.getMatchingMembers;
import static org.jooq.impl.Tools.hasColumnAnnotations;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.ConverterContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.RecordUnmapper;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import org.jooq.impl.DefaultRecordMapper.MappedMember;
import org.jooq.impl.DefaultRecordMapper.MappedMethod;

/**
 * A default implementation for unmapping a custom type to a {@link Record}.
 * <p>
 * This default implementation currently supports unmapping the following types
 * to a {@link Record}:
 * <p>
 * <ul>
 * <li>{@link Map}: See also {@link Record#fromMap(Map)}</li>
 * <li><code>Object[]</code>: See also {@link Record#fromArray(Object...)}</li>
 * <li>POJOs: See also {@link Record#from(Object)}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class DefaultRecordUnmapper<E, R extends Record> implements RecordUnmapper<E, R> {

    private final Class<? extends E>              type;
    private final RecordType<R>                   rowType;
    private final AbstractRow<R>                  row;
    private final Class<? extends AbstractRecord> recordType;
    private final Field<?>[]                      fields;
    private final Configuration                   configuration;
    private final ConverterContext                converterContext;
    private RecordUnmapper<E, R>                  delegate;

    public DefaultRecordUnmapper(Class<? extends E> type, RecordType<R> rowType, Configuration configuration) {
        this.type = type;
        this.rowType = rowType;
        this.row = Tools.row0((FieldsImpl<R>) rowType);
        this.recordType = Tools.recordType(rowType.size());
        this.fields = rowType.fields();
        this.configuration = configuration;
        this.converterContext = converterContext(configuration);

        init();
    }

    private final void init() {
        if (type.isArray())
            delegate = new ArrayUnmapper();
        else if (Map.class.isAssignableFrom(type))
            delegate = new MapUnmapper();
        else if (Iterable.class.isAssignableFrom(type))
            delegate = new IterableUnmapper();
        else if (Struct.class.isAssignableFrom(type))
            delegate = new StructUnmapper();
        else
            delegate = new PojoUnmapper();
    }

    @Override
    public final R unmap(E source) {
        return delegate.unmap(source);
    }

    private final Record newRecord() {
        return Tools.newRecord(false, configuration, recordType, row).operate(null);
    }

    private static final void setValue(Record record, Object source, java.lang.reflect.Field member, Field<?> field, ConverterContext converterContext)
        throws IllegalAccessException {

        Class<?> mType = member.getType();

        if (mType.isPrimitive()) {
            if (mType == byte.class)
                Tools.setValue(record, field, member.getByte(source), converterContext);
            else if (mType == short.class)
                Tools.setValue(record, field, member.getShort(source), converterContext);
            else if (mType == int.class)
                Tools.setValue(record, field, member.getInt(source), converterContext);
            else if (mType == long.class)
                Tools.setValue(record, field, member.getLong(source), converterContext);
            else if (mType == float.class)
                Tools.setValue(record, field, member.getFloat(source), converterContext);
            else if (mType == double.class)
                Tools.setValue(record, field, member.getDouble(source), converterContext);
            else if (mType == boolean.class)
                Tools.setValue(record, field, member.getBoolean(source), converterContext);
            else if (mType == char.class)
                Tools.setValue(record, field, member.getChar(source), converterContext);
        }
        else
            Tools.setValue(record, field, member.get(source), converterContext);
    }

    private final class ArrayUnmapper implements RecordUnmapper<E, R> {

        @SuppressWarnings({ "unchecked" })
        @Override
        public final R unmap(E source) {
            if (source instanceof Object[] array) {
                int size = rowType.size();
                AbstractRecord record = (AbstractRecord) newRecord();

                for (int i = 0; i < size && i < array.length; i++)
                    Tools.setValue(record, rowType.field(i), i, array[i], converterContext);

                return (R) record;
            }

            throw new MappingException("Object[] expected. Got: " + klass(source));
        }
    }

    private final class IterableUnmapper implements RecordUnmapper<E, R> {

        @SuppressWarnings({ "unchecked" })
        @Override
        public final R unmap(E source) {
            if (source instanceof Iterable<?> iterable) {
                Iterator<?> it = iterable.iterator();
                int size = rowType.size();
                AbstractRecord record = (AbstractRecord) newRecord();

                for (int i = 0; i < size && it.hasNext(); i++)
                    Tools.setValue(record, rowType.field(i), i, it.next(), converterContext);

                return (R) record;
            }

            throw new MappingException("Iterable expected. Got: " + klass(source));
        }
    }

    private final class StructUnmapper implements RecordUnmapper<E, R> {

        final ArrayUnmapper a = new ArrayUnmapper();

        @SuppressWarnings({ "unchecked" })
        @Override
        public final R unmap(E source) {
            if (source instanceof Struct s) {
                try {
                    return a.unmap((E) s.getAttributes());
                }
                catch (SQLException e) {
                    throw new DataAccessException("Error while reading Struct", e);
                }
            }

            throw new MappingException("Struct expected. Got: " + klass(source));
        }
    }

    private final class MapUnmapper implements RecordUnmapper<E, R> {

        @SuppressWarnings("unchecked")
        @Override
        public R unmap(E source) {

            // [#1987] Distinguish between various types to load data from
            // Maps are loaded using a {field-name -> value} convention
            if (source instanceof Map<?, ?> map) {
                Record record = newRecord();

                for (int i = 0; i < fields.length; i++) {
                    String name = fields[i].getName();

                    // Set only those values contained in the map
                    if (map.containsKey(name))
                        Tools.setValue(record, fields[i], map.get(name), converterContext);
                }

                return (R) record;
            }

            throw new MappingException("Map expected. Got: " + klass(source));
        }
    }

    private final class PojoUnmapper implements RecordUnmapper<E, R> {

        @SuppressWarnings("unchecked")
        @Override
        public R unmap(E source) {
            Record record = newRecord();

            try {
                boolean useAnnotations = hasColumnAnnotations(configuration, type);

                for (Field<?> field : fields) {
                    List<MappedMember> members;
                    MappedMethod method;

                    // Annotations are available and present
                    if (useAnnotations) {
                        members = getAnnotatedMembers(configuration, type, field, true);
                        method = getAnnotatedGetter(configuration, type, field, true);
                    }

                    // No annotations are present
                    else {
                        members = getMatchingMembers(configuration, type, field, true);
                        method = getMatchingGetter(configuration, type, field, true);
                    }

                    // Use only the first applicable method or member
                    if (method != null)
                        Tools.setValue(record, field, method.method().invoke(source), converterContext);
                    else if (members.size() > 0)
                        setValue(record, source, members.get(0).member(), field, converterContext);
                }

                return (R) record;
            }

            // All reflection exceptions are intercepted
            catch (Exception e) {
                throw new MappingException("An error occurred when mapping record from " + type, e);
            }
        }
    }

    private static final String klass(Object o) {
        return o == null ? "null" : o.getClass().toString();
    }
}
