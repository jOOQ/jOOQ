/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.test.all.testcases;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.val;
import static org.jooq.tools.StringUtils.defaultString;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Row;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.Seq;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test.all.tools.DOMBuilder;
import org.jooq.tools.StringUtils;

import org.w3c.dom.Document;

/**
 * @author Lukas Eder
 * @author Ivan Dugic
 * @author Johannes Buehler
 */
public class FormatTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public FormatTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testFetchFromTXT() throws Exception {
        Result<L> result = create().fetch(VLibrary());

        assertEquals(result, create().fetchFromTXT(result.format()));
    }

    public void testFormat() throws Exception {
        jOOQAbstractTest.reset = false;

        // Insert test numbers
        create().insertInto(T639(), T639_ID(), T639_BIG_DECIMAL())
                .values(1, null)
                .values(2, new BigDecimal("0"))
                .values(3, new BigDecimal("1"))
                .values(4, new BigDecimal("-1.20"))
                .values(5, new BigDecimal("1.23"))
                .values(6, new BigDecimal("1.23456789"))
                .values(7, new BigDecimal("12.3"))
                .values(8, new BigDecimal("123.4"))
                .values(9, new BigDecimal("1234.5"))
                .values(10, new BigDecimal("12345678.9"))
                .values(11, new BigDecimal("-0.1"))
                .values(12, new BigDecimal("0.12"))
                .values(13, new BigDecimal("0.123456789"))
                .execute();

        Result<Record2<Integer, BigDecimal>> result = create()
            .select(T639_ID(), T639_BIG_DECIMAL())
            .from(T639())
            .orderBy(T639_ID())
            .fetch();

        for (int i = 2; i <= result.size(); i++) {
            testFormatDecimalAlignment(result.format(i));
        }
    }

    private void testFormatDecimalAlignment(String format) {
        Pattern pattern = Pattern.compile("(\\s*-?\\d+)(\\.?(?:\\d*\\s*))");

        // Collect lenghts of strings to both sides of the decimal point
        Set<Integer> lhs = new HashSet<Integer>();
        Set<Integer> rhs = new HashSet<Integer>();

        for (String formatLine : format.split("\n")) {

            // Include only data lines (lines starting with an ID value | 13 |)
            if (formatLine.matches("^\\|\\s*\\d+\\|.*") && !formatLine.contains("{null}")) {

                // Find the value in the second column
                String bigDecimalValue = formatLine.replaceAll("^\\|\\s*\\d+\\|([^\\|]+)\\|", "$1");

                // $1: The left side of the decimal point
                // $2: The right side of the decimal point, or \\s+
                Matcher matcher = pattern.matcher(bigDecimalValue);

                assertTrue(matcher.find());
                lhs.add(matcher.group(1).length());
                rhs.add(matcher.group(2).length());
            }
        }

        // Check if all decimal points have the same position
        assertEquals(1, lhs.size());
        assertEquals(1, rhs.size());
    }

    public void testFormatHTML() throws Exception {
        Row row = TBook().fieldsRow();
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
        assertEquals("" + row.size(),
                          xp.evaluate("count(/table/thead/tr/th)", doc));

        for (int i = 0; i < row.size(); i++) {
            assertEquals(row.field(i).getName(),
                          xp.evaluate("/table/thead/tr/th[" + (i + 1) + "]/text()", doc));
        }

        assertEquals("1", xp.evaluate("count(/table/tbody)", doc));
        assertEquals("4", xp.evaluate("count(/table/tbody/tr)", doc));
        assertEquals("" + 4 * row.size(),
                          xp.evaluate("count(/table/tbody/tr/td)", doc));

        for (int j = 0; j < books.size(); j++) {
            for (int i = 0; i < row.size(); i++) {
                assertEquals(defaultString(books.get(j).getValue(i, String.class), "{null}"),
                          xp.evaluate("/table/tbody/tr[" + (j + 1) + "]/td[" + (i + 1) + "]/text()", doc));
            }
        }
    }

    public void testFetchFromCSV() throws Exception {
        testFetchFromCSV0(
            "A,B,\"C\",\"\"\"D\"\n" +
            "1,,a,b\n" +
            "1,2,a\n" +
            "1,2,a,b,c",
            true,
            "A", "B", "C", "\"D"
        );

        testFetchFromCSV0(
            "1,,a,b\n" +
            "1,2,a\n" +
            "1,2,a,b,c",
            false,
            "COL1", "COL2", "COL3", "COL4"
        );
    }

    private void testFetchFromCSV0(String input, boolean header, String... colNames) throws Exception {
        Result<Record> result1 = create().fetchFromCSV(input, header);

        // Check meta data
        assertEquals(4, result1.fieldsRow().size());
        assertEquals(3, result1.size());
        assertEquals(colNames[0], result1.field(0).getName());
        assertEquals(colNames[1], result1.field(1).getName());
        assertEquals(colNames[2], result1.field(2).getName());
        assertEquals(colNames[3], result1.field(3).getName());

        // Check column correctness
        assertEquals(asList("1", "1", "1"), result1.getValues(0));
        assertEquals(asList(1, 1, 1), result1.getValues(0, Integer.class));
        assertEquals(asList("", "2", "2"), result1.getValues(1));
        assertEquals(asList(null, 2, 2), result1.getValues(1, Integer.class));
        assertEquals(asList("a", "a", "a"), result1.getValues(2));
        assertEquals(asList("b", null, "b"), result1.getValues(3));

        // Check row correctness
        assertEquals(asList("1", "", "a", "b"), asList(result1.get(0).intoArray()));
        assertEquals(asList("1", "2", "a", null), asList(result1.get(1).intoArray()));
        assertEquals(asList("1", "2", "a", "b"), asList(result1.get(2).intoArray()));

        // [#1688] Check correct behaviour when passing array instances, not classes
        String[] array1a = new String[2];
        String[] array2a = new String[3];
        String[] array3a = new String[4];

        String[] array1b = result1.get(0).into(array1a);
        String[] array2b = result1.get(1).into(array2a);
        String[] array3b = result1.get(2).into(array3a);

        assertEquals(asList("1", "", "a", "b"), asList(array1b));
        assertEquals(asList("1", "2", "a", null), asList(array2b));
        assertEquals(asList("1", "2", "a", "b"), asList(array3b));

        assertTrue(array1a != array1b);
        assertTrue(array2a != array2b);
        assertTrue(array3a == array3b);

        // Factory.fetchFromCSV() should be the inverse of Result.formatCSV()
        // ... apart from the loss of type information
        String csv = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch().formatCSV();
        Result<Record> result2 = create().fetchFromCSV(csv);

        assertEquals(4, result2.size());
        assertEquals(BOOK_IDS, result2.getValues(TBook_ID(), Integer.class));
        assertEquals(BOOK_AUTHOR_IDS, result2.getValues(TBook_AUTHOR_ID(), Integer.class));
        assertEquals(BOOK_TITLES, result2.getValues(TBook_TITLE()));
    }

    public void testFormatCSV() throws Exception {
        jOOQAbstractTest.reset = false;

        B b1 = newBook(5);
        B b2 = newBook(6);
        B b3 = newBook(7);

        b1.setValue(TBook_TITLE(), "\"\"\"\"");
        b2.setValue(TBook_TITLE(), "Hello\\World");
        b3.setValue(TBook_TITLE(), "Hello\\,\\World");
        create().batchStore(b1, b2).execute();

        Row row = TBook().fieldsRow();
        Result<B> books = create().selectFrom(TBook()).fetch();
        String csv = books.formatCSV();
        String csvNoHeader = books.formatCSV(false);

        assertTrue(csv.endsWith(csvNoHeader));
        assertEquals(Seq.of(csv.split("\n")).skip(1).join("\n").trim(), csvNoHeader.trim());

        String[] lines = csv.split("\n");
        String[] fieldNames = lines[0].split(",");

        assertEquals(books.size() + 1, lines.length);
        assertEquals(row.size(), fieldNames.length);

        for (int i = 0; i < row.size(); i++) {
            assertEquals(row.field(i).getName(), fieldNames[i]);
        }

        // Check every CSV line
        for (int j = 1; j < lines.length; j++) {

            // Check every value in the record
            for (int i = 0; i < row.size(); i++) {
                String value = books.get(j - 1).getValue(i, String.class);

                if (value == null || "".equals(value)) {
                    value = "\"\"";
                }

                String regex1 = "";
                String regex2 = "";

                // Generate a regular expression matching dummy values for
                // fields != i and an actual value expression for field == i
                for (int x = 0; x < row.size(); x++) {
                    if (x > 0) {
                        regex1 += ",";
                        regex2 += ",";
                    }

                    // This is the field whose contents we're checking.
                    // regex1 is the actual field value, without quoting/escaping
                    // regex2 is the quoted/escaped field value
                    if (x == i) {
                        regex1 += value.replace("\\", "\\\\");
                        regex2 += "\"" + value.replace("\\", "\\\\\\\\").replace("\"", "\"\"") + "\"";
                    }

                    // These are all the other fields
                    else {
                        regex1 += "((?!\")[^,]+|\".*?\"(?=(,|$)))";
                        regex2 += "((?!\")[^,]+|\".*?\"(?=(,|$)))";
                    }
                }

                assertTrue(lines[j], lines[j].matches(regex1) || lines[j].matches(regex2));
            }
        }
    }

    public void testFormatJSON() throws Exception {
        Row row = TBook().fieldsRow();
        Result<B> books = create().selectFrom(TBook()).fetch();
        String json = books.formatJSON();

        // Fields header
        String token1 = "{\"fields\":[";
        assertTrue(json.startsWith(token1));
        json = json.replace(token1, "");

        // Field names
        String token2 = "";
        String separator = "";
        for (Field<?> field : row.fields()) {
            token2 += separator + "{\"name\":\"" + field.getName() + "\"" + ",\"type\":\""
                + field.getDataType().getTypeName().toUpperCase() + "\"}";
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
            for (Field<?> field : row.fields()) {
                Object value = record.getValue(field);

                if (value == null) {
                    token4 += separator + null;
                }
                else if (value instanceof Number) {
                    token4 += separator + value;
                }
                else if (value instanceof byte[]) {
                    token4 += separator + DatatypeConverter.printBase64Binary((byte[]) value);
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

    public void testFetchFromJSON() {

        // Factory.fetchFromJSON() should be the inverse of Result.formatJSON()
        // ... apart from the loss of type information
        String json = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch().formatJSON();
        Result<Record> result2 = create().fetchFromJSON(json);
        int expectedRecords = create().selectFrom(TBook()).fetchCount();
        assertEquals(expectedRecords, result2.size());
        assertEquals(BOOK_IDS, result2.getValues(TBook_ID(), Integer.class));
        assertEquals(BOOK_AUTHOR_IDS, result2.getValues(TBook_AUTHOR_ID(), Integer.class));
        assertEquals(BOOK_TITLES, result2.getValues(TBook_TITLE()));
    }

    public void testFormatXML() throws Exception {
        Result<B> books = create().selectFrom(TBook()).fetch();
        String xml = books.formatXML();
        InputStream is = new ByteArrayInputStream(xml.getBytes());

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);

        testXML(doc, books);
    }

    public void testFormatInsert() throws Exception {
        jOOQAbstractTest.reset = false;

        String inserts =
        create().select(TBookStore_NAME(), val(4), val(10))
                .from(TBookStore())
                .fetch()
                .formatInsert(TBookToBookStore(), TBookToBookStore_BOOK_STORE_NAME(), TBookToBookStore_BOOK_ID(), TBookToBookStore_STOCK());

        for (String insert : inserts.split(";"))
            if (!StringUtils.isBlank(insert))
                assertEquals(1, create().execute(insert));

        assertEquals(9, create().fetchCount(TBookToBookStore()));
        assertEquals(3, create().fetchCount(selectFrom(TBookToBookStore()).where(TBookToBookStore_BOOK_ID().eq(4))));
    }

    public void testIntoXML() throws Exception {
        Result<B> books = create().selectFrom(TBook()).fetch();
        testXML(books.intoXML(), books);
    }

    public void testIntoXMLContentHandler() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Result<B> books = create().selectFrom(TBook()).fetch();
        testXML((Document) books.intoXML(new DOMBuilder(document)).getRootDocument(), books);
    }

    private void testXML(Document doc, Result<B> books) throws XPathExpressionException {
        XPathFactory xpfactory = XPathFactory.newInstance();
        XPath xp = xpfactory.newXPath();

        Row row = TBook().fieldsRow();
        assertEquals("1", xp.evaluate("count(/result)", doc));
        assertEquals("1", xp.evaluate("count(/result/fields)", doc));
        assertEquals("" + row.size(),
                          xp.evaluate("count(/result/fields/field)", doc));

        for (int i = 0; i < row.size(); i++) {
            assertEquals(row.field(i).getName(),
                          xp.evaluate("/result/fields/field[" + (i + 1) + "]/@name", doc));
            assertEquals(row.field(i).getDataType().getTypeName().toUpperCase(),
                xp.evaluate("/result/fields/field[" + (i + 1) + "]/@type", doc));
        }

        assertEquals("1", xp.evaluate("count(/result/records)", doc));
        assertEquals("4", xp.evaluate("count(/result/records/record)", doc));
        assertEquals("" + 4 * row.size(),
                          xp.evaluate("count(/result/records/record/value)", doc));

        for (int j = 0; j < books.size(); j++) {
            for (int i = 0; i < row.size(); i++) {
                assertEquals(row.field(i).getName(),
                          xp.evaluate("/result/records/record[" + (j + 1) + "]/value[" + (i + 1) + "]/@field", doc));
                assertEquals(defaultString(books.get(j).getValue(i, String.class)),
                          xp.evaluate("/result/records/record[" + (j + 1) + "]/value[" + (i + 1) + "]/text()", doc));
            }
        }
    }
}
