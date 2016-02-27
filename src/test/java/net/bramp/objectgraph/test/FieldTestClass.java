package net.bramp.objectgraph.test;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
* @author bramp
*/
public class FieldTestClass {
	private Object privateField = new Object();
	protected Object protectedField = new Object();
	public Object publicField = new Object();
	Object defaultField = new Object();
	@SuppressWarnings("unused") public Object nullField;

	public static Object staticField = new Object();

	public transient Object transientField = new Object();


	public Set<Object> allFields() {
		return ImmutableSet.of(
				privateField, protectedField, publicField, defaultField, staticField, transientField
		);
	}
}
