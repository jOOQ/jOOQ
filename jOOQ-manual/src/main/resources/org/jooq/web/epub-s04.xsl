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

    <xsl:template match="/">
        <xsl:apply-templates select="/manuals/manual[@version = $minorVersion]"/>
    </xsl:template>

    <xsl:template match="/manuals/manual[@version = $minorVersion]">
		<xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html&gt;</xsl:text>
		<!-- I hate namespaces -->
		<xsl:text disable-output-escaping='yes'>&lt;html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" xml:lang="en" lang="en"&gt;</xsl:text>
            <head>
                <title>The jOOQ User Manual</title>
		        <link href="css/epub.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
                <xsl:apply-templates select="section" mode="section"/>
            </body>
        <xsl:text disable-output-escaping='yes'>&lt;/html&gt;</xsl:text>
    </xsl:template>

    <xsl:template match="section" mode="section">
        <section id="{@id}">
            <h3>
                <xsl:value-of select="title"/>
            </h3>

            <xsl:apply-templates select="content"/>

            <xsl:if test="sections/section">
                <xsl:apply-templates select="sections/section" mode="section"/>
            </xsl:if>
        </section>
    </xsl:template>

    <!-- matching templates -->
    <xsl:template match="section" mode="href">
        <xsl:text>#</xsl:text>
        <xsl:value-of select="@id"/>
    </xsl:template>

    <xsl:template match="tabs" mode="content">
        <xsl:apply-templates select="tab" mode="content"/>
    </xsl:template>

    <xsl:template match="tab">
        <p>
            <strong><xsl:value-of select="@title"/></strong>
        </p>

        <xsl:apply-templates select="node()" mode="content"/>
    </xsl:template>

    <xsl:template match="grammar" mode="content">
        <p>
            The grammar is currently not supported in the EPUB version of the manual. Please visit the HTML documentation to see the grammar.
        </p>
    </xsl:template>

    <!-- Overridden attributes in EPUB -->
    <xsl:template match="img/@src" mode="content">
        <xsl:attribute name="src">
            <xsl:text>images/</xsl:text>
	        <xsl:call-template name="basename">
	            <xsl:with-param name="path" select="."/>
	        </xsl:call-template>
        </xsl:attribute>
    </xsl:template>

    <!-- Invalid attributes in EPUB -->
    <xsl:template match="a/@name" mode="content"/>
    <xsl:template match="img/@align" mode="content"/>
</xsl:stylesheet>