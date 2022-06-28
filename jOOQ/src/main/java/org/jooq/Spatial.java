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
package org.jooq;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

/**
 * A wrapper type for spatial data obtained from the database.
 * <p>
 * The wrapper represents spatial {@link #data()} in serialised string form
 * either as a well known text (WKT) or well known binary in hex format (WKB),
 * depending on your dialect's default behaviour. A
 * <code>CAST(NULL AS …)</code> value is represented by a
 * <code>null</code> reference of type {@link Spatial}, not as
 * <code>data() == null</code>. This is consistent with jOOQ's general way of
 * returning <code>NULL</code> from {@link Result} and {@link Record} methods.
 * <p>
 * This data type is supported only by the commercial editions of jOOQ.
 */
public interface Spatial extends Serializable {

    @NotNull
    String data();
}

