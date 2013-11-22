package org.jooq.web.services;

import static java.sql.DriverManager.getConnection;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.example.jaxrs.db.Routines.generateKey;
import static org.jooq.example.jaxrs.db.Tables.LICENSE;
import static org.jooq.example.jaxrs.db.Tables.LOG_VERIFY;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.val;

import java.sql.Connection;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * @author Lukas Eder
 */
@Path("/license/")
@Component
@Scope("request")
public class LicenseService {

    @GET
    @Produces("text/plain")
    @Path("/generate")
    public String generateGET(
        final @QueryParam("mail") String mail
    ) {
    	return run(new CtxRunnable() {
			
			@Override
			public String run(DSLContext ctx) {
	    		Timestamp licenseDate = new Timestamp(System.currentTimeMillis());
	    		
	    		return
	    		ctx.insertInto(LICENSE)
	    		   .set(LICENSE.LICENSE_, generateKey(inline(licenseDate), inline(mail)))
	    		   .set(LICENSE.LICENSE_DATE, licenseDate)
	    		   .set(LICENSE.LICENSEE, mail)
	    		   .returning()
	    		   .fetchOne()
	    		   .getLicense();
			}
		});
    }
    
    @GET
    @Produces("text/plain")
    @Path("/verify")
    public String verifyGET(
		final @Context HttpServletRequest request,
		final @QueryParam("mail") String mail,
		final @QueryParam("license") String license,
		final @QueryParam("version") String version
	) {
		return run(new CtxRunnable() {
			@Override
			public String run(DSLContext ctx) {
			    String v = (version == null || version.equals("")) ? "" : version;
			    
				return
				ctx.insertInto(LOG_VERIFY)
				   .set(LOG_VERIFY.LICENSE, license)
				   .set(LOG_VERIFY.LICENSEE, mail)
				   .set(LOG_VERIFY.REQUEST_IP, request.getRemoteAddr())
				   .set(LOG_VERIFY.MATCH, field(
						   selectCount()
						  .from(LICENSE)
						  .where(LICENSE.LICENSEE.eq(mail))
						  .and(LICENSE.LICENSE_.eq(license))
						  .and(val(v).likeRegex(LICENSE.VERSION))
						  .asField().gt(0)))
				   .set(LOG_VERIFY.VERSION, v)
				   .returning(LOG_VERIFY.MATCH)
				   .fetchOne()
				   .getValue(LOG_VERIFY.MATCH, String.class);
			}
		});
	}
	
	private String run(CtxRunnable runnable) {
    	Connection c = null;
    	
		try {
    		Class.forName("org.postgresql.Driver");
    		c = getConnection("jdbc:postgresql:postgres", "postgres", System.getProperty("pw", "test"));
    		DSLContext ctx = DSL.using(new DefaultConfiguration()
    				.set(new DefaultConnectionProvider(c))
    				.set(SQLDialect.POSTGRES)
    				.set(new DefaultExecuteListenerProvider(new LicenseLogger()))
    				.set(new Settings().withExecuteLogging(false)));
    		
    		return runnable.run(ctx);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		Response.status(Status.SERVICE_UNAVAILABLE);
    		return "Service Unavailable - Please contact support@datageekery.com for help";
    	}
    	finally {
    		JDBCUtils.safeClose(c);
    	}
	}
	
    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------
	
	private interface CtxRunnable {
		String run(DSLContext ctx);
	}
	
	private static class LicenseLogger extends DefaultExecuteListener {

		private static final JooqLogger log = JooqLogger
				.getLogger(LicenseLogger.class);

		/**
		 *  Generated UID
		 */
		private static final long serialVersionUID = 2293454050264611920L;

		@Override
		public void renderEnd(ExecuteContext ctx) {
			if (ctx.query() != null) {
				log.info("Query", ctx.query().getSQL(INLINED));
			}
		}

	    @Override
	    public void resultEnd(ExecuteContext ctx) {
	        if (log.isDebugEnabled() && ctx.result() != null) {
	            logMultiline("Result", ctx.result().format(5));
	        }
	    }

	    private void logMultiline(String comment, String message) {
	        for (String line : message.split("\n")) {
                log.info(comment, line);
	            comment = "";
	        }
	    }
	}
}
