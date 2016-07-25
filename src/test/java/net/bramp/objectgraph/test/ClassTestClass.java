package net.bramp.objectgraph.test;

/**
 * @author bramp
 */
public class ClassTestClass {

  static public class Root {
  }
  static public class Child extends Root {
  }
  static public class ChildChild extends Child {
  }

  public Root r = new Root();
  public Child c = new Child();
  public ChildChild cc = new ChildChild();

  // Duplicate the fields, but this time with the filed type as object
  public Object r1 = r;
  public Object c1 = c;
  public Object cc1 = cc;
}
