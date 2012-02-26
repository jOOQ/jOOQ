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
package org.jooq.test._;

import org.jooq.TableField;
import org.jooq.impl.CustomTable;
import org.jooq.impl.SQLDataType;

/**
 * @author Lukas Eder
 */
public class BookTable extends CustomTable<BookRecord> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -1124714471434439420L;

    public static final BookTable                      BOOK       = new BookTable();

    public static final TableField<BookRecord, String> FIRST_NAME = createField("FIRST_NAME", SQLDataType.VARCHAR, BOOK);
    public static final TableField<BookRecord, String> UNMATCHED  = createField("UNMATCHED", SQLDataType.VARCHAR, BOOK);
    public static final TableField<BookRecord, String> LAST_NAME  = createField("LAST_NAME", SQLDataType.VARCHAR, BOOK);
    public static final TableField<BookRecord, Short>  ID         = createField("ID", SQLDataType.SMALLINT, BOOK);
    public static final TableField<BookRecord, String> TITLE      = createField("TITLE", SQLDataType.VARCHAR, BOOK);

    protected BookTable() {
        super(null);
    }

    @Override
    public Class<? extends BookRecord> getRecordType() {
        return BookRecord.class;
    }
}
