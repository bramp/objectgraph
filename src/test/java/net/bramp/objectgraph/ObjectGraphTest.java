package net.bramp.objectgraph;

import com.google.common.collect.Lists;
import net.bramp.objectgraph.test.ArrayTestClass;
import net.bramp.objectgraph.test.FieldTestClass;
import net.bramp.objectgraph.test.LoopTestClass;
import net.bramp.objectgraph.test.PrimitiveTestClass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class ObjectGraphTest {

	FieldTestClass fieldTest = new FieldTestClass();
	PrimitiveTestClass primitiveTest = new PrimitiveTestClass();
	LoopTestClass loopTest = new LoopTestClass();
	ArrayTestClass arrayTest = new ArrayTestClass();

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
	public void testArrayList() {
		List<Integer> arrayList = Lists.newArrayList(4, 5, 6);

		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.includeTransient()
			.traverse(arrayList);

		assertThat(visitor.found).containsAll(arrayList);
	}

	@Test
	public void testThis() {
		TestVisitor visitor = new TestVisitor();

		ObjectGraph.visitor(visitor)
			.traverse(this);
	}

}
