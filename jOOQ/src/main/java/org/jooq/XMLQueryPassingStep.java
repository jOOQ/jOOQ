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
package org.jooq;

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;

/**
 * A step in the construction of an <code>XMLQUERY</code> expression.
 *
 * @author Lukas Eder
 */
public interface XMLQueryPassingStep {

    /**
     * Add the <code>PASSING</code> clause to the <code>XMLQUERY</code>
     * expression.
     */
    @Support({ POSTGRES })
    Field<XML> passing(XML xml);

    /**
     * Add the <code>PASSING</code> clause to the <code>XMLQUERY</code>
     * expression.
     */
    @Support({ POSTGRES })
    Field<XML> passing(Field<XML> xml);

    /**
     * Add the <code>PASSING BY REF</code> clause to the <code>XMLQUERY</code>
     * expression.
     */
    @Support({ POSTGRES })
    Field<XML> passingByRef(XML xml);

    /**
     * Add the <code>PASSING BY REF</code> clause to the <code>XMLQUERY</code>
     * expression.
     */
    @Support({ POSTGRES })
    Field<XML> passingByRef(Field<XML> xml);


















}
