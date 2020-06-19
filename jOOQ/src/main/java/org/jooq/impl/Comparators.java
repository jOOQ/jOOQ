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

import static org.jooq.impl.DSL.noCondition;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Comparator;
import java.util.List;

import org.jooq.Check;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Key;
import org.jooq.Named;
import org.jooq.SortField;

/**
 * Commonly used comparators and related utilities.
 *
 * @author Lukas Eder
 */
final class Comparators {

    static final Comparator<Named>            NAMED_COMP       = new NamedComparator();
    static final Comparator<Key<?>>           KEY_COMP         = new KeyComparator();
    static final Comparator<ForeignKey<?, ?>> FOREIGN_KEY_COMP = new ForeignKeyComparator();
    static final Comparator<Check<?>>         CHECK_COMP       = new CheckComparator();
    static final Comparator<Index>            INDEX_COMP       = new IndexComparator();

    private static final class NamedComparator implements Comparator<Named> {
        @Override
        public final int compare(Named o1, Named o2) {
            return o1.getQualifiedName().compareTo(o2.getQualifiedName());
        }
    }

    private static final class KeyComparator implements Comparator<Key<?>> {
        @Override
        public int compare(Key<?> o1, Key<?> o2) {
            List<? extends Named> f1 = o1.getFields();
            List<? extends Named> f2 = o2.getFields();

            int c = f1.size() - f2.size();
            if (c != 0)
                return c;

            for (int i = 0; i < f1.size(); i++) {
                c = NAMED_COMP.compare(f1.get(i), f2.get(i));

                if (c != 0)
                    return c;
            }

            return 0;
        }
    }

    private static final class ForeignKeyComparator implements Comparator<ForeignKey<?, ?>> {
        @Override
        public int compare(ForeignKey<?, ?> o1, ForeignKey<?, ?> o2) {
            int c = KEY_COMP.compare(o1, o2);

            if (c != 0)
                return c;
            else
                return KEY_COMP.compare(o1.getKey(), o2.getKey());
        }
    }

    private static final class CheckComparator implements Comparator<Check<?>> {
        @Override
        public int compare(Check<?> o1, Check<?> o2) {
            return o1.condition().toString().compareTo(o2.condition().toString());
        }
    }

    private static final class IndexComparator implements Comparator<Index> {
        @Override
        public int compare(Index o1, Index o2) {
            int c;

            c = Boolean.valueOf(o1.getUnique()).compareTo(o2.getUnique());
            if (c != 0)
                return c;

            c = defaultIfNull(o1.getWhere(), noCondition()).toString().compareTo(defaultIfNull(o2.getWhere(), noCondition()).toString());
            if (c != 0)
                return c;

            List<SortField<?>> f1 = o1.getFields();
            List<SortField<?>> f2 = o2.getFields();

            c = f1.size() - f2.size();
            if (c != 0)
                return c;

            for (int i = 0; i < f1.size(); i++) {
                SortField<?> s1 = f1.get(i);
                SortField<?> s2 = f2.get(i);

                c = s1.getName().compareTo(s2.getName());
                if (c != 0)
                    return c;

                c = s1.getOrder().compareTo(s2.getOrder());
                if (c != 0)
                    return c;
            }

            return 0;
        }
    }
}
