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

package org.jooq.util;

import static org.jooq.util.AbstractDatabase.fetchedSize;
import static org.jooq.util.AbstractDatabase.getDefinition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * A base implementation for element container definitions
 *
 * @author Lukas Eder
 */
public abstract class AbstractElementContainerDefinition<E extends TypedElementDefinition<?>>
extends AbstractDefinition {

    /**
     * Precision and scale for those dialects that don't formally provide that
     * information in a separate field
     */
    protected static final Pattern  PRECISION_SCALE = Pattern.compile("\\((\\d+)\\s*(?:,\\s*(\\d+))?\\)");
    private static final JooqLogger log             = JooqLogger.getLogger(AbstractElementContainerDefinition.class);

    private List<E>                 elements;
    private final PackageDefinition pkg;

    public AbstractElementContainerDefinition(SchemaDefinition schema, String name, String comment) {
        this(schema, null, name, comment);
    }

    public AbstractElementContainerDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, String comment) {
        super(schema.getDatabase(), schema, name, comment);

        this.pkg = pkg;
    }

    public final PackageDefinition getPackage() {
        return pkg;
    }

    @Override
    public final List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<Definition>();

        result.addAll(getSchema().getDefinitionPath());

        if (pkg != null)
            result.add(pkg);

        result.add(this);

        return result;
    }

    protected final List<E> getElements() {
        if (elements == null) {
            elements = new ArrayList<E>();

            try {
                Database db = getDatabase();
                List<E> e = getElements0();

                // [#5335] Warn if a table definition contains several identity columns
                if (this instanceof TableDefinition) {
                    boolean hasIdentity = false;

                    for (E c : e) {
                        boolean isIdentity = ((ColumnDefinition) c).isIdentity();

                        if (isIdentity) {
                            if (hasIdentity) {
                                log.warn("Multiple identities", "Table " + getOutputName() + " has multiple identity columns. Only the first one is considered.");
                                break;
                            }

                            hasIdentity = true;
                        }
                    }
                }

                // [#2603] Filter exclude / include also for table columns
                if (this instanceof TableDefinition && db.getIncludeExcludeColumns()) {
                    elements = db.filterExcludeInclude(e);
                    log.info("Columns fetched", fetchedSize(e, elements));
                }
                else {
                    elements = e;
                }
            }
            catch (Exception e) {
                log.error("Error while initialising type", e);
            }
        }

        return elements;
    }

    protected final E getElement(String name) {
        return getElement(name, false);
    }

    protected final E getElement(String name, boolean ignoreCase) {
        return getDefinition(getElements(), name, ignoreCase);
    }

    protected final E getElement(int index) {
        return getElements().get(index);
    }

    protected abstract List<E> getElements0() throws SQLException;

    protected Number parsePrecision(String typeName) {
        if (typeName.contains("(")) {
            Matcher m = PRECISION_SCALE.matcher(typeName);

            if (m.find()) {
                if (!StringUtils.isBlank(m.group(1))) {
                    return Integer.valueOf(m.group(1));
                }
            }
        }

        return 0;
    }

    protected Number parseScale(String typeName) {
        if (typeName.contains("(")) {
            Matcher m = PRECISION_SCALE.matcher(typeName);

            if (m.find()) {
                if (!StringUtils.isBlank(m.group(2))) {
                    return Integer.valueOf(m.group(2));
                }
            }
        }

        return 0;
    }

    protected String parseTypeName(String typeName) {
        return typeName.replace(" NOT NULL", "");
    }

    protected boolean parseNotNull(String typeName) {
        return typeName.toUpperCase().contains("NOT NULL");
    }
}
