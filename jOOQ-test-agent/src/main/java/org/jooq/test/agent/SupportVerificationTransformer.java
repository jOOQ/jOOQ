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
package org.jooq.test.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
public class SupportVerificationTransformer implements ClassFileTransformer {

    private static final JooqLogger log = JooqLogger.getLogger(SupportVerificationTransformer.class);
    private ClassPool               classPool;

    public SupportVerificationTransformer() {
        classPool = new ClassPool();
        classPool.appendSystemPath();

        try {
            classPool.appendPathList(System.getProperty("java.class.path"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] transform(
        ClassLoader loader,
        String className,
        Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain,
        byte[] classfileBuffer
    ) throws IllegalClassFormatException {
        className = className.replace("/", ".");

        // Consider only jOOQ classes
        if (!className.startsWith("org.jooq.") ||
             className.startsWith("org.jooq.lambda."))
            return null;

        classPool.appendClassPath(new ByteArrayClassPath(className, classfileBuffer));

        try {
            CtClass ctClass = classPool.get(className);
            boolean isClassModified = false;

            for (CtMethod method : ctClass.getDeclaredMethods()) {
                Support support = support(ctClass, method);

                if (support != null) {
                    if (method.getMethodInfo().getCodeAttribute() == null)
                        continue;

                    StringBuilder dialects = new StringBuilder();
                    String separator = "";
                    for (SQLDialect dialect : support.value()) {
                        dialects.append(separator);
                        dialects.append("org.jooq.SQLDialect.");
                        dialects.append(dialect.name());

                        separator = ", ";
                    }

                    method.insertBefore(
                        "org.jooq.test.jOOQAbstractTest.call"
                      + "(\""
                      + method.getLongName() + "\", "
                      + (dialects.length() > 0
                      ? "new org.jooq.SQLDialect[] { " + dialects + " }"
                      : "new org.jooq.SQLDialect[0]")
                      + ");"
                    );
                    isClassModified = true;
                }
            }

            if (!isClassModified)
                return null;

            return ctClass.toBytecode();
        }
        catch (Exception e) {
            log.info("Skip class " + className, e.getMessage());
            return null;
        }
    }

    private Support support(CtClass clazz, CtMethod method) {
        try {
            String methodName = method.getName();
            CtClass[] methodParams = method.getParameterTypes();

            if (method.hasAnnotation(Support.class))
                return (Support) method.getAnnotation(Support.class);

            Set<CtClass> set = new LinkedHashSet<CtClass>();
            set.add(clazz);
            while (true) {
                int before = set.size();

                for (CtClass c : new ArrayList<CtClass>(set)) {
                    set.addAll(Arrays.asList(c.getInterfaces()));
                    if (c.getSuperclass() != null)
                        set.add(c.getSuperclass());
                }

                if (set.size() == before)
                    break;
            }

            for (CtClass c : set) {
                try {
                    CtMethod m = c.getDeclaredMethod(methodName, methodParams);

                    if (m.hasAnnotation(Support.class))
                        return (Support) m.getAnnotation(Support.class);
                }
                catch (Exception ignore) {}
            }
        }
        catch (Exception ignore) {
        }

        return null;
    }
}
