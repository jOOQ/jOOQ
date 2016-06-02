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

    <xsl:import href="src/main/resources/org/jooq/web/html-util.xsl"/>

    <xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>
    <xsl:param name="sectionID"/>
    <xsl:param name="relativePath"/>
    <xsl:param name="root"/>
    <xsl:param name="minorVersion"/>

    <xsl:variable name="apos">&apos;</xsl:variable>

    <!-- Main match -->

    <xsl:template match="/">
        <xsl:apply-templates select="/manuals/manual[@version = $minorVersion]"/>
    </xsl:template>

    <xsl:template match="/manuals/manual[@version = $minorVersion]">
        <xsl:text disable-output-escaping="yes">
&lt;?php
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '</xsl:text>
        <xsl:value-of select="$relativePath"/>
<xsl:text disable-output-escaping="yes">frame.php';
function getH1() {
    return "</xsl:text>
    <xsl:value-of select="/manuals/manual[@version = $minorVersion]//section[@id = $sectionID]/title"/>
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
        <xsl:apply-templates select="/manuals/manual[@version = $minorVersion]//section[@id = $sectionID]" mode="content"/>
        </div>

        <div id="comments">
          <div class="section">
            <div class="row col col-100 col-white">
              <div id="disqus_thread"></div>
              <script type="text/javascript">
                  var disqus_shortname = 'jooq'; // required: replace example with your forum shortname
                  var disqus_identifier = 'manual/<xsl:value-of select="$sectionID"/>';
                  var disqus_url = 'http://www.jooq.org<xsl:apply-templates select="/manuals/manual[@version = $minorVersion]//section[@id = $sectionID]" mode="href"/>';

                  (function() {
                      var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
                      dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
                      (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
                  })();
              </script>
            </div>
          </div>
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

    <xsl:template match="/manuals/manual[@version = $minorVersion]//section[@id = $sectionID]" mode="content">
        <div class="section">
            <div class="row col col-100 col-white">
                <xsl:apply-templates select="." mode="navigation"/>
            </div>

            <div class="row col col-100 col-red">
				<p>
                This page in other versions:

                <xsl:for-each select="/manuals/manual[.//section[@id = $sectionID]]">
                    <xsl:sort select="@version" order="descending"/>

                    <xsl:variable name="position" select="position()"/>
                    <xsl:variable name="version" select="@version"/>
                    <xsl:variable name="manual" select="."/>

                    <xsl:if test="$position > 1">
                        <xsl:text> | </xsl:text>
                        <xsl:if test="@end-of-life = 'true' and not(/manuals/manual[count(/manuals/manual) - $position + 2]/@end-of-life = 'true')">
                            <xsl:text> Old, end-of-life releases: </xsl:text>
                        </xsl:if>
                    </xsl:if>

                    <xsl:choose>
                        <xsl:when test="@version != $minorVersion">
                            <xsl:variable name="href">
                                <xsl:call-template name="replace">
                                    <xsl:with-param name="text">
                                        <xsl:apply-templates select="$manual//section[@id = $sectionID]" mode="href"/>
                                    </xsl:with-param>
                                    <xsl:with-param name="replace" select="$minorVersion"/>
                                    <xsl:with-param name="by" select="$version"/>
                                </xsl:call-template>
                            </xsl:variable>
                            <a href="{$href}">
                                <xsl:value-of select="$version"/>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <strong style="font-size: 2em">
                                <xsl:value-of select="$version"/>
                            </strong>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
                </p>
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
        </div>
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
            <xsl:for-each select="/manuals/manual[@version = $minorVersion]//section">
                <xsl:if test="@id = $id">
                    <xsl:value-of select="position()"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>

        <xsl:for-each select="/manuals/manual[@version = $minorVersion]//section">
            <xsl:if test="position() = $position - 1">
                <xsl:value-of select="@id"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="section" mode="next-id">
        <xsl:variable name="id" select="@id"/>

        <xsl:variable name="position">
            <xsl:for-each select="/manuals/manual[@version = $minorVersion]//section">
                <xsl:if test="@id = $id">
                    <xsl:value-of select="position()"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>

        <xsl:for-each select="/manuals/manual[@version = $minorVersion]//section">
            <xsl:if test="position() = $position + 1">
                <xsl:value-of select="@id"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>