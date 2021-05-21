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
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.Tools.findAny;
import static org.jooq.tools.reflect.Reflect.wrapper;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.sql.Struct;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

// ...
import org.jooq.Converter;
import org.jooq.ConverterProvider;
import org.jooq.EnumType;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.QualifiedRecord;
import org.jooq.Record;
import org.jooq.XML;
import org.jooq.exception.DataTypeException;

import org.jetbrains.annotations.Nullable;

/**
 * A default converter provider offering the functionality of {@link Convert}.
 *
 * @author Lukas Eder
 */
public final class DefaultConverterProvider implements ConverterProvider, Serializable {

    @Nullable
    @Override
    public final <T, U> Converter<T, U> provide(final Class<T> tType, final Class<U> uType) {
        Class<T> tWrapper = wrapper(tType);
        Class<U> uWrapper = wrapper(uType);

        // TODO: [#10071] These checks are required to be able to return null in
        //                case this implementation cannot produce a Converter.
        //                It corresponds to a super set of what org.jooq.tools.Convert
        //                can do. There is certainly room for refactoring the two
        //                classes.
        if (tWrapper == uWrapper
            || uWrapper.isAssignableFrom(tWrapper)
            || isCollection(tWrapper) && isCollection(uWrapper)
            || tWrapper == Optional.class
            || uWrapper == Optional.class
            || uWrapper == String.class
            || uWrapper == byte[].class
            || Number.class.isAssignableFrom(uWrapper) // No fail-fast implemented yet!
            || Boolean.class.isAssignableFrom(uWrapper) // No fail-fast implemented yet!
            || Character.class.isAssignableFrom(uWrapper)
            || uWrapper == URI.class && tWrapper == String.class
            || uWrapper == URL.class && tWrapper == String.class
            || uWrapper == File.class && tWrapper == String.class
            || isDate(tWrapper) && isDate(uWrapper)
            || isEnum(tWrapper) && isEnum(uWrapper)
            || isUUID(tWrapper) && isUUID(uWrapper)

            // [#10072] out of the box JSON binding is supported via Jackson or Gson
            || isJSON(tWrapper)

            // [#10072] out of the box XML binding is supported via JAXB
            || isXML(tWrapper)

            || Record.class.isAssignableFrom(tWrapper)
            || Struct.class.isAssignableFrom(tWrapper) && QualifiedRecord.class.isAssignableFrom(uWrapper)

            // [#10229] Any type A can be converted into its wrapper B if a constructor B(A) exists.
            || findAny(uWrapper.getDeclaredConstructors(), c -> {
                Class<?>[] types = c.getParameterTypes();

                // [#11183] Prevent StackOverflowError when recursing into UDT POJOs
                return types.length == 1 && types[0] != uWrapper && provide(tType, types[0]) != null;
            }) != null
        ) {
            return new AbstractConverter<T, U>(tType, uType) {

                @Override
                public U from(T t) {
                    return Convert.convert(t, uType);
                }

                @Override
                public T to(U u) {
                    return Convert.convert(u, tType);
                }
            };
        }

        // [#11762] Make sure possibly legal downcasts / upcasts are working
        //          This is especially important if we don't know the data type
        //          (SQLDataType.OTHER)
        else if (tWrapper.isAssignableFrom(uWrapper)) {
            return Converter.ofNullable(
                tWrapper,
                uWrapper,
                t -> {
                    if (uWrapper.isInstance(t))
                        return uWrapper.cast(t);
                    else
                        throw new DataTypeException("Cannot cast from " + tWrapper + " (instance type: " + t.getClass() + " to " + tWrapper);
                },
                u -> tWrapper.cast(u)
            );
        }
        else
            return null;
    }

    private final boolean isJSON(Class<?> type) {
        return type == JSON.class
            || type == JSONB.class;
    }

    private final boolean isXML(Class<?> type) {
        return type == XML.class;
    }

    private final boolean isUUID(Class<?> type) {
        return type == String.class
            || type == byte[].class
            || type == UUID.class;
    }

    private final boolean isEnum(Class<?> type) {
        return Enum.class.isAssignableFrom(type)
            || type == String.class
            || EnumType.class.isAssignableFrom(type);
    }

    private final boolean isDate(Class<?> type) {
        return java.util.Date.class.isAssignableFrom(type)
            || Calendar.class.isAssignableFrom(type)
            || Temporal.class.isAssignableFrom(type)
            || type == Long.class
            || type == String.class;
    }

    private final boolean isCollection(Class<?> type) {
        return type.isArray()
            || Collection.class.isAssignableFrom(type)



            // [#3443] Conversion from Object[] to JDBC Array
            || type == java.sql.Array.class;
    }
}
