package hr.fer.zemris.bf.model;

import java.util.function.UnaryOperator;

/**
 * A node that represents a unary operator. It contains the following data:
 * operator name, child of the node (i.e. value or expression that is the
 * operand of this operator) and a reference to a {@link UnaryOperator} that is
 * used to execute an operation with child of this node as operand.
 *
 * @author Alen Magdić
 *
 */
public class UnaryOperatorNode implements Node {
	/** Operator name. **/
	private String name;
	/** Node child **/
	private Node child;
	/**
	 * An operator used to execute an operation with child of this node as
	 * operand
	 **/
	private UnaryOperator<Boolean> operator;

	/**
	 * Constructor.
	 *
	 * @param name
	 *            operator name
	 * @param child
	 *            node child
	 * @param operator
	 *            operator used to execute an operation with child of this node
	 *            as operand
	 */
	public UnaryOperatorNode(String name, Node child, UnaryOperator<Boolean> operator) {
		if (name == null || child == null || operator == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		}
		this.name = name;
		this.child = child;
		this.operator = operator;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Gets operator name.
	 *
	 * @return operator name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets node children.
	 *
	 * @return node children
	 */
	public Node getChild() {
		return child;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public UnaryOperator<Boolean> getOperator() {
		return operator;
	}

}
