package org.jooq.debugger.console.misc;

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
