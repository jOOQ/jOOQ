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

    <xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes" indent="yes"/>

    <xsl:template match="html-only" mode="content">
        <xsl:apply-templates mode="content"/>
    </xsl:template>

    <xsl:template match="section" mode="toc">
        <xsl:if test="count(sections/section) &gt; 0">
            <dl class="toc">
                <xsl:apply-templates select="." mode="toc-contents"/>
            </dl>
        </xsl:if>
    </xsl:template>

    <xsl:template match="section" mode="toc-contents">
        <xsl:if test="count(sections/section) &gt; 0">
            <xsl:for-each select="sections/section">
                <xsl:variable name="class">
                    <xsl:if test="../../@id = 'manual'">toc-main</xsl:if>
                </xsl:variable>

                <dt class="{$class}">
                    <xsl:apply-templates select="." mode="chapter-number"/>
                </dt>

                <dd class="{$class}">
                    <xsl:variable name="href">
                        <xsl:apply-templates select="." mode="href"/>
                    </xsl:variable>

                    <a href="{$href}" title="{title}">
                        <xsl:value-of select="title"/>
                    </a>
                </dd>

                <xsl:apply-templates select="." mode="toc-contents"/>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>

    <xsl:template match="content">
        <xsl:apply-templates select="@*|node()" mode="content"/>
    </xsl:template>

    <xsl:template match="html" mode="content">
        <div class="row col col-100 col-white">
            <xsl:apply-templates select="@*|node()" mode="content"/>
        </div>
    </xsl:template>

    <xsl:template match="@*|node()" mode="content">
        <xsl:param name="colwidth" select="'100'"/>
        <xsl:param name="col1" select="false()"/>
        <xsl:param name="col2" select="false()"/>

        <xsl:choose>
            <xsl:when test="name(.) = 'reference'">
                <xsl:variable name="id" select="@id"/>

                <a>
                    <xsl:attribute name="href">
                        <xsl:choose>
                            <xsl:when test="@id">
                                <xsl:apply-templates select="/manuals/manual[@version = $minorVersion]//section[@id = $id]" mode="href"/>
                                <xsl:value-of select="@anchor"/>

                                <xsl:if test="not(/manuals/manual[@version = $minorVersion]//section[@id = $id])">
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

                            <xsl:when test="@class and starts-with(@class, 'org.jooq')">
                                <xsl:text>http://www.jooq.org/javadoc/</xsl:text>
                                <xsl:value-of select="$minorVersion"/>
                                <xsl:text>.x/</xsl:text>
                                <xsl:value-of select="translate(@class, '.', '/')"/>
                                <xsl:text>.html</xsl:text>
                                <xsl:choose>
                                    <xsl:when test="$minorVersion > '3.3'">
                                        <xsl:call-template name="java-8-javadoc-anchor">
                                            <xsl:with-param name="anchor" select="@anchor"/>
                                        </xsl:call-template>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="@anchor"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>

                            <xsl:when test="@class and (starts-with(@class, 'javax.persistence'))">
                                <xsl:text>http://docs.oracle.com/javaee/7/api/</xsl:text>
                                <xsl:value-of select="translate(@class, '.', '/')"/>
                                <xsl:text>.html</xsl:text>
                                <xsl:value-of select="@anchor"/>
                            </xsl:when>

                            <xsl:when test="@class and (starts-with(@class, 'java') or starts-with(@class, 'org.w3c.dom'))">
                                <xsl:text>http://download.oracle.com/javase/8/docs/api/</xsl:text>
                                <xsl:value-of select="translate(@class, '.', '/')"/>
                                <xsl:text>.html</xsl:text>
                                <xsl:call-template name="java-8-javadoc-anchor">
                                    <xsl:with-param name="anchor" select="@anchor"/>
                                </xsl:call-template>
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
                                <xsl:value-of select="/manuals/manual[@version = $minorVersion]//section[@id = $id]/title"/>
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
                </a>
            </xsl:when>
            <xsl:when test="name(.) = 'java'">
                <pre class="row col col-{$colwidth} col-black prettyprint lang-java">
                    <xsl:value-of select="text()"/>
                </pre>
            </xsl:when>
            <xsl:when test="name(.) = 'javascript'">
                <pre class="row col col-{$colwidth} col-black prettyprint lang-javascript">
                    <xsl:value-of select="text()"/>
                </pre>
            </xsl:when>
            <xsl:when test="name(.) = 'scala'">
                <pre class="row col col-{$colwidth} col-black prettyprint lang-scala">
                    <xsl:value-of select="text()"/>
                </pre>
            </xsl:when>
            <xsl:when test="name(.) = 'groovy'">
                <pre class="row col col-{$colwidth} col-black prettyprint lang-groovy">
                    <xsl:value-of select="text()"/>
                </pre>
            </xsl:when>
            <xsl:when test="name(.) = 'sql'">
                <pre class="row col col-{$colwidth} col-black prettyprint lang-sql">
                    <xsl:value-of select="text()"/>
                </pre>
            </xsl:when>
            <xsl:when test="name(.) = 'xml'">
                <pre class="row col col-{$colwidth} col-black prettyprint lang-xml">
                    <xsl:value-of select="text()"/>
                </pre>
            </xsl:when>
            <xsl:when test="name(.) = 'config'">
                <pre class="row col col-{$colwidth} col-black prettyprint">
                    <xsl:value-of select="text()"/>
                </pre>
            </xsl:when>
            <xsl:when test="name(.) = 'text'">
                <pre class="row col col-{$colwidth} col-red">
                    <xsl:value-of select="text()"/>
                </pre>
            </xsl:when>
            <xsl:when test="name(.) = 'code-pair'">
                <xsl:apply-templates select="./*[position() = 1]" mode="content">
                    <xsl:with-param name="colwidth" select="'50'"/>
                    <xsl:with-param name="col1" select="true()"/>
                </xsl:apply-templates>
                <xsl:apply-templates select="./*[position() = 2]" mode="content">
                    <xsl:with-param name="colwidth" select="'50'"/>
                    <xsl:with-param name="col2" select="true()"/>
                </xsl:apply-templates>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@*|node()" mode="content"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="section" mode="chapter-number">
        <xsl:if test="@id != 'manual'">
            <xsl:apply-templates select="../.." mode="chapter-number"/>

            <xsl:value-of select="count(preceding-sibling::section) + 1"/>
            <xsl:text>.</xsl:text>
        </xsl:if>
    </xsl:template>

    <!-- Replace function found here: http://stackoverflow.com/q/7677072/521799 -->
    <xsl:template name="replace">
        <xsl:param name="text" />
        <xsl:param name="replace" />
        <xsl:param name="by" />
        <xsl:choose>
            <xsl:when test="contains($text, $replace)">
                <xsl:value-of select="substring-before($text,$replace)" />
                <xsl:value-of select="$by" />
                <xsl:call-template name="replace">
                    <xsl:with-param name="text"
                        select="substring-after($text,$replace)" />
                    <xsl:with-param name="replace" select="$replace" />
                    <xsl:with-param name="by" select="$by" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="basename">
        <xsl:param name="path" />

        <xsl:choose>
            <xsl:when test="contains($path, '/')">
                <xsl:call-template name="basename">
                    <xsl:with-param name="path" select="substring-after($path, '/')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$path" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <!-- Java 8 Javadoc now uses different anchor formatting, to prevent collisions with URLencoding -->
    <xsl:template name="java-8-javadoc-anchor">
        <xsl:param name="anchor" />

        <xsl:call-template name="replace">
            <xsl:with-param name="replace" select="'('"/>
            <xsl:with-param name="by" select="'-'"/>
            <xsl:with-param name="text">
                <xsl:call-template name="replace">
                    <xsl:with-param name="replace" select="')'"/>
                    <xsl:with-param name="by" select="'-'"/>
                    <xsl:with-param name="text">
                        <xsl:call-template name="replace">
                            <xsl:with-param name="replace" select="','"/>
                            <xsl:with-param name="by" select="'-'"/>
                            <xsl:with-param name="text">
                                <xsl:call-template name="replace">
                                    <xsl:with-param name="replace" select="' '"/>
                                    <xsl:with-param name="by" select="''"/>
                                    <xsl:with-param name="text">
                                        <xsl:value-of select="$anchor"/>
                                    </xsl:with-param>
                                </xsl:call-template>
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
</xsl:stylesheet>