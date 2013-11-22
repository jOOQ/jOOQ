/**
 * Copyright (c) 2011-2013, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.example.jaxrs;

import static java.sql.DriverManager.getConnection;
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
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.tools.jdbc.JDBCUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * The license server.
 */
@Path("/license/")
@Component
@Scope("request")
public class LicenseService {

    /**
     * <code>/license/generate</code> generates and returns a new license key.
     *
     * @param mail The input email address of the licensee.
     */
    @GET
    @Produces("text/plain")
    @Path("/generate")
    public String generate(
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
    
    /**
     * <code>/license/verify</code> checks if a given licensee has access to version using a license.
     *
     * @param request The servlet request from the JAX-RS context.
     * @param mail The input email address of the licensee.
     * @param license The license used by the licensee.
     * @param version The product version being accessed.
     */
    @GET
    @Produces("text/plain")
    @Path("/verify")
    public String verify(
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
    
    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------

    /**
     * This method encapsulates a transaction and initialises a jOOQ DSLcontext.
     * This could also be achieved with Spring and DBCP for connection pooling.
     */
	private String run(CtxRunnable runnable) {
    	Connection c = null;
    	
		try {
    		Class.forName("org.postgresql.Driver");
    		c = getConnection("jdbc:postgresql:postgres", "postgres", System.getProperty("pw", "test"));
    		DSLContext ctx = DSL.using(new DefaultConfiguration()
    				.set(new DefaultConnectionProvider(c))
    				.set(SQLDialect.POSTGRES)
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
	
	private interface CtxRunnable {
		String run(DSLContext ctx);
	}
}
