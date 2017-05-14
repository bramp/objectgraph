package net.bramp.objectgraph.test;

/** @author bramp */
public class ClassTestClass {

  public static class Root {}

  public static class Child extends Root {}

  public static class ChildChild extends Child {}

  public Root r = new Root();
  public Child c = new Child();
  public ChildChild cc = new ChildChild();

  // Duplicate the fields, but this time with the filed type as object
  public Object r1 = r;
  public Object c1 = c;
  public Object cc1 = cc;
}
