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
 *
 *
 *
 */
package org.jooq.example.jpa;

import static org.jooq.example.jpa.jooq.Tables.ACTOR;
import static org.jooq.example.jpa.jooq.Tables.FILM;
import static org.jooq.example.jpa.jooq.Tables.FILM_ACTOR;
import static org.jooq.example.jpa.jooq.Tables.LANGUAGE;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;

import javax.persistence.EntityManager;

import org.jooq.DSLContext;

/**
 * @author Lukas Eder
 */
class JPAExample {

    private static void run(EntityManager em, DSLContext ctx) {
        System.out.println(
            ctx.select(
                    ACTOR.FIRSTNAME,
                    ACTOR.LASTNAME,
                    count().as("Total"),
                    count().filterWhere(LANGUAGE.NAME.eq("English")).as("English"),
                    count().filterWhere(LANGUAGE.NAME.eq("German")).as("German"),
                    min(FILM.RELEASE_YEAR),
                    max(FILM.RELEASE_YEAR))
               .from(ACTOR)
               .join(FILM_ACTOR).on(ACTOR.ACTORID.eq(FILM_ACTOR.ACTORS_ACTORID))
               .join(FILM).on(FILM.FILMID.eq(FILM_ACTOR.FILMS_FILMID))
               .join(LANGUAGE).on(FILM.LANGUAGE_LANGUAGEID.eq(LANGUAGE.LANGUAGEID))
               .groupBy(
                    ACTOR.ACTORID,
                    ACTOR.FIRSTNAME,
                    ACTOR.LASTNAME)
               .orderBy(ACTOR.FIRSTNAME, ACTOR.LASTNAME, ACTOR.ACTORID)
               .fetch()
        );
    }

    public static void main(String[] args) throws Exception {
        Setup.run(JPAExample::run);
    }
}
