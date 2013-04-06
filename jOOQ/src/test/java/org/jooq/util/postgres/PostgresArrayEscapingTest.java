package org.jooq.util.postgres;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Henrik Johansson
 */
public class PostgresArrayEscapingTest {

    @Test(expected = NullPointerException.class)
    public void nullObjectShouldNotWork() {
        PostgresUtils.toPGArrayString(null);
    }

    @Test
    public void simpleStrings() {
        assertEquals("{\"foo\"}", PostgresUtils.toPGArrayString(new String[] { "foo" }));
        assertEquals("{\"foo\", \"bar\"}", PostgresUtils.toPGArrayString(new String[] { "foo", "bar" }));
    }

    @Test
    public void stringsWithSingleQuotesAreFine() {
        assertEquals("{\"'foo\"}", PostgresUtils.toPGArrayString(new String[] { "'foo" }));
    }

    @Test
    public void numbers() {
        assertEquals("{\"1\", \"2\"}", PostgresUtils.toPGArrayString(new Object[] { 1, 2 }));
    }

    @Test
    public void nulls() {
        assertEquals("{null}", PostgresUtils.toPGArrayString(new Object[] { null }));
    }

    @Test
    public void stringsWithQuotesShouldBeEscaped() {
        assertEquals("{\"\\\"foo\"}", PostgresUtils.toPGArrayString(new String[] { "\"foo" }));
    }

    @Test
    public void stringsWithBackslashesShouldBeEncoded() {
        assertEquals("{\"\\\\foo\"}", PostgresUtils.toPGArrayString(new String[] { "\\foo" }));
    }
}
