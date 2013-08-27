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
package org.jooq.impl;

import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.DataType;

/**
 * A <code>DataType</code> used for converted types using {@link Converter}
 *
 * @author Lukas Eder
 */
class ConvertedDataType<T, U> extends DefaultDataType<U> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -2321926692580974126L;

    private final DataType<T>             delegate;
    private final Converter<? super T, U> converter;

    ConvertedDataType(DataType<T> delegate, Converter<? super T, U> converter) {
        super(null, converter.toType(), delegate.getTypeName(), delegate.getCastTypeName());

        this.delegate = delegate;
        this.converter = converter;

        DataTypes.registerConverter(converter.toType(), converter);
    }

    @Override
    public int getSQLType() {
        return delegate.getSQLType();
    }

    @Override
    public String getTypeName(Configuration configuration) {
        return delegate.getTypeName(configuration);
    }

    @Override
    public String getCastTypeName(Configuration configuration) {
        return delegate.getCastTypeName(configuration);
    }

    @SuppressWarnings("unchecked")
    @Override
    public U convert(Object object) {
        return converter.from(delegate.convert(converter.to((U) object)));
    }
}
