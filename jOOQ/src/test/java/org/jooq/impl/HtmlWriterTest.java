package org.jooq.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.test.AbstractTest;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

/**
 * Tests HTML formatting
 *
 * @author Art O Cathain
 */
public class HtmlWriterTest extends AbstractTest {

    private final Mockery context = new Mockery();
    private final StringWriter writer = new StringWriter();

    private HtmlWriter<Record> recordHtmlWriter;

    @Test
    public void testFormatNormal() {
        setupExpectations("field1Name", "field1value1");

        recordHtmlWriter.formatHTML(writer);

        assertCorrectHtml("<table><thead><tr><th>field1Name</th></tr></thead><tbody><tr><td>field1value1</td></tr></tbody></table>");
    }

    @Test
    public void testFormatWithXssAttempt() {
        setupExpectations("field1Name", "<script>alert('xss')</script>");

        recordHtmlWriter.formatHTML(writer);

        assertCorrectHtml("<table><thead><tr><th>field1Name</th></tr></thead><tbody><tr><td>&lt;script&gt;alert('xss')&lt;/script&gt;</td></tr></tbody></table>");
    }

    private void setupExpectations(final String field1Name, final String field1value1) {
        final Field field = context.mock(Field.class);
        final Record record = context.mock(Record.class);
        context.checking(
                new Expectations() {{
                    allowing(field).getName();

                    will(returnValue(field1Name));

                    allowing(record).getValue(0);
                    will(returnValue(field1value1));

                }});

        Fields<Record> fields = new Fields<Record>(field);
        List<Record> records = Collections.singletonList(record);

        recordHtmlWriter = new HtmlWriter<Record>(fields, records);
    }

    private void assertCorrectHtml(String expected) {
        assertEquals("HTML should be rendered correctly", expected, writer.toString());
    }


}
