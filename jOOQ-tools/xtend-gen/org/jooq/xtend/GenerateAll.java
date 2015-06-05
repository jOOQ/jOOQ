/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 * 
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 * 
 * For more information, please visit http://www.jooq.org/licenses
 * 
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.xtend;

import org.jooq.xtend.BetweenAndSteps;
import org.jooq.xtend.Constraint;
import org.jooq.xtend.DSL;
import org.jooq.xtend.DSLContext;
import org.jooq.xtend.InsertDSL;
import org.jooq.xtend.MergeDSL;
import org.jooq.xtend.Records;
import org.jooq.xtend.Rows;
import org.jooq.xtend.ScalaConversions;
import org.jooq.xtend.SelectSeekStep;
import org.jooq.xtend.UpdateDSL;
import org.jooq.xtend.With;
import org.jooq.xtend.XtendConversions;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("all")
public class GenerateAll {
  public static void main(final String[] args) {
    BetweenAndSteps.main(args);
    ScalaConversions.main(args);
    XtendConversions.main(args);
    DSLContext.main(args);
    DSL.main(args);
    Constraint.main(args);
    InsertDSL.main(args);
    MergeDSL.main(args);
    Records.main(args);
    Rows.main(args);
    SelectSeekStep.main(args);
    UpdateDSL.main(args);
    With.main(args);
  }
}
