/*
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
package org.jooq.tools.reflect;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

// import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;


/**
 * A utility that simplifies in-memory compilation of new classes.
 *
 * @author Lukas Eder
 */
class Compile {

    static Class<?> compile(String className, String content) {
        Lookup lookup = MethodHandles.lookup();
        ClassLoader cl = lookup.lookupClass().getClassLoader();

        try {
            return cl.loadClass(className);
        }
        catch (ClassNotFoundException ignore) {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            try {
                ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));

                List<CharSequenceJavaFileObject> files = new ArrayList<CharSequenceJavaFileObject>();
                files.add(new CharSequenceJavaFileObject(className, content));
                StringWriter out = new StringWriter();

                List<String> options = new ArrayList<String>();
                StringBuilder classpath = new StringBuilder();
                String separator = System.getProperty("path.separator");
                String prop = System.getProperty("java.class.path");

                if (prop != null && !"".equals(prop))
                    classpath.append(prop);

                if (cl instanceof URLClassLoader) {
                    for (URL url : ((URLClassLoader) cl).getURLs()) {
                        if (classpath.length() > 0)
                            classpath.append(separator);

                        if ("file".equals(url.getProtocol()))
                            classpath.append(new File(url.getFile()));
                    }
                }

                options.addAll(Arrays.asList("-classpath", classpath.toString()));
                compiler.getTask(out, fileManager, null, options, null, files).call();

                if (fileManager.o == null)
                    throw new ReflectException("Compilation error: " + out);

                Class<?> result = null;

                // This works if we have private-access to the interfaces in the class hierarchy
                // if (Reflect.CACHED_LOOKUP_CONSTRUCTOR != null) {
                    byte[] b = fileManager.o.getBytes();
                    result = Reflect.on(cl).call("defineClass", className, b, 0, b.length).get();
                // }
                /* [java-9] */

                // Lookup.defineClass() has only been introduced in Java 9. It is
                // required to get private-access to interfaces in the class hierarchy
                // else {
                //
                //     // This method is called by client code from two levels up the current stack frame
                //     // We need a private-access lookup from the class in that stack frame in order to get
                //     // private-access to any local interfaces at that location.
                //     Class<?> caller = StackWalker
                //         .getInstance(RETAIN_CLASS_REFERENCE)
                //         .walk(s -> s
                //             .skip(2)
                //             .findFirst()
                //             .get()
                //             .getDeclaringClass());
                //
                //     // If the compiled class is in the same package as the caller class, then
                //     // we can use the private-access Lookup of the caller class
                //     if (className.startsWith(caller.getPackageName() + ".")) {
                //         result = MethodHandles
                //             .privateLookupIn(caller, lookup)
                //             .defineClass(fileManager.o.getBytes());
                //     }
                //
                //     // Otherwise, use an arbitrary class loader. This approach doesn't allow for
                //     // loading private-access interfaces in the compiled class's type hierarchy
                //     else {
                //         result = new ClassLoader() {
                //             @Override
                //             protected Class<?> findClass(String name) throws ClassNotFoundException {
                //                 byte[] b = fileManager.o.getBytes();
                //                 return defineClass(className, b, 0, b.length);
                //             }
                //         }.loadClass(className);
                //     }
                // }
                /* [/java-9] */

                return result;
            }
            catch (ReflectException e) {
                throw e;
            }
            catch (Exception e) {
                throw new ReflectException("Error while compiling " + className, e);
            }
        }
    }

    static final class JavaFileObject extends SimpleJavaFileObject {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        JavaFileObject(String name, JavaFileObject.Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        }

        byte[] getBytes() {
            return os.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() {
            return os;
        }
    }

    static final class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        JavaFileObject o;

        ClassFileManager(StandardJavaFileManager standardManager) {
            super(standardManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location,
            String className,
            JavaFileObject.Kind kind,
            FileObject sibling
        ) {
            return o = new JavaFileObject(className, kind);
        }
    }

    static final class CharSequenceJavaFileObject extends SimpleJavaFileObject {
        final CharSequence content;

        public CharSequenceJavaFileObject(String className, CharSequence content) {
            super(URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }
}

