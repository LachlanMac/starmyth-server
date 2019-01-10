package com.pineconeindustries.server;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.pineconeindustries.server.config.Settings;

import javax.xml.parsers.*;
import java.io.*;

public class ConfigReader {

	public static void readServerSettings() {

		File file = new File("config/serversettings.xml");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("ServerSettings");

			for (int i = 0; i < nList.getLength(); i++) {

				Node n = nList.item(i);

				if (n.getNodeType() == Node.ELEMENT_NODE) {

					Element e = (Element) n;

					Settings.SERVER_IP = e.getElementsByTagName("GameServerIP").item(0).getTextContent();
					Settings.WEB_SERVER_IP = e.getElementsByTagName("WebServerIP").item(0).getTextContent();
					Settings.WEB_SERVER_PORT = Integer
							.parseInt(e.getElementsByTagName("WebServerPort").item(0).getTextContent());

				}

			}

			NodeList dbNlist = doc.getElementsByTagName("DatabaseSettings");

			for (int i = 0; i < dbNlist.getLength(); i++) {

				Node n = dbNlist.item(i);

				if (n.getNodeType() == Node.ELEMENT_NODE) {

					Element e = (Element) n;

					Settings.DATABASE_PATH = e.getElementsByTagName("DatabasePath").item(0).getTextContent();

				}

			}

			NodeList logNlist = doc.getElementsByTagName("LoggingSettings");

			for (int i = 0; i < logNlist.getLength(); i++) {

				Node n = logNlist.item(i);

				if (n.getNodeType() == Node.ELEMENT_NODE) {

					Element e = (Element) n;
					Settings.LOGGING_PATH = e.getElementsByTagName("LoggingPath").item(0).getTextContent();
					String logging = e.getElementsByTagName("Logging").item(0).getTextContent();
					if (logging.equals("true")) {
						Settings.LOGGING = true;
					}

				}

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {

		}

	}

}
