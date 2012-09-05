<?xml version="1.0" encoding="UTF-8"?>
<!-- 
  - Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
function printContent() {
    global $root;
?&gt;
</xsl:text>

		<!-- Display the main section's content -->
		<xsl:apply-templates select="/manual/section/content"/>

		<!-- Display the overall table of contents -->
		<h2 id="toc"><a href="#toc" name="toc">#</a> Table of contents</h2>
		<xsl:apply-templates select="/manual/section" mode="toc"/>

		<xsl:for-each select="/manual/section//section">
			<h2 id="{@id}">
				<a name="{@id}" href="#{@id}">#</a>
				<xsl:text> </xsl:text>
				<xsl:apply-templates select="." mode="chapter-number"/>
				<xsl:text> </xsl:text>
				<xsl:value-of select="title"/>
			</h2>

			<xsl:apply-templates select="content" />
		</xsl:for-each>

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

	<xsl:template match="section" mode="chapter-number">
		<xsl:if test="@id != 'manual'">
			<xsl:apply-templates select="../.." mode="chapter-number"/>

			<xsl:value-of select="count(preceding-sibling::section) + 1"/>
			<xsl:text>.</xsl:text>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>