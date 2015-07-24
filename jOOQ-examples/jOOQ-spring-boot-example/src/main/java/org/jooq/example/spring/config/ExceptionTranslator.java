package org.jooq.example.spring.config;

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

/**
 * This class transforms SQLException into a Spring specific DataAccessException. The idea behind this is borrowed from
 * Adam Zell's Gist
 *
 * @author Petri Kainulainen
 * @author Adam Zell
 * @author Lukas Eder
 * @see <a
 *      href="http://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-configuration/">http://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-configuration/</a>
 * @see <a href="https://gist.github.com/azell/5655888">https://gist.github.com/azell/5655888</a>
 */
public class ExceptionTranslator extends DefaultExecuteListener {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -2450323227461061152L;

	@Override
	public void exception(ExecuteContext ctx) {
		SQLDialect dialect = ctx.configuration().dialect();
		SQLExceptionTranslator translator = (dialect != null) ? new SQLErrorCodeSQLExceptionTranslator(dialect.name())
				: new SQLStateSQLExceptionTranslator();

		ctx.exception(translator.translate("jOOQ", ctx.sql(), ctx.sqlException()));
	}
}
