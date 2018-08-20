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

import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Lukas Eder
 */
public class LocaleAdapter extends XmlAdapter<String, Locale> {

    private static final Pattern SIMPLE_LANGUAGE_TAG = Pattern.compile("(\\w+)(?:-(\\w+)(?:-(\\w+))?)?");

    @Override
    public Locale unmarshal(String v) throws Exception {
        Locale result = null;

        if (v == null)
            return result;


        result = Locale.forLanguageTag(v);
















        return result;
    }

    @Override
    public String marshal(Locale v) throws Exception {
        String result = null;

        if (v == null)
            return result;


        result = v.toLanguageTag();





















        return result;
    }
}
