package com.ydesetiawan.xslt;

import org.testng.annotations.Test;

public class TransformationTest extends BaseTransform {

	private static final String TEST_DOCS = "src/test/resources/";
	private static final String XLS_FORM = "src/test/resources/";

	@Test
	public void transformViewHtml() throws Exception {
		String sourceXml = TEST_DOCS + "catalog-data.xml";
		String sourceXsl = XLS_FORM + "catalog-html.xslt";
		String destinationName = "catalog.html";
		transformToHtml(sourceXml, sourceXsl, destinationName);
	}

}
