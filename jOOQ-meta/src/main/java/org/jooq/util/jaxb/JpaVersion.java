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
package org.jooq.util.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p> Java class for JpaVersion
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="JpaVersion"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="V_2_0"/&gt;
 *     &lt;enumeration value="V_2_1"/&gt;
 *     &lt;enumeration value="V_2_2"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 * @author Timur Shaidullin
 */
@XmlType(name = "JpaVersion")
@XmlEnum
public enum JpaVersion {
    V_2_0(null),
    V_2_1(V_2_0, "Index"),
    V_2_2(V_2_1);

    private final List<String> annotations;

    JpaVersion(JpaVersion previous) {
        this(previous, new ArrayList<String>());
    }

    JpaVersion(JpaVersion previous, String... annotations) {
        this(previous, new ArrayList<String>(Arrays.asList(annotations)));
    }

    JpaVersion(JpaVersion previous, List<String> annotations) {
        this.annotations = annotations;

        if (previous != null)
            annotations.addAll(previous.annotations);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public boolean supportedIndex() {
        return annotations.contains("Index");
    }

    public String getName() {
        return name().substring(2).replace('_', '.');
    }

    public static JpaVersion latest() {
        return V_2_2;
    }
}
