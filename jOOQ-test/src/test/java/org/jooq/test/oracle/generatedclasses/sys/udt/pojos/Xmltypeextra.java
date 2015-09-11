/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.sys.udt.pojos;


import java.io.Serializable;

import org.jooq.test.oracle.generatedclasses.sys.udt.records.XmltypepiRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Xmltypeextra implements Serializable {

	private static final long serialVersionUID = -126245371;

	private XmltypepiRecord namespaces;
	private XmltypepiRecord extradata;

	public Xmltypeextra() {}

	public Xmltypeextra(Xmltypeextra value) {
		this.namespaces = value.namespaces;
		this.extradata = value.extradata;
	}

	public Xmltypeextra(
		XmltypepiRecord namespaces,
		XmltypepiRecord extradata
	) {
		this.namespaces = namespaces;
		this.extradata = extradata;
	}

	public XmltypepiRecord getNamespaces() {
		return this.namespaces;
	}

	public void setNamespaces(XmltypepiRecord namespaces) {
		this.namespaces = namespaces;
	}

	public XmltypepiRecord getExtradata() {
		return this.extradata;
	}

	public void setExtradata(XmltypepiRecord extradata) {
		this.extradata = extradata;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Xmltypeextra other = (Xmltypeextra) obj;
		if (namespaces == null) {
			if (other.namespaces != null)
				return false;
		}
		else if (!namespaces.equals(other.namespaces))
			return false;
		if (extradata == null) {
			if (other.extradata != null)
				return false;
		}
		else if (!extradata.equals(other.extradata))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((namespaces == null) ? 0 : namespaces.hashCode());
		result = prime * result + ((extradata == null) ? 0 : extradata.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Xmltypeextra (");

		sb.append(namespaces);
		sb.append(", ").append(extradata);

		sb.append(")");
		return sb.toString();
	}
}
