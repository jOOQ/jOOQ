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
package org.jooq.exception;

import org.jooq.Loader;

/**
 * The <code>LoaderConfigurationException</code> indicates that a {@link Loader}
 * was executed with an illegal execution configuration.
 *
 * @author Lukas Eder
 */
public class LoaderConfigurationException extends DataAccessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1070990582126075536L;

    /**
     * Constructor for LoaderConfigurationException.
     *
     * @param message the detail message
     */
    public LoaderConfigurationException(String message) {
        super(message);
    }
}
