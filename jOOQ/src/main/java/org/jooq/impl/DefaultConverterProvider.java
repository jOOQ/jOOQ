/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.Converters.inverse;
import static org.jooq.impl.Tools.EMPTY_CLASS;
import static org.jooq.tools.StringUtils.rightPad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jooq.Converter;
import org.jooq.ConverterProvider;
import org.jooq.Converters;

/**
 * @author Lukas Eder
 * @deprecated - This API is still EXPERIMENTAL. Do not use yet
 */
@Deprecated
public class DefaultConverterProvider implements ConverterProvider {

    final Graph graph = new Graph();

    @SuppressWarnings("unchecked")
    @Override
    public <T, U> Converter<T, U> provide(Class<T> tType, Class<U> uType) {
        if (tType == uType)
            return (Converter<T, U>) Converters.identity(tType);
        else
            return graph.get(new Endpoints<T, U>(tType, uType));
    }

    public <T, U> void add(Converter<T, U> converter) {
        graph.add(converter);
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    /**
     * A graph modelling all the possible conversion paths.
     */
    static class Graph {

        final Set<Class<?>>                       vertices;
        final Map<Class<?>, Set<Converter<?, ?>>> adjacency;
        final Map<Endpoints<?, ?>, Converter<?, ?>> paths;

        Graph() {
            vertices = new HashSet<Class<?>>();
            adjacency = new HashMap<Class<?>, Set<Converter<?, ?>>>();
            paths = new ConcurrentHashMap<Endpoints<?, ?>, Converter<?, ?>>();
        }

        /**
         * Get a converter for a pair of classes.
         */
        @SuppressWarnings("unchecked")
        <T, U> Converter<T, U> get(Endpoints<T, U> classes) {
            build();
            return (Converter<T, U>) paths.get(classes);
        }

        /**
         * Add a Converter to the graph.
         */
        <T, U> void add(Converter<T, U> converter) {
            synchronized (paths) {
                paths.clear();

                Class<T> t = converter.fromType();
                Class<U> u = converter.toType();

                vertices.add(t);
                vertices.add(u);

                Set<Converter<?, ?>> tSet = adjacency.get(t);
                if (tSet == null) {
                    tSet = new HashSet<Converter<?, ?>>();
                    adjacency.put(t, tSet);
                }
                tSet.add(converter);

                Set<Converter<?, ?>> uSet = adjacency.get(u);
                if (uSet == null) {
                    uSet = new HashSet<Converter<?, ?>>();
                    adjacency.put(u, uSet);
                }
                uSet.add(inverse(converter));
            }
        }

        /**
         * Build the graph.
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private void build() {
            if (paths.isEmpty()) {
                synchronized (paths) {
                    for (Entry<Class<?>, Set<Converter<?, ?>>> entry : adjacency.entrySet()) {
                        for (Converter<?, ?> converter : entry.getValue()) {
                            path(new Endpoints(converter.fromType(), converter.toType()), converter);
                        }
                    }

                    int size;
                    do {
                        size = paths.size();

                        List<Endpoints<?, ?>> keys = new ArrayList<Endpoints<?, ?>>(paths.keySet());
                        for (Endpoints<?, ?> key : keys) {
                            for (Converter<?, ?> converter : adjacency.get(key.toType)) {
                                path(new Endpoints(key.fromType, converter.toType()), Converters.of((Converter) paths.get(key), converter));
                            }
                        }
                    }
                    while (size < paths.size());
                }
            }
        }

        /**
         * Add a path configuration if there isn't already one for the given
         * endpoints.
         */
        private void path(Endpoints<?, ?> key, Converter<?, ?> converter) {
            if (key.fromType != key.toType)
                if (!paths.containsKey(key))
                    paths.put(key, converter);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public String toString() {
            build();

            synchronized (paths) {
                StringBuilder sb = new StringBuilder();

                Class<?>[] classes = vertices.toArray(EMPTY_CLASS);
                Arrays.sort(classes, new Comparator<Class<?>>() {
                    @Override
                    public int compare(Class<?> o1, Class<?> o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                int maxLength = Integer.MIN_VALUE;
                for (Class<?> c : classes)
                    maxLength = Math.max(maxLength, c.getName().length());

                String sep1 = "";
                for (Class<?> c1 : classes) {
                    sb.append(sep1);
                    sb.append(rightPad(c1.getName(), maxLength));

                    for (Class<?> c2 : classes) {
                        if (paths.containsKey(new Endpoints(c1, c2))) {
                            sb.append("\n -> ")
                              .append(c2.getName());
                        }
                    }

                    sep1 = "\n\n";
                }

                return sb.toString();
            }
        }
    }

    /**
     * A type modelling two end points inside of a graph.
     */
    static class Endpoints<T, U> {
        final Class<T> fromType;
        final Class<U> toType;

        Endpoints(Class<T> t, Class<U> u) {
            this.fromType = t;
            this.toType = u;
        }

        @Override
        public int hashCode() {
            return 17 * fromType.hashCode() + toType.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj instanceof Endpoints) {
                Endpoints<?, ?> that = (Endpoints<?, ?>) obj;
                return fromType == that.fromType && toType == that.toType;
            }

            return false;
        }

        @Override
        public String toString() {
            return "(" + fromType.getName() + ", " + toType.getName() + ")";
        }
    }
}
