<?xml version="1.0" encoding="UTF-8"?>
<!-- 
  - Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
  - All rights reserved.
  -
  - This software is licensed to you under the Apache License, Version 2.0
  - (the "License"); You may obtain a copy of the License at
  -
  -   http://www.apache.org/licenses/LICENSE-2.0
  -
  - Redistribution and use in source and binary forms, with or without
  - modification, are permitted provided that the following conditions are met:
  -
  - . Redistributions of source code must retain the above copyright notice, this
  -   list of conditions and the following disclaimer.
  -
  - . Redistributions in binary form must reproduce the above copyright notice,
  -   this list of conditions and the following disclaimer in the documentation
  -   and/or other materials provided with the distribution.
  -
  - . Neither the name "jOOQ" nor the names of its contributors may be
  -   used to endorse or promote products derived from this software without
  -   specific prior written permission.
  -
  - THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  - AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  - IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
  - ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
  - LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
  - CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
  - SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
  - INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
  - CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
  - ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
  - POSSIBILITY OF SUCH DAMAGE.
  -->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="src/main/resources/html-util.xsl"/>

	<xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>
	<xsl:param name="sectionID"/>
	<xsl:param name="relativePath"/>
	<xsl:param name="root"/>

	<xsl:variable name="apos">&apos;</xsl:variable>

	<!-- Main match -->

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">
&lt;?php
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '</xsl:text>
		<xsl:value-of select="$relativePath"/>
<xsl:text disable-output-escaping="yes">frame.php';
function getH1() {
    return "</xsl:text>
	<xsl:value-of select="//section[@id = $sectionID]/title"/>
<xsl:text disable-output-escaping="yes">";
}
function getActiveMenu() {
	return "learn";
}
function printContent() {
    global $root;
?&gt;
</xsl:text>
		<xsl:apply-templates select="//section[@id = $sectionID]" mode="content"/>
		<xsl:text disable-output-escaping="yes">
&lt;?php
}
?&gt;
</xsl:text>
	</xsl:template>

	<!-- matching templates -->

    <xsl:template match="h3" mode="content">
    	<xsl:variable name="id" select="generate-id(.)"/>

        <h2 id="{$id}">
        	<a href="#{$id}" name="{$id}">#</a>
        	<xsl:text> </xsl:text>
        	<xsl:apply-templates mode="content"/>
        </h2>
    </xsl:template>

	<xsl:template match="//section[@id = $sectionID]" mode="content">
		<xsl:apply-templates select="." mode="navigation"/>
		<xsl:apply-templates select="content"/>

		<xsl:if test="count(sections/section) &gt; 0">
			<h2 id="toc"><a href="#toc" name="toc">#</a> Table of contents</h2>
		</xsl:if>
		<xsl:apply-templates select="." mode="toc"/>

		<br/>
		<xsl:apply-templates select="." mode="navigation"/>
	</xsl:template>

	<xsl:template match="section" mode="navigation">
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td align="left" valign="top">
					<xsl:apply-templates select="." mode="breadcrumb"/>
				</td>
				<td align="right" valign="top" style="white-space: nowrap">
					<xsl:apply-templates select="." mode="prev-next"/>
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="section" mode="breadcrumb">
		<xsl:if test="name(../..) = 'section'">
			<xsl:apply-templates select="../.." mode="breadcrumb"/>
			<xsl:text> : </xsl:text>
		</xsl:if>

		<xsl:variable name="href">
			<xsl:apply-templates select="." mode="href"/>
		</xsl:variable>

		<a href="{$href}">
			<xsl:value-of select="title"/>
		</a>
	</xsl:template>

	<xsl:template match="section" mode="href">
		<xsl:choose>
			<xsl:when test="name(../..) = 'section'">
				<xsl:apply-templates select="../.." mode="href"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>&lt;?=$root?&gt;/</xsl:text>
				<xsl:value-of select="$root"/>
			</xsl:otherwise>
		</xsl:choose>

		<xsl:value-of select="@id"/>
		<xsl:text>/</xsl:text>
	</xsl:template>

	<xsl:template match="section" mode="prev-next">
		<xsl:variable name="prev" select="(preceding::section | ancestor::section)[last()]"/>
		<xsl:variable name="prevhref">
			<xsl:apply-templates select="$prev" mode="href"/>
		</xsl:variable>

		<xsl:variable name="next" select="(following::section | descendant::section)[1]"/>
		<xsl:variable name="nexthref">
			<xsl:apply-templates select="$next" mode="href"/>
		</xsl:variable>

		<xsl:if test="$prev">
			<a href="{$prevhref}" title="Previous section: {$prev/title}">previous</a>
		</xsl:if>

		<xsl:if test="$prev and $next">
			<xsl:text> : </xsl:text>
		</xsl:if>

		<xsl:if test="$next">
			<a href="{$nexthref}" title="Next section: {$next/title}">next</a>
		</xsl:if>
	</xsl:template>

	<xsl:template match="section" mode="prev-id">
		<xsl:variable name="id" select="@id"/>

		<xsl:variable name="position">
			<xsl:for-each select="//section">
				<xsl:if test="@id = $id">
					<xsl:value-of select="position()"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>

		<xsl:for-each select="//section">
			<xsl:if test="position() = $position - 1">
				<xsl:value-of select="@id"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="section" mode="next-id">
		<xsl:variable name="id" select="@id"/>

		<xsl:variable name="position">
			<xsl:for-each select="//section">
				<xsl:if test="@id = $id">
					<xsl:value-of select="position()"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>

		<xsl:for-each select="//section">
			<xsl:if test="position() = $position + 1">
				<xsl:value-of select="@id"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>