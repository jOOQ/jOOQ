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
 */
package org.jooq.impl;

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.impl.DSL.inline;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;

/**
 * Abstraction for various "containsIgnoreCase" operations
 *
 * @author lmarchau
 */
final class ContainsIgnoreCase<T> extends AbstractCondition {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1795491010886118843L;

	private static final Clause[] CLAUSES = {CONDITION, CONDITION_COMPARISON};

	private final Field<T> lhs;
	private final Field<T> rhs;
	private final T value;

	ContainsIgnoreCase(Field<T> field, T value) {
		this.lhs = field;
		this.rhs = null;
		this.value = value;
	}

	ContainsIgnoreCase(Field<T> field, Field<T> rhs) {
		this.lhs = field;
		this.rhs = rhs;
		this.value = null;
	}

	@Override
	public final void accept(Context<?> ctx) {
		ctx.visit(condition(ctx.configuration()));
	}

	@Override
	public final Clause[] clauses(Context<?> ctx) {
		return CLAUSES;
	}

	private final Condition condition(Configuration configuration) {

		Field<String> concat;

		if (rhs == null) {
			concat = DSL.concat(inline("%"), Tools.escapeForLike(value, configuration), inline("%"));
		} else {
			concat = DSL.concat(inline("%"), Tools.escapeForLike(rhs, configuration), inline("%"));
		}

		return lhs.likeIgnoreCase(concat, Tools.ESCAPE);
	}
}
