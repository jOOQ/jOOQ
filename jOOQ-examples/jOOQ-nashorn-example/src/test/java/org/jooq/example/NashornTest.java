/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.example;

import static javax.script.ScriptContext.ENGINE_SCOPE;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.stream.Stream;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class NashornTest {

    private static Connection connection;

    @BeforeClass
    public static void before() throws Exception {
        Properties properties = new Properties();
        properties.load(NashornTest.class.getResourceAsStream("/config.properties"));

        Class.forName(properties.getProperty("db.driver"));
        connection = DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.username"),
            properties.getProperty("db.password")
        );
    }

    @Test
    public void testScripts() throws Exception {
        Stream.of(
            new File(getClass().getResource("/org/jooq/example/test").toURI()).listFiles((dir, name) -> name.endsWith(".js"))
        ).forEach(file -> {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("nashorn");

            Bindings bindings = engine.getBindings(ENGINE_SCOPE);
            bindings.put("connection", connection);

            try {
                engine.eval(new FileReader(file));
            }
            catch (Exception e) {
                throw new RuntimeException("Error while running " + file, e);
            }
        });

    }
}
