package org.jooq.tools.jdbc;

import org.jooq.exception.ErroneousRowSpecificationException;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by deghou on 19/11/2016.
 */
public class MockFileDatabaseTest{


    @Test
    public void testCorrectRowSpecification() throws IOException {
        //File contains one statement
        MockFileDatabase mockFileDatabase1 = new MockFileDatabase(this.getClass().getResourceAsStream("/mock-file-database-file-1.txt"));
        //File contains multiple statements
        MockFileDatabase mockFileDatabase2 = new MockFileDatabase(this.getClass().getResourceAsStream("/mock-file-database-file-4.txt"));
    }

    @Test(expected = ErroneousRowSpecificationException.class)
    public void testErroneousRowSpecification() throws IOException {
        MockFileDatabase mockFileDatabase = new MockFileDatabase(this.getClass().getResourceAsStream("/mock-file-database-file-2.txt"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCorrectRowSpecificationValue() throws IOException {
        MockFileDatabase mockFileDatabase = new MockFileDatabase(this.getClass().getResourceAsStream("/mock-file-database-file-3.txt"));
    }
}
