/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TBooleans implements org.jooq.test.postgres.generatedclasses.tables.interfaces.ITBooleans {

	private static final long serialVersionUID = 1331243344;

	private final java.lang.Integer                              id;
	private final org.jooq.test.all.converters.Boolean_10        oneZero;
	private final org.jooq.test.all.converters.Boolean_TF_LC     trueFalseLc;
	private final org.jooq.test.all.converters.Boolean_TF_UC     trueFalseUc;
	private final org.jooq.test.all.converters.Boolean_YES_NO_LC yesNoLc;
	private final org.jooq.test.all.converters.Boolean_YES_NO_UC yesNoUc;
	private final org.jooq.test.all.converters.Boolean_YN_LC     yNLc;
	private final org.jooq.test.all.converters.Boolean_YN_UC     yNUc;
	private final java.lang.Boolean                              vcBoolean;
	private final java.lang.Boolean                              cBoolean;
	private final java.lang.Boolean                              nBoolean;

	public TBooleans(
		java.lang.Integer                              id,
		org.jooq.test.all.converters.Boolean_10        oneZero,
		org.jooq.test.all.converters.Boolean_TF_LC     trueFalseLc,
		org.jooq.test.all.converters.Boolean_TF_UC     trueFalseUc,
		org.jooq.test.all.converters.Boolean_YES_NO_LC yesNoLc,
		org.jooq.test.all.converters.Boolean_YES_NO_UC yesNoUc,
		org.jooq.test.all.converters.Boolean_YN_LC     yNLc,
		org.jooq.test.all.converters.Boolean_YN_UC     yNUc,
		java.lang.Boolean                              vcBoolean,
		java.lang.Boolean                              cBoolean,
		java.lang.Boolean                              nBoolean
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

	@Override
	public java.lang.Integer getId() {
		return this.id;
	}

	@Override
	public org.jooq.test.all.converters.Boolean_10 getOneZero() {
		return this.oneZero;
	}

	@Override
	public org.jooq.test.all.converters.Boolean_TF_LC getTrueFalseLc() {
		return this.trueFalseLc;
	}

	@Override
	public org.jooq.test.all.converters.Boolean_TF_UC getTrueFalseUc() {
		return this.trueFalseUc;
	}

	@Override
	public org.jooq.test.all.converters.Boolean_YES_NO_LC getYesNoLc() {
		return this.yesNoLc;
	}

	@Override
	public org.jooq.test.all.converters.Boolean_YES_NO_UC getYesNoUc() {
		return this.yesNoUc;
	}

	@Override
	public org.jooq.test.all.converters.Boolean_YN_LC getYNLc() {
		return this.yNLc;
	}

	@Override
	public org.jooq.test.all.converters.Boolean_YN_UC getYNUc() {
		return this.yNUc;
	}

	@Override
	public java.lang.Boolean getVcBoolean() {
		return this.vcBoolean;
	}

	@Override
	public java.lang.Boolean getCBoolean() {
		return this.cBoolean;
	}

	@Override
	public java.lang.Boolean getNBoolean() {
		return this.nBoolean;
	}
}
