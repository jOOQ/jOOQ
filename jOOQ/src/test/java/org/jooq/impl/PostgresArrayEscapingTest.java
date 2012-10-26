package org.jooq.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PostgresArrayEscapingTest {
    @Test(expected=NullPointerException.class)
    public void nullObjectShouldNotWork() {
        DefaultBindContext.postgresArrayString(null);
    }

    @Test
    public void simpleStrings() {
        assertEquals("{\"foo\"}", DefaultBindContext.postgresArrayString(new String[]{"foo"}));
        assertEquals("{\"foo\", \"bar\"}", DefaultBindContext.postgresArrayString(new String[]{"foo", "bar"}));
    }

    @Test
    public void stringsWithSingleQuotesAreFine() {
        assertEquals("{\"'foo\"}", DefaultBindContext.postgresArrayString(new String[]{"'foo"}));
    }

    @Test
    public void numbers() {
        assertEquals("{\"1\", \"2\"}", DefaultBindContext.postgresArrayString(new Object[]{1, 2}));
    }

    @Test
    public void nulls() {
        assertEquals("{null}", DefaultBindContext.postgresArrayString(new Object[]{null}));
    }

    @Test
    public void stringsWithQuotesShouldBeEscaped() {
        assertEquals("{\"\\\"foo\"}", DefaultBindContext.postgresArrayString(new String[]{"\"foo"}));
    }

    @Test
    public void stringsWithBackslashesShouldBeEncoded() {
        assertEquals("{\"\\\\foo\"}", DefaultBindContext.postgresArrayString(new String[]{"\\foo"}));
    }
}
