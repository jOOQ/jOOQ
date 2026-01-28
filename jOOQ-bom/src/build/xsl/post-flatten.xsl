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
  * For more information, please visit https://www.jooq.org/legal/licensing
  *
  * Apache Software License 2.0:
  * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *  https://www.apache.org/licenses/LICENSE-2.0
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
  * and Maintenance Agreement for more details: https://www.jooq.org/legal/licensing
  -->
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" 
    xmlns:pom="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <xsl:output encoding="UTF-8" method="xml"/>

    <xsl:template match="/">
        <xsl:apply-templates select="@*|node()"/>
    </xsl:template>
    
    <xsl:template match="pom:dependency">
        <xsl:if test="starts-with(pom:groupId, 'org.jooq') 
                and not(starts-with(pom:artifactId, 'jooq-test'))
                and not(starts-with(pom:artifactId, 'jooq-tools'))">
            <xsl:copy>
                <xsl:apply-templates select="@*|node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>

        <xsl:if test="name() = 'description'">
          <xsl:value-of disable-output-escaping="yes" select="concat('&#10;  &lt;packaging&gt;pom&lt;/packaging&gt;')" />
        </xsl:if>

    </xsl:template>
</xsl:stylesheet>
