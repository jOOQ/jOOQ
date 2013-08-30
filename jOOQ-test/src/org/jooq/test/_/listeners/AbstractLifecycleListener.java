/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.test._.listeners;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;

import org.jooq.impl.DefaultExecuteListener;

import org.junit.Test;

/**
 * An <code>ExecuteListener</code> that collects data about the lifecycle of
 * execute listeners in all integration tests.
 *
 * @author Lukas Eder
 */
public abstract class AbstractLifecycleListener extends DefaultExecuteListener {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID  = -2283264126211556442L;

    protected static Comparator<Method> METHOD_COMPARATOR = new Comparator<Method>() {

                                                              @Override
                                                              public int compare(Method o1, Method o2) {
                                                                  return o1.getName().compareTo(o2.getName());
                                                              }
                                                          };

    private static final Object         INCREMENT_MONITOR = new Object();

    protected final void increment(Map<Method, Integer> map) {
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
                for (Method m : Class.forName(e.getClassName()).getMethods()) {
                    if (m.getName().equals(e.getMethodName()) && m.getAnnotation(Test.class) != null) {
                        return m;
                    }
                }
            }
            catch (Exception ignore) {}
        }

        return null;
    }
}
