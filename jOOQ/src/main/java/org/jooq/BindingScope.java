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
 *
 *
 *
 */
package org.jooq;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * A {@link Scope} that lives in the context of a data type {@link Binding}
 * operation.
 * <h3>Known subtypes of various binding operations</h3>
 * <p>
 * The various {@link Binding} operations and their corresponding
 * {@link BindingScope} subtypes include:
 * <ul>
 * <li>{@link Binding#sql(BindingSQLContext)} with {@link BindingSQLContext} to
 * generate the SQL string for a bind value.</li>
 * <li>{@link Binding#set(BindingSetStatementContext)} with
 * {@link BindingSetStatementContext} to set the bind value to the JDBC
 * {@link PreparedStatement}.</li>
 * <li>{@link Binding#set(BindingSetSQLOutputContext)} with
 * {@link BindingSetSQLOutputContext} to write the bind value to a JDBC
 * {@link SQLOutput} (used for {@link UDT}).</li>
 * <li>{@link Binding#register(BindingRegisterContext)} with
 * {@link BindingRegisterContext} to register a procedural <code>OUT</code>
 * parameter on a JDBC {@link CallableStatement}.</li>
 * <li>{@link Binding#get(BindingGetResultSetContext)} with
 * {@link BindingGetResultSetContext} to read a result value from a JDBC
 * {@link ResultSet}.</li>
 * <li>{@link Binding#get(BindingGetSQLInputContext)} with
 * {@link BindingGetSQLInputContext} to read a result value from a JDBC
 * {@link SQLInput} (used for {@link UDT}).</li>
 * <li>{@link Binding#get(BindingGetStatementContext)} with
 * {@link BindingGetStatementContext} to read a procedural <code>OUT</code>
 * argument from a JDBC {@link CallableStatement}.</li>
 * </ul>
 * <h3>Lifecycle</h3> The various {@link Binding} operations are very short
 * lived operations and thus this scope is also very short lived, although some
 * implementations (e.g. {@link BindingGetResultSetContext}) may be cached for
 * the duration of an {@link ExecuteScope} for performance reasons.
 * <p>
 * The {@link BindingScope#data()} map is that of the parent scope, which is:
 * <ul>
 * <li>The {@link Context} for {@link BindingSQLContext}</li>
 * <li>The {@link ExecuteScope} for all the others</li>
 * </ul>
 * <p>
 * This allows for interacting with the rendering lifecycle or the execution
 * lifecycle from within a custom data type {@link Binding}.
 * <h3>Interactions with other {@link Scope} types</h3>
 * <p>
 * Most but not all {@link BindingScope} types are also {@link ExecuteScope}.
 * Specifically, the {@link BindingSQLContext} type isn't an
 * {@link ExecuteScope} because it can also be used in non-execution use-cases,
 * such as {@link DSLContext#render(QueryPart)}, where a bind value needs to
 * render its SQL to the output SQL string, without the SQL string ever being
 * executed.
 *
 * @author Lukas Eder
 */
public interface BindingScope extends Scope {

}
