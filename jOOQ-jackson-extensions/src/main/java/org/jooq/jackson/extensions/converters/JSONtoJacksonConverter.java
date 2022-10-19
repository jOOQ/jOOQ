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
package org.jooq.jackson.extensions.converters;

import org.jooq.JSON;


/**
 * A base class for {@link JSON} to Jackson POJO conversion.
 *
 * @author Lukas Eder
 */
public class JSONtoJacksonConverter<U> extends AbstractToJacksonConverter<JSON, U> {

    public JSONtoJacksonConverter(Class<U> toType) {
        super(JSON.class, toType);
    }

    @Override
    final String data(JSON json) {
        return json.data();
    }

    @Override
    final JSON json(String string) {
        return JSON.json(string);
    }
}
