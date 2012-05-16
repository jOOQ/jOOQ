package org.jooq.test.util.spring;

import java.util.List;

import org.jooq.test.util.spring.domain.Author;

public interface AuthorDao {

    Integer add(Author author);

    void addBatch(List<Author> authors);

    void save(Author author);

    void delete(Author author);

    Author findById(Integer id);

    List<Author> findAll();

    long countAuthors();

    long countDistinctForLastName(String name);

    boolean authorExists(Integer id);

    boolean authorExists(Author c);

}
