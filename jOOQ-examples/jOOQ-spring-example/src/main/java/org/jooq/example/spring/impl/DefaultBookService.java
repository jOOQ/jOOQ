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
package org.jooq.example.spring.impl;

import static org.jooq.example.db.h2.Tables.BOOK;

import org.jooq.DSLContext;
import org.jooq.example.spring.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lukas Eder
 */
public class DefaultBookService implements BookService {

    @Autowired
    DSLContext dsl;

    @Override
    @Transactional
    public void create(int id, int authorId, String title) {

        // This method has a "bug". It creates the same book twice. The second insert
        // should lead to a constraint violation, which should roll back the whole transaction
        for (int i = 0; i < 2; i++)
            dsl.insertInto(BOOK)
               .set(BOOK.ID, id)
               .set(BOOK.AUTHOR_ID, authorId)
               .set(BOOK.TITLE, title)
               .execute();
    }
}
