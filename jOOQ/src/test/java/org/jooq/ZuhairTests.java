package org.jooq;

import org.jooq.tools.Convert;
import org.junit.Test;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.Month;



public class ZuhairTests {

    @Test
    public void testYearDateConversion() throws ClassNotFoundException {
        LocalDateTime res = (LocalDateTime) Convert.convert("2007-12-03T10:15:30",  Class.forName("java.time.LocalDateTime"));
        LocalDateTime expected = LocalDateTime.of(2007, Month.DECEMBER, 3, 10, 15, 30);
        Assert.assertEquals(expected, res);
    }

    @Test
    public void test3DigitYearDateConversion() throws ClassNotFoundException {
        LocalDateTime res = (LocalDateTime) Convert.convert("0201-12-03T10:15:30", Class.forName("java.time.LocalDateTime"));
        LocalDateTime expected = LocalDateTime.of(201, Month.DECEMBER, 3, 10, 15, 30);
        Assert.assertEquals(expected, res);
    }

    @Test
    public void testInvalidDateConversion() throws ClassNotFoundException {
        LocalDateTime res = (LocalDateTime) Convert.convert("0202-03T10:15:30", Class.forName("java.time.LocalDateTime"));
        Assert.assertNull(res);
    }
}
