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

import static org.jooq.impl.R2DBC.mapping;
import static org.jooq.impl.R2DBC.rowCountSubscriber;

import java.util.function.Function;

import org.jooq.Configuration;
import org.jooq.Publisher;
import org.jooq.Record;
import org.jooq.RowCountQuery;
import org.jooq.impl.R2DBC.BlockingRowCountSubscription;
import org.jooq.impl.R2DBC.QuerySubscription;
import org.jooq.impl.R2DBC.RowCount;

import org.reactivestreams.Subscriber;

import io.r2dbc.spi.ConnectionFactory;

/**
 * @author Lukas Eder
 */
abstract class AbstractRowCountQuery extends AbstractQuery<Record> implements RowCountQuery {

    AbstractRowCountQuery(Configuration configuration) {
        super(configuration);
    }

    @Override
    public final void subscribe(Subscriber<? super Integer> subscriber) {
        subscribe0(subscriber, RowCount::rows, false);
    }

    @Override
    public Publisher<Long> largePublisher() {
        return s -> subscribe0(s, RowCount::largeRows, true);
    }

    final <R> void subscribe0(
        Subscriber<? super R> subscriber,
        Function<? super RowCount, ? extends R> extract,
        boolean large
    ) {
        Configuration c = configuration();
        ConnectionFactory cf = c.connectionFactory();
        Subscriber<? super RowCount> rowCountSubscriber = mapping(subscriber, extract, c.subscriberProvider());

        if (!(cf instanceof NoConnectionFactory))
            subscriber.onSubscribe(new QuerySubscription<>(this, rowCountSubscriber, (t, u, s) -> rowCountSubscriber(u, s)));
        else
            subscriber.onSubscribe(new BlockingRowCountSubscription(this, rowCountSubscriber, large));
    }
}
