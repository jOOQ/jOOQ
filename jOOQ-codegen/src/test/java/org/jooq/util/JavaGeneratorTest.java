package org.jooq.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.junit.Assert.*;

/**
 * @author <A href="mailto:alexey at abashev dot ru">Alexey Abashev</A>
 */
public class JavaGeneratorTest {
    private final Logger log = LoggerFactory.getLogger(JavaGeneratorTest.class);

    private final static Pattern[] GENERIC_LINES = new Pattern[] {
            compile("\t\t\"jOOQ version:\\d\\.\\d\\.\\d\"")
    };

    /**
     * Compare generated files with original.
     *
     * @throws Exception
     */
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

    /**
     * Check two files have been equal, special lines (with timestamp or versions) will compared by regexp.
     *
     * @param actualFile path to actual file
     * @param expectedFile path to file with expected content
     */
    protected void assertFilesEqual(String actualFile, String expectedFile) {
        BufferedReader actual = null, expected = null;

        try {
            actual = new BufferedReader(new FileReader(actualFile));
            expected = new BufferedReader(new FileReader(expectedFile));

            String l1, l2;

            while ((l1 = actual.readLine()) != null) {
                l2 = expected.readLine();

                boolean isGeneric = false;

                for (Pattern p : GENERIC_LINES) {
                    if (p.matcher(l1).matches()) {
                        log.debug("Found generic line: [{}]", l1);

                        isGeneric = true;

                        assertTrue(p.matcher(l2).matches());

                        break;
                    }
                }

                if (!isGeneric) {
                    assertEquals(l1, l2);
                }
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
