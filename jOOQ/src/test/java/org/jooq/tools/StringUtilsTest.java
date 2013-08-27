/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
