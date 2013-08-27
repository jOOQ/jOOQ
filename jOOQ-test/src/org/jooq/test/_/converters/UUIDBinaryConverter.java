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
package org.jooq.test._.converters;

import java.util.UUID;

import org.jooq.Converter;

public class UUIDBinaryConverter implements Converter<byte[], UUID> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4543995777404574519L;

    @Override
    public UUID from(byte[] data) {
        if (data == null)
            return null;

        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);

        return new UUID(msb, lsb);
    }

    @Override
    public byte[] to(UUID data) {
        if (data == null)
            return null;

        byte[] result = new byte[16];
        long msb = data.getMostSignificantBits();
        long lsb = data.getLeastSignificantBits();

        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (msb & 0xFF);
            msb >>= 8;
        }

        for (int i = 15; i >= 8; i--) {
            result[i] = (byte) (lsb & 0xFF);
            lsb >>= 8;
        }

        return result;
    }

    @Override
    public Class<byte[]> fromType() {
        return byte[].class;
    }

    @Override
    public Class<UUID> toType() {
        return UUID.class;
    }
}
