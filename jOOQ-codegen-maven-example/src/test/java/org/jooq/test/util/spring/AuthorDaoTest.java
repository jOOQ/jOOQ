package org.jooq.test.util.spring;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jooq.test.util.spring.domain.Author;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = "classpath:spring-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthorDaoTest {

    @Autowired
    private AuthorDao authorDao;

    @Test
    public void testFindAll() {
        List<Author> authors = authorDao.findAll();
        Assert.assertEquals(2, authors.size());
    }

    @Test
    public void testFindById() {
        Author a = authorDao.findById(1);
        Assert.assertEquals("George", a.getFirstName());
    }

    @Transactional
    @Test
    public void testAdd() {
        Author a = new Author();
        a.setFirstName("Lewis");
        a.setLastName("Carroll");
        authorDao.add(a);
        List<Author> authors = authorDao.findAll();
        Assert.assertEquals(3, authors.size());
    }

    @Transactional
    @Test
    public void testAddBatch() {
        List<Author> authors = new ArrayList<Author>();
        Author a1 = new Author();
        a1.setFirstName("Lewis");
        a1.setLastName("Carroll");
        authors.add(a1);
        Author a2 = new Author();
        a2.setFirstName("Agatha");
        a2.setLastName("Christie");
        authors.add(a2);
        Author a3 = new Author();
        a3.setFirstName("Charles");
        a3.setLastName("Dickens");
        authors.add(a3);
        Author a4 = new Author();
        a4.setFirstName("Mark");
        a4.setLastName("Twain");
        authors.add(a4);
        authorDao.addBatch(authors);
        List<Author> results = authorDao.findAll();
        Assert.assertEquals(6, results.size());
    }

    @Transactional
    @Test
    public void testSaveAuthor() {
        Author author = authorDao.findById(1);
        author.setFirstName("Arthur Conan");
        author.setLastName("Doyle");
        authorDao.save(author);
        Author found = authorDao.findById(1);
        Assert.assertEquals("Arthur Conan", found.getFirstName());
    }

    @Transactional
    @Test
    public void testDeleteAuthor() {
        Author c = authorDao.findById(1);
        authorDao.delete(c);
        List<Author> authors = authorDao.findAll();
        Assert.assertEquals(1, authors.size());
    }

    @Transactional
    @Test
    public void testCountAuthor() {
        Author a = new Author();
        a.setFirstName("Lewis");
        a.setLastName("Carroll");
        authorDao.add(a);
        long count = authorDao.countAuthors();
        List<Author> authors = authorDao.findAll();
        for (Author author : authors) {
            System.out.println(author.getFirstName());
        }
        Assert.assertEquals(3, count);
    }

    @Transactional
    @Test
    public void testCountDistinctAuthor() {
        Author a1 = new Author();
        a1.setFirstName("Calvin");
        a1.setLastName("Coolidge");
        authorDao.add(a1);
        Author a2 = new Author();
        a2.setFirstName("James Fenimore");
        a2.setLastName("Cooper");
        authorDao.add(a2);
        long count = authorDao.countDistinctForLastName("Coo");
        Assert.assertEquals(2, count);
    }

    @Test
    public void testAuthorExists() {
        Author c = authorDao.findById(1);
        boolean exists = authorDao.authorExists(c);
        Assert.assertTrue(exists);
    }

    @Test
    public void testAuthorNotExists() {
        boolean exists = authorDao.authorExists(1000);
        Assert.assertFalse(exists);
    }
}
