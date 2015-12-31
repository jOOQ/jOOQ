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
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:regexp="http://exslt.org/regexp">

    <xsl:output encoding="UTF-8" method="xml" indent="yes"/>

    <xsl:key name="schema" match="/DatabaseModel/Tables/Table/Properties/Property[Name = 'Schema']" use="." />

    <xsl:template match="/">
        <information_schema xmlns="http://www.jooq.org/xsd/jooq-meta-3.5.4.xsd">
            <schemata>
                <xsl:apply-templates select="/DatabaseModel/Tables/Table/Properties/Property[Name = 'Schema'][generate-id() = generate-id(key('schema', .)[1])]" mode="schema"/>
            </schemata>

            <tables>
                <xsl:apply-templates select="/DatabaseModel/Tables/Table" mode="table"/>
            </tables>

            <columns>
                <xsl:apply-templates select="/DatabaseModel/Tables/Table/Columns/Column" mode="column"/>
            </columns>

            <sequences>
                <xsl:apply-templates select="/DatabaseModel/Sequences/Sequence" mode="sequence"/>
            </sequences>

            <table_constraints>
                <xsl:comment>Primary keys</xsl:comment>
                <xsl:apply-templates select="/DatabaseModel/Tables/Table[Columns/Column/PK/text() = 'true']" mode="table_constraint"/>

                <xsl:comment>Unique keys</xsl:comment>
                <xsl:apply-templates select="/DatabaseModel/Tables/Table/AlternateKeys/AlternateKey" mode="table_constraint"/>
            </table_constraints>

            <key_column_usages>
                <xsl:comment>Primary keys</xsl:comment>
                <xsl:apply-templates select="/DatabaseModel/Tables/Table/Columns/Column/PK[text() = 'true']" mode="key_column_usage"/>

                <xsl:comment>Unique keys</xsl:comment>
                <xsl:apply-templates select="/DatabaseModel/Tables/Table/AlternateKeys/AlternateKey/Columns/Column" mode="key_column_usage"/>

                <xsl:comment>Foreign keys</xsl:comment>
                <xsl:apply-templates select="/DatabaseModel/References/Reference/ReferenceColumns/ReferenceColumn" mode="key_column_usage"/>
            </key_column_usages>

            <referential_constraints>
                <xsl:apply-templates select="/DatabaseModel/References/Reference" mode="referential_constraint"/>
            </referential_constraints>
        </information_schema>
    </xsl:template>

    <xsl:template match="Property" mode="schema">
        <schema>
            <schema_name><xsl:value-of select="Value"/></schema_name>
        </schema>
    </xsl:template>

    <xsl:template match="Sequence" mode="sequence">
        <sequence>
            <sequence_schema><xsl:value-of select="Properties/Property[Name = 'Schema']/Value"/></sequence_schema>
            <sequence_name><xsl:value-of select="Name"/></sequence_name>
            <data_type>BIGINT</data_type>
        </sequence>
    </xsl:template>

    <xsl:template match="Table" mode="table">
        <table>
            <table_schema><xsl:value-of select="Properties/Property[Name = 'Schema']/Value"/></table_schema>
            <table_name><xsl:value-of select="Name"/></table_name>
        </table>
    </xsl:template>

    <xsl:template match="Column" mode="column">
        <xsl:variable name="Id" select="@Id"/>

        <column>
            <table_schema><xsl:value-of select="ancestor::Table/Properties/Property[Name = 'Schema']/Value"/></table_schema>
            <table_name><xsl:value-of select="ancestor::Table/Name"/></table_name>
            <column_name><xsl:value-of select="Name"/></column_name>
            <data_type><xsl:value-of select="Type"/></data_type>
            <ordinal_position><xsl:value-of select="1 + count(preceding-sibling::Column)"/></ordinal_position>
            <is_nullable><xsl:value-of select="Nullable"/></is_nullable>
        </column>
    </xsl:template>

    <xsl:template match="Table" mode="table_constraint">
        <table_constraint>
            <constraint_schema><xsl:value-of select="Properties/Property[Name = 'Schema']/Value"/></constraint_schema>
            <constraint_name><xsl:text>PK_</xsl:text><xsl:value-of select="Name"/></constraint_name>
            <constraint_type>PRIMARY KEY</constraint_type>
            <table_schema><xsl:value-of select="Properties/Property[Name = 'Schema']/Value"/></table_schema>
            <table_name><xsl:value-of select="Name"/></table_name>
        </table_constraint>
    </xsl:template>

    <xsl:template match="AlternateKey" mode="table_constraint">
        <table_constraint>
            <constraint_schema><xsl:value-of select="ancestor::Table/Properties/Property[Name = 'Schema']/Value"/></constraint_schema>
            <constraint_name><xsl:value-of select="Name"/></constraint_name>
            <constraint_type>UNIQUE KEY</constraint_type>
            <table_schema><xsl:value-of select="ancestor::Table/Properties/Property[Name = 'Schema']/Value"/></table_schema>
            <table_name><xsl:value-of select="ancestor::Table/Name"/></table_name>
        </table_constraint>
    </xsl:template>

    <xsl:template match="PK" mode="key_column_usage">
        <key_column_usage>
            <constraint_schema><xsl:value-of select="ancestor::Table/Properties/Property[Name = 'Schema']/Value"/></constraint_schema>
            <constraint_name><xsl:text>PK_</xsl:text><xsl:value-of select="ancestor::Table/Name"/></constraint_name>
            <table_schema><xsl:value-of select="ancestor::Table/Properties/Property[Name = 'Schema']/Value"/></table_schema>
            <table_name><xsl:value-of select="ancestor::Table/Name"/></table_name>
            <column_name><xsl:value-of select="ancestor::Column/Name"/></column_name>
            <ordinal_position><xsl:value-of select="1 + count(ancestor::Column/preceding-sibling::Column[PK/text() = 'true'])"/></ordinal_position>
        </key_column_usage>
    </xsl:template>

    <xsl:template match="Column" mode="key_column_usage">
        <xsl:variable name="column" select="text()"/>

        <key_column_usage>
            <constraint_schema><xsl:value-of select="ancestor::Table/Properties/Property[Name = 'Schema']/Value"/></constraint_schema>
            <constraint_name><xsl:value-of select="ancestor::AlternateKey/Name"/></constraint_name>
            <table_schema><xsl:value-of select="ancestor::Table/Properties/Property[Name = 'Schema']/Value"/></table_schema>
            <table_name><xsl:value-of select="ancestor::Table/Name"/></table_name>
            <column_name><xsl:value-of select="/DatabaseModel/Tables/Table/Columns/Column[@Id = $column]/Name"/></column_name>
            <ordinal_position><xsl:value-of select="1 + count(preceding-sibling::Column)"/></ordinal_position>
        </key_column_usage>
    </xsl:template>

    <xsl:template match="ReferenceColumn" mode="key_column_usage">
        <xsl:variable name="pkTable" select="ancestor::Reference/PKTable"/>
        <xsl:variable name="fkTable" select="ancestor::Reference/FKTable"/>
        <xsl:variable name="fkColumn" select="FKColumn"/>

        <key_column_usage>
            <constraint_schema><xsl:value-of select="/DatabaseModel/Tables/Table[@Id = $fkTable]/Properties/Property[Name = 'Schema']/Value"/></constraint_schema>
            <constraint_name><xsl:value-of select="ancestor::Reference/Name"/></constraint_name>
            <table_schema><xsl:value-of select="/DatabaseModel/Tables/Table[@Id = $fkTable]/Properties/Property[Name = 'Schema']/Value"/></table_schema>
            <table_name><xsl:value-of select="/DatabaseModel/Tables/Table[@Id = $fkTable]/Name"/></table_name>
            <column_name><xsl:value-of select="/DatabaseModel/Tables/Table/Columns/Column[@Id = $fkColumn]/Name"/></column_name>
            <ordinal_position><xsl:value-of select="1 + count(preceding-sibling::ReferenceColumn)"/></ordinal_position>
        </key_column_usage>
    </xsl:template>

    <xsl:template match="Reference" mode="referential_constraint">
        <xsl:variable name="pkTable" select="PKTable"/>
        <xsl:variable name="fkTable" select="FKTable"/>

        <referential_constraint>
            <constraint_schema><xsl:value-of select="/DatabaseModel/Tables/Table[@Id = $fkTable]/Properties/Property[Name = 'Schema']/Value"/></constraint_schema>
            <constraint_name><xsl:value-of select="Name"/></constraint_name>
            <unique_constraint_schema><xsl:value-of select="/DatabaseModel/Tables/Table[@Id = $pkTable]/Properties/Property[Name = 'Schema']/Value"/></unique_constraint_schema>
            <unique_constraint_name><xsl:text>PK_</xsl:text><xsl:value-of select="/DatabaseModel/Tables/Table[@Id = $fkTable]/Name"/></unique_constraint_name>
        </referential_constraint>
    </xsl:template>
</xsl:stylesheet>