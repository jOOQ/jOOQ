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
 */
package org.jooq.conf;

import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Lukas Eder
 */
public class RegexAdapter extends XmlAdapter<String, Pattern> {

    @Override
    public Pattern unmarshal(String v) throws Exception {
        return v == null ? null : Pattern.compile(v);
    }

    @Override
    public String marshal(Pattern v) throws Exception {
        return v == null ? null : v.pattern();
    }
}
