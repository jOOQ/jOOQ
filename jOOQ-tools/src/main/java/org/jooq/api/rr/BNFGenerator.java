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
package org.jooq.api.rr;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.jooq.api.annotation.State;
import org.jooq.api.annotation.Transition;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;


/**
 * A parser for <code>org.jooq.api.annotation</code> annotations, generating a
 * BNF notation of the jOOQ API.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public class BNFGenerator {

    static Map<String, Set<Edge>> edgesFrom = new LinkedHashMap<String, Set<Edge>>();
    static Map<String, Set<Edge>> edgesTo = new LinkedHashMap<String, Set<Edge>>();
    static Map<Path, Set<Edge>> edges = new LinkedHashMap<Path, Set<Edge>>();
    static Set<Class<?>> visited = new LinkedHashSet<Class<?>>();
    static Set<String> terminals = new LinkedHashSet<String>();

    public static void main(String[] args) throws Exception {
    	resolve(DSL.class);
        resolveStates();

        grammar("before substituting");
        substitute();
        grammar("after substituting");

        System.out.println();
        System.out.println();

        for (String terminal : terminals) {
        	if (!edgesTo.containsKey(terminal)) {
        		System.err.println("Terminal is not contained in grammar: " + terminal);
        	}
        }

        for (String name : edgesTo.keySet()) {
        	if (!terminals.contains(name)) {
        		System.err.println("Rule for a non-terminal in grammar: " + name);
        	}
        }
    }

	private static void grammar(String comment) {
		System.out.println();
        System.out.println();
        System.out.println();

        String s = "Grammar: (" + comment + ")";
		System.out.println(s);
        System.out.println(StringUtils.rightPad("", s.length(), "-"));

        for (Entry<String, Set<Edge>> entry : edgesTo.entrySet()) {
            System.out.println(string(entry.getValue()));
        }
	}

    private static String string(Edge... e) {
        return string(Arrays.asList(e));
    }

    private static String string(Collection<? extends Edge> e) {
        return string(e, true);
    }

    private static String string(Collection<? extends Edge> e, boolean collect) {
        StringBuilder sb = new StringBuilder();
        String previous = "";
        String separator = "";

        for (Edge edge : e) {
            if (!edge.path.to.equals(previous) || !collect) {
                if (sb.length() > 0) {
                    sb.append(";\n");
                }

                sb.append(edge.path.to);
                sb.append(" ::= ");

                previous = edge.path.to;
                separator = "";
            }

            sb.append(separator);
            sb.append(edge);

            separator = " | ";
        }

        sb.append(";");
        return sb.toString();
    }

    private static void substitute() {
        // Remove all states that appear exactly once as from and once as to

        pruneLoop:
        while (true) {

        	// Some debugging....
        	String check = "WindowPartitionByStep";
            Set<Edge> e = edgesTo.get(check);
            if (e != null) {
                System.err.println();
                System.err.println();
                System.err.println();
                System.err.println(string(e, false));
                System.err.println(string(edgesFrom.get(check), false));
                System.err.println();
                System.err.println();
                System.err.println();
            }

            // Alternatives with equal paths are substituted
            // A ::= B x | B y; => A ::= B ( x | y );
            // -----------------------------------------------------------------
            for (Path path : new ArrayList<Path>(edges.keySet())) {
                Set<Edge> set = edges.get(path);

                if (set.size() > 1) {
                    List<Edge> list = new ArrayList<Edge>(set);
                    List<Expr> expr = new ArrayList<Expr>();

                    for (Edge edge : list) {
                        expr.add(edge.expr);
                    }

                    Edge substitute = new Edge(
                        list.get(0).path,
                        new Choice(expr.toArray(new Expr[0]))
                    );

                    for (Edge edge : list) {
                        remove(edge);
                    }

                    edge(substitute);

                    System.out.println();
                    System.out.println("Substituted Choice: ( A ::= B x | B y; => A ::= B ( x | y ); )");
                    System.out.println("--------------------------------------------------------------");
                    System.out.println(string(list, false));
                    System.out.println(" -> ");
                    System.out.println(string(substitute));
                    continue pruneLoop;
                }
            }

            // Alternatives with equal expressions are substituted
            // A ::= X a | Y a; B ::= A b; => A ::= X | Y; B ::= A a b;
            // -----------------------------------------------------------------
            factoringLoop:
            for (String name : new ArrayList<String>(edgesTo.keySet())) {
                if (terminals.contains(name)) {
                    continue factoringLoop;
                }

                Set<Edge> set = edgesTo.get(name);
                Set<Expr> expressions = new HashSet<Expr>();

                if (set.size() > 1) {
                    for (Edge edge : set) {
                        expressions.add(edge.expr);
                    }

                    if (expressions.size() == 1) {
                        Expr expr = expressions.iterator().next();

                        if (expr.toString().length() > 0) {
                            List<Edge> list = new ArrayList<Edge>(set);
                            List<Edge> before = new ArrayList<Edge>(edgesFrom.get(name));

                            for (Edge edge : list) {
                                remove(edge);
                                edge(new Edge(edge.path));
                            }

                            for (Edge edge : before) {
                                remove(edge);
                                edge(new Edge(edge.path, new Sequence(expr, edge.expr)));
                            }

                            System.out.println();
                            System.out.println("Factored out Expression: ( A ::= X a | Y a; B ::= A b; => A ::= X | Y; B ::= A a b; )");
                            System.out.println("-------------------------------------------------------------------------------------");
                            System.out.println(string(list, false));
                            System.out.println(string(before, false));
                            System.out.println(" -> ");
                            System.out.println(string(edgesTo.get(name), false));
                            System.out.println(string(edgesFrom.get(name), false));
                        }
                    }
                }
            }

            // Sequences are substituted (only if B consumed exactly once)
            // A ::= B x; B ::= C y; => A ::= C y x;
            // -----------------------------------------------------------------
            for (int maxSize : new int[] { 1, Integer.MAX_VALUE })
            substitutionLoop:
            for (String name : new ArrayList<String>(edgesFrom.keySet())) {
                if (terminals.contains(name)) {
                    continue substitutionLoop;
                }

                if (edgesFrom.get(name).size() == 1) {
                	Edge from = edgesFrom.get(name).iterator().next();
                	Set<Edge> set = edgesTo.get(name);

                	if (set != null) {
                	    if (set.size() > maxSize) {
                	        continue substitutionLoop;
                	    }

                	    if (set.size() > 1) {
                	        if (!"".equals(from.expr.toString())) {
                	            continue substitutionLoop;
                	        }
                	        else {
                	            System.out.println();
                	            System.out.println();
                	            System.out.println("Substituting multi-sequence");
                	            System.out.println(string(from));
                	            System.out.println("...");
                	            System.out.println(string(set));
                	        }
                	    }

                		List<Edge> list = new ArrayList<Edge>(set);
                		List<Edge> substitutes = new ArrayList<Edge>();

                        remove(from);

                        for (Edge to : list) {
                        	remove(to);

                        	Edge substitute = new Edge(
                    			new Path(to.path.from, from.path.to),
                    			new Sequence(to.expr, from.expr)
                			);

                        	edge(substitute);
                        	substitutes.add(substitute);
                        }

                        System.out.println();
                        System.out.println("Substituted Sequence: ( A ::= B x; B ::= C y; => A ::= C y x; )");
                        System.out.println("---------------------------------------------------------------");
                        System.out.println(string(list, false));
                        System.out.println(string(from));
                        System.out.println(" -> ");
                        System.out.println(string(substitutes, false));
                        continue pruneLoop;
                    }
                }
            }

            break pruneLoop;
        }
    }

    private static void resolveStates() {
        for (Class<?> type : visited) {
            State state = type.getAnnotation(State.class);

            if (state != null) {
                String stateName = state.name();
                if ("".equals(stateName)) {
                    stateName = type.getSimpleName();
                }

                // Collect terminals and add edges from states to their aliases
                // -------------------------------------------------------------
                if (notEmpty(state.aliases())) {
                    for (String alias : state.aliases()) {
                        edge(new Edge(new Path(alias, stateName)));

                        if (state.terminal()) {
                            terminals.add(alias);
                        }
                    }
                }

                if (state.terminal()) {
                    terminals.add(stateName);
                }

                // Add edges from states to their super states
                // -------------------------------------------------------------
                for (Class<?> superType : type.getInterfaces()) {
                    State superState = superType.getAnnotation(State.class);

                    if (superState != null) {
                        String superStateName = superState.name();
                        if ("".equals(superStateName)) {
                            superStateName = superType.getSimpleName();
                        }

                        edge(new Edge(new Path(stateName, superStateName)));
                    }
                }
            }
        }
    }

    private static boolean notEmpty(String[] array) {
        return array != null
            && array.length > 0
            && array[0] != null
            && array[0].length() > 0;
    }

    private static void resolve(Class<?> type) {
    	if (!visited.contains(type)) {
    		visited.add(type);

    		for (Class<?> i : type.getInterfaces()) {
	    		resolve(i);
	    	}

	    	resolve(type.getDeclaredMethods());
    	}
    }

    private static void resolve(Method[] methods) {
        for (Method method : methods) {
            Transition transition = method.getAnnotation(Transition.class);

            if (transition != null) {
            	Class<?> declaringClass = method.getDeclaringClass();
            	Class<?> returnType = method.getReturnType();

                // From state
                // -------------------------------------------------------------
                String from = transition.from();
                if ("".equals(from)) {
                    State fromState = declaringClass.getAnnotation(State.class);

                    if (fromState == null) {
                        System.err.println("No State defined on " + declaringClass.getName());
                    }
                    else {
                        from = fromState.name();
                    }
                }

                if ("".equals(from)) {
                    from = declaringClass.getSimpleName();
                }

                // To state
                // -------------------------------------------------------------
                String to = transition.to();
                if ("".equals(to)) {
                    State toState = returnType.getAnnotation(State.class);

                    if (toState == null) {
                        System.err.println("No State defined on " + returnType.getName());
                    }
                    else {
                        to = toState.name();
                    }
                }

                if ("".equals(to)) {
                    to = returnType.getSimpleName();
                }

                // Transition name
                // -------------------------------------------------------------
                String name = transition.name();
                if ("".equals(name)) {
                    name = method.getName();
                }

                // Transition arguments
                // -------------------------------------------------------------
                String[] args = transition.args();

                // Calculation
                // -------------------------------------------------------------
                edge(new Edge(new Path(from, to), new Args(name, args)));
                resolve(returnType);
            }
        }
    }

    private static void remove(Edge edge) {
        remove(edge, edges, edge.path);
        remove(edge, edgesFrom, edge.path.from);
        remove(edge, edgesTo, edge.path.to);
    }

    private static <K> void remove(Edge edge, Map<K, Set<Edge>> map, K key) {
        Set<Edge> set = map.get(key);

        if (set != null) {
            set.remove(edge);

            if (set.isEmpty()) {
                map.remove(key);
            }
        }
    }

    private static void edge(Edge edge) {
        edge(edge, edges, edge.path);
        edge(edge, edgesFrom, edge.path.from);
        edge(edge, edgesTo, edge.path.to);
    }

    private static <K> void edge(Edge edge, Map<K, Set<Edge>> map, K key) {
        Set<Edge> set = map.get(key);

        if (set == null) {
            set = new TreeSet<Edge>();
            map.put(key, set);
        }

        set.add(edge);
    }

    private static class Path implements Comparable<Path> {
        final String from;
        final String to;

        Path(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public int compareTo(Path o) {
            int result = 0;

            if (to != null) {
                result = to.compareTo(o.to);
                if (result != 0)
                    return result;
            }

            if (from != null) {
                result = from.compareTo(o.from);
                if (result != 0)
                    return result;
            }

            return result;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((from == null) ? 0 : from.hashCode());
            result = prime * result + ((to == null) ? 0 : to.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Path other = (Path) obj;
            if (from == null) {
                if (other.from != null)
                    return false;
            } else if (!from.equals(other.from))
                return false;
            if (to == null) {
                if (other.to != null)
                    return false;
            } else if (!to.equals(other.to))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return from + " -> " + to;
        }
    }

    private static class Args extends Expr {
        final String name;
        final String[] args;

        Args(String name, String[] args) {
            this.name = name;
            this.args = args;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (!"".equals(name)) {
                sb.append("'");
                sb.append(name);
                sb.append("'");
            }

            if (notEmpty(args)) {
                sb.append(" ");

                String separator = "";
                for (String arg : args) {
                    sb.append(separator);

                    if (arg.endsWith("+")) {
                        String sub = arg.substring(0, arg.length() - 1);
                        sb.append(sub);
                        sb.append(" { ',' ");
                        sb.append(sub);
                        sb.append(" }");
                    }
                    else {
                        sb.append(arg);
                    }

                    separator = " ',' ";
                }
            }

            return sb.toString();
        }
    }

    private static class Choice extends Expr {
        final Expr[] expr;

        Choice(Expr... expr) {
            this.expr = expr;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            boolean optional = false;
            boolean any = false;

            if (expr.length > 1)
                sb.append("( ");

            String separator = "";
            for (Expr e : expr) {
                String s = e.toString();

                if ("".equals(s)) {
                    optional = true;
                    continue;
                }

                any = true;

                sb.append(separator);
                sb.append(e);

                separator = " | ";
            }

            if (expr.length > 1)
                sb.append(" )");

            if (optional)
                sb.append(" ?");

            return any ? sb.toString() : "";
        }
    }

    private static class Sequence extends Expr {
        final Expr[] expr;

        Sequence(Expr... expr) {
            this.expr = expr;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            String separator = "";
            for (Expr e : expr) {
            	String s = e.toString();

            	if (s.length() > 0) {
	                sb.append(separator);
	                sb.append(e);

	                separator = " ";
            	}
            }

            return sb.toString();
        }
    }

    private static abstract class Expr implements Comparable<Expr> {

        @Override
        public final int compareTo(Expr o) {
            return toString().compareTo(o.toString());
        }

        @Override
        public final int hashCode() {
            return toString().hashCode();
        }

        @Override
        public final boolean equals(Object obj) {
            return toString().equals(obj.toString());
        }
    }

    private static class Edge implements Comparable<Edge> {

        final Path path;
        final Expr expr;

        Edge(Path path) {
            this(path, new Sequence());
        }

        Edge(Path path, Expr expr) {
            this.path = path;
            this.expr = expr;
        }

        @Override
        public int compareTo(Edge o) {
            int result = 0;

            if (path != null) {
                result = path.compareTo(o.path);
                if (result != 0)
                    return result;
            }

            if (expr != null) {
                result = expr.compareTo(o.expr);
                if (result != 0)
                    return result;
            }

            return result;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((expr == null) ? 0 : expr.hashCode());
            result = prime * result + ((path == null) ? 0 : path.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Edge other = (Edge) obj;
            if (expr == null) {
                if (other.expr != null)
                    return false;
            } else if (!expr.equals(other.expr))
                return false;
            if (path == null) {
                if (other.path != null)
                    return false;
            } else if (!path.equals(other.path))
                return false;
            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            String s1 = "";
            String s2 = "";

            if (path.from != null && !"DSL".equals(path.from)) {
                s1 = path.from;
            }

            s2 = expr.toString();

            sb.append(s1);
            if (!s1.isEmpty() && !s2.isEmpty())
                sb.append(" ");
            sb.append(s2);

            return sb.toString();
        }
    }
}
