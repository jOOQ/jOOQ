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
package org.jooq.academy.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


/**
 * @author Lukas Eder
 */
public class Tools {

    static Properties properties;
    static Connection connection;

    /**
     * Get a fresh connection from H2.
     */
    public static Connection connection() {
        if (connection == null) {
            try {
                Class.forName(driver());

                connection = DriverManager.getConnection(
                    url(),
                    username(),
                    password());
                connection.setAutoCommit(false);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return connection;
    }

    public static String password() {
        return properties().getProperty("db.password");
    }

    public static String username() {
        return properties().getProperty("db.username");
    }

    public static String url() {
        return properties().getProperty("db.url");
    }

    public static String driver() {
        return properties().getProperty("db.driver");
    }

    /**
     * Get the connection properties
     */
    public static Properties properties() {
        if (properties == null) {
            try {
                properties = new Properties();
                properties.load(Tools.class.getResourceAsStream("/config.properties"));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return properties;
    }

    /**
     * Some pretty printing
     */
    public static void title(String title) {
        String dashes = "=============================================================================================";

        System.out.println();
        System.out.println(title);
        System.out.println(dashes);
        System.out.println();
    }

    public static void print(Object o) {
        System.out.println(o);
    }

}
