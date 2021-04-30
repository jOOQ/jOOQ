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

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.RowCountQuery;
import org.jooq.impl.R2DBC.BlockingRowCountSubscription;
import org.jooq.impl.R2DBC.QuerySubscription;
import org.jooq.impl.R2DBC.RowCountSubscriber;

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
        ConnectionFactory cf = configuration().connectionFactory();

        if (!(cf instanceof NoConnectionFactory))
            subscriber.onSubscribe(new QuerySubscription<>(this, subscriber, (t, u) -> new RowCountSubscriber(u)));
        else
            subscriber.onSubscribe(new BlockingRowCountSubscription(this, subscriber));
    }
}
