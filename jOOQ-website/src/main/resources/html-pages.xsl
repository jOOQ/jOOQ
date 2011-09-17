<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>
	<xsl:param name="sectionID"/>
	<xsl:param name="relativePath"/>

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
function printH1() {
    print "</xsl:text>
	<xsl:value-of select="//section[@id = $sectionID]/title"/>
<xsl:text disable-output-escaping="yes">";
}
function getSlogan() {
	return "</xsl:text>
	<xsl:value-of select="//section[@id = $sectionID]/slogan"/>
<xsl:text disable-output-escaping="yes">";
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
	
	<xsl:template match="//section[@id = $sectionID]" mode="content">
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
		
		
		<xsl:apply-templates select="content"/>
		
		<xsl:if test="count(sections/section) &gt; 0">
			<h3>Table of contents</h3>
		</xsl:if>
		<xsl:apply-templates select="." mode="toc"/>	
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
			<a href="{$prevhref}" title="{$prev/title}">previous</a>
		</xsl:if>
		
		<xsl:if test="$prev and $next">
			<xsl:text> : </xsl:text>
		</xsl:if>
		
		<xsl:if test="$next">
			<a href="{$nexthref}" title="{$next/title}">next</a>
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

								<xsl:if test="not(//section[@id = $id])">
									<xsl:message>
										<xsl:text>Reference not found: </xsl:text>
										<xsl:value-of select="$id"/>
									</xsl:message>
								</xsl:if>
							</xsl:when>
							
							<xsl:when test="@class and starts-with(@class, 'org.jooq')">
								<xsl:text>https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.java</xsl:text>
							</xsl:when>
							
							<xsl:when test="@class and starts-with(@class, 'java')">
								<xsl:text>http://download.oracle.com/javase/6/docs/api/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.html</xsl:text>
							</xsl:when>
							
							<xsl:otherwise>
								<xsl:message>
									<xsl:text>Reference not supported</xsl:text>
								</xsl:message>
							</xsl:otherwise>
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
					</xsl:choose>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
		            <xsl:apply-templates select="@*|node()" mode="content"/>
		        </xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>
</xsl:stylesheet>