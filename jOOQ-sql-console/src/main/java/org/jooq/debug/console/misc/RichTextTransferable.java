/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
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
package org.jooq.debug.console.misc;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Christopher Deckers & others
 */
public class RichTextTransferable implements Transferable {

	private static DataFlavor[] htmlFlavors;
	private static DataFlavor[] plainFlavors;

	static {
		try {
			htmlFlavors = new DataFlavor[] {
					new DataFlavor("text/html;class=java.lang.String"),
					new DataFlavor("text/html;class=java.io.Reader"),
			};
			plainFlavors = new DataFlavor[] {
					new DataFlavor("text/plain;class=java.lang.String"),
					new DataFlavor("text/plain;class=java.io.Reader"),
					new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.lang.String"),
					DataFlavor.stringFlavor,
			};
		} catch (ClassNotFoundException cle) {
			System.err.println("Failed to initialize RichTextTransferable!");
		}
	}

	private String htmlContent;
	private String plainContent;

	public RichTextTransferable(String htmlContent, String plainContent) {
		this.htmlContent = htmlContent;
		this.plainContent = plainContent;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		for(DataFlavor flavor_: htmlFlavors) {
			if(flavor_.equals(flavor)) {
				if(String.class.equals(flavor.getRepresentationClass())) {
					return htmlContent != null? htmlContent: "";
				}
				if(Reader.class.equals(flavor.getRepresentationClass())) {
					return new StringReader(htmlContent != null? htmlContent: "");
				}
				throw new UnsupportedFlavorException(flavor);
			}
		}
		for(DataFlavor flavor_: plainFlavors) {
			if(flavor_.equals(flavor)) {
				if(String.class.equals(flavor.getRepresentationClass())) {
					return plainContent != null? plainContent: "";
				}
				if(Reader.class.equals(flavor.getRepresentationClass())) {
					return new StringReader(plainContent != null? plainContent: "");
				}
				throw new UnsupportedFlavorException(flavor);
			}
		}
		throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		int htmlCount = htmlContent != null? htmlFlavors.length : 0;
		int plainCount = plainContent != null? plainFlavors.length: 0;
		DataFlavor[] flavors = new DataFlavor[htmlCount + plainCount];
		if(htmlCount > 0) {
			System.arraycopy(htmlFlavors, 0, flavors, 0, htmlCount);
		}
		if(plainCount > 0) {
			System.arraycopy(plainFlavors, 0, flavors, htmlCount, plainCount);
		}
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for(DataFlavor flavor_: getTransferDataFlavors()) {
			if(flavor_.equals(flavor)) {
				return true;
			}
		}
		return false;
	}

}
