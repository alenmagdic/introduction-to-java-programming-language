package hr.fer.zemris.bf.model;

/**
 * A tree node.
 *
 * @author Alen Magdić
 *
 */
public interface Node {
	/**
	 * Calls an appropriate visit method of the specified visitor.
	 *
	 * @param visitor
	 *            a visitor that is to be called
	 */
	void accept(NodeVisitor visitor);

}
