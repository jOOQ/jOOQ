/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses;

/**
 * This class is generated by jOOQ.
 *
 * A class modelling identity columns of the <code>PUBLIC</code> schema
 */
@java.lang.SuppressWarnings("all")
public final class Identities {

	public static final org.jooq.Identity<org.jooq.test.h2.generatedclasses.tables.records.TIdentityPkRecord, java.lang.Integer> IDENTITY_T_IDENTITY_PK = Identities0.IDENTITY_T_IDENTITY_PK;
	public static final org.jooq.Identity<org.jooq.test.h2.generatedclasses.tables.records.TIdentityRecord, java.lang.Integer> IDENTITY_T_IDENTITY = Identities0.IDENTITY_T_IDENTITY;
	public static final org.jooq.Identity<org.jooq.test.h2.generatedclasses.tables.records.TTriggersRecord, java.lang.Integer> IDENTITY_T_TRIGGERS = Identities0.IDENTITY_T_TRIGGERS;
	public static final org.jooq.Identity<org.jooq.test.h2.generatedclasses.tables.records.T_877Record, java.lang.Integer> IDENTITY_T_877 = Identities0.IDENTITY_T_877;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends org.jooq.impl.AbstractKeys {
		static org.jooq.Identity<org.jooq.test.h2.generatedclasses.tables.records.TIdentityPkRecord, java.lang.Integer> IDENTITY_T_IDENTITY_PK = createIdentity(org.jooq.test.h2.generatedclasses.tables.TIdentityPk.T_IDENTITY_PK, org.jooq.test.h2.generatedclasses.tables.TIdentityPk.ID);
		static org.jooq.Identity<org.jooq.test.h2.generatedclasses.tables.records.TIdentityRecord, java.lang.Integer> IDENTITY_T_IDENTITY = createIdentity(org.jooq.test.h2.generatedclasses.tables.TIdentity.T_IDENTITY, org.jooq.test.h2.generatedclasses.tables.TIdentity.ID);
		static org.jooq.Identity<org.jooq.test.h2.generatedclasses.tables.records.TTriggersRecord, java.lang.Integer> IDENTITY_T_TRIGGERS = createIdentity(org.jooq.test.h2.generatedclasses.tables.TTriggers.T_TRIGGERS, org.jooq.test.h2.generatedclasses.tables.TTriggers.ID_GENERATED);
		static org.jooq.Identity<org.jooq.test.h2.generatedclasses.tables.records.T_877Record, java.lang.Integer> IDENTITY_T_877 = createIdentity(org.jooq.test.h2.generatedclasses.tables.T_877.T_877, org.jooq.test.h2.generatedclasses.tables.T_877.ID);
	}

	/**
	 * No further instances allowed
	 */
	private Identities() {}
}
