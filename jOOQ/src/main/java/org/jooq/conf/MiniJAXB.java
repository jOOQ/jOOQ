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
package org.jooq.conf;

import java.io.StringWriter;

import javax.xml.bind.JAXB;

/**
 * This class allows for mashalling / unmarshalling XML content to jOOQ
 * configuration objects.
 * <p>
 * With jOOQ 3.12, the JAXB dependency has been removed in favour of this home
 * grown solution. Due to the modularisation that happened with JDK 9+ and the
 * removal of JAXB from the JDK 11+, it is unreasonable to leave the burden of
 * properly configuring transitive JAXB dependency to jOOQ users.
 *
 * @author Lukas Eder
 */
public class MiniJAXB {

    /**
     * This method is used internally by jOOQ to patch XML content in order to
     * work around a bug in JAXB.
     * <p>
     * [#7579] [#8044] On JDK 9, 10, depending on how JAXB is loaded onto the
     * classpath / module path, the xmlns seems to be considered for
     * (un)marshalling, or not. This seems to be a bug in JAXB, with no known
     * tracking ID as of yet.
     * <p>
     * The following quick fix tests the presence of the xmlns when marshalling,
     * and if absent removes it prior to unmarshalling.
     */
    public static String jaxbNamespaceBugWorkaround(String xml, Object annotated) {
        StringWriter test = new StringWriter();

        try {
            JAXB.marshal(annotated, test);

            if (!test.toString().contains("xmlns"))
                xml = xml.replaceAll("xmlns=\"[^\"]*\"", "");
        }
        catch (Exception ignore) {}

        return xml;
    }
}
