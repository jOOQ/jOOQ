/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.pojos;


import java.sql.Date;

import org.jooq.test.postgres.generatedclasses.enums.UCountry;
import org.jooq.test.postgres.generatedclasses.tables.interfaces.IFGetArrays;
import org.jooq.test.postgres.generatedclasses.udt.records.UStreetTypeRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FGetArrays implements IFGetArrays {

	private static final long serialVersionUID = -968309999;

	private final Integer             id;
	private final String[]            stringArray;
	private final Integer[]           numberArray;
	private final Date[]              dateArray;
	private final UStreetTypeRecord[] udtArray;
	private final UCountry[]          enumArray;
	private final Integer[]           arrayArray;
	private final String[]            numberList;
	private final String[]            stringList;
	private final String[]            dateList;

	public FGetArrays(FGetArrays value) {
		this.id = value.id;
		this.stringArray = value.stringArray;
		this.numberArray = value.numberArray;
		this.dateArray = value.dateArray;
		this.udtArray = value.udtArray;
		this.enumArray = value.enumArray;
		this.arrayArray = value.arrayArray;
		this.numberList = value.numberList;
		this.stringList = value.stringList;
		this.dateList = value.dateList;
	}

	public FGetArrays(
		Integer             id,
		String[]            stringArray,
		Integer[]           numberArray,
		Date[]              dateArray,
		UStreetTypeRecord[] udtArray,
		UCountry[]          enumArray,
		Integer[]           arrayArray,
		String[]            numberList,
		String[]            stringList,
		String[]            dateList
	) {
		this.id = id;
		this.stringArray = stringArray;
		this.numberArray = numberArray;
		this.dateArray = dateArray;
		this.udtArray = udtArray;
		this.enumArray = enumArray;
		this.arrayArray = arrayArray;
		this.numberList = numberList;
		this.stringList = stringList;
		this.dateList = dateList;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public String[] getStringArray() {
		return this.stringArray;
	}

	@Override
	public Integer[] getNumberArray() {
		return this.numberArray;
	}

	@Override
	public Date[] getDateArray() {
		return this.dateArray;
	}

	@Override
	public UStreetTypeRecord[] getUdtArray() {
		return this.udtArray;
	}

	@Override
	public UCountry[] getEnumArray() {
		return this.enumArray;
	}

	@Override
	public Integer[] getArrayArray() {
		return this.arrayArray;
	}

	@Override
	public String[] getNumberList() {
		return this.numberList;
	}

	@Override
	public String[] getStringList() {
		return this.stringList;
	}

	@Override
	public String[] getDateList() {
		return this.dateList;
	}
}
