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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.impl.QueryPartCollectionView.wrap;

import java.util.Map;
import java.util.function.Function;

import org.jooq.Context;
import org.jooq.QueryPart;

/**
 * @author Lukas Eder
 */
final class QueryPartMapView<K extends QueryPart, V extends QueryPart>
extends
    AbstractQueryPartMap<K, V>
{

    QueryPartMapView(Map<K, V> map) {
        super(map);
    }

    // -------------------------------------------------------------------------
    // The QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {

        // This is a placeholder implementation without any real SQL usage
        ctx.visit(wrap($tuples()));
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final Function<? super Map<K, V>, ? extends AbstractQueryPartMap<K, V>> $construct() {
        return QueryPartMapView::new;
    }
}
