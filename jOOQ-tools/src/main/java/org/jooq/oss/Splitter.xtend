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
package org.jooq.oss

import java.io.File
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.ArrayList
import java.util.Arrays
import java.util.List
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern
import org.jooq.SQLDialect
import org.jooq.xtend.Generators

import static java.util.regex.Pattern.*

// Use this to generate the jOOQ Open Source Edition code
class RemoveProCode {
    def static void main(String[] args) {
        Splitter.split("/../workspace-jooq-oss", "pro");
    }
}

// Use this to generate the jOOQ Professional and Enterprise Edition code
class RemoveTrialCode {
    def static void main(String[] args) {
        Splitter.split("/../workspace-jooq-pro", "trial");
    }
}

// Use this to generate the jOOQ Professional and Enterprise Edition code for Java 6
class RemoveTrialAndJava8Code {
    def static void main(String[] args) {
        Splitter.split("/../workspace-jooq-pro-java-6", "trial", "java-8");
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
    static AtomicInteger charsTotal = new AtomicInteger(0);
    static AtomicInteger charsMasked = new AtomicInteger(0);
    List<String> tokens;

    def static void split(String workspace, String... tokens) {
        ex = Executors.newFixedThreadPool(4);

        val splitter = new Splitter(tokens);

        val workspaceIn = new File("../..").canonicalFile;
        val workspaceOut = new File(workspaceIn.canonicalPath + workspace).canonicalFile;

        for (project : workspaceIn.listFiles[name.startsWith("jOOQ")]) {
            val in = new File(workspaceIn, project.name);
            val out = new File(workspaceOut, project.name);
            splitter.transform(in, out, in);
        }

        ex.shutdown();
//        ex.awaitTermination(1, TimeUnit.MINUTES);
//
//        System.out.println();
//        System.out.println("Total  chars : " + charsTotal);
//        System.out.println("Masked chars : " + charsMasked);
//        System.out.println("Percentage   : " + (100.0 * charsMasked.get / charsTotal.get));
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
                && !canonicalPath.contains("jOOQ-test")
                && !canonicalPath.contains("jOOQ-tools")
                && !canonicalPath.contains("jOOQ-vaadin-example")
                && !canonicalPath.contains("jOOQ-website")
                && !canonicalPath.contains("jOOQ-websites")
                && !canonicalPath.contains("jOOQ-webservices")
                && !canonicalPath.contains("jOOQ-parse")
                && !canonicalPath.contains("\\target\\")
                && !canonicalPath.contains("\\bin\\")
                && !canonicalPath.contains("\\.idea\\")
                && !canonicalPath.contains("\\.settings")
                && !canonicalPath.contains("\\.project")
                && !canonicalPath.endsWith("\\.classpath")

                // Activate this when we change anything to the Sakila db
                && !canonicalPath.contains("\\Sakila\\")

                && (tokens.contains("trial") || (
                       !canonicalPath.contains("\\access\\")
                    && !canonicalPath.contains("\\ase\\")
                    && !canonicalPath.contains("\\db2\\")
                    && !canonicalPath.contains("\\hana\\")
                    && !canonicalPath.contains("\\informix\\")
                    && !canonicalPath.contains("\\ingres\\")
                    && !canonicalPath.contains("\\jdbcoracle\\")
                    && !canonicalPath.contains("\\redshift\\")
                    && !canonicalPath.contains("\\oracle\\")
                    && !canonicalPath.contains("\\oracle2\\")
                    && !canonicalPath.contains("\\oracle3\\")
                    && !canonicalPath.contains("\\sqlserver\\")
                    && !canonicalPath.contains("\\sybase\\")
                    && !canonicalPath.contains("\\vertica\\")
                ))
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
Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
All rights reserved.

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
        else {
            ex.submit[
                var original = read(in);
                var content = original;

                for (pattern : translateAll) {
                    val m = pattern.matcher(content);

                    while (m.find) {
                        content = content.substring(0, m.start)
                                + m.group(1)
                                + m.group(2).replaceAll("\\S", "x")
                                + m.group(3)
                                + content.substring(m.end);
                    }
                }
                for (pair : replaceFirst) {
                    content = pair.key.matcher(content).replaceAll(pair.value);
                }
                for (pair : replaceAll) {
                    content = pair.key.matcher(content).replaceAll(pair.value);
                }

                write(out, content);
                compare(content, original);
            ];
        }
    }

    def compare(String content, String original) {
        charsTotal.addAndGet(original.length);

        for (i : 0 .. Math.min(content.length, original.length)) {
            if (("" + content.charAt(i) == "x") && ("" + original.charAt(i) != "x")) {
                charsMasked.incrementAndGet;
            }
        }
    }

    val translateAll = new ArrayList<Pattern>();
    val replaceAll = new ArrayList<SimpleImmutableEntry<Pattern, String>>();
    val replaceFirst = new ArrayList<SimpleImmutableEntry<Pattern, String>>();

    new(String... tokens) {
        this.tokens = Arrays.asList(tokens);

        if (tokens.contains("pro")) {
            replaceFirst.add(new SimpleImmutableEntry(compile('''-trial\.jar'''), '''.jar'''));
        }
        else if (tokens.contains("java-8")) {
            replaceFirst.add(new SimpleImmutableEntry(compile('''-trial\.jar'''), '''-pro-java-6.jar'''));
        }
        else {
            replaceFirst.add(new SimpleImmutableEntry(compile('''-trial\.jar'''), '''-pro.jar'''));
        }

        if (tokens.contains("pro")) {

            // Replace a couple of imports
            replaceFirst.add(new SimpleImmutableEntry(compile('''import (org\.jooq\.(ArrayConstant|ArrayRecord|VersionsBetweenAndStep|impl\.ArrayRecordImpl|impl\.FlashbackTable.*?)|(com.microsoft.*?));'''), "// ..."));
            replaceFirst.add(new SimpleImmutableEntry(compile('''import static org\.jooq\.impl\.DSL\.(cube|grouping|groupingId|groupingSets|link);'''), "// ..."));

            // Replace the Java / Scala / Xtend license header
            replaceFirst.add(new SimpleImmutableEntry(compile('''(?s:/\*\*\R \* Copyright.*?Data Geekery GmbH.*?\R \*/)'''), '''
/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
 */'''));

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

                // Remove commercial dialects from imports
                replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?org\.jooq\.SQLDialect\.«d.name()»;'''), "// ..."));
                replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?org\.jooq\.util\.«d.name().toLowerCase»\..*?;'''), "// ..."));
                replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?org\.jooq\..*?(\b|(?<=_))«d.name().toUpperCase»(\b|(?=_)).*?;'''), "// ..."));
            }
        }

        if (tokens.contains("java-8")) {

            // Remove all Java 7 imports
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.lang\.AutoCloseable;'''), "// ..."));

            // Remove all Java 8 imports
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.time\..*?;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.Optional;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.Spliterator;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.Spliterators;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.stream\..*?;'''), "// ..."));
            replaceAll.add(new SimpleImmutableEntry(compile('''import (static )?java\.util\.function\..*?;'''), "// ..."));
        }

        // Remove sections of delimited code
        for (token : tokens) {
            translateAll.add(compile('''(?s:(/\* \[«token»\])( \*.*?/\* )(\[/«token»\] \*/))'''));
            translateAll.add(compile('''(?s:(<!-- \[«token»\])( -->.*?<!-- )(\[/«token»\] -->))'''));
            translateAll.add(compile('''(?s:(# \[«token»\])()(?:.*?)(# \[/«token»\]))'''));
        }
    }
}
