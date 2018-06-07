package glog.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Node;

public class XMLUtil {
	/**
	 * Unmarshal XML to Wrapper and return List value.
	 */
	public static <T> List<T> unmarshal(Unmarshaller unmarshaller, Class<T> clazz, String xmlLocation) throws JAXBException {
		StreamSource xml = new StreamSource(xmlLocation);
		XMLWrapper<T> wrapper = unmarshaller.unmarshal(xml, XMLWrapper.class).getValue();
		return wrapper.getItems();
	}

	public static <T> List<T> unmarshalString(Unmarshaller unmarshaller, Class<T> clazz, String str) throws JAXBException {
		StreamSource xml = new StreamSource(new StringReader(str));
		XMLWrapper<T> wrapper = unmarshaller.unmarshal(xml, XMLWrapper.class).getValue();
		return wrapper.getItems();
	}

	/**
	 * Wrap List in Wrapper, then leverage JAXBElement to supply root element information.
	 */
	public static String marshal(Marshaller marshaller, List<?> list, String name) throws JAXBException {
		QName qName = new QName(name);
		XMLWrapper wrapper = new XMLWrapper(list);
		JAXBElement<XMLWrapper> jaxbElement = new JAXBElement<XMLWrapper>(qName, XMLWrapper.class, wrapper);
		StringWriter sw = new StringWriter();
		marshaller.marshal(jaxbElement, sw);
		return sw.toString();
	}

	public static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}
}
