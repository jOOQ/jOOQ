/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
