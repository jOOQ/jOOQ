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
package org.jooq.postgres.extensions.types;

import java.net.InetAddress;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A data type representing the PostgreSQL <code>inet</code> type.
 *
 * @author Lukas Eder
 */
public final class Inet extends AbstractInet {

    private Inet(InetAddress address, Integer prefix) {
        super(address, prefix);
    }

    @Override
    @Nullable
    public final Integer prefix() {
        return super.prefix();
    }

    /**
     * Create a new {@link Inet} instance.
     */
    @NotNull
    public static final Inet valueOf(InetAddress address) {
        return new Inet(address, null);
    }

    /**
     * Create a new {@link Inet} instance.
     */
    @NotNull
    public static final Inet valueOf(InetAddress address, Integer prefix) {
        return new Inet(address, prefix);
    }

    /**
     * Create a new {@link Inet} instance.
     * <p>
     * This is the same as {@link #valueOf(InetAddress)}, but it can be static
     * imported.
     */
    @NotNull
    public static final Inet inet(InetAddress address) {
        return new Inet(address, null);
    }

    /**
     * Create a new {@link Inet} instance.
     * <p>
     * This is the same as {@link #valueOf(InetAddress, Integer)}, but it can be
     * static imported.
     */
    @NotNull
    public static final Inet inet(InetAddress address, Integer prefix) {
        return new Inet(address, prefix);
    }

    /**
     * Create a new {@link Inet} instance, or <code>null</code> if the input
     * address is <code>null</code>.
     */
    @Nullable
    public static final Inet inetOrNull(InetAddress address) {
        return address == null ? null : new Inet(address, null);
    }

    /**
     * Create a new {@link Inet} instance, or <code>null</code> if the input
     * address is <code>null</code>.
     */
    @Nullable
    public static final Inet inetOrNull(InetAddress address, Integer prefix) {
        return address == null ? null : new Inet(address, prefix);
    }
}
