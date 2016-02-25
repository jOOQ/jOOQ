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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jooq.util.xml.XMLDatabase;

/**
 * The Vertabelo XML Database
 *
 * @author Michał Kołodziejski
 */
public class VertabeloXMLDatabase extends BaseVertabeloDatabase {


	protected void readXML() {

		String xml = "";
		String url = properties.getProperty(XMLDatabase.P_XML_FILE);
		try {
			try (BufferedReader fr = new BufferedReader(new FileReader(url))) {
				String line;
				while ((line = fr.readLine()) != null) {
					xml += line;
				}
				
			}
			this.setVertabeloXML(xml);
		} catch (IOException e) {
			throw new RuntimeException("Error while reading file: " + url, e);
		}
	}
}