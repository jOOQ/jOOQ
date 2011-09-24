<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>
	
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
								<xsl:text>https://github.com/lukaseder/jOOQ/blob/master/jOOQ-test/src/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.java</xsl:text>
								<xsl:value-of select="@anchor"/>
							</xsl:when>
							
							<xsl:when test="@class and starts-with(@class, 'org.jooq')">
								<xsl:text>https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.java</xsl:text>
								<xsl:value-of select="@anchor"/>
							</xsl:when>
							
							<xsl:when test="@class and starts-with(@class, 'java')">
								<xsl:text>http://download.oracle.com/javase/6/docs/api/</xsl:text>
								<xsl:value-of select="translate(@class, '.', '/')"/>
								<xsl:text>.html</xsl:text>
								<xsl:value-of select="@anchor"/>
							</xsl:when>
							
							<xsl:when test="@ticket">
								<xsl:text>https://sourceforge.net/apps/trac/jooq/ticket/</xsl:text>
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
								<xsl:text>Trac ticket: #</xsl:text>
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
						<xsl:apply-templates select="sql" mode="content"/>
					</td>
					<td width="50%" class="right">
						<xsl:apply-templates select="java" mode="content"/>
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