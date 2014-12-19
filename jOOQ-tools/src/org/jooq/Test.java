/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq;

import static java.lang.System.out;
import static javax.xml.bind.JAXB.unmarshal;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

public class Test {
    public static void main(String[] args) {
        X x1 = unmarshal(new StringReader("<x></x>"), X.class);
        X x2 = unmarshal(new StringReader("<x><flags/></x>"), X.class);
        X x3 = unmarshal(new StringReader("<x><flags>X Y</flags></x>"), X.class);

        out.println("First unmarshal:");
        out.println(x1);
        out.println(x2);
        out.println(x3);

        // Marshal the xml again. This will add the <flags/> element
        StringWriter s1 = new StringWriter(); JAXB.marshal(x1, s1);
        StringWriter s2 = new StringWriter(); JAXB.marshal(x2, s2);
        StringWriter s3 = new StringWriter(); JAXB.marshal(x3, s3);
        
        // Now we're talking!
        x1 = unmarshal(new StringReader(s1.toString()), X.class);
        x2 = unmarshal(new StringReader(s2.toString()), X.class);
        x3 = unmarshal(new StringReader(s3.toString()), X.class);

        out.println();
        out.println("Second unmarshal:");
        out.println(x1);
        out.println(x2);
        out.println(x3);
    }
}

@XmlRootElement
class X {
    
    protected List<String> flags;
    
    @XmlList
    @XmlElement(defaultValue = "A B")
    public List<String> getFlags() {
        if (flags == null)
            flags = new ArrayList<>();
        
        return flags;
    }
    
    @Override
    public String toString() {
        return "X [flags=" + flags + "]";
    }
}