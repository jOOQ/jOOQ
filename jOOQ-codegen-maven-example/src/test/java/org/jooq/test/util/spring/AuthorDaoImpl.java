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
        TAuthorRecord authorRecord = factory.selectFrom(T_AUTHOR)
                .where(T_AUTHOR.ID.equal(id)).fetchOne();
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
        return factory.select(Factory.countDistinct(T_AUTHOR.LAST_NAME)).from(T_AUTHOR)
                .where(T_AUTHOR.LAST_NAME.like(name + "%"))
                .fetchOne().getValue(0, Long.class);
    }

    @Override
    public boolean authorExists(Integer id) {
        return factory.selectFrom(T_AUTHOR).where(T_AUTHOR.ID.equal(id)).fetchOne() != null;
    }

    @Override
    public boolean authorExists(Author author) {
        return factory.selectFrom(T_AUTHOR).where(T_AUTHOR.ID.equal(author.getId()))
                .fetchOne() != null;
    }
}
