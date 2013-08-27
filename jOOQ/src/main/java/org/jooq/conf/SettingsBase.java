/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.conf;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import javax.xml.bind.JAXB;

/**
 * This base class is extended by all XJC-generated {@link Settings} classes
 * <p>
 * Using such a base class seems to be a lot simpler than depending on any one
 * of those many JAXB / XJC plugins. Besides, cloning objects through the
 * standard Java {@link Cloneable} mechanism is around factor 1000x faster than
 * using {@link Serializable}, and even 10000x faster than using
 * {@link JAXB#marshal(Object, java.io.OutputStream)}, marshalling a JAXB object
 * into a {@link ByteArrayOutputStream}.
 *
 * @author Lukas Eder
 */
abstract class SettingsBase implements Serializable, Cloneable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 958655542175990197L;

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
