/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.test.all.listeners;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.jooq.lambda.Unchecked;

import org.junit.Test;

/**
 * An <code>ExecuteListener</code> that collects data about the lifecycle of
 * execute listeners in all integration tests.
 *
 * @author Lukas Eder
 */
public class Lifecycle {

    protected static Comparator<Method>        METHOD_COMPARATOR = new Comparator<Method>() {
        @Override
        public int compare(Method o1, Method o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private static final Object                INCREMENT_MONITOR = new Object();
    private static final Map<String, Method[]> METHODS           = new HashMap<>();

    protected static final void increment(Map<Method, Integer> map) {
        synchronized (INCREMENT_MONITOR) {
            Method m = testMethod();

            if (m != null) {
                Integer count = map.get(m);

                if (count == null) {
                    count = 0;
                }

                count = count + 1;
                map.put(m, count);
            }
        }
    }

    private static Method testMethod() {
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            try {
                if (e.getClassName().startsWith("org.jooq.test"))
                    for (Method m : METHODS.computeIfAbsent(e.getClassName(), Unchecked.function(x -> Class.forName(e.getClassName()).getMethods())))
                        if (m.getName().equals(e.getMethodName()) && m.getAnnotation(Test.class) != null)
                            return m;

            }
            catch (Exception ignore) {}
        }

        return null;
    }
}
