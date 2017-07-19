/**
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
package org.jooq.xtend

import org.jooq.Constants

/**
 * @author Lukas Eder
 */
class Constraint extends Generators {
    
    def static void main(String[] args) {
        val constraint = new Constraint();
        
        constraint.generateConstraintTypeStepForeignKey();
        constraint.generateConstraintForeignKeyReferencesSteps();
        constraint.generateConstraintImplImplements();
        constraint.generateConstraintImplForeignKey();
    }
    
    def generateConstraintImplForeignKey() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                «generatedMethod»
                @Override
                public final <«TN(degree)»> ConstraintImpl foreignKey(«Field_TN_fieldn(degree)») {
                	return foreignKey(new Field[] { «fieldn(degree)» });
                }
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                «generatedMethod»
                @Override
                public final ConstraintImpl foreignKey(«XXXn(degree, "Name field")») {
                    return foreignKey(new Name[] { «fieldn(degree)» });
                }
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                «generatedMethod»
                @Override
                public final ConstraintImpl foreignKey(«XXXn(degree, "String field")») {
                    return foreignKey(new String[] { «fieldn(degree)» });
                }
            ''');
        }

        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                «generatedMethod»
                @Override
                public final ConstraintImpl references(Table table, «Field_tn(degree)») {
                	return references(table, new Field[] { «tn(degree)» });
                }
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                «generatedMethod»
                @Override
                public final ConstraintImpl references(Name table, «XXXn(degree, "Name field")») {
                    return references(table, new Name[] { «fieldn(degree)» });
                }
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                «generatedMethod»
                @Override
                public final ConstraintImpl references(String table, «XXXn(degree, "String field")») {
                    return references(table, new String[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.impl.ConstraintImpl", out, "foreignKey");
    }
         
    def generateConstraintImplImplements() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
          	out.append('''  , ConstraintForeignKeyReferencesStep«degree»
            ''');
        }

        insert("org.jooq.impl.ConstraintImpl", out, "implements");
    }
       
    def generateConstraintTypeStepForeignKey() {
        val out = new StringBuilder();
        val outDSL = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
                 */
                «generatedMethod»
                @Support
                <«TN(degree)»> ConstraintForeignKeyReferencesStep«degree»<«TN(degree)»> foreignKey(«Field_TN_fieldn(degree)»);
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
                 */
                «generatedMethod»
                @Support
                ConstraintForeignKeyReferencesStep«degree»<«(1 .. degree).map["?"].join(", ")»> foreignKey(«XXXn(degree, "Name field")»);
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
                 */
                «generatedMethod»
                @Support
                ConstraintForeignKeyReferencesStep«degree»<«(1 .. degree).map["?"].join(", ")»> foreignKey(«XXXn(degree, "String field")»);
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            outDSL.append('''
            
                /**
                 * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> ConstraintForeignKeyReferencesStep«degree»<«TN(degree)»> foreignKey(«Field_TN_fieldn(degree)») {
                    return constraint().foreignKey(«XXXn(degree, "field")»);
                }
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            outDSL.append('''
            
                /**
                 * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
                 */
                «generatedMethod»
                @Support
                public static ConstraintForeignKeyReferencesStep«degree»<«(1 .. degree).map["?"].join(", ")»> foreignKey(«XXXn(degree, "Name field")») {
                    return constraint().foreignKey(«XXXn(degree, "field")»);
                }
            ''');
        }
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            outDSL.append('''
            
                /**
                 * Add an unnamed (system named) <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
                 */
                «generatedMethod»
                @Support
                public static ConstraintForeignKeyReferencesStep«degree»<«(1 .. degree).map["?"].join(", ")»> foreignKey(«XXXn(degree, "String field")») {
                    return constraint().foreignKey(«XXXn(degree, "field")»);
                }
            ''');
        }

        insert("org.jooq.ConstraintTypeStep", out, "foreignKey");
        insert("org.jooq.impl.DSL", outDSL, "foreignKey");
    }
    
    def generateConstraintForeignKeyReferencesSteps() {
        for (degree : (1 .. Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;

            import javax.annotation.Generated;
            
            /**
             * The step in the {@link Constraint} construction DSL API that allows for
             * matching a <code>FOREIGN KEY</code> clause with a <code>REFERENCES</code>
             * clause.
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface ConstraintForeignKeyReferencesStep«degree»<«TN(degree)»> {
            
                /**
                 * Add a <code>REFERENCES</code> clause to the <code>CONSTRAINT</code>.
                 */
                @Support
                ConstraintForeignKeyOnStep references(String table, «XXXn(degree, "String field")»);
            
                /**
                 * Add a <code>REFERENCES</code> clause to the <code>CONSTRAINT</code>.
                 */
                @Support
                ConstraintForeignKeyOnStep references(Name table, «XXXn(degree, "Name field")»);
            
                /**
                 * Add a <code>REFERENCES</code> clause to the <code>CONSTRAINT</code>.
                 */
                @Support
                ConstraintForeignKeyOnStep references(Table<?> table, «Field_TN_fieldn(degree)»);
            }
            ''');
             
            write("org.jooq.ConstraintForeignKeyReferencesStep" + degree, out);
        }
    }
}
