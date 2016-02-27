package net.bramp.objectgraph.test;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * @author bramp
 */
public class SubFieldTestClass extends FieldTestClass {
  Object subField = new Object();

  public Set<Object> allFields() {
    return ImmutableSet.builder()
        .addAll(super.allFields())
        .add(subField).build();
  }
}
