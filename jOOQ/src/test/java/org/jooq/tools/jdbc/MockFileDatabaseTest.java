package org.jooq.tools.jdbc;

import org.jooq.exception.ErroneousRowSpecificationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by deghou on 19/11/2016.
 */
public class MockFileDatabaseTest{

    @Before
    public void setUp(){

    }

    @After
    public void tearDown(){

    }


    @Test
    public void testCorrectRowSpecification() throws IOException {
        MockFileDatabase mockFileDatabase = new MockFileDatabase(this.getClass().getResourceAsStream("/mock-file-database-file-1.txt"));
    }

    @Test(expected= ErroneousRowSpecificationException.class)
    public void testErroneousRowSpecification() throws IOException {
        MockFileDatabase mockFileDatabase = new MockFileDatabase(this.getClass().getResourceAsStream("/mock-file-database-file-2.txt"));
    }

}
