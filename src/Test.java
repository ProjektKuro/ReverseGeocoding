import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class Test {

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
		// TODO Auto-generated method stub
		Converter c = new Converter();
		System.out.println(c.getLocationByLonLat(44.0229224, 22.8277253, SearchType.COUNTRY));
	}
}