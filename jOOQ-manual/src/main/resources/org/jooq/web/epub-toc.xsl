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
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output encoding="UTF-8" method="xml" indent="yes"/>

    <xsl:param name="minorVersion"/>

    <xsl:template match="/">
        <xsl:apply-templates select="/manuals/manual[@version = $minorVersion]"/>
    </xsl:template>

    <xsl:template match="/manuals/manual[@version = $minorVersion]">
        <xsl:text disable-output-escaping='yes'>&lt;ncx
            xmlns="http://www.daisy.org/z3986/2005/ncx/"
            xmlns:ncx="http://www.daisy.org/z3986/2005/ncx/"
            version="2005-1" xml:lang="en"&gt;</xsl:text>

            <head>
                <meta name="dtb:uid" content="http://www.jooq.org/doc/{$minorVersion}/manual-epub/jOOQ-manual-{$minorVersion}.epub"/>
            </head>
            <docTitle>
		        <text>The jOOQ User Manual</text>
		    </docTitle>
		    <navMap>
		        <navInfo>
		            <text>THE CONTENTS</text>
		        </navInfo>

		        <xsl:apply-templates select="section" mode="navPoint"/>
		    </navMap>

		    <!--
		    <pageList>

		    </pageList>
		     -->
        <xsl:text disable-output-escaping='yes'>&lt;/ncx&gt;</xsl:text>
    </xsl:template>

    <xsl:template match="section" mode="navPoint">
        <navPoint id="{@id}">
            <navLabel>
                <text>
                    <xsl:value-of select="title"/>
                </text>
            </navLabel>

            <content src="s04.xhtml#{@id}"/>

            <xsl:apply-templates select="sections/section" mode="navPoint"/>
        </navPoint>
    </xsl:template>
</xsl:stylesheet>