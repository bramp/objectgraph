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

	boolean excludeTransient = true;
	boolean excludeStatic = true;

	// TODO Consider added a excludedClasses setting. We won't decend into these classes
	//Set<Class> excludedClasses;

	public interface Visitor {
		/**
		 * Called for each Object visited
		 * @param object The object being visited
		 * @param clazz  The field type this object was in. This will differ from object.getClass(), when the Clazz is one of the primitive types
		 * @return return true if you wish the graph transversal to stop, otherwise it will continue.
		 */
		boolean visit(Object object, Class clazz);
	}

	ObjectGraph(Visitor visitor) {
		this.visitor = visitor;
	}

	static public ObjectGraph visitor(Visitor visitor) {
		return new ObjectGraph(visitor);
	}

	/**
	 * Include transient fields. By default they are excluded.
	 * @return
	 */
	public ObjectGraph includeTransient() {
		excludeTransient = false;
		return this;
	}

	/**
	 * Exclude transient fields. By default they are excluded
	 * @return
	 */
	public ObjectGraph excludeTransient() {
		excludeTransient = true;
		return this;
	}

	/**
	 * Include static fields. By default they are excluded.
	 * @return
	 */
	public ObjectGraph includeStatic() {
		excludeStatic = false;
		return this;
	}

	/**
	 * Exclude static fields. By default they are excluded.
	 * @return
	 */
	public ObjectGraph excludeStatic() {
		excludeStatic = true;
		return this;
	}

	/**
	 * Conducts a breath first search of the object graph
	 * @param root the object to start at.
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

	/**
	 * Is this class a type we can descend deeper into. For example, primitives do not
	 * contains fields, so we can not descend into them.
	 * @param clazz
	 * @return
	 */
	private boolean canDescend(Class clazz) {
		// We can't descend into Primitives (they are not objects)
		return !clazz.isPrimitive();
	}

	/**
	 * Add this object to be visited if it has not already been visited, or scheduled to be.
	 * @param object
	 * @param clazz
	 */
	private void addIfNotVisited(Object object, Class clazz) {
		if (object != null && !visited.containsKey(object)) {
			toVisit.add(object);
			visited.put(object, clazz);
		}
	}

	private List<Field> getAllFields(List<Field> fields, Class<?> type) {
		fields.addAll(Arrays.asList(type.getDeclaredFields()));

		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	private void start() {

		while (!toVisit.isEmpty()) {

			Object obj = toVisit.remove();
			Class clazz = visited.get(obj);

			boolean terminate = visitor.visit(obj, clazz);
			if (terminate)
				return;

			if (!canDescend(clazz))
				continue;

			if (clazz.isArray()) {
				// If an Array, add each element to follow up
				Class arrayType = clazz.getComponentType();

				final int len = Array.getLength(obj);

				for (int i = 0; i < len; i++) {
					addIfNotVisited(Array.get(obj, i), arrayType);
				}
			} else if (Collection.class.isAssignableFrom(clazz)) {
				final Collection collection = (Collection) obj;

				for (Object o : collection) {
					if (o == null) {
						continue;
					}
					addIfNotVisited(o, o.getClass());
				}

			} else {
				// If a normal class, add each field
				List<Field> fields = getAllFields(new ArrayList<>(), obj.getClass());
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