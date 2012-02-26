<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="xml" indent="yes"/>

	<xsl:variable name="apos">&apos;</xsl:variable>
	<xsl:variable name="page-width">210</xsl:variable>
	<xsl:variable name="page-margin">17</xsl:variable>
	<xsl:variable name="content-width" select="$page-width - (2 * $page-margin)"/>

	<xsl:attribute-set name="static">
		<xsl:attribute name="font-size">7pt</xsl:attribute>
		<xsl:attribute name="color">#555555</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="div">
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="pre">
		<xsl:attribute name="font-family">Courier</xsl:attribute>
		<xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
		<xsl:attribute name="white-space-collapse">false</xsl:attribute>
		<xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
		<xsl:attribute name="font-size">6pt</xsl:attribute>
		<xsl:attribute name="margin">12pt</xsl:attribute>
		<xsl:attribute name="padding">4pt</xsl:attribute>
		<xsl:attribute name="border">2px solid #882222</xsl:attribute>
		<xsl:attribute name="background-color">#FFEEDD</xsl:attribute>
		<xsl:attribute name="page-break-inside">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="strong">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="h1">
		<xsl:attribute name="font-family">Georgia</xsl:attribute>
		<xsl:attribute name="font-size">18pt</xsl:attribute>
		<xsl:attribute name="padding-top">12pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">12pt</xsl:attribute>
		<xsl:attribute name="page-break-after">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="h2">
		<xsl:attribute name="font-family">Georgia</xsl:attribute>
		<xsl:attribute name="font-size">16pt</xsl:attribute>
		<xsl:attribute name="padding-top">10pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">10pt</xsl:attribute>
		<xsl:attribute name="page-break-after">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="h3">
		<xsl:attribute name="font-family">Georgia</xsl:attribute>
		<xsl:attribute name="font-size">16pt</xsl:attribute>
		<xsl:attribute name="padding-top">10pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">10pt</xsl:attribute>
		<xsl:attribute name="page-break-after">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="a">
		<xsl:attribute name="font-size">11pt</xsl:attribute>
		<xsl:attribute name="color">#882222</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="text-shadow">0 1px 2px #666666</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="p">
		<xsl:attribute name="font-size">11pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="ul">
		<xsl:attribute name="padding-top">16pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">16pt</xsl:attribute>
		<xsl:attribute name="page-break-inside">avoid</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="ol-toc">
		<xsl:attribute name="margin-bottom">6pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="ol">
		<xsl:attribute name="padding-top">16pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">16pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="li-toc">
		<xsl:attribute name="font-size">11pt</xsl:attribute>
		<xsl:attribute name="page-break-inside">avoid</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="li">
		<xsl:attribute name="font-size">11pt</xsl:attribute>
		<xsl:attribute name="page-break-inside">avoid</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="img">
		<xsl:attribute name="padding-top">10pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">10pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- Main match -->

	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
			    <fo:simple-page-master 
			        page-height="297mm" 
			        page-width="{$page-width}mm"
					master-name="simple">
					
         			<fo:region-body 
						region-name="xsl-region-body"  
            			margin="{$page-margin}mm" />
					<fo:region-before
            			region-name="xsl-region-before" 
            			extent="{$page-margin}mm" />
         			<fo:region-after 
						region-name="xsl-region-after"
          				extent="{$page-margin}mm" />
      			</fo:simple-page-master>
   			</fo:layout-master-set>
   			
   			<fo:page-sequence master-reference="simple">
   				<fo:flow flow-name="xsl-region-body" font-family="Helvetica">
   					<fo:block width="100%" margin-left="20mm" margin-right="20mm" font-family="Georgia">
	   					<fo:block width="100%" text-align="center" font-size="36pt" padding-top="60mm">
	   						<xsl:text>The jOOQ User Manual</xsl:text>
	   					</fo:block>
	   					<fo:block width="100%" text-align="justify" font-size="12pt" color="#555555" padding-top="20mm">
	   						<xsl:text>SQL was never meant to be abstracted. To be confined in the narrow 
	   						boundaries of heavy mappers, hiding the beauty and simplicity of relational data. 
	   						SQL was never meant to be object-oriented. SQL was never meant to be anything 
	   						other than... SQL!</xsl:text>
	   					</fo:block>
	   					<fo:block width="100%" text-align="center" padding-top="20mm">
	   						<fo:external-graphic 
	   							src="url('C:/Users/lukas/workspace/jOOQ-website/img/logo.png')"
								xsl:use-attribute-sets="img"/>
	   					</fo:block>
   					</fo:block>
   				</fo:flow>
   			</fo:page-sequence>
   			
   			<fo:page-sequence master-reference="simple">
   				<fo:static-content flow-name="xsl-region-before" font-family="Helvetica">
   					<fo:block>
	   					<fo:table table-layout="fixed" width="100%">
							<fo:table-column column-width="proportional-column-width(1)"/>
							<fo:table-column column-width="150mm"/>
						
							<fo:table-body>
							  	<fo:table-row>
							    	<fo:table-cell padding-left="{$page-margin}mm" padding-top="{$page-margin * 2 div 3}mm">
							      		<fo:block xsl:use-attribute-sets="static">
							      			<xsl:text>
							      			The jOOQ User Manual
							      			</xsl:text>
							      		</fo:block>
							    	</fo:table-cell>
							    	<fo:table-cell padding-right="{$page-margin}mm" padding-top="{$page-margin * 2 div 3}mm" text-align="right">
							     		<fo:block xsl:use-attribute-sets="static">
							     			<fo:retrieve-marker retrieve-class-name="section"/> 
							     		</fo:block>
							    	</fo:table-cell>
							  	</fo:table-row>
							</fo:table-body>
						</fo:table>
   					</fo:block>
   				</fo:static-content>
   				
   				<fo:static-content flow-name="xsl-region-after" font-family="Helvetica">
   					<fo:block>
			      		<fo:table table-layout="fixed" width="100%">
							<fo:table-column column-width="150mm"/>
							<fo:table-column column-width="proportional-column-width(1)"/>
						
							<fo:table-body>
							  	<fo:table-row>
							    	<fo:table-cell padding-left="{$page-margin}mm">
							      		<fo:block xsl:use-attribute-sets="static">
							      			<xsl:text>
							      			jOOQ is brought to you by Lukas Eder. Distributed under the Apache 2 licence
							      			</xsl:text>
							      		</fo:block>
							    	</fo:table-cell>
							    	<fo:table-cell padding-right="{$page-margin}mm" text-align="right">
							     		<fo:block xsl:use-attribute-sets="static">
							     			<xsl:text>Page&#160;</xsl:text>
							     			<fo:page-number/>
							     			<xsl:text>&#160;/&#160;</xsl:text>
							     			<fo:page-number-citation ref-id="last-page"/>
							     		</fo:block>
							    	</fo:table-cell>
							  	</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
			    </fo:static-content>
			    
      			<fo:flow flow-name="xsl-region-body" font-family="Helvetica">
					<!-- Display the main section's content -->
					<xsl:apply-templates select="/manual/section/content"/>
					
					<!-- Display the overall table of contents -->
					<fo:block break-after='page'/>
					<fo:block xsl:use-attribute-sets="h3">Table of contents</fo:block>
					<xsl:apply-templates select="/manual/section" mode="toc"/>

					<xsl:for-each select="/manual/section//section">
						<!-- Break before top-level chapters -->
						<xsl:variable name="id" select="@id"/>
						<xsl:if test="/manual/section/sections/section[@id = $id]">
							<fo:block break-after='page'/>
						</xsl:if>
						
						<fo:block xsl:use-attribute-sets="h1" id="{@id}">
							<fo:marker marker-class-name="section">
								<fo:block>
									<xsl:apply-templates select="." mode="chapter-number"/>
									<xsl:text> </xsl:text>
									<xsl:value-of select="title"/>
								</fo:block>
							</fo:marker>
							
							<xsl:apply-templates select="." mode="chapter-number"/>
							<xsl:text> </xsl:text>
							<xsl:value-of select="title"/>
						</fo:block>
						<fo:block xsl:use-attribute-sets="p">
							<xsl:value-of select="slogan"/>
						</fo:block>
						
						<xsl:apply-templates select="content" />
					</xsl:for-each>
					
					<fo:block id="last-page"/>
      			</fo:flow>
  			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<xsl:template match="html-only" mode="content"/>
	
	<xsl:template match="content">
		<xsl:apply-templates select="@*|node()" mode="content"/>
	</xsl:template>
	
	<xsl:template match="div" mode="content">
		<fo:block xsl:use-attribute-sets="div">
			<xsl:apply-templates mode="content"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="h1" mode="content">
		<fo:block xsl:use-attribute-sets="h1">
			<xsl:apply-templates mode="content"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="h2" mode="content">
		<fo:block xsl:use-attribute-sets="h2">
			<xsl:apply-templates mode="content"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="h3" mode="content">
		<fo:block xsl:use-attribute-sets="h3">
			<xsl:apply-templates mode="content"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="a" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a">
			<xsl:attribute name="external-destination">
				<xsl:text>url('</xsl:text>
				<xsl:value-of select="@href"/>
				<xsl:text>')</xsl:text>
			</xsl:attribute>
			
			<xsl:apply-templates mode="content"/>
		</fo:basic-link>
	</xsl:template>
	
	<xsl:template match="reference[@id]" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a" internal-destination="{@id}">
			<xsl:apply-templates select="." mode="reference-content"/>
		</fo:basic-link>
	</xsl:template>
	
	<xsl:template match="reference[@class and starts-with(@class, 'org.jooq.test')]" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a">
			<xsl:attribute name="external-destination">
				<xsl:text>url('</xsl:text>
				<xsl:text>https://github.com/lukaseder/jOOQ/blob/master/jOOQ-test/src/</xsl:text>
				<xsl:value-of select="translate(@class, '.', '/')"/>
				<xsl:text>.java</xsl:text>
				<xsl:value-of select="@anchor"/>
				<xsl:text>')</xsl:text>
			</xsl:attribute>
			
			<xsl:apply-templates select="." mode="reference-content"/>
		</fo:basic-link>
	</xsl:template>
	
	<xsl:template match="reference[@class and starts-with(@class, 'org.jooq')]" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a">
			<xsl:attribute name="external-destination">
				<xsl:text>url('</xsl:text>
				<xsl:text>https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/</xsl:text>
				<xsl:value-of select="translate(@class, '.', '/')"/>
				<xsl:text>.java</xsl:text>
				<xsl:value-of select="@anchor"/>
				<xsl:text>')</xsl:text>
			</xsl:attribute>
			
			<xsl:apply-templates select="." mode="reference-content"/>
		</fo:basic-link>
	</xsl:template>
	
	<xsl:template match="reference[@class and starts-with(@class, 'java')]" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a">
			<xsl:attribute name="external-destination">
				<xsl:text>url('</xsl:text>
				<xsl:text>http://download.oracle.com/javase/6/docs/api/</xsl:text>
				<xsl:value-of select="translate(@class, '.', '/')"/>
				<xsl:text>.html</xsl:text>
				<xsl:value-of select="@anchor"/>
				<xsl:text>')</xsl:text>
			</xsl:attribute>
			
			<xsl:apply-templates select="." mode="reference-content"/>
		</fo:basic-link>
	</xsl:template>
	
	<xsl:template match="reference[@ticket]" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a">
			<xsl:attribute name="external-destination">
				<xsl:text>url('</xsl:text>
				<xsl:text>https://sourceforge.net/apps/trac/jooq/ticket/</xsl:text>
				<xsl:value-of select="@ticket"/>
				<xsl:value-of select="@anchor"/>
				<xsl:text>')</xsl:text>
			</xsl:attribute>
			
			<xsl:apply-templates select="." mode="reference-content"/>
		</fo:basic-link>
	</xsl:template>
	
	<xsl:template match="reference" mode="reference-content">
		<xsl:variable name="id" select="@id"/>
		
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
	</xsl:template>
	
	<xsl:template match="img" mode="content">
		<fo:block text-align="center">
			<fo:external-graphic 
				src="url('C:/Users/lukas/workspace/jOOQ-website/img/{substring-after(@src, 'img/')}')"
				xsl:use-attribute-sets="img"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="p" mode="content">
		<fo:block xsl:use-attribute-sets="p">
			<xsl:apply-templates mode="content"/>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="ul" mode="content">
		<fo:block xsl:use-attribute-sets="ul">
			<fo:list-block>
				<xsl:apply-templates mode="content" />
			</fo:list-block>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="ol" mode="content">
		<fo:block xsl:use-attribute-sets="ol">
			<fo:list-block>
				<xsl:apply-templates mode="content" />
			</fo:list-block>
		</fo:block>
	</xsl:template>

	<xsl:template match="li" mode="content">
		<fo:list-item>
			<fo:list-item-label>
				<fo:block xsl:use-attribute-sets="li">
					<xsl:choose>
						<xsl:when test="count(ancestor::ul)=1">-</xsl:when>
						<xsl:when test="count(ancestor::ul)=2">*</xsl:when>
						<xsl:otherwise>o</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block xsl:use-attribute-sets="li">
					<xsl:apply-templates mode="content" />
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>
	
	<xsl:template match="br" mode="content">
		<fo:block/>
	</xsl:template>
	
	<xsl:template match="strong" mode="content">
		<fo:inline xsl:use-attribute-sets="strong">
			<xsl:apply-templates mode="content" />
		</fo:inline>
	</xsl:template>
	
	<xsl:template match="code-pair" mode="content">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="{$content-width div 2}mm"/>
			<fo:table-column column-width="{$content-width div 2}mm"/>
		
			<fo:table-body>
			  	<fo:table-row>
			    	<fo:table-cell>
			      		<fo:block>
			      			<xsl:apply-templates select="sql" mode="content" />
			      		</fo:block>
			    	</fo:table-cell>
			    	<fo:table-cell>
			     		<fo:block>
			     			<xsl:apply-templates select="java" mode="content" />
			     		</fo:block>
			    	</fo:table-cell>
			  	</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<xsl:template match="java | sql | xml | text | config" mode="content">
		<fo:block xsl:use-attribute-sets="pre">
			<xsl:apply-templates mode="content" />
        </fo:block>
	</xsl:template>
	
	<xsl:template match="@*|node()" mode="content">
		<xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="content"/>
        </xsl:copy>
    </xsl:template>
    
   	<xsl:template match="section" mode="toc">
		<xsl:if test="count(sections/section) &gt; 0">
			<fo:block xsl:use-attribute-sets="ol-toc">
				<fo:list-block>
					<xsl:for-each select="sections/section">
						<fo:list-item>
			          		<fo:list-item-label>
			          			<fo:block xsl:use-attribute-sets="li-toc">
			            			<xsl:number format="1." />
		            			</fo:block>
			          		</fo:list-item-label>
			          		<fo:list-item-body start-indent="body-start()">
			          			<fo:block xsl:use-attribute-sets="li-toc">
			          				<fo:block text-align-last="justify">
				          				<fo:inline>
				          					<fo:basic-link internal-destination="{@id}">
						            			<xsl:value-of select="title" />
						            			<fo:leader leader-pattern="dots"/>
						            			<fo:page-number-citation ref-id="{@id}"/>
					            			</fo:basic-link>
				          				</fo:inline>
			          				</fo:block>
			            			<xsl:apply-templates select="." mode="toc"/>
			            		</fo:block>
			          		</fo:list-item-body>
			        	</fo:list-item>
					</xsl:for-each>
				</fo:list-block>
			</fo:block>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="section" mode="chapter-number">
		<xsl:if test="@id != 'manual'">
			<xsl:apply-templates select="../.." mode="chapter-number"/>
			
			<xsl:value-of select="count(preceding-sibling::section) + 1"/>
			<xsl:text>.</xsl:text>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>