package net.bramp.objectgraph;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
* @author bramp
*/
class TestVisitor implements ObjectGraph.Visitor {
	public final Set<Object> found = Sets.newIdentityHashSet();

	@Override
	public boolean visit(Object object, Class clazz) {

		System.out.println(clazz.toString() + " " + object.toString());

		assertNotNull(object);
		assertNotNull(clazz);

		assertFalse(found.contains(object));

		found.add(object);
		return false;
	}
}
