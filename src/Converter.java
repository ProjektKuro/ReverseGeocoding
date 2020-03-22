import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.*;

/**
 * @author Timo 'eXodiquas' Netzer
 *
 */
public class Converter {
	
	/**
	 * @param lon Längengrad des gesuchten Ortes
	 * @param lat Breitengrad des gesuchten Ortes
	 * @return Antwort des Servers von openstreetmap.org
	 * @throws IOException
	 */
	private String getServerResponse(double lon, double lat) throws IOException {
		URL url = new URL("https://nominatim.openstreetmap.org/reverse?format=xml&lat=" + lat + "&lon=" + lon + "&zoom=18&addressdetails=1");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.addRequestProperty("User-Agent", "Mozilla");
		con.setReadTimeout(5000);
		con.setConnectTimeout(5000);
		con.setRequestMethod("GET");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String response = "";
		String currentLine;
		
		while ((currentLine = br.readLine()) != null) {
			response += currentLine;
		}
		
		return response;
	}
	
	
	/*
	 * Copy Pasta from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
	 * */
	/**
	 * @param lon Längengrad des gesuchten Ortes
	 * @param lat Breitengrad des gesuchten Ortes
	 * @return XML Document aus Server Response
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	private Document parseXML(double lon, double lat) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append(getServerResponse(lon, lat));
		ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
		Document doc = builder.parse(input);
		return doc;
	}
	
	
	/**
	 * @param lon Längengrad des gesuchten Ortes
	 * @param lat Breitengrad des gesuchten Ortes
	 * @param st SearchType des gesuchten Feldes, es gibt TOWN, COUNTRY, POSTCODE und STREET
	 * @return Gibt das erste Vorkommen des gesuchten Feldes als String zurück
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	public String getLocationByLonLat(double lon, double lat, SearchType st) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr;
		
		switch(st) {
		case COUNTRY:
			expr = xpath.compile("//reversegeocode/addressparts/country/text()");
			break;
		case POSTCODE:
			expr = xpath.compile("//reversegeocode/addressparts/postcode/text()");
			break;
		case STREET:
			expr = xpath.compile("//reversegeocode/addressparts/road/text()");
			break;
		case TOWN:
			expr = xpath.compile("//reversegeocode/addressparts/town/text()");
			break;
		default:
			expr = xpath.compile("//reversegeocode/addressparts/town/text()");
			break;
		}
		
		Object result = expr.evaluate(parseXML(lon, lat), XPathConstants.NODESET);
		
		if(((NodeList) result).getLength() <= 0) {
			return "Keinen Ort gefunden.";
		}
		
		return ((NodeList) result).item(0).getTextContent();
	}
	
	
}
