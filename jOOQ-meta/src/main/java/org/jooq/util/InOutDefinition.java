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

package org.jooq.util;

/**
 * The parameter type of a stored procedure
 *
 * @author Lukas Eder
 */
public enum InOutDefinition {

	/**
	 * An in parameter. Default if the in/out keyword is absent.
	 */
	IN,

	/**
	 * An out parameter
	 */
	OUT,

	/**
	 * An in/out parameter
	 */
	INOUT,

	/**
	 * A return value for a function
	 */
	RETURN;

	/**
	 * Convert a string into the corresponding {@link InOutDefinition} value.
	 *
	 * @param string IN, OUT, INOUT or <code>null</code>
	 * @return The in/out value
	 */
	public static final InOutDefinition getFromString(String string) {
		if (string == null) {
			return IN;
		}

		else if ("IN/OUT".equalsIgnoreCase(string)) {
		    return INOUT;
		}

		else {
			return InOutDefinition.valueOf(string.toUpperCase());
		}
	}
}
