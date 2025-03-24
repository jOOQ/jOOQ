/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.util.jaxb.tools;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * Wrapper around a {@link StringBuilder} which can be used to serialize
 * a JAXB-annotated Java object graph to XML. The JAXB objects must however
 * also implement the {@link XMLAppendable} interface for this to work.
 * <p>
 * Use {@link #formatting()} to create an instance producing formatted XML
 * output and {@link #nonFormatting()} to produce XML without any formatting
 * whitespace (i.e. everything on one line).
 *
 * @author Knut Wannheden
 */
@Internal
public final class XMLBuilder {

    private final StringBuilder builder = new StringBuilder();

    private final boolean format;
    private int indentLevel;
    private boolean onNewLine;

    private XMLBuilder(boolean format) {
        this.format = format;
    }

    public static XMLBuilder formatting() {
        return new XMLBuilder(true);
    }

    public static XMLBuilder nonFormatting() {
        return new XMLBuilder(false);
    }

    public XMLBuilder append(XMLAppendable appendable) {
        if (appendable != null)
            appendable.appendTo(this);
        return this;
    }

    public XMLBuilder append(String elementName, XMLAppendable appendable) {
        if (appendable != null) {
            openTag(elementName).newLine().indent();
            appendable.appendTo(this);
            unindent().closeTag(elementName).newLine();
        }
        return this;
    }

    public XMLBuilder append(String wrappingElementName, String elementName, List<?> list) {
        if (list != null && !list.isEmpty()) {
            openTag(wrappingElementName).newLine().indent();
            for (Object o : list) {
                if (o instanceof XMLAppendable x)
                    append(elementName, x);
                else
                    append(elementName, o);
            }
            unindent().closeTag(wrappingElementName).newLine();
        }
        return this;
    }

    private XMLBuilder openTag(String elementName) {
        if (format && onNewLine)
            for (int i = 0; i < indentLevel; i++)
                builder.append("    ");
        builder.append('<').append(elementName).append('>');
        onNewLine = false;
        return this;
    }

    private XMLBuilder closeTag(String elementName) {
        if (format && onNewLine)
            for (int i = 0; i < indentLevel; i++)
                builder.append("    ");
        builder.append("</").append(elementName).append('>');
        onNewLine = false;
        return this;
    }

    private XMLBuilder indent() {
        indentLevel++;
        return this;
    }

    private XMLBuilder unindent() {
        indentLevel--;
        return this;
    }

    private XMLBuilder newLine() {
        if (format)
            builder.append('\n');
        onNewLine = true;
        return this;
    }

    public XMLBuilder append(String elementName, int i) {
        openTag(elementName);
        builder.append(i);
        closeTag(elementName).newLine();
        return this;
    }

    public XMLBuilder append(String elementName, boolean b) {
        openTag(elementName);
        builder.append(b);
        closeTag(elementName).newLine();
        return this;
    }

    public XMLBuilder append(String elementName, String s) {
        if (s != null) {
            openTag(elementName);
            builder.append(escape(s));
            closeTag(elementName).newLine();
        }
        return this;
    }

    public XMLBuilder append(String elementName, Pattern p) {
        if (p != null)
            append(elementName, p.pattern());

        return this;
    }

    public XMLBuilder append(String elementName, Object o) {
        if (o != null)
            append(elementName, "" + o);

        return this;
    }

    private static final Pattern P_XML_SPECIAL_CHARACTERS = Pattern.compile("[<>&]");

    private static final String escape(String string) {
        return P_XML_SPECIAL_CHARACTERS.matcher(string).find()
             ? string.replace("&", "&amp;")
                     .replace("<", "&lt;")
                     .replace(">", "&gt;")
             : string;
    }

    public void appendTo(Appendable a) throws IOException {
        a.append(builder);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
