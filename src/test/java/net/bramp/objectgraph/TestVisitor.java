package net.bramp.objectgraph;

import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/** @author bramp */
class TestVisitor implements ObjectGraph.Visitor {
  public final Set<Object> found = Sets.newIdentityHashSet();

  @Override
  public boolean visit(Object object, Class<?> clazz) {

    System.out.println(clazz.toString() + " " + object.toString());

    // We expect non-null arguments
    assertThat(object).isNotNull();
    assertThat(clazz).isNotNull();

    // Check we haven't double visited this node
    assertThat(found).usingElementComparator(Ordering.arbitrary()).doesNotContain(object);

    found.add(object);
    return false;
  }
}
