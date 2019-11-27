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

    private Long startWith;
    private Long incrementBy;
    private Long minValue;
    private Long maxValue;
    private boolean cycle;
    private Long cache;

    public DefaultSequenceDefinition(SchemaDefinition schema, String name, DataTypeDefinition type) {
        this(schema, name, type, null);
    }

    public DefaultSequenceDefinition(SchemaDefinition schema, String name, DataTypeDefinition type, String comment) {
        this(schema, name, type, comment, null, null, null, null, false, null);
    }

    public DefaultSequenceDefinition(SchemaDefinition schema, String name, DataTypeDefinition type, String comment, Long startWith, Long incrementBy, Long minValue, Long maxValue, boolean cycle, Long cache) {
        super(schema, name, -1, type, comment);

        this.startWith = startWith;
        this.incrementBy = incrementBy;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.cycle = cycle;
        this.cache = cache;
    }

    @Override
    public Long getStartWith() {
        return startWith;
    }

    @Override
    public Long getIncrementBy() {
        return incrementBy;
    }

    @Override
    public Long getMinValue() {
        return minValue;
    }

    @Override
    public Long getMaxValue() {
        return maxValue;
    }

    @Override
    public boolean getCycle() {
        return cycle;
    }

    @Override
    public Long getCache() {
        return cache;
    }
}
