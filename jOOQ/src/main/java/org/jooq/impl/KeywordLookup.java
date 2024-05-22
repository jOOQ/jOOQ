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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.impl;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A lookup utility for keywords stored in a Trie.
 *
 * @author Lukas Eder
 */
class KeywordLookup {

    private final KeywordTrie trie;

    KeywordLookup() {
        this(new KeywordTrie());
    }

    private KeywordLookup(KeywordTrie trie) {
        this.trie = trie;
    }

    static final KeywordLookup from(String... keywords) {
        KeywordLookup result = new KeywordLookup();

        for (String keyword : keywords)
            result.insert(keyword);

        return result;
    }

    final int skipWhitespace(String text, int i) {
        int l = text.length();

        while (Character.isWhitespace(text.charAt(i)) && i + 1 < l)
            i++;

        return i;
    }

    final boolean lookup(String text) {
        return lookup(text.toCharArray(), 0, i -> skipWhitespace(text, i)) == text.length();
    }

    final int lookup(char[] text, int position, IntToIntFunction afterWhitespace) {
        KeywordTrie t = trie;

        for (int i = position; i < text.length && t != null; i++) {
            char c = upper(character(text, i));

            if ((t = t.next[encode(c)]) != null) {
                if (t.terminal && !isIdentifierPart(character(text, i + 1)) && character(text, i + 1) != '.')
                    return i + 1;

                if (Character.isWhitespace(c))
                    i = afterWhitespace.applyAsInt(i) - 1;
            }
        }

        return position;
    }

    private final boolean isIdentifierPart(char character) {
        return Character.isJavaIdentifierPart(character)
           || ((character == '@'
           ||   character == '#')
           &&   character != ';');
    }

    static final char character(char[] text, int pos) {
        return pos >= 0 && pos < text.length ? text[pos] : ' ';
    }

    /**
     * Insert a new keyword into the trie.
     *
     * @param keyword The keyword
     * @return Whether the trie changed as a result of this operation.
     */
    final boolean insert(String keyword) {
        boolean result = false;
        KeywordTrie t = trie;

        for (int i = 0; i < keyword.length(); i++) {
            int pos = encode(keyword.charAt(i));

            if (result |= t.next[pos] == null)
                t = t.next[pos] = new KeywordTrie();
            else
                t = t.next[pos];
        }

        if (t.terminal)
            return result;
        else
            return t.terminal = true;
    }

    /**
     * Get a {@link Set} representation of this lookup's trie.
     */
    final Set<String> set() {
        return set(new LinkedHashSet<>(), new StringBuilder(), trie);
    }

    private static final int encode(char c) {
        if (c == ' ')
            return 0;

        char C = Character.toUpperCase(c);

        if (C >= 'A' && C <= 'Z')
            return C - '@';
        else
            return 0;
    }

    private static final char decode(int i) {
        return i == 0 ? ' ' : (char) (i + '@');
    }

    private static final char upper(char c) {
        return c >= 'a' && c <= 'z' ? (char) (c - ('a' - 'A')) : c;
    }

    private static final Set<String> set(Set<String> s, StringBuilder sb, KeywordTrie t) {
        if (t.terminal)
            s.add(sb.toString());

        for (int i = 0; i < t.next.length; i++) {
            if (t.next[i] != null) {
                set(s, sb.append(decode(i)), t.next[i]);
                sb.deleteCharAt(sb.length() - 1);
            }
        }

        return s;
    }

    private static class KeywordTrie {
        final KeywordTrie[] next = new KeywordTrie[27];
        boolean             terminal;

        @Override
        public String toString() {
            return "Terminal: " + terminal + ", Trie: " + new KeywordLookup(this).toString();
        }
    }

    @Override
    public String toString() {
        return set().toString();
    }
}