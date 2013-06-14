/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
 * . Neither the name "jOOQ" nor the names of its contributors may be
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

    protected void increment(Map<Method, Integer> map) {
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
