/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test.util.spring;

import static org.jooq.impl.Factory.param;
import static org.jooq.util.maven.example.hsqldb.Sequences.S_AUTHOR_ID;
import static org.jooq.util.maven.example.hsqldb.Tables.T_AUTHOR;

import java.util.List;

import org.jooq.BatchBindStep;
import org.jooq.FactoryOperations;
import org.jooq.impl.Factory;
import org.jooq.test.util.spring.domain.Author;
import org.jooq.util.maven.example.hsqldb.tables.records.TAuthorRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sergey Epik
 */
@Transactional(readOnly = true)
@Component
public class AuthorDaoImpl implements AuthorDao {

    private FactoryOperations factory;

    @Autowired
    public void setFactoryOperations(FactoryOperations factory) {
        this.factory = factory;
    }

    @Override
    public Author findById(Integer id) {
        TAuthorRecord authorRecord = factory
            .selectFrom(T_AUTHOR)
            .where(T_AUTHOR.ID.equal(id))
            .fetchOne();

        return authorRecord == null ? null : authorRecord.into(Author.class);
    }

    @Override
    public List<Author> findAll() {
        return factory.selectFrom(T_AUTHOR).fetchInto(Author.class);
    }

    @Override
    public Integer add(Author author) {
        Integer id = factory.nextval(S_AUTHOR_ID);

        factory.insertInto(T_AUTHOR)
               .set(T_AUTHOR.ID, id)
               .set(T_AUTHOR.FIRST_NAME, author.getFirstName())
               .set(T_AUTHOR.LAST_NAME, author.getLastName())
               .execute();

        return id;
    }

    @Override
    public void addBatch(List<Author> authors) {
        BatchBindStep step = factory.batch(
                factory.insertInto(T_AUTHOR, T_AUTHOR.ID, T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME)
                       .values(param("id"), param("first"), param("last")));

        for (Author author : authors) {
            Integer id = factory.nextval(S_AUTHOR_ID).intValue();
            step = step.bind(id, author.getFirstName(), author.getLastName());
        }

        step.execute();
    }

    @Override
    public void save(Author author) {
        factory.update(T_AUTHOR)
               .set(T_AUTHOR.FIRST_NAME, author.getFirstName())
               .set(T_AUTHOR.LAST_NAME, author.getLastName())
               .where(T_AUTHOR.ID.equal(author.getId()))
               .execute();
    }

    @Override
    public void delete(Author author) {
        factory.delete(T_AUTHOR)
               .where(T_AUTHOR.ID.equal(author.getId()))
               .execute();
    }

    @Override
    public long countAuthors() {
        return factory.selectCount().from(T_AUTHOR)
                .fetchOne().getValue(0, Long.class);
    }

    @Override
    public long countDistinctForLastName(String name) {
        return factory.select(Factory.countDistinct(T_AUTHOR.LAST_NAME))
                      .from(T_AUTHOR)
                      .where(T_AUTHOR.LAST_NAME.like(name + "%"))
                      .fetchOne()
                      .getValue(0, Long.class);
    }

    @Override
    public boolean authorExists(Integer id) {
        return factory.selectFrom(T_AUTHOR).where(T_AUTHOR.ID.equal(id)).fetchOne() != null;
    }

    @Override
    public boolean authorExists(Author author) {
        return factory.selectFrom(T_AUTHOR)
                      .where(T_AUTHOR.ID.equal(author.getId()))
                      .fetchOne() != null;
    }
}
