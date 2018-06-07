package glog.util;

import java.io.File;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathParser {
	private TagNode tagNode;
	private Document doc;
	private XPath xp;
	private String pageContents;

	public XPathParser(String url) throws Exception {
		URLGrabber ug = new URLGrabber(url);
		this.pageContents = ug.getContents();
		this.tagNode = new HtmlCleaner().clean(this.pageContents);
		this.doc = new DomSerializer(new CleanerProperties()).createDOM(this.tagNode);
		this.xp = XPathFactory.newInstance().newXPath();
	}

	public XPathParser(File url) throws Exception {
		this.pageContents = IOUtil.getContents(url);
		this.tagNode = new HtmlCleaner().clean(this.pageContents);
		this.doc = new DomSerializer(new CleanerProperties()).createDOM(this.tagNode);
		this.xp = XPathFactory.newInstance().newXPath();
	}

	public String parse(String xpath) throws Exception {
		return (String) xp.evaluate(xpath, doc, XPathConstants.STRING);
	}

	public NodeList parseList(String xpath) throws Exception {
		return (NodeList) xp.evaluate(xpath, doc, XPathConstants.NODESET);
	}

	public Node parseNode(String xpath) throws Exception {
		return (Node) xp.evaluate(xpath, doc, XPathConstants.NODE);
	}

	public String getPageContents() {
		return this.pageContents;
	}
}
