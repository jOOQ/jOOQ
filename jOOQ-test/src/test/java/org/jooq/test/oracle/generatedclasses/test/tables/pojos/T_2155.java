/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.tables.pojos;


import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.jooq.test.oracle.generatedclasses.test.udt.pojos.U_2155Object;
import org.jooq.test.oracle.generatedclasses.test.udt.records.U_2155ArrayRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "T_2155", schema = "TEST")
public class T_2155 implements Serializable {

	private static final long serialVersionUID = -362326278;

	private Integer           id;
	private LocalDate         d1;
	private U_2155Object      d2;
	private U_2155ArrayRecord d3;

	public T_2155() {}

	public T_2155(T_2155 value) {
		this.id = value.id;
		this.d1 = value.d1;
		this.d2 = value.d2;
		this.d3 = value.d3;
	}

	public T_2155(
		Integer           id,
		LocalDate         d1,
		U_2155Object      d2,
		U_2155ArrayRecord d3
	) {
		this.id = id;
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
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

	@Column(name = "D1")
	public LocalDate getD1() {
		return this.d1;
	}

	public void setD1(LocalDate d1) {
		this.d1 = d1;
	}

	@Column(name = "D2")
	public U_2155Object getD2() {
		return this.d2;
	}

	public void setD2(U_2155Object d2) {
		this.d2 = d2;
	}

	@Column(name = "D3")
	public U_2155ArrayRecord getD3() {
		return this.d3;
	}

	public void setD3(U_2155ArrayRecord d3) {
		this.d3 = d3;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final T_2155 other = (T_2155) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (d1 == null) {
			if (other.d1 != null)
				return false;
		}
		else if (!d1.equals(other.d1))
			return false;
		if (d2 == null) {
			if (other.d2 != null)
				return false;
		}
		else if (!d2.equals(other.d2))
			return false;
		if (d3 == null) {
			if (other.d3 != null)
				return false;
		}
		else if (!d3.equals(other.d3))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((d1 == null) ? 0 : d1.hashCode());
		result = prime * result + ((d2 == null) ? 0 : d2.hashCode());
		result = prime * result + ((d3 == null) ? 0 : d3.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("T_2155 (");

		sb.append(id);
		sb.append(", ").append(d1);
		sb.append(", ").append(d2);
		sb.append(", ").append(d3);

		sb.append(")");
		return sb.toString();
	}
}
