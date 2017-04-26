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

import static org.jooq.impl.Tools.getAnnotatedGetter;
import static org.jooq.impl.Tools.getAnnotatedMembers;
import static org.jooq.impl.Tools.getMatchingGetter;
import static org.jooq.impl.Tools.getMatchingMembers;
import static org.jooq.impl.Tools.hasColumnAnnotations;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.RecordUnmapper;
import org.jooq.exception.MappingException;

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

    private final Class<? extends E> type;
    private final RecordType<R>      rowType;
    private final Field<?>[]         fields;
    private final Configuration      configuration;
    private RecordUnmapper<E, R>     delegate;

    public DefaultRecordUnmapper(Class<? extends E> type, RecordType<R> rowType, Configuration configuration) {
        this.type = type;
        this.rowType = rowType;
        this.fields = rowType.fields();
        this.configuration = configuration;

        init();
    }

    private final void init() {
        if (type.isArray())
            delegate = new ArrayUnmapper();
        else if (Map.class.isAssignableFrom(type))
            delegate = new MapUnmapper();
        else
            delegate = new PojoUnmapper();
    }

    @Override
    public final R unmap(E source) {
        return delegate.unmap(source);
    }

    private final Record newRecord() {
        return DSL.using(configuration).newRecord(rowType.fields());
    }

    private static final void setValue(Record record, Object source, java.lang.reflect.Field member, Field<?> field)
        throws IllegalAccessException {

        Class<?> mType = member.getType();

        if (mType.isPrimitive()) {
            if (mType == byte.class) {
                Tools.setValue(record, field, member.getByte(source));
            }
            else if (mType == short.class) {
                Tools.setValue(record, field, member.getShort(source));
            }
            else if (mType == int.class) {
                Tools.setValue(record, field, member.getInt(source));
            }
            else if (mType == long.class) {
                Tools.setValue(record, field, member.getLong(source));
            }
            else if (mType == float.class) {
                Tools.setValue(record, field, member.getFloat(source));
            }
            else if (mType == double.class) {
                Tools.setValue(record, field, member.getDouble(source));
            }
            else if (mType == boolean.class) {
                Tools.setValue(record, field, member.getBoolean(source));
            }
            else if (mType == char.class) {
                Tools.setValue(record, field, member.getChar(source));
            }
        }
        else {
            Tools.setValue(record, field, member.get(source));
        }
    }

    private final class ArrayUnmapper implements RecordUnmapper<E, R> {

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public final R unmap(E source) {
            if (source == null)
                return null;

            if (source instanceof Object[]) {
                Object[] array = (Object[]) source;
                int size = rowType.size();
                Record record = newRecord();

                for (int i = 0; i < size && i < array.length; i++) {
                    Field field = rowType.field(i);

                    if (rowType.field(field) != null)
                        Tools.setValue(record, field, array[i]);
                }

                return (R) record;
            }

            throw new MappingException("Object[] expected. Got: " + source.getClass());
        }
    }

    private final class MapUnmapper implements RecordUnmapper<E, R> {

        @SuppressWarnings("unchecked")
        @Override
        public R unmap(E source) {
            if (source == null)
                return null;

            // [#1987] Distinguish between various types to load data from
            // Maps are loaded using a {field-name -> value} convention
            if (source instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) source;
                Record record = newRecord();

                for (int i = 0; i < fields.length; i++) {
                    String name = fields[i].getName();

                    // Set only those values contained in the map
                    if (map.containsKey(name))
                        Tools.setValue(record, fields[i], map.get(name));
                }

                return (R) record;
            }

            throw new MappingException("Map expected. Got: " + source.getClass());
        }
    }

    private final class PojoUnmapper implements RecordUnmapper<E, R> {

        @SuppressWarnings("unchecked")
        @Override
        public R unmap(E source) {
            if (source == null)
                return null;

            Record record = newRecord();

            try {
                boolean useAnnotations = hasColumnAnnotations(configuration, type);

                for (Field<?> field : fields) {
                    List<java.lang.reflect.Field> members;
                    Method method;

                    // Annotations are available and present
                    if (useAnnotations) {
                        members = getAnnotatedMembers(configuration, type, field.getName());
                        method = getAnnotatedGetter(configuration, type, field.getName());
                    }

                    // No annotations are present
                    else {
                        members = getMatchingMembers(configuration, type, field.getName());
                        method = getMatchingGetter(configuration, type, field.getName());
                    }

                    // Use only the first applicable method or member
                    if (method != null) {
                        Tools.setValue(record, field, method.invoke(source));
                    }
                    else if (members.size() > 0) {
                        setValue(record, source, members.get(0), field);
                    }
                }

                return (R) record;
            }

            // All reflection exceptions are intercepted
            catch (Exception e) {
                throw new MappingException("An error ocurred when mapping record from " + type, e);
            }
        }
    }
}
