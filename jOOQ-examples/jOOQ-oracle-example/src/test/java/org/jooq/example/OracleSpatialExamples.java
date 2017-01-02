/*
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
 */
package org.jooq.example;

import static org.jooq.example.db.oracle.sp.Tables.COLA_MARKETS;
import static org.jooq.impl.DSL.val;

import org.jooq.Record1;
import org.jooq.example.db.oracle.mdsys.packages.SdoGeom;
import org.jooq.example.db.oracle.sp.tables.ColaMarkets;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class OracleSpatialExamples extends Utils {

    @Test
    public void testSDO_GEOM() {

        // These examples were inspired from the examples given in the Oracle
        // documentation:
        // https://docs.oracle.com/cd/B19306_01/appdev.102/b14255/sdo_objrelschema.htm#SPATL020
        // OTN License restrictions may apply.

        ColaMarkets c_a = COLA_MARKETS.as("c_a");
        ColaMarkets c_b = COLA_MARKETS.as("c_b");
        ColaMarkets c_c = COLA_MARKETS.as("c_c");
        ColaMarkets c_d = COLA_MARKETS.as("c_d");

        // SELECT SDO_GEOM.SDO_INTERSECTION(c_a.shape, c_c.shape, 0.005)
        //    FROM cola_markets c_a, cola_markets c_c
        //    WHERE c_a.name = 'cola_a' AND c_c.name = 'cola_c';

        System.out.println();
        System.out.println("Return the topological intersection of two geometries.");
        System.out.println("------------------------------------------------------");

        dsl.select(SdoGeom.sdoIntersection2(c_a.SHAPE, c_c.SHAPE, val(0.005)))
           .from(c_a, c_c)
           .where(c_a.NAME.eq("cola_a"))
           .and(c_c.NAME.eq("cola_c"))
           .fetch()
           .map(Record1::value1)
           .forEach(System.out::println);

        // SELECT SDO_GEOM.RELATE(c_b.shape, 'anyinteract', c_d.shape, 0.005)
        //   FROM cola_markets c_b, cola_markets c_d
        //   WHERE c_b.name = 'cola_b' AND c_d.name = 'cola_d';

        System.out.println();
        System.out.println("Do two geometries have any spatial relationship?");
        System.out.println("------------------------------------------------");

        dsl.select(SdoGeom.relate6(c_b.SHAPE, val("anyinteract"), c_d.SHAPE, val(0.005)))
           .from(c_b, c_d)
           .where(c_b.NAME.eq("cola_b"))
           .and(c_d.NAME.eq("cola_d"))
           .fetch()
           .map(Record1::value1)
           .forEach(System.out::println);

        // -- Return the areas of all cola markets.
        // SELECT name, SDO_GEOM.SDO_AREA(shape, 0.005) FROM cola_markets;

        System.out.println();
        System.out.println("Return the areas of all cola markets.");
        System.out.println("-------------------------------------");

        dsl.select(SdoGeom.sdoArea2(c_a.SHAPE, val(0.005)))
           .from(c_a)
           .fetch()
           .map(Record1::value1)
           .forEach(System.out::println);

        // SELECT SDO_GEOM.SDO_DISTANCE(c_b.shape, c_d.shape, 0.005)
        //    FROM cola_markets c_b, cola_markets c_d
        //    WHERE c_b.name = 'cola_b' AND c_d.name = 'cola_d';

        System.out.println();
        System.out.println("Return the distance between two geometries.");
        System.out.println("-------------------------------------------");

        dsl.select(SdoGeom.sdoDistance2(c_b.SHAPE, c_d.SHAPE, val(0.005)))
           .from(c_b, c_d)
           .where(c_b.NAME.eq("cola_b"))
           .and(c_d.NAME.eq("cola_d"))
           .fetch()
           .map(Record1::value1)
           .forEach(System.out::println);
    }
}
