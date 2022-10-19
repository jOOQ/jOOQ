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

import java.net.InetAddress;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A data type representing the PostgreSQL <code>cidr</code> type.
 *
 * @author Lukas Eder
 */
public final class Cidr extends AbstractInet {

    private Cidr(InetAddress address, Integer prefix) {
        super(address, prefix);
    }

    @Override
    @NotNull
    public final Integer prefix() {
        return super.prefix();
    }

    /**
     * Create a new {@link Cidr} instance.
     */
    @NotNull
    public static final Cidr valueOf(InetAddress address, Integer prefix) {
        return new Cidr(address, prefix);
    }

    /**
     * Create a new {@link Cidr} instance.
     * <p>
     * This is the same as {@link #valueOf(InetAddress, Integer)}, but it can be
     * static imported.
     */
    @NotNull
    public static final Cidr cidr(InetAddress address, Integer prefix) {
        return new Cidr(address, prefix);
    }

    /**
     * Create a new {@link Cidr} instance, or <code>null</code> if the input
     * address is <code>null</code>.
     */
    @Nullable
    public static final Cidr cidrOrNull(InetAddress address, Integer prefix) {
        return address == null ? null : new Cidr(address, prefix);
    }
}
