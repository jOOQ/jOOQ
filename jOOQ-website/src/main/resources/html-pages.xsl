<?xml version="1.0" encoding="UTF-8"?>
<!-- 
  - Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
  - All rights reserved.
  - 
  - This work is dual-licensed Open Source, under AGPL and jOOQ EULA
  - ============================================================================
  - You may freely choose which license applies to you. For more information 
  - about licensing, please visit http://www.jooq.org/licenses
  -
  - AGPL:  
  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
  - This library is free software; you can redistribute it and/or
  - modify it under the terms of the GNU Affero General Public
  - License as published by the Free Software Foundation; either 
  - version 2.1 of the License, or (at your option) any later version.
  -
  - This library is distributed in the hope that it will be useful,
  - but WITHOUT ANY WARRANTY; without even the implied warranty of
  - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  - Lesser General Public License for more details.
  - 
  - You should have received a copy of the GNU Affero General Public 
  - License along with this library.
  - If not, see http://www.gnu.org/licenses.
  - 
  - jOOQ End User License Agreement:
  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
  - This library is commercial software; you may not redistribute it and/or
  - modify it.
  - 
  - This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
  - License Agreement for more details.
  - 
  - You should have received a copy of the jOOQ End User License Agreement
  - along with this library.
  - If not, see http://www.jooq.org/eula
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
function printTheme() {
    noTheme();
}
function printContent() {
    global $root;
?&gt;
</xsl:text>
        <div id="manual">
        <xsl:apply-templates select="//section[@id = $sectionID]" mode="content"/>
        </div>
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
            <a href="#{$id}" name="{$id}">
                <xsl:apply-templates mode="content"/>
            </a>
        </h2>
    </xsl:template>

    <xsl:template match="//section[@id = $sectionID]" mode="content">
        <section>
            <div class="row col col-100 col-white">
                <xsl:apply-templates select="." mode="navigation"/>
            </div>
            
            <xsl:apply-templates select="content"/>
        
            <div class="row col col-100 col-white">
                <xsl:if test="count(sections/section) &gt; 0">
                    <h2 id="toc"><a href="#toc" name="toc">Table of contents</a></h2>
                </xsl:if>
                <xsl:apply-templates select="." mode="toc"/>
        
                <br/>
                <xsl:apply-templates select="." mode="navigation"/>
            </div>
        </section>
    </xsl:template>

    <xsl:template match="section" mode="navigation">
        <table cellpadding="0" cellspacing="0" border="0" width="936">
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