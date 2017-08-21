package Serialization;

import Reflection.ReflectionHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.Resource;

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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

    public static void main(String[] args) {
        System.out.println((String) deserializeString("rO0ABXNyAC9NZXNzYWdlU3lzdGVtLm1lc3NhZ2VzLkZyb250ZW5kLkZVcGRhdGVTZXNzaW9ucxtJwzakza/qAgAESQAEdHlwZUwACHNlc3Npb25zdAAPTGphdmEvdXRpbC9TZXQ7TAAGc3RhdHVzdAAcTGZyb250ZW5kL1VzZXJTZXNzaW9uU3RhdHVzO0wABnVzZXJJZHQAEExqYXZhL2xhbmcvTG9uZzt4cgA4TWVzc2FnZVN5c3RlbS5tZXNzYWdlcy5Gcm9udGVuZC5fRnJvbnRlbmRNZXNzYWdlVGVtcGxhdGWJmA+II9pTtwIAAHhyABVtYXN0ZXJTZXJ2aWNlLk1lc3NhZ2X6R90uNm7KmgIAAkwABGZyb210AB1MbWFzdGVyU2VydmljZS9ub2Rlcy9BZGRyZXNzO0wAAnRvdAARTGphdmEvbGFuZy9DbGFzczt4cHNyABttYXN0ZXJTZXJ2aWNlLm5vZGVzLkFkZHJlc3MpUrLVMkxo4gIAAUwAAmlkdAATTGphdmEvbGFuZy9JbnRlZ2VyO3hwc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAF2cgARZnJvbnRlbmQuRnJvbnRlbmQ0qvGSl6UdWQIAAHhwAAAAAXNyABFqYXZhLnV0aWwuSGFzaFNldLpEhZWWuLc0AwAAeHB3DAAAABA/QAAAAAAAAXNyAB1tYWluLkdhbWVNZWNoYW5pY3NTZXNzaW9uSW1wbMsLcFWF8LB5AgACTAAJc3RhcnRUaW1lcQB+AANMAAd1c2VySWRzcQB+AAF4cHNyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cQB+AA0AAAFd8Yw7OnNxAH4AEXcMAAAAAj9AAAAAAAABc3EAfgAVAAAAAAAAAAl4eH5yABpmcm9udGVuZC5Vc2VyU2Vzc2lvblN0YXR1cwAAAAAAAAAAEgAAeHIADmphdmEubGFuZy5FbnVtAAAAAAAAAAASAAB4cHQABUZJR0hUcA=="));
    }


    public static <T> T deserializeString(String message) {
        byte[] messageBytes = Base64.getDecoder().decode(message);
        ByteArrayInputStream bais = new ByteArrayInputStream(messageBytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (Exception ignored) {
            System.out.println(message);
            ignored.printStackTrace();
        }
        T resource = null;
        if (ois != null) {
            try {
                //noinspection unchecked
                resource = (T) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resource;
    }
}