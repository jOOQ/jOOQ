/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.test.data;

import org.jooq.TableField;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.test.data.converter.Bool;
import org.jooq.test.data.converter.BoolConverter;

public class BoolTable extends TableImpl<BoolRecord> {

    /**
     * Generated UID
     */
    private static final long                 serialVersionUID = 2618099850927000707L;

    public static final BoolTable             BOOL_TABLE       = new BoolTable();

    public final TableField<BoolRecord, Bool> BOOL             = createField("BOOL",
                                                                   SQLDataType.BOOLEAN
                                                                       .asConvertedDataType(new BoolConverter()), this);

    public BoolTable() {
        super("BOOL_TABLE");
    }

    @Override
    public Class<BoolRecord> getRecordType() {
        return BoolRecord.class;
    }
}
