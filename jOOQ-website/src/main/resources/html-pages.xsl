<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>
	<xsl:param name="sectionID"/>
	<xsl:param name="relativePath"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">
&lt;?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '</xsl:text>
		<xsl:value-of select="$relativePath"/>
<xsl:text disable-output-escaping="yes">frame.php';
function printH1() {
    print '</xsl:text>
	<xsl:apply-templates select="//section[@id = $sectionID]" mode="title"/>
<xsl:text disable-output-escaping="yes">';
}
function printSlogan() {}
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

	<xsl:template match="//section[@id = $sectionID]" mode="title">
		<xsl:value-of select="title"/>
	</xsl:template>

	<xsl:template match="//section[@id = $sectionID]" mode="content">
		<p>
			<xsl:apply-templates select="." mode="breadcrumb"/>
		</p>
		
		
		<xsl:apply-templates select="content"/>
		<xsl:apply-templates select="." mode="toc"/>	
	</xsl:template>
	
	<xsl:template match="section" mode="breadcrumb">
		<xsl:param name="href"/>
		
		<xsl:if test="name(../..) = 'section'">
			<xsl:apply-templates select="../.." mode="breadcrumb">
				<xsl:with-param name="href">
					<xsl:text>../</xsl:text>
					<xsl:value-of select="$href"/>
				</xsl:with-param>
			</xsl:apply-templates>
			
			<xsl:text> : </xsl:text>
		</xsl:if>
		
		<a href="{$href}">
			<xsl:value-of select="title"/>
		</a>
	</xsl:template>
	
	<xsl:template match="section" mode="toc">
		<xsl:param name="path" select="@id"/>
		
		<xsl:if test="toc and count(sections/section) &gt; 0">
			<ol>
				<xsl:for-each select="sections/section">
					<li>
						<a href="&lt;?=$root?&gt;/{$path}/{@id}" title="{title}">
							<xsl:value-of select="title"/>
						</a>
						
						<xsl:apply-templates select="." mode="toc">
							<xsl:with-param name="path" select="concat($path, '/', @id)"/>
						</xsl:apply-templates>
					</li>
				</xsl:for-each>
			</ol>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="content">
		<xsl:copy-of select="*"/>
	</xsl:template>
</xsl:stylesheet>