<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="html" encoding="UTF-8"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Fixed width visualizer</title>
            </head>
            <body>
                <table >
                    <tbody>
                        <xsl:apply-templates select="ListN//item" />
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="ListN//item">
        <tr>
            <xsl:apply-templates select="fieldValues//fieldValues"/>
        </tr>
    </xsl:template>

    <xsl:template match="fieldValues//fieldValues">
        <td style="border-bottom-width: 1px; border-bottom-style: solid;">
            <xsl:value-of select="./value/text()"/>
        </td>
    </xsl:template>
</xsl:stylesheet>