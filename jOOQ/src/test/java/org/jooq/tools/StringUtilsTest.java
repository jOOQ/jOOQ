/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.tools;

import static org.jooq.tools.StringUtils.toCamelCase;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class StringUtilsTest {

    @Test
    public void testToCamelCase() {
        assertEquals("A", toCamelCase("A"));
        assertEquals("A", toCamelCase("a"));

        assertEquals("Aa", toCamelCase("AA"));
        assertEquals("Aa", toCamelCase("Aa"));
        assertEquals("Aa", toCamelCase("aA"));
        assertEquals("Aa", toCamelCase("aa"));

        assertEquals("AA", toCamelCase("A_A"));
        assertEquals("AA", toCamelCase("A_a"));
        assertEquals("AA", toCamelCase("a_A"));
        assertEquals("AA", toCamelCase("a_a"));

        assertEquals("AaAa", toCamelCase("AA_AA"));
        assertEquals("AaAa", toCamelCase("AA_aa"));
        assertEquals("AaAa", toCamelCase("aa_AA"));
        assertEquals("AaAa", toCamelCase("aa_aa"));

        assertEquals("Aa_Aa", toCamelCase("AA__AA"));
        assertEquals("Aa_Aa", toCamelCase("AA__aa"));
        assertEquals("Aa_Aa", toCamelCase("aa__AA"));
        assertEquals("Aa_Aa", toCamelCase("aa__aa"));

        assertEquals("Aa__Aa", toCamelCase("AA___AA"));
        assertEquals("Aa__Aa", toCamelCase("AA___aa"));
        assertEquals("Aa__Aa", toCamelCase("aa___AA"));
        assertEquals("Aa__Aa", toCamelCase("aa___aa"));

        assertEquals("AaAa_", toCamelCase("AA_AA_"));
        assertEquals("AaAa_", toCamelCase("AA_aa_"));
        assertEquals("AaAa_", toCamelCase("aa_AA_"));
        assertEquals("AaAa_", toCamelCase("aa_aa_"));

        assertEquals("_AaAa_", toCamelCase("_AA_AA_"));
        assertEquals("_AaAa_", toCamelCase("_AA_aa_"));
        assertEquals("_AaAa_", toCamelCase("_aa_AA_"));
        assertEquals("_AaAa_", toCamelCase("_aa_aa_"));

        assertEquals("__AaAa__", toCamelCase("__AA_AA__"));
        assertEquals("__AaAa__", toCamelCase("__AA_aa__"));
        assertEquals("__AaAa__", toCamelCase("__aa_AA__"));
        assertEquals("__AaAa__", toCamelCase("__aa_aa__"));

        assertEquals("__Aa_Aa__", toCamelCase("__AA__AA__"));
        assertEquals("__Aa_Aa__", toCamelCase("__AA__aa__"));
        assertEquals("__Aa_Aa__", toCamelCase("__aa__AA__"));
        assertEquals("__Aa_Aa__", toCamelCase("__aa__aa__"));
    }
}
