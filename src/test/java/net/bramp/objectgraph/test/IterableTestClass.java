package net.bramp.objectgraph.test;

import java.util.*;

/**
 * Created by gerardl on 14/02/2016.
 */
public class IterableTestClass {

	List<String> stringArrayList = Arrays.asList("String1", "String2");
	Collection<SimpleClass> objectCollection = Arrays.asList(new SimpleClass(1));

	public static class SimpleClass {

		private int Int = 1;

		public SimpleClass(int Int) {
			this.Int = Int;
		}
	}
}
