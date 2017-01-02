/*
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
package org.jooq.util.jaxb.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * An {@link XmlAdapter} that implements useful features after parsing XML
 * strings with JAXB.
 * <p>
 * Supported features are:
 * <ul>
 * <li>[#2401] String-trimming, taking out whitespace from JAXB-bound XML
 * content.</li>
 * <li>[#4550] Property expression resolution</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class StringAdapter extends XmlAdapter<String, String> {

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

    @Override
    public final String unmarshal(String v) throws Exception {
        if (v == null)
            return null;

        String result = v.trim();

        Matcher matcher = PROPERTY_PATTERN.matcher(result);
        while (matcher.find())
            result = result.replace(matcher.group(0), System.getProperty(matcher.group(1), matcher.group(0)));

        return result;
    }

    @Override
    public final String marshal(String v) throws Exception {
        if (v == null)
            return null;
        return v.trim();
    }
}
