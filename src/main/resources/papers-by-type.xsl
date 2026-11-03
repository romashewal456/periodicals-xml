<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="papers-by-type" match="paper" use="type"/>

    <xsl:template match="/papers">
        <papers-by-type>
            <xsl:for-each select="paper[generate-id() = generate-id(key('papers-by-type', type)[1])]">
                <xsl:sort select="type"/>
                <group type="{type}" count="{count(key('papers-by-type', type))}">
                    <xsl:apply-templates select="key('papers-by-type', type)"/>
                </group>
            </xsl:for-each>
        </papers-by-type>
    </xsl:template>

    <xsl:template match="paper">
        <paper id="{@id}">
            <xsl:copy-of select="@language"/>
            <xsl:apply-templates select="title | monthly | chars"/>
        </paper>
    </xsl:template>

    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
