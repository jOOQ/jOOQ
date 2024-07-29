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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;



/**
 * The <code>ROLLBACK</code> statement.
 */
@SuppressWarnings({ "hiding", "unused" })
final class Rollback
extends
    AbstractRowCountQuery
implements
    QOM.Rollback,
    RollbackToSavepointStep
{

    Name toSavepoint;

    Rollback(
        Configuration configuration
    ) {
        this(
            configuration,
            null
        );
    }

    Rollback(
        Configuration configuration,
        Name toSavepoint
    ) {
        super(configuration);

        this.toSavepoint = toSavepoint;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final Rollback toSavepoint(String toSavepoint) {
        return toSavepoint(DSL.name(toSavepoint));
    }

    @Override
    public final Rollback toSavepoint(Name toSavepoint) {
        this.toSavepoint = toSavepoint;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {











            default:
                ctx.visit(K_ROLLBACK);

                if (toSavepoint != null)
                    ctx.sql(' ').visit(K_TO).sql(' ').visit(K_SAVEPOINT).sql(' ').visit(toSavepoint);
                break;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Name $toSavepoint() {
        return toSavepoint;
    }

    @Override
    public final QOM.Rollback $toSavepoint(Name newValue) {
        return $constructor().apply(newValue);
    }

    public final Function1<? super Name, ? extends QOM.Rollback> $constructor() {
        return (a1) -> new Rollback(configuration(), a1);
    }





















}
