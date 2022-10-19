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

// ...
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.xmlparseDocument;
import static org.jooq.impl.DSL.xmlquery;
import static org.jooq.impl.Keywords.K_CONTENT;
import static org.jooq.impl.Keywords.K_DOCUMENT;
import static org.jooq.impl.Keywords.K_PRESERVE;
import static org.jooq.impl.Keywords.K_WHITESPACE;
import static org.jooq.impl.Names.N_XMLPARSE;
import static org.jooq.impl.QOM.DocumentOrContent.DOCUMENT;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.QueryPart;
// ...
// ...
import org.jooq.XML;
import org.jooq.impl.QOM.DocumentOrContent;


/**
 * @author Lukas Eder
 */
final class XMLParse extends AbstractField<XML> implements QOM.XMLParse {
    private final Field<String>     content;
    private final DocumentOrContent documentOrContent;

    XMLParse(Field<String> content, DocumentOrContent documentOrContent) {
        super(N_XMLPARSE, SQLDataType.XML);

        this.content = content;
        this.documentOrContent = documentOrContent;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {





















            case POSTGRES:
            default:
                acceptStandard(ctx, documentOrContent, content);
                break;
        }
    }

    private static final void acceptStandard(
        Context<?> ctx,
        DocumentOrContent documentOrContent,
        Field<String> content
    ) {
        ctx.visit(N_XMLPARSE).sql('(')
           .visit(documentOrContent == DOCUMENT ? K_DOCUMENT : K_CONTENT).sql(' ')
           .visit(content);






        ctx.sql(')');
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $content() {
        return content;
    }

    @Override
    public final DocumentOrContent $documentOrContent() {
        return documentOrContent;
    }














}
