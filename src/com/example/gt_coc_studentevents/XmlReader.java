/*
 * This class downloads an XML file from Mercury and converts the feed into a
 * list of objects
 * 
 * Current feed is at http://hg.gatech.edu/feed/309051/xml/automatic
 * 
 * http://www.javacodegeeks.com/2013/05/parsing-xml-using-dom-sax-and-stax-parser-in-java.html
 * 
 */

package com.example.gt_coc_studentevents;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Downloads an XML feed from Mercury (http://hg.gatech.edu), and converts it into an ArrayList
 * @author Dan Holdridge
 *
 */
public class XmlReader {
	
	/** Connects to the Mercury webserver and downloads the XML feed
	 * @return The XML file as an InputStream
	 */
	private static InputStream getXml() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://hg.gatech.edu/feed/309051/xml/automatic");
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		try {
			return entity.getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
	}
	
	/** Convers the XML InputStream into a DOM document
	 * @return The converted DOM document as a org.w3c.dom.document type
	 */
	private static Document buildXmlTree() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Document document = null;
		try {
			document = builder.parse(getXml());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return document;
		
		
	}
	
	/** Iterates though the Document, pulling only the information we want and creating an ArrayList with it
	 * @return A list of events as an ArrayList<EventListing>
	 */
	public static ArrayList<EventListing> buildList() {
		ArrayList<EventListing> eventList = new ArrayList<EventListing>();
		Document document = buildXmlTree();
		
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		
		for (int i =0; i < nodeList.getLength() && i < 10; i++){
			//We are at the <node> tag, representing a single event
			Node node = nodeList.item(i);
			if (node instanceof Element){
				EventListing evt = new EventListing();
				NodeList childNodes = node.getChildNodes();
				//Getting the details for the event
				//Indices:
					//0 - Title
					//2 - Body (details)
					//16 - Start
					//17 - End
					//31 - Location
				evt.setEventName( childNodes.item(0).getLastChild().getTextContent().trim() );
				evt.setDescription( childNodes.item(2).getLastChild().getTextContent().trim() );
				evt.setTime( childNodes.item(16).getLastChild().getTextContent().trim() );
				evt.setLocation( childNodes.item(31).getLastChild().getTextContent().trim() );
				eventList.add(evt);
			}
			
			
		}
		return eventList;
		
	}
	
	/** Downloads the info as an ArrayList and prints it out for testing purposes.
	 * @param args
	 */
	public static void main(String[] args){
		
		ArrayList<EventListing> eventList = buildList();
		for (EventListing e : eventList) {
			System.out.println( e.toString() );
		}
	}

}
