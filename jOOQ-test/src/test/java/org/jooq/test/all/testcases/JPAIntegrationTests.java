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
package org.jooq.test.all.testcases;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.VERTICA;
import static org.jooq.lambda.Seq.seq;
import static org.jooq.test.jOOQAbstractTest.datasource;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.lambda.Seq;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test.all.pojos.jpa.JPAAuthor;
import org.jooq.test.all.pojos.jpa.JPABook;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
public class JPAIntegrationTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public JPAIntegrationTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testJPANativeQuery() {
        // https://hibernate.atlassian.net/browse/HHH-9560
        assumeDialectNotIn(SQLITE, VERTICA);

        emTx(em -> {
            List<Object[]> books =
            nativeQuery(em, create()
                .select(TBook_ID(), TBook_AUTHOR_ID(), TBook_TITLE())
                .from(TBook())
                .where(TBook_ID().gt(1))
                .orderBy(TBook_ID()));

            assertEquals(3, books.size());
            assertEquals(BOOK_IDS.subList(1, 4), seq(books).map(a -> a[0]).map(this::toInteger).toList());
            assertEquals(BOOK_AUTHOR_IDS.subList(1, 4), seq(books).map(a -> a[1]).map(this::toInteger).toList());
            assertEquals(BOOK_TITLES.subList(1, 4), seq(books).map(a -> a[2]).toList());
        });
    }

    Integer toInteger(Object o) {
        return Integer.valueOf("" + o);
    }

    static List<Object[]> nativeQuery(EntityManager em, org.jooq.Query query) {
        Query result = em.createNativeQuery(query.getSQL());

        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        return result.getResultList();
    }

    static <E> List<E> nativeQuery(EntityManager em, org.jooq.Query query, Class<E> type) {
        Query result = em.createNativeQuery(query.getSQL(), type);

        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        return result.getResultList();
    }

    static <E> List<E> nativeQuery(EntityManager em, org.jooq.Query query, String resultSetMapping) {
        Query result = em.createNativeQuery(query.getSQL(), resultSetMapping);

        List<Object> values = query.getBindValues();
        for (int i = 0; i < values.size(); i++) {
            result.setParameter(i + 1, values.get(i));
        }

        return result.getResultList();
    }

    public void testJPANativeQueryAndEntites() {
        // https://hibernate.atlassian.net/browse/HHH-9560
        assumeDialectNotIn(SQLITE, VERTICA);

        emTx(em -> {
            List<JPAAuthor> authors =
            nativeQuery(em,
                create().select()
                        .from(TAuthor())
                        .orderBy(TAuthor_ID())
            , JPAAuthor.class);

            assertEquals(2, authors.size());
            assertEquals(AUTHOR_IDS, seq(authors).map(JPAAuthor::getId).toList());
            assertEquals(AUTHOR_FIRST_NAMES, seq(authors).map(JPAAuthor::getFirstName).toList());
            assertEquals(AUTHOR_LAST_NAMES, seq(authors).map(JPAAuthor::getLastName).toList());
            assertEquals(asList(2, 2), seq(authors).map(JPAAuthor::getBooks).map(Collection::size).toList());
            assertSame(BOOK_TITLES, seq(authors).map(JPAAuthor::getBooks).flatMap(Seq::seq).map(b -> b.title).toList());
        });
    }

    public void testJPANativeQueryAndSqlResultSetMapping() {
        // https://hibernate.atlassian.net/browse/HHH-9560
        assumeDialectNotIn(SQLITE, VERTICA);

        jOOQAbstractTest.reset = false;

        emTx(em -> {
            List<Object[]> books =
            nativeQuery(em,
                create().select(
                            TAuthor_ID().as("a_id"),
                            TAuthor_FIRST_NAME().as("a_first_name"),
                            TAuthor_LAST_NAME().as("a_last_name"),
                            TBook_ID().as("b_id"),
                            TBook_AUTHOR_ID().as("b_author_id"),
                            TBook_TITLE().as("b_title")
                        )
                        .from(TAuthor())
                        .join(TBook())
                        .on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                        .orderBy(TBook_ID())
            , "bookmapping");

            assertEquals(4, books.size());
            assertEquals(BOOK_AUTHOR_IDS, seq(books).map(a -> (JPABook) a[0]).map(b -> b.author.getId()).toList());
            assertEquals(BOOK_FIRST_NAMES, seq(books).map(a -> (JPABook) a[0]).map(b -> b.author.getFirstName()).toList());
            assertEquals(BOOK_LAST_NAMES, seq(books).map(a -> (JPABook) a[0]).map(b -> b.author.getLastName()).toList());
            assertEquals(BOOK_AUTHOR_IDS, seq(books).map(a -> (JPAAuthor) a[1]).map(a -> a.getId()).toList());
            assertEquals(BOOK_FIRST_NAMES, seq(books).map(a -> (JPAAuthor) a[1]).map(a -> a.getFirstName()).toList());
            assertEquals(BOOK_LAST_NAMES, seq(books).map(a -> (JPAAuthor) a[1]).map(a -> a.getLastName()).toList());
            assertEquals(BOOK_IDS, seq(books).map(a -> (JPABook) a[0]).map(b -> b.id).toList());
            assertEquals(BOOK_TITLES, seq(books).map(a -> (JPABook) a[0]).map(b -> b.title).toList());

            seq(books).map(b -> (JPABook) b[0]).zipWithIndex().forEach(t -> t.v1.title = "title " + (t.v2 + 1));
        });

        assertSame(asList("title 1", "title 2", "title 3", "title 4"), create().fetchValues(TBook_TITLE()));
    }

    void emTx(Consumer<EntityManager> consumer) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform(dialect().thirdParty().hibernateDialect());

        bean.setDataSource(datasource);
        bean.setPackagesToScan("org.jooq.test.all.pojos.jpa");
        bean.setJpaVendorAdapter(adapter);
        bean.setPersistenceUnitName("test");
        bean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        bean.afterPropertiesSet();

        EntityManagerFactory emf = bean.getObject();
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        try {
            consumer.accept(em);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
        finally {
            em.close();
            emf.close();
        }
    }
}
