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
 *
 *
 *
 */
package org.jooq;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.conf.TransformUnneededArithmeticExpressions;
import org.jooq.impl.DSL;
import org.jooq.impl.ParserException;

/**
 * A command line interface to the Parser API, which works in a similar way as
 * <a href="https://www.jooq.org/translate">https://www.jooq.org/translate</a>.
 *
 * @author Lukas Eder
 */
public final class ParserCLI {

    private static final Pattern FLAG = Pattern.compile("^/([\\w\\-]+)(?:\\s+(\\w+))?\\s*$");

    public static final void main(String... args) throws Exception {
        Args a;
        Settings settings = new Settings();
        DSLContext ctx;

        a = parse(args);
        settings(a, settings);
        ctx = ctx(a, settings);

        if (a.interactive || args == null || args.length == 0) {
            interactiveMode(ctx, a);
        }
        else if (a.done) {}
        else if (a.toDialect == null || a.sql == null) {
            System.out.println("Mandatory arguments: -T and -s. Use -h for help");
            throw new RuntimeException();
        }
        else {
            render(ctx, a);
        }
    }

    private static final DSLContext ctx(Args a, Settings settings) {
        return DSL.using(a.toDialect, settings);
    }

    private static final void settings(Args a, Settings settings) {
        if (a.formatted)
            settings.setRenderFormatted(true);
        if (a.keywords != null)
            settings.setRenderKeywordCase(a.keywords);
        if (a.name != null)
            settings.setRenderNameCase(a.name);
        if (a.quoted != null)
            settings.setRenderQuotedNames(a.quoted);
        if (a.fromDialect != null)
            settings.setParseDialect(a.fromDialect);
        if (a.transformAnsiJoinToTableLists)
            settings.setTransformAnsiJoinToTableLists(true);
        if (a.transformTableListsToAnsiJoin)
            settings.setTransformTableListsToAnsiJoin(true);
        if (a.transformUnneededArithmetic != null)
            settings.setTransformUnneededArithmeticExpressions(a.transformUnneededArithmetic);
    }

    private static final void interactiveMode(DSLContext ctx, Args a) {
        Scanner scan = new Scanner(System.in);

        System.out.print("> ");

        cliLoop:
        do {
            String line = scan.nextLine();

            // TODO: Allow reading history again using arrow keys
            // https://stackoverflow.com/q/572001/521799
            a.history.add(line);

            if (a.sql == null && line.startsWith("/")) {
                if ("/q".equals(line) || "/quit".equals(line) ||
                    "/e".equals(line) || "/exit".equals(line)) {
                    System.out.println("Bye");
                    break cliLoop;
                }
                else if ("/?".equals(line) || "/h".equals(line) || "/help".equals(line))
                    helpInteractive();
                else if ("/d".equals(line) || "/display".equals(line))
                    displayArguments(a);
                else {
                    Matcher matcher = FLAG.matcher(line);

                    if (matcher.find()) {
                        String flag = matcher.group(1);
                        String arg = matcher.group(2);

                        if (flag != null) {
                            if ("f".equals(flag) || "formatted".equals(flag)) {
                                if (arg != null)
                                    a.formatted = Boolean.parseBoolean(arg.toLowerCase());

                                displayFormatted(a);
                            }
                            else if ("k".equals(flag) || "keyword".equals(flag)) {
                                try {
                                    if (arg != null)
                                        a.keywords = RenderKeywordCase.valueOf(arg.toUpperCase());

                                    displayKeywords(a);
                                }
                                catch (IllegalArgumentException e) {
                                    invalid(arg, RenderKeywordCase.class);
                                }
                            }
                            else if ("i".equals(flag) || "identifier".equals(flag)) {
                                try {
                                    if (arg != null)
                                        a.name = RenderNameCase.valueOf(arg.toUpperCase());

                                    displayIdentifiers(a);
                                }
                                catch (IllegalArgumentException e) {
                                    invalid(arg, RenderNameCase.class);
                                }
                            }
                            else if ("Q".equals(flag) || "quoted".equals(flag)) {
                                try {
                                    if (arg != null)
                                        a.quoted = RenderQuotedNames.valueOf(arg.toUpperCase());

                                    displayQuoted(a);
                                }
                                catch (IllegalArgumentException e) {
                                    invalid(arg, RenderQuotedNames.class);
                                }
                            }
                            else if ("F".equals(flag) || "from-dialect".equals(flag)) {
                                try {
                                    if (arg != null)
                                        a.fromDialect = SQLDialect.valueOf(arg.toUpperCase());

                                    displayFromDialect(a);
                                }
                                catch (IllegalArgumentException e) {
                                    invalid(arg, SQLDialect.class);
                                }
                            }
                            else if ("transform-ansi-join-to-table-lists".equals(flag)) {
                                if (arg != null)
                                    a.transformAnsiJoinToTableLists = Boolean.parseBoolean(arg.toLowerCase());

                                displayTransformAnsiJoinToTablesLists(a);
                            }
                            else if ("transform-table-lists-to-ansi-join".equals(flag)) {
                                if (arg != null)
                                    a.transformTableListsToAnsiJoin = Boolean.parseBoolean(arg.toLowerCase());

                                displayTransformTableListsToAnsiJoin(a);
                            }
                            else if ("transform-unneeded-arithmetic".equals(flag)) {
                                try {
                                    if (arg != null)
                                        a.transformUnneededArithmetic = TransformUnneededArithmeticExpressions.valueOf(arg.toUpperCase());

                                    displayTransformUnneededArithmetic(a);
                                }
                                catch (IllegalArgumentException e) {
                                    invalid(arg, TransformUnneededArithmeticExpressions.class);
                                }
                            }

                            // [#9144] /t maintained for backwards compatibility
                            else if ("t".equals(flag) || "T".equals(flag) || "to-dialect".equals(flag)) {
                                try {
                                    if (arg != null)
                                        a.toDialect = SQLDialect.valueOf(arg.toUpperCase());

                                    displayToDialect(a);
                                }
                                catch (IllegalArgumentException e) {
                                    invalid(arg, SQLDialect.class);
                                }
                            }
                        }
                    }
                    else {
                        System.out.println("Unrecognised command: " + line);
                        System.out.println("Type /h for help");
                    }
                }

                settings(a, ctx.settings());
                ctx = ctx(a, ctx.settings());
            }

            if (a.sql != null || !line.startsWith("/")) {
                if (a.sql == null)
                    a.sql = line;
                else
                    a.sql = a.sql + "\n" + line;

                if (a.sql.trim().endsWith(";")) {
                    render(ctx, a);
                    a.sql = null;
                    System.out.println();
                }
            }

            System.out.print("> ");
        }
        while (scan.hasNextLine());
    }

    private static final void displayArguments(Args a) {
        displayFormatted(a);
        displayFromDialect(a);
        displayToDialect(a);
        displayKeywords(a);
        displayIdentifiers(a);
        displayQuoted(a);
        displayTransformAnsiJoinToTablesLists(a);
        displayTransformTableListsToAnsiJoin(a);
        displayTransformUnneededArithmetic(a);
    }

    private static void displayIdentifiers(Args a) {
        System.out.println("Identifiers                        : " + a.name);
    }

    private static void displayQuoted(Args a) {
        System.out.println("Quoted                             : " + a.quoted);
    }

    private static void displayKeywords(Args a) {
        System.out.println("Keywords                           : " + a.keywords);
    }

    private static void displayToDialect(Args a) {
        System.out.println("To dialect                         : " + a.toDialect);
    }

    private static void displayFromDialect(Args a) {
        System.out.println("From dialect                       : " + a.fromDialect);
    }

    private static void displayFormatted(Args a) {
        System.out.println("Formatted                          : " + a.formatted);
    }

    private static void displayTransformAnsiJoinToTablesLists(Args a) {
        System.out.println("Transform ANSI join to table lists : " + a.transformAnsiJoinToTableLists);
    }

    private static void displayTransformTableListsToAnsiJoin(Args a) {
        System.out.println("Transform table lists to ANSI join : " + a.transformTableListsToAnsiJoin);
    }

    private static void displayTransformUnneededArithmetic(Args a) {
        System.out.println("Transform unneeded arithmetic      : " + a.transformUnneededArithmetic);
    }

    private static final void render(DSLContext ctx, Args a) {
        String sql = a.sql.trim();

        try {







                System.out.println(ctx.render(ctx.parser().parse(a.sql)));
        }
        catch (ParserException e1) {
            ParserException e = e1;

            if (!sql.matches("^(?is:(?:ALTER|BEGIN|COMMENT|CREATE|DECLARE|DELETE|DESCRIBE|DROP|GRANT|INSERT|MERGE|RENAME|REVOKE|SELECT|SET|SHOW|TRUNCATE|UPDATE|USE).*)$")) {
                try {
                    System.out.println(ctx.render(ctx.parser().parseField(a.sql)));
                }
                catch (ParserException e2) {
                    e = e1.position() >= e2.position() ? e1 : e2;
                }
            }

            System.out.println(e.getMessage());
        }
    }

    private static final Args parse(String[] args) {
        Args result = new Args();

        argsLoop:
        for (int i = 0; i < args.length; i++) {
            if ("-f".equals(args[i]) || "--formatted".equals(args[i])) {
                result.formatted = true;
            }
            else if ("-k".equals(args[i]) || "--keyword".equals(args[i])) {
                try {
                    result.keywords = RenderKeywordCase.valueOf(args[++i].toUpperCase());
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], RenderKeywordCase.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag -k / --keyword requires <RenderKeywordCase> argument");
                    throw e;
                }
            }
            else if ("-i".equals(args[i]) || "--identifier".equals(args[i])) {
                try {
                    result.keywords = RenderKeywordCase.valueOf(args[++i].toUpperCase());
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], RenderKeywordCase.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag -i / --identifier requires <RenderNameCase> argument");
                    throw e;
                }
            }
            else if ("-Q".equals(args[i]) || "--quoted".equals(args[i])) {
                try {
                    result.quoted = RenderQuotedNames.valueOf(args[++i].toUpperCase());
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], RenderQuotedNames.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag -Q / --quoted requires <RenderQuotedNames> argument");
                    throw e;
                }
            }
            else if ("-F".equals(args[i]) || "--from-dialect".equals(args[i])) {
                try {
                    result.fromDialect = SQLDialect.valueOf(args[++i].toUpperCase());
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], SQLDialect.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag -F / --from-dialect requires <SQLDialect> argument");
                    throw e;
                }
            }

            // [#9144] -t maintained for backwards compatibility
            else if ("-t".equals(args[i]) || "-T".equals(args[i]) || "--to-dialect".equals(args[i])) {
                try {
                    result.toDialect = SQLDialect.valueOf(args[++i].toUpperCase());
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], SQLDialect.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag -T / --to-dialect requires <SQLDialect> argument");
                    throw e;
                }
            }
            else if ("--transform-ansi-join-to-table-lists".equals(args[i])) {
                result.transformAnsiJoinToTableLists = true;
            }
            else if ("--transform-table-lists-to-ansi-join".equals(args[i])) {
                result.transformTableListsToAnsiJoin = true;
            }
            else if ("--transform-unneeded-arithmetic".equals(args[i])) {
                try {
                    result.transformUnneededArithmetic = TransformUnneededArithmeticExpressions.valueOf(args[++i].toUpperCase());
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], TransformUnneededArithmeticExpressions.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag --transform-unneeded-arithmetic requires <TransformUnneededArithmeticExpressions> argument");
                    throw e;
                }
            }
            else if ("-s".equals(args[i]) || "--sql".equals(args[i])) {
                try {
                    result.sql = args[++i];
                    continue argsLoop;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag -s / --sql requires <String> argument");
                    throw e;
                }
            }
            else if ("-I".equals(args[i]) || "--interactive".equals(args[i])) {
                result.interactive = true;
            }
            else if ("-h".equals(args[i]) || "--help".equals(args[i])) {
                help();
                result.done = true;
            }
            else {
                System.out.println("Unknown flag: " + args[i] + ". Use -h or --help");
                throw new RuntimeException();
            }
        }

        return result;
    }

    private static final void invalid(String string, Class<? extends Enum<?>> type) {
        System.out.println("Invalid " + type.getSimpleName() + ": " + string);
        System.out.println("Possible values:");

        for (Enum<?> e : type.getEnumConstants())
            System.out.println("  " + e.name());
    }

    private static final void help() {
        System.out.println("Usage:");
        System.out.println("  -f / --formatted                                              Format output SQL");
        System.out.println("  -h / --help                                                   Display this help");
        System.out.println("  -k / --keyword                            <RenderKeywordCase> Specify the output keyword case (org.jooq.conf.RenderKeywordCase)");
        System.out.println("  -i / --identifier                         <RenderNameCase>    Specify the output identifier case (org.jooq.conf.RenderNameCase)");
        System.out.println("  -Q / --quoted                             <RenderQuotedNames> Specify the output identifier quoting (org.jooq.conf.RenderQuotedNames)");
        System.out.println("  -F / --from-dialect                       <SQLDialect>        Specify the input dialect (org.jooq.SQLDialect)");
        System.out.println("  -T / --to-dialect                         <SQLDialect>        Specify the output dialect (org.jooq.SQLDialect)");
        System.out.println("  -s / --sql                                <String>            Specify the input SQL string");
        System.out.println("");
        System.out.println("Commercial distribution only features:");
        System.out.println("  --transform-ansi-join-to-table-lists");
        System.out.println("  --transform-table-lists-to-ansi-join");
        System.out.println("  --transform-unneeded-arithmetic           <TransformUnneededArithmeticExpressions>");
        System.out.println("");
        System.out.println("  -I / --interactive                                            Start interactive mode");
    }

    private static final void helpInteractive() {
        System.out.println("Usage:");
        System.out.println("  /d  or  /display                                             Display arguments");
        System.out.println("  /f  or  /formatted                       <boolean>           Format output SQL");
        System.out.println("  /h  or  /help                                                Display this help");
        System.out.println("  /k  or  /keyword                         <RenderKeywordCase> Specify the output keyword case (org.jooq.conf.RenderKeywordCase)");
        System.out.println("  /i  or  /identifier                      <RenderNameCase>    Specify the output identifier case (org.jooq.conf.RenderNameCase)");
        System.out.println("  /Q  or  /quoted                          <RenderQuotedNames> Specify the output identifier quoting (org.jooq.conf.RenderQuotedNames)");
        System.out.println("  /F  or  /from-dialect                    <SQLDialect>        Specify the input dialect (org.jooq.SQLDialect)");
        System.out.println("  /T  or  /to-dialect                      <SQLDialect>        Specify the output dialect (org.jooq.SQLDialect)");
        System.out.println("                                           <String>            Specify the input SQL string");
        System.out.println("");
        System.out.println("Commercial distribution only features:");
        System.out.println("  /transform-ansi-join-to-table-lists");
        System.out.println("  /transform-table-lists-to-ansi-join");
        System.out.println("  /transform-unneeded-arithmetic           <TransformUnneededArithmeticExpressions>");
        System.out.println("");
        System.out.println("  /q  or  /quit                                                Quit");
        System.out.println("  /e  or  /exit                                                Also quit");
    }

    public static final class Args {
        List<String>                           history                     = new ArrayList<>();
        String                                 sql;
        RenderKeywordCase                      keywords                    = RenderKeywordCase.LOWER;
        RenderNameCase                         name                        = RenderNameCase.LOWER;
        RenderQuotedNames                      quoted                      = RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED;
        SQLDialect                             toDialect                   = SQLDialect.DEFAULT;
        SQLDialect                             fromDialect                 = SQLDialect.DEFAULT;
        boolean                                formatted;
        boolean                                interactive;
        boolean                                done;
        boolean                                transformAnsiJoinToTableLists;
        boolean                                transformTableListsToAnsiJoin;
        TransformUnneededArithmeticExpressions transformUnneededArithmetic = TransformUnneededArithmeticExpressions.NEVER;
    }
}
