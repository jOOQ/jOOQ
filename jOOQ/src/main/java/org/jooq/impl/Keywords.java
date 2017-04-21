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
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.keyword;

import org.jooq.Keyword;

/**
 * An internal keyword cache.
 *
 * @author Lukas Eder
 */
final class Keywords {

    public static final Keyword K_ADD                              = keyword("add");
    public static final Keyword K_ALTER                            = keyword("alter");
    public static final Keyword K_ALTER_COLUMN                     = keyword("alter column");
    public static final Keyword K_ALTER_CONSTRAINT                 = keyword("alter constraint");
    public static final Keyword K_ALTER_INDEX                      = keyword("alter index");
    public static final Keyword K_ALTER_SCHEMA                     = keyword("alter schema");
    public static final Keyword K_ALTER_TABLE                      = keyword("alter table");
    public static final Keyword K_AND                              = keyword("and");
    public static final Keyword K_ARRAY                            = keyword("array");
    public static final Keyword K_AS                               = keyword("as");
    public static final Keyword K_AS_OF                            = keyword("as of");
    public static final Keyword K_AUTO_INCREMENT                   = keyword("auto_increment");
    public static final Keyword K_AUTOINCREMENT                    = keyword("autoincrement");
    public static final Keyword K_BEGIN                            = keyword("begin");
    public static final Keyword K_BEGIN_CATCH                      = keyword("begin catch");
    public static final Keyword K_BEGIN_TRY                        = keyword("begin try");
    public static final Keyword K_BETWEEN                          = keyword("between");
    public static final Keyword K_BLOB                             = keyword("blob");
    public static final Keyword K_BOOLEAN                          = keyword("boolean");
    public static final Keyword K_BY                               = keyword("by");
    public static final Keyword K_CASCADE                          = keyword("cascade");
    public static final Keyword K_CASE                             = keyword("case");
    public static final Keyword K_CAST                             = keyword("cast");
    public static final Keyword K_CHANGE_COLUMN                    = keyword("change column");
    public static final Keyword K_CHECK                            = keyword("check");
    public static final Keyword K_COALESCE                         = keyword("coalesce");
    public static final Keyword K_COLUMNS                          = keyword("columns");
    public static final Keyword K_CONNECT_BY                       = keyword("connect by");
    public static final Keyword K_CONSTRAINT                       = keyword("constraint");
    public static final Keyword K_CONTINUE_IDENTITY                = keyword("continue identity");
    public static final Keyword K_CREATE                           = keyword("create");
    public static final Keyword K_CREATE_SCHEMA                    = keyword("create schema");
    public static final Keyword K_CREATE_VIEW                      = keyword("create view");
    public static final Keyword K_CROSS_JOIN_LATERAL               = keyword("cross join lateral");
    public static final Keyword K_CURRENT_ROW                      = keyword("current row");
    public static final Keyword K_DATE                             = keyword("date");
    public static final Keyword K_DATETIME                         = keyword("datetime");
    public static final Keyword K_DECIMAL                          = keyword("decimal");
    public static final Keyword K_DECLARE                          = keyword("declare");
    public static final Keyword K_DEFAULT                          = keyword("default");
    public static final Keyword K_DEFAULT_VALUES                   = keyword("default values");
    public static final Keyword K_DELETE                           = keyword("delete");
    public static final Keyword K_DELETE_WHERE                     = keyword("delete where");
    public static final Keyword K_DENSE_RANK                       = keyword("dense_rank");
    public static final Keyword K_DISTINCT                         = keyword("distinct");
    public static final Keyword K_DISTINCT_ON                      = keyword("distinct on");
    public static final Keyword K_DO                               = keyword("do");
    public static final Keyword K_DO_NOTHING                       = keyword("do nothing");
    public static final Keyword K_DO_UPDATE                        = keyword("do update");
    public static final Keyword K_DROP                             = keyword("drop");
    public static final Keyword K_DROP_COLUMN                      = keyword("drop column");
    public static final Keyword K_DROP_CONSTRAINT                  = keyword("drop constraint");
    public static final Keyword K_DROP_INDEX                       = keyword("drop index");
    public static final Keyword K_DROP_SCHEMA                      = keyword("drop schema");
    public static final Keyword K_DROP_TABLE                       = keyword("drop table");
    public static final Keyword K_DROP_VIEW                        = keyword("drop view");
    public static final Keyword K_ELSE                             = keyword("else");
    public static final Keyword K_END                              = keyword("end");
    public static final Keyword K_END_CATCH                        = keyword("end catch");
    public static final Keyword K_END_IF                           = keyword("end if");
    public static final Keyword K_END_LOOP                         = keyword("end loop");
    public static final Keyword K_END_TRY                          = keyword("end try");
    public static final Keyword K_ESCAPE                           = keyword("escape");
    public static final Keyword K_EXCEPTION                        = keyword("exception");
    public static final Keyword K_EXEC                             = keyword("exec");
    public static final Keyword K_EXECUTE_BLOCK                    = keyword("execute block");
    public static final Keyword K_EXECUTE_IMMEDIATE                = keyword("execute immediate");
    public static final Keyword K_EXECUTE_STATEMENT                = keyword("execute statement");
    public static final Keyword K_EXISTS                           = keyword("exists");
    public static final Keyword K_FALSE                            = keyword("false");
    public static final Keyword K_FETCH_FIRST                      = keyword("fetch first");
    public static final Keyword K_FETCH_NEXT                       = keyword("fetch next");
    public static final Keyword K_FILTER                           = keyword("filter");
    public static final Keyword K_FINAL_TABLE                      = keyword("final table");
    public static final Keyword K_FIRST                            = keyword("first");
    public static final Keyword K_FOLLOWING                        = keyword("following");
    public static final Keyword K_FOR                              = keyword("for");
    public static final Keyword K_FOR_SHARE                        = keyword("for share");
    public static final Keyword K_FOR_UPDATE                       = keyword("for update");
    public static final Keyword K_FOREIGN_KEY                      = keyword("foreign key");
    public static final Keyword K_FROM                             = keyword("from");
    public static final Keyword K_GENERATED_BY_DEFAULT_AS_IDENTITY = keyword("generated by default as identity");
    public static final Keyword K_GLOBAL_TEMPORARY                 = keyword("global temporary");
    public static final Keyword K_GROUP_BY                         = keyword("group by");
    public static final Keyword K_HAVING                           = keyword("having");
    public static final Keyword K_HOUR_TO_SECOND                   = keyword("hour to second");
    public static final Keyword K_IDENTITY                         = keyword("identity");
    public static final Keyword K_IF                               = keyword("if");
    public static final Keyword K_IF_EXISTS                        = keyword("if exists");
    public static final Keyword K_IF_NOT_EXISTS                    = keyword("if not exists");
    public static final Keyword K_IGNORE                           = keyword("ignore");
    public static final Keyword K_IGNORE_NULLS                     = keyword("ignore nulls");
    public static final Keyword K_IMMEDIATE                        = keyword("immediate");
    public static final Keyword K_IN                               = keyword("in");
    public static final Keyword K_INDEX                            = keyword("index");
    public static final Keyword K_INNER_JOIN                       = keyword("inner join");
    public static final Keyword K_INSERT                           = keyword("insert");
    public static final Keyword K_INT                              = keyword("int");
    public static final Keyword K_INTO                             = keyword("into");
    public static final Keyword K_IS_NOT_NULL                      = keyword("is not null");
    public static final Keyword K_IS_NULL                          = keyword("is null");
    public static final Keyword K_KEEP                             = keyword("keep");
    public static final Keyword K_KEY                              = keyword("key");
    public static final Keyword K_LAST                             = keyword("last");
    public static final Keyword K_LATERAL                          = keyword("lateral");
    public static final Keyword K_LEFT_OUTER_JOIN_LATERAL          = keyword("left outer join lateral");
    public static final Keyword K_LIKE                             = keyword("like");
    public static final Keyword K_LIKE_REGEX                       = keyword("like_regex");
    public static final Keyword K_LIMIT                            = keyword("limit");
    public static final Keyword K_LOCK_IN_SHARE_MODE               = keyword("lock in share mode");
    public static final Keyword K_LOOP                             = keyword("loop");
    public static final Keyword K_MERGE_INTO                       = keyword("merge into");
    public static final Keyword K_MINUS                            = keyword("minus");
    public static final Keyword K_MODIFY                           = keyword("modify");
    public static final Keyword K_NOCYCLE                          = keyword("nocycle");
    public static final Keyword K_NOT                              = keyword("not");
    public static final Keyword K_NOT_EXISTS                       = keyword("not exists");
    public static final Keyword K_NOT_IN                           = keyword("not in");
    public static final Keyword K_NOT_NULL                         = keyword("not null");
    public static final Keyword K_NULL                             = keyword("null");
    public static final Keyword K_NULLS_FIRST                      = keyword("nulls first");
    public static final Keyword K_NULLS_LAST                       = keyword("nulls last");
    public static final Keyword K_OF                               = keyword("of");
    public static final Keyword K_OFFSET                           = keyword("offset");
    public static final Keyword K_ON                               = keyword("on");
    public static final Keyword K_ON_COMMIT_DELETE_ROWS            = keyword("on commit delete rows");
    public static final Keyword K_ON_COMMIT_DROP                   = keyword("on commit drop");
    public static final Keyword K_ON_COMMIT_PRESERVE_ROWS          = keyword("on commit preserve rows");
    public static final Keyword K_ON_CONFLICT                      = keyword("on conflict");
    public static final Keyword K_ON_DELETE                        = keyword("on delete");
    public static final Keyword K_ON_DUPLICATE_KEY_UPDATE          = keyword("on duplicate key update");
    public static final Keyword K_ON_UPDATE                        = keyword("on update");
    public static final Keyword K_OPEN                             = keyword("open");
    public static final Keyword K_OR                               = keyword("or");
    public static final Keyword K_ORDER                            = keyword("order");
    public static final Keyword K_ORDER_BY                         = keyword("order by");
    public static final Keyword K_OVER                             = keyword("over");
    public static final Keyword K_OVERLAPS                         = keyword("overlaps");
    public static final Keyword K_PARTITION_BY                     = keyword("partition by");
    public static final Keyword K_PASSING                          = keyword("passing");
    public static final Keyword K_PERCENT                          = keyword("percent");
    public static final Keyword K_PIVOT                            = keyword("pivot");
    public static final Keyword K_PRECEDING                        = keyword("preceding");
    public static final Keyword K_PRIMARY_KEY                      = keyword("primary key");
    public static final Keyword K_PRIOR                            = keyword("prior");
    public static final Keyword K_RAISE                            = keyword("raise");
    public static final Keyword K_RECURSIVE                        = keyword("recursive");
    public static final Keyword K_REFERENCES                       = keyword("references");
    public static final Keyword K_REGEXP                           = keyword("regexp");
    public static final Keyword K_RENAME                           = keyword("rename");
    public static final Keyword K_RENAME_COLUMN                    = keyword("rename column");
    public static final Keyword K_RENAME_CONSTRAINT                = keyword("rename constraint");
    public static final Keyword K_RENAME_INDEX                     = keyword("rename index");
    public static final Keyword K_RENAME_TABLE                     = keyword("rename table");
    public static final Keyword K_RENAME_TO                        = keyword("rename to");
    public static final Keyword K_RESPECT_NULLS                    = keyword("respect nulls");
    public static final Keyword K_RESTART                          = keyword("restart");
    public static final Keyword K_RESTART_IDENTITY                 = keyword("restart identity");
    public static final Keyword K_RESTART_WITH                     = keyword("restart with");
    public static final Keyword K_RESTRICT                         = keyword("restrict");
    public static final Keyword K_RETURNING                        = keyword("returning");
    public static final Keyword K_ROW                              = keyword("row");
    public static final Keyword K_ROWCOUNT                         = keyword("rowcount");
    public static final Keyword K_ROWS                             = keyword("rows");
    public static final Keyword K_ROWS_FROM                        = keyword("rows from");
    public static final Keyword K_ROWS_ONLY                        = keyword("rows only");
    public static final Keyword K_SELECT                           = keyword("select");
    public static final Keyword K_SEPARATOR                        = keyword("separator");
    public static final Keyword K_SEQUENCE                         = keyword("sequence");
    public static final Keyword K_SERIAL                           = keyword("serial");
    public static final Keyword K_SERIAL8                          = keyword("serial8");
    public static final Keyword K_SET                              = keyword("set");
    public static final Keyword K_SET_DATA_TYPE                    = keyword("set data type");
    public static final Keyword K_SET_DEFAULT                      = keyword("set default");
    public static final Keyword K_SIBLINGS                         = keyword("siblings");
    public static final Keyword K_SKIP                             = keyword("skip");
    public static final Keyword K_SQL                              = keyword("sql");
    public static final Keyword K_START_AT                         = keyword("start at");
    public static final Keyword K_START_WITH                       = keyword("start with");
    public static final Keyword K_SWITCH                           = keyword("switch");
    public static final Keyword K_SYMMETRIC                        = keyword("symmetric");
    public static final Keyword K_TABLE                            = keyword("table");
    public static final Keyword K_TEMPORARY                        = keyword("temporary");
    public static final Keyword K_THEN                             = keyword("then");
    public static final Keyword K_THROW                            = keyword("throw");
    public static final Keyword K_TIME                             = keyword("time");
    public static final Keyword K_TIME_WITH_TIME_ZONE              = keyword("time with time zone");
    public static final Keyword K_TIMESTAMP                        = keyword("timestamp");
    public static final Keyword K_TIMESTAMP_WITH_TIME_ZONE         = keyword("timestamp with time zone");
    public static final Keyword K_TO                               = keyword("to");
    public static final Keyword K_TOP                              = keyword("top");
    public static final Keyword K_TRIM                             = keyword("trim");
    public static final Keyword K_TRUE                             = keyword("true");
    public static final Keyword K_TRUNCATE_TABLE                   = keyword("truncate table");
    public static final Keyword K_TYPE                             = keyword("type");
    public static final Keyword K_UNBOUNDED_FOLLOWING              = keyword("unbounded following");
    public static final Keyword K_UNBOUNDED_PRECEDING              = keyword("unbounded preceding");
    public static final Keyword K_UNIQUE                           = keyword("unique");
    public static final Keyword K_UNNEST                           = keyword("unnest");
    public static final Keyword K_UPDATE                           = keyword("update");
    public static final Keyword K_UPSERT                           = keyword("upsert");
    public static final Keyword K_USING                            = keyword("using");
    public static final Keyword K_USING_INDEX                      = keyword("using index");
    public static final Keyword K_VALUES                           = keyword("values");
    public static final Keyword K_VARCHAR                          = keyword("varchar");
    public static final Keyword K_VERSIONS_BETWEEN                 = keyword("versions between");
    public static final Keyword K_VIEW                             = keyword("view");
    public static final Keyword K_WHEN                             = keyword("when");
    public static final Keyword K_WHEN_MATCHED_THEN_UPDATE_SET     = keyword("when matched then update set");
    public static final Keyword K_WHEN_NOT_MATCHED_THEN_INSERT     = keyword("when not matched then insert");
    public static final Keyword K_WHERE                            = keyword("where");
    public static final Keyword K_WINDOW                           = keyword("window");
    public static final Keyword K_WITH                             = keyword("with");
    public static final Keyword K_WITH_CHECK_OPTION                = keyword("with check option");
    public static final Keyword K_WITH_DATA                        = keyword("with data");
    public static final Keyword K_WITH_LOCK                        = keyword("with lock");
    public static final Keyword K_WITH_PRIMARY_KEY                 = keyword("with primary key");
    public static final Keyword K_WITH_READ_ONLY                   = keyword("with read only");
    public static final Keyword K_WITH_ROLLUP                      = keyword("with rollup");
    public static final Keyword K_WITHIN_GROUP                     = keyword("within group");
    public static final Keyword K_XMLTABLE                         = keyword("xmltable");
    public static final Keyword K_YEAR_TO_DAY                      = keyword("year to day");
    public static final Keyword K_YEAR_TO_FRACTION                 = keyword("year to fraction");

    private Keywords() {}
}
