/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.util.vertabelo;

import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * The Vertabelo API Database
 *
 * @author Michał Kołodziejski
 */
public class VertabeloAPIDatabase extends BaseVertabeloDatabase {

	private static final JooqLogger log = JooqLogger.getLogger(VertabeloAPIDatabase.class);

	protected static final String API_TOKEN_PARAM = "api-token";
	protected static final String MODEL_ID_PARAM = "model-id";
	protected static final String TAG_NAME_PARAM = "tag-name";

	protected String apiToken;
	protected String modelId;
	protected String tagName;

	public VertabeloAPIDatabase() {
		
	}
	
	@Override
	protected void readXML() {
		readSettings();
		
		VertabeloAPIClient client = new VertabeloAPIClient(apiToken);
		
		String xml = client.getXML(modelId, tagName);
		setVertabeloXML(xml);

	}


	private void readSettings() {

		apiToken = properties.getProperty(API_TOKEN_PARAM);
		if (StringUtils.isEmpty(apiToken)) {
			throw new IllegalStateException("Lack of \"" + API_TOKEN_PARAM + "\" parameter.");
		}

		modelId = properties.getProperty(MODEL_ID_PARAM);
		if (StringUtils.isEmpty(modelId)) {
			throw new IllegalStateException("Lack of \"" + MODEL_ID_PARAM + "\" parameter.");
		}

		tagName = properties.getProperty(TAG_NAME_PARAM);
	}

}
