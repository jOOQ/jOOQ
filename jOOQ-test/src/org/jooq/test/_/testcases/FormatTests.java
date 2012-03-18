/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test._.testcases;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;
import org.w3c.dom.Document;

public class FormatTests<
    A    extends UpdatableRecord<A>,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public FormatTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testFormatHTML() throws Exception {
        List<Field<?>> fields = TBook().getFields();
        Result<B> books = create().selectFrom(TBook()).fetch();
        String html = books.formatHTML();
        InputStream is = new ByteArrayInputStream(html.getBytes());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);

        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xp = xpfactory.newXPath();

        assertEquals("1", xp.evaluate("count(/table)", doc));
        assertEquals("1", xp.evaluate("count(/table/thead)", doc));
        assertEquals("1", xp.evaluate("count(/table/thead/tr)", doc));
        assertEquals("0", xp.evaluate("count(/table/thead/tr/td)", doc));
        assertEquals("" + fields.size(),
                          xp.evaluate("count(/table/thead/tr/th)", doc));

        for (int i = 0; i < fields.size(); i++) {
            assertEquals(fields.get(i).getName(),
                          xp.evaluate("/table/thead/tr/th[" + (i + 1) + "]/text()", doc));
        }

        assertEquals("1", xp.evaluate("count(/table/tbody)", doc));
        assertEquals("4", xp.evaluate("count(/table/tbody/tr)", doc));
        assertEquals("" + 4 * fields.size(),
                          xp.evaluate("count(/table/tbody/tr/td)", doc));

        for (int j = 0; j < books.size(); j++) {
            for (int i = 0; i < fields.size(); i++) {
                assertEquals(books.getValueAsString(j, i, "{null}"),
                          xp.evaluate("/table/tbody/tr[" + (j + 1) + "]/td[" + (i + 1) + "]/text()", doc));
            }
        }
    }

    @Test
    public void testFormatCSV() throws Exception {
        List<Field<?>> fields = TBook().getFields();
        Result<B> books = create().selectFrom(TBook()).fetch();
        String csv = books.formatCSV();

        String[] lines = csv.split("\n");
        String[] fieldNames = lines[0].split(",");

        assertEquals(5, lines.length);
        assertEquals(fields.size(), fieldNames.length);

        for (int i = 0; i < fields.size(); i++) {
            assertEquals(fields.get(i).getName(), fieldNames[i]);
        }

        for (int j = 1; j < lines.length; j++) {
            for (int i = 0; i < fields.size(); i++) {
                String value = books.getValueAsString(j - 1, i);

                if (value == null || "".equals(value)) {
                    value = "\"\"";
                }

                String regex1 = "";
                String regex2 = "";

                for (int x = 0; x < fields.size(); x++) {
                    if (x > 0) {
                        regex1 += ",";
                        regex2 += ",";
                    }

                    if (x == i) {
                        regex1 += value;
                        regex2 += "\"" + value.replaceAll("\"", "\"\"") + "\"";
                    }
                    else {
                        regex1 += "((?!\")[^,]+|\"[^\"]*\")";
                        regex2 += "((?!\")[^,]+|\"[^\"]*\")";
                    }
                }

                assertTrue(lines[j].matches(regex1) || lines[j].matches(regex2));
            }
        }
    }

    @Test
    public void testFormatJSON() throws Exception {
        List<Field<?>> fields = TBook().getFields();
        Result<B> books = create().selectFrom(TBook()).fetch();
        String json = books.formatJSON();

        // Fields header
        String token1 = "{\"fields\":[";
        assertTrue(json.startsWith(token1));
        json = json.replace(token1, "");

        // Field names
        String token2 = "";
        String separator = "";
        for (Field<?> field : fields) {
            token2 += separator + "\"" + field.getName() + "\"";
            separator = ",";
        }
        assertTrue(json.startsWith(token2));
        json = json.replace(token2, "");

        // Records header
        String token3 = "],\"records\":[";
        assertTrue(json.startsWith(token3));
        json = json.replace(token3, "");

        // Record values
        int i = 0;
        for (Record record : books) {
            i++;
            String token4 = "[";

            if (i > 1) {
                token4 = ",[";
            }

            separator = "";
            for (Field<?> field : fields) {
                Object value = record.getValue(field);

                if (value == null) {
                    token4 += separator + null;
                }
                else if (value instanceof Number) {
                    token4 += separator + value;
                }
                else {
                    token4 += separator + "\"" + value.toString().replaceAll("\"", "\"\"") + "\"";
                }

                separator = ",";
            }
            token4 += "]";
            assertTrue(json.startsWith(token4));
            json = json.replace(token4, "");
        }

        assertEquals("]}", json);
    }

    @Test
    public void testFormatXML() throws Exception {
        Result<B> books = create().selectFrom(TBook()).fetch();
        String xml = books.formatXML();
        InputStream is = new ByteArrayInputStream(xml.getBytes());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);

        testXML(doc, books);
    }

    @Test
    public void testIntoXML() throws Exception {
        Result<B> books = create().selectFrom(TBook()).fetch();
        testXML(books.intoXML(), books);
    }

    private void testXML(Document doc, Result<B> books) throws XPathExpressionException {
        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xp = xpfactory.newXPath();

        List<Field<?>> fields = TBook().getFields();
        assertEquals("1", xp.evaluate("count(/result)", doc));
        assertEquals("1", xp.evaluate("count(/result/fields)", doc));
        assertEquals("" + fields.size(),
                          xp.evaluate("count(/result/fields/field)", doc));

        for (int i = 0; i < fields.size(); i++) {
            assertEquals(fields.get(i).getName(),
                          xp.evaluate("/result/fields/field[" + (i + 1) + "]/@name", doc));
        }

        assertEquals("1", xp.evaluate("count(/result/records)", doc));
        assertEquals("4", xp.evaluate("count(/result/records/record)", doc));
        assertEquals("" + 4 * fields.size(),
                          xp.evaluate("count(/result/records/record/value)", doc));

        for (int j = 0; j < books.size(); j++) {
            for (int i = 0; i < fields.size(); i++) {
                assertEquals(fields.get(i).getName(),
                          xp.evaluate("/result/records/record[" + (j + 1) + "]/value[" + (i + 1) + "]/@field", doc));
                assertEquals(books.getValueAsString(j, i, ""),
                          xp.evaluate("/result/records/record[" + (j + 1) + "]/value[" + (i + 1) + "]/text()", doc));
            }
        }
    }
}
