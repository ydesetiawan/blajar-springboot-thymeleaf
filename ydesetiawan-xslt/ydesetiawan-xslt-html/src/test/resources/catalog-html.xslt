<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (C) 2014 ASYX International B.V. All rights reserved. -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:th="http://www.thymeleaf.org" xmlns:order="urn:ean.ucc:order:2" xmlns:deliver="urn:ean.ucc:deliver:2"
	xmlns:pay="urn:ean.ucc:pay:2" xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
	<xsl:output method="html" encoding="UTF-8" />

	<xsl:template name="roundingValue">
		<xsl:param name="param" />
		<xsl:value-of select="format-number($param, '###,###,###,##0.00')" />
	</xsl:template>

	<xsl:template name="conditional">
		<xsl:param name="param" />
		<xsl:choose>
			<xsl:when test="$param &lt; 100000">
				C
			</xsl:when>
			<xsl:when test="$param > 100000 and $param &lt; 500000">
				B
			</xsl:when>
			<xsl:when test="$param > 500000">
				A
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="/">
		<html>
			<body>
				<h2>CD Catalog</h2>
				<table border="1">
					<tr>
						<th>Title</th>
						<th>Artist</th>
						<th>Price</th>
						<th>Quantity</th>
						<th>Sub Total</th>
						<th>Level</th>
					</tr>
					<xsl:for-each select="catalog/cd">
						<tr>
							<td>
								<xsl:value-of select="title" />
							</td>
							<td>
								<xsl:value-of select="artist" />
							</td>
							<td>
								<xsl:value-of select="price" />
							</td>
							<td>
								<xsl:value-of select="quantity" />
							</td>
							<xsl:variable name="subtotal">
								<xsl:value-of select="price*quantity" />
							</xsl:variable>
							<td>
								<xsl:call-template name="roundingValue">
									<xsl:with-param name="param" select="$subtotal" />
								</xsl:call-template>
							</td>
							<td>
								<xsl:call-template name="conditional">
									<xsl:with-param name="param" select="$subtotal" />
								</xsl:call-template>
							</td>
						</tr>
					</xsl:for-each>
					<xsl:for-each select="catalog/extension">
						<tr>
							<td colspan="3">Total</td>
							<td>
								<xsl:value-of select="totalQuantity" />
							</td>
							<td colspan="1">
								<xsl:value-of select="format-number(totalAmount, '###,###,###,##0.00')" />
							</td>
							<td>
								<xsl:call-template name="conditional">
									<xsl:with-param name="param" select="totalAmount" />
								</xsl:call-template>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<p>
					Date Created :
					<xsl:value-of select="//@datecreated" />
				</p>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>