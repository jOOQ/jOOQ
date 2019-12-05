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
package org.jooq.meta;

/**
 * @author Lukas Eder
 */
public class DefaultSequenceDefinition
    extends AbstractTypedElementDefinition<SchemaDefinition>
    implements SequenceDefinition {

    private Number startWith;
    private Number incrementBy;
    private Number minValue;
    private Number maxValue;
    private boolean cycle;
    private Number cache;

    public DefaultSequenceDefinition(SchemaDefinition schema, String name, DataTypeDefinition type) {
        this(schema, name, type, null);
    }

    public DefaultSequenceDefinition(SchemaDefinition schema, String name, DataTypeDefinition type, String comment) {
        this(schema, name, type, comment, null, null, null, null, false, null);
    }

    public DefaultSequenceDefinition(SchemaDefinition schema, String name, DataTypeDefinition type, String comment, Number startWith, Number incrementBy, Number minValue, Number maxValue, boolean cycle, Number cache) {
        super(schema, name, -1, type, comment);

        this.startWith = startWith;
        this.incrementBy = incrementBy;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.cycle = cycle;
        this.cache = cache;
    }

    @Override
    public Number getStartWith() {
        return startWith;
    }

    @Override
    public Number getIncrementBy() {
        return incrementBy;
    }

    @Override
    public Number getMinvalue() {
        return minValue;
    }

    @Override
    public Number getMaxvalue() {
        return maxValue;
    }

    @Override
    public boolean getCycle() {
        return cycle;
    }

    @Override
    public Number getCache() {
        return cache;
    }
}
