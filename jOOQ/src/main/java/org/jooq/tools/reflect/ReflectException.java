/**
 * Copyright (c) 2011-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOR" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.tools.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 * A unchecked wrapper for any of Java's checked reflection exceptions:
 * <p>
 * These exceptions are
 * <ul>
 * <li> {@link ClassNotFoundException}</li>
 * <li> {@link IllegalAccessException}</li>
 * <li> {@link IllegalArgumentException}</li>
 * <li> {@link InstantiationException}</li>
 * <li> {@link InvocationTargetException}</li>
 * <li> {@link NoSuchMethodException}</li>
 * <li> {@link NoSuchFieldException}</li>
 * <li> {@link SecurityException}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class ReflectException extends RuntimeException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6213149635297151442L;

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException() {
        super();
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}
