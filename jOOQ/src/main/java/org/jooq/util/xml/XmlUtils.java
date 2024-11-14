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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
package org.jooq.util.xml;

import org.jooq.impl.QOM;
import org.jooq.util.xml.jaxb.ForeignKeyRule;

import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * Internal utilities for this package.
 *
 * @author Lukas Eder
 */
@Internal
public final class XmlUtils {

    /**
     * Convert from {@link ForeignKeyRule} (JAXB class) to
     * {@link org.jooq.impl.QOM.ForeignKeyRule} (model class).
     */
    public static final QOM.ForeignKeyRule foreignKeyRule(ForeignKeyRule rule) {
        if (rule == null)
            return null;

        switch (rule) {
            case CASCADE:
                return QOM.ForeignKeyRule.CASCADE;
            case NO_ACTION:
                return QOM.ForeignKeyRule.NO_ACTION;
            case RESTRICT:
                return QOM.ForeignKeyRule.RESTRICT;
            case SET_DEFAULT:
                return QOM.ForeignKeyRule.SET_DEFAULT;
            case SET_NULL:
                return QOM.ForeignKeyRule.SET_NULL;
            default:
                throw new IllegalArgumentException("Unsupported rule: " + rule);
        }
    }

    /**
     * Convert from {@link org.jooq.impl.QOM.ForeignKeyRule} (model class) to
     * {@link ForeignKeyRule} (JAXB class).
     */
    public static final ForeignKeyRule foreignKeyRule(QOM.ForeignKeyRule rule) {
        if (rule == null)
            return null;

        switch (rule) {
            case CASCADE:
                return ForeignKeyRule.CASCADE;
            case NO_ACTION:
                return ForeignKeyRule.NO_ACTION;
            case RESTRICT:
                return ForeignKeyRule.RESTRICT;
            case SET_DEFAULT:
                return ForeignKeyRule.SET_DEFAULT;
            case SET_NULL:
                return ForeignKeyRule.SET_NULL;
            default:
                throw new IllegalArgumentException("Unsupported rule: " + rule);
        }
    }
}
