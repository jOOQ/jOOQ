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

import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.ParserException;

/**
 * A command line interface to the Parser API, which works in a similar way as
 * <a href="https://www.jooq.org/translate">https://www.jooq.org/translate</a>.
 *
 * @author Lukas Eder
 */
public final class ParserCLI {

    public static final void main(String[] args) {
        Args a;

        try {
            a = parse(args);

            if (a.toDialect == null || a.sql == null) {
                System.err.println("Mandatory arguments: -t and -s");
                throw new RuntimeException();
            }
        }
        catch (Exception e) {
            System.exit(-1);
            return;
        }

        Settings settings = new Settings();

        if (a.formatted)
            settings.setRenderFormatted(true);
        if (a.keywords != null)
            settings.setRenderKeywordStyle(a.keywords);
        if (a.name != null)
            settings.setRenderNameStyle(a.name);

        DSLContext ctx = DSL.using(a.toDialect, settings);
        try {
            System.out.println(ctx.render(ctx.parser().parse(a.sql)));
        }
        catch (ParserException e1) {
            ParserException e = e1;

            if (!a.sql.trim().matches("^(?is:(ALTER|BEGIN|COMMENT|CREATE|DECLARE|DELETE|DESCRIBE|DROP|GRANT|INSERT|MERGE|RENAME|REVOKE|SELECT|SET|SHOW|TRUNCATE|UPDATE|USE).*)$")) {
                try {
                    System.out.println(ctx.render(ctx.parser().parseField(a.sql)));
                }
                catch (ParserException e2) {
                    e = e1.position() >= e2.position() ? e1 : e2;
                }
            }

            System.err.println(e.getMessage());
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
                    result.keywords = RenderKeywordStyle.valueOf(args[++i]);
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], RenderKeywordStyle.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Flag -k / --keyword requires <RenderKeywordStyle> argument");
                    throw e;
                }
            }
            else if ("-i".equals(args[i]) || "--identifier".equals(args[i])) {
                try {
                    result.keywords = RenderKeywordStyle.valueOf(args[++i]);
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], RenderKeywordStyle.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Flag -i / --identifier requires <RenderNameStyle> argument");
                    throw e;
                }
            }
            else if ("-t".equals(args[i]) || "--to-dialect".equals(args[i])) {
                try {
                    result.toDialect = SQLDialect.valueOf(args[++i]);
                    continue argsLoop;
                }
                catch (IllegalArgumentException e) {
                    invalid(args[i], SQLDialect.class);
                    throw e;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Flag -t / --to-dialect requires <SQLDialect> argument");
                    throw e;
                }
            }
            else if ("-s".equals(args[i]) || "--sql".equals(args[i])) {
                try {
                    result.sql = args[++i];
                    continue argsLoop;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Flag -s / --sql requires <String> argument");
                    throw e;
                }
            }
            else if ("-h".equals(args[i]) || "--help".equals(args[i])) {
                help();
                throw new RuntimeException();
            }
            else {
                System.err.println("Unknown flag: " + args[i] + ". Use -h or --help");
                throw new RuntimeException();
            }
        }

        return result;
    }

    private static void invalid(String string, Class<? extends Enum<?>> type) {
        System.err.println("Invalid " + type.getSimpleName() + ": " + string);
        System.err.println("Possible values:");

        for (Enum<?> e : type.getEnumConstants())
            System.err.println("  " + e.name());
    }

    private static final void help() {
        System.out.println("Usage:");
        System.out.println("  -f / --formatted                        Format output SQL");
        System.out.println("  -h / --help                             Display this help");
        System.out.println("  -k / --keyword    <RenderKeywordStyle>  Specify the output keyword style (org.jooq.conf.RenderKeywordStyle)");
        System.out.println("  -i / --identifier <RenderNameStyle>     Specify the output identifier style (org.jooq.conf.RenderNameStyle)");
        System.out.println("  -t / --to-dialect <SQLDialect>          Specify the output dialect (org.jooq.SQLDialect)");
        System.out.println("  -s / --sql        <String>              Specify the input SQL string");
    }

    public static final class Args {
        String             sql;
        RenderKeywordStyle keywords;
        RenderNameStyle    name;
        SQLDialect         toDialect;
        boolean            formatted;
    }
}
