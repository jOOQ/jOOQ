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
package org.jooq;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lukas Eder
 */
final class EnumTypes {

    private static final Map<Class<?>, Map<String, ? extends EnumType>> LOOKUP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    // Avoid intersection type because of Eclipse compiler bug:
    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=577466
    static <E extends /* Enum<E> & */ EnumType> E lookupLiteral(Class<E> enumType, String literal) {
        return (E) LOOKUP.computeIfAbsent(enumType, t -> stream(enumType.getEnumConstants()).collect(toMap(E::getLiteral, identity()))).get(literal);
    }

    private EnumTypes() {}
}