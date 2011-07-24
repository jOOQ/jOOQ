/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

package org.jooq.test;

import java.sql.Date;

import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.AbstractDataType;
import org.jooq.impl.TableFieldImpl;
import org.jooq.impl.TableImpl;
import org.jooq.impl.TableRecordImpl;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("serial")
public final class Data {
    public static final SQLDialect DIALECT = SQLDialect.ORACLE;

	public static final Table<Table1Record> TABLE1 = new TableImpl<Table1Record>("TABLE1") {
		@Override
		public Class<Table1Record> getRecordType() {
			return Table1Record.class;
		}
	};
	public static final Table<Table2Record> TABLE2 = new TableImpl<Table2Record>("TABLE2"){
		@Override
		public Class<Table2Record> getRecordType() {
			return Table2Record.class;
		}
	};
	public static final Table<Table3Record> TABLE3 = new TableImpl<Table3Record>("TABLE3") {
		@Override
		public Class<Table3Record> getRecordType() {
			return Table3Record.class;
		}
	};

	public static final class TestDataType<T> extends AbstractDataType<T> {
        protected TestDataType(Class<? extends T> type) {
            super(DIALECT, null, type, type.getSimpleName());
        }
	}

	public static final DataType<Integer> INTEGER_TYPE = new TestDataType<Integer>(Integer.class);
	public static final DataType<String> STRING_TYPE = new TestDataType<String>(String.class);
	public static final DataType<Date> DATE_TYPE = new TestDataType<Date>(Date.class);

	public static final TableField<Table1Record, Integer> FIELD_ID1 = new TableFieldImpl<Table1Record, Integer>("ID1", INTEGER_TYPE, TABLE1);
	public static final TableField<Table2Record, Integer> FIELD_ID2 = new TableFieldImpl<Table2Record, Integer>("ID2", INTEGER_TYPE, TABLE2);
	public static final TableField<Table3Record, Integer> FIELD_ID3 = new TableFieldImpl<Table3Record, Integer>("ID3", INTEGER_TYPE, TABLE3);

	public static final TableField<Table1Record, String> FIELD_NAME1 = new TableFieldImpl<Table1Record, String>("NAME1", STRING_TYPE, TABLE1);
	public static final TableField<Table2Record, String> FIELD_NAME2 = new TableFieldImpl<Table2Record, String>("NAME2", STRING_TYPE, TABLE2);
	public static final TableField<Table3Record, String> FIELD_NAME3 = new TableFieldImpl<Table3Record, String>("NAME3", STRING_TYPE, TABLE3);

	public static final TableField<Table1Record, Date> FIELD_DATE1 = new TableFieldImpl<Table1Record, Date>("DATE1", DATE_TYPE, TABLE1);
	public static final TableField<Table2Record, Date> FIELD_DATE2 = new TableFieldImpl<Table2Record, Date>("DATE2", DATE_TYPE, TABLE2);
	public static final TableField<Table3Record, Date> FIELD_DATE3 = new TableFieldImpl<Table3Record, Date>("DATE3", DATE_TYPE, TABLE3);

	public static class Table1Record extends TableRecordImpl<Table1Record> {
		public Table1Record() {
	        super(TABLE1);
		}
	}
	public static class Table2Record extends TableRecordImpl<Table2Record> {
		public Table2Record() {
	        super(TABLE2);
		}
	}
	public static class Table3Record extends TableRecordImpl<Table3Record> {
		public Table3Record() {
	        super(TABLE3);
		}
	}

	private Data() {}
}
