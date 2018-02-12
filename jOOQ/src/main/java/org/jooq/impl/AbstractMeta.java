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

import static org.jooq.impl.DSL.name;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Catalog;
import org.jooq.Meta;
import org.jooq.Name;
import org.jooq.Named;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
abstract class AbstractMeta implements Meta, Serializable {

    private static final long                  serialVersionUID = 910484713008245977L;

    private final Map<Name, Catalog>           cachedCatalogs;
    private final Map<Name, Schema>            cachedQualifiedSchemas;
    private final Map<Name, Table<?>>          cachedQualifiedTables;
    private final Map<Name, Sequence<?>>       cachedQualifiedSequences;
    private final Map<Name, List<Schema>>      cachedUnqualifiedSchemas;
    private final Map<Name, List<Table<?>>>    cachedUnqualifiedTables;
    private final Map<Name, List<Sequence<?>>> cachedUnqualifiedSequences;

    AbstractMeta() {

        // [#7165] TODO: Allow for opting out of this cache
        this.cachedCatalogs = new LinkedHashMap<Name, Catalog>();
        this.cachedQualifiedSchemas = new LinkedHashMap<Name, Schema>();
        this.cachedQualifiedTables = new LinkedHashMap<Name, Table<?>>();
        this.cachedQualifiedSequences = new LinkedHashMap<Name, Sequence<?>>();
        this.cachedUnqualifiedSchemas = new LinkedHashMap<Name, List<Schema>>();
        this.cachedUnqualifiedTables = new LinkedHashMap<Name, List<Table<?>>>();
        this.cachedUnqualifiedSequences = new LinkedHashMap<Name, List<Sequence<?>>>();
    }

    @Override
    public final Catalog getCatalog(String name) {
        return getCatalog(name(name));
    }

    @Override
    public final Catalog getCatalog(Name name) {
        if (cachedCatalogs.isEmpty())
            for (Catalog catalog : getCatalogs())
                cachedCatalogs.put(catalog.getQualifiedName(), catalog);

        return cachedCatalogs.get(name);
    }

    @Override
    public final List<Schema> getSchemas(String name) {
        return getSchemas(name(name));
    }

    @Override
    public final List<Schema> getSchemas(Name name) {
        return get(name, new Iterable<Schema>() {
            @Override
            public Iterator<Schema> iterator() {
                return getSchemas().iterator();
            }
        }, cachedQualifiedSchemas, cachedUnqualifiedSchemas);
    }

    @Override
    public final List<Table<?>> getTables(String name) {
        return getTables(name(name));
    }

    @Override
    public final List<Table<?>> getTables(Name name) {
        return get(name, new Iterable<Table<?>>() {
            @Override
            public Iterator<Table<?>> iterator() {
                return getTables().iterator();
            }
        }, cachedQualifiedTables, cachedUnqualifiedTables);
    }

    @Override
    public final List<Sequence<?>> getSequences(String name) {
        return getSequences(name(name));
    }

    @Override
    public final List<Sequence<?>> getSequences(Name name) {
        return get(name, new Iterable<Sequence<?>>() {
            @Override
            public Iterator<Sequence<?>> iterator() {
                return getSequences().iterator();
            }
        }, cachedQualifiedSequences, cachedUnqualifiedSequences);
    }

    private final <T extends Named> List<T> get(Name name, Iterable<T> i, Map<Name, T> qualified, Map<Name, List<T>> unqualified) {
        if (qualified.isEmpty()) {
            for (T object : i) {
                Name q = object.getQualifiedName();
                Name u = object.getUnqualifiedName();

                qualified.put(q, object);

                List<T> list = unqualified.get(u);
                if (list == null) {
                    list = new ArrayList<T>();
                    unqualified.get(u);
                }

                list.add(object);
            }
        }

        T object = qualified.get(name);
        if (object != null)
            return Collections.singletonList(object);

        List<T> list = unqualified.get(name);
        if (list == null)
            return Collections.emptyList();
        else
            return Collections.unmodifiableList(list);
    }
}
