/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
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
package org.jooq.lambda.generator

import java.io.{File, PrintWriter}

object Generator {
  def main(args: Array[String]) {
    val max = 8;
    val copyright = """/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 *
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
"""
    write(
      "src/main/java/org/jooq/lambda/tuple/Tuple.java",
      s"""$copyright
package org.jooq.lambda.tuple;

import java.util.List;

/**
 * A tuple.
 *
 * @author Lukas Eder
 */
public interface Tuple extends Iterable<Object> {
${(for (degree <- (1 to max)) yield s"""
    /**
     * Construct a tuple of degree $degree.
     */
    static <${TN(degree)}> Tuple$degree<${TN(degree)}> tuple(${TN_vn(degree)}) {
        return new Tuple$degree<>(${vn(degree)});
    }
""").mkString}
    /**
     * Create a new range.
     */
    static <T extends Comparable<T>> Range<T> range(T t1, T t2) {
        return new Range<>(t1, t2);
    }

    /**
     * Get an array representation of this tuple.
     */
    Object[] array();

    /**
     * Get a list representation of this tuple.
     */
    List<?> list();

    /**
     * The degree of this tuple.
     */
    int degree();
}
"""
    )

    for (degree <- 1 to max) {
      write(
        s"src/main/java/org/jooq/lambda/tuple/Tuple$degree.java",
        s"""$copyright
package org.jooq.lambda.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
${if (degree == 2) "import java.util.Optional;" else ""}

${if (degree != 1) "import org.jooq.lambda.function.Function1;" else ""}
import org.jooq.lambda.function.Function$degree;

/**
 * A tuple of degree $degree.
 *
 * @author Lukas Eder
 */
public class Tuple$degree<${TN(degree)}> implements Tuple, Comparable<Tuple$degree<${TN(degree)}>>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    ${(for (d <- (1 to degree)) yield s"""
    public final T$d v$d;""").mkString}
    ${(for (d <- (1 to degree)) yield s"""
    public T$d v$d() {
        return v$d;
    }
    """).mkString}
    public Tuple$degree(Tuple$degree<${TN(degree)}> tuple) {${(for (d <- (1 to degree)) yield s"""
        this.v$d = tuple.v$d;""").mkString}
    }

    public Tuple$degree(${TN_vn(degree)}) {${(for (d <- (1 to degree)) yield s"""
        this.v$d = v$d;""").mkString}
    }
    ${if (degree == 2) s"""
    /**
     * Get a tuple with the two attributes swapped.
     */
    public final Tuple2<T2, T1> swap() {
        return new Tuple2<>(v2, v1);
    }

    /**
     * Whether two tuples overlap.
     * <p>
     * <code><pre>
     * // true
     * range(1, 3).overlaps(range(2, 4))
     *
     * // false
     * range(1, 3).overlaps(range(5, 8))
     * </pre></code>
     */
    public static final <T extends Comparable<T>> boolean overlaps(Tuple2<T, T> left, Tuple2<T, T> right) {
        return left.v1.compareTo(right.v2) <= 0
            && left.v2.compareTo(right.v1) >= 0;
    }

    /**
     * The intersection of two ranges.
     * <p>
     * <code><pre>
     * // (2, 3)
     * range(1, 3).intersect(range(2, 4))
     *
     * // none
     * range(1, 3).intersect(range(5, 8))
     * </pre></code>
     */
    public static final <T extends Comparable<T>> Optional<Tuple2<T, T>> intersect(Tuple2<T, T> left, Tuple2<T, T> right) {
        if (overlaps(left, right))
            return Optional.of(new Tuple2<>(
                left.v1.compareTo(right.v1) >= 0 ? left.v1 : right.v1,
                left.v2.compareTo(right.v2) <= 0 ? left.v2 : right.v2
            ));
        else
            return Optional.empty();
    }
    """ else ""}
    /**
     * Apply this tuple as arguments to a function.
     */
    public final <R> R map(Function$degree<${TN(degree)}, R> function) {
        return function.apply(this);
    }
    ${(for (d <- 1 to degree) yield s"""
    /**
     * Apply attribute $d as argument to a function and return a new tuple with the substituted argument.
     */
    public final <U$d> Tuple$degree<${TN(1, d - 1)}${if (d > 1) ", " else ""}U$d${if (d < degree) ", " else ""}${TN(d + 1, degree)}> map$d(Function1<T$d, U$d> function) {
        return Tuple.tuple(${vn(1, d - 1)}${if (d > 1) ", " else ""}function.apply(v$d)${if (d < degree) ", " else ""}${vn(d + 1, degree)});
    }
    """).mkString}
    @Override
    public final Object[] array() {
        return new Object[] { ${(for (d <- 1 to degree) yield s"v$d").mkString(", ")} };
    }

    @Override
    public final List<?> list() {
        return Arrays.asList(array());
    }

    /**
     * The degree of this tuple: $degree.
     */
    @Override
    public final int degree() {
        return $degree;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @Override
    public int compareTo(Tuple$degree<${TN(degree)}> other) {
        int result;
        ${(for (d <- 1 to degree) yield s"""
        result = Tuples.compare(v$d, other.v$d); if (result != 0) return result;""").mkString}

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tuple$degree))
            return false;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple$degree<${TN(degree)}> that = (Tuple$degree) o;
        ${(for (d <- 1 to degree) yield s"""
        if (!Objects.equals(v$d, that.v$d)) return false;""").mkString}

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        ${(for (d <- 1 to degree) yield s"""
        result = prime * result + ((v$d == null) ? 0 : v$d.hashCode());""").mkString}

        return result;
    }

    @Override
    public String toString() {
        return "("${(for (d <- (1 to degree)) yield s"""
             + ${if (d > 1) """", " + """ else """       """}v$d""").mkString}
             + ")";
    }

    @Override
    public Tuple$degree<${TN(degree)}> clone() {
        return new Tuple$degree<>(this);
    }
}
"""
      )
    }

    for (degree <- 1 to max) {
      write(
        s"src/main/java/org/jooq/lambda/function/Function$degree.java",
        s"""$copyright
package org.jooq.lambda.function;

${if      (degree == 1) "import java.util.function.Function;"
  else if (degree == 2) "import java.util.function.BiFunction;"
  else ""}
import org.jooq.lambda.tuple.Tuple$degree;

/**
 * A function with $degree arguments
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface Function$degree<${TN(degree)}, R> ${if (degree == 1) "extends Function<T1, R> " else if (degree == 2) "extends BiFunction<T1, T2, R> " else ""}{

    /**
     * Apply this function to the arguments.
     */
    default R apply(Tuple$degree<${TN(degree)}> args) {
        return apply(${xxxn(degree, "args.v")});
    }

    /**
     * Apply this function to the arguments.
     */
    ${if (degree <= 2) "@Override\n    " else ""}R apply(${TN_vn(degree)});
    ${if (degree == 1) s"""
    /**
     * Convert this function to a {@link java.util.function.Function}
     */
    default Function<T1, R> toFunction() {
        return this::apply;
    }

    /**
     * Convert to this function from a {@link java.util.function.Function}
     */
    static <T1, R> Function1<T1, R> from(Function<T1, R> function) {
        return function::apply;
    }
    """ else if (degree == 2) s"""
    /**
     * Convert this function to a {@link java.util.function.BiFunction}
     */
    default BiFunction<T1, T2, R> toBiFunction() {
        return this::apply;
    }

    /**
     * Convert to this function to a {@link java.util.function.BiFunction}
     */
    static <T1, T2, R> Function2<T1, T2, R> from(BiFunction<T1, T2, R> function) {
        return function::apply;
    }
    """
        else ""}
}
"""
      )
    }
  }

  def write(file : String, text : String) = {
    println("Writing " + file)
    val w = new PrintWriter(new File(file))

    w.print(text)
    w.flush
    w.close
  }

  def TN   (degree : Int                         ) : String = xxxn(degree, "T")
  def TN   (from   : Int, to  : Int              ) : String = xxxn(from, to, "T")
  def vn   (degree : Int                         ) : String = xxxn(degree, "v")
  def vn   (from   : Int, to  : Int              ) : String = xxxn(from, to, "v")
  def xxxn (degree : Int           , xxx : String) : String = xxxn(1, degree, xxx)
  def xxxn (from   : Int, to  : Int, xxx : String) : String = (from to to).map(i => xxx + i).mkString(", ")
  def TN_vn(degree : Int                         ) : String = (1 to degree).map(i => "T" + i + " v" + i).mkString(", ")
}