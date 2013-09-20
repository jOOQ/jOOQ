/**
 * This class is generated by jOOQ
 */
package org.jooq.test.mysql2.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "t_booleans", schema = "test2")
public class TBooleans implements java.io.Serializable {

	private static final long serialVersionUID = 832886014;

	private java.lang.Integer                            id;
	private org.jooq.test._.converters.Boolean_10        oneZero;
	private org.jooq.test._.converters.Boolean_TF_LC     trueFalseLc;
	private org.jooq.test._.converters.Boolean_TF_UC     trueFalseUc;
	private org.jooq.test._.converters.Boolean_YES_NO_LC yesNoLc;
	private org.jooq.test._.converters.Boolean_YES_NO_UC yesNoUc;
	private org.jooq.test._.converters.Boolean_YN_LC     yNLc;
	private org.jooq.test._.converters.Boolean_YN_UC     yNUc;
	private java.lang.Boolean                            vcBoolean;
	private java.lang.Boolean                            cBoolean;
	private java.lang.Boolean                            nBoolean;

	public TBooleans() {}

	public TBooleans(
		java.lang.Integer                            id,
		org.jooq.test._.converters.Boolean_10        oneZero,
		org.jooq.test._.converters.Boolean_TF_LC     trueFalseLc,
		org.jooq.test._.converters.Boolean_TF_UC     trueFalseUc,
		org.jooq.test._.converters.Boolean_YES_NO_LC yesNoLc,
		org.jooq.test._.converters.Boolean_YES_NO_UC yesNoUc,
		org.jooq.test._.converters.Boolean_YN_LC     yNLc,
		org.jooq.test._.converters.Boolean_YN_UC     yNUc,
		java.lang.Boolean                            vcBoolean,
		java.lang.Boolean                            cBoolean,
		java.lang.Boolean                            nBoolean
	) {
		this.id = id;
		this.oneZero = oneZero;
		this.trueFalseLc = trueFalseLc;
		this.trueFalseUc = trueFalseUc;
		this.yesNoLc = yesNoLc;
		this.yesNoUc = yesNoUc;
		this.yNLc = yNLc;
		this.yNUc = yNUc;
		this.vcBoolean = vcBoolean;
		this.cBoolean = cBoolean;
		this.nBoolean = nBoolean;
	}

	@javax.persistence.Id
	@javax.persistence.Column(name = "id", unique = true, nullable = false, precision = 10)
	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "one_zero", precision = 10)
	public org.jooq.test._.converters.Boolean_10 getOneZero() {
		return this.oneZero;
	}

	public void setOneZero(org.jooq.test._.converters.Boolean_10 oneZero) {
		this.oneZero = oneZero;
	}

	@javax.persistence.Column(name = "true_false_lc", length = 5)
	public org.jooq.test._.converters.Boolean_TF_LC getTrueFalseLc() {
		return this.trueFalseLc;
	}

	public void setTrueFalseLc(org.jooq.test._.converters.Boolean_TF_LC trueFalseLc) {
		this.trueFalseLc = trueFalseLc;
	}

	@javax.persistence.Column(name = "true_false_uc", length = 5)
	public org.jooq.test._.converters.Boolean_TF_UC getTrueFalseUc() {
		return this.trueFalseUc;
	}

	public void setTrueFalseUc(org.jooq.test._.converters.Boolean_TF_UC trueFalseUc) {
		this.trueFalseUc = trueFalseUc;
	}

	@javax.persistence.Column(name = "yes_no_lc", length = 3)
	public org.jooq.test._.converters.Boolean_YES_NO_LC getYesNoLc() {
		return this.yesNoLc;
	}

	public void setYesNoLc(org.jooq.test._.converters.Boolean_YES_NO_LC yesNoLc) {
		this.yesNoLc = yesNoLc;
	}

	@javax.persistence.Column(name = "yes_no_uc", length = 3)
	public org.jooq.test._.converters.Boolean_YES_NO_UC getYesNoUc() {
		return this.yesNoUc;
	}

	public void setYesNoUc(org.jooq.test._.converters.Boolean_YES_NO_UC yesNoUc) {
		this.yesNoUc = yesNoUc;
	}

	@javax.persistence.Column(name = "y_n_lc", length = 1)
	public org.jooq.test._.converters.Boolean_YN_LC getYNLc() {
		return this.yNLc;
	}

	public void setYNLc(org.jooq.test._.converters.Boolean_YN_LC yNLc) {
		this.yNLc = yNLc;
	}

	@javax.persistence.Column(name = "y_n_uc", length = 1)
	public org.jooq.test._.converters.Boolean_YN_UC getYNUc() {
		return this.yNUc;
	}

	public void setYNUc(org.jooq.test._.converters.Boolean_YN_UC yNUc) {
		this.yNUc = yNUc;
	}

	@javax.persistence.Column(name = "vc_boolean", length = 1)
	public java.lang.Boolean getVcBoolean() {
		return this.vcBoolean;
	}

	public void setVcBoolean(java.lang.Boolean vcBoolean) {
		this.vcBoolean = vcBoolean;
	}

	@javax.persistence.Column(name = "c_boolean", length = 1)
	public java.lang.Boolean getCBoolean() {
		return this.cBoolean;
	}

	public void setCBoolean(java.lang.Boolean cBoolean) {
		this.cBoolean = cBoolean;
	}

	@javax.persistence.Column(name = "n_boolean", precision = 10)
	public java.lang.Boolean getNBoolean() {
		return this.nBoolean;
	}

	public void setNBoolean(java.lang.Boolean nBoolean) {
		this.nBoolean = nBoolean;
	}
}
