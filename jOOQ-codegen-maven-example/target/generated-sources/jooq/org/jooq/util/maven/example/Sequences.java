/**
 * This class is generated by jOOQ
 */
package org.jooq.util.maven.example;

/**
 * This class is generated by jOOQ.
 *
 * Convenience access to all sequences in public
 */
@javax.annotation.Generated(value    = "http://jooq.sourceforge.net",
                            comments = "This class is generated by jOOQ")
public final class Sequences {

	/**
	 * The sequence s_author_id
	 */
	public static final org.jooq.Sequence S_AUTHOR_ID = new org.jooq.impl.SequenceImpl("s_author_id", org.jooq.util.maven.example.Public.PUBLIC);

	/**
	 * The sequence t_book_store_id_seq
	 */
	public static final org.jooq.Sequence T_BOOK_STORE_ID_SEQ = new org.jooq.impl.SequenceImpl("t_book_store_id_seq", org.jooq.util.maven.example.Public.PUBLIC);

	/**
	 * The sequence t_triggers_id_generated_seq
	 */
	public static final org.jooq.Sequence T_TRIGGERS_ID_GENERATED_SEQ = new org.jooq.impl.SequenceImpl("t_triggers_id_generated_seq", org.jooq.util.maven.example.Public.PUBLIC);

	/**
	 * No instances
	 */
	private Sequences() {}
}
