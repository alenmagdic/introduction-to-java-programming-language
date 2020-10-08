package hr.fer.zemris.bf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * A node that represents a binary operator. It contains the following data:
 * operator name, children of the node (i.e. values or expressions that are the
 * operands of this operator) and a reference to a {@link BinaryOperator} that
 * is used to execute an operation with children of this node as operands.
 *
 * @author Alen Magdić
 *
 */
public class BinaryOperatorNode implements Node {
	/** Operator name **/
	private String name;
	/** Node children **/
	private List<Node> children;
	/**
	 * An operator used to execute an operation with children of this node as
	 * operands
	 **/
	private BinaryOperator<Boolean> operator;

	/**
	 * Constructor.
	 *
	 * @param name
	 *            operator name
	 * @param children
	 *            node children
	 * @param operator
	 *            operator used to execute an operation with children of this
	 *            node as operands
	 */
	public BinaryOperatorNode(String name, List<Node> children, BinaryOperator<Boolean> operator) {
		if (name == null || children == null || operator == null) {
			throw new IllegalArgumentException("Null is not a legal argument.");
		} else if (children.size() < 2) {
			throw new IllegalArgumentException(
					"Number of children can not be less then 2. Number of children given: " + children.size());
		}

		this.name = name;
		this.children = new ArrayList<>(children);
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
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public BinaryOperator<Boolean> getOperator() {
		return operator;
	}

}
