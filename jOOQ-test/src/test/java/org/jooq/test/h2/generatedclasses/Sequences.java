/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses;


import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;


/**
 * Convenience access to all sequences in PUBLIC
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

	/**
	 * The sequence <code>PUBLIC.S_AUTHOR_ID</code>
	 */
	public static final Sequence<Long> S_AUTHOR_ID = new SequenceImpl<Long>("S_AUTHOR_ID", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT);

	/**
	 * The sequence <code>PUBLIC.S_TRIGGERS_SEQUENCE</code>
	 */
	public static final Sequence<Long> S_TRIGGERS_SEQUENCE = new SequenceImpl<Long>("S_TRIGGERS_SEQUENCE", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT);
}
