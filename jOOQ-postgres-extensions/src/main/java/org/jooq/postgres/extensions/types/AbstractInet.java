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
package org.jooq.postgres.extensions.types;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;


/**
 * A data type representing the PostgreSQL <code>inet</code> or
 * <code>cidr</code> type.
 *
 * @author Lukas Eder
 */
public abstract class AbstractInet implements Serializable {

    private final InetAddress address;
    private final Integer     prefix;

    AbstractInet(InetAddress address, Integer prefix) {
        this.address = address;
        this.prefix = prefix;
    }

    @NotNull
    public final InetAddress address() {
        return address;
    }

    public /* non-final */ Integer prefix() {
        return prefix;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof AbstractInet)
            return address.equals(((AbstractInet) obj).address) && Objects.equals(prefix, ((AbstractInet) obj).prefix);
        return false;
    }

    @Override
    public String toString() {
        return prefix == null ? address.getHostAddress() : address.getHostAddress() + "/" + prefix;
    }
}
