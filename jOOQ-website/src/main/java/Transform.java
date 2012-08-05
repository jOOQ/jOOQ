/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
import static org.joox.JOOX.$;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.joox.Context;
import org.joox.Each;
import org.joox.Match;

/**
 * XSL transformation utility for HTML pages
 *
 * @author Lukas Eder
 */
@SuppressWarnings("resource")
public class Transform {
    private static final FopFactory fopFactory = FopFactory.newInstance();
    private static final String version = "2.5";

    public static void main(String[] args) throws Exception {
        System.out.println("Transforming multi-page manual");
        System.out.println("------------------------------");
        multiplePages();

        System.out.println();
        System.out.println("Transforming single-page manual");
        System.out.println("-------------------------------");
        singlePage();

        System.out.println();
        System.out.println("Transforming PDF manual");
        System.out.println("-------------------------------");
        pdf();
    }

    private static String file(String name) {
        if (version == null || version.equals("")) {
            return name;
        }

        return name.replaceAll("\\.\\w{3}$", "-" + version + "$0");
    }

    private static String path(String path) {
        return root() + path;
    }

    private static String root() {
        if (version == null || version.equals("")) {
            return "";
        }

        return "doc/" + version + "/";
    }

    private static String relative(String path) {
        return StringUtils.join(Collections.nCopies(path.split("/").length, ".."), "/") + "/";
    }

    private static void replaceVariables(Match manual) {
        manual.find("content").each(new Each() {
            @Override
            public void each(Context context) {
                String content = $(context.element()).content();
                boolean changed = false;

                if (content.contains("{jooq-version}")) {
                    content = content.replace("{jooq-version}", version + ".0");
                    changed = true;
                }

                if (changed) {
                    $(context.element()).content(content);
                }
            }
        });
    }

    public static void multiplePages() throws Exception {
        InputStream isXML = Transform.class.getResourceAsStream(file("manual.xml"));
        InputStream isXSL = Transform.class.getResourceAsStream("html-pages.xsl");

        StreamSource xsl = new StreamSource(isXSL);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsl);

        Match manual = $(isXML);
        replaceVariables(manual);

        List<String> ids = manual.find("section").ids();
        HashSet<String> uniqueIds = new HashSet<String>(ids);

        if (ids.size() != uniqueIds.size()) {
            for (String id : uniqueIds) {
                ids.remove(id);
            }

            throw new Exception("Duplicate section ids found! " + ids);
        }

        int blanks = 0, completed = 0;
        for (Match section : manual.find("section").each()) {
            Match sections = section.add(section.parents("section")).reverse();

            String path = path(StringUtils.join(sections.ids(), "/"));
            String relativePath = relative(path);
            String root = root();
            File dir = new File(path);
            dir.mkdirs();

            boolean blank = StringUtils.isBlank(section.find("content").text());
            if (blank) {
                blanks++;
            }
            else {
                completed++;
            }

            System.out.print("[");
            System.out.print(blank ? " " : "x");
            System.out.println("] Transforming section " + path);
            File file = new File(dir, "index.php");
            file.delete();
            FileOutputStream out = new FileOutputStream(file);

            Source source = new DOMSource(manual.document());
            Result target = new StreamResult(out);

            transformer.setParameter("sectionID", section.id());
            transformer.setParameter("relativePath", relativePath);
            transformer.setParameter("root", root);
            transformer.transform(source, target);

            out.close();
        }
        System.out.println("    Completed sections : " + completed + " / " + (blanks + completed) + " (" + (100 * completed / (blanks + completed)) + "%)");
    }

    public static void singlePage() throws Exception {
        InputStream isXML = Transform.class.getResourceAsStream(file("manual.xml"));
        InputStream isXSL = Transform.class.getResourceAsStream("html-page.xsl");

        StreamSource xsl = new StreamSource(isXSL);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsl);

        String relativePath = relative(path("manual-single-page"));
        String root = root();
        Match manual = $(isXML);
        replaceVariables(manual);

        File dir = new File(path("manual-single-page"));
        dir.mkdirs();

        System.out.println("Transforming manual");
        File file = new File(dir, "index.php");
        file.delete();
        FileOutputStream out = new FileOutputStream(file);

        Source source = new DOMSource(manual.document());
        Result target = new StreamResult(out);

        transformer.setParameter("relativePath", relativePath);
        transformer.setParameter("root", root);
        transformer.transform(source, target);

        out.close();
    }

    public static void pdf() throws Exception {
        if (true) return;

        // XML -> FO
        // ---------------------------------------------------------------------
        System.out.println("Transforming XML -> FO");
        InputStream isXML = Transform.class.getResourceAsStream(file("manual.xml"));
        InputStream isXSL = Transform.class.getResourceAsStream("pdf.xsl");

        StreamSource xsl = new StreamSource(isXSL);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsl);

        Match manual = $(isXML);
        replaceVariables(manual);

        File dir = new File(path("manual-pdf"));
        dir.mkdirs();
        FileOutputStream fout = new FileOutputStream(new File(dir, file("jOOQ-manual.fo.xml")));

        Source source = new DOMSource(manual.document());
        Result target = new StreamResult(fout);

        transformer.transform(source, target);
        fout.close();

        // FO -> PDF
        // ---------------------------------------------------------------------
        // See an example about how to do it here:
        // http://svn.apache.org/viewvc/xmlgraphics/fop/trunk/examples/embedding/java/embedding/ExampleFO2PDF.java?view=markup
        System.out.println("Transforming FO -> PDF");

        OutputStream out = null;

        fopFactory.setUserConfig(new File("C:/Users/lukas/workspace/jOOQ-website/src/main/resources/fop.config.xml"));
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // configure foUserAgent as desired

        // Setup output stream.  Note: Using BufferedOutputStream
        // for performance reasons (helpful with FileOutputStreams).
        out = new FileOutputStream(new File(dir, file("jOOQ-manual.pdf")));
        out = new BufferedOutputStream(out);

        // Construct fop with desired output format
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

        // Setup JAXP using identity transformer
        transformer = factory.newTransformer(); // identity transformer

        // Setup input stream
        Source src = new StreamSource(new File(dir, file("jOOQ-manual.fo.xml")));

        // Resulting SAX events (the generated FO) must be piped through to FOP
        Result res = new SAXResult(fop.getDefaultHandler());

        // Start XSLT transformation and FOP processing
        transformer.transform(src, res);

        out.flush();
        out.close();

        // Open the PDF and check it
        Runtime.getRuntime().exec(new String[] {
                "C:\\Program Files (x86)\\Adobe\\Reader 9.0\\Reader\\AcroRd32.exe",
                file("C:\\Users\\lukas\\workspace\\jOOQ-website\\manual-pdf\\jOOQ-manual.pdf") });
    }
}
