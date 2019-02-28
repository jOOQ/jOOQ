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
package org.jooq.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jooq.meta.jaxb.RegexFlag;

/**
 * A cache for {@link Pattern} instances.
 *
 * @author Lukas Eder
 */
public final class Patterns {

    private final Map<String, Pattern> patterns;
    private List<RegexFlag>            regexFlags;

    public Patterns() {
        patterns = new HashMap<String, Pattern>();
    }

    public final Pattern pattern(String regex) {
        if (regex == null)
            return null;

        Pattern pattern = patterns.get(regex);

        if (pattern == null) {
            int flags = 0;

            List<RegexFlag> list = new ArrayList<RegexFlag>(getRegexFlags());

            // [#3860] This should really be handled by JAXB, but apparently, @XmlList and @XmlElement(defaultValue=...)
            // cannot be combined: http://stackoverflow.com/q/27528698/521799
            if (list.isEmpty()) {
                list.add(RegexFlag.COMMENTS);
                list.add(RegexFlag.CASE_INSENSITIVE);
            }

            for (RegexFlag flag : list) {
                switch (flag) {
                    case CANON_EQ:                flags |= Pattern.CANON_EQ;                break;
                    case CASE_INSENSITIVE:        flags |= Pattern.CASE_INSENSITIVE;        break;
                    case COMMENTS:                flags |= Pattern.COMMENTS;                break;
                    case DOTALL:                  flags |= Pattern.DOTALL;                  break;
                    case LITERAL:                 flags |= Pattern.LITERAL;                 break;
                    case MULTILINE:               flags |= Pattern.MULTILINE;               break;
                    case UNICODE_CASE:            flags |= Pattern.UNICODE_CASE;            break;
                    case UNICODE_CHARACTER_CLASS: flags |= 0x100;                           break; // Pattern.UNICODE_CHARACTER_CLASS: Java 1.7 only
                    case UNIX_LINES:              flags |= Pattern.UNIX_LINES;              break;
                }
            }

            pattern = Pattern.compile(regex, flags);
            patterns.put(regex, pattern);
        }

        return pattern;
    }

    public void setRegexFlags(List<RegexFlag> regexFlags) {
        this.regexFlags = regexFlags;
        this.patterns.clear();
    }

    public List<RegexFlag> getRegexFlags() {
        if (regexFlags == null)
            regexFlags = new ArrayList<RegexFlag>();

        return regexFlags;
    }
}
