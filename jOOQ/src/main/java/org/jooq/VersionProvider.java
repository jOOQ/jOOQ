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

import java.util.Set;

/**
 * An SPI that allows for providing a graph of versions.
 *
 * @author Lukas Eder
 */
@Internal // TODO This SPI is still being worked on and might change incompatibly
public interface VersionProvider {

    /**
     * Provide a set of versions relevant to a migration.
     * <p>
     * This can include the entire set of known versions, or a subset thereof.
     * There is no requirement to provide a fully connected graph, although
     * {@link Version#migrateTo(Version)} and other operations are undefined
     * if two versions do not have a common ancestor.
     */
    @Internal // TODO Produce a better type than Set<Version>. Possibly, #current() can be obtained from the new result type.
    Set<Version> provide();
}
