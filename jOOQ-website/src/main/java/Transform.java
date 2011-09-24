/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.io.File;
import java.io.InputStream;
import java.util.Collections;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.joox.Match;

/**
 * XSL transformation utility for HTML pages
 *
 * @author Lukas Eder
 */
public class Transform {
    public static void main(String[] args) throws Exception {
        System.out.println("Transforming multi-page manual");
        System.out.println("------------------------------");
        multiplePages();

        System.out.println();
        System.out.println("Transforming single-page manual");
        System.out.println("-------------------------------");
        singlePage();
    }


    public static void multiplePages() throws Exception {
        InputStream isXML = Transform.class.getResourceAsStream("manual.xml");
        InputStream isXSL = Transform.class.getResourceAsStream("html-pages.xsl");

        StreamSource xsl = new StreamSource(isXSL);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsl);

        Match manual = $(isXML);
        for (Match section : manual.find("section").each()) {
            Match sections = section.add(section.parents("section")).reverse();

            String path = StringUtils.join(sections.ids(), "/");
            String relativePath = StringUtils.join(Collections.nCopies(sections.size(), ".."), "/") + "/";
            File dir = new File(path);
            dir.mkdirs();

            System.out.println("Transforming section " + path);

            Source source = new DOMSource(manual.document());
            Result target = new StreamResult(new File(dir, "index.php"));

            transformer.setParameter("sectionID", section.id());
            transformer.setParameter("relativePath", relativePath);
            transformer.transform(source, target);
        }
    }

    public static void singlePage() throws Exception {
        InputStream isXML = Transform.class.getResourceAsStream("manual.xml");
        InputStream isXSL = Transform.class.getResourceAsStream("html-page.xsl");

        StreamSource xsl = new StreamSource(isXSL);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsl);

        Match manual = $(isXML);

        File dir = new File("manual-single-page");
        dir.mkdirs();

        System.out.println("Transforming manual");

        Source source = new DOMSource(manual.document());
        Result target = new StreamResult(new File(dir, "index.php"));

        transformer.transform(source, target);
    }
}
