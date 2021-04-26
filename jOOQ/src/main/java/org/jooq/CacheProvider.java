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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.jetbrains.annotations.Nullable;

/**
 * A provider for cache implementations to replace the default.
 *
 * @author Lukas Eder
 */
@FunctionalInterface
public interface CacheProvider {

    /**
     * Provide a thread safe map for the given {@link CacheType}.
     * <p>
     * Implementations must ensure that the resulting {@link Map} is thread
     * safe. Two examples of such thread safe maps are {@link ConcurrentMap} or
     * {@link Collections#synchronizedMap(Map)}.
     * <p>
     * A <code>null</code> cache effectively turns off caching for the key.
     */
    @Nullable
    Map<Object, Object> provide(CacheContext context);
}
