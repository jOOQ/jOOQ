/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.postgres.extensions.converters;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jooq.exception.DataTypeException;
import org.jooq.impl.AbstractConverter;
import org.jooq.postgres.extensions.types.AbstractInet;

/**
 * A converter for the PostgreSQL <code>inet</code> or <code>cidr</code> data
 * type.
 *
 * @author Lukas Eder
 */
abstract class AbstractInetConverter<U extends AbstractInet> extends AbstractConverter<Object, U> {

    public AbstractInetConverter(Class<U> uType) {
        super(Object.class, uType);
    }

    abstract U construct(InetAddress address, Integer prefix);

    @Override
    public U from(Object t) {
        if (t == null)
            return null;

        String[] s = t.toString().split("/");
        try {
            InetAddress a = InetAddress.getByName(s[0]);

            if (s.length == 1)
                return construct(a, null);
            else
                return construct(a, Integer.valueOf(s[1]));
        }
        catch (UnknownHostException e) {
            throw new DataTypeException("Cannot parse InetAddress", e);
        }
    }

    @Override
    public Object to(AbstractInet u) {
        return u == null
             ? null
             : u.prefix() == null
             ? u.address().getHostAddress()
             : u.address().getHostAddress() + "/" + u.prefix();
    }
}
