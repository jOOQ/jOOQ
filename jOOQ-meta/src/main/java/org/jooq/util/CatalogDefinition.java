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

import java.util.Arrays;
import java.util.List;

import org.jooq.tools.StringUtils;

/**
 * The definition of a database catalog.
 *
 * @author Lukas Eder
 */
public class CatalogDefinition extends AbstractDefinition {

	public CatalogDefinition(Database database, String name, String comment) {
		super(database, null, name, comment);
	}

	@Override
    public final CatalogDefinition getCatalog() {
        return this;
    }

    public final List<SchemaDefinition> getSchemata() {
	    return getDatabase().getSchemata(this);
	}

	public final SchemaDefinition getSchema(String name) {
	    for (SchemaDefinition schema : getSchemata())
	        if (schema.getInputName().equals(name))
	            return schema;

	    return null;
	}

	@SuppressWarnings("deprecation")
    @Override
    public final String getOutputName() {
	    return getDatabase().getOutputCatalog(getInputName());
    }

    @Override
    public final List<Definition> getDefinitionPath() {
        return Arrays.<Definition>asList(this);
    }

    public boolean isDefaultCatalog() {
        return StringUtils.isBlank(getOutputName());
    }
}
