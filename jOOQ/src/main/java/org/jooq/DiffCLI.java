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

import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

/**
 * A command line interface to the Parser API, which works in a similar way as
 * <a href="https://www.jooq.org/translate">https://www.jooq.org/translate</a>.
 *
 * @author Lukas Eder
 */
public final class DiffCLI {

    public static final void main(String... args) throws Exception {
        Args a;
        Settings settings = new Settings();
        DSLContext ctx;

        a = parse(args);
        settings(a, settings);
        ctx = ctx(a, settings);

        if (a.done) {}
        else if (a.toDialect == null || a.sql1 == null || a.sql2 == null) {
            System.out.println("Mandatory arguments: -T and -1, -2. Use -h for help");
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
        if (a.fromDialect != null)
            settings.setParseDialect(a.fromDialect);
    }

    private static final void render(DSLContext ctx, Args a) {
        String sql1 = a.sql1.trim();
        String sql2 = a.sql2.trim();

        System.out.println(ctx.render(ctx.meta(sql1).migrateTo(ctx.meta(sql2))));
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
            else if ("-1".equals(args[i]) || "--sql1".equals(args[i])) {
                try {
                    result.sql1 = args[++i];
                    continue argsLoop;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag -1 / --sql1 requires <String> argument");
                    throw e;
                }
            }
            else if ("-2".equals(args[i]) || "--sql2".equals(args[i])) {
                try {
                    result.sql2 = args[++i];
                    continue argsLoop;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Flag -2 / --sql2 requires <String> argument");
                    throw e;
                }
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
        System.out.println("  -f / --formatted                        Format output SQL");
        System.out.println("  -h / --help                             Display this help");
        System.out.println("  -k / --keyword      <RenderKeywordCase> Specify the output keyword case (org.jooq.conf.RenderKeywordCase)");
        System.out.println("  -i / --identifier   <RenderNameCase>    Specify the output identifier case (org.jooq.conf.RenderNameCase)");
        System.out.println("  -F / --from-dialect <SQLDialect>        Specify the input dialect (org.jooq.SQLDialect)");
        System.out.println("  -T / --to-dialect   <SQLDialect>        Specify the output dialect (org.jooq.SQLDialect)");
        System.out.println("  -1 / --sql1         <String>            Specify the input SQL string 1 (from SQL)");
        System.out.println("  -2 / --sql2         <String>            Specify the input SQL string 2 (to SQL)");
    }

    public static final class Args {
        String            sql1;
        String            sql2;
        RenderKeywordCase keywords    = RenderKeywordCase.LOWER;
        RenderNameCase    name        = RenderNameCase.LOWER;
        SQLDialect        toDialect   = SQLDialect.DEFAULT;
        SQLDialect        fromDialect = SQLDialect.DEFAULT;
        boolean           formatted;
        boolean           done;
    }
}
