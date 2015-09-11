/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * An unused table in the same schema.
 * 
 * "Its comments contain special characters"
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "X_UNUSED", schema = "TEST", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"ID", "NAME"})
})
public class XUnused implements Serializable {

	private static final long serialVersionUID = -331158189;

	private Integer    id;
	private String     name;
	private BigInteger bigInteger;
	private Integer    idRef;
	private Integer    class_;
	private Integer    fields;
	private Integer    configuration;
	private Integer    uDT;
	private Integer    metaData;
	private Integer    type0;
	private Integer    primaryKey;
	private Integer    primarykey;
	private String     nameRef;
	private BigDecimal field_737;
	private Integer    msUnusedIdRef;
	private String     msUnusedNameRef;

	public XUnused() {}

	public XUnused(XUnused value) {
		this.id = value.id;
		this.name = value.name;
		this.bigInteger = value.bigInteger;
		this.idRef = value.idRef;
		this.class_ = value.class_;
		this.fields = value.fields;
		this.configuration = value.configuration;
		this.uDT = value.uDT;
		this.metaData = value.metaData;
		this.type0 = value.type0;
		this.primaryKey = value.primaryKey;
		this.primarykey = value.primarykey;
		this.nameRef = value.nameRef;
		this.field_737 = value.field_737;
		this.msUnusedIdRef = value.msUnusedIdRef;
		this.msUnusedNameRef = value.msUnusedNameRef;
	}

	public XUnused(
		Integer    id,
		String     name,
		BigInteger bigInteger,
		Integer    idRef,
		Integer    class_,
		Integer    fields,
		Integer    configuration,
		Integer    uDT,
		Integer    metaData,
		Integer    type0,
		Integer    primaryKey,
		Integer    primarykey,
		String     nameRef,
		BigDecimal field_737,
		Integer    msUnusedIdRef,
		String     msUnusedNameRef
	) {
		this.id = id;
		this.name = name;
		this.bigInteger = bigInteger;
		this.idRef = idRef;
		this.class_ = class_;
		this.fields = fields;
		this.configuration = configuration;
		this.uDT = uDT;
		this.metaData = metaData;
		this.type0 = type0;
		this.primaryKey = primaryKey;
		this.primarykey = primarykey;
		this.nameRef = nameRef;
		this.field_737 = field_737;
		this.msUnusedIdRef = msUnusedIdRef;
		this.msUnusedNameRef = msUnusedNameRef;
	}

	@Column(name = "ID", unique = true, nullable = false, precision = 7)
	@NotNull
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 10)
	@NotNull
	@Size(max = 10)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "BIG_INTEGER", precision = 38)
	public BigInteger getBigInteger() {
		return this.bigInteger;
	}

	public void setBigInteger(BigInteger bigInteger) {
		this.bigInteger = bigInteger;
	}

	@Column(name = "ID_REF", precision = 7)
	public Integer getIdRef() {
		return this.idRef;
	}

	public void setIdRef(Integer idRef) {
		this.idRef = idRef;
	}

	@Column(name = "CLASS", precision = 7)
	public Integer getClass_() {
		return this.class_;
	}

	public void setClass_(Integer class_) {
		this.class_ = class_;
	}

	@Column(name = "FIELDS", precision = 7)
	public Integer getFields() {
		return this.fields;
	}

	public void setFields(Integer fields) {
		this.fields = fields;
	}

	@Column(name = "CONFIGURATION", precision = 7)
	public Integer getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(Integer configuration) {
		this.configuration = configuration;
	}

	@Column(name = "U_D_T", precision = 7)
	public Integer getUDT() {
		return this.uDT;
	}

	public void setUDT(Integer uDT) {
		this.uDT = uDT;
	}

	@Column(name = "META_DATA", precision = 7)
	public Integer getMetaData() {
		return this.metaData;
	}

	public void setMetaData(Integer metaData) {
		this.metaData = metaData;
	}

	@Column(name = "TYPE0", precision = 7)
	public Integer getType0() {
		return this.type0;
	}

	public void setType0(Integer type0) {
		this.type0 = type0;
	}

	@Column(name = "PRIMARY_KEY", precision = 7)
	public Integer getPrimaryKey_() {
		return this.primaryKey;
	}

	public void setPrimaryKey_(Integer primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "PRIMARYKEY", precision = 7)
	public Integer getPrimarykey() {
		return this.primarykey;
	}

	public void setPrimarykey(Integer primarykey) {
		this.primarykey = primarykey;
	}

	@Column(name = "NAME_REF", length = 10)
	@Size(max = 10)
	public String getNameRef() {
		return this.nameRef;
	}

	public void setNameRef(String nameRef) {
		this.nameRef = nameRef;
	}

	@Column(name = "FIELD 737", precision = 25, scale = 2)
	public BigDecimal getField_737() {
		return this.field_737;
	}

	public void setField_737(BigDecimal field_737) {
		this.field_737 = field_737;
	}

	@Column(name = "MS_UNUSED_ID_REF", precision = 7)
	public Integer getMsUnusedIdRef() {
		return this.msUnusedIdRef;
	}

	public void setMsUnusedIdRef(Integer msUnusedIdRef) {
		this.msUnusedIdRef = msUnusedIdRef;
	}

	@Column(name = "MS_UNUSED_NAME_REF", length = 10)
	@Size(max = 10)
	public String getMsUnusedNameRef() {
		return this.msUnusedNameRef;
	}

	public void setMsUnusedNameRef(String msUnusedNameRef) {
		this.msUnusedNameRef = msUnusedNameRef;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final XUnused other = (XUnused) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (bigInteger == null) {
			if (other.bigInteger != null)
				return false;
		}
		else if (!bigInteger.equals(other.bigInteger))
			return false;
		if (idRef == null) {
			if (other.idRef != null)
				return false;
		}
		else if (!idRef.equals(other.idRef))
			return false;
		if (class_ == null) {
			if (other.class_ != null)
				return false;
		}
		else if (!class_.equals(other.class_))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		}
		else if (!fields.equals(other.fields))
			return false;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		}
		else if (!configuration.equals(other.configuration))
			return false;
		if (uDT == null) {
			if (other.uDT != null)
				return false;
		}
		else if (!uDT.equals(other.uDT))
			return false;
		if (metaData == null) {
			if (other.metaData != null)
				return false;
		}
		else if (!metaData.equals(other.metaData))
			return false;
		if (type0 == null) {
			if (other.type0 != null)
				return false;
		}
		else if (!type0.equals(other.type0))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		}
		else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (primarykey == null) {
			if (other.primarykey != null)
				return false;
		}
		else if (!primarykey.equals(other.primarykey))
			return false;
		if (nameRef == null) {
			if (other.nameRef != null)
				return false;
		}
		else if (!nameRef.equals(other.nameRef))
			return false;
		if (field_737 == null) {
			if (other.field_737 != null)
				return false;
		}
		else if (!field_737.equals(other.field_737))
			return false;
		if (msUnusedIdRef == null) {
			if (other.msUnusedIdRef != null)
				return false;
		}
		else if (!msUnusedIdRef.equals(other.msUnusedIdRef))
			return false;
		if (msUnusedNameRef == null) {
			if (other.msUnusedNameRef != null)
				return false;
		}
		else if (!msUnusedNameRef.equals(other.msUnusedNameRef))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((bigInteger == null) ? 0 : bigInteger.hashCode());
		result = prime * result + ((idRef == null) ? 0 : idRef.hashCode());
		result = prime * result + ((class_ == null) ? 0 : class_.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result + ((uDT == null) ? 0 : uDT.hashCode());
		result = prime * result + ((metaData == null) ? 0 : metaData.hashCode());
		result = prime * result + ((type0 == null) ? 0 : type0.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((primarykey == null) ? 0 : primarykey.hashCode());
		result = prime * result + ((nameRef == null) ? 0 : nameRef.hashCode());
		result = prime * result + ((field_737 == null) ? 0 : field_737.hashCode());
		result = prime * result + ((msUnusedIdRef == null) ? 0 : msUnusedIdRef.hashCode());
		result = prime * result + ((msUnusedNameRef == null) ? 0 : msUnusedNameRef.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("XUnused (");

		sb.append(id);
		sb.append(", ").append(name);
		sb.append(", ").append(bigInteger);
		sb.append(", ").append(idRef);
		sb.append(", ").append(class_);
		sb.append(", ").append(fields);
		sb.append(", ").append(configuration);
		sb.append(", ").append(uDT);
		sb.append(", ").append(metaData);
		sb.append(", ").append(type0);
		sb.append(", ").append(primaryKey);
		sb.append(", ").append(primarykey);
		sb.append(", ").append(nameRef);
		sb.append(", ").append(field_737);
		sb.append(", ").append(msUnusedIdRef);
		sb.append(", ").append(msUnusedNameRef);

		sb.append(")");
		return sb.toString();
	}
}
