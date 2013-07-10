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
package org.jooq;


/**
 * A functional interface to return custom {@link QueryPart} objects from input.
 * <p>
 * This interface is used for templating in jOOQ. Any type of
 * <code>QueryPart</code> can be created through templating mechanisms.
 * Depending on the template, some <code>input</code> data is needed - for
 * example an array or a collection of bind values.
 * <p>
 * Simple examples of templates are plain SQL construction templates, which are
 * used by jOOQ internally for all plain SQL factory methods. More sophisticated
 * examples include XSL transformation templates, MyBatis templates, etc.
 * <p>
 * An example using Apache Velocity is given here:
 * <p>
 * <code><pre>
 * class VelocityTemplate implements Template {
 *     private final String file;
 *
 *     public VelocityTemplate(String file) {
 *         this.file = file;
 *     }
 *
 *     public QueryPart transform(Object... input) {
 *         VelocityEngine ve = new VelocityEngine();
 *         ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
 *         ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, new File("authors-and-books.vm").getAbsolutePath());
 *         ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");
 *         ve.init();
 *
 *         VelocityContext context = new VelocityContext();
 *         context.put("p", input);
 *
 *         StringWriter writer = new StringWriter();
 *         ve.getTemplate(file, "UTF-8").merge(context, writer);
 *         return DSL.queryPart(writer.toString(), input);
 *     }
 * }
 * </pre></code>
 * <p>
 * And then:
 * <p>
 * <code><pre>
 * DSL.using(configuration)
 *    .resultQuery(new VelocityTemplate("authors-and-books.vm"), 1, 2, 3)
 *    .fetch();
 * </pre></code>
 * <p>
 * With the contents of authors-and-books.vm being
 * <p>
 * <code><pre>
 * SELECT
 *   a.first_name,
 *   a.last_name,
 *   count(*)
 * FROM
 *   t_author a
 * LEFT OUTER JOIN
 *   t_book b ON a.id = b.author_id
 * WHERE
 *   1 = 0
 * #foreach ($param in $p)
 *   OR a.id = ?
 * #end
 * GROUP BY
 *   a.first_name,
 *   a.last_name
 * ORDER BY
 *   a.id ASC
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface Template {

    /**
     * Transform some input data into a {@link QueryPart}.
     */
    QueryPart transform(Object... input);
}
