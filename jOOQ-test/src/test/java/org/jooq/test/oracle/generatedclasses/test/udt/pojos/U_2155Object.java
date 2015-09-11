/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.udt.pojos;


import java.io.Serializable;
import java.time.LocalDate;

import org.jooq.test.oracle.generatedclasses.test.udt.records.U_2155ArrayRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class U_2155Object implements Serializable {

	private static final long serialVersionUID = 1024828968;

	private LocalDate         d;
	private U_2155ArrayRecord a;

	public U_2155Object() {}

	public U_2155Object(U_2155Object value) {
		this.d = value.d;
		this.a = value.a;
	}

	public U_2155Object(
		LocalDate         d,
		U_2155ArrayRecord a
	) {
		this.d = d;
		this.a = a;
	}

	public LocalDate getD() {
		return this.d;
	}

	public void setD(LocalDate d) {
		this.d = d;
	}

	public U_2155ArrayRecord getA() {
		return this.a;
	}

	public void setA(U_2155ArrayRecord a) {
		this.a = a;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final U_2155Object other = (U_2155Object) obj;
		if (d == null) {
			if (other.d != null)
				return false;
		}
		else if (!d.equals(other.d))
			return false;
		if (a == null) {
			if (other.a != null)
				return false;
		}
		else if (!a.equals(other.a))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("U_2155Object (");

		sb.append(d);
		sb.append(", ").append(a);

		sb.append(")");
		return sb.toString();
	}
}
