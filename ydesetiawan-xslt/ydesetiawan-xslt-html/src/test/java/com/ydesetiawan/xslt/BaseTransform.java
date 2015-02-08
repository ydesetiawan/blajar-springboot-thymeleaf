package com.ydesetiawan.xslt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;

/**
 * @author edys
 * @version 1.0, Mar 19, 2014
 * @since
 */
public abstract class BaseTransform {

	private TransformerFactory factory;
    private Map<String, Object> parameters;

    public BaseTransform() {
        factory = TransformerFactory.newInstance();
    }
    private static final String ENCODING = "UTF-8";

    protected void transformToHtml(String sourceXml, String sourceXsl,
            String destinationName) {
        StringWriter strWriter = new StringWriter();
        try {
            Reader xmlInput = new StringReader(
                    FileUtils.readFileToString(new File(sourceXml)));
            Reader xslInput = new StringReader(
                    FileUtils.readFileToString(new File(sourceXsl)));

            process(xmlInput, xslInput, strWriter);
            FileUtils.writeStringToFile(new File("target/" + destinationName),
                    strWriter.toString(), ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    protected void transformToPdf(String sourceXml, String destinationName,
            Transformer transformer) throws FOPException, TransformerException {

        Fop fop;
        try {
            Reader xmlInput = new StringReader(
                    FileUtils.readFileToString(new File(sourceXml)));

            StreamSource xmlSource = new StreamSource(xmlInput);

            FopFactory fopFactory = FopFactory.newInstance();
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            fop = fopFactory.newFop(
                    org.apache.xmlgraphics.util.MimeConstants.MIME_PDF,
                    foUserAgent, outStream);

            Result outputTarget = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, outputTarget);

            FileUtils.writeByteArrayToFile(
                    new File("target/" + destinationName),
                    outStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void process(Reader xmlFile, Reader xslFile, Writer output)
            throws TransformerException {
        process(new StreamSource(xmlFile), new StreamSource(xslFile),
                new StreamResult(output));
    }
    
    private void process(Source xml, Source xsl, Result result)
            throws TransformerException {
        try {
            Templates template = factory.newTemplates(xsl);
            Transformer transformer = template.newTransformer();
            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    transformer.setParameter(entry.getKey(), entry.getValue());
                }
            }
            transformer.transform(xml, result);
        } catch (TransformerConfigurationException tce) {
            throw new TransformerException(tce.getMessageAndLocation());
        } catch (TransformerException te) {
            throw new TransformerException(te.getMessageAndLocation());
        }
    }

}
