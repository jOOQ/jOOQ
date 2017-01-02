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
