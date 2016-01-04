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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXB;

import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.util.vertabelo.jaxb.DatabaseModel;

/**
 * The Vertabelo API Database
 *
 * @author Michał Kołodziejski
 */
public class VertabeloAPIDatabase extends VertabeloXMLDatabase {

    private static final JooqLogger log = JooqLogger.getLogger(VertabeloAPIDatabase.class);

    protected static final String API_URL_PREFIX = "https://my.vertabelo.com/api/xml/";

    // codegen properties
    protected static final String API_TOKEN_PARAM = "api-token";
    protected static final String MODEL_ID_PARAM = "model-id";
    protected static final String TAG_NAME_PARAM = "tag-name";

    protected String apiToken;
    protected String modelId;
    protected String tagName;


    @Override
    protected DatabaseModel databaseModel() {
        if(databaseModel == null) {
            readSettings();
            String xml = getXMLFromAPI();

            ByteArrayInputStream stream;
            try {
                stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            databaseModel = JAXB.unmarshal(stream, DatabaseModel.class);
        }

        return databaseModel;
    }


    protected void readSettings() {
        apiToken = getProperties().getProperty(API_TOKEN_PARAM);
        if (StringUtils.isEmpty(apiToken)) {
            throw new IllegalStateException("Lack of \"" + API_TOKEN_PARAM + "\" parameter.");
        }


        modelId = getProperties().getProperty(MODEL_ID_PARAM);
        if (StringUtils.isEmpty(modelId)) {
            throw new IllegalStateException("Lack of \"" + MODEL_ID_PARAM + "\" parameter.");
        }

        tagName = getProperties().getProperty(TAG_NAME_PARAM);
    }


    protected String getXMLFromAPI() {
        String xml = null;

        String apiUrl = getApiUrl();

        try {
            log.info("Creating connection to Vertabelo server...");

            URL url = new URL(apiUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // authorization data
            String encodedAuthData = DatatypeConverter.printBase64Binary((apiToken + ":").getBytes());
            connection.addRequestProperty("Authorization", "Basic " + encodedAuthData);


            // do request
            int responseCode=connection.getResponseCode();
            log.info("Response code: " + responseCode);

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new RuntimeException("Request failed with status code: " + responseCode);
            }

            // read response
            String response = "";
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null) {
                response += line;
            }

            xml = response;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return xml;
    }


    protected String getApiUrl() {
        String apiUrl = API_URL_PREFIX + modelId;
        if(!StringUtils.isEmpty(tagName)) {
            apiUrl += "/" + tagName;
        }

        return apiUrl;
    }
}
