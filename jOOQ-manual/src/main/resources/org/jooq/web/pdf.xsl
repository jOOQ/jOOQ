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
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="xml" indent="yes"/>

    <xsl:param name="minorVersion"/>
    <xsl:param name="currentYear"/>

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
		<xsl:attribute name="margin-top">15pt</xsl:attribute>
        <xsl:attribute name="margin-bottom">15pt</xsl:attribute>
        <xsl:attribute name="margin-left">3pt</xsl:attribute>
        <xsl:attribute name="margin-right">3pt</xsl:attribute>
		<xsl:attribute name="padding">4pt</xsl:attribute>
        <xsl:attribute name="color">#333333</xsl:attribute>
		<xsl:attribute name="background-color">#eeeeee</xsl:attribute>
		<xsl:attribute name="page-break-inside">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="strong">
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="h1">
		<xsl:attribute name="font-family">Roboto</xsl:attribute>
		<xsl:attribute name="font-size">22pt</xsl:attribute>
        <xsl:attribute name="font-weight">300</xsl:attribute>
		<xsl:attribute name="padding-top">24pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">24pt</xsl:attribute>
		<xsl:attribute name="page-break-after">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="h2">
		<xsl:attribute name="font-family">Roboto</xsl:attribute>
		<xsl:attribute name="font-size">16pt</xsl:attribute>
        <xsl:attribute name="font-weight">300</xsl:attribute>
		<xsl:attribute name="padding-top">15pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">15pt</xsl:attribute>
		<xsl:attribute name="page-break-after">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="h3">
		<xsl:attribute name="font-family">Roboto</xsl:attribute>
		<xsl:attribute name="font-size">15pt</xsl:attribute>
        <xsl:attribute name="font-weight">300</xsl:attribute>
		<xsl:attribute name="padding-top">15pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">15pt</xsl:attribute>
		<xsl:attribute name="page-break-after">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="a">
		<xsl:attribute name="font-size">11pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="text-decoration">underline</xsl:attribute>
        <xsl:attribute name="color">#3333ee</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="p">
		<xsl:attribute name="font-size">11pt</xsl:attribute>
		<xsl:attribute name="text-align">justify</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="ul">
		<xsl:attribute name="padding-top">16pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">16pt</xsl:attribute>
		<xsl:attribute name="page-break-inside">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="ol-toc">
		<xsl:attribute name="margin-bottom">3pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="ol">
		<xsl:attribute name="padding-top">16pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">16pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="li-toc">
		<xsl:attribute name="font-size">9pt</xsl:attribute>
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
        <xsl:apply-templates select="/manuals/manual[@version = $minorVersion]"/>
    </xsl:template>

    <xsl:template match="/manuals/manual[@version = $minorVersion]">
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

            <fo:bookmark-tree>
                <xsl:apply-templates select="/manuals/manual[@version = $minorVersion]/section/sections/section" mode="bookmark"/>
            </fo:bookmark-tree>

   			<fo:page-sequence master-reference="simple">
   				<fo:flow flow-name="xsl-region-body" font-family="Open Sans" font-weight="300">
   					<fo:block width="100%" margin-left="20mm" margin-right="20mm" font-family="Roboto" font-weight="300">
	   					<fo:block width="100%" text-align="center" font-size="36pt" padding-top="60mm">
	   						<xsl:text>The jOOQ™ User Manual</xsl:text>
	   					</fo:block>
	   					<fo:block width="100%" text-align="justify" font-size="12pt" color="#555555" padding-top="20mm">
	   						<xsl:text>SQL was never meant to be abstracted. To be confined in the narrow
	   						boundaries of heavy mappers, hiding the beauty and simplicity of relational data.
	   						SQL was never meant to be object-oriented. SQL was never meant to be anything
	   						other than... SQL!</xsl:text>
	   					</fo:block>
	   					<fo:block width="100%" text-align="center" padding-top="20mm">
	   						<fo:external-graphic
	   							src="url('jooq.org/img/jooq-logo-black.png')"
                                content-height="scale-down-to-fit"
                                content-width="scale-down-to-fit"
								xsl:use-attribute-sets="img"/>
	   					</fo:block>
   					</fo:block>
   				</fo:flow>
   			</fo:page-sequence>

   			<fo:page-sequence master-reference="simple">
   				<fo:static-content flow-name="xsl-region-before" font-family="Open Sans" font-weight="300">
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

   				<fo:static-content flow-name="xsl-region-after" font-family="Open Sans" font-weight="300">
   					<fo:block>
			      		<fo:table table-layout="fixed" width="100%">
							<fo:table-column column-width="150mm"/>
							<fo:table-column column-width="proportional-column-width(1)"/>

							<fo:table-body>
							  	<fo:table-row>
							    	<fo:table-cell padding-left="{$page-margin}mm">
							      		<fo:block xsl:use-attribute-sets="static">
							      			<xsl:text>
							      			    © 2009 -
							      			</xsl:text>
                                            <xsl:value-of select="$currentYear"/>
                                            <xsl:text>
                                                by Data Geekery™ GmbH.
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

      			<fo:flow flow-name="xsl-region-body" font-family="Open Sans" font-weight="300">
					<!-- Display the main section's content -->
					<xsl:apply-templates select="/manuals/manual[@version = $minorVersion]/section/content"/>

					<!-- Display the overall table of contents -->
					<fo:block break-after='page'/>
					<fo:block xsl:use-attribute-sets="h3">Table of contents</fo:block>
					<xsl:apply-templates select="/manuals/manual[@version = $minorVersion]/section" mode="toc"/>

					<xsl:for-each select="/manuals/manual[@version = $minorVersion]/section//section">
						<!-- Break before top-level chapters -->
						<xsl:variable name="id" select="@id"/>
						<xsl:if test="/manuals/manual[@version = $minorVersion]/section/sections/section[@id = $id]">
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

    <xsl:template match="html" mode="content">
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
				<xsl:text>https://github.com/jOOQ/jOOQ/blob/master/jOOQ-test/src/</xsl:text>
				<xsl:value-of select="translate(@class, '.', '/')"/>
				<xsl:text>.java</xsl:text>
				<xsl:value-of select="@anchor"/>
				<xsl:text>')</xsl:text>
			</xsl:attribute>

			<xsl:apply-templates select="." mode="reference-content"/>
		</fo:basic-link>
	</xsl:template>

	<xsl:template match="reference[@class and starts-with(@class, 'org.jooq.debug')]" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a">
			<xsl:attribute name="external-destination">
				<xsl:text>url('</xsl:text>
				<xsl:text>https://github.com/jOOQ/jOOQ/blob/master/jOOQ-console/src/main/java/</xsl:text>
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
                <xsl:text>http://www.jooq.org/javadoc/</xsl:text>
                <xsl:value-of select="$minorVersion"/>
                <xsl:text>.x/</xsl:text>
				<xsl:value-of select="translate(@class, '.', '/')"/>
				<xsl:text>.html</xsl:text>
				<xsl:value-of select="@anchor"/>
				<xsl:text>')</xsl:text>
			</xsl:attribute>

			<xsl:apply-templates select="." mode="reference-content"/>
		</fo:basic-link>
	</xsl:template>

	<xsl:template match="reference[@class and starts-with(@class, 'javax.persistence')]" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a">
			<xsl:attribute name="external-destination">
				<xsl:text>url('</xsl:text>
				<xsl:text>http://docs.oracle.com/javaee/6/api/</xsl:text>
				<xsl:value-of select="translate(@class, '.', '/')"/>
				<xsl:text>.html</xsl:text>
				<xsl:value-of select="@anchor"/>
				<xsl:text>')</xsl:text>
			</xsl:attribute>

			<xsl:apply-templates select="." mode="reference-content"/>
		</fo:basic-link>
	</xsl:template>

	<xsl:template match="reference[@class and (starts-with(@class, 'java') or starts-with(@class, 'org.w3c.dom'))]" mode="content">
		<fo:basic-link xsl:use-attribute-sets="a">
			<xsl:attribute name="external-destination">
				<xsl:text>url('</xsl:text>
				<xsl:text>http://download.oracle.com/javase/8/docs/api</xsl:text>
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
				<xsl:text>https://github.com/jOOQ/jOOQ/issues/</xsl:text>
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
				<xsl:value-of select="/manuals/manual[@version = $minorVersion]//section[@id = $id]/title"/>
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
		<fo:external-graphic
			src="url('jooq.org/img/{substring-after(@src, 'img/')}')"
            width="100%"
            content-height="100%"
            content-width="scale-to-fit"
            scaling="uniform"
			xsl:use-attribute-sets="img"/>
	</xsl:template>

	<xsl:template match="p" mode="content">
		<fo:block xsl:use-attribute-sets="p">
			<xsl:if test="following-sibling::node()[name(.) = 'p']">
				<xsl:attribute name="margin-bottom">6pt</xsl:attribute>
			</xsl:if>

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
			      			<xsl:apply-templates select="*[position() = 1]" mode="content" />
			      		</fo:block>
			    	</fo:table-cell>
			    	<fo:table-cell>
			     		<fo:block>
			     			<xsl:apply-templates select="*[position() = 2]" mode="content" />
			     		</fo:block>
			    	</fo:table-cell>
			  	</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<xsl:template match="java | javascript | scala | sql | xml | text | config" mode="content">
		<fo:block xsl:use-attribute-sets="pre">
			<xsl:apply-templates mode="content" />
        </fo:block>
	</xsl:template>

	<!-- Ignore code blocks, render contents as such -->
	<xsl:template match="code" mode="content">
		<xsl:apply-templates mode="content"/>
	</xsl:template>

    <xsl:template match="grammar" mode="content">
        <p>
            The grammar is currently not supported in the PDF version of the manual. Please visit the HTML documentation to see the grammar.
        </p>
    </xsl:template>

	<xsl:template match="@*|node()" mode="content">
		<xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="content"/>
        </xsl:copy>
    </xsl:template>

   	<xsl:template match="section" mode="toc">
   		<xsl:for-each select=".//section">
			<fo:block xsl:use-attribute-sets="ol-toc">
				<fo:list-block>
					<fo:list-item>
		          		<fo:list-item-label>
		          			<fo:block xsl:use-attribute-sets="li-toc">
	            			</fo:block>
		          		</fo:list-item-label>
		          		<fo:list-item-body>
		          			<fo:block xsl:use-attribute-sets="li-toc">
		          				<fo:block text-align-last="justify">
			          				<fo:inline>
			          					<fo:basic-link internal-destination="{@id}">
					          				<xsl:apply-templates select="." mode="chapter-number"/>
					          				<xsl:text>  </xsl:text>
					            			<xsl:value-of select="title" />
					            			<fo:leader leader-pattern="dots"/>
					            			<fo:page-number-citation ref-id="{@id}"/>
				            			</fo:basic-link>
			          				</fo:inline>
		          				</fo:block>
		            		</fo:block>
		          		</fo:list-item-body>
		        	</fo:list-item>
				</fo:list-block>
			</fo:block>
   		</xsl:for-each>
	</xsl:template>

	<xsl:template match="section" mode="chapter-number">
		<xsl:if test="@id != 'manual'">
			<xsl:apply-templates select="../.." mode="chapter-number"/>

			<xsl:value-of select="count(preceding-sibling::section) + 1"/>
			<xsl:text>.</xsl:text>
		</xsl:if>
	</xsl:template>

    <xsl:template match="section" mode="bookmark">
        <xsl:variable name="id" select="@id"/>

        <fo:bookmark internal-destination="{$id}">

            <!-- Hide all bookmarks except the top-level one -->
            <xsl:attribute name="starting-state">
                <xsl:choose>
                    <xsl:when test=". = /manuals/manual/section">show</xsl:when>
                    <xsl:otherwise>hide</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>

            <fo:bookmark-title>
                <xsl:value-of select="title" />
            </fo:bookmark-title>

            <xsl:apply-templates select="sections/section" mode="bookmark"/>
        </fo:bookmark>
    </xsl:template>
</xsl:stylesheet>