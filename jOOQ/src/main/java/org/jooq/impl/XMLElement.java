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

import static org.jooq.impl.Keywords.K_NAME;
import static org.jooq.impl.Names.N_XMLCONCAT;
import static org.jooq.impl.Names.N_XMLELEMENT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.XML;
import org.jooq.XMLAttributes;

/**
 * @author Lukas Eder
 */
final class XMLElement extends AbstractField<XML> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = 4505809303211506197L;
    private final Name                    elementName;
    private final XMLAttributes           attributes;
    private final QueryPartList<Field<?>> content;

    XMLElement(Name elementName, XMLAttributes attributes, Collection<? extends Field<?>> content) {
        super(N_XMLCONCAT, SQLDataType.XML);

        this.elementName = elementName;
        this.attributes = attributes;
        this.content = new QueryPartList<>(content);
    }

    @Override
    public final void accept(Context<?> ctx) {
        boolean hasAttributes = attributes != null && !((XMLAttributesImpl) attributes).attributes.isEmpty();
        boolean hasContent = !content.isEmpty();
        boolean format =
               hasAttributes && ((XMLAttributesImpl) attributes).attributes.size() > 1
            || hasContent && content.size() > 1;
        Object previous = ctx.data(DATA_LIST_ALREADY_INDENTED);

        ctx.visit(N_XMLELEMENT).sql('(');

        if (format) {
            ctx.formatIndentStart()
               .formatNewLine();
            ctx.data(DATA_LIST_ALREADY_INDENTED, true);
        }

        ctx.visit(K_NAME).sql(' ').visit(elementName);

        if (hasAttributes)
            if (format)
                ctx.sql(',').formatSeparator().visit(attributes);
            else
                ctx.sql(", ").visit(attributes);

        if (hasContent)
            if (format)
                ctx.sql(',').formatSeparator().visit(content);
            else
                ctx.sql(", ").visit(content);

        if (format) {
            ctx.formatIndentEnd()
               .formatNewLine();
            ctx.data(DATA_LIST_ALREADY_INDENTED, previous);
        }

        ctx.sql(')');
    }
}
