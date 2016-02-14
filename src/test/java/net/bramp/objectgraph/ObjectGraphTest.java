package net.bramp.objectgraph;

import net.bramp.objectgraph.test.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectGraphTest {

	FieldTestClass fieldTest = new FieldTestClass();
	PrimitiveTestClass primitiveTest = new PrimitiveTestClass();
	LoopTestClass loopTest = new LoopTestClass();
	ArrayTestClass arrayTest = new ArrayTestClass();
	IterableTestClass iterableTest = new IterableTestClass();

	@Before
	public void before() {
		loopTest.child = new LoopTestClass();
		loopTest.child.child = loopTest;

	}

	@Test
	public void testFieldTypes() {

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.includeStatic()
			.includeTransient()
			.traverse(fieldTest);

		assertTrue(visitor.found.containsAll(fieldTest.allFields()));
	}

	@Test
	public void testNoStaticTypes() {

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.excludeStatic()
			.includeTransient()
			.traverse(fieldTest);

		assertFalse(visitor.found.contains(FieldTestClass.staticField));
	}

	@Test
	public void testNoTransientTypes() {

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.includeStatic()
			.excludeTransient()
			.traverse(fieldTest);

		assertFalse(visitor.found.contains(fieldTest.transientField));
	}

	@Test
	public void testPrimitivesTypes() {

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.traverse(primitiveTest);

		assertFalse(visitor.found.isEmpty());
	}

	@Test
	public void testLoops() {

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.traverse(loopTest);

		assertTrue(visitor.found.size() == 2);
	}

	@Test
	public void testArrays() {

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.traverse(arrayTest);
	}

	@Test
	public void testIterables() {

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
				.traverse(iterableTest);
	}

	@Test
	public void testThis() {

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.traverse(this);
	}

}