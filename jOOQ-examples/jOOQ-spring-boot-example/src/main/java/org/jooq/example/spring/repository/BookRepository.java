package org.jooq.example.spring.repository;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.example.db.h2.tables.daos.BookDao;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository extends BookDao {

  private final DSLContext dslContext;

  public BookRepository(Configuration configuration, DSLContext dslContext) {
    super(configuration);
    this.dslContext = dslContext;
  }

  @Override
  public DSLContext ctx() {
    return dslContext;
  }
}
