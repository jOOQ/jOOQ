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
package org.jooq.oss

import static java.util.regex.Pattern.*;

import java.io.File
import java.util.ArrayList
import java.util.regex.Pattern
import org.apache.commons.lang3.tuple.ImmutablePair
import org.jooq.SQLDialect
import org.jooq.xtend.Generators

class OSS extends Generators {
    
    def static void main(String[] args) {
        val oss = new OSS();
        
        val workspace = new File("..");
        val inRoot = new File(workspace, "jOOQ");
        val outRoot = new File(workspace, "OSS-jOOQ");
        
        oss.transform(inRoot, outRoot, inRoot);
    }

    def transform(File inRoot, File outRoot, File in) {
        val out = new File(outRoot.canonicalPath + "/" + in.canonicalPath.replace(inRoot.canonicalPath, ""));
        
        if (in.directory) {
            val files = in.listFiles[path | 
                   !path.canonicalPath.endsWith(".class") 
                && !path.canonicalPath.endsWith(".project")
                && !path.canonicalPath.endsWith("pom.xml")
                && !path.canonicalPath.contains("\\org\\jooq\\util\\access")
                && !path.canonicalPath.contains("\\org\\jooq\\util\\ase")
                && !path.canonicalPath.contains("\\org\\jooq\\util\\db2")
                && !path.canonicalPath.contains("\\org\\jooq\\util\\ingres")
                && !path.canonicalPath.contains("\\org\\jooq\\util\\oracle")
                && !path.canonicalPath.contains("\\org\\jooq\\util\\sqlserver")
                && !path.canonicalPath.contains("\\org\\jooq\\util\\sybase")
                && !path.canonicalPath.contains("\\target\\")
            ];

            for (file : files) {
                transform(inRoot, outRoot, file);
            }            
        }
        else {
            var content = read(in);

            for (pair : patterns) {
                content = pair.left.matcher(content).replaceAll(pair.right);
            }
            
            write(out, content);
        }
    }
    
    val patterns = new ArrayList<ImmutablePair<Pattern, String>>();
    
    new() {
        
        // Remove sections of commercial code
        patterns.add(new ImmutablePair(compile('''(?s:[ \t]+«quote("/* [com] */")»[ \t]*[\r\n]{0,2}.*?«quote("/* [/com] */")»[ \t]*[\r\n]{0,2})'''), ""));
        patterns.add(new ImmutablePair(compile('''(?s:«quote("/* [com] */")».*?«quote("/* [/com] */")»)'''), ""));
        
        for (d : SQLDialect::values.filter[d | d.commercial]) {
            
            // Remove commercial dialects from @Support annotations
            patterns.add(new ImmutablePair(compile('''(?s:(\@Support\([^\)]*?),\s*\b«d.name»\b([^\)]*?\)))'''), "$1$2"));
            patterns.add(new ImmutablePair(compile('''(?s:(\@Support\([^\)]*?)\b«d.name»\b,\s*([^\)]*?\)))'''), "$1$2"));
            patterns.add(new ImmutablePair(compile('''(?s:(\@Support\([^\)]*?)\s*\b«d.name»\b\s*([^\)]*?\)))'''), "$1$2"));
            
            // Remove commercial dialects from Arrays.asList() expressions
            patterns.add(new ImmutablePair(compile('''(asList\([^\)]*?),\s*\b«d.name»\b([^\)]*?\))'''), "$1$2"));
            patterns.add(new ImmutablePair(compile('''(asList\([^\)]*?)\b«d.name»\b,\s*([^\)]*?\))'''), "$1$2"));
            patterns.add(new ImmutablePair(compile('''(asList\([^\)]*?)\s*\b«d.name»\b\s*([^\)]*?\))'''), "$1$2"));
            
            // Remove commercial dialects from imports
            patterns.add(new ImmutablePair(compile('''import (static )?org\.jooq\.SQLDialect\.«d.name»;[\r\n]{0,2}'''), ""));
            patterns.add(new ImmutablePair(compile('''import (static )?org\.jooq\.util\.«d.name.toLowerCase»\..*?;[\r\n]{0,2}'''), ""));
        }
    }
}
