/**
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
package org.jooq.oss

import java.io.File
import java.net.URI
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.ArrayList
import java.util.Arrays
import java.util.List
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.regex.Pattern
import org.jooq.SQLDialect
import org.jooq.xtend.Generators

import static java.io.File.separator
import static java.util.regex.Pattern.*

// Use this to generate the jOOQ Open Source Edition code
class RemoveProCode {
    def static void main(String[] args) {
        Splitter.split("/../workspace-3.4-jooq-oss", "jOOQ", "jOOQ", "pro");
    }
}

// Use this to generate the jOOQ Professional and Enterprise Edition code
class RemoveTrialCode {
    def static void main(String[] args) {
        Splitter.split("/../workspace-3.4-jooq-pro", "jOOQ", "jOOQ", "trial");
    }
}

// Use this to generate the jOOQ Professional and Enterprise Edition code for Java 6
class RemoveTrialAndJava8Code {
    def static void main(String[] args) {
        Splitter.split("/../workspace-3.4-jooq-pro-java-6", "jOOQ", "jOOQ", "trial", "java-8");
    }
}

// Use this to generate the jOOR Java 6 build
class RemoveJOORJava8Code {
    def static void main(String[] args) {
        Splitter.split("/../workspace", "jOOR/jOOR", "jOOR/jOOR-java-6", "java-8");
    }
}

class Splitter extends Generators {
    
    def static void main(String[] args) {
        val split = System.getProperty("split");
        
        if (split == "oss") {
            RemoveProCode.main(args);
        }
        else if (split == "pro") {
            RemoveTrialCode.main(args);
        }
        else if (split == "pro-java-6") {
            RemoveTrialAndJava8Code.main(args);
        }
        else {
            System.err.println("Usage: Splitter -Dsplit={oss, pro, pro-java-6}");
        }
    }

    static ExecutorService ex;
    static Pattern patterns = Pattern.compile('''(?:compile|split|replaceAll|replaceFirst)\("(.*?)"''')
    List<String> tokens;

    def static void split(String workspace, String projectIn, String projectOut, String... tokens) {
        ex = Executors.newFixedThreadPool(4);

        val splitter = new Splitter(tokens);

        val basePath = typeof(Splitter).getResource("/org/jooq/oss/Splitter.class").toString().replaceAll("^(.*?workspace[^/]*)/?.*$", "$1");
        val workspaceIn = new File(new URI(basePath)).canonicalFile;
        val workspaceOut = new File(workspaceIn.canonicalPath + workspace).canonicalFile;

        System.out.println('''Workspace IN : «workspaceIn», Project in: «projectIn»''');
        System.out.println('''Workspace OUT: «workspaceOut», Project out: «projectOut»''');

        // Workspace directly contains jOOQ content
        if (new File(workspaceIn, "pom.xml").exists) {
            splitter.transform(workspaceIn, workspaceOut, workspaceIn);
        }
        
        // Workspace contains a jOOQ subdirectory
        else {
            val in1 = new File(workspaceIn, projectIn);
            val out1 = new File(workspaceOut, projectOut);
            splitter.transform(in1, out1, in1);
            
            val in2Root = new File(workspaceIn, "jOOQ-website");
            val out2Root = new File(workspaceOut, "jOOQ/jOOQ-manual");
            val in2 = new File(workspaceIn, "jOOQ-website/src/main/resources/org/jooq/web");
            splitter.transform(in2Root, out2Root, in2);
        }

        ex.shutdown();
    }

    def void transform(File inRoot, File outRoot, File in) {
        val out = new File(outRoot.canonicalPath + "/" + in.canonicalPath.replace(inRoot.canonicalPath, ""));

        if (in.directory) {
            val files = in.listFiles[
                   !canonicalPath.endsWith(".class")
                && !canonicalPath.endsWith(".dat")
                && !canonicalPath.endsWith(".git")
                && !canonicalPath.endsWith(".jar")
                && !canonicalPath.endsWith(".pdf")
                && !canonicalPath.endsWith(".zip")
                && !canonicalPath.endsWith("._trace")
                && !canonicalPath.contains("jOOQ-explorer")
                && !canonicalPath.contains("jOOQ-playground")
                && !canonicalPath.contains("jOOQ-test")
                && !canonicalPath.contains("jOOQ-tools")
                && !canonicalPath.contains("jOOQ-tools-xjc")
                && !canonicalPath.contains("jOOQ-vaadin-example")
             // && !canonicalPath.contains("jOOQ-website")
                && !canonicalPath.contains("jOOQ-websites")
                && !canonicalPath.contains("jOOQ-webservices")
                && !canonicalPath.contains("jOOQ-jooby-example")
                && !canonicalPath.contains("jOOQ-parse")
                && !canonicalPath.contains(separator + "target" + separator)
                && !canonicalPath.contains(separator + "bin" + separator)
                && !canonicalPath.contains(separator + ".idea" + separator)
                && !canonicalPath.contains(separator + ".settings")
                && !canonicalPath.contains(separator + ".project")
                && !canonicalPath.endsWith(separator + ".classpath")

                // Activate this when we change anything to the Sakila db
                && !canonicalPath.contains(separator + "Sakila" + separator)

                && (tokens.contains("trial") || (
                       !canonicalPath.contains(separator + "access" + separator)
                    && !canonicalPath.contains(separator + "ase" + separator)
                    && !canonicalPath.contains(separator + "db2" + separator)
                    && !canonicalPath.contains(separator + "hana" + separator)
                    && !canonicalPath.contains(separator + "informix" + separator)
                    && !canonicalPath.contains(separator + "ingres" + separator)
                    && !canonicalPath.contains(separator + "jdbcoracle" + separator)
                    && !canonicalPath.contains(separator + "redshift" + separator)
                    && !canonicalPath.contains(separator + "oracle" + separator)
                    && !canonicalPath.contains(separator + "oracle2" + separator)
                    && !canonicalPath.contains(separator + "oracle3" + separator)
                    && !canonicalPath.contains(separator + "sqlserver" + separator)
                    && !canonicalPath.contains(separator + "sybase" + separator)
                    && !canonicalPath.contains(separator + "vertica" + separator)

                    && !canonicalPath.contains(separator + "jOOQ" + separator + "src" + separator + "test" + separator)
                ))
                
                && !canonicalPath.contains(separator + "jOOQ-scala" + separator + "src" + separator + "test" + separator)
            ];

            for (file : files) {
                transform(inRoot, outRoot, file);
            }
        }
        else if (tokens.contains("pro") && (
                    in.name.equals("LICENSE.txt") ||
                    in.name.equals("LICENSE"))
        ) {
            ex.submit[
                write(out, '''
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Other licenses:
-----------------------------------------------------------------------------
Commercial licenses for this work are available. These replace the above
ASL 2.0 and offer limited warranties, support, maintenance, and commercial
database integrations.

For more information, please visit: http://www.jooq.org/licenses''');
            ];
        }
        
        // Don't translate manual files
        else if (in.name.contains("manual") && in.name.endsWith(".xml")) {
            ex.submit[
                
                // People starting to reverse engineer the semantics of
                // masked out code blocks:
                // http://stackoverflow.com/questions/34530121/what-is-this-weird-comment-found-in-a-popular-library
                write(out, read(in).replaceAll("[ \t]+(\r?\n)", "$1"));
            ]
        }
        else {
            ex.submit[
                var original = read(in);
                var content = original;

                if (tokens.contains("java-8")) {
                    val matcher = patterns.matcher(original);
                    
                    while (matcher.find) {
                        val exp = matcher.group(1);
                        
                        if (exp.contains("\\R")) {
                            System.err.println("Pattern not supported in Java 6: " + exp);
                            return;
                        }
                    }
                }

                for (pattern : translateAll) {
                    val m = pattern.matcher(content);

                    while (m.find) {
                        content = content.substring(0, m.start)
                                + m.group(1).replaceAll("\\S", " ")
                                + m.group(2).replaceAll("\\S", " ")
                                + m.group(3).replaceAll("\\S", " ")
                                + content.substring(m.end);
                    }
                }
                for (pair : replaceFirst) {
                    content = pair.key.matcher(content).replaceAll(pair.value);
                }
                for (pair : replaceAll) {
                    content = pair.key.matcher(content).replaceAll(pair.value);
                }
                    
                // People starting to reverse engineer the semantics of
                // masked out code blocks:
                // http://stackoverflow.com/questions/34530121/what-is-this-weird-comment-found-in-a-popular-library
                content = content.replaceAll("[ \t]+(\r?\n)", "$1");
                write(out, content);
            ];
        }
    }

    val translateAll = new ArrayList<Pattern>();
    val replaceAll = new ArrayList<SimpleImmutableEntry<Pattern, String>>();
    val replaceFirst = new ArrayList<SimpleImmutableEntry<Pattern, String>>();

    new(String... tokens) {
        this.tokens = Arrays.asList(tokens);

        if (tokens.contains("pro")) {
            replaceFirst.add(new SimpleImmutableEntry(compile('''-trial\.jar'''), '''.jar'''));
            replaceFirst.add(new SimpleImmutableEntry(compile('''<groupId>org.jooq.trial</groupId>'''), '''<groupId>org.jooq</groupId>'''));
            replaceFirst.add(new SimpleImmutableEntry(compile('''-DgroupId=org.jooq.trial'''), '''-DgroupId=org.jooq'''));
        }
        else if (tokens.contains("java-8")) {
            replaceFirst.add(new SimpleImmutableEntry(compile('''-trial\.jar'''), '''-pro-java-6.jar'''));
            replaceFirst.add(new SimpleImmutableEntry(compile('''<groupId>org.jooq.trial</groupId>'''), '''<groupId>org.jooq.pro-java-6</groupId>'''));
            replaceFirst.add(new SimpleImmutableEntry(compile('''-DgroupId=org.jooq.trial'''), '''-DgroupId=org.jooq.pro-java-6'''));
            
            replaceFirst.add(new SimpleImmutableEntry(compile('''<artifactId>joor</artifactId>'''), '''<artifactId>joor-java-6</artifactId>'''));
            replaceFirst.add(new SimpleImmutableEntry(compile('''<source>1.8</source>'''), '''<source>1.6</source>'''));
            replaceFirst.add(new SimpleImmutableEntry(compile('''<target>1.8</target>'''), '''<target>1.6</target>'''));
        }
        else {
            replaceFirst.add(new SimpleImmutableEntry(compile('''-trial\.jar'''), '''-pro.jar'''));
            replaceFirst.add(new SimpleImmutableEntry(compile('''<groupId>org.jooq.trial</groupId>'''), '''<groupId>org.jooq.pro</groupId>'''));
            replaceFirst.add(new SimpleImmutableEntry(compile('''-DgroupId=org.jooq.trial'''), '''-DgroupId=org.jooq.pro'''));
        }

        if (tokens.contains("pro")) {

            // Replace a couple of imports
            replaceFirst.add(new SimpleImmutableEntry(compile('''import (org\.jooq\.(ArrayConstant|ArrayRecord|Link|VersionsBetweenAndStep|impl\.ArrayRecordImpl|impl\.FlashbackTable.*?)|(com.microsoft.*?));'''), "// ..."));
            replaceFirst.add(new SimpleImmutableEntry(compile('''import static org\.jooq\.impl\.DSL\.(link);'''), "// ..."));

            // Replace the Java / Scala / Xtend license header
            replaceFirst.add(new SimpleImmutableEntry(compile('''(?s:/\*\R \* This work is dual-licensed.*?\R \*/)'''), '''
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
 */'''));

            replaceAll.add(new SimpleImmutableEntry(compile('''/\* \[/?java-8\] \*/'''), ""));

            for (d : SQLDialect.values.filter[commercial]) {

                // Remove commercial dialects from @Support annotations
                replaceAll.add(new SimpleImmutableEntry(compile('''(?s:(\@Support\([^\)]*?),\s*\b«d.name()»\b([^\)]*?\)))'''), "$1$2"));
                replaceAll.add(new SimpleImmutableEntry(compile('''(?s:(\@Support\([^\)]*?)\b«d.name()»\b,\s*([^\)]*?\)))'''), "$1$2"));
                replaceAll.add(new SimpleImmutableEntry(compile('''(?s:(\@Support\([^\)]*?)\s*\b«d.name()»\b\s*([^\)]*?\)))'''), "$1$2"));

                // Remove commercial dialects from assume*(...) listings
                replaceAll.add(new SimpleImmutableEntry(compile('''(?:(assume(?:Family|Dialect)NotIn.*\([^\)]*?),\s*\b«d.name()»\b([^\)]*\)))'''), "$1$2"));
                replaceAll.add(new SimpleImmutableEntry(compile('''(?:(assume(?:Family|Dialect)NotIn.*\([^\)]*?)\b«d.name()»\b,\s*([^\)]*\)))'''), "$1$2"));
                replaceAll.add(new SimpleImmutableEntry(compile('''(?:(assume(?:Family|Dialect)NotIn.*\([^\)]*?)\s*\b«d.name()»\b\s*([^\)]*\)))'''), "$1$2"));

                // Remove commercial dialects from Arrays.asList() expressions
                replaceAll.add(new SimpleImmutableEntry(compile('''(asList\([^\)]*?),\s*\b«d.name()»\b([^\)]*?\))'''), "$1$2"));
                replaceAll.add(new SimpleImmutableEntry(compile('''(asList\([^\)]*?)\b«d.name()»\b,\s*([^\)]*?\))'''), "$1$2"));
                replaceAll.add(new SimpleImmutableEntry(compile('''(asList\([^\)]*?)\s*\b«d.name()»\b\s*([^\)]*?\))'''), "$1$2"));

                // Remove commercial dialects from EnumSet.of() expressions
                replaceAll.add(new SimpleImmutableEntry(compile('''(EnumSet.of\([^\)]*?),\s*\b«d.name()»\b([^\)]*?\))'''), "$1$2"));
                replaceAll.add(new SimpleImmutableEntry(compile('''(EnumSet.of\([^\)]*?)\b«d.name()»\b,\s*([^\)]*?\))'''), "$1$2"));
                replaceAll.add(new SimpleImmutableEntry(compile('''(EnumSet.of\([^\)]*?)\s*\b«d.name()»\b\s*([^\)]*?\))'''), "$1$2"));

                // Remove commercial dialects from imports
                replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?org\.jooq\.SQLDialect\.«d.name()»;'''), "// ..."));
                replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?org\.jooq\.util\.«d.name().toLowerCase»\..*?;'''), "// ..."));
            }
        }

        if (tokens.contains("java-8")) {

            // Remove all Java 7 imports
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.lang\.AutoCloseable;'''), "// ..."));

            // Remove all Java 8 imports
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.lang\.FunctionalInterface;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.lang\.invoke\.MethodHandles?;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.sql\.SQLType;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.time\..*?;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.Optional;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.Spliterator;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.Spliterators;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.stream\..*?;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.function\..*?;'''), "// ..."));
            
            replaceAll.add(new SimpleImmutableEntry(compile('''import static org\.jooq\.impl\.Tools\.blocking;'''), "// ..."));
        }

        // Remove sections of delimited code
        for (token : tokens) {
            translateAll.add(compile('''(?s:(/\* \[«token»\])( \*.*?/\* )(\[/«token»\] \*/))'''));
            translateAll.add(compile('''(?s:(<!-- \[«token»\])( -->.*?<!-- )(\[/«token»\] -->))'''));
            translateAll.add(compile('''(?s:(# \[«token»\])()(?:.*?)(# \[/«token»\]))'''));
            translateAll.add(compile('''(?s:(rem \[«token»\])(.*?)(rem \[/«token»\]))'''));
        }
    }
}
