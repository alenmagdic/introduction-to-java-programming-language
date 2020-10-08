package hr.fer.zemris.bf.model;

/**
 * A node representing a constant, true or false.
 *
 * @author Alen Magdić
 *
 */
public class ConstantNode implements Node {
	/** Constant value **/
	private boolean value;

	/**
	 * Constructor.
	 *
	 * @param value
	 *            constant value
	 */
	public ConstantNode(boolean value) {
		this.value = value;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets the constant value held in this node.
	 *
	 * @return the constant value
	 */
	public boolean getValue() {
		return value;
	}
}
