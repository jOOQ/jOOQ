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
package org.jooq.jackson.extensions.converters;

import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.AbstractConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * A base class for {@link JSON} or {@link JSONB} to Jackson POJO conversion.
 *
 * @author Lukas Eder
 */
abstract class AbstractToJacksonConverter<J, U> extends AbstractConverter<J, U> {

    final ObjectMapper mapper;

    public AbstractToJacksonConverter(Class<J> fromType, Class<U> toType) {
        super(fromType, toType);

        mapper = JsonMapper
            .builder()
            .findAndAddModules()
            .build();
    }

    abstract String data(J json);

    abstract J json(String string);

    @Override
    public U from(J databaseObject) {
        if (databaseObject == null)
            return null;

        try {
            return mapper.readValue(data(databaseObject), toType());
        }
        catch (JsonProcessingException e) {
            throw new DataTypeException("Error when converting JSON to " + toType(), e);
        }
    }

    @Override
    public J to(U userObject) {
        if (userObject == null)
            return null;

        try {
            return json(mapper.writeValueAsString(userObject));
        }
        catch (JsonProcessingException e) {
            throw new DataTypeException("Error when converting object of type " + toType() + " to JSON", e);
        }
    }
}
