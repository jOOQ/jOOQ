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

package org.jooq.meta;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.jooq.impl.QOM.GenerationMode;
import org.jooq.meta.jaxb.SyntheticDefaultType;
import org.jooq.meta.jaxb.SyntheticEnumType;
import org.jooq.meta.jaxb.SyntheticIdentityGenerationMode;
import org.jooq.meta.jaxb.SyntheticIdentityType;
import org.jooq.meta.jaxb.SyntheticReadonlyColumnType;
import org.jooq.tools.JooqLogger;

/**
 * A base implementation for column definitions.
 *
 * @author Lukas Eder
 */
public class DefaultColumnDefinition
extends
    AbstractTypedElementDefinition<TableDefinition>
implements
    ColumnDefinition
{

    private static final JooqLogger              log = JooqLogger.getLogger(DefaultColumnDefinition.class);
    private final int                            position;
    private final GenerationMode                 identity;
    private final String                         defaultValue;
    private final boolean                        hidden;
    private final boolean                        redacted;
    private final boolean                        readonly;
    private transient List<EmbeddableDefinition> replacedByEmbeddables;
    private boolean                              synthetic;

    /**
     * @deprecated - 3.21.0 - [#15952] - Use
     *             {@link #DefaultColumnDefinition(TableDefinition, String, int, DataTypeDefinition, GenerationMode, String)}
     *             instead.
     */
    @Deprecated
    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        boolean identity,
        String comment
    ) {
        this(table, name, position, type, identity, false, comment);
    }

    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        GenerationMode identity,
        String comment
    ) {
        this(table, name, position, type, identity, false, comment);
    }

    /**
     * @deprecated - 3.21.0 - [#15952] - Use
     *             {@link #DefaultColumnDefinition(TableDefinition, String, int, DataTypeDefinition, GenerationMode, boolean, String)}
     *             instead.
     */
    @Deprecated
    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        boolean identity,
        boolean readonly,
        String comment
    ) {
        this(table, name, position, type, identity, type.isHidden(), readonly, comment);
    }

    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        GenerationMode identity,
        boolean readonly,
        String comment
    ) {
        this(table, name, position, type, identity, type.isHidden(), readonly, comment);
    }

    /**
     * @deprecated - 3.21.0 - [#15952] - Use
     *             {@link #DefaultColumnDefinition(TableDefinition, String, int, DataTypeDefinition, GenerationMode, boolean, boolean, String)}
     *             instead.
     */
    @Deprecated
    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        boolean identity,
        boolean hidden,
        boolean readonly,
        String comment
    ) {
        this(table, name, position, type, identity, hidden, type.isRedacted(), readonly, comment);
    }

    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        GenerationMode identity,
        boolean hidden,
        boolean readonly,
        String comment
    ) {
        this(table, name, position, type, identity, hidden, type.isRedacted(), readonly, comment);
    }

    /**
     * @deprecated - 3.21.0 - [#15952] - Use
     *             {@link #DefaultColumnDefinition(TableDefinition, String, int, DataTypeDefinition, GenerationMode, boolean, boolean, boolean, String)}
     *             instead.
     */
    @Deprecated
    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        boolean identity,
        boolean hidden,
        boolean redacted,
        boolean readonly,
        String comment
    ) {
        this(table, name, position, type, identity ? GenerationMode.BY_DEFAULT : null, hidden, redacted, readonly, comment);
    }

    public DefaultColumnDefinition(
        TableDefinition table,
        String name,
        int position,
        DataTypeDefinition type,
        GenerationMode identity,
        boolean hidden,
        boolean redacted,
        boolean readonly,
        String comment
    ) {
        super(table, name, position, type, comment);

        GenerationMode si = isSyntheticIdentity(this);

        this.position = position;
        this.identity = identity != null
            ? identity
            : si != null
            ? si
            : null;
        this.defaultValue = getSyntheticDefault(this);
        this.hidden = hidden;
        this.redacted = redacted;
        this.readonly = readonly || isSyntheticReadonlyColumn(this, this.identity != null);

        // [#6222] Copy the column's identity flag to the data type definition
        if (type instanceof DefaultDataTypeDefinition dd) {
            dd.identity(this.identity);
            dd.hidden(this.hidden);
            dd.redacted(this.redacted);
            dd.readonly(this.readonly);
            dd.defaultValue(this.defaultValue);






        }
    }

    @SuppressWarnings("unused")
    private static GenerationMode isSyntheticIdentity(DefaultColumnDefinition column) {
        AbstractDatabase db = (AbstractDatabase) column.getDatabase();

        for (SyntheticIdentityType id : db.getConfiguredSyntheticIdentities()) {
            for (TableDefinition t : db.filter(singletonList(column.getContainer()), id.getTables())) {
                for (ColumnDefinition c : db.filter(singletonList(column), id.getFields())) {
                    log.info("Synthetic identity", column.getQualifiedName());
                    db.markUsed(id);

                    return id.getGenerationMode() == SyntheticIdentityGenerationMode.ALWAYS
                        ? GenerationMode.ALWAYS
                        : GenerationMode.BY_DEFAULT;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unused")
    private static String getSyntheticDefault(DefaultColumnDefinition column) {














        return column.getDefinedType().getDefaultValue();
    }

    private static boolean isSyntheticReadonlyColumn(DefaultColumnDefinition column, boolean identity) {

















        return false;
    }

    final DefaultColumnDefinition synthetic(boolean s) {
        this.synthetic = s;
        return this;
    }

    @Override
    public final boolean isSynthetic() {
        return synthetic;
    }

    @Override
    public final int getPosition() {
        return position;
    }

    @Override
    public final UniqueKeyDefinition getPrimaryKey() {
        return getDatabase().getRelations().getPrimaryKey(this);
    }

    @Override
    public final List<UniqueKeyDefinition> getUniqueKeys() {
        return getDatabase().getRelations().getUniqueKeys(this);
    }

    @Override
    public final List<UniqueKeyDefinition> getKeys() {
        return getDatabase().getRelations().getKeys(this);
    }

    @Override
    public final List<ForeignKeyDefinition> getForeignKeys() {
        return getDatabase().getRelations().getForeignKeys(this);
    }

    @Override
    public final List<IndexDefinition> getIndexes() {
        return getIndexes0(i -> true);
    }

    @Override
    public final List<IndexDefinition> getUniqueIndexes() {
        return getIndexes0(i -> i.isUnique());
    }

    private final List<IndexDefinition> getIndexes0(Predicate<? super IndexDefinition> test) {
        return getDatabase()
            .getIndexes(getContainer())
            .stream()
            .filter(test)
            .filter(i -> i.getColumns().contains(this))
            .collect(toList());
    }

    @Override
    public final GenerationMode getIdentityMode() {
        return identity;
    }

    @Override
    public final boolean isIdentity() {
        return identity != null;
    }

    @Override
    public final boolean isHidden() {
        return hidden;
    }

    @Override
    public final boolean isRedacted() {
        return redacted;
    }

    @Override
    public final boolean isReadonly() {
        return readonly;
    }

















}
