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

	<xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>

	<xsl:template match="html-only" mode="content">
		<xsl:apply-templates mode="content"/>
	</xsl:template>

	<xsl:template match="section" mode="toc">
		<xsl:if test="count(sections/section) &gt; 0">
			<ol>
				<xsl:for-each select="sections/section">
					<li>
						<xsl:variable name="href">
							<xsl:apply-templates select="." mode="href"/>
						</xsl:variable>

						<a href="{$href}" title="{title}">
							<xsl:value-of select="title"/>
						</a>

						<xsl:apply-templates select="." mode="toc"/>
					</li>
				</xsl:for-each>
			</ol>
		</xsl:if>
	</xsl:template>

	<xsl:template match="content">
		<xsl:apply-templates select="@*|node()" mode="content"/>
	</xsl:template>

	<xsl:template match="@*|node()" mode="content">
		<xsl:choose>
			<xsl:when test="name(.) = 'reference'">
				<xsl:variable name="id" select="@id"/>

				<a>
					<xsl:attribute name="href">
						<xsl:choose>
							<xsl:when test="@id">
								<xsl:apply-templates select="//section[@id = $id]" mode="href"/>
								<xsl:value-of select="@anchor"/>

								<xsl:if test="not(//section[@id = $id])">
									<xsl:message>
										<xsl:text>Reference not found: </xsl:text>
										<xsl:value-of select="$id"/>
									</xsl:message>
								</xsl:if>
							</xsl:when>

							<xsl:when test="@class and starts-with(@class, 'org.jooq.test')">
								<xsl:text>https://github.com/jOOQ/jOOQ/blob/master/jOOQ-test/src/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.java</xsl:text>
								<xsl:value-of select="@anchor"/>
							</xsl:when>

							<xsl:when test="@class and starts-with(@class, 'org.jooq.debug')">
								<xsl:text>https://github.com/jOOQ/jOOQ/blob/master/jOOQ-console/src/main/java/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.java</xsl:text>
								<xsl:value-of select="@anchor"/>
							</xsl:when>

							<xsl:when test="@class and starts-with(@class, 'org.jooq')">
								<xsl:text>http://www.jooq.org/javadoc/latest/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.html</xsl:text>
								<xsl:value-of select="@anchor"/>
							</xsl:when>

							<xsl:when test="@class and (starts-with(@class, 'javax.persistence'))">
								<xsl:text>http://docs.oracle.com/javaee/6/api/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.html</xsl:text>
								<xsl:value-of select="@anchor"/>
							</xsl:when>

							<xsl:when test="@class and (starts-with(@class, 'java') or starts-with(@class, 'org.w3c.dom'))">
								<xsl:text>http://download.oracle.com/javase/6/docs/api/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.html</xsl:text>
								<xsl:value-of select="@anchor"/>
							</xsl:when>

							<xsl:when test="@ticket">
								<xsl:text>https://github.com/jOOQ/jOOQ/issues/</xsl:text>
								<xsl:value-of select="@ticket"/>
								<xsl:value-of select="@anchor"/>
							</xsl:when>

							<xsl:otherwise>
								<xsl:message>
									<xsl:text>Reference not supported</xsl:text>
								</xsl:message>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>

					<xsl:attribute name="title">
						<xsl:choose>
							<xsl:when test="@id">
								<xsl:text>jOOQ Manual reference: </xsl:text>
								<xsl:value-of select="//section[@id = $id]/title"/>
							</xsl:when>
							<xsl:when test="@class and starts-with(@class, 'org.jooq')">
								<xsl:text>Internal API reference: </xsl:text>
								<xsl:value-of select="@class"/>
							</xsl:when>
							<xsl:when test="@class and starts-with(@class, 'java')">
								<xsl:text>External API reference: </xsl:text>
								<xsl:value-of select="@class"/>
							</xsl:when>
							<xsl:when test="@ticket">
								<xsl:text>GitHub issue: #</xsl:text>
								<xsl:value-of select="@ticket"/>
							</xsl:when>
						</xsl:choose>
					</xsl:attribute>

					<xsl:choose>
						<xsl:when test="@title">
							<xsl:value-of select="@title"/>
						</xsl:when>
						<xsl:when test="@id">
							<xsl:value-of select="//section[@id = $id]/title"/>
						</xsl:when>
						<xsl:when test="@class">
							<xsl:value-of select="@class"/>
						</xsl:when>
						<xsl:when test="@ticket">
							<xsl:text>#</xsl:text>
							<xsl:value-of select="@ticket"/>
						</xsl:when>
					</xsl:choose>
				</a>
			</xsl:when>
			<xsl:when test="name(.) = 'java'">
				<pre class="prettyprint lang-java">
					<xsl:value-of select="text()"/>
				</pre>
			</xsl:when>
			<xsl:when test="name(.) = 'sql'">
				<pre class="prettyprint lang-sql">
					<xsl:value-of select="text()"/>
				</pre>
			</xsl:when>
			<xsl:when test="name(.) = 'xml'">
				<pre class="prettyprint lang-xml">
					<xsl:value-of select="text()"/>
				</pre>
			</xsl:when>
			<xsl:when test="name(.) = 'config'">
				<pre class="prettyprint">
					<xsl:value-of select="text()"/>
				</pre>
			</xsl:when>
			<xsl:when test="name(.) = 'text'">
				<pre>
					<xsl:value-of select="text()"/>
				</pre>
			</xsl:when>
			<xsl:when test="name(.) = 'code-pair'">
				<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td width="50%" class="left">
						<xsl:apply-templates select="./*[position() = 1]" mode="content"/>
					</td>
					<td width="50%" class="right">
						<xsl:apply-templates select="./*[position() = 2]" mode="content"/>
					</td>
				</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
		            <xsl:apply-templates select="@*|node()" mode="content"/>
		        </xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>
</xsl:stylesheet>