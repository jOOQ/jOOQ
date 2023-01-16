/**
 * JDBC tooling.
 * <p>
 * This package contains
 * <ul>
 * <li>Compatibility classes to bridge between JDBC 4.0, 4.1, 4.2, 4.3 (see
 * {@link org.jooq.tools.jdbc.JDBC41Connection})</li>
 * <li>The {@link org.jooq.tools.jdbc.BatchedConnection} that allows for
 * batching statements transparently on a JDBC level</li>
 * <li>A {@link org.jooq.tools.jdbc.DefaultConnection} that implements all the
 * API and delegates to another connection, useful as base class for other
 * utilities</li>
 * <li>A {@link org.jooq.tools.jdbc.MockConnection}, which mocks the JDBC API
 * based on fixed data</li>
 * <li>A {@link org.jooq.tools.jdbc.LoggingConnection}, which logs all
 * statements that are executed by it</li>
 * </ul>
 */
package org.jooq.tools.jdbc;
