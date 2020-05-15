package com.company;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Main {
    public static void main(String[] args) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse("file.xml");
            XPathFactory pathFactory = XPathFactory.newInstance();
            XPath xpath = pathFactory.newXPath();
            //Клиенты с превышением лимита
            List<String> clients = getOverLimit(doc, xpath);
            System.out.print("Клиенты с превышением лимита: ");
            clients.forEach((c)->System.out.print(c+" "));
            System.out.println();

            //Клиенты из России
            clients = getClientsByCountry(doc, xpath, "Russia");
            System.out.print("Клиенты из России: ");
            clients.forEach((c)->System.out.print(c+" "));


        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getOverLimit(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            XPathExpression xExp = xpath.compile(String.format(
                    "//clients/client[account/balance > account/limit]/name"));
            NodeList nodes = (NodeList) xExp.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                list.add(nodes.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getClientsByCountry(Document doc, XPath xpath, String country) {
        List<String> list = new ArrayList<>();
        try {
            String str = String.format("//clients/client[country = '"+country+"']/name");
            XPathExpression xExp = xpath.compile(str);
            NodeList nodes = (NodeList) xExp.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                list.add(nodes.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }
}
