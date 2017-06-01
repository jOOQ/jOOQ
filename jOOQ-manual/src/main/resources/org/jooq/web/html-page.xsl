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
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:str="xalan://java.lang.String">

	<xsl:import href="src/main/resources/org/jooq/web/html-util.xsl"/>

	<xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>

	<xsl:variable name="apos">&apos;</xsl:variable>
	<xsl:param name="relativePath"/>
	<xsl:param name="root"/>
    <xsl:param name="minorVersion"/>

	<!-- Main match -->

	<xsl:template match="/">
        <xsl:apply-templates select="/manuals/manual[@version = $minorVersion]"/>
    </xsl:template>

    <xsl:template match="/manuals/manual[@version = $minorVersion]">
		<xsl:text disable-output-escaping="yes">
&lt;?php
// The following content has been XSL transformed from manual.xml using html-page.xsl
// Please do not edit this content manually
require '</xsl:text><xsl:value-of select="$relativePath"/><xsl:text disable-output-escaping="yes">frame.php';
function getH1() {
    return "The jOOQ User Manual. Single Page";
}
function getActiveMenu() {
    return "learn";
}
function printTheme() {
    noTheme();
}
function printContent() {
    global $root;
?&gt;
</xsl:text>

        <div id="manual">

        <!-- Version Jumpers -->
        <div class="row col col-100 col-red">
            <p>
                <xsl:text>All versions: </xsl:text>

                <xsl:apply-templates select="/manuals/manual[(@end-of-life = 'false' or not(@end-of-life)) and (@development = 'false' or not(@development))]" mode="version-links">
                    <xsl:sort select="str:replaceAll(string(@version), '^(\d)\.(\d)$', '$1.0$2')" order="descending"/>
                </xsl:apply-templates>

                <xsl:text> | Development versions: </xsl:text>

                <xsl:apply-templates select="/manuals/manual[@development = 'true']" mode="version-links">
                    <xsl:sort select="str:replaceAll(string(@version), '^(\d)\.(\d)$', '$1.0$2')" order="descending"/>
                </xsl:apply-templates>

                <xsl:text> | Unsupported versions: </xsl:text>

                <xsl:apply-templates select="/manuals/manual[@end-of-life = 'true']" mode="version-links">
                    <xsl:sort select="str:replaceAll(string(@version), '^(\d)\.(\d)$', '$1.0$2')" order="descending"/>
                </xsl:apply-templates>
            </p>
        </div>

		<!-- Display the main section's content -->
        <div class="row col col-100 col-white">
		<xsl:apply-templates select="/manuals/manual[@version = $minorVersion]/section/content"/>
        </div>

		<!-- Display the overall table of contents -->
        <div class="row col col-100 col-white">
		<h2 id="toc"><a href="#toc" name="toc">Table of contents</a></h2>
		<xsl:apply-templates select="/manuals/manual[@version = $minorVersion]/section" mode="toc"/>
        </div>

		<xsl:for-each select="/manuals/manual[@version = $minorVersion]/section//section">
            <xsl:variable name="id" select="@id"/>

            <div class="section">
                <div class="row col col-100 col-white">
                    <a id="{@id}" name="{@id}" class="manual-single-page-anchor"> </a>
                    <xsl:for-each select="/manuals/manual[@version = $minorVersion]/manual[@version = $minorVersion]//redirect[@redirect-to = $id]">
                        <a id="{@id}" name="{@id}" class="manual-single-page-anchor"/>
                    </xsl:for-each>

        			<h2>
        				<a href="#{@id}">
            				<xsl:apply-templates select="." mode="chapter-number"/>
            				<xsl:text> </xsl:text>
            				<xsl:value-of select="title"/>
                        </a>
        			</h2>
                </div>

    			<xsl:apply-templates select="content" />
            </div>
		</xsl:for-each>

        </div>
		<xsl:text disable-output-escaping="yes">
&lt;?php
}
?&gt;
</xsl:text>
	</xsl:template>

	<!-- matching templates -->

	<xsl:template match="/manuals/manual[@version = $minorVersion]//section" mode="content">
		<xsl:value-of select="@id"/>
		<br/>
	</xsl:template>

	<xsl:template match="section" mode="href">
		<xsl:text>#</xsl:text>
		<xsl:value-of select="@id"/>
	</xsl:template>

    <xsl:template match="manual" mode="version-links">
        <xsl:variable name="position" select="position()"/>

        <xsl:if test="$position > 1">
            <xsl:text> | </xsl:text>
        </xsl:if>

        <xsl:apply-templates select="." mode="version-link"/>
    </xsl:template>

    <xsl:template match="manual" mode="version-link">
        <xsl:choose>
            <xsl:when test="@version != $minorVersion">
                <a href="/doc/{@version}/manual-single-page">
                    <xsl:value-of select="@version"/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <strong style="font-size: 2em">
                    <xsl:value-of select="@version"/>
                </strong>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>