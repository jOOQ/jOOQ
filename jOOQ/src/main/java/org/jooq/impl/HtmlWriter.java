package org.jooq.impl;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.exception.IOException;

import java.io.Writer;
import java.util.List;

class HtmlWriter<R extends Record>  {

    HtmlWriter(Fields<R> fields, List<R> records) {
        this.fields = fields;
        this.records = records;
    }

    private final Fields<R>   fields;
    private final List<R> records;

    final void formatHTML(Writer writer) {
        try {
            writer.append("<table>");
            writer.append("<thead>");
            writer.append("<tr>");

            for (Field<?> field : fields.fields) {
                writer.append("<th>");
                writer.append(field.getName());
                writer.append("</th>");
            }

            writer.append("</tr>");
            writer.append("</thead>");
            writer.append("<tbody>");

            for (Record record : records) {
                writer.append("<tr>");

                for (int index = 0; index < fields.fields.length; index++) {
                    writer.append("<td>");
                    writer.append(ResultImpl.format0(record.getValue(index), false, true));
                    writer.append("</td>");
                }

                writer.append("</tr>");
            }

            writer.append("</tbody>");
            writer.append("</table>");
        }
        catch (java.io.IOException e) {
            throw new IOException("Exception while writing HTML", e);
        }
    }

}
