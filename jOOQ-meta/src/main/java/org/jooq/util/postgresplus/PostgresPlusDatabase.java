package org.jooq.util.postgresplus;

import org.jooq.util.postgresplus.pg_catalog.tables.PgNamespace;
import org.jooq.util.postgresplus.pg_catalog.tables.PgProc;
import org.jooq.CommonTableExpression;
import org.jooq.Record11;
import org.jooq.impl.DSL;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.postgres.PostgresDatabase;
import org.jooq.util.postgres.PostgresRoutineDefinition;
import org.jooq.util.postgres.information_schema.tables.Routines;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.util.postgresplus.pg_catalog.Tables.*;
import static org.jooq.impl.DSL.*;
import static org.jooq.util.postgres.PostgresDSL.oid;
import static org.jooq.util.postgres.information_schema.Tables.PARAMETERS;
import static org.jooq.util.postgres.information_schema.Tables.ROUTINES;

/**
 * Created by rbellamy on 1/4/16.
 */
public class PostgresPlusDatabase extends PostgresDatabase {
    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> routineDefinitions = new ArrayList<RoutineDefinition>();

        Routines r = ROUTINES.as("r");
        PgNamespace n = PG_NAMESPACE.as("n");
        PgProc p = PG_PROC.as("p");

        CommonTableExpression<Record11<String, String, String, String, String, Integer, Integer, Integer, String, String, Boolean>> routines = name("routines").as(
                select( // schema-level routines
                        r.ROUTINE_SCHEMA,
                        r.ROUTINE_NAME,
                        r.SPECIFIC_NAME,
                        field("edb_get_function_arguments({0})", String.class, oid(p.asTable())).as("arguments"),

                        // Ignore the data type when there is at least one out parameter
                        decode()
                                .when(DSL.exists(
                                        selectOne()
                                                .from(PARAMETERS)
                                                .where(PARAMETERS.SPECIFIC_SCHEMA.eq(r.SPECIFIC_SCHEMA))
                                                .and(PARAMETERS.SPECIFIC_NAME.eq(r.SPECIFIC_NAME))
                                                .and(upper(PARAMETERS.PARAMETER_MODE).ne("IN"))),
                                        val("void"))
                                .otherwise(r.DATA_TYPE).as("data_type"),
                        r.CHARACTER_MAXIMUM_LENGTH,
                        r.NUMERIC_PRECISION,
                        r.NUMERIC_SCALE,
                        r.TYPE_UDT_SCHEMA,
                        r.TYPE_UDT_NAME,
                        p.PROISAGG)
                        .from(r)
                        .join(n).on(r.SPECIFIC_SCHEMA.eq(n.NSPNAME))
                        .join(p).on(p.PRONAMESPACE.eq(oid(n.asTable())))
                        .and(p.PRONAME.concat("_").concat(oid(p.asTable())).eq(r.SPECIFIC_NAME))
                        .where(r.ROUTINE_SCHEMA.in(getInputSchemata()))
                        .andNot(r.DATA_TYPE.eq("trigger"))
                        .andNot(p.PRORETSET)
                        .orderBy(r.ROUTINE_SCHEMA.asc(), r.ROUTINE_NAME.asc())
        );

        routineDefinitions.addAll(create()
                .with(routines)
                .select(
                        routines.field(r.ROUTINE_SCHEMA),
                        routines.field(r.ROUTINE_NAME),
                        routines.field(r.SPECIFIC_NAME),
                        routines.field("data_type", String.class),
                        routines.field(r.CHARACTER_MAXIMUM_LENGTH),
                        routines.field(r.NUMERIC_PRECISION),
                        routines.field(r.NUMERIC_SCALE),
                        routines.field(r.TYPE_UDT_SCHEMA),
                        routines.field(r.TYPE_UDT_NAME),

                        // calculate the overload index if applicable
                        decode().when(count().over(
                                partitionBy(
                                        routines.field(r.ROUTINE_SCHEMA),
                                        routines.field(r.ROUTINE_NAME),
                                        routines.field(r.SPECIFIC_NAME),
                                        routines.field("data_type", String.class))
                        ).eq(1), val(null, Integer.class))
                                .otherwise(rowNumber().over(
                                        partitionBy(
                                                routines.field(r.ROUTINE_SCHEMA),
                                                routines.field(r.ROUTINE_NAME),
                                                routines.field(r.SPECIFIC_NAME),
                                                routines.field("data_type", String.class))))
                                .as("overload"),
                        routines.field(p.PROISAGG))
                .from(routines).fetch().stream().map(record -> new PostgresRoutineDefinition(this, record)).collect(Collectors.toList()));

        return routineDefinitions;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> packageDefinitions = new ArrayList<>();

        packageDefinitions.addAll(create().select(
                PG_NAMESPACE.NSPNAME,
                EDB_PACKAGE.PKGNAME)
                .from(EDB_PACKAGE)
                .join(PG_NAMESPACE).on(EDB_PACKAGE.PKGNAMESPACE.eq(oid(PG_NAMESPACE)))
                .where(PG_NAMESPACE.NSPNAME.in(getInputSchemata()))
                .fetch()
                .stream()
                .map(record -> new PostgresPlusPackageDefinition(
                        getSchema(record.getValue(PG_NAMESPACE.NSPNAME)),
                        record.getValue(EDB_PACKAGE.PKGNAME),
                        ""))
                .collect(Collectors.toList()));

        return packageDefinitions;
    }
}
