/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
