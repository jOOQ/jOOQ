package org.jooq.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jooq.test.mysql.generatedclasses.tables.TAuthor;
import org.jooq.test.mysql.generatedclasses.tables.TBook;
import org.jooq.util.mysql.MySQLFactory;
import org.jooq.util.mysql.information_schema.tables.Tables;

import com.google.appengine.api.rdbms.AppEngineDriver;

@SuppressWarnings("serial")
public class JOOQTest extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        try {
            DriverManager.registerDriver(new AppEngineDriver());
            Connection c = DriverManager.getConnection(
                "jdbc:google:rdbms://jooq-integration-test:jooq-integration-test/test", "root", "");

            resp.getWriter().println("<style>" +
                "body { font-family: 'Courier New'; } " +
                "th { text-align: left; padding-bottom: 10px; padding-right: 10px; } " +
                "table, pre { margin-left: 20px; }</style>");
            resp.getWriter().println("<h1>Some INFORMATION_SCHEMA meta data, formatted as HTML</h1>");

            MySQLFactory create = new MySQLFactory(c);
            resp.getWriter().println(
            create.select(
                        Tables.TABLE_NAME,
                        Tables.TABLE_ROWS,
                        Tables.TABLE_COMMENT)
                  .from(Tables.TABLES)
                  .where(Tables.TABLE_SCHEMA.equal("test"))
                  .orderBy(Tables.TABLE_NAME)
                  .fetch()
                  .formatHTML());



            resp.getWriter().println("<h1>All books and authors, formatted as text</h1>");
            resp.getWriter().println("<pre>");
            resp.getWriter().println(
            create.select(
                        TAuthor.FIRST_NAME,
                        TAuthor.LAST_NAME,
                        TBook.TITLE)
                   .from(TAuthor.T_AUTHOR)
                   .join(TBook.T_BOOK)
                   .on(TAuthor.ID.equal(TBook.AUTHOR_ID))
                   .orderBy(TBook.TITLE)
                   .fetch()
                   .format());
            resp.getWriter().println("</pre>");
        }
        catch (Exception e) {
            e.printStackTrace(resp.getWriter());
        }
    }
}
