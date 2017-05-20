package org.jooq.util.postgresplus;

import org.jooq.util.postgresplus.pg_catalog.tables.EdbPackage;
import org.jooq.util.postgresplus.pg_catalog.tables.PgNamespace;
import org.jooq.util.postgresplus.pg_catalog.tables.PgProc;
import org.jooq.CommonTableExpression;
import org.jooq.Record12;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractPackageDefinition;
import org.jooq.util.AttributeDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
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
public class PostgresPlusPackageDefinition extends AbstractPackageDefinition {
    public PostgresPlusPackageDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> routineDefinitions = new ArrayList<RoutineDefinition>();

        Routines r = ROUTINES.as("r");
        PgNamespace n = PG_NAMESPACE.as("n");
        PgProc p = PG_PROC.as("p");
        EdbPackage pkg = EDB_PACKAGE.as("pkg");

        CommonTableExpression<Record12<String, String, String, String, String, String, Integer, Integer, Integer, String, String, Boolean>> routines = name("routines").as(
                select(
                        n.NSPNAME,
                        pkg.PKGNAME,
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
                        .join(pkg).on(r.SPECIFIC_SCHEMA.eq(pkg.PKGNAME))
                        .join(n).on(pkg.PKGNAMESPACE.eq(oid(n.asTable())))
                        .join(p).on(oid(pkg.asTable()).eq(p.PRONAMESPACE))
                        .and(p.PRONAME.concat("_").concat(oid(p.asTable())).eq(r.SPECIFIC_NAME))
                        .where(n.NSPNAME.in(getSchema().getName()))
                        .and(pkg.PKGNAME.eq(getName()))
                        .andNot(r.DATA_TYPE.eq("trigger"))
                        .andNot(p.PRORETSET)
                        .orderBy(r.ROUTINE_SCHEMA.asc(), pkg.PKGNAME.asc(), r.ROUTINE_NAME.asc())
        );

        routineDefinitions.addAll(create()
                .with(routines)
                .select(
                        routines.field(n.NSPNAME),
                        routines.field(pkg.PKGNAME),
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
                                        routines.field(n.NSPNAME),
                                        routines.field(pkg.PKGNAME),
                                        routines.field(r.ROUTINE_NAME),
                                        routines.field(r.SPECIFIC_NAME),
                                        routines.field("data_type", String.class))
                        ).eq(1), val(null, Integer.class))
                                .otherwise(rowNumber().over(
                                        partitionBy(
                                                routines.field(n.NSPNAME),
                                                routines.field(pkg.PKGNAME),
                                                routines.field(r.ROUTINE_NAME),
                                                routines.field(r.SPECIFIC_NAME),
                                                routines.field("data_type", String.class))))
                                .as("overload"),
                        routines.field(p.PROISAGG))
                .from(routines).fetch().stream().map(record ->
                        new PostgresPlusRoutineDefinition(getSchema(), this, record)
                ).collect(Collectors.toList()));

        return routineDefinitions;
    }

    @Override
    protected List<AttributeDefinition> getConstants0() throws SQLException {
        return new ArrayList<>();
    }

}
