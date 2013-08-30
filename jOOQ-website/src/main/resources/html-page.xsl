<?xml version="1.0" encoding="UTF-8"?>
<!-- 
  - Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
  - All rights reserved.
  -
  - This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
  - =============================================================================
  - You may choose which license applies to you:
  -
  - - If you're using this work with Open Source databases, you may choose
  -   ASL 2.0 or jOOQ EULA.
  - - If you're using this work with at least one commercial database, you may
  -   choose AGPL 3.0 or jOOQ EULA.
  -
  - For more information, please visit http://www.jooq.org/licenses
  -
  - Apache Software License 2.0:
  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -  http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -
  - AGPL 3.0
  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  - This library is free software; you can redistribute it and/or
  - modify it under the terms of the GNU Affero General Public
  - License as published by the Free Software Foundation; either
  - version 3.0 of the License, or (at your option) any later version.
  -
  - This library is distributed in the hope that it will be useful,
  - but WITHOUT ANY WARRANTY; without even the implied warranty of
  - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  - Lesser General Public License for more details.
  -
  - You should have received a copy of the GNU Affero General Public
  - License along with this library.
  - If not, see http://www.gnu.org/licenses.
  -
  - jOOQ End User License Agreement:
  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  - This library is commercial software; you may not redistribute it nor
  - modify it.
  -
  - This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
  - License Agreement for more details: http://www.jooq.org/eula
  -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="src/main/resources/html-util.xsl"/>

	<xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>

	<xsl:variable name="apos">&apos;</xsl:variable>
	<xsl:param name="relativePath"/>
	<xsl:param name="root"/>

	<!-- Main match -->

	<xsl:template match="/">
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
		<!-- Display the main section's content -->
        <div class="row col col-100 col-white">
		<xsl:apply-templates select="/manual/section/content"/>
        </div>

		<!-- Display the overall table of contents -->
        <div class="row col col-100 col-white">
		<h2 id="toc"><a href="#toc" name="toc">Table of contents</a></h2>
		<xsl:apply-templates select="/manual/section" mode="toc"/>
        </div>
        
		<xsl:for-each select="/manual/section//section">
            <xsl:variable name="id" select="@id"/>
            
            <section>
                <div class="row col col-100 col-white">
        			<h2 id="{@id}">
                        <xsl:for-each select="//redirect[@redirect-to = $id]">
                            <a id="{@id}" name="{@id}"/>
                        </xsl:for-each>
                        
        				<a name="{@id}" href="#{@id}">
            				<xsl:apply-templates select="." mode="chapter-number"/>
            				<xsl:text> </xsl:text>
            				<xsl:value-of select="title"/>
                        </a>
        			</h2>
                </div>
                    
    			<xsl:apply-templates select="content" />
            </section>
		</xsl:for-each>

        </div>
		<xsl:text disable-output-escaping="yes">
&lt;?php
}
?&gt;
</xsl:text>
	</xsl:template>

	<!-- matching templates -->

	<xsl:template match="//section" mode="content">
		<xsl:value-of select="@id"/>
		<br/>
	</xsl:template>

	<xsl:template match="section" mode="href">
		<xsl:text>#</xsl:text>
		<xsl:value-of select="@id"/>
	</xsl:template>
</xsl:stylesheet>