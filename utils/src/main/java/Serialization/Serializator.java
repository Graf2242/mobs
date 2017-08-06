package Serialization;

import Reflection.ReflectionHelper;
import graf.server.Base.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Field;

public class Serializator {
    public static void serializeToFileBin(Object o, String path) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Resource deserializeXmlFile(String path) {
        File file = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        Object obj = null;
        try {
            builder = dbf.newDocumentBuilder();
            doc = builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        Element root;
        if (doc != null) {
            root = doc.getDocumentElement();
            obj = ReflectionHelper.createInstance(root.getNodeName());
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node item = nodes.item(i);
                String value = item.getNodeName();
                if (value.equals("#text")) {
                    continue;
                }
                ReflectionHelper.setField(obj, value, nodes.item(i).getTextContent());
            }
        }
        return (Resource) obj;
    }

    public static void serializeXmlFile(Object o, String path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        FileOutputStream fos = null;
        try {
            File f = new File(path);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element rootElement = document.createElement(o.getClass().getName());
            document.appendChild(rootElement);
            Node n;
            for (Field field : ReflectionHelper.getFields(o)) {
                n = document.createElement(field.getName());
                n.setTextContent(String.valueOf(ReflectionHelper.getFieldValue(o, field)));
                rootElement.appendChild(n);
            }

            // Use a Transformer for output
            TransformerFactory tFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            fos = new FileOutputStream(f);

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(fos);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException | FileNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> T deserializeBinFile(String path) {
        FileInputStream fis;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
        } catch (Exception ignored) {
        }
        T resource = null;
        if (ois != null) {
            try {
                //noinspection unchecked
                resource = (T) ois.readObject();
                ois.close();
            } catch (Exception ignored) {
            }
        }
        return resource;
    }
}
