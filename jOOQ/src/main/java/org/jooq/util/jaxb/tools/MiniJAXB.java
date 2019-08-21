/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.util.jaxb.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jooq.Constants;
import org.jooq.Internal;
import org.jooq.exception.ConfigurationException;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class allows for mashalling / unmarshalling XML content to jOOQ
 * configuration objects.
 * <p>
 * With jOOQ 3.12, the JAXB dependency has been removed in favour of this home
 * grown solution. Due to the modularisation that happened with JDK 9+ and the
 * removal of JAXB from the JDK 11+, it is unreasonable to leave the burden of
 * properly configuring transitive JAXB dependency to jOOQ users.
 *
 * @author Lukas Eder
 */
@Internal
public final class MiniJAXB {

    private static final JooqLogger log = JooqLogger.getLogger(MiniJAXB.class);
    private static final Map<String, String> PROVIDED_SCHEMAS;

    static {
        PROVIDED_SCHEMAS = new HashMap<>();
        PROVIDED_SCHEMAS.put(Constants.NS_CODEGEN, Constants.CP_CODEGEN);
        PROVIDED_SCHEMAS.put(Constants.NS_EXPORT,  Constants.CP_EXPORT);
        PROVIDED_SCHEMAS.put(Constants.NS_META,    Constants.CP_META);
        PROVIDED_SCHEMAS.put(Constants.NS_RUNTIME, Constants.CP_RUNTIME);
    }

    public static String marshal(XMLAppendable object) {
        StringWriter writer = new StringWriter();
        marshal(object, writer);
        return writer.toString();
    }

    public static void marshal(XMLAppendable object, OutputStream out) {
        marshal(object, new OutputStreamWriter(out));
    }

    public static void marshal(XMLAppendable object, Writer out) {
        try {
            XMLBuilder builder = XMLBuilder.formatting();
            XmlRootElement e = object.getClass().getAnnotation(XmlRootElement.class);
            if (e != null) {
                out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
                builder.append(e.name(), object);
            }
            else
                builder.append(object);

            builder.appendTo(out);
        }
        catch (Exception e) {
            throw new ConfigurationException("Cannot print object", e);
        }
    }

    public static <T extends XMLAppendable> T unmarshal(InputStream in, Class<T> type) {
        return unmarshal0(new InputSource(in), type);
    }

    public static <T extends XMLAppendable> T unmarshal(String xml, Class<T> type) {
        return unmarshal0(new InputSource(new StringReader(xml)), type);
    }

    public static <T extends XMLAppendable> T unmarshal(File xml, Class<T> type) {
        try {
            return unmarshal0(new InputSource(new FileInputStream(xml)), type);
        }
        catch (Exception e) {
            throw new ConfigurationException("Error while opening file", e);
        }
    }

    private static <T extends XMLAppendable> T unmarshal0(InputSource in, Class<T> type) {
        try {
            addDefaultNamespace(in, type);
            Document document = builder(type).parse(in);
            T result = Reflect.on(type).create().get();
            unmarshal0(result, document.getDocumentElement(), new IdentityHashMap<Class<?>, Map<String,Field>>());
            return result;
        }
        catch (Exception e) {
            throw new ConfigurationException("Error while reading xml", e);
        }
    }

    private static void addDefaultNamespace(InputSource in, Class<?> type)
        throws IOException {
        String namespace = getNamespace(type);
        if (namespace != null) {
            Reader reader;
            if (in.getCharacterStream() != null)
                reader = in.getCharacterStream();
            else
                reader = new InputStreamReader(in.getByteStream(),
                    in.getEncoding() != null ? in.getEncoding() : Charset.defaultCharset().name());
            StringWriter writer = new StringWriter();
            copyLarge(reader, writer);
            String xml = writer.toString();

            int startIdx = xml.indexOf('<');
            // skip over all processing instructions (like <?xml ...?>
            while (startIdx > 0 && xml.length() > startIdx + 1 && xml.charAt(startIdx + 1) == '?')
                startIdx = xml.indexOf('<', startIdx + 1);
            int endIdx = xml.indexOf('>', startIdx);
            if (!xml.substring(startIdx, endIdx).contains("xmlns")) {
                xml = xml.replaceFirst(
                    "<([a-z_]+)\\s*(/?>)",
                    "<$1 xmlns=\"" + namespace + "\"$2");
            }
            in.setCharacterStream(new StringReader(xml));
        }
    }

    private static long copyLarge(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[1024 * 4];
        long count = 0;
        int n = 0;
        while (-1 != (n = reader.read(buffer))) {
            writer.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private static void unmarshal0(Object result, Element element, Map<Class<?>, Map<String, Field>> fieldsByClass) throws Exception {
        if (result == null)
            return;

        Map<String, Field> map = fieldsByElementName(fieldsByClass, result.getClass());

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);

            if (item.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element childElement = (Element) item;
            Field child = map.get(childElement.getTagName());
            if (child == null)
                child = map.get(childElement.getLocalName());
            // skip unknown elements
            if (child == null)
                continue;

            XmlElementWrapper w = child.getAnnotation(XmlElementWrapper.class);
            XmlElement e = child.getAnnotation(XmlElement.class);
            XmlJavaTypeAdapter a = child.getAnnotation(XmlJavaTypeAdapter.class);

            String childName = child.getName();
            Class<?> childType = child.getType();

            if (List.class.isAssignableFrom(childType) && w != null && e != null) {
                List<Object> list = new ArrayList<Object>();
                unmarshalList0(list, childElement, e.name(), (Class<?>) ((ParameterizedType) child.getGenericType()).getActualTypeArguments()[0], fieldsByClass);
                Reflect.on(result).set(childName, list);
            }
            else if (childType.getAnnotation(XmlEnum.class) != null) {
                Reflect.on(result).set(childName, Reflect.onClass(childType).call("fromValue", childElement.getTextContent().trim()));
            }
            else if (childType.getAnnotation(XmlType.class) != null) {
                Object object = Reflect.on(childType).create().get();
                Reflect.on(result).set(childName, object);

                unmarshal0(object, childElement, fieldsByClass);
            }
            else if (a != null) {
                @SuppressWarnings("unchecked")
                XmlAdapter<Object, Object> adapter = a.value().getConstructor().newInstance();
                Reflect.on(result).set(childName, adapter.unmarshal(childElement.getTextContent().trim()));
            }
            else {
                Reflect.on(result).set(childName, Convert.convert(childElement.getTextContent().trim(), childType));
            }
        }
    }

    private static void unmarshalList0(List<Object> result, Element element, String name, Class<?> type, Map<Class<?>, Map<String, Field>> fieldsByClass) throws Exception {
        if (result == null)
            return;

        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node item = list.item(i);

            if (item.getNodeType() == Node.ELEMENT_NODE) {
                if (name.equals(((Element) item).getTagName()) || name.equals(((Element) item).getLocalName())) {
                    Object o = Reflect.on(type).create().get();
                    unmarshal0(o, (Element) item, fieldsByClass);
                    result.add(o);
                }
            }
        }
    }

    private static Map<String, Field> fieldsByElementName(Map<Class<?>, Map<String, Field>> fieldsByClass, Class<?> type) {
        Map<String, Field> result = fieldsByClass.get(type);
        if (result == null) {
            result = new HashMap<String, Field>();
            fieldsByClass.put(type, result);

            for (Field child : type.getDeclaredFields()) {
                int modifiers = child.getModifiers();
                if (Modifier.isFinal(modifiers) ||
                    Modifier.isStatic(modifiers))
                    continue;

                XmlElementWrapper w = child.getAnnotation(XmlElementWrapper.class);
                XmlElement e = child.getAnnotation(XmlElement.class);

                String childName = child.getName();
                String childElementName = w != null
                    ? "##default".equals(w.name())
                        ? child.getName()
                        : w.name()
                    : e == null || "##default".equals(e.name())
                        ? childName
                        : e.name();

                result.put(childElementName, child);
            }
        }
        return result;
    }

    private static DocumentBuilder builder(Class<?> type) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // -----------------------------------------------------------------
            // [JOOX #136] FIX START: Prevent OWASP attack vectors
            try {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            }
            catch (ParserConfigurationException ignore) {}

            try {
                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            }
            catch (ParserConfigurationException ignore) {}

            try {
                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            }
            catch (ParserConfigurationException ignore) {}

            // [JOOX #149] Not implemented on Android
            try {
                factory.setXIncludeAware(false);
            }
            catch (UnsupportedOperationException ignore) {}

            // [#8918] log warnings for unknown elements
            String namespace = getNamespace(type);
            if (namespace != null)
                try {
                    Schema schema = getSchema(type, namespace);
                    factory.setSchema(schema);
                }
                catch (UnsupportedOperationException ignore) {}

            factory.setExpandEntityReferences(false);
            // [JOOX #136] FIX END
            // -----------------------------------------------------------------

            // [JOOX #9] [JOOX #107] In order to take advantage of namespace-related DOM
            // features, the internal builder should be namespace-aware
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    log.warn(exception);
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    log.warn(exception);
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    log.warn(exception);
                }
            });

            return builder;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getNamespace(Class<?> type) {
        if (type != null && type.getPackage() != null && type.getPackage().isAnnotationPresent(XmlSchema.class))
            return type.getPackage().getAnnotation(XmlSchema.class).namespace();
        return null;
    }

    private static Schema getSchema(Class<?> type, String namespace) {
        try {
            URL url;
            if (PROVIDED_SCHEMAS.containsKey(namespace))
                url = type.getResource(PROVIDED_SCHEMAS.get(namespace));
            else
                url = new URL(namespace);

            if (url != null) {
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = schemaFactory.newSchema(url);
                return schema;
            }
        }
        catch (MalformedURLException e) {
            log.warn("Failed to load schema for namespace " + namespace, e);
        }
        catch (SAXException e) {
            log.warn("Failed to load schema for namespace " + namespace, e);
        }
        return null;
    }

    /**
     * Appends a <code>second</code> JAXB annotated object to a
     * <code>first</code> one using Maven's
     * <code>combine.children="append"</code> semantics.
     *
     * @return The modified <code>first</code> argument.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T append(T first, T second) {
        if (first == null)
            return second;
        if (second == null)
            return first;

        Class<T> klass = (Class<T>) first.getClass();
        if (klass != second.getClass())
            throw new IllegalArgumentException("Can only append identical types");
        // [#8527] support enum types
        else if (klass.isEnum())
            return first;

        // We're assuming that XJC generated objects are all in the same package
        Package pkg = klass.getPackage();
        try {
            T defaults = klass.getConstructor().newInstance();

            for (Method setter : klass.getMethods()) {
                if (setter.getName().startsWith("set")) {
                    Method getter;

                    try {
                        getter = klass.getMethod("get" + setter.getName().substring(3));
                    }
                    catch (NoSuchMethodException e) {
                        getter = klass.getMethod("is" + setter.getName().substring(3));
                    }

                    Class<?> childType = setter.getParameterTypes()[0];
                    Object firstChild = getter.invoke(first);
                    Object secondChild = getter.invoke(second);
                    Object defaultChild = getter.invoke(defaults);

                    if (Collection.class.isAssignableFrom(childType))
                        ((List) firstChild).addAll((List) secondChild);
                    else if (secondChild != null && (firstChild == null || firstChild.equals(defaultChild)))
                        setter.invoke(first, secondChild);
                    else if (secondChild != null && pkg == childType.getPackage())
                        append(firstChild, secondChild);
                    else
                        ; // All other types cannot be merged
                }
            }
        }
        catch (Exception e) {
            throw new ReflectException(e);
        }

        return first;
    }
}
