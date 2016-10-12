package org.jooq.tools;

import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by nfischer on 9/24/2016.
 */
public class ConvertTest {

	@Test
	public void testFromArray(){
		String[] arr = new String[]{"Hello", "World", "!"};
		List convertedList = Convert.convert(arr, List.class);
		assertEquals(ArrayList.class, convertedList.getClass());
		assertTrue(Arrays.equals(arr, convertedList.toArray()));

		Set convertedSet = Convert.convert(arr, Set.class);
		assertEquals(LinkedHashSet.class, convertedSet.getClass());
		assertTrue(Arrays.equals(arr, convertedSet.toArray()));
	}

	@Test
	public void testFromCollection(){
		List<String> list = asList("Hello", "world", "!");
		String[] arr = Convert.convertCollection(list, String[].class);
		assertEquals(list, asList(arr));

		String[] numStrings = new String[]{"1", "2", "3"};
		List<Integer> integerList = asList(1, 2, 3);

		String[] convertedNumString = Convert.convertCollection(integerList, String[].class);
		assertTrue(Arrays.equals(numStrings, convertedNumString));

		assertTrue(Arrays.equals(new String[0], Convert.convertCollection(new LinkedList<Integer>(), String[].class)));
	}
}
