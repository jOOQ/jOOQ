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

import java.io.StringReader;
import java.io.StringWriter;

import org.jooq.XML;

import jakarta.xml.bind.JAXB;

/**
 * A base class for {@link XML} to JAXB POJO conversion.
 *
 * @author Lukas Eder
 */
public class XMLtoJAXBConverter<U> extends AbstractConverter<XML, U> {

    public XMLtoJAXBConverter(Class<U> toType) {
        super(XML.class, toType);
    }

    @Override
    public U from(XML databaseObject) {
        if (databaseObject == null)
            return null;
        else
            return JAXB.unmarshal(new StringReader(databaseObject.data()), toType());
    }

    @Override
    public XML to(U userObject) {
        if (userObject == null) {
            return null;
        }
        else {
            StringWriter w = new StringWriter();
            JAXB.marshal(userObject, w);
            return XML.xml(w.toString());
        }
    }
}
