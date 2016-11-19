/*
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 *
 *
 *
 */
package org.jooq.exception;

import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockResult;

/**
 * An unexpected result was encountered while loading a {@link MockResult} from a
 * file-based {@link MockDataProvider}. This exception indicates that the number of rows
 * is not in accordance with the specified number of rows.
 * <p>
 * It should for instance not be allowed for the file-based {@link MockDataProvider} to have this content:
 * select "TABLE2"."ID2", "TABLE2"."NAME2" from "TABLE2"
 * > +---+-----+
 * > |ID2|NAME2|
 * > +---+-----+
 * > |1  |X    |
 * > |2  |Y    |
 * > +---+-----+
 *@ rows: 1000
 * @author Samy Deghou
 */
public class ErroneousRowSpecificationException extends DataAccessException {

    /**
     * TODO
     * Generated UID
     */
    private static long serialVersionUID;

    /**
     * Constructor for InvalidResultException.
     *
     * @param message the detail message
     */
    public ErroneousRowSpecificationException(String message) {
        super(message);
    }
}
