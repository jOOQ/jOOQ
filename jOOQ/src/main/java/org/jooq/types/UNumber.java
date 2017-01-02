/*
 * Copyright (c) 2011-2017, Data Geekery GmbH (http://www.datageekery.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jooq.types;

import java.math.BigInteger;

/**
 * A base type for unsigned numbers.
 *
 * @author Lukas Eder
 */
public abstract class UNumber extends Number {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7666221938815339843L;

    /**
     * Get this number as a {@link BigInteger}. This is a convenience method for
     * calling <code>new BigInteger(toString())</code>
     */
    public BigInteger toBigInteger() {
        return new BigInteger(toString());
    }
}
