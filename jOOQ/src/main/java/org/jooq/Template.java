/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
 * <p>
 *
 * @deprecated - This type is still very experimental and not yet released to a
 *             broad public. Do not use this type yet.
 * @author Lukas Eder
 */
@Deprecated
public interface Template {

    /**
     * Transform some input data into a {@link QueryPart}.
     */
    QueryPart transform(Object... input);
}
