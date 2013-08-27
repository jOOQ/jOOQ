/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;

import org.xml.sax.InputSource;

/**
 * The <code>Loader</code> API is used for configuring data loads.
 * <p>
 * The step in constructing the {@link Loader} object where you can specify the
 * load type and data source.
 *
 * @author Lukas Eder
 */
public interface LoaderSourceStep<R extends TableRecord<R>> {

    /**
     * Load CSV data
     */
    @Support
    LoaderCSVStep<R> loadCSV(File file) throws FileNotFoundException;

    /**
     * Load CSV data
     */
    @Support
    LoaderCSVStep<R> loadCSV(String data);

    /**
     * Load CSV data
     */
    @Support
    LoaderCSVStep<R> loadCSV(InputStream stream);

    /**
     * Load CSV data
     */
    @Support
    LoaderCSVStep<R> loadCSV(Reader reader);

    /**
     * Load XML data
     */
    @Support
    LoaderXMLStep<R> loadXML(File file) throws FileNotFoundException;

    /**
     * Load XML data
     */
    @Support
    LoaderXMLStep<R> loadXML(String data);

    /**
     * Load XML data
     */
    @Support
    LoaderXMLStep<R> loadXML(InputStream stream);

    /**
     * Load XML data
     */
    @Support
    LoaderXMLStep<R> loadXML(Reader reader);

    /**
     * Load XML data
     */
    @Support
    LoaderXMLStep<R> loadXML(InputSource source);
}
