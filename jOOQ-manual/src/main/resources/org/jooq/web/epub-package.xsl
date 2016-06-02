<?xml version="1.0" encoding="UTF-8"?>
<!--
  * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
  * All rights reserved.
  *
  * This work is dual-licensed
  * - under the Apache Software License 2.0 (the "ASL")
  * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
  * ===========================================================================
  * You may choose which license applies to you:
  *
  * - If you're using this work with Open Source databases, you may choose
  *   either ASL or jOOQ License.
  * - If you're using this work with at least one commercial database, you must
  *   choose jOOQ License
  *
  * For more information, please visit http://www.jooq.org/licenses
  *
  * Apache Software License 2.0:
  * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
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
  * jOOQ License and Maintenance Agreement:
  * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  * Data Geekery grants the Customer the non-exclusive, timely limited and
  * non-transferable license to install and use the Software under the terms of
  * the jOOQ License and Maintenance Agreement.
  *
  * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
  * and Maintenance Agreement for more details: http://www.jooq.org/licensing
  -->
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:import href="src/main/resources/org/jooq/web/html-util.xsl"/>

    <xsl:output encoding="UTF-8" method="xml" indent="yes"/>

    <xsl:param name="minorVersion"/>
    <xsl:param name="date"/>

    <xsl:template match="/">
		<package xmlns="http://www.idpf.org/2007/opf" version="3.0" unique-identifier="id">
		    <metadata xmlns:dc="http://purl.org/dc/elements/1.1/">
		        <dc:identifier id="id">http://www.jooq.org/doc/<xsl:value-of select="$minorVersion"/>/manual-epub/jOOQ-manual-<xsl:value-of select="$minorVersion"/>.epub</dc:identifier>
		        <meta property="dcterms:modified">2015-05-11T13:37:00Z</meta>

		        <dc:title id="t1">jOOQ User Manual</dc:title>
		        <meta refines="#t1" property="title-type">main</meta>
		        <meta refines="#t1" property="display-seq">1</meta>

		        <dc:creator id="dg">Data Geekery GmbH</dc:creator>
		        <dc:language>en</dc:language>
		        <dc:date><xsl:value-of select="$date"/></dc:date>

		        <dc:subject>Children -- Books and reading</dc:subject>
		        <dc:subject>Children's literature -- Study and teaching</dc:subject>

		        <dc:source>http://www.jooq.org/learn</dc:source>
		        <dc:rights>All rights reserved</dc:rights>
		    </metadata>
		    <manifest>
		        <xsl:for-each select="/manuals/manual[@version = $minorVersion]//img">
		            <xsl:variable name="basename">
		                <xsl:call-template name="basename">
		                    <xsl:with-param name="path" select="@src"/>
		                </xsl:call-template>
		            </xsl:variable>

		            <item href="images/{$basename}" id="{$basename}" media-type="image/png"/>
		        </xsl:for-each>

                <item href="images/jooq-logo-black.png" id="jooq-logo-black.png" media-type="image/png"/>
		        <item href="css/epub.css" id="css01" media-type="text/css"/>
		        <item href="css/nav.css" id="css02" media-type="text/css"/>
		        <item href="cover.xhtml" id="cover" media-type="application/xhtml+xml"/>
		        <item href="s04.xhtml" id="s04" media-type="application/xhtml+xml"/>
		        <item href="nav.xhtml" id="nav" media-type="application/xhtml+xml" properties="nav"/>
		        <item href="toc.ncx" id="ncx" media-type="application/x-dtbncx+xml"/>
		    </manifest>
		    <spine toc="ncx">
		        <itemref idref="cover"/>
		        <itemref idref="nav"/>
		        <itemref idref="s04"/>
		    </spine>
		</package>
    </xsl:template>
</xsl:stylesheet>