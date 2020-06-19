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

import static org.jooq.impl.Keywords.K_XMLEXISTS;
import static org.jooq.impl.XMLPassingMechanism.BY_REF;
import static org.jooq.impl.XMLPassingMechanism.BY_VALUE;
import static org.jooq.impl.XMLTable.acceptPassing;
import static org.jooq.impl.XMLTable.acceptXPath;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.XML;
import org.jooq.XMLExistsPassingStep;

/**
 * @author Lukas Eder
 */
final class XMLExists extends AbstractCondition implements XMLExistsPassingStep {

    /**
     * Generated UID
     */
    private static final long         serialVersionUID = -4881363881968319258L;
    private final Field<String>       xpath;
    private final Field<XML>          passing;
    private final XMLPassingMechanism passingMechanism;

    XMLExists(Field<String> xpath) {
        this(xpath, null, null);
    }

    private XMLExists(Field<String> xpath, Field<XML> passing, XMLPassingMechanism passingMechanism) {
        this.xpath = xpath;
        this.passing = passing;
        this.passingMechanism = passingMechanism;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------
    @Override
    public final Condition passing(XML xml) {
        return passing(Tools.field(xml));
    }

    @Override
    public final Condition passing(Field<XML> xml) {
        return new XMLExists(xpath, xml, null);
    }

    @Override
    public final Condition passingByRef(XML xml) {
        return passingByRef(Tools.field(xml));
    }

    @Override
    public final Condition passingByRef(Field<XML> xml) {
        return new XMLExists(xpath, xml, BY_REF);
    }

    @Override
    public final Condition passingByValue(XML xml) {
        return passingByRef(Tools.field(xml));
    }

    @Override
    public final Condition passingByValue(Field<XML> xml) {
        return new XMLExists(xpath, xml, BY_VALUE);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(K_XMLEXISTS).sql('(')
           .formatIndentStart()
           .formatNewLine();

        acceptXPath(ctx, xpath);
        acceptPassing(ctx, passing, passingMechanism);

        ctx.formatIndentEnd()
           .formatNewLine()
           .sql(')');
    }
}
