/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
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
package chrriis.jooq.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.debugger.SqlQueryDebugger;
import org.jooq.debugger.SqlQueryDebuggerData;
import org.jooq.debugger.SqlQueryDebuggerRegister;
import org.jooq.debugger.SqlQueryDebuggerResultSetData;
import org.jooq.debugger.SqlQueryType;
import org.jooq.debugger.console.DatabaseDescriptor;
import org.jooq.debugger.console.SqlConsoleFrame;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.TableImpl;

public class SQLConsoleTest {

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				DatabaseDescriptor editorDatabaseDescriptor = null;
				editorDatabaseDescriptor = new DatabaseDescriptor() {
					@Override
					public Schema getSchema() {
						final List<Table<?>> tableList = new ArrayList<Table<?>>();
						tableList.add(new TableImpl<Record>("An_Example_Table") {{
							createField("SomeDecimal", SQLDataType.DECIMAL, this);
							createField("AnInt", SQLDataType.BIGINT, this);
						}});
						tableList.add(new TableImpl<Record>("Another_Table") {{
							createField("Wow", SQLDataType.CLOB, this);
							createField("Great", SQLDataType.BIT, this);
						}});
						tableList.add(new TableImpl<Record>("Basic") {{
							createField("Stuff", SQLDataType.CLOB, this);
							createField("Thing", SQLDataType.BIT, this);
						}});
						tableList.add(new TableImpl<Record>("Yet_Another_One") {{
							createField("SoGood", SQLDataType.DECIMAL, this);
							createField("Great", SQLDataType.VARCHAR, this);
						}});
						return new SchemaImpl("Test") {
							@Override
							public List<Table<?>> getTables() {
								return tableList;
							}
						};
					}
					@Override
					public SQLDialect getSQLDialect() {
						return SQLDialect.SQLSERVER;
					}
					@Override
					public Connection createConnection() {
						return null;
					}
				};
				SqlConsoleFrame sqlConsoleFrame = new SqlConsoleFrame(editorDatabaseDescriptor, true);
				sqlConsoleFrame.setLoggingActive(true);
				sqlConsoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				sqlConsoleFrame.setVisible(true);
				new Thread("SQL Thread") {
					public void run() {
						while(true) {
							try {
								sleep(Math.round(Math.random() * 3000));
							} catch (InterruptedException e) {
							}
							if(Math.random() < 0.2) {
								performSP();
							} else if(Math.random() < 0.6) {
								performInsert();
							} else {
								performSelectWithIndirection();
							}
						}
					}

				}.start();
			}
		});
	}

	private static void performSP() {
		SqlQueryDebuggerData sqlQueryDebuggerData = new SqlQueryDebuggerData(SqlQueryType.OTHER, new String[] {"spCustomFnt" + Math.round(Math.random() * 10) + "('Some params')"}, Math.round(Math.random() * 1500), Math.round(Math.random() * 1500), Math.round(Math.random() * 1500));
		for(SqlQueryDebugger debugger: SqlQueryDebuggerRegister.getSqlQueryDebuggerList()) {
			debugger.debugQueries(sqlQueryDebuggerData);
		}
	}

	private static void performSelectWithIndirection() {
		performSelectWithAnotherIndirection();
	}

	private static void performSelectWithAnotherIndirection() {
		yetAnotherSelectIndirection();
	}

	private static void yetAnotherSelectIndirection() {
		performSelect();
	}

	private static void performSelect() {
		SqlQueryDebuggerData sqlQueryDebuggerData = new SqlQueryDebuggerData(SqlQueryType.READ, new String[] {"SELECT * FROM Table" + Math.round(Math.random() * 10)}, null, null, Math.round(Math.random() * 1500));
		for(SqlQueryDebugger debugger: SqlQueryDebuggerRegister.getSqlQueryDebuggerList()) {
			debugger.debugQueries(sqlQueryDebuggerData);
		}
		try {
			Thread.sleep(Math.round(Math.random() * 1000));
		} catch (InterruptedException e) {
		}
		int sqlQueryDebuggerDataID = sqlQueryDebuggerData.getID();
		for(SqlQueryDebugger debugger: SqlQueryDebuggerRegister.getSqlQueryDebuggerList()) {
			debugger.debugResultSet(sqlQueryDebuggerDataID, new SqlQueryDebuggerResultSetData((int)Math.round(Math.random() * 1000), (int)Math.round(Math.random() * 1000000), (int)Math.round(Math.random() * 1000000), 0));
		}
	}

	private static void performInsert() {
		SqlQueryDebuggerData sqlQueryDebuggerData = new SqlQueryDebuggerData(SqlQueryType.WRITE, new String[] {"INSERT INTO Table" + Math.round(Math.random() * 10) + " (Col1, Col2, Col3) VALUES ('abc', NULL, " + Math.round(Math.random() * 150) + ")"}, null, null, Math.round(Math.random() * 1500));
		for(SqlQueryDebugger debugger: SqlQueryDebuggerRegister.getSqlQueryDebuggerList()) {
			debugger.debugQueries(sqlQueryDebuggerData);
		}
	}

}
