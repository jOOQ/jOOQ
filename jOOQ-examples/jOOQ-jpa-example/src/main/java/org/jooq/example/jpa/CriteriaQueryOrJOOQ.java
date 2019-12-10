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

import static org.jooq.example.jpa.jooq.Tables.FILM;
import static org.jooq.example.jpa.jooq.Tables.FILM_ACTOR;
import static org.jooq.example.jpa.jooq.Tables.LANGUAGE;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.countDistinct;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.example.jpa.entity.Actor;
import org.jooq.example.jpa.entity.Actor_;
import org.jooq.example.jpa.entity.Film;
import org.jooq.example.jpa.entity.Film_;
import org.jooq.example.jpa.entity.Language;
import org.jooq.example.jpa.entity.Language_;

/**
 * @author Lukas Eder
 */
class CriteriaQueryOrJOOQ {

    private static void run(EntityManager em, DSLContext ctx) {
        filmLengthAndLanguages(em, ctx);
        numberOfFilmsPerLanguage(em, ctx);
        numberOfActorsPerLanguage(em, ctx);
    }

    private static void filmLengthAndLanguages(EntityManager em, DSLContext ctx) {
        // Using criteria query
        // --------------------

        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Film> q = qb.createQuery(Film.class);
        Root<Film> root = q.from(Film.class);
        q = q.select(root);
        TypedQuery<Film> typed = em.createQuery(q);

        for (Film film : typed.getResultList())
            System.out.println(film.title.value + " (" + film.length + " minutes) in " + film.language.name);

        // Using jOOQ
        // ----------

        for (Record rec :
            ctx.select(FILM.TITLE, FILM.LENGTH, LANGUAGE.NAME)
               .from(FILM)
               .join(LANGUAGE).on(FILM.LANGUAGE_LANGUAGEID.eq(LANGUAGE.LANGUAGEID)))
            System.out.println(rec.get(FILM.TITLE) + " (" + rec.get(FILM.LENGTH) + " minutes) in " + rec.get(LANGUAGE.NAME));
    }

    private static void numberOfFilmsPerLanguage(EntityManager em, DSLContext ctx) {

        // Using criteria query
        // --------------------

        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> q = qb.createTupleQuery();
        Root<Film> filmRoot = q.from(Film.class);
        Join<Film, Language> filmJoin = filmRoot.join(Film_.language);
        Path<String> languagePath = filmJoin.get(Language_.name);
        Path<Integer> filmPath = filmRoot.get(Film_.filmId);
        Expression<Long> count = qb.count(filmPath);
        q = q.multiselect(languagePath, count);
        q = q.groupBy(languagePath);
        TypedQuery<Tuple> typed = em.createQuery(q);

        for (Tuple tuple : typed.getResultList())
            System.out.println(tuple.get(languagePath) + " (" + tuple.get(count) + " films)");

        // Using jOOQ
        // ----------

        for (Record rec :
            ctx.select(LANGUAGE.NAME, count())
               .from(LANGUAGE)
               .join(FILM).on(FILM.LANGUAGE_LANGUAGEID.eq(LANGUAGE.LANGUAGEID))
               .groupBy(LANGUAGE.NAME))
            System.out.println(rec.get(LANGUAGE.NAME) + " (" + rec.get(count()) + " films)");
    }

    private static void numberOfActorsPerLanguage(EntityManager em, DSLContext ctx) {

        // Using criteria query
        // --------------------

        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> q = qb.createTupleQuery();
        Root<Film> filmRoot = q.from(Film.class);
        Join<Film, Language> filmJoin = filmRoot.join(Film_.language);
        Path<String> languagePath = filmJoin.get(Language_.name);
        SetJoin<Film, Actor> actorJoin = filmRoot.join(Film_.actors);
        Path<Integer> actorPath = actorJoin.get(Actor_.actorId);
        Expression<Long> count = qb.countDistinct(actorPath);
        q = q.multiselect(languagePath, count);
        q = q.groupBy(languagePath);
        TypedQuery<Tuple> typed = em.createQuery(q);

        for (Tuple tuple : typed.getResultList())
            System.out.println(tuple.get(languagePath) + " (" + tuple.get(count) + " actors)");

        // Using jOOQ
        // ----------

        for (Record rec :
            ctx.select(LANGUAGE.NAME, countDistinct(FILM_ACTOR.ACTORS_ACTORID).as("c"))
               .from(LANGUAGE)
               .join(FILM).on(FILM.LANGUAGE_LANGUAGEID.eq(LANGUAGE.LANGUAGEID))
               .join(FILM_ACTOR).on(FILM.FILMID.eq(FILM_ACTOR.FILMS_FILMID))
               .groupBy(LANGUAGE.NAME))
            System.out.println(rec.get(LANGUAGE.NAME) + " (" + rec.get("c") + " actors)");
    }

    public static void main(String[] args) throws Exception {
        Setup.run(CriteriaQueryOrJOOQ::run);
    }
}
