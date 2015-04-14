/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.tables.pojos;


import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.jooq.test.oracle.generatedclasses.sys.udt.pojos.Xmltype;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "T_EXOTIC_TYPES", schema = "TEST")
public class TExoticTypes implements Serializable {

	private static final long serialVersionUID = -657858464;

	private Integer      id;
	private UUID         uu;
	private Serializable javaIoSerializable;
	private String       plainSqlConverterXml;
	private String       plainSqlBindingXml;
	private Xmltype      oracleXmlAsIs;
	private Xmltype      oracleXmlAsDocument;

	public TExoticTypes() {}

	public TExoticTypes(TExoticTypes value) {
		this.id = value.id;
		this.uu = value.uu;
		this.javaIoSerializable = value.javaIoSerializable;
		this.plainSqlConverterXml = value.plainSqlConverterXml;
		this.plainSqlBindingXml = value.plainSqlBindingXml;
		this.oracleXmlAsIs = value.oracleXmlAsIs;
		this.oracleXmlAsDocument = value.oracleXmlAsDocument;
	}

	public TExoticTypes(
		Integer      id,
		UUID         uu,
		Serializable javaIoSerializable,
		String       plainSqlConverterXml,
		String       plainSqlBindingXml,
		Xmltype      oracleXmlAsIs,
		Xmltype      oracleXmlAsDocument
	) {
		this.id = id;
		this.uu = uu;
		this.javaIoSerializable = javaIoSerializable;
		this.plainSqlConverterXml = plainSqlConverterXml;
		this.plainSqlBindingXml = plainSqlBindingXml;
		this.oracleXmlAsIs = oracleXmlAsIs;
		this.oracleXmlAsDocument = oracleXmlAsDocument;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 7)
	@NotNull
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "UU")
	public UUID getUu() {
		return this.uu;
	}

	public void setUu(UUID uu) {
		this.uu = uu;
	}

	@Column(name = "JAVA_IO_SERIALIZABLE")
	public Serializable getJavaIoSerializable() {
		return this.javaIoSerializable;
	}

	public void setJavaIoSerializable(Serializable javaIoSerializable) {
		this.javaIoSerializable = javaIoSerializable;
	}

	@Column(name = "PLAIN_SQL_CONVERTER_XML")
	public String getPlainSqlConverterXml() {
		return this.plainSqlConverterXml;
	}

	public void setPlainSqlConverterXml(String plainSqlConverterXml) {
		this.plainSqlConverterXml = plainSqlConverterXml;
	}

	@Column(name = "PLAIN_SQL_BINDING_XML")
	public String getPlainSqlBindingXml() {
		return this.plainSqlBindingXml;
	}

	public void setPlainSqlBindingXml(String plainSqlBindingXml) {
		this.plainSqlBindingXml = plainSqlBindingXml;
	}

	@Column(name = "ORACLE_XML_AS_IS")
	public Xmltype getOracleXmlAsIs() {
		return this.oracleXmlAsIs;
	}

	public void setOracleXmlAsIs(Xmltype oracleXmlAsIs) {
		this.oracleXmlAsIs = oracleXmlAsIs;
	}

	@Column(name = "ORACLE_XML_AS_DOCUMENT")
	public Xmltype getOracleXmlAsDocument() {
		return this.oracleXmlAsDocument;
	}

	public void setOracleXmlAsDocument(Xmltype oracleXmlAsDocument) {
		this.oracleXmlAsDocument = oracleXmlAsDocument;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TExoticTypes other = (TExoticTypes) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (uu == null) {
			if (other.uu != null)
				return false;
		}
		else if (!uu.equals(other.uu))
			return false;
		if (javaIoSerializable == null) {
			if (other.javaIoSerializable != null)
				return false;
		}
		else if (!javaIoSerializable.equals(other.javaIoSerializable))
			return false;
		if (plainSqlConverterXml == null) {
			if (other.plainSqlConverterXml != null)
				return false;
		}
		else if (!plainSqlConverterXml.equals(other.plainSqlConverterXml))
			return false;
		if (plainSqlBindingXml == null) {
			if (other.plainSqlBindingXml != null)
				return false;
		}
		else if (!plainSqlBindingXml.equals(other.plainSqlBindingXml))
			return false;
		if (oracleXmlAsIs == null) {
			if (other.oracleXmlAsIs != null)
				return false;
		}
		else if (!oracleXmlAsIs.equals(other.oracleXmlAsIs))
			return false;
		if (oracleXmlAsDocument == null) {
			if (other.oracleXmlAsDocument != null)
				return false;
		}
		else if (!oracleXmlAsDocument.equals(other.oracleXmlAsDocument))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((uu == null) ? 0 : uu.hashCode());
		result = prime * result + ((javaIoSerializable == null) ? 0 : javaIoSerializable.hashCode());
		result = prime * result + ((plainSqlConverterXml == null) ? 0 : plainSqlConverterXml.hashCode());
		result = prime * result + ((plainSqlBindingXml == null) ? 0 : plainSqlBindingXml.hashCode());
		result = prime * result + ((oracleXmlAsIs == null) ? 0 : oracleXmlAsIs.hashCode());
		result = prime * result + ((oracleXmlAsDocument == null) ? 0 : oracleXmlAsDocument.hashCode());
		return result;
	}
}
