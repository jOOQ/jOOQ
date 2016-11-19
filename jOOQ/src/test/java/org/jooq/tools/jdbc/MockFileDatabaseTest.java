package org.jooq.tools.jdbc;

import org.jooq.exception.ErroneousRowSpecificationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

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


    @Test(expected= ErroneousRowSpecificationException.class)
    public void test() throws IOException {
        InputStream fileName = this.getClass().getResourceAsStream("/mock-file-database-file-2.txt");
        MockFileDatabase mockFileDatabase = new MockFileDatabase(fileName);
    }

}
