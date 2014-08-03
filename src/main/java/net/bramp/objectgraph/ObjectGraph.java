package net.bramp.objectgraph;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Uses a breath-first search to transverse a object graph.
 * This is done a similar manner to how a library would serialise a object
 *
 * @author bramp
 */
public class ObjectGraph {

	final Map<Object, Class> visited = new IdentityHashMap<Object, Class>();
	final Queue<Object> toVisit = new ArrayDeque<Object>();

	final Visitor visitor;

	boolean excludeTransient  = true;
	boolean excludeStatic     = true;

	// TODO Consider added a excludedClasses setting. We won't decend into these classes
	//Set<Class> excludedClasses;

	public static interface Visitor {
		/**
		 * Called for each Object visited
		 * @param object The object being visited
		 * @param clazz The field type this object was in. This will differ from object.getClass(), when the Clazz is one of the primitive types
		 * @return return true if you wish the graph transversal to stop, otherwise it will continue.
		 */
		public boolean visit(Object object, Class clazz);
	}

	ObjectGraph(Visitor visitor) {
		this.visitor = visitor;
	}

	static ObjectGraph visitor(Visitor visitor) {
		return new ObjectGraph(visitor);
	}

	public ObjectGraph includeTransient() {
		excludeTransient = false;
		return this;
	}

	public ObjectGraph excludeTransient() {
		excludeTransient = true;
		return this;
	}

	public ObjectGraph includeStatic() {
		excludeStatic = false;
		return this;
	}

	public ObjectGraph excludeStatic() {
		excludeStatic = true;
		return this;
	}

	/**
	 * Conducts a breath first search of the object graph
	 * @param root
	 */
	public void traverse(Object root) {
		// Reset the state
		visited.clear();
		toVisit.clear();

		if (root == null)
			return;

		addIfNotVisited(root, root.getClass());
		start();
	}

	private boolean canDecend(Class clazz) {
		// We can't decend into Primitives (they are not objects)
		return !clazz.isPrimitive();
	}

	private void addIfNotVisited(Object object, Class clazz) {
		if (object != null && !visited.containsKey(object)) {
			toVisit.add(object);
			visited.put(object, clazz);
		}
	}

	private void start() {

		while (!toVisit.isEmpty()) {

			Object obj = toVisit.remove();
			Class clazz = visited.get(obj);

			boolean terminate = visitor.visit(obj, clazz);
			if (terminate)
				return;

			if (!canDecend(clazz))
				continue;

			if (clazz.isArray()) {
				// If an Array, add each element to follow up
				Class arrayType = clazz.getComponentType();

				final int len = Array.getLength(obj);

				for (int i = 0; i < len; i++) {
					addIfNotVisited(Array.get(obj, i), arrayType);
				}

			} else {
				// If a normal class, add each field
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					int modifiers = field.getModifiers();

					if (excludeStatic && (modifiers & Modifier.STATIC) == Modifier.STATIC)
						continue;

					if (excludeTransient && (modifiers & Modifier.TRANSIENT) == Modifier.TRANSIENT)
						continue;

					try {
						field.setAccessible(true);
						Object value = field.get(obj);
						addIfNotVisited(value, field.getType());

					} catch (IllegalAccessException e) {
						// Ignore the exception
					}
				}
			}
		}
	}
}
