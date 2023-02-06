package org.jooq.example.picocli;

import org.jooq.CloseableDSLContext;
import org.jooq.impl.DSL;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.sql.SQLException;

@Command
public class Main implements Runnable {

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    @Override
    public void run() {
        try (CloseableDSLContext ctx = DSL.using("jdbc:h2:mem:jooq-example-picocli", "sa", "")) {
            System.out.println(ctx.fetch("select 1"));
        }
    }
}
