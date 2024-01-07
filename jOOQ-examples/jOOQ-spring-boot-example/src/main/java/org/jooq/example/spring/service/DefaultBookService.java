package org.jooq.example.spring.service;

import static org.jooq.example.db.h2.Tables.BOOK;

import org.jooq.DSLContext;
import org.jooq.example.db.h2.tables.records.BookRecord;
import org.jooq.example.spring.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lukas Eder
 */
@Service
public class DefaultBookService implements BookService {

	@Autowired DSLContext dsl;
	@Autowired BookRepository bookRepository;

	@Override
	@Transactional
	public void create(int id, int authorId, String title) {

		// This method has a "bug". It creates the same book twice. The second insert
		// should lead to a constraint violation, which should roll back the whole transaction
		for (int i = 0; i < 2; i++) {
			dsl.insertInto(BOOK).set(BOOK.ID, id).set(BOOK.AUTHOR_ID, authorId).set(BOOK.TITLE, title).execute();
		}
	}

	public void create(int authorId, String title) {
		BookRecord bookRecord = bookRepository.ctx().newRecord(BOOK);
		bookRecord.setAuthorId(authorId);
		bookRecord.setTitle(title);
		bookRecord.store();
	}
}
