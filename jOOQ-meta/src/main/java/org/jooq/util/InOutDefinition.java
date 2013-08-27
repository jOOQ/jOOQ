/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
