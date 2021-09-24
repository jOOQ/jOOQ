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
package org.jooq.impl;
























































































            case POSTGRES:
            case YUGABYTE:
                acceptPostgres(ctx);
                break;


            case HSQLDB:
            case MARIADB:
            case MYSQL:
                acceptDefaultPullingUpDeclarations(ctx);
                break;

            default:
                acceptDefault(ctx, value, messageText);
                break;
        }
    }

    private final void acceptPostgres(Context<?> ctx) {
        ctx.visit(K_RAISE).sql(' ').visit(K_SQLSTATE).sql(' ').visit(value);

        if (messageText != null)
            ctx.sql(' ').visit(K_USING).sql(' ').visit(K_MESSAGE).sql(" = ").visit(messageText);
    }

    private final void acceptDefaultPullingUpDeclarations(Context<?> ctx) {
        boolean bv = value instanceof ParamOrVariable;
        boolean bm = messageText == null || messageText instanceof ParamOrVariable;

        if (bv && bm) {
            ctx.paramType(ParamType.INLINED, c -> acceptDefault(c, value, messageText));
        }
        else {
            List<Statement> s = new ArrayList<>();

            Field<?> v = bv ? value : variable("sqlstate", CHAR(5));
            Field<String> m = bm ? messageText : variable("messagetext", VARCHAR);

            if (v != value)
                s.add(declare((Variable) v).set(value));
            if (m != messageText)
                s.add(declare((Variable<String>) m).set(messageText));

            s.add(messageText != null ? signalSQLState(v).setMessageText(m) : signalSQLState(v));
            ctx.visit(begin(s));
        }
    }

    private static final void acceptDefault(Context<?> ctx, Field<?> value, Field<String> messageText) {
        ctx.visit(K_SIGNAL).sql(' ').visit(ctx.family() == HANA ? K_SQL_ERROR_CODE : K_SQLSTATE).sql(' ').visit(value);

        if (messageText != null)
            ctx.sql(' ').visit(K_SET).sql(' ').visit(K_MESSAGE_TEXT).sql(" = ").visit(messageText);
    }



    @Pro
    private final void acceptJava(Context<?> ctx) {
        ctx.sql("signalSQLState(");
        String s = "";
        ctx.sql(s).visit(Val.getJavaValue(value)); s = ", ";
        ctx.sql(s).visit(Val.getJavaValue(messageText)); s = ", ";
        ctx.sql(')');
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof Signal) {
            return
                StringUtils.equals(value, ((Signal) that).value) &&
                StringUtils.equals(messageText, ((Signal) that).messageText)
            ;
        }
        else
            return super.equals(that);
    }
}

/* [/pro] */
