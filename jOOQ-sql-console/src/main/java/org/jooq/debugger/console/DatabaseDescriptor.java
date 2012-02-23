package org.jooq.debugger.console;

import java.sql.Connection;

import org.jooq.SQLDialect;
import org.jooq.Schema;

/**
 * @author Christopher Deckers
 */
public abstract class DatabaseDescriptor {

	public abstract Schema getSchema();

	public abstract SQLDialect getSQLDialect();
	
	public abstract Connection createConnection();
	
	public boolean isReadOnly() {
		return false;
	}
	
}
