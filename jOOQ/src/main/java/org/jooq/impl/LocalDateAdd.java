package org.jooq.impl;

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.keyword;

import java.time.LocalDate;

import org.jooq.Configuration;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.QueryPart;

/**
 * @author Miguel Gonzalez Sanchez
 */
final class LocalDateAdd extends AbstractFunction<LocalDate> {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -7700140530346259350L;

	private final Field<LocalDate> date;
	private final Field<? extends Number> interval;
	private final DatePart datePart;

	LocalDateAdd(Field<LocalDate> date, Field<? extends Number> interval, DatePart datePart) {
		super("localDateAdd", date.getDataType().getSQLDataType());

		this.date = date;
		this.interval = interval;
		this.datePart = datePart;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	final QueryPart getFunction0(Configuration configuration) {
		String keyword = null;

		switch (configuration.family()) {
		case CUBRID:
		case MARIADB:
		case MYSQL: {
			switch (datePart) {
			case YEAR:
				keyword = "year";
				break;
			case MONTH:
				keyword = "month";
				break;
			case DAY:
				keyword = "day";
				break;
			case HOUR:
				keyword = "hour";
				break;
			case MINUTE:
				keyword = "minute";
				break;
			case SECOND:
				keyword = "second";
				break;
			default:
				throwUnsupported();
			}

			return DSL.field("{date_add}({0}, {interval} {1} {2})", getDataType(), date, interval, keyword(keyword));
		}

		case DERBY:
		case HSQLDB: {
			switch (datePart) {
			case YEAR:
				keyword = "sql_tsi_year";
				break;
			case MONTH:
				keyword = "sql_tsi_month";
				break;
			case DAY:
				keyword = "sql_tsi_day";
				break;
			case HOUR:
				keyword = "sql_tsi_hour";
				break;
			case MINUTE:
				keyword = "sql_tsi_minute";
				break;
			case SECOND:
				keyword = "sql_tsi_second";
				break;
			default:
				throwUnsupported();
			}

			return DSL.field("{fn {timestampadd}({0}, {1}, {2}) }", getDataType(), keyword(keyword), interval, date);
		}

		case FIREBIRD: {
			switch (datePart) {
			case YEAR:
				keyword = "year";
				break;
			case MONTH:
				keyword = "month";
				break;
			case DAY:
				keyword = "day";
				break;
			case HOUR:
				keyword = "hour";
				break;
			case MINUTE:
				keyword = "minute";
				break;
			case SECOND:
				keyword = "second";
				break;
			default:
				throwUnsupported();
			}

			return DSL.field("{dateadd}({0}, {1}, {2})", getDataType(), keyword(keyword), interval, date);
		}

		case H2: {
			switch (datePart) {
			case YEAR:
				keyword = "year";
				break;
			case MONTH:
				keyword = "month";
				break;
			case DAY:
				keyword = "day";
				break;
			case HOUR:
				keyword = "hour";
				break;
			case MINUTE:
				keyword = "minute";
				break;
			case SECOND:
				keyword = "second";
				break;
			default:
				throwUnsupported();
			}

			return DSL.field("{dateadd}({0}, {1}, {2})", getDataType(), inline(keyword), interval, date);
		}

		case POSTGRES: {
			switch (datePart) {
			case YEAR:
				keyword = " year";
				break;
			case MONTH:
				keyword = " month";
				break;
			case DAY:
				keyword = " day";
				break;
			case HOUR:
				keyword = " hour";
				break;
			case MINUTE:
				keyword = " minute";
				break;
			case SECOND:
				keyword = " second";
				break;
			default:
				throwUnsupported();
			}

			return DSL.field("({0} + ({1} || {2})::interval)::date", getDataType(), date, interval, inline(keyword));
		}

		case SQLITE: {
			switch (datePart) {
			case YEAR:
				keyword = " year";
				break;
			case MONTH:
				keyword = " month";
				break;
			case DAY:
				keyword = " day";
				break;
			case HOUR:
				keyword = " hour";
				break;
			case MINUTE:
				keyword = " minute";
				break;
			case SECOND:
				keyword = " second";
				break;
			default:
				throwUnsupported();
			}

			return DSL.field("{datetime}({0}, '+' || {1} || {2})", getDataType(), date, interval, inline(keyword));
		}

		}

		return null;
	}

	private final void throwUnsupported() {
		throw new UnsupportedOperationException("Unknown date part : " + datePart);
	}
}