package it.pagopa.selfcare.party.registry_proxy.connector.rest.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLCleaner {

    public static byte[] cleanXml(byte[] rawXml, List<String> nodesToRemove) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(rawXml));

        for (String nodeToRemove : nodesToRemove) {
            NodeList elements = doc.getElementsByTagName(nodeToRemove);
            for (int i = elements.getLength() - 1; i >= 0; i--) {
                Node node = elements.item(i);
                Node parent = node.getParentNode();
                if (parent != null) {
                    parent.removeChild(node);
                }
            }
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(output));
        return output.toByteArray();
    }
}
