package org.jooq.util;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author <A href="mailto:alexey at abashev dot ru">Alexey Abashev</A>
 */
public class JavaGeneratorTest {
    private final Logger log = LoggerFactory.getLogger(JavaGeneratorTest.class);

    @Test
    public void checkGeneratedDAO() throws Exception {
        GenerationTool.main(new String[] {"/jooq-config-java.xml"});
        GenerationTool.main(new String[] {"/jooq-config-scala.xml"});

        assertFilesEqual(
                "target/generated-sources/jooq/test_java/tables/daos/PersonDao.java",
                "src/test/resources/output/PersonDao.java"
        );
        assertFilesEqual(
                "target/generated-sources/jooq/test_scala/tables/daos/PersonDao.scala",
                "src/test/resources/output/PersonDao.scala"
        );
    }

    protected void assertFilesEqual(String actualFile, String expectedFile) {
        BufferedReader actual = null, expected = null;

        try {
            actual = new BufferedReader(new FileReader(actualFile));
            expected = new BufferedReader(new FileReader(expectedFile));

            String l1, l2;

            while ((l1 = actual.readLine()) != null) {
                l2 = expected.readLine();

                assertEquals(l1, l2);
            }

            assertNull(expected.readLine());
        } catch (IOException e) {
            log.error("No able to read file", e);

            fail();
        } finally {
            if (actual != null) {
                try {
                    actual.close();
                } catch (IOException e1) {
                }
            }

            if (expected != null) {
                try {
                    expected.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
