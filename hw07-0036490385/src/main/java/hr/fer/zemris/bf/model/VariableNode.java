package hr.fer.zemris.bf.model;

/**
 * A node representing a variable.
 *
 * @author Alen Magdić
 *
 */
public class VariableNode implements Node {
	/** Variable name **/
	private String name;

	/**
	 * Constructor.
	 *
	 * @param name
	 *            variable name
	 */
	public VariableNode(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		this.name = name;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets the variable name.
	 *
	 * @return variable name
	 */
	public String getName() {
		return name;
	}

}
