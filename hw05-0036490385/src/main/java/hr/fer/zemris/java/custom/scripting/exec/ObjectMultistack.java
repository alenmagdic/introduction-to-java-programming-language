package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a multistack. It is actually a special kind of map,
 * that has strings as keys (i.e. stack names), and stacks as values. It
 * contains methods for pushing new elements to the specified stack, for
 * removing elements from the specified stack and for getting the last added
 * element to the specified stack. All the metods have O(1) complexity.
 *
 * @author Alen Magdić
 *
 */
public class ObjectMultistack {
	/** A map used to store keys and references to stacks. **/
	private Map<String, MultistackEntry> map;

	/**
	 * Default constructor.
	 */
	public ObjectMultistack() {
		map = new HashMap<>();
	}

	/**
	 * Pushes the specified element to the specified stack. The stack is
	 * specified by it's name while an element is given wrapped in a
	 * {@link ValueWrapper}.
	 *
	 * @param name
	 *            stack name
	 * @param valueWrapper
	 *            a wrapped element that is to be pushed to the specified stack
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if (name == null || valueWrapper == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		MultistackEntry stack = map.get(name);
		MultistackEntry newEntry = new MultistackEntry(stack, valueWrapper);
		map.put(name, newEntry);
	}

	/**
	 * Pops an element from the top of the specified stack. The stack is
	 * specified by it's name.
	 *
	 * @param name
	 *            stack name
	 * @return an element from the top of the specified stack
	 */
	public ValueWrapper pop(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		MultistackEntry stack = peekEntry(name);
		map.put(name, stack.next);
		return stack.valueWrapper;
	}

	/**
	 * Gets an element from the top of the specified stack, but it does not
	 * remove it from the stack. The stack is specified by it's name.
	 *
	 * @param name
	 *            stack name
	 * @return an element from the top of the specified stack
	 */
	public ValueWrapper peek(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}
		return peekEntry(name).valueWrapper;
	}

	/**
	 * A method that gets a {link MultistackEntry} of the last added element to
	 * the specified stack. The stack is specified by it's name.
	 *
	 * @param name
	 *            stack name
	 * @return a {link MultistackEntry} of the last added element to the
	 *         specified stack
	 */
	private MultistackEntry peekEntry(String name) {
		MultistackEntry stack = map.get(name);
		if (stack == null) {
			throw new EmptyStackException(
					"There is no any element associated with the specified key in the collection. The specified key: "
							+ name);
		}
		return stack;
	}

	/**
	 * Returns true if there are no elements on the specified stack. The stack
	 * is specified by it's name.
	 *
	 * @param name
	 *            stack name
	 * @return true if there are no elements on the specified stack
	 */
	public boolean isEmpty(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A null reference is not a legal argument for this method.");
		}

		return map.get(name) == null;
	}

	/**
	 * This class represents a node of a stack. It contains informaton such as
	 * value of the element that it represents, and a reference to the
	 * {@link MultistackEntry} of the next element on the stack.
	 *
	 * @author Alen Magdić
	 *
	 */
	private static class MultistackEntry {
		/**
		 * A reference to the next entry of the stack.
		 */
		private MultistackEntry next;
		/**
		 * An element pushed to the stack.
		 */
		private ValueWrapper valueWrapper;

		/**
		 * Constructor.
		 *
		 * @param next
		 *            an entry of the next element on the stack
		 * @param valueWrapper
		 *            a wrapped value of this entry
		 */
		public MultistackEntry(MultistackEntry next, ValueWrapper valueWrapper) {
			this.next = next;
			this.valueWrapper = valueWrapper;
		}
	}

}
