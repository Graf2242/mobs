package utils.Serialization;

import base.utils.Resource;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.Reflection.ReflectionHelper;
import utils.logger.LoggerImpl;

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
import java.util.Base64;
import java.util.Objects;

public class Serializator {

    public static void serializeToFileBin(Object o, String path) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            fos.close();
        } catch (IOException e) {
            LoggerImpl.getLogger().error(e);
        }
    }

    public static String serializeToString(Object o) throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            LoggerImpl.getLogger().error(e);
        }

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Resource deserializeXmlFile(String path) throws NoSuchFieldException, IllegalAccessException {
        File file = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        Object obj = null;
        try {
            builder = dbf.newDocumentBuilder();
            doc = builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LoggerImpl.getLogger().error(e);
        }
        Element root;
        if (doc != null) {
            root = doc.getDocumentElement();
            obj = ReflectionHelper.createInstance(root.getNodeName());
            if (obj == null) {
                return null;
            }
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node item = nodes.item(i);
                String value = item.getNodeName();
                if (value.equals("#text")) {
                    continue;
                }
                if (!Objects.equals(item.getChildNodes(), null)) {
                    recursiveReadXML(obj, item);
                }
                ReflectionHelper.setField(obj, value, nodes.item(i).getTextContent());
            }
        }
        return (Resource) obj;
    }

    private static void recursiveReadXML(Object obj, Node item) throws NoSuchFieldException, IllegalAccessException {
        NodeList inNodes = item.getChildNodes();
        Field field = obj.getClass().getDeclaredField(item.getNodeName());
        Object fieldValue = ReflectionHelper.getFieldValue(obj, field);
        for (int i = 0; i < inNodes.getLength(); i++) {
            Node item1 = inNodes.item(i);
            if (Objects.equals(item1.getChildNodes(), null)) {
                recursiveReadXML(obj, item1);
            }
            String nodeName = item1.getNodeName();
            if (nodeName.equals("#text")) {
                continue;
            }
            Field field1 = fieldValue.getClass().getDeclaredField(nodeName);
            ReflectionHelper.setField(fieldValue, field1.getName(), item1.getTextContent());
        }
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
            xmlWriteObj(o, document, rootElement);

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
            LoggerImpl.getLogger().error(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LoggerImpl.getLogger().error(e);
                }
            }
        }
    }

    private static void xmlWriteObj(Object o, Document document, Element rootElement) throws IllegalAccessException {
        Node n;
        for (Field field : ReflectionHelper.getFields(o)) {
            if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
                n = document.createElement(field.getName());
                n.setTextContent(String.valueOf(ReflectionHelper.getFieldValue(o, field)));
                rootElement.appendChild(n);
            } else {
                n = document.createElement(field.getName());
                rootElement.appendChild(n);
                xmlWriteObj(ReflectionHelper.getFieldValue(o, field), document, (Element) n);
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

    public static <T> T deserializeString(String message) {
        byte[] messageBytes = Base64.getDecoder().decode(message);
        ByteArrayInputStream bais = new ByteArrayInputStream(messageBytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (Exception ignored) {
            Logger logger = LoggerImpl.getLogger();
            logger.error(message);
            logger.error(ignored);
        }
        T resource = null;
        if (ois != null) {
            try {
                //noinspection unchecked
                resource = (T) ois.readObject();
                ois.close();
            } catch (Exception e) {
                LoggerImpl.getLogger().error(e);
            }
        }
        return resource;
    }
}